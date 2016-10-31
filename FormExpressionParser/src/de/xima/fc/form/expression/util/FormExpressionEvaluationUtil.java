package de.xima.fc.form.expression.util;

import javax.annotation.Nonnull;

import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.pool.FormcycleEcFactory;
import de.xima.fc.form.expression.impl.pool.GenericEcFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public final class FormExpressionEvaluationUtil {
	private final static Logger LOG = LoggerFactory.getLogger(FormExpressionEvaluationUtil.class);

	private FormExpressionEvaluationUtil() {
	}

	public final static class Generic {
		private Generic() {
		}

		public static ALangObject evalProgram(@Nonnull final String code, @Nonnull final IExternalContext externalContext)
				throws ParseException, EvaluationException {
			final Node node = FormExpressionParsingUtil.Program.parse(code);
			return eval(node, externalContext);
		}

		public static ALangObject evalTemplate(@Nonnull final String code, @Nonnull final IExternalContext externalContext)
				throws ParseException, EvaluationException {
			final Node node = FormExpressionParsingUtil.Template.parse(code);
			return eval(node, externalContext);
		}

		public static ALangObject eval(@Nonnull final Node node, @Nonnull final IExternalContext externalContext)
				throws EvaluationException {
			return FormExpressionEvaluationUtil.eval(node, GenericEcFactory.getPoolInstance(), externalContext);
		}
	}

	public final static class Formcycle {
		private Formcycle() {
		}

		public static ALangObject evalProgram(@Nonnull final String code, @Nonnull final FormcycleExternalContext formcycle)
				throws ParseException, EvaluationException, IllegalArgumentException {
			final Node node = FormExpressionParsingUtil.Program.parse(code);
			return eval(node, formcycle);
		}

		public static ALangObject evalTemplate(@Nonnull final String code, @Nonnull final FormcycleExternalContext formcycle)
				throws ParseException, EvaluationException, IllegalArgumentException {
			final Node node = FormExpressionParsingUtil.Template.parse(code);
			return eval(node, formcycle);
		}

		public static ALangObject eval(@Nonnull final Node node, @Nonnull final FormcycleExternalContext formcycle)
				throws EvaluationException, IllegalArgumentException {
			return FormExpressionEvaluationUtil.eval(node, FormcycleEcFactory.getPoolInstance(), formcycle);
		}
	}

	private static  ALangObject eval(final Node node, final ObjectPool<IEvaluationContext> pool, final IExternalContext externalContext) throws EvaluationException {
		final IEvaluationContext ec;
		try {
			ec = pool.borrowObject();
		} catch (final Exception exception) {
			throw new CannotAcquireEvaluationContextException(exception);
		}
		ec.setExternalContext(externalContext);
		try {
			return EvaluateVisitor.evaluateCode(node, ec);
		} finally {
			try {
				pool.returnObject(ec);
			} catch (final Exception e) {
				LOG.error(String.format(CmnCnst.Error.POOL_FAILED_TO_RETURN_EC, ec), e);
			}
		}
	}
}
