package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter;
import de.xima.fc.form.expression.highlight.style.EHighlightThemePack;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.iface.factory.IFormExpressionFactory;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/HighlightServlet")
public class HighlightServlet extends AFormExpressionServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HighlightServlet() {
		super(CmnCnst.TIMEOUT, CmnCnst.TIMEOUT_UNIT);
	}

	@Override
	protected Callable<JSONObject> getCallable(final HttpServletRequest request) {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final String code = getCode(request);
				final String cssClassPrefix = getCssClassPrefix(request);
				final String type = getType(request);
				final JSONObject json = new JSONObject();
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					final HtmlHighlighter highlighter = HtmlHighlighter.create(cssClassPrefix, true);
					final IFormExpressionHighlightTheme theme = EHighlightThemePack.ECLIPSE;
					final IFormExpressionFactory factory = CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)
							? FormExpressionFactory.forProgram() : FormExpressionFactory.forTemplate();
					try {
						factory.highlight(code, highlighter, theme);
					}
					catch (ParseException | TokenMgrError | IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
					json.put(CmnCnst.RESPONSE_HTML, highlighter.getHtml());
					json.put(CmnCnst.RESPONSE_CSS, highlighter.getCss());
				}
				return json;
			}

		};
	}
}