package de.xima.fc.form.expression.test;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.test.TestUtil.ETestType;
import de.xima.fc.form.expression.test.TestUtil.ITestCase;

enum SyntaxSuccess implements ITestCase {
	TEST001(""),
	TEST002(";"),
	TEST003(";;;;"),
	TEST004("2+2;;;2;;"),
	TEST005("if (i==0) error(i);"),
	TEST006("if (i==0) error(i); else if (j==0) fail();"),
	TEST007("if (i==0) error(i); else if (j==0) fail(); else success();"),
	TEST008("if (i==0) { error(i); }"),
	TEST009("if (i==0) { error(i); } else if (j==0) { fail(); fail();}"),
	TEST010("if (i==0) { error(i); error(); } else if (j==0) { fail(); fail();} else { success(j); }"),
	TEST011("if(a) a; else if(c) c; else b;"),
	TEST012("for (node : nodeList) print(node);"),
	TEST013("for (i=0; i != 10; ++i) print(i);"),
	TEST014("for (i=0 , j = 0; i != 10 , i>=0; ++i, ++j) { print(i); log(j);}"),
	TEST015("for (;;) spam();"),
	TEST016("try { 1/0; } catch (e) { log(me); }"),
	TEST017("switch(getEnum()) { }"),
	TEST018("switch(getEnum()) {case 1: case 2: multiple.stuff();}"),
	TEST019("switch(getEnum()) { default: a.b;}"),
	TEST020("switch(getEnum()) { case 1: shout(); loud(); case 2: cry(); case 3: ;}"),
	TEST021("switch(getEnum()) { case 1: shout(); case 2: cry(); default: beHappy();}"),
	TEST022("if(a) if (b) do; while(false);"),
	TEST023("while(complex-expression) stuff();"),
	TEST024("a()(++i);"),
	TEST025("a.b().c();"),
	TEST026("3**4**5;"),
	TEST027("a.b().c.d(foo,bar).e;"),
	TEST028("getFunction()(foo);"),
	TEST029("(a+b)(foo);"),
	TEST030("a[b.foo(bar)]().baz;"),
	TEST031("for (i:10) if (i>5) break;"),
	TEST032("'012345'[0];"),
	TEST033("with (foo, bar) foobar;"),
	TEST034("a.b.c=foobar=4+2;"),
	TEST035("myscope::myvar;"),
	TEST036("scoped::var.works['great']();"),
	TEST037("foo::bar = baz = 'hello';"),
	TEST038("<foo></foo>", ETestType.TEMPLATE),
	TEST039("<foo>[% a+b;%]</foo>", ETestType.TEMPLATE),
	TEST040("<foo>[% a+b;%]", ETestType.TEMPLATE),
	TEST041("[% a+b;%]", ETestType.TEMPLATE),
	TEST042("<foo>[% for(i:10) { %]</foo>[% k+1;%]<bar/>[% } %]", ETestType.TEMPLATE),
	TEST044("<foo>[%i=0;%][%j=0;%]</foo>", ETestType.TEMPLATE),
	;
	private final String code;
	private final ETestType type;

	private SyntaxSuccess(final String code) {
		this.code = code;
		this.type = ETestType.PROGRAM;
	}

	private SyntaxSuccess(final String code, final ETestType type) {
		this.code = code;
		this.type = type;
	}

	@Override
	public ETestType getTestType() {
		return type;
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
	public Class<? extends EvaluationException> getExpectedException() {
		return null;
	}
	@Override
	public String getErrorBegin() {
		return null;
	}
}