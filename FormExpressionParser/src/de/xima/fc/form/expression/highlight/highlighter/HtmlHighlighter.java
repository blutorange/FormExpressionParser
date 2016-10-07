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

public class HtmlHighlighter extends AHighlighter {
	private Writer html, css;
	private Set<Color> colorSet;
	private String cssClassPrefix;
	private HtmlHighlighter(IHighlightTheme theme) throws IllegalArgumentException {
		super(theme);
	}

	@Override
	protected void writeSpace(int numberOfSpaces) throws IOException {
		if (html == null || css == null) return;
		html.write("<span>");
		for (int i = numberOfSpaces; i-->0;) html.append("&nbsp;");
		html.write("</span>");
	}

	@Override
	protected void writeNewline(int numberOfNewlines) throws IOException {
		if (html == null || css == null) return;
		html.write("</p><p>");
	}
	
	@Override
	protected void writeStyledText(String text, Style style) throws IOException {
		if (html == null || css == null) return;
		html.write("<span class=\"");
		html.write(cssClassPrefix);
		html.write(StringUtils.SPACE);
		styleCssClasses(style, html);
		html.write("\">");
		html.write(StringEscapeUtils.escapeHtml4(text).replaceAll(" ", "&nbsp;"));
		html.write("</span>");
	}
	
	@Override
	protected void prepareProcessing() throws IOException {
		if (html == null || css == null) return;
		colorSet = new HashSet<>();
		cssGeneral();
		cssWeight();
		cssSize();
		cssFeature();
		html.write("<div class=\"");
		html.write(cssClassPrefix);
		html.write("\"><p>");
	}
	
	@Override
	protected void finishProcessing() throws IOException {
		if (html == null || css == null) return;
		cssColor();
		html.write("</p></div>");
		html.flush();
		css.flush();
		html = null;
		css = null;
		colorSet.clear();
		colorSet = null;
		cssClassPrefix = null;
	}
	
	public final void process(final Token[] tokenList, String cssClassPrefix, Writer html, Writer css) throws IOException {
		this.html = html;
		this.css = css;
		this.cssClassPrefix = sanitizeCssClassPrefix(cssClassPrefix);
		super.process(tokenList);
	}
	
	private void styleCssClasses(Style style, Writer writer) throws IOException {
		// Color
		if (colorSet != null) colorSet.add(style.color);
		writer.write(cssClassPrefix);
		writer.write("-cl");
		writer.write(style.color.getHexStringRgb());
		// Weight
		writer.write(StringUtils.SPACE);
		writer.write(cssClassPrefix);
		writer.write("-wt");
		writer.write(style.weight.name());
		// Size
		writer.write(StringUtils.SPACE);
		writer.write(cssClassPrefix);
		writer.write("-sz");
		writer.write(style.size.name());
		// Features
		for (Feature feature : style.featureSet) {
			writer.write(StringUtils.SPACE);
			writer.write(cssClassPrefix);
			writer.write("-ft");
			writer.write(feature.name());
		}
	}
	
	private void cssWeight() throws IOException {
		for (Weight weight : Weight.values()) {
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
			case NORMAL:
				css.write("font-weight:500;");
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
		for (Size size: Size.values()) {
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
			case NORMAL:
				css.write("inherit");
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
		for (Feature feature: Feature.values()) {
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
		for (Color color: colorSet) {
			final String hexString = color.getHexStringRgb();
			css.write(".");
			css.write(cssClassPrefix);
			css.write("-cl");
			css.write(hexString);
			css.write("{");
			css.write("color:#");
			css.write(hexString);
			css.write(";}");
		}
	}
	
	private void cssGeneral() throws IOException {
			css.write(".");
			css.write(cssClassPrefix);
			css.write(">p{margin:0;}");
			css.write(".");
			css.write(cssClassPrefix);
			css.write("{font-family:monospace;}");
	}
	
	public static HtmlHighlighter getFor(IHighlightTheme theme) {
		return new HtmlHighlighter(theme);
	}
	
	public static String sanitizeCssClassPrefix(String cssClassPrefix) {
		if (cssClassPrefix == null) cssClassPrefix = StringUtils.EMPTY;
		cssClassPrefix = cssClassPrefix.replaceAll("[^-a-zA-Z0-9_]", StringUtils.EMPTY);
		if (cssClassPrefix.isEmpty()) cssClassPrefix = "hglt";
		return cssClassPrefix;
	}

}