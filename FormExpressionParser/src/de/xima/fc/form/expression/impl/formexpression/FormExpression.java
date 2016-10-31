package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.context.IFormExpression;
import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

class FormExpression implements IFormExpression {
	private final static Logger LOG = LoggerFactory.getLogger(FormExpression.class);
	private static final long serialVersionUID = 1L;

	@Nullable
	private transient String unparse;
	@Nonnull
	private final Node node;

	public FormExpression(@Nonnull final Node node) {
		this.node = node;
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nonnull final ObjectPool<IEvaluationContext> pool,
			@Nonnull final IExternalContext externalContext) throws EvaluationException {
		final IEvaluationContext ec;
		try {
			ec = pool.borrowObject();
		}
		catch (final Exception exception) {
			throw new CannotAcquireEvaluationContextException(exception);
		}
		ec.setExternalContext(externalContext);
		try {
			return EvaluateVisitor.evaluateCode(node, ec);
		}
		finally {
			try {
				pool.returnObject(ec);
			}
			catch (final Exception e) {
				LOG.error(String.format(CmnCnst.Error.POOL_FAILED_TO_RETURN_EC, ec), e);
			}
		}
	}

	@Override
	public String unparse(final UnparseVisitorConfig config) {
		if (unparse != null) return unparse;
		return unparse = UnparseVisitor.unparse(node, config == null ? UnparseVisitorConfig.getDefaultConfig() : config);
	}

	@Override
	public Node getRootNode() {
		return node;
	}

}
