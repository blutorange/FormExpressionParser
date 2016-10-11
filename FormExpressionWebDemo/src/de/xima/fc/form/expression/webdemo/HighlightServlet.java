package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.style.HighlightThemeEclipse;
import de.xima.fc.form.expression.impl.externalcontext.StringBuilderWriter;
import de.xima.fc.form.expression.util.FormExpressionHighlightingUtil;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/HighlightServlet")
public class HighlightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HighlightServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final String code = request.getParameter("code");
		final String cssClassPrefix = request.getParameter("prefix");
		final String type = request.getParameter("type");
		final JSONObject json = new JSONObject();
		if (code == null) {
			response.setStatus(200, "Missing Parameter");
			json.put("error", "Parameter code must be given.");
		}
		else {
			try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
				if ("program".equalsIgnoreCase(type)) {
					FormExpressionHighlightingUtil.Program.highlightHtml(code, HighlightThemeEclipse.getInstance(),
							cssClassPrefix, true, html, css);
				}
				else {
					FormExpressionHighlightingUtil.Template.highlightHtml(code, HighlightThemeEclipse.getInstance(),
							cssClassPrefix, true, html, css);
				}
				json.put("html", html.toString());
				json.put("css", css.toString());

			}
			catch (ParseException | TokenMgrError | IOException e) {
				response.setStatus(200, "Invalid Program");
				json.put("error", "Could not parse code: " + e.getMessage());
			}
		}
		response.getWriter().append(json.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
