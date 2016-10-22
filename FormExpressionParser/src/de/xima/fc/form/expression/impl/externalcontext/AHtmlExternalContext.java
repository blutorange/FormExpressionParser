package de.xima.fc.form.expression.impl.externalcontext;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
import de.xima.fc.form.expression.impl.contextcommand.EDocumentCommandPack;
import de.xima.fc.form.expression.impl.contextcommand.EDocumentCommandPack.PositionedDocumentCommand;

public abstract class AHtmlExternalContext implements IExternalContext {

	private StringBuilder builder;
	private List<PositionedDocumentCommand> docCommandList;

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
			// Remove paragraphs etc.
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
		final EDocumentCommandPack docCommand = command.castOrNull(EDocumentCommandPack.class);
		if (docCommand != null)
			docCommandList.add(new PositionedDocumentCommand(docCommand, builder.length()));
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
			switch (pdc.command) {
			case REMOVE_ENCLOSING_PARAGRAPH:
				initial = removeEnclosing(pdc, initial, "p");
				break;
			case REMOVE_ENCLOSING_TABLE:
				initial = removeEnclosing(pdc, initial, "table");
				break;
			default:
				break;
			}
		}
		return initial;
	}

	private Token removeEnclosing(final PositionedDocumentCommand pdc, Token initial, final String tagName) {
		return (pdc.token != null && !pdc.token.isDetached) ? removeEnclosing(initial, pdc.token, tagName) : initial;
	}

	private Token removeEnclosing(Token initial, Token token, String tagName) {
		// search for opening <p>
		Token beg = seekToPrevOpeningTag(token, tagName);
		if (beg == null)
			return initial;
		// search for closing <p>
		Token end = seekToNextClosingTag(token, tagName);
		if (end == null)
			return initial;
		end = seekToEndOfTag(end);
		if (end == null)
			return initial;
		// Remove intermediate tokens
		// This loop terminates as we got to the beg and end token
		// via next and prev references from the start token. When
		// this throws a NullPointerException, there is some inconsistency
		// in the next and prev fields of some of the tokens.
		token = beg;
		while (token != end) {
			token.isDetached = true;
			token = token.next;
		}
		end.isDetached = true;
		// remove paragraph
		if (beg.prev != null) {
			beg.prev.next = end.next;
		}
		// We need to remove all of the tokens when beg is the initial
		// token and end the final token.
		else if (end.next == null) return null;
		if (end.next != null)
			end.next.prev = beg.prev;
		return initial;
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
	private Token seekToPrevOpeningTag(Token token, String tagName) {
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
	private Token seekToNextClosingTag(Token token, String tagName) {
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
