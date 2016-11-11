package de.xima.fc.form.expression.test;

import static org.junit.Assert.fail;

import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.context.IFormExpression;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.WriterOnlyExternalContext;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.impl.pool.FormcycleEcFactory;
import de.xima.fc.form.expression.impl.pool.GenericEcFactory;
import de.xima.fc.form.expression.impl.writer.StringBuilderWriter;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;

@SuppressWarnings("nls")
public final class TestUtil {
	private TestUtil() {}
	public static enum ETestType {
		PROGRAM,
		TEMPLATE;
	}
	public static enum EContextType {
		GENERIC,
		FORMCYCLE;
	}
	public static interface ITestCase {
		@Nonnull public String getCode();
		@Nonnull public ETestType getTestType();

		@Nullable public ALangObject getExpectedResult();

		public boolean isPerformEvaluation();

		@Nullable public Class<? extends Throwable> getExpectedException();

		@Nonnull public EContextType getContextType();

		@Nullable public String getErrorBegin();
	}

	public static void test(final Class<? extends ITestCase> clazz) throws IllegalArgumentException, AssertionError {
		if (!clazz.isEnum()) throw new IllegalAccessError(String.format("%s is not an enum.", clazz));
		for (final ITestCase test : clazz.getEnumConstants()) {
			System.out.println(String.format("Running test %s::%s.", clazz.getSimpleName(), test));
			Throwable exception = null;
			ALangObject res = null;
			try {
				final IFormExpression fe = parse(test.getCode(), test.getTestType());
				if (test.isPerformEvaluation()) res = evaluate(fe, test.getContextType(), test.getTestType());
			} catch (final TokenMgrError e) {
				exception = e;
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
							test.getExpectedException(), exception);
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
				final ALangObject er = test.getExpectedResult();
				if (res == null) {
					msg = "No language object was returned.";
				}
				else if (er != null && !er.equals(res)) {
					msg = String.format("Expected result was %s, but code evaluated to %s.", er.inspect(), res.inspect());
				}
			}
			if (msg != null) {
				System.err.println(msg);
				fail(msg);
			}
		}
	}

	private static ALangObject evaluate(@Nonnull final IFormExpression fe, @Nonnull final EContextType context, @Nonnull final ETestType type) throws EvaluationException {
		switch(context) {
		case GENERIC:
			switch (type) {
			case PROGRAM:
				return fe.evaluate(GenericEcFactory.getPoolInstance(), DummyExternalContext.INSTANCE);
			case TEMPLATE:
				final WriterOnlyExternalContext woec = new WriterOnlyExternalContext(new StringBuilderWriter());
				fe.evaluate(GenericEcFactory.getPoolInstance(), woec);
				return StringLangObject.create(woec.toString());
			default:
				throw new RuntimeException("Unkown enum: " + type);
			}
		case FORMCYCLE:
			switch (type) {
			case PROGRAM:
				return fe.evaluate(FormcycleEcFactory.getPoolInstance(), new FormcycleExternalContext());
			case TEMPLATE:
				final Writer sbw = new StringBuilderWriter();
				fe.evaluate(FormcycleEcFactory.getPoolInstance(), new FormcycleExternalContext(sbw));
				return StringLangObject.create(sbw.toString());
			default:
				throw new RuntimeException("Unkown enum: " + type);
			}
		default:
			throw new RuntimeException("Unknown enum: " + type);
		}
	}

	@Nonnull
	private static IFormExpression parse(@Nonnull final String code, @Nonnull final ETestType type) throws ParseException, TokenMgrError {
		switch (type) {
		case PROGRAM:
			return FormExpressionFactory.Program.parse(code);
		case TEMPLATE:
			return FormExpressionFactory.Template.parse(code);
		default:
			throw new ParseException("Unkown enum: " + type);
		}
	}
}
