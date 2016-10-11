package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.DummyWriter;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.StringBuilderWriter;
import de.xima.fc.form.expression.impl.externalcontext.WriterOnlyExternalContext;
import de.xima.fc.form.expression.util.FormExpressionEvaluationUtil;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/EvaluateServlet")
public class EvaluateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EvaluateServlet() {
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
		final String type = request.getParameter("type");
		final String context = request.getParameter("context");
		final JSONObject json = new JSONObject();
		if (code == null) {
			response.setStatus(200, "Missing Parameter");
			json.put("error", "Parameter code must be given.");
		}
		else {
			try {
				final String res;
				if ("program".equalsIgnoreCase(type)) {
					if ("formcycle".equalsIgnoreCase(context)) {
						res = FormExpressionEvaluationUtil.Formcycle.evalProgram(code, new FormcycleExternalContext(DummyWriter.getInstance())).toString();
					}
					else {
						res = FormExpressionEvaluationUtil.Generic.evalProgram(code, DummyExternalContext.INSTANCE).toString();
					}
				}
				else {
					try (final Writer writer = new StringBuilderWriter()) {
						if ("formcycle".equalsIgnoreCase(context)) {
							FormExpressionEvaluationUtil.Formcycle.evalTemplate(code, new FormcycleExternalContext(writer));
						}
						else {
							FormExpressionEvaluationUtil.Generic.evalTemplate(code, new WriterOnlyExternalContext(writer));

						}
						res = writer.toString();
					}
				}
				json.put("text", res.toString());
			}
			catch (ParseException | TokenMgrError | IOException e) {
				response.setStatus(200, "Invalid Program");
				json.put("error", "Could not parse code: " + e.getMessage());
			}
			catch (final EvaluationException e) {
				response.setStatus(200, "Invalid Logic");
				final StringBuilder sb = new StringBuilder();
				sb.append("Could not evaluate code: ").append(e.getMessage()).append(StringUtils.LF);
				for (final StackTraceElement el : e.getStackTrace()) {
					sb.append(el.toString()).append(StringUtils.LF);
				}
				json.put("error", sb.toString());
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
