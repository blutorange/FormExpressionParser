package de.xima.fc.form.expression.grammar.html;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.impl.externalcontext.AHtmlExternalContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;

/**
 * Describes the input token stream.
 */

public class Token implements java.io.Serializable {

	/**
	 * The version identifier for this Serializable class. Increment only if the
	 * <i>serialized</i> form of the class changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An integer that describes the kind of this token. This numbering system
	 * is determined by JavaCCParser, and a table of these numbers is stored in
	 * the file ...Constants.java.
	 */
	public int kind;

	/** The line number of the first character of this Token. */
	public int beginLine;
	/** The column number of the first character of this Token. */
	public int beginColumn;
	/** The line number of the last character of this Token. */
	public int endLine;
	/** The column number of the last character of this Token. */
	public int endColumn;

	/**
	 * Used by {@link AHtmlExternalContext} etc. Tokens form a linked list, when
	 * this flag is set, the token has been removed from the linked list. Simply
	 * setting prev and next to null does not work because the list could be
	 * made up of only a single token.
	 */
	public boolean isDetached;

	/**
	 * The string image of the token.
	 */
	public String image;

	/**
	 * A reference to the next regular (non-special) token from the input
	 * stream. If this is the last token from the input stream, or if the token
	 * manager has not read tokens beyond this one, this field is set to null.
	 * This is true only if this token is also a regular token. Otherwise, see
	 * below for a description of the contents of this field.
	 */
	@Nullable
	public Token next;

	/**
	 * A reference the the previous token. <code>null</code> when there is none.
	 */
	@Nullable
	public Token prev;

	/**
	 * This field is used to access special tokens that occur prior to this
	 * token, but after the immediately preceding regular (non-special) token.
	 * If there are no such special tokens, this field is set to null. When
	 * there are more than one such special token, this field refers to the last
	 * of these special tokens, which in turn refers to the next previous
	 * special token through its specialToken field, and so on until the first
	 * special token (whose specialToken field is null). The next fields of
	 * special tokens refer to other special tokens that immediately follow it
	 * (without an intervening regular token). If there is no such token, this
	 * field is null.
	 */
	@Nullable
	public Token specialToken;

	/**
	 * An optional attribute value of the Token. Tokens which are not used as
	 * syntactic sugar will often contain meaningful values that will be used
	 * later on by the compiler or interpreter. This attribute value is often
	 * different from the image. Any subclass of Token that actually wants to
	 * return a non-null value can override this method as appropriate.
	 */
	@Nullable
	public Object getValue() {
		return null;
	}

	/**
	 * No-argument constructor
	 */
	public Token() {
		this(0,CmnCnst.EMPTY_STRING);
	}

	/**
	 * Constructs a new token for the specified Image.
	 */
	public Token(final int kind) {
		this(kind, CmnCnst.EMPTY_STRING);
	}

	/**
	 * Constructs a new token for the specified Image and Kind.
	 */
	public Token(final int kind, final String image) {
		this.kind = kind;
		this.image = image;
	}

	/**
	 * Returns the image.
	 */
	@Override
	public String toString() {
		return image;
	}

	/**
	 * Inserts the given token after this token and returns the inserted token for chaining.
	 * Uses the default literal image when there is none, or <code>null</code> otherwise.
	 * @param kind Token type.
	 * @return Inserted token.
	 */
	public Token insertToken(final int kind) {
		return insertToken(Token.newToken(kind));
	}

	/**
	 * Inserts the given token after this token and returns the inserted token for chaining.
	 * @param kind Token type.
	 * @param image Token image.
	 * @return Inserted token.
	 */
	public Token insertToken(final int kind, final String image) {
		return insertToken(Token.newToken(kind, image));
	}

	public Token insertDoubleString(final String string) {
		return insertToken(HtmlParserConstants.attvDoubleString,
				Syntax.QUOTE + StringEscapeUtils.escapeHtml4(string) + Syntax.QUOTE);
	}

	/**
	 * Inserts the given token after this token and returns the inserted token for chaining.
	 * @param token Token to insert.
	 * @return Inserted token.
	 */
	public Token insertToken(final Token token) {
		token.specialToken = null;
		token.next = this.next;
		token.prev = this;
		if (next != null) next.prev = token;
		this.next = token;
		return token;
	}

	public Token insertHtmlText(final String text) {
		return text != null ? insertToken(HtmlParserConstants.htmlText, StringEscapeUtils.escapeHtml4(text)) : this;
	}

	public Token insertClosingTag(final String tagName) {
		return tagName == null ? this : insertToken(HtmlParserConstants.tagBegin)
				.insertToken(HtmlParserConstants.tagSlash)
				.insertToken(HtmlParserConstants.tagName, tagName)
				.insertToken(HtmlParserConstants.tagEnd);
	}

	public Token insertOpeningTagFragment(final String tagName) {
		return tagName == null ? this
				: insertToken(HtmlParserConstants.tagBegin).insertToken(HtmlParserConstants.tagName, tagName);
	}

	public Token insertOpeningTag(final String tagName, final String... attributes) {
		if (tagName == null) return this;
		Token t = insertOpeningTagFragment(tagName);
		for (int i = 1; i < attributes.length; i += 2) {
			t = t.insertAttribute(attributes[i-1], attributes[i]);
		}
		return t.insertToken(HtmlParserConstants.tagEnd);
	}

	public Token insertAttribute(final String name, final String value) {
		if (name != null && value != null) {
			return insertToken(HtmlParserConstants.tagWs, StringUtils.SPACE)
					.insertToken(HtmlParserConstants.tagName, name)
					.insertToken(HtmlParserConstants.tagEquals)
					.insertDoubleString(value);
		}
		return this;
	}

	/**
	 * Sets the position (line and column) of this and the next tokens so that
	 * they are continous.
	 * @return This token for chaining.
	 */
	public Token rebuildPositions() {
		Token token = this;
		while ((token = token.next) != null) {
			final int deltaLine = token.endLine - token.beginLine;
			final Token tmp = token.prev;
			if (token.beginColumn <= 1) {
				if (tmp != null) token.beginLine = tmp.endLine + 1;
			}
			else {
				final int deltaColumn = token.endColumn - token.beginColumn;
				if (tmp != null) {
					token.beginColumn = tmp.endColumn + 1;
					token.beginLine = tmp.endLine;
				}
				if (deltaLine == 0)
					token.endColumn = token.beginColumn + deltaColumn;
			}
			token.endLine = token.beginLine + deltaLine;
		}
		return this;
	}

	/**
	 * Returns a new Token object, by default. However, if you want, you can
	 * create and return subclass objects based on the value of ofKind. Simply
	 * add the cases to the switch for all those special cases. For example, if
	 * you have a subclass of Token called IDToken that you want to create if
	 * ofKind is ID, simply add something like :
	 *
	 * case MyParserConstants.ID : return new IDToken(ofKind, image);
	 *
	 * to the following switch statement. Then you can cast matchedToken
	 * variable to the appropriate type and use sit in your lexical actions.
	 */
	public static Token newToken(final int ofKind, final String image) {
		final Token token = new Token(ofKind, image);
		token.beginColumn = token.beginLine = token.endLine = token.endColumn = 2;
		if (image != null && !image.isEmpty()) token.endColumn = image.length() + 1;
		return token;
	}

	/**
	 * Return a new token with its default literal image. When there
	 * is no literal image, the image is set to <code>null</code>.
	 * Tokens with regexps have not got a default literal image.
	 * @param ofKind Node type.
	 * @return The token.
	 */
	public static Token newToken(final int ofKind) {
		String image = null;
		if (ofKind >= 0 && ofKind < HtmlParserConstants.tokenImage.length) {
			final String literal = HtmlParserConstants.tokenImage[ofKind];
			if (literal.length() > 0 && literal.charAt(0) == '"' && literal.charAt(literal.length() - 1) == '"') {
				image = StringEscapeUtils.unescapeJava(literal.substring(1, literal.length() - 1));
			}
		}
		return Token.newToken(ofKind, image);
	}

}