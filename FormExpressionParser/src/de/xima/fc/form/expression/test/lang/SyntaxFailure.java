package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.parse.HeaderAssignmentNotCompileTimeConstantException;
import de.xima.fc.form.expression.exception.parse.IllegalExternalScopeAssignmentException;
import de.xima.fc.form.expression.exception.parse.IllegalJumpClauseException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableDeclarationAtGlobalScopeException;
import de.xima.fc.form.expression.exception.parse.MissingRequireScopeStatementException;
import de.xima.fc.form.expression.exception.parse.ScopedFunctionOutsideHeaderException;
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
	TEST001("if (i==0) { alert() }", "Encountered \" \"}\" \"} \"\" at line 1, column 21."),
	TEST002("if(a) a; else b; else if(c) c;", "Encountered \" \"else\" \"else \"\" at line 1, column 18."),
	TEST003("if(a) a; else if (c) c; else b; else c;", "Encountered \" \"else\" \"else \"\" at line 1, column 33."),
	TEST004("with (me you) stuff;", "Encountered \" <Identifier> \"you \"\" at line 1, column 10."),
	TEST005("with (me::you) stuff;", "Encountered \" \"::\" \":: \"\" at line 1, column 9."),
	TEST006("try { 1/0; } catch (e) { log(me) };","Encountered \" \"}\" \"} \"\" at line 1, column 34."),
	TEST007("a()9;","Encountered \" <Integer> \"9 \"\" at line 1, column 4."),
	TEST008("^|n};","Encountered \" \"^\" \"^ \"\" at line 1, column 1."),
	TEST009("with (foo bar) foobar;","Encountered \" <Identifier> \"bar \"\" at line 1, column 11."),
	TEST010("foo()%]", "Embedded blocks are not allowed."),
	TEST011(new Cfg("foo=bar;/*unclosed").err("Lexical error at line 1, column 19.  Encountered: <EOF> after : \"\"").err(TokenMgrError.class)),
	TEST012("#foobar#q;", "Encountered \" <Identifier> \"q \"\" at line 1, column 9."),
	TEST013("#(\\d+#", "Encountered invalid regex at line 1, column 1: Unclosed group near index 4"),
	TEST014("42 = 42;", "Encountered illegal LVALUE ASTNumberNode in assignment at line 1, column 1."),
	TEST015(new Cfg("\"\\\";").err("Lexical error at line 1, column 5.  Encountered: <EOF> after : \"\\\"\\\\\\\";\"").err(TokenMgrError.class)),
	TEST016("++(1+2);", "Encountered illegal LVALUE ASTParenthesisExpressionNode in prefix operation at line 1, column 3."),
	TEST017("++a.b();", "Encountered illegal LVALUE (function call) ASTPropertyExpressionNode(null,NONE(),1:3-1:7) in prefix operation at line 1, column 1."),
	TEST018("++--a;", "Encountered illegal LVALUE ASTUnaryExpressionNode in prefix operation at line 1, column 3."),
	TEST019("if (true) require scope math;", "Encountered \" \"require\" \"require \"\" at line 1, column 11."),
	TEST020("scoped::var.works['great']();", "Encountered \" \"var\" \"var \"\" at line 1, column 9."),
	TEST021("a = var b = c;", "Encountered \" \"var\" \"var \"\" at line 1, column 5."),
	TEST022(new Cfg("var i = i;").err("Error during parsing at line 1, column 1: Encountered variable declaration for i at global scope. Global variable must be declared in a global block.").err(IllegalVariableDeclarationAtGlobalScopeException.class)),
	TEST024("if(false){function foo(){}}", "Encountered \" \"function\" \"function \"\" at line 1, column 11."),
	TEST025(new Cfg("global scope {var i=0;var j=i;}j;").err("Error during parsing at line 1, column 29: Illegal assignment for j. Assignment in header definitions must be compile-time constant.").err(HeaderAssignmentNotCompileTimeConstantException.class)),
	TEST026(new Cfg("scope myscope{var j=''.lower();}myscope::j;").err("Error during parsing at line 1, column 21: Illegal assignment for j. Assignment in header definitions must be compile-time constant.").err(HeaderAssignmentNotCompileTimeConstantException.class)),
	TEST027(new Cfg("if(true){var i=0;}else{var j=i+3;}").err("Error during parsing at line 1, column 30: Variable i cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST028(new Cfg("require scope math;math::pi=9;").err("Error during parsing at line 1, column 20: Variable math::pi belongs to an external scope and cannot be assigned to.").err(IllegalExternalScopeAssignmentException.class)),
	TEST029(new Cfg("break;").err("Error during parsing at line 1, column 1: Break without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST030(new Cfg("continue;").err("Error during parsing at line 1, column 1: Continue without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST031(new Cfg("return;").err("Error during parsing at line 1, column 1: Return clause used outside a function.").err(IllegalJumpClauseException.class)),
	TEST032(new Cfg("while(true)break foo;").err("Error during parsing at line 1, column 12: Break foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST033(new Cfg("while<foo>(true)break;").err("Error during parsing at line 1, column 17: Break without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST034(new Cfg("while<foo>(true);while(true)break foo;").err("Error during parsing at line 1, column 29: Break foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST035(new Cfg("do{continue foo;}while(true);").err("Error during parsing at line 1, column 4: Continue foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST036(new Cfg("for<foo>(i in 10)continue;").err("Error during parsing at line 1, column 18: Continue without label used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST037(new Cfg("switch<foo>(true){};while(true)continue foo;").err("Error during parsing at line 1, column 32: Continue foo used outside of loop or switch, or label does not match any loop or switch.").err(IllegalJumpClauseException.class)),
	TEST038(new Cfg("if(true)var i = i;").err("Error during parsing at line 1, column 17: Variable i cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	// Closures not supported currently.
	TEST039(new Cfg("function foo(){var i = 10;->(){i;};}foo();").err("Error during parsing at line 1, column 32: Variable i cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST040(new Cfg("function foo(){arguments.length;}foo(1,2,3);").err("Error during parsing at line 1, column 16: Variable arguments cannot be resolved to a defined variable.").err(VariableNotResolvableException.class)),
	TEST041(new Cfg("\"foo\\\";").err(TokenMgrError.class).err("Lexical error at line 1, column 8.  Encountered: <EOF> after : \"\\\"foo\\\\\\\";\"")),
	TEST042(new Cfg("#(\\d#;").err(ParseException.class).err("Encountered invalid regex at line 1, column 1: Unclosed group near index 3")),

	STRICT001(new Cfg("a = b;").err("Error during parsing at line 1, column 1: Variable a was not declared. Variables must be declared before they are used in strict mode.").err(VariableUsageBeforeDeclarationException.class).strict()),
	STRICT002(new Cfg("function foo::bar(){};").err("Error during parsing at line 1, column 1: Occurence of function foo::bar at top level. Scoped function must be defined in a scope block in strict mode.").err(ScopedFunctionOutsideHeaderException.class).strict()),
	STRICT003(new Cfg("math::pi;").err("Error during parsing at line 1, column 1: Scope math is provided by the context, but require scope statement is missing. Strict mode requires importing scopes explicitly.").err(MissingRequireScopeStatementException.class).strict()),

	TEMPLATE001(new Cfg("<foo>[% i = %]</foo> [% 42; %]").template().err("Encountered \" \"%]\" \"%] \"\" at line 1, column 13.").err(ParseException.class)),
	TEMPLATE002(new Cfg("<foo>[% i = 0;").template().err("Final code block in templates must be closed.").err(ParseException.class)),
	TEMPLATE003(new Cfg("<foo> [%foo=0;foo(); <bar>").template().err("Encountered \" \"<\" \"< \"\" at line 1, column 22.").err(ParseException.class)),
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
}