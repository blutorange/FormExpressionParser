package de.xima.fc.form.expression.util;

import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
@SuppressWarnings("nls")
public final class CmnCnst {
	private CmnCnst() {}

	// Used when constructing the stack trace.
	public static final String TRACER_POSITION_NAME_ANONYMOUS_FUNCTION = "anonymous function";
	public static final String TRACER_POSITION_NAME_GLOBAL = "main";

	public static final String SYSTEM_LOGGER_FORMAT = "[%s] (%s) %s %s";
	public static final Pattern PATTERN_FORM_FIELD_NAME = NullUtil.checkNotNull(Pattern.compile("^[a-zA-Z0-9_]+$"));

	public final static class Warning {
		private Warning(){}
		public static final String UNUSED_VARIABLE = "Variable {0} is never used.";
		public static final String EMPTY_STATEMENT = "Encountered an empty statement.";
		public static final String MISSING_FORM_FIELD = "Form field {0} does not exist in the current form version.";
	}

	public final static class NonnullConstant {
		private NonnullConstant() {}
		public static final Boolean BOOLEAN_TRUE = new Boolean(true);
		public static final Boolean BOOLEAN_FALSE = new Boolean(false);
		public static final String STRING_EMPTY = "";
		public static final String STRING_SPACE = " ";
		public static final String STRING_LF = "\n";
		public static final String STRING_CR = "\r";
		public static final String STRING_TAB = "\t";
		public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
		public static final int[] EMPTY_INT_ARRAY = new int[0];
		public static final Node[] EMPTY_NODE_ARRAY = new Node[0];
		public static final ALangObject[] EMPTY_SYMBOL_TABLE = new ALangObject[0];
		public static final String[] EMPTY_STRING_ARRAY = new String[0];
		public static final IVariableType[] EMPTY_VARIABLE_TYPE_ARRAY = new IVariableType[0];
		public static final EVariableTypeFlag[] EMPTY_VARIABLE_TYPE_FLAG_ARRAY = new EVariableTypeFlag[0];
		public static final String STRING_PATTERN_UNMATCHABLE = "(?!)";
		public static final String STRING_PATTERN_ALL_MATCHING = "";
		public static final Pattern PATTERN_UNMATCHABLE = NullUtil.checkNotNull(Pattern.compile(STRING_PATTERN_UNMATCHABLE));
		public static final Pattern PATTERN_ALL_MATCHING = NullUtil.checkNotNull(Pattern.compile(STRING_PATTERN_ALL_MATCHING));
	}

	public final static class CustomScope {
		private CustomScope() {}
		/** Scope with variables for form field variables/placeholders. */
		public static final String FORM_FIELD = "field";
		public static final String PREFIX_FORM_FIELD_SILENT = "[%@";
		public static final String PREFIX_FORM_FIELD_VERBOSE = "[%";
		/** Scope with variables for system variables/placeholders. */
		public static final String FC_SYSTEM = "fcsystem";
		public static final Object ACTION_VARIABLE_NAME_RESULT = "RESULT";
		public static final String PREFIX_FC_SYSTEM_SILENT = "[%$@";
		public static final String PREFIX_FC_SYSTEM_VERBOSE = "[%$";
		/** Scope with variables for system variables/placeholders. */
		public static final String TEMPLATE = "template";
		public static final String PREFIX_TEMPLATE_SILENT = "[%$$@";
		public static final String PREFIX_TEMPLATE_VERBOSE = "[%$$";
		public static final String GENERAL_NO_OUTPUT = "[%%";
		public static final String GENERAL_YES_OUTPUT = "[%%=";
		/** Scope with functions for retrieving form field values etc. */
		public static final String FORM_FUNCTIONS = "form";
		/** Scope for math-related utilities. */
		public static final String MATH = "math";
		public static final String MATH_PI = "pi";
		public static final String MATH_E = "e";
		/**
		 * Scope for document commands, manipulating the document. Usually used by templates.
		 * Document command names are loosely based on jQuery names.
		 */
		public static final String DOC = "doc";
		public static final String DOC_REMOVE_ENCLOSING_TAG = "unwrap";
		public static final String DOC_REMOVE_ENCLOSING_TAG_ARG0 = "tagName";
		public static final String DOC_OUTPUT = "write";
		public static final String DOC_OUTPUT_ARG0 = "dataToWrite";
		public static final String DOC_OUTPUT_NEWLINE = "writeln";
		public static final String DOC_OUTPUT_NEWLINE_ARG0 = "dataToWrite";
	}

	public final static class Name {
		private Name() {}
		public static final String ATTRIBUTE_ACCESSOR = "attribute accessor";
		public static final String DOT_ATTRIBUTE_ACCESSOR = "dot attribute accessor";
		public static final String BRACKET_ATTRIBUTE_ACCESSOR = "bracket attribute accessor";
		public static final String ATTRIBUTE_ASSIGNER = "attribute assigner";
		public static final String DOT_ATTRIBUTE_ASSIGNER = "dot attribute assigner";
		public static final String BRACKET_ATTRIBUTE_ASSIGNER = "bracket attribute assigner";
		public static final String METHOD = "method";
		public static final String PREFIX_OPERATION = "prefix operation";
		public static final String SUFFIX_OPERATION = "suffix operation";
		public static final String FOR_ITERATING_NODE = "ForIteratingLoopNode";
		public static final String FOR_PLAIN_NODE = "ForPlainLoopNode";
		public static final String ASSIGNMENT = "assignment";
		public static final String SYSTEM_LOGGER = "SystemLogger:";
		public static final String ARRAY = "array";
		public static final String ELEMENT = "element";
		public static final String KEY = "key";
		public static final String HASH = "hash";
		public static final String VALUE = "value";
		public static final String EMPTY_LABEL = "without label";
		public static final String DEFAULT_LOGGER_NAME = "Logger.%08X";
	}

	public final static class ExpressionMethod {
		private ExpressionMethod() {}
		public static final String PLUS = "+";
		public static final String DOUBLE_PLUS = "++";
		public static final String DASH = "-";
		public static final String DOUBLE_DASH = "--";
		public static final String STAR = "*";
		public static final String DOUBLE_STAR = "**";
		public static final String PERCENT = "%";
		public static final String SLASH = "/";
		public static final String CIRCUMFLEX = "^";
		public static final String EXCLAMATION = "!";
		public static final String EQUAL = "=";
		public static final String DOUBLE_EQUAL = "==";
		public static final String TRIPLE_EQUAL = "===";
		public static final String EXCLAMATION_EQUAL = "!=";
		public static final String EXCLAMATION_DOUBLE_EQUAL = "!==";
		public static final String BAR = "|";
		public static final String DOUBLE_BAR = "||";
		public static final String AMPERSAND = "&";
		public static final String DOUBLE_AMPERSAND = "&&";
		public static final String TILDE = "~";
		public static final String EXCLAMATION_TILDE = "!~";
		public static final String EQUAL_TILDE = "=~";
		public static final String PLUS_EQUAL = "+=";
		public static final String DASH_EQUAL = "-=";
		public static final String STAR_EQUAL = "*=";
		public static final String SLASH_EQUAL = "/=";
		public static final String PERCENT_EQUAL = "%=";
		public static final String DOUBLE_STAR_EQUAL = "**=";
		public static final String AMPERSAND_EQUAL = "&=";
		public static final String BAR_EQUAL = "|=";
		public static final String DOUBLE_BAR_EQUAL = "||=";
		public static final String DOUBLE_AMPERSAND_EQUAL = "&&=";
		public static final String ANGLE_OPEN_EQUAL = "<=";
		public static final String ANGLE_CLOSE_EQUAL = ">=";
		public static final String DOUBLE_ANGLE_OPEN_EQUAL = "<<=";
		public static final String TRIPLE_ANGLE_OPEN_EQUAL = "<<<=";
		public static final String DOUBLE_ANGLE_CLOSE_EQUAL = ">>=";
		public static final String TRIPLE_ANGLE_CLOSE_EQUAL = ">>>=";
		public static final String COERCE = "=>";
		public static final String DOT = ".";
		public static final String BRACKET = "[]";
		public static final String PARENTHESIS = "()";
		public static final String SWITCHCASE = "SWITCHCASE";
		public static final String SWITCHDEFAULT = "SWITCHDEFAULT";
		public static final String SWITCHCLAUSE = "SWITCHCLAUSE";
		public static final String ANGLE_OPEN = "<";
		public static final String ANGLE_CLOSE = ">";
		public static final String DOUBLE_ANGLE_OPEN = "<<";
		public static final String TRIPLE_ANGLE_OPEN = "<<<";
		public static final String DOUBLE_ANGLE_CLOSE = ">>";
		public static final String TRIPLE_ANGLE_CLOSE = ">>>";
	}

	public final static class Syntax {
		private Syntax() {}
		public static final String GLOBAL = "global";
		public static final String REQUIRE = "require";
		public static final String SCOPE = "scope";
		public static final String SWITCH = "switch";
		public static final String CASE = "case";
		public static final String DEFAULT = "default";
		public static final String FOR = "for";
		public static final String WHILE = "while";
		public static final String DO = "do";
		public static final String WITH = "with";
		public static final String EXCEPTION = "exception";
		public static final String THROW = "throw";
		public static final String TRY = "try";
		public static final String CATCH = "catch";
		public static final String IF = "if";
		public static final String ELSE = "else";
		public static final String FUNCTION = "function";
		public static final String RETURN = "return";
		public static final String CONTINUE = "continue";
		public static final String BREAK = "break";
		public static final String LOG_DEBUG = "logdebug";
		public static final String LOG_ERROR = "logerror";
		public static final String LOG_INFO = "loginfo";
		public static final String LOG_WARN = "logwarn";
		public static final String VAR = "var";
		public static final String LOS_CLOSE = "%]";
		public static final String LAMBDA_ARROW = "=>";
		public static final String BRACE_OPEN = "{";
		public static final String BRACE_CLOSE = "}";
		public static final String PAREN_OPEN = "(";
		public static final String PAREN_CLOSE = ")";
		public static final String BRACKET_OPEN = "[";
		public static final String BRACKET_CLOSE = "]";
		public static final String QUESTION_MARK = "?";
		public static final String ANGLE_OPEN = "<";
		public static final String ANGLE_CLOSE = ">";
		public static final String DOT = ".";
		public static final String COMMA = ",";
		public static final String EQUAL = "=";
		public static final String COLON = ":";
		public static final String SEMI_COLON = ";";
		public static final String SCOPE_SEPARATOR = "::";
		public static final String ENHANCED_FOR_LOOP_SEPARATOR = "in";
		public static final String LINEFEED = "\n";
		public static final String INDENT = "  ";
		public static final String TRUE = "true";
		public static final String FALSE = "false";
		public static final String NULL = "null";
		public static final String NATIVE_CODE = "'native code'";
		public static final String FAILED_TO_UNPARSE_LAMBDA = "'[error during unparsing]'";
		public static final String QUOTE_DOUBLE = "\"";
		public static final String QUOTE_SINGLE = "'";
		public static final String SINGLE_LINE_COMMENT_START = "//";
		public static final String MULTI_LINE_COMMENT_START = "/*";
		public static final String MULTI_LINE_COMMENT_END = "*/";
		public static final String TRIPLE_DOT = "...";
		public static final String BOOLEAN = "boolean";
		public static final String STRING = "string";
		public static final String NUMBER = "number";
		public static final String ARRAY = "array";
		public static final String HASH = "hash";
		public static final String REGEX = "regex";
		public static final String ERROR = "error";
		public static final String METHOD = "method";
		public static final String OBJECT = "object";
		public static final String VOID = "void";
		public static final String INLNE_EXPRESSION_OPEN = "${";
		public static final String INLNE_EXPRESSION_CLOSE = "}";
		public static final Object ESCAPE_LINEFEED = "\\n";
		public static final Object ESCAPE_CARRIAGE_RETURN = "\\r";
		public static final Object ESCAPE_TAB = "\\t";
	}

	public static final class Error {
		private Error() {}
		public static final String ARRAY_INDEX_OUT_OF_BOUNDS = "Index {0} out of bounds for array {1}.";
		public static final String BREAK_CLAUSE = "Break {0} used outside of loop or switch, or label does not match any loop or switch.";
		public static final String CANNOT_ACQUIRE_EVALUATION_CONTEXT = "Failed to acquire evaluation context.";
		public static final String COERCION = "Object {0} of class {1} cannot be coerced to {2}.";
		public static final String CONTINUE_CLAUSE = "Continue {0} used outside of loop or switch, or label does not match any loop or switch.";
		public static final String CUSTOM_RUNTIME_EXCEPTION = "Custom Exception: {0}";
		public static final String EMBEDMENT_OUPTUT = "Failed to write output due to an I/O exception.";
		public static final String ILLEGAL_ARGUMENT_VALUE = "Illegal value {0} for argument {1} of function {2}: {3}.";
		public static final String ILLEGAL_THIS_CONTEXT = "Provided this context {0} of type {1} does not match the expected type {2} for function <{3}>.";
		public static final String INVALID_REGEX_PATTERN = "Invalid regex pattern {0} and/or flags {1}.";
		public static final String ITERATION_NOT_SUPPORTED = "Object {0} of type {1} does not support iteration.";
		public static final String MATH_DIVISION_BY_ZERO = "Division by zero: {0} / {1}";
		public static final String MATH = "Error during math operation: {0}";
		public static final String NESTING_LEVEL_TOO_DEEP = "Nesting level too deep: {0}.";
		public static final String NO_SUCH_FUNCTION_NO_THIS = "No such {0} named {1}.";
		public static final String NO_SUCH_FUNCTION_WITH_THIS = "No such {0} named {1} for object {2} of type {3}.";
		public static final String NO_SUCH_FUNCTION_WITH_NULL = "Null pointer exception: No such {0} named {1} for null type.";
		public static final String NO_SUCH_METHOD = "{0}({1})";
		public static final String OPERATION_NOT_YET_IMPLEMENTED = "Operation {0} has not yet been implemented yet.";
		public static final String RETURN_CLAUSE = "Return clause used outside a function.";
		public static final String STRING_INDEX_OUT_OF_BOUNDS = "Index {0} out of bounds for string {1}";
		public static final String VARIABLE_NOT_DEFINED_LOCAL = "Variable {0} not resolvable to a defined variable.";
		public static final String VARIABLE_NOT_DEFINED_SCOPED = "Scoped variable {0}::{1} not resolvable to a defined variable.";

		public static final String INTERNAL_ERROR = "This is likely an error with the program. Contact support.";
		public static final String VARIABLE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		public static final String EXTERNAL_SCOPE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		public static final String MANUAL_SCOPE_NODE_NULL_NAME = "Name is null. " + INTERNAL_ERROR;
		public static final String ILLEGAL_LVALUE_FUNCTION = "Encountered illegal LVALUE (function call) {0} in {1}.";
		public static final String ILLEGAL_LVALUE = "Encountered illegal LVALUE {0} in {1}.";
		public static final String NODE_COUNT_NOT_ODD = "Node count is not odd: {0}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_NOT_EVEN = "Node count is not even: {0}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_AT_MOST = "Node can have at most {0} children, but it has {1}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_AT_LEAST = "Node must have at least {0} children, but it has {1}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_EXACTLY_ONE_OF = "Node must have exactly {0} or {1} children, not {2}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_EXACTLY = "Node must have exactly {0} children, not {1}. " + INTERNAL_ERROR;
		public static final String NODE_COUNT_BETWEEN = "Node must have between {0} and {1} children, but it has {2}. " + INTERNAL_ERROR;
		public static final String NODE_INCORRECT_TYPE = "Node type is {0}, but expected {1}. " + INTERNAL_ERROR;
		public static final String NODE_NULL_STRING = "String is null. " + INTERNAL_ERROR;
		public static final String NODE_IMPROPER_STRING_TERMINATION = "String {0} not terminated properly. " + INTERNAL_ERROR;
		public static final String NODE_INVALID_STRING = "Encountered invalid string: {0}";
		public static final String NODE_NULL_REGEX = "Regex is null. " + INTERNAL_ERROR;
		public static final String NODE_IMPROPER_REGEX_TERMINATION = "Regex {0} not terminated properly. " + INTERNAL_ERROR;
		public static final String NODE_INVALID_REGEX = "Encountered invalid regex: {0}";
		public static final String NODE_INVALID_NUMBER = "Encountered invalid number {0} at line {1}, column {2}: {3}";
		public static final String NULL_EXTERNAL_CONTEXT_OBJECT = "Object for the external context must not be null";
		public static final String TOKEN_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Token iterator does not support removal.";
		public static final String INVALID_JUMP_TYPE = "Invalid jump type {0}. " + INTERNAL_ERROR;
		public static final String CANNOT_EVALUATE_VARIABLE_TYPE_NODE = "ASTVariableTypeDeclarationNode cannot be evaluated. " + INTERNAL_ERROR;
		public static final String NULL_WRITER = "Writer must not be null";
		public static final String TODO = "TODO - not yet implemented. " + INTERNAL_ERROR;
		public static final String COERCION_TYPE_NOT_MATCHING = "Argument type and return class do not match";
		public static final String DEPRECATED_ALANGOBJECT_ITERATOR = "Do not iterate directly, use getIterable(IEvaluationContext).";
		public static final String FAILURE_UNPARSING_LAMBDA = "Failed to unparse lambda expression.";
		public static final String NUMBER_TOO_LONG_FOR_LONG = "Number too large or too small to be represented as a long: {0}.";
		public static final String NUMBER_TOO_LONG_FOR_INT = "Number too large or too small to be represented as an int: {0}.";
		public static final String NUMBER_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for NumberLangObject::iterator.";
		public static final String STRING_ITERATOR_DOES_NOT_SUPPORT_REMOVAL = "Removal not supported for StringLangObject::iterator.";
		public static final String NULL_MAP = "Map must not be null";
		public static final String ILLEGAL_STATE_NAMESPACE_BUILDER = "All expression method, attribute accessors, and attribute assigners must be set.";
		public static final String ILLEGAL_STATE_EC_BUILDER_BINDING = "Binding not set.";
		public static final String ILLEGAL_STATE_EC_BUILDER_SCOPE = "Scope not set.";
		public static final String CANNOT_UNNEST_GLOBAL_BINDING = "Cannot unnest global binding. " + INTERNAL_ERROR;
		public static final String ILLEGAL_ARGUMENTS_FOR_DOCUMENT_COMMAND = "Document command of type {0} requires {1} arguments, but {2} were given: {3}.";
		public static final String NULL_TYPE = "Type must not be null.";
		public static final String INVALID_HTML_TEMPLATE = "Invalid html.";
		public static final String UNKNOWN_COMMAND_FOR_HTML_CONTEXT = "Command {0}.{1} cannot be processed by AHtmlExternalContext.";
		public static final String UNKNOWN_COMMAND_FOR_SYSTEM_OUT_CONTEXT = "Command {0}. {1} cannot be processed by SystemOutExternalContext.";
		public static final String NULL_CHILD_NODE = "Child node must not be null. " + INTERNAL_ERROR;
		public static final String NULL_METHOD = "Method must not be null. " + INTERNAL_ERROR;
		public static final String NULL_NODE_INTERNAL = "Node must not be null." + INTERNAL_ERROR;
		public static final String NULL_NODE = "Node must not be null.";
		public static final String NULL_PARSER_CONFIG = "Parser config must not be null.";
		public static final String PARSER_RETURNED_NULL_NODE = "Failed to parse code: parser returned null node.";
		public static final String FUNCTION_MISSING_RETURN_TYPE = "Functions must declare their return type.";
		public static final String INVALID_EQUAL_METHOD = "Invalid enum for equal method: {0}.";
		public static final String INVALID_COMPARISON_METHOD = "Invalid enum for comparison method: {0}";
		public static final String NULL_ERROR_VARIABLE_NAME = "Catch clause must specifiy a variable name. " + INTERNAL_ERROR;
		public static final String EC_POOL_RETURNED_NULL = "Pool returned null evaluation context.";
		public static final String EC_FACTORY_RETURNED_NULL = "Factory returned null evaluation context.";
		public static final String EXPRESSION_METHOD_NULL_INTERNAL = "Expression method must not be null here. " + INTERNAL_ERROR;
		public static final String NOT_A_COMMENT_TOKEN = "Token kind is not a comment token, but {0}.";
		public static final String VARIABLE_DECLARATION_AT_GLOBAL_SCOPE = "Encountered variable declaration for {0} at global scope. Global variable must be declared in a global block.";
		public static final String VARIABLE_ALREADY_DECLARED_IN_NESTING_LEVEL = "Variable {0} was already declared at the current nesting level.";
		public static final String VARIABLE_USED_BEFORE_DECLARED = "Variable {0} was not declared. Variables must be declared before they are used in strict mode.";
		public static final String VARIABLE_NOT_RESOLVABLE = "Variable {0} cannot be resolved to a defined variable.";
		public static final String SCOPE_MISSING_VARIABLE = "Scope {0} does not define the variable {1}.";
		public static final String BAD_AST_PARENT_CHILD = "Bad AST - chhildren of parent do not contains this node. " + INTERNAL_ERROR;
		public static final String FUNCTION_NAME_ALREADY_DEFINED = "Function name {0} already exists as a global variable.";
		public static final String NODE_WITHOUT_CHILDREN = "Node does not have any children.";
		public static final String WITH_CLAUSE_NODE_SCOPE_OUT_OF_BOUNDS = "Cannot access scope name {0}, with clause node contains only {1} scopes.";
		public static final String DUPLICATE_FUNCTION_ARGUMENT = "Duplicate argument {0} encountered in function parameters.";
		public static final String ILLEGAL_SCOPE_DEFINITIONS_AT_EVALUATION = "Scope definition {0} cannot occur in the main program. " + INTERNAL_ERROR;
		public static final String MISSING_EXTERNAL_CONTEXT = "Evaluation context is missing external context, but it is required. " + INTERNAL_ERROR;
		public static final String SCOPED_VARIABLE_NOT_RESOLVED = "Variable {0}::{1} is not resolved. " + INTERNAL_ERROR;
		public static final String UNSCOPED_VARIABLE_NOT_RESOLVED = "Variable {0} is not resolved. " + INTERNAL_ERROR;
		public static final String ASSIGNMENT_OF_UNASSIGNABLE_VARIABLE = "Variable {0} of type {1} cannot be assigned to, most likely because it is a variable from an external scope.";
		public static final String DUPLICATE_REQUIRE_SCOPE = "External scope {0} was already required previously.";
		public static final String MANUAL_SCOPE_ALREADY_REQUIRED = "Cannot define manual scope {0}, it was already required previously.";
		public static final String VARIABLE_SOURCE_ALREADY_RESOLVED = "Illegal attempt to resolve variable {0} to {1}; it was already resolved. " + INTERNAL_ERROR;
		public static final String VARIABLE_SCOPE_ALREADY_RESOLVED = "Illegal attempt to resolve the scope of variable {0}, it was already resolved to scope {1}. " + INTERNAL_ERROR;
		public static final String DUPLICATE_LABEL = "Duplicate label {0} is already in scope.";
		public static final String HEADER_ASSIGNMENT_NOT_COMPILE_TIME_CONSTANT = "Illegal assignment for {0}. Assignment in header definitions must be compile-time constant.";
		public static final String SEMANTIC_PARSE_EXCEPTION = "Error during parsing at line {0}, column {1}: {2}";
		public static final String DUPLICATE_SCOPED_VARIABLE = "Variable {0} from scope {1} was already declared previously.";
		public static final String SCOPED_FUNCTION_OUTSIDE_HEADER = "Occurence of function {0}::{1} at top level. Scoped function must be defined in a scope block in strict mode.";
		public static final String UNDEFINED_EMBEDMENT = "Embedment {0} is not defined.";
		public static final String VAR_ARGS_WITHOUT_ARGUMENTS = "Function cannot have varArgs without any arguments. " + INTERNAL_ERROR;
		public static final String ILLEGAL_ARGUMENT_COUNT = "Function requires {0} parameter(s), but {1} were given.";
		public static final String ILLEGAL_VARARG_ARGUMENT_COUNT = "Varargs function requires {0} or more parameters, but {1} were given.";
		public static final String NULL_VARIABLE_TYPE = "Variable type must not be null. " + INTERNAL_ERROR;
		public static final String ILLEGAL_VARIABLE_TYPE_AT_EVALUATION = "Variable type declaration {0} cannot occur in the main program. " + INTERNAL_ERROR;
		public static final String NOT_A_SIMPLE_TYPE = "Lang object type {0} is not a simple type. " + INTERNAL_ERROR;
		public static final String NOT_A_COMPOUND_TYPE = "Lang object type {0} is not a compound type. " + INTERNAL_ERROR;
		public static final String NULL_LANG_OBJECT_TYPE = "Basic lang object type must not be null. " + INTERNAL_ERROR;
		public static final String MISSING_TYPE_DECLARATION = "Variable {0} without type declaration. Type declarations are required in strict mode.";
		public static final String EXTERNAL_SOURCE_FOR_MANUAL_VARIABLE = "Manually declared variable resolved to external scope. " + INTERNAL_ERROR;
		public static final String GET_TYPE_NODE_CALLED_BUT_NO_TYPE_NODE_PRESENT = "No type node present, use hasType() to check. " + INTERNAL_ERROR;
		public static final String INCOMPATIBLE_SIMPLE_TYPES = "Incompatible simple types.";
		public static final String INCOMPATIBLE_GENERIC_TYPES = "Incompatible generic types.";
		public static final String INCOMPATIBLE_VARIABLE_TYPES_KNOWN_SHOULD = "Found incompatible variable type {0} but expected {1}: {2}";
		public static final String INCOMPATIBLE_VARIABLE_TYPES_UNKNOWN_SHOULD = "Found incompatible variable type {0}: {1}";
		public static final String CONDITION_MUST_BE_BOOLEAN = "Condition must evaluate to boolean type.";
		public static final String UNREACHABLE_CODE = "Unreachable code, previous code never completes normally.";
		public static final String ILLEGAL_NODE_DURING_TYPECHECKING = "Illegal node during variable type checking: {0}. " + INTERNAL_ERROR;
		public static final String INCOMPATIBLE_TYPE_IN_FOR_HEADER = "Iterable value cannot be assigned to the iteration variable.";
		public static final String INCOMPATIBLE_SWITCH_CASE_TYPE = "Switch case type must be compatible with the value switched on in strict mode.";
		public static final String JUMP_WITHOUT_MATCHING_LABEL_OR_FUNCTION = "Jump clause used without label or enclosing function.";
		public static final String TRACER_KNOWN_POSITION = "at {0} (line {1}, column {2})";
		public static final String TRACER_UNKNOWN_POSITION = "at unknown";
		public static final String EVALUATION_EXCEPTION_NAMESPACE = "Namespace is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_LIBRARY = "Library is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_TRACER = "Tracer is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_LOGGER = "Logger is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_EMBEDMENT = "Embedment is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_KNOWN_EC = "Evaluation context is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_UNKNOWN_EC = "Evaluation context is unknown.";
		public static final String EVALUATION_EXCEPTION_KNOWN_EX = "External context is {0} ({1}).";
		public static final String EVALUATION_EXCEPTION_UNKNOWN_EX = "External context is unknown.";
		public static final String NO_SUCH_SCOPE = "No such scope {0}.";
		public static final String MISSING_REQUIRE_SCOPE_STATEMENT = "Scope {0} is provided by the context, but require scope statement is missing. Strict mode requires importing scopes explicitly.";
		public static final String MISSING_EXPLICIT_RETURN = "Function must explicitly return a value of type {0} for all code paths in strict mode.";
		public static final String INCOMPATIBLE_FUNCTION_RETURN_TYPE = "Type returned is not compatible with the declared return type.";
		public static final String NULL_MAP_VALUE = "Map must not contain any null value.";
		public static final String INCOMPATIBLE_VARIABLE_ASSIGNMENT_TYPE = "Variable {0} cannot be assigned to this type.";
		public static final String INCOMPATIBLE_VARIABLE_CONVERSION_TYPE = "Type cannot be upconverted to this type.";
		public static final String INCONSISTENT_CLASS_HIERARCHY = "Inconsistent class hierarchy: {0} < {1}";
		public static final String NOT_A_FUNCTION = "A function type is required.";
		public static final String INCOMPATIBLE_FUNCTION_PARAMETER_TYPE = "Function parameter type not compatible.";
		public static final String UNBOUND_FUNCTION_CALL = "No this context bound to called function {0}. " + INTERNAL_ERROR;
		public static final String INCOMPATIBLE_VOID_RETURN_TYPE = "Void function must not return a value.";
		public static final String TOKEN_MGR_ERROR = "Lexical error at line {0}, column {1}. Encountered {2} after \"{3}\"";
		public static final String INCOMPATIBLE_EXPRESSION_METHOD_TYPES = "Expression method {0} for {1} does not accept this type on the right hand side.";
		public static final String INCOMPATIBLE_DOT_ASSIGNER_TYPES = "Dot assigner named {0} for {1} does not accept this type as the value to be assigned.";
		public static final String INCOMPATIBLE_BRACKET_ACCESSOR_TYPES = "Bracket accessor for type {0} does not accept values of this type for the property to be accessed.";
		public static final String INCOMPATIBLE_BRACKET_ASSIGNER_VALUE_TYPES = "Bracket assigner for type {0} does not accept values of this type to be assigned.";
		public static final String INCOMPATIBLE_BRACKET_ASSIGNER_PROPERTY_TYPES = "Bracket assigner for type {0} does not accept values of this type for the property to be assigned to.";
		public static final String NO_SUCH_EXPRESSION_METHOD = "No such expression method {0} for type {1}.";
		public static final String NO_SUCH_DOT_ACCESSOR_WITH_GENERICS = "No such dot accesor named <{0}>{1} for type {2}.";
		public static final String NO_SUCH_DOT_ACCESSOR_WITHOUT_GENERICS = "No such dot accesor named {0} for type {1}.";
		public static final String NO_SUCH_BRACKET_ACCESSOR = "No such bracket accesor for type {0}.";
		public static final String NO_SUCH_BRACKET_ASSIGNER = "No such bracket assigner for type {0}.";
		public static final String NO_SUCH_DOT_ASSIGNER = "No such dot assigner named {0} for type {1}.";
		public static final String INVALID_STRING_DELIMITER = "Delimiter must be either '' or \", but found {0}. " + INTERNAL_ERROR;
		public static final String UNMATCHING_STRING_DELIMITER = "Delimiter {0} does not match parent delimiter {1}. " + INTERNAL_ERROR;
		public static final String STRING_CONTAINS_DELIMITER = "String contains delimiter {0} at position {1}.";
		public static final String STRING_ENDS_ON_BACKSLASH = "String ends on a backslash.";
		public static final String TEMPLATE_LITERAL_CONTAINS_DOLLAR = "Template literal contains unescaped $ at position {0}.";
		public static final String EMBEDDED_BLOCK_NOT_ALLOWED = "Embedded blocks are not allowed.";
		public static final String FINAL_CODE_BLOCK_NOT_CLOSED = "Final code block in templates must be closed.";
		public static final String VAR_ARG_IN_NON_FINAL_PLACE = "Variable argument specifier allowed only for the final argument.";
		public static final String STRING_INVALID_UNICODE_HEX = "Invalid hex digit encountered in string unicode escape: {0}";
		public static final String STRING_UNFINISHED_UNICODE_ESCAPE = "String unicode escape must contain exactly four hex digits.";
		public static final String NULL_TOKEN_IMAGE = "Token image must not be null. " + INTERNAL_ERROR;
		public static final String CALL_ID_NOT_RESOLVED = "Call ID of function node {0} not resolved. " + INTERNAL_ERROR;
		public static final String UNHANDLED_ENUM = "Unhandled enum: {0}. " + INTERNAL_ERROR;
		public static final String UNHANDLED_NODE_TYPE = "Unhandled node type with id {0} and class {1}. " + INTERNAL_ERROR;
		public static final String NEGATIVE_CLASS_ID = "Class id {0} for {1} is negative.";
		public static final String METHOD_ALREADY_SET = "Method {0} already set for {1}.";
		public static final String CLASS_NOT_ITERABLE = "Class {0} is not iterable.";
		public static final String TYPE_NOT_ITERABLE = "Variable type {0} is not iterable.";
		public static final String INNER_TYPE_NOT_MATCHING = "Actual inner type {0} does not match declared inner type {1}.";
		public static final String RETURN_TYPE_NOT_MATCHING = "Basic variable type {0} does not match return type {1}.";
		public static final String NEGATIVE_CALL_ID = "Call ID must be non-negative, but it is {0}.";
		public static final String NO_MAPPING_FOUND = "No mapping for variable {0} with source {1}.";
		public static final String VARIABLE_WITH_ILLEGAL_SOURCE = "Variable with illegal source {0}. " + INTERNAL_ERROR;
		public static final String FUNCTION_INFO_NOT_SET = "Function info not set yet for {0}. " + INTERNAL_ERROR;
		public static final String NO_MAPPING_FROM_LOCAL_TO_CLOSURE = "No such mapping of local to closure source for variable {0}. " + INTERNAL_ERROR;
		public static final String CLOSURE_VARIABLE_LIMIT_EXCEEDED = "Closure variable count limit exceeded: {0}";
		public static final String ASSIGNMENT_NODE_EXISTS_ALREADY = "Cannot add assignment node as there exists one already. " + INTERNAL_ERROR;
		public static final String ILLEGAL_NODE_TYPE = "Expected child {0} to be of type {1}, but it is of type {2}. " + INTERNAL_ERROR;
		public static final String VARIABLE_USED_BEFORE_ASSIGNMENT = "Variable {0} was used before it was definitely assigned.";
		public static final String NO_MATCHING_LABEL_INFO = "Cannot find matching info for label {0}. " + INTERNAL_ERROR;
		public static final String DEFAULT_CASE_MUST_BE_LAST = "Default case of a switch statement must occur last.";
		public static final String DEFAULT_USED_MULTIPLE_TIMES = "Default case must not occur more than once.";
		public static final String ILLEGAL_GENERIC_IN_DOT_PROPERTY_ASSIGNMENT = "Attempt to use dot assigner {0} with generic specifiers.";
		public static final String TYPE_NODE_WITHOUT_IMPLICIT_TYPE = "Type node {0} did not return implicit type. " + INTERNAL_ERROR;
		public static final String VARIABLE_TYPE_CHECKER_VISITED_DOT_PROPERTY_NODE = "Visited dot property node. " + INTERNAL_ERROR;
	}

	public static final class ToString {
		private ToString() {}
		public static final String COLOR = "Color(%s)";
		public static final String STYLE = "Style(%s,%s,%s,%s)";
		public static final String BOOLEAN_TRUE = "true";
		public static final String BOOLEAN_FALSE = "false";
		public static final char[] INSPECT_A_LANG_OBJECT = NullUtil.checkNotNull("ALangObject@".toCharArray());
		public static final char[] INSPECT_ARRAY_LANG_OBJECT = NullUtil.checkNotNull("ArrayLangObject".toCharArray());
		public static final char[] INSPECT_BOOLEAN_LANG_OBJECT = NullUtil.checkNotNull("BooleanLangObject".toCharArray());
		public static final char[] INSPECT_EXCEPTION_LANG_OBJECT = NullUtil.checkNotNull("ExceptionLangObject".toCharArray());
		public static final char[] INSPECT_FUNCTION_LANG_OBJECT = NullUtil.checkNotNull("FunctionLangObject".toCharArray());
		public static final char[] INSPECT_HASH_LANG_OBJECT = NullUtil.checkNotNull("HashLangObject".toCharArray());
		public static final char[] INSPECT_NUMBER_LANG_OBJECT = NullUtil.checkNotNull("NumberLangObject".toCharArray());
		public static final char[] INSPECT_REGEX_LANG_OBJECT = NullUtil.checkNotNull("RegexLangObject".toCharArray());
		public static final char[] INSPECT_STRING_LANG_OBJECT = NullUtil.checkNotNull("StringLangObject".toCharArray());
		public static final String INSPECT_NULL_LANG_OBJECT = "NullLangObject";
		public static final String DOCUMENT_COMMAND = "%s(%s)";
		public static final String POSITIONED_DOCUMENT_COMMAND = "%s@%s(%s)";
		public static final String E_METHOD = "%s(%s)";
		public static final String HEADER_NODE_IMPL = "HeaderNodeImpl({0},source={1})";
		public static final String NODE_INFO = "(return:{0},implicit:{1},throws:{2},labels:{3})";
		public static final String LABEL_INFO = "forBreak:{0},forContinue:{1}";
	}

	public static final class Html {
		private Html() {}
		public static final String A = "a";
		public static final String HREF = "href";
		public static final String TARGET = "target";
	}
}