package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public class Semantics {

	private final static NumberLangObject N42 = NumberLangObject.create(42);
	
	private static interface ITest {
		public String getCode();
		public ALangObject getExpectedResult();
		public Class<? extends EvaluationException> getExpectedException();
	}
	
	private static enum Tests implements ITest {
		// Object literals.
		LITERALS001("null;", NullLangObject.getInstance()),
		LITERALS002("true;", BooleanLangObject.getTrueInstance()),
		LITERALS003("false;", BooleanLangObject.getFalseInstance()),
		LITERALS004("42;", NumberLangObject.create(42)),
		LITERALS005("42.35;", NumberLangObject.create(42.35d)),
		LITERALS006("'single';", StringLangObject.create("single")),
		LITERALS007("'double';", StringLangObject.create("double")),
		LITERALS008("'escape\\\\';", StringLangObject.create("escape\\")),
		LITERALS009("'吵';", StringLangObject.create("吵")),
		LITERALS010("'\u5435';", StringLangObject.create("吵")),
		LITERALS011("[];", ArrayLangObject.create()),
		LITERALS012("[null, true, 0, '', [], {}];", ArrayLangObject.create(
					NullLangObject.getInstance(),
					BooleanLangObject.getTrueInstance(),
					NumberLangObject.getZeroInstance(),
					StringLangObject.getEmptyInstance(),
					ArrayLangObject.create(),
					HashLangObject.create()
				)),
		LITERALS013("{};", HashLangObject.create()),
		LITERALS014("{null:true, 0:'',[]:{},false:1};", HashLangObject.create(
					NullLangObject.getInstance(),
					BooleanLangObject.getTrueInstance(),
					NumberLangObject.getZeroInstance(),
					StringLangObject.getEmptyInstance(),
					ArrayLangObject.create(),
					HashLangObject.create(),
					BooleanLangObject.getFalseInstance(),
					NumberLangObject.getOneInstance()
				)),
		LITERALS015("->(x){x;}(42);", N42),
		
		// Variables and scopes.
		SCOPE001("k=42;k;", N42), // local variables
		SCOPE002("k=0;if(true)k=42;k;", N42), // can access parent when nesting
		SCOPE003("k=42;->(){k=0;}();k;", N42), // functions with separate scope
		SCOPE004("k=42;function foo(){k=0;};foo();k;", N42), // functions with separate scope
		SCOPE005("k=42;->(){k;}();", N42), // accessing global variables
		SCOPE006("k=0;->(){k=42;k;}();", N42), // shadowing global variables
		SCOPE007("foo::k=0;bar::k=42;bar::k;",N42), // scopes
		SCOPE008("foo::k=0;bar::k=42;with(foo,bar)k;",N42), // default scopes reading vars
		SCOPE009("foo::k=0;with(foo){k=42;k;}", N42), // default scopes writing to vars
		SCOPE010("foo::k=42;with(foo){k=0;foo::k;}", N42), // can still access explicitly
		SCOPE011("k=42;foo::k=0;with(foo){k;}", N42), // first global, then default scope
		
		// Expression methods number.
		EMETHODUN001("-(-42);", N42),
		EMETHODUN002("+42;", N42),
		EMETHODBIN001("40+2;", N42),
		EMETHODBIN002("50-8;", N42),
		EMETHODBIN003("2*21;", N42),
		EMETHODBIN004("168/4;", N42),
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
		@Override
		public Class<? extends EvaluationException> getExpectedException() {
			return null;
		}
	}

	private static enum TestsFailure implements ITest{
		TEST001("k;", VariableNotDefinedException.class),
		TEST002("break;", BreakClauseException.class),
		TEST003("continue;", ContinueClauseException.class),
		TEST004("throw exception('');", CustomRuntimeException.class),
		TEST005("if (true) k=0;k;", VariableNotDefinedException.class),
		TEST006("for(i:2) k=0;k;", VariableNotDefinedException.class),
		TEST007("try{k=0;}catch(e){k=0;};k;", VariableNotDefinedException.class),
		;

		private String code;
		private Class<? extends EvaluationException> expectedException;
		private TestsFailure (String code, Class<? extends EvaluationException> exception) {
			this.code = code;
			this.expectedException = exception;
		}
		@Override
		public String getCode() {
			return code;
		}
		@Override
		public ALangObject getExpectedResult() {
			return NullLangObject.getInstance();
		}
		@Override
		public Class<? extends EvaluationException> getExpectedException() {
			return expectedException;
		}
	}

	@Test
	public void testSemantics() {
		test(Tests.class);
	}

	@Test
	public void testSemanticsFailure() {
		test(TestsFailure.class);
	}

	public void test(Class<? extends ITest> clazz) throws IllegalArgumentException {
		if (!clazz.isEnum()) throw new IllegalAccessError(String.format("%s is not an enum.", clazz));
		for (final ITest test : clazz.getEnumConstants()) {
			System.out.println(String.format("Running test %s::%s.", clazz.getSimpleName(), test));
			Throwable exception = null;
			ALangObject res = null;
			try {
				res = run(test.getCode());
			} catch (ParseException e) {
				exception = e;
			} catch (EvaluationException e) {
				exception = e;
			} catch (final Throwable throwable) {
				throwable.printStackTrace();
				final String msg = "Code threw an exception.";
				System.err.println(msg);
				fail(msg);
			}
			String msg = null;
			final Class<? extends Throwable> exceptionClass = exception == null ? null : exception.getClass();
			if (test.getExpectedException() != null) {
				if (test.getExpectedException() != exceptionClass) {
					if (exception != null) exception.printStackTrace();
					msg = String.format(
						"Code was expected to throw an Exception of type %s, but instead it threw %s.",
						test.getExpectedException(), exceptionClass);
				}
			}
			else if (test.getExpectedException() == null && exceptionClass != null) {
				if (exception != null) exception.printStackTrace();
				msg = String.format("Code was not expected to throw an Exception, but it threw %s.",
						exceptionClass);
			}
			else if (res == null) {
				msg = "No language object was returned.";
			}
			else if (test.getExpectedResult() != null && !test.getExpectedResult().equals(res)) {
				msg = String.format("Expected result was %s, but code evaluated to %s.", test.getExpectedResult().inspect(), res.inspect());
			}
			if (msg != null) {
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	private ALangObject run(final String code) throws ParseException, EvaluationException {
		return evaluate(parse(code));
	}
	
	private ALangObject evaluate(final Node node) throws EvaluationException {
		return EvaluateVisitor.evaluateProgram(node, GenericEvaluationContext.getNewBasicEvaluationContext());
	}
	
	private Node parse(final String code) throws ParseException {
		return FormExpressionParseFactory.Program.parseString(code);
	}
}
