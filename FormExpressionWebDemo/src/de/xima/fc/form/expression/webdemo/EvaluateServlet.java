package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationResult;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractFormcycle;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractWriter;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
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
	protected Callable<JSONObject> getCallable(final HttpServletRequest request) {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final JSONObject json = new JSONObject();
				final String code = getCode(request);
				final String type = getType(request);
				final String context = getContext(request);
				final ISeverityConfig config = getSeverityConfig(request);
				final Offset offset = getOffset(request);
				final JSONArray lint = new JSONArray();
				json.put(CmnCnst.RESPONSE_LINT, lint);
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					try {
						final IEvaluationResult res;
						final String output;
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							try (final Writer writer = new StringBuilderWriter()) {
								if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
									res = FormExpressionFactory.forProgram()
											.compile(code, EEvaluationContextContractFormcycle.INSTANCE, config)
											.evaluate(new Formcycle(writer));
								}
								else {
									res = FormExpressionFactory.forProgram()
											.compile(code, EEvaluationContextContractWriter.INSTANCE, config)
											.evaluate(writer);
								}
								output = writer.toString();
							}
						}
						else {
							try (final Writer writer = new StringBuilderWriter()) {
								if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
									res = FormExpressionFactory.forTemplate()
											.compile(code, EEvaluationContextContractFormcycle.INSTANCE, config)
											.evaluate(new Formcycle(writer));
								}
								else {
									res = FormExpressionFactory.forTemplate()
											.compile(code, EEvaluationContextContractWriter.INSTANCE, config)
											.evaluate(writer);

								}
								output = writer.toString();
							}
						}
						addWarning(res.getWarnings(), lint, offset);
						json.put(CmnCnst.RESPONSE_RESULT_OBJECT, res.getObject().toString());
						json.put(CmnCnst.RESPONSE_RESULT_OUTPUT, output);
					}
					catch (final ParseException | TokenMgrError e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
						addError(e, lint, offset);
					}
					catch (final IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
					catch (final EvaluationException e) {
						addError(e, lint, offset);
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
