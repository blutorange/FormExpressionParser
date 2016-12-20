package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.style.HighlightThemeEclipse;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
import de.xima.fc.form.expression.util.FormExpressionHighlightingUtil;

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
					try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							FormExpressionHighlightingUtil.Program.highlightHtml(code,
									HighlightThemeEclipse.getInstance(), cssClassPrefix, true, html, css);
						}
						else {
							FormExpressionHighlightingUtil.Template.highlightHtml(code,
									HighlightThemeEclipse.getInstance(), cssClassPrefix, true, html, css);
						}
						json.put(CmnCnst.RESPONSE_HTML, html.toString());
						json.put(CmnCnst.RESPONSE_CSS, css.toString());

					}
					catch (ParseException | TokenMgrError | IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
				}
				return json;
			}

		};
	}

}
