package de.xima.fc.form.expression.webdemo;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.xima.fc.form.expression.iface.IPositionedError;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.impl.config.SeverityConfig;

/**
 * Servlet implementation class HighlightServlet
 */
public abstract class AFormExpressionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final int timeout;
	private final TimeUnit timeoutUnit;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AFormExpressionServlet(final int timeout, final TimeUnit timeoutUnit) {
		super();
		this.timeout = timeout;
		this.timeoutUnit = timeoutUnit;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<JSONObject> future = executor.submit(getCallable(request));
		JSONObject json;
		try {
			json = future.get(timeout, timeoutUnit);
		}
		catch (final TimeoutException e) {
			json = new JSONObject();
			json.put(CmnCnst.RESPONSE_ERROR, String.format(CmnCnst.RESPONSE_ERROR_TIMEOUT_REACHED,
					Integer.valueOf(CmnCnst.TIMEOUT), CmnCnst.TIMEOUT_UNIT, getErrorMessage(e)));
		}
		catch (final CancellationException e) {
			json = new JSONObject();
			json.put(CmnCnst.RESPONSE_ERROR, String.format(CmnCnst.RESPONSE_ERROR_TASK_CANCELLED, getErrorMessage(e)));
		}
		catch (final InterruptedException e) {
			json = new JSONObject();
			json.put(CmnCnst.RESPONSE_ERROR,
					String.format(CmnCnst.RESPONSE_ERROR_TASK_INTERRUPTED, getErrorMessage(e)));
		}
		catch (final ExecutionException e) {
			json = new JSONObject();
			json.put(CmnCnst.RESPONSE_ERROR, String.format(CmnCnst.RESPONSE_ERROR_UNKNOWN, getErrorMessage(e)));
		}
		response.setStatus(200);
		response.getWriter().append(json.toJSONString());
	}

	private static Object getErrorMessage(@Nonnull final Throwable e) {
		final StringBuilder sb = new StringBuilder();
		for (Throwable cause = e; cause != null; cause = cause.getCause()) {
			sb.append(e.getClass().getCanonicalName()).append('\n');
			sb.append(cause.getMessage()).append('\n');
			for (final StackTraceElement el : cause.getStackTrace())
				sb.append(el.toString()).append('\n');
		}
		return sb.toString();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected static void addError(final IPositionedError e, final JSONArray lint, final Offset offset) {
		addError(e, lint, e.getMessage(), offset);
	}

	protected static void addError(final IPositionedError e, final JSONArray lint, final String message, final Offset offset) {
		if (e.isPositionInformationAvailable())
			addEntry(lint, message, CmnCnst.RESPONSE_LINT_ERROR, e.getBeginLine(), e.getBeginColumn(), e.getEndLine(), e.getEndColumn(), offset);
	}

	protected static void addWarning(final IEvaluationWarning warning, final JSONArray lint, final Offset offset) {
		addEntry(lint, warning.getMessage(), CmnCnst.RESPONSE_LINT_WARNING, warning.getBeginLine(),
				warning.getBeginColumn(), warning.getEndLine(), warning.getEndColumn(), offset);
	}

	protected static void addWarning(final Iterable<IEvaluationWarning> warnings, final JSONArray lint, final Offset offset) {
		for (final IEvaluationWarning warning : warnings)
			addWarning(warning, lint, offset);
	}

	@SuppressWarnings("unchecked")
	private static void addEntry(final JSONArray lint, final String message, final String severity, final int beginLine,
			final int beginColumn, final int endLine, final int endColumn, final Offset offset) {
		final JSONObject entry = new JSONObject();
		final JSONObject from = new JSONObject();
		final JSONObject to = new JSONObject();
		entry.put(CmnCnst.RESPONSE_LINT_FROM, from);
		entry.put(CmnCnst.RESPONSE_LINT_TO, to);
		entry.put(CmnCnst.RESPONSE_LINT_MESSAGE, message);
		entry.put(CmnCnst.RESPONSE_LINT_SEVERITY, severity);
		from.put(CmnCnst.RESPONSE_LINT_LINE, Integer.valueOf(beginLine-offset.offsetLineBegin));
		from.put(CmnCnst.RESPONSE_LINT_COLUMN, Integer.valueOf(beginColumn-offset.offsetColumnBegin));
		to.put(CmnCnst.RESPONSE_LINT_LINE, Integer.valueOf(endLine-offset.offsetLineEnd));
		to.put(CmnCnst.RESPONSE_LINT_COLUMN, Integer.valueOf(endColumn-offset.offsetColumnEnd));
		lint.add(entry);
	}

	protected static int asInt(final String value, final int defaultValue) {
		if (value == null)
			return defaultValue;
		try {
			return Integer.parseInt(value, 10);
		}
		catch (final NumberFormatException e) {
			e.printStackTrace(System.err);
			return defaultValue;
		}
	}

	@Nonnull
	protected static Offset getOffset(final HttpServletRequest request) {
		if (request == null)
			return new Offset(0, 0, 0, 0);
		final int offsetLineBegin = asInt(request.getParameter(CmnCnst.URL_PARAM_KEY_OFFSET_LINE_BEGIN), 0);
		final int offsetColumnBegin = asInt(request.getParameter(CmnCnst.URL_PARAM_KEY_OFFSET_COLUMN_BEGIN), 0);
		final int offsetLineEnd = asInt(request.getParameter(CmnCnst.URL_PARAM_KEY_OFFSET_LINE_END), 0);
		final int offsetColumnEnd = asInt(request.getParameter(CmnCnst.URL_PARAM_KEY_OFFSET_COLUMN_END), 0);
		final Offset offset = new Offset(offsetLineBegin, offsetColumnBegin, offsetLineEnd, offsetColumnEnd);
		return offset;
	}

	@Nonnull
	protected static String getType(final HttpServletRequest request) {
		if (request == null)
			return CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM;
		final String type = request.getParameter(CmnCnst.URL_PARAM_KEY_TYPE);
		if (type == null)
			return CmnCnst.URL_PARAM_VALUE_TYPE_PROGRAM;
		return type;
	}

	@Nonnull
	protected static String getContext(final HttpServletRequest request) {
		if (request == null)
			return CmnCnst.URL_PARAM_VALUE_CONTEXT_GENERIC;
		final String context = request.getParameter(CmnCnst.URL_PARAM_KEY_CONTEXT);
		if (context == null)
			return CmnCnst.URL_PARAM_VALUE_CONTEXT_GENERIC;
		return context;
	}

	@Nonnull
	protected static String getIndent(final HttpServletRequest request) {
		if (request == null)
			return CmnCnst.DEFAULT_INDENT_PREFIX;
		final String indent= request.getParameter(CmnCnst.URL_PARAM_KEY_INDENT);
		return indent != null ? indent : CmnCnst.DEFAULT_INDENT_PREFIX;
	}

	@Nonnull
	protected static String getCssClassPrefix(final HttpServletRequest request) {
		if (request == null)
			return CmnCnst.DEFAULT_CSS_PREFIX;
		final String prefix = request.getParameter(CmnCnst.URL_PARAM_KEY_PREFIX);
		return prefix != null ? prefix : CmnCnst.DEFAULT_CSS_PREFIX;
	}

	@Nullable
	protected static String getCode(final HttpServletRequest request) {
		return request != null ? request.getParameter(CmnCnst.URL_PARAM_KEY_CODE) : null;
	}

	@Nonnull
	protected static ISeverityConfig getSeverityConfig(final HttpServletRequest request) {
		final boolean strict = request != null ? Boolean.parseBoolean(request.getParameter(CmnCnst.URL_PARAM_KEY_STRICT)) : false;
		return strict ? SeverityConfig.getStrictConfig()
				: SeverityConfig.getLooseConfig();
	}

	protected abstract Callable<JSONObject> getCallable(HttpServletRequest request);

	protected static class Offset {
		public final int offsetLineBegin, offsetColumnBegin;
		public final int offsetLineEnd, offsetColumnEnd;
		public Offset(final int lineBegin, final int columnBegin,final int lineEnd, final int columnEnd) {
			offsetLineBegin = lineBegin;
			offsetColumnBegin = columnBegin;
			offsetLineEnd = lineEnd;
			offsetColumnEnd = columnEnd;
		}
	}
}