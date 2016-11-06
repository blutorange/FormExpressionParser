package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.WriterOnlyExternalContext;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.impl.pool.FormcycleEcFactory;
import de.xima.fc.form.expression.impl.pool.GenericEcFactory;
import de.xima.fc.form.expression.impl.writer.DummyWriter;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/EvaluateServlet")
public class EvaluateServlet extends AFormExpressionServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EvaluateServlet() {
		super(CmnCnst.TIMEOUT, CmnCnst.TIMEOUT_UNIT);
	}

	@Override
	protected Callable<JSONObject> getCallable() {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final JSONObject json = new JSONObject();
				final String code = request.getParameter(CmnCnst.URL_PARAM_KEY_CODE);
				final String type = request.getParameter(CmnCnst.URL_PARAM_KEY_TYPE);
				final String context = request.getParameter(CmnCnst.URL_PARAM_KEY_CONTEXT);
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					try {
						final String res;
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
								res = FormExpressionFactory.Program.parse(code)
										.evaluate(FormcycleEcFactory.getPoolInstance(),
												new FormcycleExternalContext(DummyWriter.getInstance()))
										.toString();
							}
							else {
								res = FormExpressionFactory.Program.parse(code)
										.evaluate(GenericEcFactory.getPoolInstance(), DummyExternalContext.INSTANCE)
										.toString();
							}
						}
						else {
							try (final Writer writer = new StringBuilderWriter()) {
								if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
									FormExpressionFactory.Template.parse(code).evaluate(
											FormcycleEcFactory.getPoolInstance(), new FormcycleExternalContext(writer));
								}
								else {
									FormExpressionFactory.Template.parse(code).evaluate(
											GenericEcFactory.getPoolInstance(), new WriterOnlyExternalContext(writer));

								}
								res = writer.toString();
							}
						}
						json.put(CmnCnst.RESPONSE_TEXT, res.toString());
					}
					catch (ParseException | TokenMgrError | IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
					catch (final EvaluationException e) {
						final StringBuilder sb = new StringBuilder();
						sb.append(String.format(CmnCnst.RESPONSE_ERROR_EVALUATION_FAILED, e.getMessage()))
						.append(StringUtils.LF);
						for (final StackTraceElement el : e.getStackTrace()) {
							sb.append(el.toString()).append(StringUtils.LF);
						}
						json.put(CmnCnst.RESPONSE_ERROR, sb.toString());
					}
				}
				return json;
			}
		};
	}
}
