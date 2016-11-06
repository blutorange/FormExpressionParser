package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.json.simple.JSONObject;

import de.xima.fc.form.expression.context.IFormExpression;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
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
				final String code = request.getParameter(CmnCnst.URL_PARAM_KEY_CODE);
				final String type = request.getParameter(CmnCnst.URL_PARAM_KEY_TYPE);
				final String indent = request.getParameter(CmnCnst.URL_PARAM_KEY_INDENT);
				final UnparseVisitorConfig config = new UnparseVisitorConfig.Builder()
						.setLinefeed('\n')
						.setIndentPrefix(indent)
						.setOptionalSpace(1)
						.setRequiredSpace(1)
						.setKeepComments(true)
						.build();
				final JSONObject json = new JSONObject();
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
						final IFormExpression expr;
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							expr = FormExpressionFactory.Program.parse(code);
						}
						else {
							expr = FormExpressionFactory.Template.parse(code);
						}
						final String format = expr.unparse(config);
						json.put(CmnCnst.RESPONSE_TEXT, format);
					}
					catch (ParseException | TokenMgrError | IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR, String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
				}
				return json;
			}
		};
	}
}
