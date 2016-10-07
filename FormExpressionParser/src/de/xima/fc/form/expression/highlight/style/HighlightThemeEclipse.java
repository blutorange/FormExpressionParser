package de.xima.fc.form.expression.highlight.style;

import de.xima.fc.form.expression.highlight.ABasicHighlightTheme;
import de.xima.fc.form.expression.highlight.Color;
import de.xima.fc.form.expression.highlight.IHighlightTheme;
import de.xima.fc.form.expression.highlight.Style;
import de.xima.fc.form.expression.highlight.Weight;

public class HighlightThemeEclipse extends ABasicHighlightTheme {	
	private final static class InstanceHolder {
		public final static HighlightThemeEclipse INSTANCE = new HighlightThemeEclipse();
	}
	private HighlightThemeEclipse(){}
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
	protected Style getStyleDotColonComma() {
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
	public Style getStyleForBackground() {
		return new Style(Color.WHITE);
	}
	public static IHighlightTheme getInstance() {
		return InstanceHolder.INSTANCE;
	}

}