package de.xima.fc.form.expression.webdemo;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("nls")
public final class CmnCnst {
	private CmnCnst() {}
	public final static int TIMEOUT = 20000;
	public final static TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

	public static final String URL_PARAM_KEY_CODE = "code";
	public static final String URL_PARAM_KEY_TYPE = "type";
	public static final String URL_PARAM_KEY_INDENT = "indent";
	public static final String URL_PARAM_KEY_CONTEXT = "context";
	public static final String URL_PARAM_KEY_PREFIX = "prefix";
	public static final String URL_PARAM_KEY_STRICT = "strict";
	public static final String URL_PARAM_VALUE_TYPE_PROGRAM = "program";
	public static final String URL_PARAM_VALUE_CONTEXT_FORMCYCLE = "formcycle";

	public static final Object RESPONSE_ERROR = "error";
	public static final Object RESPONSE_ERROR_PARAM_CODE_REQUIRED = "Parameter code must be given.";
	public static final String RESPONSE_ERROR_PARSING_FAILED = "Could not parse code: %s";
	public static final String RESPONSE_ERROR_EVALUATION_FAILED = "Could not evaluate code: %s";
	public static final Object RESPONSE_TEXT = "text";
	public static final Object RESPONSE_HTML = "html";
	public static final Object RESPONSE_CSS = "css";
	public static final String RESPONSE_ERROR_TIMEOUT_REACHED = "Could not complete task, reached timeout of %s %s. %s";
	public static final String RESPONSE_ERROR_TASK_CANCELLED = "Could not complete task, it was cancelled. %s";
	public static final String RESPONSE_ERROR_TASK_INTERRUPTED = "Could not complete task, an interruption occurred. %s";
	public static final String RESPONSE_ERROR_UNKNOWN = "Could not complete task, some unknown error occurred. That is all I know. %s";
}
