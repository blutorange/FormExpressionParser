package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;

public class Syntax {

	private static interface ITest {
		public String getCode();
	}

	private static enum Tests implements ITest {
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
		TEST037("foo::bar = baz = 'hello';"),;
		private String code;
		private Tests(String code) {
			this.code = code;
		}
		@Override
		public String getCode() {
			return code;
		}
	}

	private static enum TestsFailure implements ITest {
		TEST001("if (i==0) { alert() }"),
		TEST002("if(a) a; else b; else if(c) c;"),
		TEST003("if(a) a; else if (c) c; else b; else c;"),
		TEST004("with (me you) stuff;"),
		TEST005("with (me::you) stuff;"),
		TEST006("try { 1/0; } catch (e) { log(me) };"),
		TEST007("a()9;"),
		TEST008("^|n};"),
		TEST009("with (foo bar) foobar;"),
		;
		private String code;
		private TestsFailure(String code) {
			this.code = code;
		}
		@Override
		public String getCode() {
			return code;
		}
	}

	@Test
	public final void testSyntax() {
		for (final ITest test: Tests.values()) {
			System.out.println(String.format("Running test %s::%s.", Tests.class.getSimpleName(), test));
			try {
				parse(test.getCode());
			}
			catch (final ParseException e) {
				e.printStackTrace();
				final String msg = "Code threw a parse exception.";
				System.err.println(msg);
				fail(msg);
			} catch (final Throwable t) {
				t.printStackTrace();
				final String msg = "Code threw an uncaught exception.";
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	@Test
	public final void testSyntaxFail() {
		for (final ITest test : TestsFailure.values()) {
			System.out.println(String.format("Running test %s::%s.", TestsFailure.class.getSimpleName(), test));
			try {
				parse(test.getCode());
				final String msg = "Parser did not throw an exception.";
				System.err.println(msg);
				fail(msg);
			} catch (final ParseException e) {
				// Good
			} catch (final Throwable t) {
				t.printStackTrace();
				final String msg = "Code threw an excpetion, but not the excpected parse exception.";
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	private Node parse(final String code) throws ParseException {
		return FormExpressionParseFactory.Program.parseString(code);
	}
}
