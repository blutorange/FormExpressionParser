package de.xima.fc.form.expression.highlight.highlighter;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.highlight.AHighlighter;
import de.xima.fc.form.expression.highlight.Color;
import de.xima.fc.form.expression.highlight.Feature;
import de.xima.fc.form.expression.highlight.IHighlightTheme;
import de.xima.fc.form.expression.highlight.Size;
import de.xima.fc.form.expression.highlight.Style;
import de.xima.fc.form.expression.highlight.Weight;

@SuppressWarnings("nls")
public class HtmlHighlighter extends AHighlighter {
	private Writer html, css;
	private Set<Color> colorSet;
	private String cssClassPrefix;
	private boolean basicStyling = true;
	private StringBuilder currentLine;
	private HtmlHighlighter(final IHighlightTheme theme) throws IllegalArgumentException {
		super(theme);
	}

	@Override
	protected void writeSpace(final int numberOfSpaces) throws IOException {
		if (currentLine == null) return;
		currentLine.append("<span>");
		for (int i = numberOfSpaces; i-->0;) currentLine.append("&nbsp;");
		currentLine.append("</span>");
	}

	@Override
	protected void writeNewline(final int numberOfNewlines) throws IOException {
		if (html == null || css == null || numberOfNewlines < 1) return;
		// Write one newline.
		if (currentLine.length() == 0) html.write("<br>");
		else {
			html.write("<p>");
			html.write(currentLine.toString());
			html.write("</p>");
		}
		// Write more new lines.
		for (int i = numberOfNewlines-1; i-->0;) html.write("<br>");
		currentLine.setLength(0);
	}

	@Override
	protected void writeStyledText(final String text, final Style style) throws IOException {
		if (currentLine == null) return;
		final String line = StringEscapeUtils.escapeHtml4(text).replaceAll(" ", "&nbsp;");
		currentLine.append("<span class=\"");
		currentLine.append(cssClassPrefix);
		currentLine.append(StringUtils.SPACE);
		styleCssClasses(style);
		currentLine.append("\">");
		currentLine.append(line);
		currentLine.append("</span>");
	}

	@Override
	protected void prepareProcessing(final Color backgroundColor) throws IOException {
		if (html == null || css == null) return;
		colorSet = new HashSet<>();
		currentLine = new StringBuilder();
		cssGeneral(backgroundColor);
		cssWeight();
		cssSize();
		cssFeature();
		html.write("<div class=\"");
		html.write(cssClassPrefix);
		html.write("\">");
	}

	@Override
	protected void finishProcessing(final Color backgroundColor) throws IOException {
		if (html == null || css == null) return;
		cssColor();
		if (currentLine.length() == 0) html.write("<br>");
		else {
			html.write("<p>");
			html.write(currentLine.toString());
			html.write("</p>");
		}
		html.write("</div>");
		html.flush();
		css.flush();
		html = null;
		css = null;
		currentLine = null;
		colorSet.clear();
		colorSet = null;
		cssClassPrefix = null;
	}

	public final void process(final Iterable<Token> tokenStream, final Writer html, final Writer css) throws IOException {
		process(tokenStream, null, true, html, css);
	}

	public final void process(final Token[] tokenArray, final String cssClassPrefix, final boolean basicStyling, final Writer html, final Writer css) throws IOException {
		this.html = html;
		this.css = css;
		this.cssClassPrefix = sanitizeCssClassPrefix(cssClassPrefix);
		this.basicStyling = basicStyling;
		super.process(tokenArray);
	}

	public final void process(final Iterable<Token> tokenStream, final String cssClassPrefix, final boolean basicStyling, final Writer html, final Writer css) throws IOException {
		this.html = html;
		this.css = css;
		this.cssClassPrefix = sanitizeCssClassPrefix(cssClassPrefix);
		this.basicStyling = basicStyling;
		super.process(tokenStream);
	}

	private void styleCssClasses(final Style style) throws IOException {
		// Color
		if (colorSet != null) colorSet.add(style.color);
		currentLine.append(cssClassPrefix);
		currentLine.append("-cl");
		currentLine.append(style.color.getHexStringRgb());
		// Weight
		if (style.weight != Weight.DEFAULT) {
			currentLine.append(StringUtils.SPACE);
			currentLine.append(cssClassPrefix);
			currentLine.append("-wt");
			currentLine.append(style.weight.name());
		}
		// Size
		if (style.size != Size.DEFAULT) {
			currentLine.append(StringUtils.SPACE);
			currentLine.append(cssClassPrefix);
			currentLine.append("-sz");
			currentLine.append(style.size.name());
		}
		// Features
		for (final Feature feature : style.featureSet) {
			currentLine.append(StringUtils.SPACE);
			currentLine.append(cssClassPrefix);
			currentLine.append("-ft");
			currentLine.append(feature.name());
		}
	}

	private void cssWeight() throws IOException {
		for (final Weight weight : Weight.values()) {
			css.write(".");
			css.write(cssClassPrefix);
			css.write("-wt");
			css.write(weight.name());
			css.write("{");
			switch (weight) {
			case BOLDEST:
				css.write("font-weight:900;");
				break;
			case BOLDER:
				css.write("font-weight:700;");
				break;
			case DEFAULT:
				//css.write("font-weight:500;");
				break;
			case LIGHTER:
				css.write("font-weight:300;");
				break;
			case LIGHTEST:
				css.write("font-weight:100;");
				break;
			default:
				throw new RuntimeException("missing case for enum: " + weight);
			}
			css.write("}");
		}
	}

	private void cssSize() throws IOException {
		for (final Size size: Size.values()) {
			css.write(".");
			css.write(cssClassPrefix);
			css.write("-sz");
			css.write(size.name());
			css.write("{font-size:");
			switch (size) {
			case LARGEST:
				css.write("larger");
				break;
			case LARGER:
				css.write("larger");
				break;
			case DEFAULT:
				//css.write("inherit");
				break;
			case SMALLER:
				css.write("smaller");
				break;
			case SMALLEST:
				css.write("smaller");
				break;
			default:
				throw new RuntimeException("missing case for enum: " + size);
			}
			css.write(";}");
		}
	}

	private void cssFeature() throws IOException {
		for (final Feature feature: Feature.values()) {
			css.write(".");
			css.write(cssClassPrefix);
			css.write("-ft");
			css.write(feature.name());
			css.write("{");
			switch (feature) {
			case CURSIVE:
				css.write("font-style:italic;");
				break;
			case STRIKETHROUGH:
				css.write("text-decoration:line-through;");
				break;
			case UNDERLINE:
				css.write("text-decoration:underline;");
				break;
			default:
				throw new RuntimeException("missing case for enum: " + feature);
			}
			css.write("}");
		}
	}

	private void cssColor() throws IOException {
		if (colorSet == null) return;
		for (final Color color: colorSet) {
			final String hexString = color.getHexStringRgb();
			css.write(".");
			css.write(cssClassPrefix);
			css.write("-cl");
			css.write(hexString);
			css.write("{");
			if (color.isFullyTransparent()) css.write("opacity:0;");
			else cssColor(css, color, "color");
			css.write("}");
		}
	}

	private static void cssColor(final Writer css, final Color color, final String attribute) throws IOException {
		css.write(attribute);
		css.write(":rgb(");
		css.write(Integer.toString(color.getByteR(), 10));
		css.write(',');
		css.write(Integer.toString(color.getByteG(), 10));
		css.write(',');
		css.write(Integer.toString(color.getByteB(), 10));
		css.write(");");
		css.write(attribute);
		css.write(":rgba(");
		css.write(Integer.toString(color.getByteR(), 10));
		css.write(',');
		css.write(Integer.toString(color.getByteG(), 10));
		css.write(',');
		css.write(Integer.toString(color.getByteB(), 10));
		css.write(',');
		css.write(Integer.toString(color.getByteA(), 10));
		css.write(");");
	}

	private void cssGeneral(final Color backgroundColor) throws IOException {
		if (!backgroundColor.isFullyTransparent()) {
			css.write("div.");
			css.write(cssClassPrefix);
			css.write('{');
			cssColor(css, backgroundColor, "background-color");
			css.write('}');
		}

		if (basicStyling) {
			css.write(".");
			css.write(cssClassPrefix);
			css.write(">p{margin:0;}");
			css.write("div.");
			css.write(cssClassPrefix);
			css.write("{font-family:monospace;white-space:nowrap;overflow-x: scroll;}");
		}
	}

	public static HtmlHighlighter getFor(final IHighlightTheme theme) {
		return new HtmlHighlighter(theme);
	}

	public static String sanitizeCssClassPrefix(String cssClassPrefix) {
		if (cssClassPrefix == null) cssClassPrefix = StringUtils.EMPTY;
		cssClassPrefix = cssClassPrefix.replaceAll("[^-a-zA-Z0-9_]", StringUtils.EMPTY);
		if (cssClassPrefix.isEmpty()) cssClassPrefix = "hglt";
		return cssClassPrefix;
	}

}