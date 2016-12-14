package de.xima.fc.form.expression.util;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.GenericSourceResolvable;

@SuppressWarnings("nls")
public final class CmnCnst {
	private CmnCnst() {}


	// Used when constructing the stack trace.
	@Nonnull public static final String TRACER_POSITION_NAME_ANONYMOUS_FUNCTION = "anonymous function";
	@Nonnull public static final String TRACER_POSITION_NAME_GLOBAL = "main";

	@Nonnull public static final String SYSTEM_LOGGER_FORMAT = "[%s] (%s) %s %s";
	@Nonnull public static final Pattern PATTERN_FORM_FIELD_NAME = NullUtil.checkNotNull(Pattern.compile("^[a-zA-Z0-9_]+$"));

	public final static class Warning {
		private Warning(){}
		@Nonnull public static final String UNUSED_VARIABLE = "Variable {0} is never used.";
		@Nonnull public static final String EMPTY_STATEMENT = "Encountered an empty statement.";
	}

	public final static class NonnullConstant {
		private NonnullConstant() {}
		@Nonnull public static final Boolean BOOLEAN_TRUE = new Boolean(true);
		@Nonnull public static final Boolean BOOLEAN_FALSE = new Boolean(false);
		@Nonnull public static final String STRING_EMPTY = "";
		@Nonnull public static final String STRING_SPACE = " ";
		@Nonnull public static final String STRING_LF = "\n";
		@Nonnull public static final Pattern EMPTY_PATTERN = NullUtil.checkNotNull(Pattern.compile(STRING_EMPTY));
		@Nonnull public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
		@Nonnull public static final int[] EMPTY_INT_ARRAY = new int[0];
		@Nonnull public static final Node[] EMPTY_NODE_ARRAY = new Node[0];
		@Nonnull public static final IVariableReference[] EMPTY_SYMBOL_TABLE = new IVariableReference[0];
		@Nonnull public static final String[] EMPTY_STRING_ARRAY = new String[0];
		@Nonnull public static final GenericSourceResolvable[] EMPTY_GENERIC_SOURCE_RESOLVABLE_ARRAY = new GenericSourceResolvable[0];
		@Nonnull public static final IVariableType[] EMPTY_VARIABLE_TYPE_ARRAY = new IVariableType[0];
	}

	public final static class CustomScope {
		private CustomScope() {}
		/** Scope with variables for form field variables/placeholders. */
		@Nonnull public static final String FORM_FIELD = "field";
		@Nonnull public static final String PREFIX_FORM_FIELD_SILENT = "[%@";
		@Nonnull public static final String PREFIX_FORM_FIELD_VERBOSE = "[%";
		/** Scope with variables for system variables/placeholders. */
		@Nonnull public static final String FC_SYSTEM = "fcsystem";
		@Nonnull public static final Object ACTION_VARIABLE_NAME_RESULT = "RESULT";
		@Nonnull public static final String PREFIX_FC_SYSTEM_SILENT = "[%$@";
		@Nonnull public static final String PREFIX_FC_SYSTEM_VERBOSE = "[%$";
		/** Scope with variables for system variables/placeholders. */
		@Nonnull public static final String TEMPLATE = "template";
		@Nonnull public static final String PREFIX_TEMPLATE_SILENT = "[%$$@";
		@Nonnull public static final String PREFIX_TEMPLATE_VERBOSE = "[%$$";
		@Nonnull public static final String GENERAL_NO_OUTPUT = "[%%";
		@Nonnull public static final String GENERAL_YES_OUTPUT = "[%%=";
		/** Scope with functions for retrieving form field values etc. */
		@Nonnull public static final String FORM_FUNCTIONS = "form";
		/** Scope for math-related utilities. */
		@Nonnull public static final String MATH = "math";

	}

	public final static class Name {
		private Name() {}
		@Nonnull public static final String VARIABLE_ARGUMENTS = "arguments";
		@Nonnull public static final String VARIABLE_THIS = "this";
		@Nonnull public static final String ATTRIBUTE_ACCESSOR = "attribute accessor";
		@Nonnull public static final String DOT_ATTRIBUTE_ACCESSOR = "dot attribute accessor";
		@Nonnull public static final String BRACKET_ATTRIBUTE_ACCESSOR = "bracket attribute accessor";
		@Nonnull public static final String ATTRIBUTE_ASSIGNER = "attribute assigner";
		@Nonnull public static final String DOT_ATTRIBUTE_ASSIGNER = "dot attribute assigner";
		@Nonnull public static final String BRACKET_ATTRIBUTE_ASSIGNER = "bracket attribute assigner";
		@Nonnull public static final String METHOD = "method";
		@Nonnull public static final String PREFIX_OPERATION = "prefix operation";
		@Nonnull public static final String SUFFIX_OPERATION = "suffix operation";
		@Nonnull public static final String FOR_ITERATING_NODE = "ForIteratingLoopNode";
		@Nonnull public static final String FOR_PLAIN_NODE = "ForPlainLoopNode";
		@Nonnull public static final String ASSIGNMENT = "assignment";
		@Nonnull public static final String SYSTEM_LOGGER = "SystemLogger:";
		@Nonnull public static final String ARRAY = "array";
		@Nonnull public static final String ELEMENT = "element";
		@Nonnull public static final String KEY = "key";
		@Nonnull public static final String HASH = "hash";
		@Nonnull public static final String VALUE = "value";
		@Nonnull public static final String EMPTY_LABEL = "without label";
	}

	public final static class ExpressionMethod {
		private ExpressionMethod() {}
		@Nonnull public static final String PLUS = "+";
		@Nonnull public static final String DOUBLE_PLUS = "++";
		@Nonnull public static final String DASH = "-";
		@Nonnull public static final String DOUBLE_DASH = "--";
		@Nonnull public static final String STAR = "*";
		@Nonnull public static final String DOUBLE_STAR = "**";
		@Nonnull public static final String PERCENT = "%";
		@Nonnull public static final String SLASH = "/";
		@Nonnull public static final String CIRCUMFLEX = "^";
		@Nonnull public static final String EXCLAMATION = "!";
		@Nonnull public static final String EQUAL = "=";
		@Nonnull public static final String DOUBLE_EQUAL = "==";
		@Nonnull public static final String TRIPLE_EQUAL = "===";
		@Nonnull public static final String EXCLAMATION_EQUAL = "!=";
		@Nonnull public static final String EXCLAMATION_DOUBLE_EQUAL = "!==";
		@Nonnull public static final String BAR = "|";
		@Nonnull public static final String DOUBLE_BAR = "||";
		@Nonnull public static final String AMPERSAND = "&";
		@Nonnull public static final String DOUBLE_AMPERSAND = "&&";
		@Nonnull public static final String TILDE = "~";
		@Nonnull public static final String EXCLAMATION_TILDE = "!~";
		@Nonnull public static final String EQUAL_TILDE = "=~";
		@Nonnull public static final String PLUS_EQUAL = "+=";
		@Nonnull public static final String DASH_EQUAL = "-=";
		@Nonnull public static final String STAR_EQUAL = "*=";
		@Nonnull public static final String SLASH_EQUAL = "/=";
		@Nonnull public static final String PERCENT_EQUAL = "%=";
		@Nonnull public static final String DOUBLE_STAR_EQUAL = "**=";
		@Nonnull public static final String AMPERSAND_EQUAL = "&=";
		@Nonnull public static final String BAR_EQUAL = "|=";
		@Nonnull public static final String DOUBLE_BAR_EQUAL = "||=";
		@Nonnull public static final String DOUBLE_AMPERSAND_EQUAL = "&&=";
		@Nonnull public static final String ANGLE_OPEN_EQUAL = "<=";
		@Nonnull public static final String ANGLE_CLOSE_EQUAL = ">=";
		@Nonnull public static final String DOUBLE_ANGLE_OPEN_EQUAL = "<<=";
		@Nonnull public static final String TRIPLE_ANGLE_OPEN_EQUAL = "<<<=";
		@Nonnull public static final String DOUBLE_ANGLE_CLOSE_EQUAL = ">>=";
		@Nonnull public static final String TRIPLE_ANGLE_CLOSE_EQUAL = ">>>=";
		@Nonnull public static final String COERCE = "=>";
		@Nonnull public static final String DOT = ".";
		@Nonnull public static final String BRACKET = "[]";
		@Nonnull public static final String PARENTHESIS = "()";
		@Nonnull public static final String SWITCHCASE = "SWITCHCASE";
		@Nonnull public static final String SWITCHDEFAULT = "SWITCHDEFAULT";
		@Nonnull public static final String SWITCHCLAUSE = "SWITCHCLAUSE";
		@Nonnull public static final String ANGLE_OPEN = "<";
		@Nonnull public static final String ANGLE_CLOSE = ">";
		@Nonnull public static final String DOUBLE_ANGLE_OPEN = "<<";
		@Nonnull public static final String TRIPLE_ANGLE_OPEN = "<<<";
		@Nonnull public static final String DOUBLE_ANGLE_CLOSE = ">>";
		@Nonnull public static final String TRIPLE_ANGLE_CLOSE = ">>>";
	}

	public final static class Syntax {
		private Syntax() {}
		@Nonnull public static final String GLOBAL = "global";
		@Nonnull public static final String REQUIRE = "require";
		@Nonnull public static final String SCOPE = "scope";
		@Nonnull public static final String SWITCH = "switch";
		@Nonnull public static final String CASE = "case";
		@Nonnull public static final String DEFAULT = "default";
		@Nonnull public static final String FOR = "for";
		@Nonnull public static final String WHILE = "while";
		@Nonnull public static final String DO = "do";
		@Nonnull public static final String WITH = "with";
		@Nonnull public static final String EXCEPTION = "exception";
		@Nonnull public static final String THROW = "throw";
		@Nonnull public static final String TRY = "try";
		@Nonnull public static final String CATCH = "catch";
		@Nonnull public static final String IF = "if";
		@Nonnull public static final String ELSE = "else";
		@Nonnull public static final String FUNCTION = "function";
		@Nonnull public static final String RETURN = "return";
		@Nonnull public static final String CONTINUE = "continue";
		@Nonnull public static final String BREAK = "break";
		@Nonnull public static final String LOG_DEBUG = "logdebug";
		@Nonnull public static final String LOG_ERROR = "logerror";
		@Nonnull public static final String LOG_INFO = "loginfo";
		@Nonnull public static final String LOG_WARN = "logwarn";
		@Nonnull public static final String VAR = "var";
		@Nonnull public static final String LOS_CLOSE = "%]";
		@Nonnull public static final String LAMBDA_ARROW = "->";
		@Nonnull public static final String BRACE_OPEN = "{";
		@Nonnull public static final String BRACE_CLOSE = "}";
		@Nonnull public static final String PAREN_OPEN = "(";
		@Nonnull public static final String PAREN_CLOSE = ")";
		@Nonnull public static final String BRACKET_OPEN = "[";
		@Nonnull public static final String BRACKET_CLOSE = "]";
		@Nonnull public static final String QUESTION_MARK = "?";
		@Nonnull public static final String ANGLE_OPEN = "<";
		@Nonnull public static final String ANGLE_CLOSE = ">";
		@Nonnull public static final String DOT = ".";
		@Nonnull public static final String COMMA = ",";
		@Nonnull public static final String EQUAL = "=";
		@Nonnull public static final String COLON = ":";
		@Nonnull public static final String SEMI_COLON = ";";
		@Nonnull public static final String SCOPE_SEPARATOR = "::";
		@Nonnull public static final String ENHANCED_FOR_LOOP_SEPARATOR = "in";
		@Nonnull public static final String LINEFEED = "\n";
		@Nonnull public static final String INDENT = "  ";
		@Nonnull public static final String TRUE = "true";
		@Nonnull public static final String FALSE = "false";
		@Nonnull public static final String NULL = "null";
		@Nonnull public static final String NATIVE_CODE = "'[native code]'";
		@Nonnull public static final String FAILED_TO_UNPARSE_LAMBDA = "'[error during unparsing]'";
		@Nonnull public static final String QUOTE = "\"";
		@Nonnull public static final String SINGLE_LINE_COMMENT_START = "//";
		@Nonnull public static final String MULTI_LINE_COMMENT_START = "/*";
		@Nonnull public static final String MULTI_LINE_COMMENT_END = "*/";
		@Nonnull public static final String TRIPLE_DOT = "...";
		@Nonnull public static final String BOOLEAN = "boolean";
		@Nonnull public static final String STRING = "string";
		@Nonnull public static final String NUMBER = "number";
		@Nonnull public static final String ARRAY = "array";
		@Nonnull public static final String HASH = "hash";
		@Nonnull public static final String REGEX = "regex";
		@Nonnull public static final String ERROR = "error";
		@Nonnull public static final String METHOD = "method";
		@Nonnull public static final String OBJECT = "object";
	}

	public static final class Error {
		private Error() {}
		@Nonnull public static final String ARRAY_INDEX_OUT_OF_BOUNDS = "Index {0} out of bounds for array {1}.";
		@Nonnull public static final String BREAK_CLAUSE = "Break {0} used outside of loop or switch, or label does not match any loop or switch.";
		@Nonnull public static final String CANNOT_ACQUIRE_EVALUATION_CONTEXT = "Failed to acquire evaluation context.";
		@Nonnull public static final String COERCION = "Object {0} of class {1} cannot be coerced to {2}.";
		@Nonnull public static final String CONTINUE_CLAUSE = "Continue {0} used outside of loop or switch, or label does not match any loop or switch.";
		@Nonnull public static final String CUSTOM_RUNTIME_EXCEPTION = "Custom Exception: {0}";
		@Nonnull public static final String EMBEDMENT_OUPTUT = "Failed to write output due to an I/O exception.";
		@Nonnull public static final String ILLEGAL_ARGUMENT_VALUE = "Illegal value {0} for argument {1} of function {2}: {3}.";
		@Nonnull public static final String ILLEGAL_THIS_CONTEXT = "Provided this context <{0}> of type {1} does not match the expected type {2} for function <{3}>.";
		@Nonnull public static final String INVALID_REGEX_PATTERN = "Invalid regex pattern {0} and/or flags {1}.";
		@Nonnull public static final String ITERATION_NOT_SUPPORTED = "Object {0} of type {1} does not support iteration.";
		@Nonnull public static final String MATH_DIVISION_BY_ZERO = "Division by zero: {0} / {1}";
		@Nonnull public static final String MATH = "Error during math operation: {0}";
		@Nonnull public static final String NESTING_LEVEL_TOO_DEEP = "Nesting level too deep: {0}.";
		@Nonnull public static final String NO_SUCH_FUNCTION_1 = "No such {0} named {1}.";
		@Nonnull public static final String NO_SUCH_FUNCTION_2 = "No such {0} named {1} for object {2} of type {3}.";
		@Nonnull public static final String NO_SUCH_METHOD = "{0}({1})";
		@Nonnull public static final String OPERATION_NOT_YET_IMPLEMENTED = "Operation {0} has not yet been implemented yet.";
		@Nonnull public static final String RETURN_CLAUSE = "Return clause used outside a function.";
		@Nonnull public static final String STRING_INDEX_OUT_OF_BOUNDS = "Index {0} out of bounds for string {1}";
		@Nonnull public static final String VARIABLE_NOT_DEFINED_LOCAL = "Variable {0} not resolvable to a defined variable.";
		@Nonnull public static final String VARIABLE_NOT_DEFINED_SCOPED = "Scoped variable {0}::{1} not resolvable to a defined variable.";

		@Nonnull public static final String INTERNAL_ERROR = "This is likely an error with the program. Contact support.";
		@Nonnull public static final String VARIABLE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		@Nonnull public static final String EXTERNAL_SCOPE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		@Nonnull public static final String MANUAL_SCOPE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_LVALUE_FUNCTION = "Encountered illegal LVALUE (function call) {0} in {1} at line {2}, column {3}.";
		@Nonnull public static final String ILLEGAL_LVALUE = "Encountered illegal LVALUE {0} in {1} at line {2}, column {3}.";
		@Nonnull public static final String NODE_COUNT_NOT_ODD = "Node count is not odd: {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_NOT_EVEN = "Node count is not even: {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_AT_MOST = "Node can have at most {0} children, but it has {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_AT_LEAST = "Node must have at least {0} children, but it has {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_EXACTLY_ONE_OF = "Node must have exactly {0} or {1} children, not {2}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_EXACTLY = "Node must have exactly {0} children, not {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_COUNT_BETWEEN = "Node must have between {0} and {1} children, but it has {2}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INCORRECT_TYPE = "Node type is {0}, but expected {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_NULL_STRING = "String is null. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_IMPROPER_STRING_TERMINATION = "String {0} not terminated properly. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INVALID_STRING = "Encountered invalid string at line {0}, column {1}: {2}";
		@Nonnull public static final String NODE_NULL_REGEX = "Regex is null. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_IMPROPER_REGEX_TERMINATION = "Regex {0} not terminated properly. " + INTERNAL_ERROR;
		@Nonnull public static final String NODE_INVALID_REGEX = "Encountered invalid regex at line {0}, column {1}: {2}";
		@Nonnull public static final String NODE_INVALID_NUMBER = "Encountered invalid number {0} at line {1}, column {2}: {3}";
		@Nonnull public static final String NULL_EXTERNAL_CONTEXT = "External context must not be null";
		@Nonnull public static final String TOKEN_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Token iterator does not support removal.";
		@Nonnull public static final String INVALID_JUMP_TYPE = "Invalid jump type {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_PROPERTY_EXPRESSION = "Illegal enum constant {0} at ASTPropertyExpressionNode. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_ASSIGNMENT = "Illegal enum constant {0} at {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_SWITCH = "Illegal enum for switch : {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_EQUAL = "Unexptected enum for equal expression: {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ENUM_COMMENT = "Unexptected enum for comment: {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String CANNOT_EVALUATE_VARIABLE_TYPE_NODE = "ASTVariableTypeDeclarationNode cannot be evaluated. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_WRITER = "Writer must not be null";
		@Nonnull public static final String TODO = "TODO - not yet implemented. " + INTERNAL_ERROR;
		@Nonnull public static final String COERCION_TYPE_NOT_MATCHING = "Argument type and return class do not match";
		@Nonnull public static final String DEPRECATED_ALANGOBJECT_ITERATOR = "Do not iterate directly, use getIterable(IEvaluationContext).";
		@Nonnull public static final String FAILURE_UNPARSING_LAMBDA = "Failed to unparse lambda expression.";
		@Nonnull public static final String NUMBER_TOO_LONG_FOR_LONG = "Number too large or too small to be represented as a long: {0}.";
		@Nonnull public static final String NUMBER_TOO_LONG_FOR_INT = "Number too large or too small to be represented as an int: {0}.";
		@Nonnull public static final String NUMBER_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for NumberLangObject::iterator.";
		@Nonnull public static final String STRING_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for StringLangObject::iterator.";
		@Nonnull public static final String NULL_MAP = "Map must not be null";
		@Nonnull public static final String ILLEGAL_STATE_NAMESPACE_BUILDER = "All expression method, attribute accessors, and attribute assigners must be set.";
		@Nonnull public static final String ILLEGAL_STATE_EC_BUILDER_BINDING = "Binding not set.";
		@Nonnull public static final String ILLEGAL_STATE_EC_BUILDER_SCOPE = "Scope not set.";
		@Nonnull public static final String CANNOT_UNNEST_GLOBAL_BINDING = "Cannot unnest global binding. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ARGUMENTS_FOR_DOCUMENT_COMMAND = "Document command of type {0} requires {1} arguments, but {2} were given: {3}.";
		@Nonnull public static final String NULL_TYPE = "Type must not be null.";
		@Nonnull public static final String INVALID_HTML_TEMPLATE = "Invalid html.";
		@Nonnull public static final String UNKNOWN_COMMAND_FOR_HTML_CONTEXT = "Command {0}.{1} cannot be processed by AHtmlExternalContext.";
		@Nonnull public static final String UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT = "Command {0}.{1} cannot be processed by SystemOutExternalContext.";
		@Nonnull public static final String NULL_CHILD_NODE = "Child node must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_METHOD = "Method must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_NODE_INTERNAL = "Node must not be null." + INTERNAL_ERROR;
		@Nonnull public static final String NULL_NODE = "Node must not be null.";
		@Nonnull public static final String NULL_PARSER_CONFIG = "Parser config must not be null.";
		@Nonnull public static final String PARSER_RETURNED_NULL_NODE = "Failed to parse code: parser returned null node.";
		@Nonnull public static final String FUNCTION_MISSING_RETURN_TYPE = "Functions must declare their return type.";
		@Nonnull public static final String INVALID_EQUAL_METHOD = "Invalid enum for equal method: {0}.";
		@Nonnull public static final String INVALID_COMPARISON_METHOD = "Invalid enum for comparison method: {0}";
		@Nonnull public static final String NULL_ERROR_VARIABLE_NAME = "Catch clause must specifiy a variable name. " + INTERNAL_ERROR;
		@Nonnull public static final String EC_POOL_RETURNED_NULL = "Pool returned null evaluation context.";
		@Nonnull public static final String EC_FACTORY_RETURNED_NULL = "Factory returned null evaluation context.";
		@Nonnull public static final String EXPRESSION_METHOD_NULL_INTERNAL = "Expression method must not be null here. " + INTERNAL_ERROR;
		@Nonnull public static final String NOT_A_COMMENT_TOKEN = "Token kind is not a comment token, but {0}.";
		@Nonnull public static final String VARIABLE_DECLARATION_AT_GLOBAL_SCOPE = "Encountered variable declaration for {0} at global scope. Global variable must be declared in a global block.";
		@Nonnull public static final String VARIABLE_ALREADY_DECLARED_IN_NESTING_LEVEL = "Variable {0} was already declared at the current nesting level.";
		@Nonnull public static final String VARIABLE_USED_BEFORE_DECLARED = "Variable {0} was not declared. Variables must be declared before they are used in strict mode.";
		@Nonnull public static final String VARIABLE_NOT_RESOLVABLE = "Variable {0} cannot be resolved to a defined variable.";
		@Nonnull public static final String SCOPE_MISSING_VARIABLE = "Scope {0} does not define the variable {1}.";
		@Nonnull public static final String BAD_AST_PARENT_CHILD = "Bad AST - chhildren of parent do not contains this node. " + INTERNAL_ERROR;
		@Nonnull public static final String FUNCTION_NAME_ALREADY_DEFINED = "Function name {0} already exists as a global variable.";
		@Nonnull public static final String NODE_WITHOUT_CHILDREN = "Node does not have any children.";
		@Nonnull public static final String WITH_CLAUSE_NODE_SCOPE_OUT_OF_BOUNDS = "Cannot access scope name {0}, with clause node contains only {1} scopes.";
		@Nonnull public static final String DUPLICATE_FUNCTION_ARGUMENT = "Duplicate argument {0} encountered in function parameters.";
		@Nonnull public static final String ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION = "Scope definition {0} cannot occur in the main program. " + INTERNAL_ERROR;
		@Nonnull public static final String MISSING_EXTERNAL_CONTEXT = "Evaluation context is missing external context, but it is required. " + INTERNAL_ERROR;
		@Nonnull public static final String SCOPED_VARIABLE_NOT_RESOLVED = "Variable {0}::{1} is not resolved. " + INTERNAL_ERROR;
		@Nonnull public static final String UNSCOPED_VARIABLE_NOT_RESOLVED = "Variable {0} is not resolved. " + INTERNAL_ERROR;
		@Nonnull public static final String ASSIGNMENT_OF_EXTERNALLY_SCOPED_VARIABLE = "Variable {0}::{1} belongs to an external scope and cannot be assigned to.";
		@Nonnull public static final String DUPLICATE_REQUIRE_SCOPE = "External scope {0} was already required previously.";
		@Nonnull public static final String MANUAL_SCOPE_ALREADY_REQUIRED = "Cannot define manual scope {0}, it was already required previously.";
		@Nonnull public static final String VARIABLE_SOURCE_ALREADY_RESOLVED = "Illegal attempt to resolve variable {0} to {1}; it was already resolved to {2}. " + INTERNAL_ERROR;
		@Nonnull public static final String VARIABLE_SCOPE_ALREADY_RESOLVED = "Illegal attempt to resolve the scope of variable {0}, it was already resolved to scope {1}. " + INTERNAL_ERROR;
		@Nonnull public static final String DUPLICATE_LABEL = "Duplicate label {0} is already in scope.";
		@Nonnull public static final String HEADER_ASSIGNMENT_NOT_COMPILE_TIME_CONSTANT = "Illegal assignment for {0}. Assignment in header definitions must be compile-time constant.";
		@Nonnull public static final String SEMANTIC_PARSE_EXCEPTION = "Error during parsing at line {0}, column {1}: {2}";
		@Nonnull public static final String DUPLICATE_SCOPED_VARIABLE = "Variable {0} from scope {1} was already declared previously.";
		@Nonnull public static final String SCOPED_FUNCTION_OUTSIDE_HEADER = "Occurence of function {0}::{1} at top level. Scoped function must be defined in a scope block in strict mode.";
		@Nonnull public static final String UNDEFINED_EMBEDMENT = "Embedment {0} is not defined.";
		@Nonnull public static final String VAR_ARGS_WITHOUT_ARGUMENTS = "Function cannot have varArgs without any arguments. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_ARGUMENT_COUNT = "Function requires {0} parameters, but {1} were given.";
		@Nonnull public static final String NULL_VARIABLE_TYPE = "Variable type must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String ILLEGAL_VARIABLE_TYPE_AT_EVALUATION = "Variable type declaration {0} cannot occur in the main program. " + INTERNAL_ERROR;
		@Nonnull public static final String NOT_A_SIMPLE_TYPE = "Lang object type {0} is not a simple type. " + INTERNAL_ERROR;
		@Nonnull public static final String NOT_A_COMPOUND_TYPE = "Lang object type {0} is not a compound type. " + INTERNAL_ERROR;
		@Nonnull public static final String NULL_LANG_OBJECT_TYPE = "Basic lang object type must not be null. " + INTERNAL_ERROR;
		@Nonnull public static final String MISSING_TYPE_DECLARATION = "Variable {0} without type declaration. Type declarations are required in strict mode.";
		@Nonnull public static final String EXTERNAL_SOURCE_FOR_MANUAL_VARIABLE = "Manually declared variable resolved to external scope. " + INTERNAL_ERROR;
		@Nonnull public static final String GET_TYPE_NODE_CALLED_BUT_NO_TYPE_NODE_PRESENT = "No type node present, use hasType() to check. " + INTERNAL_ERROR;
		@Nonnull public static final String INCOMPATIBLE_SIMPLE_TYPES = "Incompatible simple types.";
		@Nonnull public static final String INCOMPATIBLE_GENERIC_TYPES = "Incompatible generic types.";
		@Nonnull public static final String INCOMPATIBLE_VARIABLE_TYPES = "Found variable type {0}, but expected {1}: {2}";
		@Nonnull public static final String CONDITION_MUST_BE_BOOLEAN = "Condition must evaluate to boolean type.";
		@Nonnull public static final String UNREACHABLE_CODE = "Unreachable code, previous code never completes normally.";
		@Nonnull public static final String ILLEGAL_NODE_DURING_TYPECHECKING = "Illegal node during variable type checking: {0}. " + INTERNAL_ERROR;
		@Nonnull public static final String INCOMPATIBLE_TYPE_IN_FOR_HEADER = "Iterable value cannot be assigned to the iteration variable.";
		@Nonnull public static final String TYPE_NOT_ITERABLE = "Variable type {0} is not iterable.";
		@Nonnull public static final String INCOMPATIBLE_SWITCH_CASE_TYPE = "Switch case type must be compatible with the value switched on in strict mode.";
		@Nonnull public static final String JUMP_WITHOUT_MATCHING_LABEL_OR_FUNCTION = "Jump clause used without label or enclosing function.";
		@Nonnull public static final String TRACER_KNOWN_POSITION = "at {0} (line {1}, column {2})";
		@Nonnull public static final String TRACER_UNKNOWN_POSITION = "at unknown";
		@Nonnull public static final String EVALUATION_EXCEPTION_KNOWN_EC = "Evaluation context is {0} ({1}).";
		@Nonnull public static final String EVALUATION_EXCEPTION_UNKNOWN_EC = "Evaluation context is unknown.";
		@Nonnull public static final String EVALUATION_EXCEPTION_KNOWN_EX = "External context is {0} ({1}).";
		@Nonnull public static final String EVALUATION_EXCEPTION_UNKNOWN_EX = "External context is unknown.";
		@Nonnull public static final String NO_SUCH_SCOPE = "No such scope {0}.";
		@Nonnull public static final String MISSING_REQUIRE_SCOPE_STATEMENT = "Scope {0} is provided by the context, but require scope statement is missing. Strict mode requires importing scopes explicitly.";
		@Nonnull public static final String MISSING_EXPLICIT_RETURN = "Function must explicitly return a value of type {0} for all code paths in strict mode.";
		@Nonnull public static final String INCOMPATIBLE_FUNCTION_RETURN_TYPE = "Type returned is not compatible with the declared return type.";
		@Nonnull public static final String NULL_MAP_VALUE = "Map must not contain any null value.";
		@Nonnull public static final String INCOMPATIBLE_VARIABLE_ASSIGNMENT_TYPE = "Variable {0} cannot be assigned to this type.";
		@Nonnull public static final String INCOMPATIBLE_EXPRESSION_METHOD_TYPES = "Expression method {0} for type {1} does not accept the right hand side type {2}.";
	}

	public static final class ToString {
		private ToString() {}
		@Nonnull public static final String COLOR = "Color(%s)";
		@Nonnull public static final String STYLE = "Style(%s,%s,%s,%s)";
		@Nonnull public static final String BOOLEAN_TRUE = "true";
		@Nonnull public static final String BOOLEAN_FALSE = "false";
		@Nonnull public static final char[] INSPECT_A_LANG_OBJECT = NullUtil.checkNotNull("ALangObject@".toCharArray());
		@Nonnull public static final char[] INSPECT_ARRAY_LANG_OBJECT = NullUtil.checkNotNull("ArrayLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_BOOLEAN_LANG_OBJECT = NullUtil.checkNotNull("BooleanLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_EXCEPTION_LANG_OBJECT = NullUtil.checkNotNull("ExceptionLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_FUNCTION_LANG_OBJECT = NullUtil.checkNotNull("FunctionLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_HASH_LANG_OBJECT = NullUtil.checkNotNull("HashLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_NUMBER_LANG_OBJECT = NullUtil.checkNotNull("NumberLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_REGEX_LANG_OBJECT = NullUtil.checkNotNull("RegexLangObject".toCharArray());
		@Nonnull public static final char[] INSPECT_STRING_LANG_OBJECT = NullUtil.checkNotNull("StringLangObject".toCharArray());
		@Nonnull public static final String INSPECT_NULL_LANG_OBJECT = "NullLangObject";
		@Nonnull public static final String DOCUMENT_COMMAND = "%s(%s)";
		@Nonnull public static final String POSITIONED_DOCUMENT_COMMAND = "%s@%s(%s)";
	}

	public static final class Html {
		private Html() {}
		@Nonnull public static final String A = "a";
		@Nonnull public static final String HREF = "href";
		@Nonnull public static final String TARGET = "target";

	}
}