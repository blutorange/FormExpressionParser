package de.xima.fc.form.expression.highlight.highlighter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.highlight.AHighlighter;
import de.xima.fc.form.expression.highlight.Color;
import de.xima.fc.form.expression.highlight.Feature;
import de.xima.fc.form.expression.highlight.Size;
import de.xima.fc.form.expression.highlight.Style;
import de.xima.fc.form.expression.highlight.Weight;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter.HtmlHighlighterState;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
@SuppressWarnings("nls")
public class HtmlHighlighter extends AHighlighter<HtmlHighlighterState>{
	private final StringBuilder html, css;
	private final String cssClassPrefix;
	private final boolean applyBasicStyling;
	
	private HtmlHighlighter(final boolean basicStyling, @Nullable final String cssClassPrefix) throws FormExpressionException {
		super();
		this.cssClassPrefix = sanitizeCssClassPrefix(cssClassPrefix);
		this.applyBasicStyling = basicStyling;
		this.html = new StringBuilder();
		this.css = new StringBuilder();
	}

	@Override
	protected void writeSpace(final int numberOfSpaces, final HtmlHighlighterState state) throws IOException {
		state.activeLine.append("<span>");
		for (int i = numberOfSpaces; i-- > 0;)
			state.activeLine.append("&nbsp;");
		state.activeLine.append("</span>");
	}

	@Override
	protected void writeNewline(final int numberOfNewlines, final HtmlHighlighterState state) throws IOException {
		if (numberOfNewlines < 1)
			return;
		// Write one newline.
		if (state.activeLine.length() == 0)
			html.append("<br>");
		else {
			html.append("<p>");
			html.append(state.activeLine.toString());
			html.append("</p>");
		}
		// Write more new lines.
		for (int i = numberOfNewlines - 1; i-- > 0;)
			html.append("<br>");
		state.activeLine.setLength(0);
	}

	@Override
	protected void writeStyledText(final String text, final Style style, final HtmlHighlighterState state) throws IOException {
		final String line = StringEscapeUtils.escapeHtml4(text).replaceAll(" ", "&nbsp;");
		state.activeLine.append("<span class=\"");
		state.activeLine.append(cssClassPrefix);
		state.activeLine.append(StringUtils.SPACE);
		styleCssClasses(style, state);
		state.activeLine.append("\">");
		state.activeLine.append(line);
		state.activeLine.append("</span>");
	}

	@Override
	protected HtmlHighlighterState createState(final IFormExpressionHighlightTheme theme) {
		return new HtmlHighlighterState(theme);
	}
	
	@Override
	protected void prepareProcessing(final Color backgroundColor, final HtmlHighlighterState state) throws IOException {
		cssGeneral(backgroundColor);
		cssWeight();
		cssSize();
		cssFeature();
		html.append("<div class=\"");
		html.append(cssClassPrefix);
		html.append("\">");
	}

	@Override
	protected void finishProcessing(final Color backgroundColor, final HtmlHighlighterState state) throws IOException {
		cssColor(state.colorSet);
		if (state.activeLine.length() == 0)
			html.append("<br>");
		else {
			html.append("<p>");
			html.append(state.activeLine.toString());
			html.append("</p>");
		}
		html.append("</div>");
	}
	
	private void styleCssClasses(final Style style, final HtmlHighlighterState state) throws IOException {
		// Color
		state.colorSet.add(style.color);
		state.activeLine.append(cssClassPrefix);
		state.activeLine.append("-cl");
		state.activeLine.append(style.color.getHexStringRgb());
		// Weight
		if (style.weight != Weight.DEFAULT) {
			state.activeLine.append(StringUtils.SPACE);
			state.activeLine.append(cssClassPrefix);
			state.activeLine.append("-wt");
			state.activeLine.append(style.weight.name());
		}
		// Size
		if (style.size != Size.DEFAULT) {
			state.activeLine.append(StringUtils.SPACE);
			state.activeLine.append(cssClassPrefix);
			state.activeLine.append("-sz");
			state.activeLine.append(style.size.name());
		}
		// Features
		for (final Feature feature : style.featureSet) {
			state.activeLine.append(StringUtils.SPACE);
			state.activeLine.append(cssClassPrefix);
			state.activeLine.append("-ft");
			state.activeLine.append(feature.name());
		}
	}

	private void cssWeight() throws IOException {
		for (final Weight weight : Weight.values()) {
			css.append(".");
			css.append(cssClassPrefix);
			css.append("-wt");
			css.append(weight.name());
			css.append("{");
			switch (weight) {
			case BOLDEST:
				css.append("font-weight:900;");
				break;
			case BOLDER:
				css.append("font-weight:700;");
				break;
			case DEFAULT:
				// css.append("font-weight:500;");
				break;
			case LIGHTER:
				css.append("font-weight:300;");
				break;
			case LIGHTEST:
				css.append("font-weight:100;");
				break;
			default:
				throw new FormExpressionException("missing case for enum: " + weight);
			}
			css.append("}");
		}
	}

	private void cssSize() throws IOException {
		for (final Size size : Size.values()) {
			css.append(".");
			css.append(cssClassPrefix);
			css.append("-sz");
			css.append(size.name());
			css.append("{font-size:");
			switch (size) {
			case LARGEST:
				css.append("larger");
				break;
			case LARGER:
				css.append("larger");
				break;
			case DEFAULT:
				// css.append("inherit");
				break;
			case SMALLER:
				css.append("smaller");
				break;
			case SMALLEST:
				css.append("smaller");
				break;
			default:
				throw new FormExpressionException("missing case for enum: " + size);
			}
			css.append(";}");
		}
	}

	private void cssFeature() throws IOException {
		for (final Feature feature : Feature.values()) {
			css.append(".");
			css.append(cssClassPrefix);
			css.append("-ft");
			css.append(feature.name());
			css.append("{");
			switch (feature) {
			case CURSIVE:
				css.append("font-style:italic;");
				break;
			case STRIKETHROUGH:
				css.append("text-decoration:line-through;");
				break;
			case UNDERLINE:
				css.append("text-decoration:underline;");
				break;
			default:
				throw new FormExpressionException("missing case for enum: " + feature);
			}
			css.append("}");
		}
	}

	private void cssColor(final Set<Color> colorSet) throws IOException {
		for (final Color color : colorSet) {
			final String hexString = color.getHexStringRgb();
			css.append(".");
			css.append(cssClassPrefix);
			css.append("-cl");
			css.append(hexString);
			css.append("{");
			if (color.isFullyTransparent())
				css.append("opacity:0;");
			else
				cssColor(color, "color");
			css.append("}");
		}
	}

	private void cssColor(final Color color, final String attribute)
			throws IOException {
		css.append(attribute);
		css.append(":rgb(");
		css.append(Integer.toString(color.getByteR(), 10));
		css.append(',');
		css.append(Integer.toString(color.getByteG(), 10));
		css.append(',');
		css.append(Integer.toString(color.getByteB(), 10));
		css.append(");");
		css.append(attribute);
		css.append(":rgba(");
		css.append(Integer.toString(color.getByteR(), 10));
		css.append(',');
		css.append(Integer.toString(color.getByteG(), 10));
		css.append(',');
		css.append(Integer.toString(color.getByteB(), 10));
		css.append(',');
		css.append(Integer.toString(color.getByteA(), 10));
		css.append(");");
	}

	private void cssGeneral(final Color backgroundColor) throws IOException {
		if (!backgroundColor.isFullyTransparent()) {
			css.append("div.");
			css.append(cssClassPrefix);
			css.append('{');
			cssColor(backgroundColor, "background-color");
			css.append('}');
		}

		if (applyBasicStyling) {
			css.append(".");
			css.append(cssClassPrefix);
			css.append(">p{margin:0;}");
			css.append("div.");
			css.append(cssClassPrefix);
			css.append("{font-family:monospace;white-space:nowrap;overflow-x: scroll;}");
		}
	}

	private static String sanitizeCssClassPrefix(final @Nullable String cssClassPrefix) {
		final String pref = cssClassPrefix == null ? CmnCnst.NonnullConstant.STRING_EMPTY : cssClassPrefix;
		String p = pref.replaceAll("[^-a-zA-Z0-9_]", CmnCnst.NonnullConstant.STRING_EMPTY);
		if (p == null)
			p = CmnCnst.NonnullConstant.STRING_EMPTY;
		if (p.isEmpty())
			p = "hglt";
		return p;
	}

	protected static class HtmlHighlighterState extends de.xima.fc.form.expression.highlight.AHighlighter.HighlighterState {
		protected final Set<Color> colorSet;
		protected  StringBuilder activeLine;
		private HtmlHighlighterState(final IFormExpressionHighlightTheme theme) {
			super(theme);
			colorSet = new HashSet<>();
			activeLine = new StringBuilder();
		}		
		@Override
		protected void clear() {
			super.clear();
			colorSet.clear();
			activeLine.setLength(0);
		}
	}
	
	public static HtmlHighlighter create() {
		return create(null, true);
	}

	public static HtmlHighlighter create(@Nullable final String cssClassPrefix, final boolean applyBasicStyling) {
		return new HtmlHighlighter(applyBasicStyling, cssClassPrefix);
	}

	public String getHtml() {
		return NullUtil.toString(html);
	}

	public String getCss() {
		return NullUtil.toString(css);
	}

	public String getHtmlWithInlinedCss() {
		return toHtmlWithInlinedCss(getHtml(), getCss(), cssClassPrefix);
	}

	private static String toHtmlWithInlinedCss(final String html, final String css, final String cssClassPrefix) {
		final StringBuilder builder = new StringBuilder();
		builder.append("<section class=\""); //$NON-NLS-1$
		builder.append(cssClassPrefix);
		builder.append("\">"); //$NON-NLS-1$
		builder.append(html);
		builder.append("<style type=\"text/css\" scoped>"); //$NON-NLS-1$
		builder.append(css);
		builder.append("</style>"); //$NON-NLS-1$
		builder.append("</section>"); //$NON-NLS-1$
		return NullUtil.toString(builder);
	}
}