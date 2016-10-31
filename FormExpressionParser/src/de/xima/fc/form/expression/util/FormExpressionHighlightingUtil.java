package de.xima.fc.form.expression.util;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.highlight.IHighlightTheme;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;

public final class FormExpressionHighlightingUtil {
	private FormExpressionHighlightingUtil(){}


	/**
	 * Highlighting utilities for programs.
	 * @author mad_gaksha
	 */
	public final static class Program {
		private Program(){}

		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @param basicStyling Whether some basic styling such as <code>font-family:monospace;</code> or <code>overflow-x:scroll;</code> should be added.
		 * @param html Writer to which to write the html.
		 * @param css Writer to which to write the css.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static void highlightHtml(final String code, final IHighlightTheme theme, final String cssClassPrefix, final boolean basicStyling, final Writer html, final Writer css) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Program.asTokenArray(code);
			final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
			highlighter.process(tokenList, cssClassPrefix, basicStyling, html, css);
		}

		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @param basicStyling Whether some basic styling such as <code>font-family:monospace;</code> or <code>overflow-x:scroll;</code> should be added.
		 * @return A string representing an HTML section tag containing the highlighted code; and a style tag with the attribute scoped set.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static String highlightHtml(final String code, final IHighlightTheme theme, final String cssClassPrefix, final boolean basicStyling) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Program.asTokenArray(code);
			return FormExpressionHighlightingUtil.highlightHtml(tokenList, theme, cssClassPrefix, basicStyling);
		}
	}

	/**
	 * Highlighting utilities for templates.
	 * @author mad_gaksha
	 */
	public final static class Template {
		private Template(){}
		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @param basicStyling Whether some basic styling such as <code>font-family:monospace;</code> or <code>overflow-x:scroll;</code> should be added.
		 * @param html Writer to which to write the html.
		 * @param css Writer to which to write the css.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static void highlightHtml(final String code, final IHighlightTheme theme, final String cssClassPrefix, final boolean basicStyling, final Writer html, final Writer css) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Template.asTokenArray(code);
			final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
			highlighter.process(tokenList, cssClassPrefix, basicStyling, html, css);
		}

		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @param basicStyling Whether some basic styling such as <code>font-family:monospace;</code> or <code>overflow-x:scroll;</code> should be added.
		 * @return A string representing an HTML section tag containing the highlighted code; and a style tag with the attribute scoped set.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static String highlightHtml(final String code, final IHighlightTheme theme, final String cssClassPrefix, final boolean basicStyling) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Template.asTokenArray(code);
			return FormExpressionHighlightingUtil.highlightHtml(tokenList, theme, cssClassPrefix, basicStyling);
		}
	}


	public static String highlightHtml(final Token[] tokenArray, final IHighlightTheme theme, String cssClassPrefix, final boolean basicStyling) throws IOException {
		cssClassPrefix = HtmlHighlighter.sanitizeCssClassPrefix(cssClassPrefix);
		final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
		try(final StringBuilderWriter html = new StringBuilderWriter();
				final StringBuilderWriter css = new StringBuilderWriter()) {
			html.write("<section class=\""); //$NON-NLS-1$
			html.write(cssClassPrefix);
			html.write("\">"); //$NON-NLS-1$
			highlighter.process(tokenArray, cssClassPrefix, basicStyling, html, css);
			html.write("<style type=\"text/css\" scoped>"); //$NON-NLS-1$
			html.write(css.toString());
			html.write("</style>"); //$NON-NLS-1$
			html.write("</section>"); //$NON-NLS-1$
			return html.toString();
		}
	}
}
