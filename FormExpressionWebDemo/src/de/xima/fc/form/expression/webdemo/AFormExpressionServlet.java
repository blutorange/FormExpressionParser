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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class HighlightServlet
 */
public abstract class AFormExpressionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected HttpServletRequest request;
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
		this.request = request;
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<JSONObject> future = executor.submit(getCallable());
		JSONObject json;
		try {
			json = future.get(timeout, timeoutUnit);
		}
		catch (final TimeoutException e) {
			json = new JSONObject();
			json.put(CmnCnst.RESPONSE_ERROR, String.format(CmnCnst.RESPONSE_ERROR_TIMEOUT_REACHED,
					new Integer(CmnCnst.TIMEOUT), CmnCnst.TIMEOUT_UNIT, getErrorMessage(e)));
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
		this.request = null;
	}

	private static Object getErrorMessage(final Throwable e) {
		final StringBuilder sb = new StringBuilder();
		sb.append(e.getMessage()).append('\n');
		for (final StackTraceElement el : e.getStackTrace()) {
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

	protected abstract Callable<JSONObject> getCallable();
}
