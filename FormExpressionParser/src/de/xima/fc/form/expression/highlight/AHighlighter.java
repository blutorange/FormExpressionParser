package de.xima.fc.form.expression.highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Token;

public abstract class AHighlighter {
	private final Pattern splitter = Pattern.compile("(\\r)?\\n");
	private final IHighlightTheme theme;
	private int currentLine, currentColumn;
	public AHighlighter(IHighlightTheme theme) {
		this.theme = theme;
	}
	protected final void process(final Token[] tokenList) throws IOException {
		currentLine = currentColumn = 0;
		prepareProcessing();
		final List<Style> styleList = new ArrayList<>(40);
		final StringBuilder embed = new StringBuilder();
		int embedBeginLine = 0;
		int embedBeginColumn = 0;
		for (Token token : tokenList) {
			// Join all los chars.
			if (token.kind == FormExpressionParserConstants.LosChar) {
				if (embed.length() == 0) {
					embedBeginLine = token.beginLine;
					embedBeginColumn = token.beginColumn;
				}
				embed.append(token.image.charAt(0));
			}
			else {
				if (embed.length() > 0) {
					processToken(new Token(FormExpressionParserConstants.LosChar, embed.toString(), embedBeginLine, embedBeginColumn, token.endLine, token.endColumn), styleList);
					embed.setLength(0);
				}
				processToken(token, styleList);
			}
		}
		finishProcessing();
	}
	
	private void processToken(Token token, List<Style> styleList) throws IOException {
		// Write spaces and line breaks to get to current position.
		if (token.beginLine > currentLine) {
			currentColumn = 0;
			writeNewline(token.beginLine-currentLine);
		}
		if (token.beginColumn > currentColumn )
			writeSpace(token.beginColumn-currentColumn);
		currentLine = token.beginLine;
		currentColumn = token.beginColumn;
		// Get and buffer style.
		if (token.kind >= styleList.size())
			for (int i = token.kind - styleList.size()+1; i --> 0;)
				styleList.add(null);
		Style style = styleList.get(token.kind);
		if (style == null) {
			style = theme.getStyleForToken(token.kind);
			if (style == null) style = new Style();
			styleList.set(token.kind, style);
		}
		// Get lines and keep track of line and column count.
		final String[] lines = splitter.split(token.image);
		if (lines.length == 1) {
			currentColumn += lines[0].length();
		}
		else if (lines.length > 1) {
			currentLine += (lines.length-1);
			currentColumn = lines[lines.length-1].length();
		}
		// Style token.
		for (String line : lines) {
			if (!line.isEmpty()) writeStyledText(line, style);
			if (lines.length > 1) writeNewline(1);
		}
	}
	
	protected abstract void writeStyledText(String text, Style style) throws IOException;
	protected abstract void writeSpace(int numberOfSpaces) throws IOException;
	protected abstract void writeNewline(int numberOfNewlines) throws IOException;
	protected abstract void prepareProcessing() throws IOException;
	protected abstract void finishProcessing() throws IOException;
}