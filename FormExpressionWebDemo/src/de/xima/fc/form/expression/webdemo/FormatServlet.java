package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.impl.config.UnparseConfig;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;

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
	protected Callable<JSONObject> getCallable(final HttpServletRequest request) {
		return new Callable<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public JSONObject call() throws Exception {
				final String code = getCode(request);
				final String type = getType(request);
				final String indent = getIndent(request);
				final Offset offset = getOffset(request);
				final IUnparseConfig config = new UnparseConfig.Builder().setLinefeed('\n').setIndentPrefix(indent)
						.setOptionalSpace(1).setRequiredSpace(1).setKeepComments(false).build();
				final JSONObject json = new JSONObject();
				final JSONArray lint = new JSONArray();
				json.put(CmnCnst.RESPONSE_LINT, lint);
				if (code == null) {
					json.put(CmnCnst.RESPONSE_ERROR, CmnCnst.RESPONSE_ERROR_PARAM_CODE_REQUIRED);
				}
				else {
					try (final Writer html = new StringBuilderWriter(); final Writer css = new StringBuilderWriter()) {
						final String formatted;
						if (CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM.equalsIgnoreCase(type)) {
							formatted = FormExpressionFactory.forProgram().format(code, config);
						}
						else {
							formatted = FormExpressionFactory.forTemplate().format(code, config);
						}
						json.put(CmnCnst.RESPONSE_FORMATTED_CODE, formatted);
					}
					catch (ParseException | TokenMgrError e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
						addError(e, lint, offset);
					}
					catch (final IOException e) {
						json.put(CmnCnst.RESPONSE_ERROR,
								String.format(CmnCnst.RESPONSE_ERROR_PARSING_FAILED, e.getMessage()));
					}
				}
				return json;
			}
		};
	}
}