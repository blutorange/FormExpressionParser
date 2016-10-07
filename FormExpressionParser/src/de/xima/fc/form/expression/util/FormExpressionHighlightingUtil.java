package de.xima.fc.form.expression.util;

import java.io.IOException;
import java.io.Writer;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.highlight.IHighlightTheme;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter;
import de.xima.fc.form.expression.impl.externalcontext.StringBuilderWriter;

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
		 * @param html Writer to which to write the html.
		 * @param css Writer to which to write the css.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static void highlightHtml(String code, IHighlightTheme theme, String cssClassPrefix, Writer html, Writer css) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Program.asTokenStream(code);
			final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
			highlighter.process(tokenList, cssClassPrefix, html, css);			
		}
		
		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @return A string representing an HTML section tag containing the highlighted code; and a style tag with the attribute scoped set.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static String highlightHtml(String code, IHighlightTheme theme, String cssClassPrefix) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Program.asTokenStream(code);
			return FormExpressionHighlightingUtil.highlightHtml(tokenList, theme, cssClassPrefix);
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
		 * @param html Writer to which to write the html.
		 * @param css Writer to which to write the css.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static void highlightHtml(String code, IHighlightTheme theme, String cssClassPrefix, Writer html, Writer css) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Template.asTokenStream(code);
			final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
			highlighter.process(tokenList, cssClassPrefix, html, css);			
		}
		
		/**
		 * @param code Code to highlight.
		 * @param theme Theme to use.
		 * @param cssClassPrefix Prefix for css classes. When null, some default is used. Must not be empty, all characters other than letter, number, dashes, and underscores are ignored.
		 * @return A string representing an HTML section tag containing the highlighted code; and a style tag with the attribute scoped set.
		 * @throws IOException When output could not be written to the string builder, should not happen.
		 * @throws ParseException When the code is invalid.
		 */
		public static String highlightHtml(String code, IHighlightTheme theme, String cssClassPrefix) throws IOException, ParseException {
			final Token[] tokenList = FormExpressionParsingUtil.Template.asTokenStream(code);
			return FormExpressionHighlightingUtil.highlightHtml(tokenList, theme, cssClassPrefix);
		}		
	}	
	
	
	private static String highlightHtml(Token[] tokenList, IHighlightTheme theme, String cssClassPrefix) throws IOException {
		cssClassPrefix = HtmlHighlighter.sanitizeCssClassPrefix(cssClassPrefix);
		final HtmlHighlighter highlighter = HtmlHighlighter.getFor(theme);
		try(final StringBuilderWriter html = new StringBuilderWriter();
			final StringBuilderWriter css = new StringBuilderWriter()) {
			html.write("<section class=\"");
			html.write(cssClassPrefix);
			html.write("\">");
			highlighter.process(tokenList, cssClassPrefix, html, css);
			html.write("<style type=\"text/css\" scoped>");
			html.write(css.toString());
			html.write("</style>");
			html.write("</section>");
			return html.toString();
		}
	}
}