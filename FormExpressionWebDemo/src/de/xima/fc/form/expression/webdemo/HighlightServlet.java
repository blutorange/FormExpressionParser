package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

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
	protected Callable<JSONObject> getCallable() {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final String code = request.getParameter("code");
				final String cssClassPrefix = request.getParameter("prefix");
				final String type = request.getParameter("type");
				final JSONObject json = new JSONObject();
				if (code == null) {
					json.put("error", "Parameter code must be given.");
				}
				else {
					try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
						if ("program".equalsIgnoreCase(type)) {
							FormExpressionHighlightingUtil.Program.highlightHtml(code,
									HighlightThemeEclipse.getInstance(), cssClassPrefix, true, html, css);
						}
						else {
							FormExpressionHighlightingUtil.Template.highlightHtml(code,
									HighlightThemeEclipse.getInstance(), cssClassPrefix, true, html, css);
						}
						json.put("html", html.toString());
						json.put("css", css.toString());

					}
					catch (ParseException | TokenMgrError | IOException e) {
						json.put("error", "Could not parse code: " + e.getMessage());
					}
				}
				return json;
			}

		};
	}

}
