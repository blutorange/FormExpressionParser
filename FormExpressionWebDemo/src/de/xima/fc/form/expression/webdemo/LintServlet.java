package de.xima.fc.form.expression.webdemo;

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
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractFormcycle;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractVoid;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.util.Void;

/**
 * Servlet implementation class HighlightServlet
 */
@WebServlet("/LintServlet")
public class LintServlet extends AFormExpressionServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LintServlet() {
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
				final Offset offset = getOffset(request);
				final ISeverityConfig config = getSeverityConfig(request);
				final JSONArray lint = new JSONArray();
				json.put(CmnCnst.RESPONSE_LINT, lint);
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					try {
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
								IFormExpression<Formcycle> expression;
								expression = FormExpressionFactory.forProgram().compile(code,
										EEvaluationContextContractFormcycle.INSTANCE, config);
								addWarning(expression.analyze(new Formcycle()), lint, offset);
							}
							else {
								IFormExpression<Void> expression;
								expression = FormExpressionFactory.forProgram().compile(code,
										EEvaluationContextContractVoid.GENERIC, config);
								addWarning(expression.analyze(Void.NULL), lint, offset);
							}
						}
						else {
							if (CmnCnst.URL_PARAM_VALUE_CONTEXT_FORMCYCLE.equalsIgnoreCase(context)) {
								IFormExpression<Formcycle> expression;
								expression = FormExpressionFactory.forTemplate().compile(code,
										EEvaluationContextContractFormcycle.INSTANCE, config);
								addWarning(expression.analyze(new Formcycle()), lint, offset);
							}
							else {
								IFormExpression<Void> expression;
								expression = FormExpressionFactory.forTemplate().compile(code,
										EEvaluationContextContractVoid.GENERIC, config);
								addWarning(expression.analyze(Void.NULL), lint, offset);
							}
						}
					}
					catch (final ParseException | TokenMgrError e) {
						addError(e, lint, offset);
					}
					catch (final EvaluationException e) {
						final StringBuilder sb = new StringBuilder();
						sb.append(String.format(CmnCnst.RESPONSE_ERROR_EVALUATION_FAILED, e.getMessage()))
								.append(StringUtils.LF);
						for (final StackTraceElement el : e.getStackTrace()) {
							sb.append(el.toString()).append(StringUtils.LF);
						}
						json.put(CmnCnst.RESPONSE_ERROR, sb.toString());
						addError(e, lint, sb.toString(), offset);
					}
				}
				return json;
			}
		};
	}
}