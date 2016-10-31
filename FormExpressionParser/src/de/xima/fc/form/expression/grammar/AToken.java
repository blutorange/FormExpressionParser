package de.xima.fc.form.expression.grammar;

public class AToken implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static Token newToken(final int kind, final String image, final int beginLine, final int beginColumn, final int endLine, final int endColumn) {
		final Token token = new Token(kind, image);
		token.beginColumn = beginColumn;
		token.beginLine = beginLine;
		token.endColumn = endColumn;
		token.endLine = endLine;
		return token;
	}

}