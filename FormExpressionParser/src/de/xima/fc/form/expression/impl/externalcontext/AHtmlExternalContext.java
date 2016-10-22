package de.xima.fc.form.expression.impl.externalcontext;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.IExternalContextCommand;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;
import de.xima.fc.form.expression.exception.InvalidTemplateDataException;
import de.xima.fc.form.expression.grammar.html.HtmlParserConstants;
import de.xima.fc.form.expression.grammar.html.HtmlParserTokenManager;
import de.xima.fc.form.expression.grammar.html.SimpleCharStream;
import de.xima.fc.form.expression.grammar.html.Token;
import de.xima.fc.form.expression.grammar.html.TokenMgrError;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand;
import de.xima.fc.form.expression.impl.contextcommand.DocumentCommand.PositionedDocumentCommand;

public abstract class AHtmlExternalContext implements IExternalContext {
	private StringBuilder builder;
	private List<PositionedDocumentCommand> docCommandList;
	private int priority = 0;

	@Override
	public final void beginWriting() throws EmbedmentOutputException {
		builder = new StringBuilder();
	}

	@Override
	public final void write(String data) throws EmbedmentOutputException, InvalidTemplateDataException {
		builder.append(data);
	}

	@Override
	public final void finishWriting() throws EmbedmentOutputException, InvalidTemplateDataException {
		final String html = builder.toString();
		if (docCommandList != null) {
			// Parse HTML stream.
			Token token;
			try {
				token = tokenize(html);
			} catch (TokenMgrError e) {
				throw new InvalidTemplateDataException("", e);
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
	public void process(IExternalContextCommand command, IEvaluationContext ec) {
		if (docCommandList == null)
			docCommandList = new ArrayList<>();
		final DocumentCommand docCommand = command.castOrNull(DocumentCommand.class);
		if (docCommand != null)
			docCommandList.add(new PositionedDocumentCommand(docCommand, builder.length(), ++priority));
		else
			ec.getLogger().info(String.format("Command %s cannot be processed by AHtmlExternalContext.", command));
	}

	private Token tokenize(String html) throws TokenMgrError {
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
	private void consumeStream(HtmlParserTokenManager tm) {
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
			switch (pdc.command.getType()) {
			case REMOVE_ENCLOSING_TAG:
				initial = removeEnclosing(pdc, initial, pdc.command.getData());
				break;
			case REMOVE_NEXT_TAG:
				initial = removeNext(pdc, initial ,pdc.command.getData());
				break;
			case REMOVE_PREVIOUS_TAG:
				initial = removePrevious(pdc, initial, pdc.command.getData());
				break;
			case INSERT_LINK:
				if (pdc.token != null && !pdc.token.isDetached)
					insertA(pdc.token, pdc.command.getData(), pdc.command.getData(), null);
				break;
			default:
				break;
			}
		}
		return initial;
	}

	/**
	 * Creates a new <code>a</code> tag.
	 * @param href The link May be <code>null</code>.
	 * @param text The link text. May be <code>null</code>.
	 * @param alt The alternative text. May be <code>null</code>.
	 * @return The initial token of kind {@link HtmlParserConstants#tagBegin}.
	 */
	private void insertA(Token token, String href, String text, String alt) {
		token = token.insertToken(HtmlParserConstants.tagBegin)
			.insertToken(HtmlParserConstants.tagName, "a");
		if (href != null) {
			token = token.insertToken(HtmlParserConstants.tagWs, StringUtils.SPACE)
				.insertToken(HtmlParserConstants.tagName, "href")
				.insertToken(HtmlParserConstants.tagEquals)
				.insertToken(HtmlParserConstants.attvDoubleString, "\"" + StringEscapeUtils.escapeHtml4(href) + "\"");
		}
		if (alt != null) {
			token = token.insertToken(HtmlParserConstants.tagWs, StringUtils.SPACE)
				.insertToken(HtmlParserConstants.tagName, "alt")
				.insertToken(HtmlParserConstants.tagEquals)
				.insertToken(HtmlParserConstants.attvDoubleString, "\"" + StringEscapeUtils.escapeHtml4(alt) + "\"");
		}
		token = token.insertToken(HtmlParserConstants.tagEnd);
		if (text != null)
			token = token.insertToken(HtmlParserConstants.htmlText, StringEscapeUtils.escapeHtml4(text));
		token = token.insertToken(HtmlParserConstants.tagBegin)
			.insertToken(HtmlParserConstants.tagSlash)
			.insertToken(HtmlParserConstants.tagName, "a")
			.insertToken(HtmlParserConstants.tagEnd);
	}

	private Token removePrevious(final PositionedDocumentCommand pdc, Token initial, final String tagName) {
		return (pdc.token != null && !pdc.token.isDetached) ? removePrevious(initial, pdc.token, tagName) : initial;
	}

	private Token removeEnclosing(final PositionedDocumentCommand pdc, Token initial, final String tagName) {
		return (pdc.token != null && !pdc.token.isDetached) ? removeEnclosing(initial, pdc.token, tagName) : initial;
	}

	private Token removeNext(final PositionedDocumentCommand pdc, Token initial, final String tagName) {
		return (pdc.token != null && !pdc.token.isDetached) ? removeNext(initial, pdc.token, tagName) : initial;
	}

	private Token removePrevious(Token initial, Token token, String tagName) {
		final Token beg = seekToPrevOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		Token end = seekToEnclosingClosingTag(beg, tagName);
		if (end == null)
			return initial;
		end = seekToEndOfTag(end);
		if (end == null)
			return initial;
		return removeTokens(initial, beg, end);
	}

	private Token removeEnclosing(Token initial, Token token, String tagName) {
		// search for opening <p>
		Token beg = seekToEnclosingOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		Token end = seekToEnclosingClosingTag(token, tagName);
		if (end == null)
			return initial;
		end = seekToEndOfTag(end);
		if (end == null)
			return initial;
		return removeTokens(initial, beg, end);
	}
	
	private Token removeNext(Token initial, Token token, String tagName) {
		final Token beg = seekToNextOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		Token end = seekToEnclosingClosingTag(beg, tagName);
		if (end == null)
			return initial;
		end = seekToEndOfTag(end);
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
	private Token removeTokens(Token initial, Token beg, Token end) {
		Token token = beg;
		// Mark tokens as removed.
		while (token != end) {
			token.isDetached = true;
			token = token.next;
		}
		end.isDetached = true;
		// Update pointers.
		if (beg.prev != null) {
			beg.prev.next = end.next;
		}
		// When all tokens need to be removed, we return null to indicate this.
		else if (end.next == null) return null;
		if (end.next != null)
			end.next.prev = beg.prev;
		return beg.prev == null ? end.next : initial;
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
	private Token seekToEnclosingOpeningTag(Token token, String tagName) {
		int count = 0;
		while (true) {
			if (isOpeningTag(token, tagName)) {
				if (count == 0)
					return token;
				--count;
			}
			token = token.prev;
			if (token == null)
				return null;
			if (isClosingTag(token, tagName))
				++count;
		}
	}

	private Token seekToPrevOpeningTag(Token token, String tagName) {
		while (true) {
			if (isOpeningTag(token, tagName))
				return token;
			if ((token = token.prev) == null)
				return null;
		}
	}

	private Token seekToNextOpeningTag(Token token, String tagName) {
		while (true) {
			if (isOpeningTag(token, tagName))
				return token;
			if ((token = token.next) == null)
				return null;
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
	private Token seekToEnclosingClosingTag(Token token, String tagName) {
		int count = 0;
		while (true) {
			if (isClosingTag(token, tagName)) {
				if (count == 0)
					return token;
				--count;
			}
			token = token.next;
			if (token == null)
				return null;
			if (isOpeningTag(token, tagName))
				++count;
		}
	}

	private boolean isOpeningTag(Token token, String tagName) {
		return token.kind == HtmlParserConstants.tagBegin && token.next != null
				&& token.next.kind == HtmlParserConstants.tagName && token.next.image.equals(tagName);
	}

	private boolean isClosingTag(Token token, String tagName) {
		return token.kind == HtmlParserConstants.tagBegin && token.next != null
				&& token.next.kind == HtmlParserConstants.tagSlash && token.next.next != null
				&& token.next.next.kind == HtmlParserConstants.tagName && token.next.next.image.equals(tagName);
	}

	/**
	 * 
	 * @param token
	 *            Current token, assumed to be somewhere inside a tag.
	 * @return A token of kind {@link HtmlParserConstants#tagEnd} closing the
	 *         current tag.
	 */
	private Token seekToEndOfTag(Token token) {
		do {
			if (token.kind == HtmlParserConstants.tagEnd)
				return token;
		} while ((token = token.next) != null);
		return null;
	}

	protected abstract void output(String html) throws EmbedmentOutputException;

	protected abstract void finishOutput() throws EmbedmentOutputException;
}
