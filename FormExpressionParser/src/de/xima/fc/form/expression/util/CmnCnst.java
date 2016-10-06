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

	public static final String SYNTAX_IF = "if";
	public static final String SYNTAX_ELSE = "else";
	public static final String SYNTAX_BRACE_OPEN = "{";
	public static final String SYNTAX_BRACE_CLOSE = "}";
	public static final String SYNTAX_PAREN_OPEN = "(";
	public static final String SYNTAX_PAREN_CLOSE = ")";
	public static final String SYNTAX_SEMI_COLON = ";";
}