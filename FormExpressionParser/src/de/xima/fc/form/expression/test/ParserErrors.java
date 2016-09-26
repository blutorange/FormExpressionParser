package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public class ParserErrors {

	public Node program(final String code) throws ParseException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(code.getBytes(Charset.forName("UTF-8")));
		final FormExpressionParser parser = new FormExpressionParser(bais, "UTF-8");
		final Node rootNode;
		rootNode = parser.Program();
		return rootNode;
	}

	private String[] testCaseProgram;
	private String[] testCaseProgramFail;

	@Before
	public void setUp() throws Exception {
		setupTestCaseProgram();
		setupTestCaseProgramFail();

	}

	private void setupTestCaseProgram() {
		testCaseProgram = new String[] {
				"",
				";",
				";;;;",
				"2+2;;;2;;",
				"if (i==0) error(i);",
				"if (i==0) error(i); else if (j==0) fail();",
				"if (i==0) error(i); else if (j==0) fail(); else success();",
				"if (i==0) { error(i); }",
				"if (i==0) { error(i); } else if (j==0) { fail(); fail();}",
				"if (i==0) { error(i); error(); } else if (j==0) { fail(); fail();} else { success(j); }",
				"if(a) a; else if(c) c; else b;",
				"for (node : nodeList) print(node);",
				"for (i=0; i != 10; ++i) print(i);",
				"for (i=0 , j = 0; i != 10 , i>=0; ++i, ++j) { print(i); log(j);}",
				"for (;;) spam();",
				"try { 1/0; } catch (e) { log(me); }",
				"switch(getEnum()) { }",
				"switch(getEnum()) {case 1: case 2: multiple.stuff();}",
				"switch(getEnum()) { default: a.b;}",
				"switch(getEnum()) { case 1: shout(); loud(); case 2: cry(); case 3: ;}",
				"switch(getEnum()) { case 1: shout(); case 2: cry(); default: beHappy();}",
				"if(a) { if (b) { do(); } }",
				"a()(++i);",
				"a.b().c();",
				"3**4**5;",
				"a.b().c.d(foo,bar).e;",
				"getFunction()(foo);",
				"(a+b)(foo);",
				"a[b.foo(bar)]().baz;"
		};
	}

	private void setupTestCaseProgramFail() {
		testCaseProgramFail = new String[] {
				"if (i==0) { alert() }",
				"if(a) a; else b; else if(c) c;",
				"if(a) a; else if (c) c; else b; else c;",
				"try { 1/0; } catch (e) { log(me) };",
				"a()9;"
		};
	}

	@Test
	public final void testProgram() {
		for (final String code : testCaseProgram) {
			System.out.println("Running code <" + code + ">");
			try {
				program(code);
			} catch (final Exception e) {
				e.printStackTrace();
				fail("Code threw an exception.");
			}
		}
	}

	@Test
	public final void testProgramFail() {
		for (final String code : testCaseProgramFail) {
			System.out.println("Running code <" + code + ">");
			try {
				program(code);
				System.out.println("Parser did not throw an exception.");
				fail("Parser did not throw an exception.");
			} catch (final ParseException e) {
				//Good
			}
			catch (final Exception e) {
				e.printStackTrace();
				System.out.println("Parser did not throw a parse exception.");
				fail("Parser did not throw a parse exception.");
			}
		}
	}

}
