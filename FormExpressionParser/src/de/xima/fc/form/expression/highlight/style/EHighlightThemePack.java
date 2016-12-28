package de.xima.fc.form.expression.highlight.style;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.highlight.ABasicHighlightTheme;
import de.xima.fc.form.expression.highlight.Color;
import de.xima.fc.form.expression.highlight.Style;
import de.xima.fc.form.expression.highlight.Weight;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;

@NonNullByDefault
public enum EHighlightThemePack implements IFormExpressionHighlightTheme {
	ECLIPSE(new EclipseImpl());
	private final IFormExpressionHighlightTheme impl;

	private EHighlightThemePack(final IFormExpressionHighlightTheme impl) {
		this.impl = impl;
	}

	@Immutable
	private static final class EclipseImpl extends ABasicHighlightTheme {
		protected EclipseImpl() {
		}

		@Override
		protected Style getStyleKeyword() {
			return new Style(new Color(0x7F0055FF), Weight.BOLDER);
		}

		@Override
		protected Style getStyleBooleanLiteral() {
			return new Style(new Color(0x7F0055FF), Weight.BOLDER);
		}

		@Override
		protected Style getStyleNullLiteral() {
			return new Style(new Color(0x7F0055FF), Weight.BOLDER);
		}

		@Override
		protected Style getStyleNumberLiteral() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleStringLiteral() {
			return new Style(new Color(0x2A00FFFF));
		}

		@Override
		protected Style getStyleRegexLiteral() {
			return new Style(new Color(0x8A2BE2FF));
		}

		@Override
		protected Style getStyleBracket() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleBraces() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleParenthesis() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleOperator() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleOperatorEqual() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleLosBody() {
			return new Style(Color.GRAY40);
		}

		@Override
		protected Style getStyleLosSeparator() {
			return new Style(new Color(0x7F7F9FFF), Weight.BOLDER);
		}

		@Override
		protected Style getStylePunctuation() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleComment() {
			return new Style(new Color(0x3F7F5FFF));
		}

		@Override
		protected Style getStyleIdentifier() {
			return new Style(new Color(0x0000C0FF));
		}

		@Override
		protected Style getStyleAttributeIdentifier() {
			return new Style(Color.BLACK);
		}

		@Override
		protected Style getStyleLambdaLiteral() {
			return new Style(new Color(0x7F0055FF), Weight.BOLDER);
		}

		@Override
		public Color getColorForBackground() {
			return Color.TRANSPARENT_WHITE;
		}

		@Override
		protected Style getStyleType() {
			return new Style(Color.BLACK);
		}
	}

	@Nullable
	@Override
	public Style getStyleForToken(final int tokenType) {
		return impl.getStyleForToken(tokenType);
	}

	@Override
	public Color getColorForBackground() {
		return impl.getColorForBackground();
	}
}