package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.json.simple.JSONObject;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/FormatServlet")
public class FormatServlet extends AFormExpressionServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FormatServlet() {
		super(CmnCnst.TIMEOUT, CmnCnst.TIMEOUT_UNIT);
	}

	@Override
	protected Callable<JSONObject> getCallable() {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final String code = request.getParameter("code");
				final String type = request.getParameter("type");
				final String indent = request.getParameter("indent");
				final UnparseVisitorConfig config = new UnparseVisitorConfig.Builder()
					.setLinefeed("\n")
					.setIndentPrefix(indent)
					.setOptionalSpace(1)
					.setRequiredSpace(1)
					.setKeepComments(true)
					.build();
				final JSONObject json = new JSONObject();
				if (code == null) {
					json.put("error", "Parameter code must be given.");
				}
				else {
					try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
						final Node node;
						if ("program".equalsIgnoreCase(type)) {
							node = FormExpressionFactory.Program.parse(code);
						}
						else {
							node = FormExpressionFactory.Template.parse(code);
						}
						final String format = UnparseVisitor.unparse(node, config);
						json.put("text", format);
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
