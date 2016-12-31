package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.parse.DuplicateFunctionArgumentException;
import de.xima.fc.form.expression.exception.parse.HeaderAssignmentNotCompileTimeConstantException;
import de.xima.fc.form.expression.exception.parse.IllegalJumpClauseException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableAssignmentException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleExpressionMethodTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleFunctionParameterTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVariableAssignmentTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVariableConversionTypeException;
import de.xima.fc.form.expression.exception.parse.IncompatibleVoidReturnTypeException;
import de.xima.fc.form.expression.exception.parse.IterationNotSupportedException;
import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.NoSuchBracketAccessorException;
import de.xima.fc.form.expression.exception.parse.NoSuchBracketAssignerException;
import de.xima.fc.form.expression.exception.parse.NoSuchDotAccessorException;
import de.xima.fc.form.expression.exception.parse.NoSuchDotAssignerException;
import de.xima.fc.form.expression.exception.parse.NoSuchExpressionMethodException;
import de.xima.fc.form.expression.exception.parse.ScopedFunctionOutsideHeaderException;
import de.xima.fc.form.expression.exception.parse.VariableDeclaredTwiceException;
import de.xima.fc.form.expression.exception.parse.VariableNotResolvableException;
import de.xima.fc.form.expression.exception.parse.VariableUsageBeforeDeclarationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.Cfg;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;

@SuppressWarnings("nls")
enum SyntaxFailure implements ITestCase {
	TEST001("if (i==0) { alert() }", "Encountered \"}\" at line 1, column 21."),
	TEST002("if(a) a; else b; else if(c) c;", "Encountered \"else\" at line 1, column 18."),
	TEST003("if(a) a; else if (c) c; else b; else c;", "Encountered \"else\" at line 1, column 33."),
	TEST004("with (me you) stuff;", "Encountered <Identifier> \"you\" at line 1, column 10."),
	TEST005("with (me::you) stuff;", "Encountered \"::\" at line 1, column 9."),
	TEST006("try { 1/0; } catch (e) { log(me) };","Encountered \"}\" at line 1, column 34."),
	TEST007("a()9;","Encountered <Integer> \"9\" at line 1, column 4."),
	TEST008("^|n};","Encountered \"^\" at line 1, column 1."),
	TEST009("with (foo bar) foobar;","Encountered <Identifier> \"bar\" at line 1, column 11."),
	TEST010("foo()%]", "Error during parsing at line 1, column 6: Embedded blocks are not allowed."),
	TEST011(new Cfg("foo=bar;/*unclosed").err("Lexical error at line 1, column 19. Encountered <EOF> after \"\"").err(TokenMgrError.class)),
	TEST012("#foobar#q;", "Encountered <Identifier> \"q\" at line 1, column 9."),
	TEST013("#(\\d+#", "Error during parsing at line 1, column 1: Encountered invalid regex: Unclosed group near index 4"),
	TEST014("42 = 42;", "Error during parsing at line 1, column 1: Encountered illegal LVALUE ASTNumberNode in assignment."),
	TEST015(new Cfg("\"\\\";").err("Lexical error at line 1, column 5. Encountered <EOF> after \"\"\\\";\"").err(TokenMgrError.class)),
	TEST016("++(1+2);", "Error during parsing at line 1, column 4: Encountered illegal LVALUE ASTExpressionNode in prefix operation."),
	TEST017("++a.b();", "Error during parsing at line 1, column 3: Encountered illegal LVALUE (function call) ASTPropertyExpressionNode(null,NONE(),1:3-1:7) in prefix operation."),
	TEST018("++--a;", "Error during parsing at line 1, column 3: Encountered illegal LVALUE ASTUnaryExpressionNode in prefix operation."),
	TEST019("if (true) require scope math;", "Encountered \"require\" at line 1, column 11."),
	TEST020("scoped::var.works['great']();", "Encountered \"var\" at line 1, column 9."),
	TEST021("a = var b = c;", "Encountered \"var\" at line 1, column 5."),
	TEST022(new Cfg("var i = i;").err("Error during parsing at line 1, column 1: Encountered variable declaration for i at global scope. Global variable must be declared in a global block.").err(IllegalVariableDeclarationAtGlobalScopeException.class)),
	TEST024("if(false){function foo(){}}", "Encountered \"function\" at line 1, column 11."),
	TEST025(new Cfg("global scope {var i=0;var j=i;}j;").err("Error during parsing at line 1, column 29: Illegal assignment for j. Assignment in header definitions must be compile-time constant.").err(HeaderAssignmentNotCompileTimeConstantException.class)),
	TEST026(new Cfg("scope myscope{var j=''.lower();}myscope::j;").err("Error during parsing at line 1, column 21: Illegal assignment for j. Assignment in header definitions must be compile-time constant.").err(HeaderAssignmentNotCompileTimeConstantException.class)),
	TEST027(new Cfg("if(true){var i=0;}else{var j=i+3;}").err("Error during parsing at line 1, column 30: Variable i cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST028(new Cfg("require scope math;math::pi=9;").err("Error during parsing at line 1, column 20: Variable pi of type LIBRARY cannot be assigned to, most likely because it is a variable from an external scope.").err(IllegalVariableAssignmentException.class)),
	TEST029(new Cfg("break;").err("Error during parsing at line 1, column 1: Break without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST030(new Cfg("continue;").err("Error during parsing at line 1, column 1: Continue without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST031(new Cfg("return;").err("Error during parsing at line 1, column 1: Return clause used outside a function.").err(IllegalJumpClauseException.class)),
	TEST032(new Cfg("while(true)break foo;").err("Error during parsing at line 1, column 12: Break foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST033(new Cfg("foo:while(true)break;").err("Error during parsing at line 1, column 16: Break without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST034(new Cfg("foo:while(true);while(true)break foo;").err("Error during parsing at line 1, column 28: Break foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST035(new Cfg("do{continue foo;}while(true);").err("Error during parsing at line 1, column 4: Continue foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST036(new Cfg("foo:for(i in 10)continue;").err("Error during parsing at line 1, column 17: Continue without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST037(new Cfg("foo:switch(true){};while(true)continue foo;").err("Error during parsing at line 1, column 31: Continue foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST038(new Cfg("if(true)var i = i;").err("Error during parsing at line 1, column 17: Variable i cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST039(new Cfg("function bar(){var baz;};function foo(){baz;}").err("Error during parsing at line 1, column 41: Variable baz cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST040(new Cfg("function foo(){arguments.length;}foo(1,2,3);").err("Error during parsing at line 1, column 16: Variable arguments cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST041(new Cfg("\"foo\\\";").err(TokenMgrError.class).err("Lexical error at line 1, column 8. Encountered <EOF> after \"\"foo\\\";\"")),
	TEST042(new Cfg("#(\\d#;").err(ParseException.class).err("Error during parsing at line 1, column 1: Encountered invalid regex: Unclosed group near index 3")),
	TEST043(new Cfg("''';").err("Lexical error at line 1, column 5. Encountered <EOF> after \"';\"").err(TokenMgrError.class)),
	TEST044(new Cfg("'\\';").err("Lexical error at line 1, column 5. Encountered <EOF> after \"\'\\\';\"").err(TokenMgrError.class)),
	TEST045(new Cfg("\"\"\";").err("Lexical error at line 1, column 5. Encountered <EOF> after \"\";\"").err(TokenMgrError.class)),
	TEST046(new Cfg("\"\\\";").err("Lexical error at line 1, column 5. Encountered <EOF> after \"\"\\\";\"").err(TokenMgrError.class)),
	TEST047(new Cfg("```;").err("Encountered \"`\" at line 1, column 3.").err(ParseException.class)),
	TEST048(new Cfg("`\\`;").err("Encountered <EOF> at line 1, column 4.").err(ParseException.class)),
	TEST049(new Cfg("`$`;").err("Lexical error at line 1, column 3. Encountered \"`\" after \"$\"").err(TokenMgrError.class)),
	TEST050("function foo(arg1,arg2,args...){}", "Encountered \"...\" at line 1, column 28"),
	TEST051("function foo(arg1,...arg2,args...){}", "Error during parsing at line 1, column 22: Variable argument specifier allowed only for the final argument."),
	TEST052(new Cfg("(a,x,b,x)=>void{};").err("Error during parsing at line 1, column 8: Duplicate argument x encountered in function parameters.").err(DuplicateFunctionArgumentException.class)),
	TEST053(new Cfg("()=>void{var x;if(true)var x;var x;};").err("Error during parsing at line 1, column 30: Variable x was already declared at the current nesting level.").err(VariableDeclaredTwiceException.class)),

	STRICT001(new Cfg("a = b;").err("Error during parsing at line 1, column 1: Variable a was not declared. Variables must be declared before they are used in strict mode.").err(VariableUsageBeforeDeclarationException.class).strict()),
	STRICT002(new Cfg("function foo::bar(){};").err("Error during parsing at line 1, column 1: Occurence of function foo::bar at top level. Scoped function must be defined in a scope block in strict mode.").err(ScopedFunctionOutsideHeaderException.class).strict()),
	STRICT003(new Cfg("math::pi;").err("Error during parsing at line 1, column 1: Scope math is provided by the context, but require scope statement is missing. Strict mode requires importing scopes explicitly.").err(MissingRequireScopeStatementException.class).strict()),
	STRICT004(new Cfg("global scope {number x;}switch(0){case 1:x=22;default:x='33';}").err("Error during parsing at line 1, column 55: Found incompatible variable type string but expected number: Variable x cannot be assigned to this type.").err(IncompatibleVariableAssignmentTypeException.class).strict()),

	VOID001(new Cfg("global scope{method<void> m;method<string,string> m2;}m2(m());").err("Error during parsing at line 1, column 58: Found incompatible variable type void but expected string: Function parameter type not compatible.").err(IncompatibleFunctionParameterTypeException.class).strict()),
	VOID002(new Cfg("global scope{method<void> m;}m()+0;").err("Error during parsing at line 1, column 34: No such expression method PLUS(+) for type void.").err(NoSuchExpressionMethodException.class).strict()),
	VOID003(new Cfg("global scope{method<void> m;}0+m();").err("Error during parsing at line 1, column 32: Found incompatible variable type void but expected number: Expression method PLUS(+) for number does not accept this type on the right hand side.").err(IncompatibleExpressionMethodTypeException.class).strict()),
	VOID004(new Cfg("global scope{method<void> m;}m().toString;").err("Error during parsing at line 1, column 32: No such dot accesor named toString for type void.").err(NoSuchDotAccessorException.class).strict()),
	VOID005(new Cfg("global scope{method<void> m;}m()[0];").err("Error during parsing at line 1, column 32: No such bracket accesor for type void.").err(NoSuchBracketAccessorException.class).strict()),
	VOID006(new Cfg("global scope{method<void> m;}m().length=0;").err("Error during parsing at line 1, column 34: No such dot assigner named length for type void.").err(NoSuchDotAssignerException.class).strict()),
	VOID007(new Cfg("global scope{method<void> m;}m()[0]=0;").err("Error during parsing at line 1, column 34: No such bracket accesor for type void.").err(NoSuchBracketAssignerException.class).strict()),
	VOID008(new Cfg("global scope{method<void> m;string v;}v=m();").err("Error during parsing at line 1, column 39: Found incompatible variable type void but expected string: Variable v cannot be assigned to this type.").err(IncompatibleVariableAssignmentTypeException.class).strict()),
	VOID009(new Cfg("global scope{method<void> m;var v;}v=m();").err("Error during parsing at line 1, column 36: Found incompatible variable type void but expected var: Variable v cannot be assigned to this type.").err(IncompatibleVariableAssignmentTypeException.class).strict()),
	VOID010(new Cfg("global scope{method<void> m;number v;}v+=m();").err("Error during parsing at line 1, column 39: Found incompatible variable type void but expected number: Expression method PLUS(+) for number does not accept this type on the right hand side.").err(IncompatibleExpressionMethodTypeException.class).strict()),
	VOID011(new Cfg("global scope{method<void> m;number v;}v=true ? 5 : m();").err("Error during parsing at line 1, column 39: Found incompatible variable type void but expected number: Variable v cannot be assigned to this type.").err(IncompatibleVariableAssignmentTypeException.class).strict()),
	VOID012(new Cfg("()=>void{return 0;};").err("Error during parsing at line 1, column 1: Found incompatible variable type number: Void function must not return a value.").err(IncompatibleVoidReturnTypeException.class).strict()),
	VOID013(new Cfg("global scope{method<void>v;}()=>void{return v();};").err("Error during parsing at line 1, column 45: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),
	VOID014(new Cfg("global scope{method<void>v;}()=>number{return v();};").err("Error during parsing at line 1, column 47: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),
	VOID015(new Cfg("global scope{method<void>v;}for(var x in v());").err("Error during parsing at line 1, column 42: Variable type void is not iterable.").err(IterationNotSupportedException.class).strict()),
	VOID016(new Cfg("global scope{method<void>v;}loginfo(v());").err("Error during parsing at line 1, column 37: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),
	VOID017(new Cfg("global scope{method<void>v;}exception(v());").err("Error during parsing at line 1, column 39: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),
	VOID018(new Cfg("global scope{method<void> m;}m()==5;").err("Error during parsing at line 1, column 30: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),
	VOID019(new Cfg("global scope{string s;method<void> m;}s=~m();").err("Error during parsing at line 1, column 42: Found incompatible variable type void but expected regex: Expression method EQUAL_TILDE(=~) for string does not accept this type on the right hand side.").err(IncompatibleExpressionMethodTypeException.class).strict()),
	VOID020(new Cfg("global scope{number n;method<void>m;}n<m();").err("Error during parsing at line 1, column 40: Found incompatible variable type void but expected var: Type cannot be upconverted to this type.").err(IncompatibleVariableConversionTypeException.class).strict()),

	TEMPLATE001(new Cfg("<foo>[% i = %]</foo> [% 42; %]").template().err("Encountered \"%]\" at line 1, column 13.").err(ParseException.class)),
	TEMPLATE002(new Cfg("<foo>[% i = 0;").template().err("Error during parsing at line 1, column 14: Final code block in templates must be closed.").err(ParseException.class)),
	TEMPLATE003(new Cfg("<foo> [%foo=0;foo(); <bar>").template().err("Encountered \"<\" at line 1, column 22.").err(ParseException.class)),
	;
	@Nonnull private final String code;
	@Nonnull private final ETestType type;
	@Nonnull private final EContextType context;
	@Nullable private final String errorBegin;
	@Nullable private final Class<? extends Throwable> errorClass;
	@Nonnull private final ISeverityConfig config;

	private SyntaxFailure(@Nonnull final String code, @Nonnull final String errMsg) {
		this(new Cfg(code).err(errMsg).err(ParseException.class));
	}
	private SyntaxFailure(final Cfg cfg) {
		this.code = cfg.code;
		this.type = cfg.type;
		this.errorBegin = cfg.errMsg;
		this.context = cfg.context;
		this.errorClass = cfg.errClass;
		this.config = cfg.config;
	}

	@Override
	public String getErrorBegin() {
		return errorBegin;
	}

	@Override
	public ETestType getTestType() {
		return type;
	}

	@Override
	public EContextType getContextType() {
		return context;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public ALangObject getExpectedResult() {
		return null;
	}

	@Override
	public boolean isPerformEvaluation() {
		return false;
	}

	@Override
	public Class<? extends Throwable> getExpectedException() {
		return errorClass;
	}

	@Override
	public ISeverityConfig getSeverityConfig() {
		return config;
	}

	static class MyNumber {}
	static class FancyNumber extends MyNumber {}
	static class AwesomeNumber extends FancyNumber {}

	static class MyString {}
	static class FancyString extends MyString {}
	static class AwesomeString extends FancyString {}
}