package de.xima.fc.form.expression.util;

public final class CmnCnst {
	public final static class Variable {
		public final static String ARGUMENTS = "arguments";
		public final static String THIS = "this";
	}

	/** Scope with variables for form field values by alias/name. */
	public static final String CUSTOM_SCOPE_FORM_FIELD = "fields";
	/** Scope with functions for retrieving form field values etc. */
	public static final String CUSTOM_SCOPE_FORM_FUNCTIONS = "form";

	// Used when constructing the stack trace.
	public static final String TRACER_POSITION_NAME_ANONYMOUS_FUNCTION = "anonymous function";
	public static final String TRACER_POSITION_NAME_GLOBAL = "main";
	public static final Object TRACER_POSITION_UNKNOWN = "unknown line";
	public static final String TRACER_POSITION_NAME_UNKNOWN = "unknown";
}
