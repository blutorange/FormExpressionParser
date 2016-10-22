package de.xima.fc.form.expression.highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.xima.fc.form.expression.grammar.AToken;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Token;

public abstract class AHighlighter {
	private final Pattern splitter = Pattern.compile("(\\r)?\\n");
	private final IHighlightTheme theme;
	private int currentLine, currentColumn;
	private Token embedBegin, embedEnd;
	private StringBuilder embed;
	private List<Style> styleList;
	
	public AHighlighter(IHighlightTheme theme) {
		this.theme = theme;
	}
	private void prepareInternal(final Color backgroundColor) throws IOException {
		currentLine = 1;
		currentColumn = 0;
		prepareProcessing(backgroundColor);
		styleList = new ArrayList<>(40);
		embed = new StringBuilder();
		embedBegin = null;
		embedEnd = null;
	}
	private void processInternal(Token token) throws IOException {
		// Join all los chars to one string.
		if (token.kind == FormExpressionParserConstants.LosChar) {
			embedEnd = token;
			if (embed.length() == 0) embedBegin = token;
			embed.append(token.image.charAt(0));
		}
		// Process current token.
		else {
			// Process joined los token.
			if (embed.length() > 0 && embedBegin != null && embedEnd != null)
				processToken(AToken.newToken(FormExpressionParserConstants.LosChar, embed.toString(), embedBegin.beginLine,
						embedBegin.beginColumn, embedEnd.endLine, embedEnd.endColumn), styleList);
			embed.setLength(0);
			// Process comment tokens, when present.
			Token special = token;
			while (special.specialToken != null) special = special.specialToken;
			if (special != token) {
				do processToken(special, styleList);
				while((special = special.next) !=null);
			}
			// Process current token.
			processToken(token, styleList);
		}
	}
	private void finishInternal() throws IOException {
		// Process joined los token.
		if (embed.length() > 0 && embedBegin != null && embedEnd != null)
			processToken(AToken.newToken(FormExpressionParserConstants.LosChar, embed.toString(), embedBegin.beginLine,
					embedBegin.beginColumn, embedEnd.endLine, embedEnd.endColumn), styleList);
		embed.setLength(0);
		styleList.clear();
		embed = null;
		styleList = null;
	}
	
	protected final void process(final Iterable<Token> tokenStream) throws IOException {
		final Color backgroundColor = theme.getColorForBackground();
		prepareInternal(backgroundColor);
		for (Token token : tokenStream)
			processInternal(token);
		finishInternal();
		finishProcessing(backgroundColor);
	}
	
	protected final void process(final Token[] tokenArray) throws IOException {
		final Color backgroundColor = theme.getColorForBackground();
		prepareInternal(backgroundColor);
		for (Token token : tokenArray)
			processInternal(token);
		finishInternal();
		finishProcessing(backgroundColor);
	}
	
	private void processToken(Token token, List<Style> styleList) throws IOException {
		// Write spaces and line breaks to get to current position.
		if (token.beginLine > currentLine) {
			currentColumn = 0;
			writeNewline(token.beginLine - currentLine);
		}
		if (token.beginColumn > currentColumn + 1)
			writeSpace(token.beginColumn-currentColumn - 1);
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
		// Get lines and style lines.
		final String[] lines = splitter.split(token.image);
		if (lines.length > 0) writeStyledText(lines[0], style);
		for (int i=1; i<lines.length; ++i) {
			writeNewline(1);
			if (!lines[i].isEmpty()) writeStyledText(lines[i], style);
		}
		// Keep track of line and column count.
		currentColumn = token.endColumn;
		currentLine = token.endLine;
	}

	protected abstract void writeStyledText(final String text, final Style style) throws IOException;
	protected abstract void writeSpace(int numberOfSpaces) throws IOException;
	protected abstract void writeNewline(int numberOfNewlines) throws IOException;
	protected abstract void prepareProcessing(final Color backgroundStyle) throws IOException;
	protected abstract void finishProcessing(final Color backgroundStyle) throws IOException;
}