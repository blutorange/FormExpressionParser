package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.Cfg;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;

@SuppressWarnings("nls")
enum SyntaxSuccess implements ITestCase {
	TEST001(""),
	TEST002(";"),
	TEST003(";;;;"),
	TEST004("2+2;;;2;;"),
	TEST005("if (i==0) bug(i);"),
	TEST006("if (i==0) bug(i); else if (j==0) fail();"),
	TEST007("if (i==0) bug(i); else if (j==0) fail(); else success();"),
	TEST008("if (i==0) { bug(i); }"),
	TEST009("if (i==0) { bug(i); } else if (j==0) { fail(); fail();}"),
	TEST010("if (i==0) { bug(i); bug(); } else if (j==0) { fail(); fail();} else { success(j); }"),
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
	TEST036("scoped::variable.works['great']();"),
	TEST037("foo::bar = baz = 'hello';"),
	TEST038(new Cfg("<foo></foo>").template()),
	TEST039(new Cfg("<foo>[% a+b;%]</foo>").template()),
	TEST040(new Cfg("<foo>[% a+b;%]").template()),
	TEST041(new Cfg("[% a+b;%]").template()),
	TEST042(new Cfg("<foo>[% for(i:10) { %]</foo>[% k+1;%]<bar/>[% } %]").template()),
	TEST043(new Cfg("<foo>[%i=0;%][%j=0;%]</foo>").template()),
	TEST044(new Cfg("<foo>[%42%]</foo>").template().fc()),
	TEST045(new Cfg("<foo>[%42;%]</foo>").template().fc()),
	TEST046(new Cfg("<foo>[%{4:5}.b.c[0].d()%]</foo>").template().fc()),
	TEST047("foo=bar;/*+&*/bar=foo;"),
	TEST048("#regex#;"),
	TEST049("#regex#i;"),
	TEST050("#regex#msii;"),
	TEST051("foo ? bar : baz;"), // Ternary
	TEST052("++a.b['c'];"),
	TEST053("++r;"),
	TEST054("a+b;//comment"),
	TEST055("a + /* comment \n end */ b;"),
	TEST056("global scope{}"),
	TEST057("global scope{var i; var j = 5+3;}"),
	TEST058("scope myScope{var i; var j = 5+3;}"),
	TEST059("scope myScope{function foo(){}}"),
	TEST060("require scope math;"),
	TEST061("a = 8;require scope math;"),
	TEST062("scope foo{var ko;}foo::ko=0;"),
	
	;
	@Nonnull private final String code;
	@Nonnull private final ETestType type;
	@Nonnull private final EContextType context;
	private final boolean strictMode;
	
	private SyntaxSuccess(@Nonnull final String code) {
		this(new Cfg(code));
	}
	
	private SyntaxSuccess(@Nonnull final Cfg cfg) {
		this.code = cfg.code;
		this.type = cfg.type;
		this.context = cfg.context;
		this.strictMode = cfg.strict;
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
	public Class<? extends EvaluationException> getExpectedException() {
		return null;
	}
	@Override
	public String getErrorBegin() {
		return null;
	}

	@Override
	public boolean isUseStrictMode() {
		return strictMode;
	}
}