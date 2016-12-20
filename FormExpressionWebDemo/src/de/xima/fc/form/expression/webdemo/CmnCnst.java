package de.xima.fc.form.expression.webdemo;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

@SuppressWarnings("nls")
public final class CmnCnst {
	private CmnCnst() {}
	public final static int TIMEOUT = 20000;
	public final static TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

	@Nonnull public static final String URL_PARAM_KEY_CODE = "code";
	@Nonnull public static final String URL_PARAM_KEY_TYPE = "type";
	@Nonnull public static final String URL_PARAM_KEY_INDENT = "indent";
	@Nonnull public static final String URL_PARAM_KEY_CONTEXT = "context";
	@Nonnull public static final String URL_PARAM_KEY_PREFIX = "prefix";
	@Nonnull public static final String URL_PARAM_KEY_STRICT = "strict";
	@Nonnull public static final String URL_PARAM_KEY_OFFSET_LINE_BEGIN = "offsetLineBegin";
	@Nonnull public static final String URL_PARAM_KEY_OFFSET_COLUMN_BEGIN = "offsetColumnBegin";
	@Nonnull public static final String URL_PARAM_KEY_OFFSET_LINE_END = "offsetLineEnd";
	@Nonnull public static final String URL_PARAM_KEY_OFFSET_COLUMN_END = "offsetColumnEnd";
	@Nonnull public static final String URL_PARAM_VALUE_TYPE_PROGRAM = "program";
	@Nonnull public static final String URL_PARAM_VALUE_CONTEXT_FORMCYCLE = "formcycle";
	@Nonnull public static final String URL_PARAM_VALUE_CONTEXT_GENERIC = "generic";

	public static final String RESPONSE_ERROR = "error";
	public static final String RESPONSE_ERROR_PARAM_CODE_REQUIRED = "Parameter code must be given.";
	public static final String RESPONSE_ERROR_PARSING_FAILED = "Could not parse code: %s";
	public static final String RESPONSE_ERROR_EVALUATION_FAILED = "Could not evaluate code: %s";
	public static final String RESPONSE_RESULT_OBJECT = "evalObject";
	public static final String RESPONSE_RESULT_OUTPUT = "evalOutput";
	public static final String RESPONSE_FORMATTED_CODE = "formatCode";
	public static final String RESPONSE_HTML = "html";
	public static final String RESPONSE_CSS = "css";
	public static final String RESPONSE_ERROR_TIMEOUT_REACHED = "Could not complete task, reached timeout of %s %s. %s";
	public static final String RESPONSE_ERROR_TASK_CANCELLED = "Could not complete task, it was cancelled. %s";
	public static final String RESPONSE_ERROR_TASK_INTERRUPTED = "Could not complete task, an interruption occurred. %s";
	public static final String RESPONSE_ERROR_UNKNOWN = "Could not complete task, some unknown error occurred. That is all I know. %s";
	public static final String RESPONSE_LINT = "lint";
	public static final String RESPONSE_LINT_FROM = "from";
	public static final String RESPONSE_LINT_TO = "to";
	public static final String RESPONSE_LINT_MESSAGE = "message";
	public static final String RESPONSE_LINT_LINE = "line";
	public static final String RESPONSE_LINT_COLUMN = "ch";
	public static final String RESPONSE_LINT_SEVERITY = "severity";
	public static final String RESPONSE_LINT_WARNING = "warning";
	public static final String RESPONSE_LINT_ERROR = "error";
}