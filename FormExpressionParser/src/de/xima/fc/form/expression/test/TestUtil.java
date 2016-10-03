package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public final class TestUtil {
	private TestUtil() {}
	public static enum ETestType {
		PROGRAM,
		TEMPLATE;
	}
	public static interface ITestCase {
		public String getCode();

		public ALangObject getExpectedResult();

		public boolean isPerformEvaluation();

		public Class<? extends Throwable> getExpectedException();

		public ETestType getTestType();

		public String getErrorBegin();
	}

	public static void test(final Class<? extends ITestCase> clazz) throws IllegalArgumentException, AssertionError {
		if (!clazz.isEnum()) throw new IllegalAccessError(String.format("%s is not an enum.", clazz));
		for (final ITestCase test : clazz.getEnumConstants()) {
			System.out.println(String.format("Running test %s::%s.", clazz.getSimpleName(), test));
			Throwable exception = null;
			ALangObject res = null;
			try {
				final Node node = parse(test.getCode(), test.getTestType());
				if (test.isPerformEvaluation()) res = evaluate(node);
			} catch (final ParseException e) {
				exception = e;
			} catch (final EvaluationException e) {
				exception = e;
			} catch (final Throwable throwable) {
				exception = throwable;
			}
			String msg = null;
			final Class<? extends Throwable> exceptionClass = exception == null ? null : exception.getClass();
			if (test.getExpectedException() != null) {
				if (exception == null) {
					msg = String.format(
							"Code was expected to throw an Exception of type %s, but it threw none.",
							test.getExpectedException());
				}
				else if (test.getExpectedException() != exceptionClass) {
					exception.printStackTrace();
					msg = String.format(
							"Code was expected to throw an Exception of type %s, but it threw %s.",
							test.getExpectedException());
				}
				else if (test.getErrorBegin() != null) {
					String em = exception.getMessage();
					if (em == null) em = "";
					if (!em.startsWith(test.getErrorBegin())) {
						exception.printStackTrace();
						msg = String.format(
								"Code was expected to throw an exception starting with \"%s\", but it did not.",
								StringEscapeUtils.escapeJava(test.getErrorBegin()));
					}
				}
			}
			else if (test.getExpectedException() == null && exceptionClass != null) {
				if (exception != null) exception.printStackTrace();
				msg = String.format("Code was not expected to throw an Exception, but it threw %s.",
						exceptionClass);
			}
			else if (test.isPerformEvaluation()) {
				if (res == null) {
					msg = "No language object was returned.";
				}
				else if (test.getExpectedResult() != null && !test.getExpectedResult().equals(res)) {
					msg = String.format("Expected result was %s, but code evaluated to %s.",
							test.getExpectedResult().inspect(), res.inspect());
				}
			}
			if (msg != null) {
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	private static ALangObject evaluate(final Node node) throws EvaluationException {
		return EvaluateVisitor.evaluateProgram(node, GenericEvaluationContext.getNewBasicEvaluationContext());
	}

	private static Node parse(final String code, final ETestType type) throws ParseException {
		switch (type) {
		case PROGRAM:
			return FormExpressionParseFactory.Program.parse(code);
		case TEMPLATE:
			return FormExpressionParseFactory.Template.parse(code);
		default:
			throw new ParseException("Unkown enum: " + type);
		}
	}
}
