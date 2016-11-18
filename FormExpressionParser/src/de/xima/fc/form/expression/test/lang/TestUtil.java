package de.xima.fc.form.expression.test.lang;

import static org.junit.Assert.fail;

import java.io.Writer;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringEscapeUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextProvider;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.impl.externalcontext.AGenericExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.DummyExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.externalcontext.WriterOnlyExternalContext;
import de.xima.fc.form.expression.impl.factory.FormcycleEcProvider;
import de.xima.fc.form.expression.impl.factory.GenericEcProvider;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
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
				switch (test.getContextType()) {
				case FORMCYCLE:
					final IFormExpression<FormcycleExternalContext> feForm = parse(test.getCode(), test.getTestType(), FormcycleEcProvider.INSTANCE);
					if (test.isPerformEvaluation()) res = evaluateFormcycle(feForm, test.getTestType());
					break;
				case GENERIC:
					final IFormExpression<AGenericExternalContext> feGeneric = parse(test.getCode(), test.getTestType(), GenericEcProvider.INSTANCE);
					if (test.isPerformEvaluation()) res = evaluateGeneric(feGeneric, test.getTestType());
					break;
				default:
					break;
				}
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

	private static ALangObject evaluateGeneric(@Nonnull final IFormExpression<AGenericExternalContext> fe, @Nonnull final ETestType type) throws EvaluationException {
		final ALangObject res;
		final Date t1 = new Date();
		switch (type) {
		case PROGRAM:
			res = fe.evaluate(DummyExternalContext.getInstance());
			break;
		case TEMPLATE:
			final WriterOnlyExternalContext woec = new WriterOnlyExternalContext(new StringBuilderWriter());
			fe.evaluate(woec);
			res = StringLangObject.create(woec.toString());
			break;
		default:
			throw new RuntimeException("Unkown enum: " + type);
		}
		final Date t2 = new Date();
		System.out.println(String.format("Evaluation took %s ms.", t2.getTime()-t1.getTime()));
		return res;
	}
	private static ALangObject evaluateFormcycle(@Nonnull final IFormExpression<FormcycleExternalContext> fe, @Nonnull final ETestType type) throws EvaluationException {
		final ALangObject res;
		final Date t1 = new Date();
		switch (type) {
		case PROGRAM:
			res = fe.evaluate(new FormcycleExternalContext());
			break;
		case TEMPLATE:
			final Writer sbw = new StringBuilderWriter();
			fe.evaluate(new FormcycleExternalContext(sbw));
			res = StringLangObject.create(sbw.toString());
			break;
		default:
			throw new RuntimeException("Unkown enum: " + type);
		}
		final Date t2 = new Date();
		System.out.println(String.format("Evaluation took %s ms.", t2.getTime()-t1.getTime()));
		return res;
	}

	@Nonnull
	private static <T extends IExternalContext> IFormExpression<T> parse(@Nonnull final String code, @Nonnull final ETestType type, @Nonnull final IEvaluationContextProvider<T> provider) throws ParseException, TokenMgrError {
		final IFormExpression<T> res;
		final Date t1 = new Date();
		switch (type) {
		case PROGRAM:
			res = FormExpressionFactory.Program.parse(code, provider);
			break;
		case TEMPLATE:
			res = FormExpressionFactory.Template.parse(code, provider);
			break;
		default:
			throw new ParseException("Unkown enum: " + type);
		}
		final Date t2 = new Date();
		System.out.println(String.format("Parsing took %s ms.", t2.getTime()-t1.getTime()));
		return res;
	}
}
