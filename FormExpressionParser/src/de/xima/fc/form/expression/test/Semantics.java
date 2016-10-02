package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public class Semantics {

	private static interface ITest {
		public String getCode();
		public ALangObject getExpectedResult();
	}
	
	private static enum Tests implements ITest {
		TEST001("42;", NumberLangObject.create(42)),
		TEST002("variableDefined = 42;variableDefined;", NumberLangObject.create(42)),
		;

		private String code;
		private ALangObject expectedResult;
		private Tests(String code, ALangObject expectedResult) {
			this.code = code;
			this.expectedResult = expectedResult;
		}
		@Override
		public String getCode() {
			return code;
		}
		@Override
		public ALangObject getExpectedResult() {
			return expectedResult;
		}
	}

	private static enum TestsFailure implements ITest{
		TEST001("variableNotDefined;"),
		;

		private String code;
		private TestsFailure (String code) {
			this.code = code;
		}
		@Override
		public String getCode() {
			return code;
		}
		@Override
		public ALangObject getExpectedResult() {
			return NullLangObject.getInstance();
		}
	}

	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSemantics() {
		for (final ITest test: Tests.values()) {
			System.out.println(String.format("Running test %s::%s.", Tests.class.getSimpleName(), test));
			try {
				final ALangObject res = run(test.getCode());
				if (!test.getExpectedResult().equals(res)) {
					final String msg = String.format("Expected result was %s, but code evaluated to %s.", test.getExpectedResult().inspect(), res.inspect());
					System.err.println(msg);
					fail(msg);
				}
			} catch (final ParseException e) {
				e.printStackTrace();
				final String msg = "Code failed to parse.";
				System.err.println(msg);				
				fail(msg);
			} catch (final EvaluationException e) {
				e.printStackTrace();
				final String msg = "Code failed to evaluate.";
				System.err.println(msg);				
				fail(msg);
			} catch (final Throwable t) {
				t.printStackTrace();
				final String msg = "Code threw an exception.";
				System.err.println(msg);				
				fail(msg);
			}
		}
	}

	@Test
	public void testSemanticsFailure() {
		for (final ITest test : TestsFailure.values()) {
			System.out.println(String.format("Running test %s::%s.", TestsFailure.class.getSimpleName(), test));
			EvaluationException exception = null;
			try {
				run(test.getCode());
			} catch (EvaluationException e) {
				//Good
				exception = e;
			} catch (final Throwable t) {
				t.printStackTrace();
				final String msg = "Code threw an exception.";
				System.err.println(msg);
				fail(msg);
			}
			if (exception == null) {
				final String msg = "Code was excpected to throw an EvaluationException, but failed to do so.";
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	private ALangObject run(final String code) throws ParseException, EvaluationException {
		return evaluate(parse(code));
	}
	
	private ALangObject evaluate(final Node node) throws EvaluationException {
		return node.jjtAccept(new EvaluateVisitor(), GenericEvaluationContext.getNewBasicEvaluationContext());
	}
	
	private Node parse(final String code) throws ParseException {
		return FormExpressionParseFactory.Program.parseString(code);
	}
}
