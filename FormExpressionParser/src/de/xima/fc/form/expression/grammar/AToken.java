package de.xima.fc.form.expression.grammar;

public class AToken implements java.io.Serializable {
    public static Token newToken(int kind, String image, int beginLine, int beginColumn, int endLine, int endColumn) {
    	final Token token = new Token(kind, image);
    	token.beginColumn = beginColumn;
    	token.beginLine = beginLine;
    	token.endColumn = endColumn;
    	token.endLine = endLine;
    	return token;
	}

}