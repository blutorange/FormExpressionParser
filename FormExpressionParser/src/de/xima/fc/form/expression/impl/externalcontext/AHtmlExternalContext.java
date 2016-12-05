package de.xima.fc.form.expression.impl.externalcontext;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;

import de.xima.fc.form.expression.exception.evaluation.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.evaluation.InvalidTemplateDataException;
import de.xima.fc.form.expression.grammar.html.HtmlParserConstants;
import de.xima.fc.form.expression.grammar.html.HtmlParserTokenManager;
import de.xima.fc.form.expression.grammar.html.SimpleCharStream;
import de.xima.fc.form.expression.grammar.html.Token;
import de.xima.fc.form.expression.grammar.html.TokenMgrError;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.contextcommand.PositionedDocumentCommand;
import de.xima.fc.form.expression.util.CmnCnst;


public abstract class AHtmlExternalContext implements IExternalContext{
	private StringBuilder builder;
	private List<PositionedDocumentCommand> docCommandList;
	private int priority = 0;

	@Override
	public final void beginWriting() throws EmbedmentOutputException {
		builder = new StringBuilder();
	}

	@Override
	public final void write(final String data) throws EmbedmentOutputException, InvalidTemplateDataException {
		builder.append(data);
	}

	@Override
	public final void finishWriting() throws EmbedmentOutputException, InvalidTemplateDataException {
		@SuppressWarnings("null")
		@Nonnull final String html = builder.toString();
		if (docCommandList != null) {
			// Parse HTML stream.
			Token token;
			try {
				token = tokenize(html);
			} catch (final TokenMgrError e) {
				throw new InvalidTemplateDataException(CmnCnst.Error.INVALID_HTML_TEMPLATE, e);
			}
			// Remove tags etc.
			token = postProcess(token);
			// Output HTML stream.
			if (token != null) {
				do {
					output(token.image);
				} while ((token = token.next) != null);
			}
			docCommandList.clear();
			docCommandList = null;
		} else {
			output(html);
		}
		finishOutput();
		builder = null;
	}

	@Override
	public void process(final IExternalContextCommand command, final IEvaluationContext ec) {
		if (docCommandList == null)
			docCommandList = new ArrayList<>();
		final Optional<DocumentCommand> docCommand = command.castTo(DocumentCommand.class);
		if (docCommand.isPresent())
			docCommandList.add(new PositionedDocumentCommand(docCommand.get(), builder.length(), ++priority));
		else
			ec.getLogger().info(String.format(CmnCnst.Error.UNKNOWN_COMMAND_FOR_HTML_CONTEXT,
					command.getClass().getCanonicalName(), command));
	}

	private Token tokenize(final String html) throws TokenMgrError {
		final Token root;
		try (final StringReader reader = new StringReader(html)) {
			final SimpleCharStream s = new SimpleCharStream(reader);
			final HtmlParserTokenManager tm = new HtmlParserTokenManager(s);
			tm.setExternalCommands(docCommandList);
			root = tm.getNextToken();
			consumeStream(tm);
			tm.setExternalCommands(null);
		}
		return root;
	}

	@SuppressWarnings("all")
	private void consumeStream(final HtmlParserTokenManager tm) {
		while (tm.getNextToken().kind != HtmlParserConstants.EOF);
	}

	/**
	 * Post processes the token stream.
	 *
	 * @param token
	 *            The initial token.
	 * @return The initial token, as it might have to be changed.
	 */
	private Token postProcess(Token initial) {
		for (final PositionedDocumentCommand pdc : docCommandList) {
			checkAttachment(pdc);
			switch (pdc.command.getType()) {
			case REMOVE_ENCLOSING_TAG:
				initial = removeEnclosing(pdc, initial, pdc.command.getData()[0]);
				break;
			case REMOVE_NEXT_TAG:
				initial = removeNext(pdc, initial ,pdc.command.getData()[0]);
				break;
			case REMOVE_PREVIOUS_TAG:
				initial = removePrevious(pdc, initial, pdc.command.getData()[0]);
				break;
			case INSERT_LINK:
				final Token tmp = pdc.token;
				if (tmp != null && !tmp.isDetached)
					insertA(pdc.token, pdc.command.getData()[0], pdc.command.getData()[1], pdc.command.getData()[2]);
				break;
			case NO_OP:
				break;
			default:
				break;
			}
		}
		return initial;
	}

	/**
	 * Checks whether the token is attached. If not, seeks to the next token that is attached.
	 * When there is no such token, seeks to a previous attached token. Where there is no
	 * such token either, sets the token to <code>null</code>.
	 * @param pdc Command with token to check.
	 */
	private static void checkAttachment(final PositionedDocumentCommand pdc) {
		final Token originalToken = pdc.token;
		if (originalToken != null && originalToken.isDetached) {
			Token tmp = originalToken.next;
			while (true) {
				if (tmp == null || !tmp.isDetached)
					break;
				tmp = tmp.next;
			}
			if (tmp == null) {
				tmp = originalToken.prev;
				while (true) {
					if (tmp == null || !tmp.isDetached)
						break;
					tmp = tmp.prev;
				}
			}
			pdc.token = tmp;
		}
	}

	/**
	 * Creates a new <code>a</code> tag.
	 * @param href The link May be <code>null</code>.
	 * @param text The link text. May be <code>null</code>.
	 * @param alt The alternative text. May be <code>null</code>.
	 * @return The initial token of kind {@link HtmlParserConstants#tagBegin}.
	 */
	private static void insertA(final Token insertPosition, final String href, final String text, final String target) {
		insertPosition.insertOpeningTag(CmnCnst.Html.A, CmnCnst.Html.HREF, href, CmnCnst.Html.TARGET, target)
		.insertHtmlText(text)
		.insertClosingTag(CmnCnst.Html.A);
		insertPosition.rebuildPositions();
	}

	private static Token removePrevious(final PositionedDocumentCommand pdc, final Token initial, final String tagName) {
		final Token tmp = pdc.token;
		return (tmp != null && !tmp.isDetached) ? removePrevious(initial, tmp, tagName) : initial;
	}

	private static Token removeEnclosing(final PositionedDocumentCommand pdc, final Token initial, final String tagName) {
		final Token tmp = pdc.token;
		return (tmp != null && !tmp.isDetached) ? removeEnclosing(initial, tmp, tagName) : initial;
	}

	private static Token removeNext(final PositionedDocumentCommand pdc, final Token initial, final String tagName) {
		final Token tmp = pdc.token;
		return (tmp != null && !tmp.isDetached) ? removeNext(initial, pdc.token, tagName) : initial;
	}

	private static Token removePrevious(final Token initial, final Token token, final String tagName) {
		final Token end = seekToPrevClosingTag(token, tagName);
		if (end == null)
			return initial;
		final Token beg = seekToEnclosingOpeningTag(end.prev, tagName);
		if (beg == null)
			return initial;
		return removeTokens(initial, beg, end);
	}

	private static Token removeEnclosing(final Token initial, final Token token, final String tagName) {
		final Token beg = seekToEnclosingOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		final Token end = seekToEnclosingClosingTag(token, tagName);
		if (end == null)
			return initial;
		return removeTokens(initial, beg, end);
	}

	private static Token removeNext(final Token initial, final Token token, final String tagName) {
		final Token beg = seekToNextOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		final Token end = seekToEnclosingClosingTag(beg, tagName);
		if (end == null)
			return initial;
		return removeTokens(initial, beg, end);
	}

	/**
	 * Removes all token starting from beg, including the token end. This
	 * assumes that end can be reached via next references from the token
	 * beg. Otherwise, this throws a {@link NullPointerException}.
	 * @param beg Start token.
	 * @param end End token.
	 * @return One token before beg, or one token after end when beg is the
	 * initial token. When end is the final token as well, returns <code>null</code>.
	 */
	private static Token removeTokens(final Token initial, @Nonnull final Token beg, @Nonnull final Token end) {
		Token token = beg;
		// Mark tokens as removed.
		while (token != null && token != end) {
			token.isDetached = true;
			token = token.next;
		}
		end.isDetached = true;
		// Update pointers.
		token = beg.prev;
		if (token != null) {
			token.next = end.next;
		}
		// When all tokens need to be removed, we return null to indicate this.
		else if (end.next == null) return null;
		token = end.next;
		if (token != null)
			token.prev = beg.prev;
		return beg.prev == null ? end.next : initial.rebuildPositions();
	}

	/**
	 * Searches for the first opening tag with the given name such that the
	 * current token is inside that tag. Consider the example below. When
	 * current tag is inside the h1 element, this would return the beginning of
	 * the p tag with the id <code>outer</code>.
	 *
	 * <pre>
	 *   &lt;p id="outer"&gt;
	 *     &lt;p id="inner"&gt;&lt;/p&gt;
	 *     &lt;h1&gt;&lt;/h1&gt;
	 *   &lt;/p&gt;
	 * </pre>
	 *
	 * @param token
	 *            Token at which to begin the search.
	 * @param tagName
	 *            Name of the tag to search for.
	 * @return A token of kind {@link HtmlParserConstants#tagName}, the next tag
	 *         with the given tag name. <code>null</code> when not found.
	 */
	private static Token seekToEnclosingOpeningTag(Token token, final String tagName) {
		int count = 0;
		while (true) {
			if (token == null)
				return null;
			else if (isOpeningTag(token, tagName) && --count == -1)
				return token;
			else if (isClosingTag(token, tagName))
				++count;
			token = token.prev;
		}
	}

	@SuppressWarnings("unused")
	private static Token seekToPrevOpeningTag(Token token, final String tagName) {
		while (true) {
			if (isOpeningTag(token, tagName))
				return token;
			if ((token = token.prev) == null)
				return null;
		}
	}

	private static Token seekToPrevClosingTag(Token token, final String tagName) {
		while (true) {
			if (isClosingTag(token, tagName))
				return token;
			if ((token = token.prev) == null)
				return null;
		}
	}

	private static Token seekToNextOpeningTag(Token token, final String tagName) {
		while (true) {
			if ((token = token.next) == null)
				return null;
			if (isOpeningTag(token, tagName))
				return token;
		}
	}


	/**
	 * Searches for the first opening tag with the given name such that the
	 * current token is inside that tag. Consider the example below. When
	 * current tag is inside the h1 element, this would return the beginning of
	 * the closing p tag with the id <code>outer</code>.
	 *
	 * <pre>
	 *   &lt;p id="outer"&gt;
	 *     &lt;h1&gt;&lt;/h1&gt;
	 *     &lt;p id="inner"&gt;&lt;/p&gt;
	 *   &lt;/p&gt;
	 * </pre>
	 *
	 * @param token
	 *            Token at which to begin the search.
	 * @param tagName
	 *            Name of the tag to search for.
	 * @return A token of kind {@link HtmlParserConstants#tagName}, the next tag
	 *         with the given tag name. <code>null</code> when not found.
	 */
	private static Token seekToEnclosingClosingTag(Token token, final String tagName) {
		int count = 0;
		while (true) {
			token = token.next;
			if (token == null)
				return null;
			else if (isOpeningTag(token, tagName))
				++count;
			if (isClosingTag(token, tagName) && --count == -1)
				return token;
		}
	}

	private static boolean isOpeningTag(final Token token, final String tagName) {
		final Token tmp = token.next;
		return token.kind == HtmlParserConstants.tagBegin && tmp != null
				&& tmp.kind == HtmlParserConstants.tagName && tmp.image.equals(tagName);
	}

	private static boolean isClosingTag(final Token token, final String tagName) {
		Token t;
		// Almost always, the first check will return false, so we do not need to check all these conditions all the time.
		return token.kind == HtmlParserConstants.tagEnd && (t = token.prev) != null
				&& (t = t.kind == HtmlParserConstants.tagWs ? t.prev : t) != null
				&& t.kind == HtmlParserConstants.tagName && tagName.equals(t.image) && (t = t.prev) != null
				&& t.kind == HtmlParserConstants.tagSlash && (t = t.prev) != null
				&& t.kind == HtmlParserConstants.tagBegin;
	}

	protected abstract void output(@Nullable String html) throws EmbedmentOutputException;
	protected abstract void finishOutput() throws EmbedmentOutputException;
}
