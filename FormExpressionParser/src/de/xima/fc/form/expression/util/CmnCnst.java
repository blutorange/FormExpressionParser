package de.xima.fc.form.expression.util;

public final class CmnCnst {
	public final static class Variable {
		public final static String ARGUMENTS = "arguments";
		public final static String THIS = "this";
	}

	/** Scope with variables for form field variables/placeholders. */
	public static final String CUSTOM_SCOPE_FORM_FIELD = "field";
	public static final String CUSTOM_SCOPE_PREFIX_FORM_FIELD_SILENT = "[%@";
	public static final String CUSTOM_SCOPE_PREFIX_FORM_FIELD_VERBOSE = "[%";
	/** Scope with variables for system variables/placeholders. */
	public static final String CUSTOM_SCOPE_FC_SYSTEM = "fcsystem";
	public static final String CUSTOM_SCOPE_PREFIX_FC_SYSTEM_SILENT = "[%$@";
	public static final String CUSTOM_SCOPE_PREFIX_FC_SYSTEM_VERBOSE = "[%$";
	/** Scope with variables for system variables/placeholders. */
	public static final String CUSTOM_SCOPE_TEMPLATE = "template";
	public static final String CUSTOM_SCOPE_PREFIX_TEMPLATE_SILENT = "[%$$@";
	public static final String CUSTOM_SCOPE_PREFIX_TEMPLATE_VERBOSE = "[%$$";
	/** Scope with functions for retrieving form field values etc. */
	public static final String CUSTOM_SCOPE_FORM_FUNCTIONS = "form";
	/** Scope for math-related utilities. */
	public static final String CUSTOM_SCOPE_MATH = "math";

	// Used when constructing the stack trace.
	public static final String TRACER_POSITION_NAME_ANONYMOUS_FUNCTION = "anonymous function";
	public static final String TRACER_POSITION_NAME_GLOBAL = "main";
	public static final Object TRACER_POSITION_UNKNOWN = "unknown line";
	public static final String TRACER_POSITION_NAME_UNKNOWN = "unknown";

	public static final String SYNTAX_SWITCH = "switch";
	public static final String SYNTAX_CASE = "case";
	public static final String SYNTAX_DEFAULT = "default";
	public static final String SYNTAX_FOR = "for";
	public static final String SYNTAX_WHILE = "while";
	public static final String SYNTAX_DO = "do";
	public static final String SYNTAX_WITH = "with";
	public static final String SYNTAX_EXCEPTION = "exception";
	public static final String SYNTAX_THROW = "throw";
	public static final String SYNTAX_TRY = "try";
	public static final String SYNTAX_CATCH = "catch";
	public static final String SYNTAX_IF = "if";
	public static final String SYNTAX_ELSE = "else";
	public static final String SYNTAX_FUNCTION = "function";
	public static final String SYNTAX_RETURN = "return";
	public static final String SYNTAX_CONTINUE = "continue";
	public static final String SYNTAX_BREAK = "break";
	public static final String SYNTAX_LOG_DEBUG = "logdebug";
	public static final String SYNTAX_LOG_ERROR = "logerror";
	public static final String SYNTAX_LOG_INFO = "loginfo";
	public static final String SYNTAX_LOG_WARN = "logwarn";
	public static final String SYNTAX_LOS_CLOSE = "%]";
	public static final String SYNTAX_LAMBDA_ARROW = "->";
	public static final String SYNTAX_BRACE_OPEN = "{";
	public static final String SYNTAX_BRACE_CLOSE = "}";
	public static final String SYNTAX_PAREN_OPEN = "(";
	public static final String SYNTAX_PAREN_CLOSE = ")";
	public static final String SYNTAX_BRACKET_OPEN = "[";
	public static final String SYNTAX_BRACKET_CLOSE = "]";
	public static final String SYNTAX_DOT = ".";
	public static final String SYNTAX_COMMA = ",";
	public static final String SYNTAX_COLON = ":";
	public static final String SYNTAX_SEMI_COLON = ";";
	public static final String SYNTAX_SCOPE_SEPARATOR = "::";
	public static final String SYNTAX_ENHANCED_FOR_LOOP_SEPARATOR = ":";

}