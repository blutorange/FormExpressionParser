package de.xima.fc.form.expression.util;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;

@SuppressWarnings("nls")
public final class CmnCnst {
	public static final class Variable {
		@Nonnull public static final String ARGUMENTS = "arguments";
		@Nonnull public static final String THIS = "this";
	}

	/** Scope with variables for form field variables/placeholders. */
	@Nonnull public static final String CUSTOM_SCOPE_FORM_FIELD = "field";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_FORM_FIELD_SILENT = "[%@";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_FORM_FIELD_VERBOSE = "[%";
	/** Scope with variables for system variables/placeholders. */
	@Nonnull public static final String CUSTOM_SCOPE_FC_SYSTEM = "fcsystem";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_FC_SYSTEM_SILENT = "[%$@";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_FC_SYSTEM_VERBOSE = "[%$";
	/** Scope with variables for system variables/placeholders. */
	@Nonnull public static final String CUSTOM_SCOPE_TEMPLATE = "template";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_TEMPLATE_SILENT = "[%$$@";
	@Nonnull public static final String CUSTOM_SCOPE_PREFIX_TEMPLATE_VERBOSE = "[%$$";
	/** Scope with functions for retrieving form field values etc. */
	@Nonnull public static final String CUSTOM_SCOPE_FORM_FUNCTIONS = "form";
	/** Scope for math-related utilities. */
	@Nonnull public static final String CUSTOM_SCOPE_MATH = "math";

	@Nonnull public static final String EMETHOD_PLUS = "+";
	@Nonnull public static final String EMETHOD_DOUBLE_PLUS = "++";
	@Nonnull public static final String EMETHOD_DASH = "-";
	@Nonnull public static final String EMETHOD_DOUBLE_DASH = "--";
	@Nonnull public static final String EMETHOD_STAR = "*";
	@Nonnull public static final String EMETHOD_DOUBLE_STAR = "**";
	@Nonnull public static final String EMETHOD_PERCENT = "%";
	@Nonnull public static final String EMETHOD_SLASH = "/";
	@Nonnull public static final String EMETHOD_CIRCUMFLEX = "^";
	@Nonnull public static final String EMETHOD_EXCLAMATION = "!";
	@Nonnull public static final String EMETHOD_EQUAL = "=";
	@Nonnull public static final String EMETHOD_DOUBLE_EQUAL = "==";
	@Nonnull public static final String EMETHOD_TRIPLE_EQUAL = "===";
	@Nonnull public static final String EMETHOD_EXCLAMATION_EQUAL = "!=";
	@Nonnull public static final String EMETHOD_EXCLAMATION_DOUBLE_EQUAL = "!==";
	@Nonnull public static final String EMETHOD_BAR = "|";
	@Nonnull public static final String EMETHOD_DOUBLE_BAR = "||";
	@Nonnull public static final String EMETHOD_AMPERSAND = "&";
	@Nonnull public static final String EMETHOD_DOUBLE_AMPERSAND = "&&";
	@Nonnull public static final String EMETHOD_TILDE = "~";
	@Nonnull public static final String EMETHOD_EXCLAMATION_TILDE = "!~";
	@Nonnull public static final String EMETHOD_EQUAL_TILDE = "=~";
	@Nonnull public static final String EMETHOD_PLUS_EQUAL = "+=";
	@Nonnull public static final String EMETHOD_DASH_EQUAL = "-=";
	@Nonnull public static final String EMETHOD_STAR_EQUAL = "*=";
	@Nonnull public static final String EMETHOD_SLASH_EQUAL = "/=";
	@Nonnull public static final String EMETHOD_PERCENT_EQUAL = "%=";
	@Nonnull public static final String EMETHOD_DOUBLE_STAR_EQUAL = "**=";
	@Nonnull public static final String EMETHOD_AMPERSAND_EQUAL = "&=";
	@Nonnull public static final String EMETHOD_BAR_EQUAL = "|=";
	@Nonnull public static final String EMETHOD_DOUBLE_BAR_EQUAL = "||=";
	@Nonnull public static final String EMETHOD_DOUBLE_AMPERSAND_EQUAL = "&&=";
	@Nonnull public static final String EMETHOD_ANGLE_OPEN_EQUAL = "<=";
	@Nonnull public static final String EMETHOD_ANGLE_CLOSE_EQUAL = ">=";
	@Nonnull public static final String EMETHOD_DOUBLE_ANGLE_OPEN_EQUAL = "<<=";
	@Nonnull public static final String EMETHOD_TRIPLE_ANGLE_OPEN_EQUAL = "<<<=";
	@Nonnull public static final String EMETHOD_DOUBLE_ANGLE_CLOSE_EQUAL = ">>=";
	@Nonnull public static final String EMETHOD_TRIPLE_ANGLE_CLOSE_EQUAL = ">>>=";
	@Nonnull public static final String EMETHOD_COERCE = "=>";
	@Nonnull public static final String EMETHOD_DOT = ".";
	@Nonnull public static final String EMETHOD_BRACKET = "[]";
	@Nonnull public static final String EMETHOD_PARENTHESIS = "()";
	@Nonnull public static final String EMETHOD_SWITCHCASE = "SWITCHCASE";
	@Nonnull public static final String EMETHOD_SWITCHDEFAULT = "SWITCHDEFAULT";
	@Nonnull public static final String EMETHOD_SWITCHCLAUSE = "SWITCHCLAUSE";
	@Nonnull public static final String EMETHOD_ANGLE_OPEN = "<";
	@Nonnull public static final String EMETHOD_ANGLE_CLOSE = ">";
	@Nonnull public static final String EMETHOD_DOUBLE_ANGLE_OPEN = "<<";
	@Nonnull public static final String EMETHOD_TRIPLE_ANGLE_OPEN = "<<<";
	@Nonnull public static final String EMETHOD_DOUBLE_ANGLE_CLOSE = ">>";
	@Nonnull public static final String EMETHOD_TRIPLE_ANGLE_CLOSE = ">>>";


	// Used when constructing the stack trace.
	@Nonnull public static final String TRACER_POSITION_NAME_ANONYMOUS_FUNCTION = "anonymous function";
	@Nonnull public static final String TRACER_POSITION_NAME_GLOBAL = "main";
	@Nonnull public static final String TRACER_POSITION_UNKNOWN = "unknown line";
	@Nonnull public static final String TRACER_POSITION_NAME_UNKNOWN = "unknown";

	@Nonnull public static final String SYNTAX_SWITCH = "switch";
	@Nonnull public static final String SYNTAX_CASE = "case";
	@Nonnull public static final String SYNTAX_DEFAULT = "default";
	@Nonnull public static final String SYNTAX_FOR = "for";
	@Nonnull public static final String SYNTAX_WHILE = "while";
	@Nonnull public static final String SYNTAX_DO = "do";
	@Nonnull public static final String SYNTAX_WITH = "with";
	@Nonnull public static final String SYNTAX_EXCEPTION = "exception";
	@Nonnull public static final String SYNTAX_THROW = "throw";
	@Nonnull public static final String SYNTAX_TRY = "try";
	@Nonnull public static final String SYNTAX_CATCH = "catch";
	@Nonnull public static final String SYNTAX_IF = "if";
	@Nonnull public static final String SYNTAX_ELSE = "else";
	@Nonnull public static final String SYNTAX_FUNCTION = "function";
	@Nonnull public static final String SYNTAX_RETURN = "return";
	@Nonnull public static final String SYNTAX_CONTINUE = "continue";
	@Nonnull public static final String SYNTAX_BREAK = "break";
	@Nonnull public static final String SYNTAX_LOG_DEBUG = "logdebug";
	@Nonnull public static final String SYNTAX_LOG_ERROR = "logerror";
	@Nonnull public static final String SYNTAX_LOG_INFO = "loginfo";
	@Nonnull public static final String SYNTAX_LOG_WARN = "logwarn";
	@Nonnull public static final String SYNTAX_LOS_CLOSE = "%]";
	@Nonnull public static final String SYNTAX_LAMBDA_ARROW = "->";
	@Nonnull public static final String SYNTAX_BRACE_OPEN = "{";
	@Nonnull public static final String SYNTAX_BRACE_CLOSE = "}";
	@Nonnull public static final String SYNTAX_PAREN_OPEN = "(";
	@Nonnull public static final String SYNTAX_PAREN_CLOSE = ")";
	@Nonnull public static final String SYNTAX_BRACKET_OPEN = "[";
	@Nonnull public static final String SYNTAX_BRACKET_CLOSE = "]";
	@Nonnull public static final String SYNTAX_QUESTION_MARK = "?";
	@Nonnull public static final String SYNTAX_ANGLE_OPEN = "<";
	@Nonnull public static final String SYNTAX_ANGLE_CLOSE = ">";
	@Nonnull public static final String SYNTAX_DOT = ".";
	@Nonnull public static final String SYNTAX_COMMA = ",";
	@Nonnull public static final String SYNTAX_COLON = ":";
	@Nonnull public static final String SYNTAX_SEMI_COLON = ";";
	@Nonnull public static final String SYNTAX_SCOPE_SEPARATOR = "::";
	@Nonnull public static final String SYNTAX_ENHANCED_FOR_LOOP_SEPARATOR = ":";
	@Nonnull public static final String SYNTAX_LINEFEED = "\n";
	@Nonnull public static final String SYNTAX_INDENT = "  ";
	@Nonnull public static final String SYNTAX_ERROR = "error";
	@Nonnull public static final String SYNTAX_TRUE = "true";
	@Nonnull public static final String SYNTAX_FALSE = "false";
	@Nonnull public static final String SYNTAX_NULL = "null";
	@Nonnull public static final String SYNTAX_NATIVE_CODE = "'[native code]'";
	@Nonnull public static final String SYNTAX_FAILED_TO_UNPARSE_LAMBDA = "'[error during unparsing]'";
	@Nonnull public static final String SYNTAX_QUOTE = "\"";

	@Nonnull public static final String NAME_ATTRIBUTE_ACCESSOR = "attribute accessor";
	@Nonnull public static final String NAME_DOT_ATTRIBUTE_ACCESSOR = "dot attribute accessor";
	@Nonnull public static final String NAME_BRACKET_ATTRIBUTE_ACCESSOR = "bracket attribute accessor";
	@Nonnull public static final String NAME_ATTRIBUTE_ASSIGNER = "attribute assigner";
	@Nonnull public static final String NAME_DOT_ATTRIBUTE_ASSIGNER = "dot attribute assigner";
	@Nonnull public static final String NAME_BRACKET_ATTRIBUTE_ASSIGNER = "bracket attribute assigner";
	@Nonnull public static final String NAME_METHOD = "method";
	@Nonnull public static final String NAME_PREFIX_OPERATION = "prefix operation";
	@Nonnull public static final String NAME_SUFFIX_OPERATION = "suffix operation";
	@Nonnull public static final String NAME_FOR_ITERATING_NODE = "ForIteratingLoopNode";
	@Nonnull public static final String NAME_FOR_PLAIN_NODE = "ForPlainLoopNode";
	@Nonnull public static final String NAME_ASSIGNMENT = "assignment";
	@Nonnull public static final String NAME_SYSTEM_LOGGER = "SystemLogger:";
	@Nonnull public static final String NAME_ARRAY = "array";
	@Nonnull public static final String NAME_ELEMENT = "element";
	@Nonnull public static final String NAME_KEY = "key";
	@Nonnull public static final String NAME_HASH = "hash";
	@Nonnull public static final String NAME_VALUE = "value";


	@Nonnull public static final String SYSTEM_LOGGER_FORMAT = "[%s] (%s) %s %s";
	@Nonnull public static final String[] EMPTY_STRING_ARRAY = new String[0];
	@Nonnull public static final String EMPTY_STRING = "";
	@Nonnull public static final Pattern EMPTY_PATTERN = NullUtil.checkNotNull(Pattern.compile(EMPTY_STRING));
	@Nonnull public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	@Nonnull public static final Node[] EMPTY_NODE_ARRAY = new Node[0];

	public static final class Error {
		@Nonnull public static final String ARRAY_INDEX_OUT_OF_BOUNDS = "Index %s out of bounds for array %s.";
		@Nonnull public static final String BREAK_CLAUSE = "Break used outside of loop or switch, or label does not match any loop or switch.";
		@Nonnull public static final String CANNOT_ACQUIRE_EVALUATION_CONTEXT = "Failed to acquire evaluation context.";
		@Nonnull public static final String COERCION = "Object %s of class %s cannot be coerced to %s.";
		@Nonnull public static final String CONTINUE_CLAUSE = "Continue used outside of loop or switch, or label does not match any loop or switch.";
		@Nonnull public static final String CUSTOM_RUNTIME_EXCEPTION = "Custom Exception: %s";
		@Nonnull public static final String EMBEDMENT_OUPTUT = "Failed to write output due to an I/O exception.";
		@Nonnull public static final String ILLEGAL_ARGUMENT_VALUE = "Illegal value %s for argument %d of function %s: %s.";
		@Nonnull public static final String ILLEGAL_NUMBER_OF_ARGUMENTS_EXACT = "Encountered %s argument(s) for function %s, expected at least %d";
		@Nonnull public static final String ILLEGAL_NUMBER_OF_ARGUMENTS_RANGE = "Encountered %d number of arguments for function %s, expected %d to %d.";
		@Nonnull public static final String ILLEGAL_THIS_CONTEXT = "Provided this context <%s> of type %s does not match the expected type %s for function <%s>.";
		@Nonnull public static final String INVALID_REGEX_PATTERN = "Invalid regex pattern %s and/or flags %s.";
		@Nonnull public static final String ITERATION_NOT_SUPPORTED = "Object <%s> of type %s does not support iteration.";
		@Nonnull public static final String MATH_DIVISION_BY_ZERO = "Division by zero: %s / %s";
		@Nonnull public static final String MATH = "Error during math operation: %s";
		@Nonnull public static final String NESTING_LEVEL_TOO_DEEP = "Nesting level too deep: %d.";
		@Nonnull public static final String NO_SUCH_FUNCTION_1 = "No such %s named %s.";
		@Nonnull public static final String NO_SUCH_FUNCTION_2 = "No such %s named <%s> for object <%s> of type %s.";
		@Nonnull public static final String NO_SUCH_METHOD = "%s(%s)";
		@Nonnull public static final String OPERATION_NOT_YET_IMPLEMENTED = "Operation %s has not yet been implemented yet.";
		@Nonnull public static final String RETURN_CLAUSE = "Return clause used outside a function.";
		@Nonnull public static final String STRING_INDEX_OUT_OF_BOUNDS = "Index %s out of bounds for string <%s>";
		@Nonnull public static final String VARIABLE_NOT_DEFINED_LOCAL = "Variable %s not resolvable to a defined variable.";
		@Nonnull public static final String VARIABLE_NOT_DEFINED_SCOPED = "Variable %s::%s not defined at this point.";
		@Nonnull public static final String EVALUATION_EXCEPTION_EC = "Evaluation context is";
		@Nonnull public static final String EVALUATION_EXCEPTION_EX = "External context is";
		@Nonnull public static final String EVALUATION_EXCEPTION_AT = "at";
		@Nonnull public static final String EVALUATION_EXCEPTION_LINE = "line";
		@Nonnull public static final String EVALUATION_EXCEPTION_COLUMN = "column";

		@Nonnull public static final String INTERNAL_ERROR = "This is likely an error with the program. Contact support.";
		@Nonnull public static final String VARIABLE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_LVALUE_FUNCTION = "Encountered illegal LVALUE (function call) %s in %s at line %s, column %s.";
		@Nonnull public static final String ILLEGAL_LVALUE = "Encountered illegal LVALUE %s in %s at line %s, column %s.";
		@Nonnull public static final String NODE_COUNT_NOT_ODD = "Node count is not odd: %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_NOT_EVEN = "Node count is not even: %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_AT_MOST = "Node can have at most %d children, but it has %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_AT_LEAST = "Node must have at least %s children, but it has %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_EXACTLY_ONE_OF = "Node must have exactly %d or %d children, not %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_EXACTLY = "Node must have exactly %d children, not %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_BETWEEN = "Node must have between %d and %d children, but it has %d. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INCORRECT_TYPE = "Node type is %s, but expected %s. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_NULL_STRING = "String is null. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_IMPROPER_STRING_TERMINATION = "String <%s> not terminated properly. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INVALID_STRING = "Encountered invalid string at line %d, column %d: %s";
		@Nonnull public static final String NODE_NULL_REGEX = "Regex is null. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_IMPROPER_REGEX_TERMINATION = "Regex <%s> not terminated properly. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INVALID_REGEX = "Encountered invalid regex at line %d, column %d: %s";
		@Nonnull public static final String NODE_INVALID_NUMBER = "Encountered invalid number %s at line %d, column %d: %s";
		@Nonnull public static final String NULL_EXTERNAL_CONTEXT = "External context must not be null";
		@Nonnull public static final String POOL_FAILED_TO_RETURN_EC = "Failed to return evaluation context %s to pool.";
		@Nonnull public static final String TOKEN_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Token iterator does not support removal.";
		@Nonnull public static final String INVALID_JUMP_TYPE = "Invalid jump type %s. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_PROPERTY_EXPRESSION = "Illegal enum constant %s at ASTPropertyExpressionNode. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_ASSIGNMENT = "Illegal enum constant %s at %s. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_SWITCH = "Illegal enum for switch : %s. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_LOGLEVEL = "No such log level %s. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_EQUAL = "Unexptected enum for equal expression: %s. " + INTERNAL_ERROR;
		@Nonnull public static final String CANNOT_EVALUATE_VARIABLE_TYPE_NODE = "ASTVariableTypeDeclarationNode cannot be evaluated. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_WRITER = "Writer must not be null";
		@Nonnull public static final String TODO = "TODO - not yet implemented. " + INTERNAL_ERROR;
		@Nonnull public static final String COERCION_TYPE_NOT_MATCHING = "Argument type and return class do not match";
		@Nonnull public static final String DEPRECATED_ALANGOBJECT_ITERATOR = "Do not iterate directly, use getIterable(IEvaluationContext).";
		@Nonnull public static final String FAILURE_UNPARSING_LAMBDA = "Failed to unparse lambda expression.";
		@Nonnull public static final String NUMBER_TOO_LONG_FOR_LONG = "Number too large or too small to be represented as a long: %s.";
		@Nonnull public static final String NUMBER_TOO_LONG_FOR_INT = "Number too large or too small to be represented as an int: %s.";
		@Nonnull public static final String NUMBER_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for NumberLangObject::iterator.";
		@Nonnull public static final String STRING_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for StringLangObject::iterator.";
		@Nonnull public static final String NULL_MAP = "Map must not be null";
		@Nonnull public static final String ILLEGAL_STATE_NAMESPACE_BUILDER = "All expression method, attribute accessors, and attribute assigners must be set.";
		@Nonnull public static final String ILLEGAL_STATE_EC_BUILDER_BINDING = "Binding not set.";
		@Nonnull public static final String ILLEGAL_STATE_EC_BUILDER_SCOPE = "Scope not set.";
		@Nonnull public static final String CANNOT_UNNEST_GLOBAL_BINDING = "Cannot unnest global binding. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ARGUMENTS_FOR_DOCUMENT_COMMAND = "Document command of type %s requires %d arguments, but %d were given: %s.";
		@Nonnull public static final String NULL_TYPE = "Type must not be null.";
		@Nonnull public static final String INVALID_HTML_TEMPLATE = "Invalid html.";
		@Nonnull public static final String UNKNOWN_COMMAND_FOR_HTML_CONTEXT = "Command %s.%s cannot be processed by AHtmlExternalContext.";
		@Nonnull public static final String UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT = "Command %s cannot be processed by SystemOutExternalContext.";
		@Nonnull public static final String NULL_CHILD_NODE = "Child node must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_METHOD = "Method must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_NODE = "Node must not be null." + INTERNAL_ERROR;
		@Nonnull public static final String PARSER_RETURNED_NULL_NODE = "Failed to parse code: parser returned null node.";
		@Nonnull public static final String FUNCTION_MISSING_RETURN_TYPE = "Functions must declare their return type.";
		@Nonnull public static final String ILLEGAL_VARIABLE_TYPE = "Encountered incompatible variable type at line %d, column %d: %s";
		@Nonnull public static final String INCOMPATIBLE_STATEMENT_LIST_RETURN_TYPE = "Statement at index %d can return type %s, but a previous statement can return type %s.";
		@Nonnull public static final String INCOMPATIBLE_IF_RETURN_TYPE = "If clause can return type %s, but else clause can return type %s.";
		@Nonnull public static final String INCOMPATIBLE_FUNCTION_RETURN_TYPE = "Expected function to return type %s, but it can return type %s.";
		@Nonnull public static final String MISSING_VARIABLE_TYPE = "Missing variable type declaration: %s";
		@Nonnull public static final String INHOMOGENOUS_COMPOUND_TYPE = "Inhomogenous %s at line %d, column %d contains %s of type %s at index %d that is incompatible with a previous %s of type %s.";
		@Nonnull public static final String INVALID_EQUAL_METHOD = "Invalid enum for equal method: %s.";
		@Nonnull public static final String INVALID_COMPARISON_METHOD = "Invalid enum for comparison method: %s";
		@Nonnull public static final String NULL_ERROR_VARIABLE_NAME = "Catch clause must specifiy a variable name. " + INTERNAL_ERROR;
		@Nonnull public static final String EC_POOL_RETURNED_NULL = "Pool returned null";
	}

	public static final class ToString {
		@Nonnull public static final String COLOR = "Color(%s)";
		@Nonnull public static final String STYLE = "Style(%s,%s,%s,%s)";
		@Nonnull public static final String BOOLEAN_TRUE = "true";
		@Nonnull public static final String BOOLEAN_FALSE = "false";
		public static final char[] INSPECT_A_LANG_OBJECT = "ALangObject@".toCharArray();
		public static final char[] INSPECT_ARRAY_LANG_OBJECT = "ArrayLangObject".toCharArray();
		public static final char[] INSPECT_BOOLEAN_LANG_OBJECT = "BooleanLangObject".toCharArray();
		public static final char[] INSPECT_EXCEPTION_LANG_OBJECT = "ExceptionLangObject".toCharArray();
		public static final char[] INSPECT_FUNCTION_LANG_OBJECT = "FunctionLangObject".toCharArray();
		public static final char[] INSPECT_HASH_LANG_OBJECT = "HashLangObject".toCharArray();
		public static final char[] INSPECT_NUMBER_LANG_OBJECT = "NumberLangObject".toCharArray();
		public static final char[] INSPECT_REGEX_LANG_OBJECT = "RegexLangObject".toCharArray();
		public static final char[] INSPECT_STRING_LANG_OBJECT = "StringLangObject".toCharArray();
		@Nonnull public static final String INSPECT_NULL_LANG_OBJECT = "NullLangObject";
		@Nonnull public static final String DOCUMENT_COMMAND = "%s(%s)";
		@Nonnull public static final String POSITIONED_DOCUMENT_COMMAND = "%s@%s(%s)";
	}

	public static final class Html {

		@Nonnull public static final String A = "a";
		@Nonnull public static final String HREF = "href";
		@Nonnull public static final String TARGET = "target";

	}
}