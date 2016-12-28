package de.xima.fc.form.expression.highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.highlight.AHighlighter.HighlighterState;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.iface.IFormExpressionHighlighter;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public abstract class AHighlighter<T extends HighlighterState> implements IFormExpressionHighlighter {
	private final static Pattern SPLITTER = NullUtil.checkNotNull(Pattern.compile("(\\r)?\\n")); //$NON-NLS-1$

	public AHighlighter() {
	}

	private final void processInternal(final Token token, final T state) throws IOException {
		// Join all los chars to one string.
		if (token.kind == FormExpressionParserConstants.LosChar) {
			final String img = token.image;
			state.embedEnd = token;
			if (state.builder.length() == 0) state.embedBegin = token;
			if (img != null && img.length() > 0)
				state.builder.append(img.charAt(0));
		}
		// Process current token.
		else {
			// Process joined los token.
			final Token embedBegin = state.embedBegin;
			final Token embedEnd = state.embedEnd;
			if (state.builder.length() > 0 && embedBegin != null && embedEnd != null)
				processToken(Token.newToken(FormExpressionParserConstants.LosChar, state.builder.toString(), embedBegin.beginLine,
						embedBegin.beginColumn, embedEnd.endLine, embedEnd.endColumn), state);
			state.builder.setLength(0);
			// Process comment tokens, when present.
			Token special = token;
			while (special.specialToken != null) special = special.specialToken;
			if (special != token) {
				do processToken(special, state);
				while((special = special.next) !=null);
			}
			// Process current token.
			processToken(token, state);
		}
	}

	private final void finishInternal(final T state) throws IOException {
		// Process joined los token.
		final Token embedBegin = state.embedBegin;
		final Token embedEnd = state.embedEnd;
		if (state.builder.length() > 0 && embedBegin != null && embedEnd != null) {
			processToken(Token.newToken(FormExpressionParserConstants.LosChar, state.builder.toString(), embedBegin.beginLine,
					embedBegin.beginColumn, embedEnd.endLine, embedEnd.endColumn), state);
		}
	}

	@Override
	public final void process(final Iterator<Token> tokenStream, final IFormExpressionHighlightTheme theme) throws IOException {
		final Color backgroundColor = theme.getColorForBackground();
		final T state = createState(theme);
		prepareProcessing(backgroundColor, state);
		Token token;
		while(tokenStream.hasNext())
			if ((token = tokenStream.next()) != null)
				processInternal(token, state);
		finishInternal(state);
		finishProcessing(backgroundColor, state);
		state.clear();
	}

	private final void processToken(final Token token, final T state) throws IOException {
		// Write spaces and line breaks to get to current position.
		if (token.beginLine > state.currentLine) {
			state.currentColumn = 0;
			writeNewline(token.beginLine - state.currentLine, state);
		}
		if (token.beginColumn > state.currentColumn + 1)
			writeSpace(token.beginColumn-state.currentColumn - 1, state);
		state.currentLine = token.beginLine;
		state.currentColumn = token.beginColumn;
		// Get and buffer style.
		if (token.kind >= state.styleList.size())
			for (int i = token.kind - state.styleList.size()+1; i --> 0;)
				state.styleList.add(null);
		Style style = state.styleList.get(token.kind);
		if (style == null) {
			style = state.theme.getStyleForToken(token.kind);
			if (style == null) style = new Style();
			state.styleList.set(token.kind, style);
		}
		// Get lines and style lines.
		final String[] lines = SPLITTER.split(token.image);
		if (lines.length > 0) writeStyledText(NullUtil.toString(lines[0]), style, state);
		for (int i=1; i<lines.length; ++i) {
			writeNewline(1, state);
			if (!lines[i].isEmpty()) writeStyledText(NullUtil.toString(lines[i]), style, state);
		}
		// Keep track of line and column count.
		state.currentColumn = token.endColumn;
		state.currentLine = token.endLine;
	}

	protected abstract T createState(IFormExpressionHighlightTheme theme);
	protected abstract void writeStyledText(final String text, final Style style, final T state) throws IOException;
	protected abstract void writeSpace(int numberOfSpaces, final T state) throws IOException;
	protected abstract void writeNewline(int numberOfNewlines, final T state) throws IOException;
	protected abstract void prepareProcessing(final Color backgroundStyle, final T state) throws IOException;
	protected abstract void finishProcessing(final Color backgroundStyle, final T state) throws IOException;

	public static class HighlighterState {
		protected int currentLine, currentColumn;
		@Nullable
		protected Token embedBegin, embedEnd;
		protected final List<Style> styleList;
		protected final StringBuilder builder;
		protected final IFormExpressionHighlightTheme theme;
		protected HighlighterState(final IFormExpressionHighlightTheme theme) {
			this.theme = theme;
			currentLine = 1;
			currentColumn = 0;
			styleList = new ArrayList<>(40);
			builder = new StringBuilder();
			embedBegin = null;
			embedEnd = null;
		}
		@OverridingMethodsMustInvokeSuper
		protected void clear() {
			builder.setLength(0);
			styleList.clear();
		}
	}
}