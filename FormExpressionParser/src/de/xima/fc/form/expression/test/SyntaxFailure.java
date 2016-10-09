package de.xima.fc.form.expression.test;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.test.TestUtil.EContextType;
import de.xima.fc.form.expression.test.TestUtil.ETestType;
import de.xima.fc.form.expression.test.TestUtil.ITestCase;

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
	TEST011("foo=bar;/*unclosed", "Lexical error at line 1, column 19.  Encountered: <EOF> after : \"\"", TokenMgrError.class),
	TEST012("#foobar#q;", "Encountered \" <Identifier> \"q \"\" at line 1, column 9."),
	TEST013("#(\\d+#", "Encountered invalid regex at line 1, column 1: Unclosed group near index 4"),
	TEST014("42 = 42;", "Encountered illegal LVALUE ASTNumberNode for assignment at line 1, column 1."),
	TEST015("\"\\\";", "Lexical error at line 1, column 5.  Encountered: <EOF> after : \"\\\"\\\\\\\";\"", TokenMgrError.class),
	
	TEMPLATE001("<foo>[% i = %]</foo> [% 42; %]", ETestType.TEMPLATE,"Encountered \" \"%]\" \"%] \"\" at line 1, column 13."),
	TEMPLATE002("<foo>[% i = 0;", ETestType.TEMPLATE,"Final code block in templates must be closed."),
	TEMPLATE003("<foo> [% foo(); <bar>", ETestType.TEMPLATE,"Encountered \" \"<\" \"< \"\" at line 1, column 17."),
	;
	private final String code;
	private final ETestType type;
	private final EContextType context;
	private final String errorBegin;
	private final Class<? extends Throwable> errorClass;

	private SyntaxFailure(final String code) {
		this(code, ETestType.PROGRAM, null);
	}
	private SyntaxFailure(final String code, final String errorBegin) {
		this(code, ETestType.PROGRAM, errorBegin);
	}
	private SyntaxFailure(final String code, final String errorBegin, final Class<? extends Throwable> errorClass) {
		this(code, ETestType.PROGRAM, errorBegin, EContextType.GENERIC, errorClass);
	}
	private SyntaxFailure(final String code, final ETestType type) {
		this(code, type, null);
	}
	private SyntaxFailure(final String code, final ETestType type, final String errorBegin) {
		this(code, type, errorBegin, EContextType.GENERIC, ParseException.class);
	}
	private SyntaxFailure(final String code, final ETestType type, final String errorBegin, final EContextType context, final Class<? extends Throwable> errorClass) {
		this.code = code;
		this.type = type;
		this.errorBegin = errorBegin;
		this.context = context;
		this.errorClass = errorClass;
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
}