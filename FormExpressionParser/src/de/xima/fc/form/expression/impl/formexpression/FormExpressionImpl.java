package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parsed.IComment;
import de.xima.fc.form.expression.iface.parsed.IFormExpression;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

class FormExpressionImpl implements IFormExpression {
	private final static Logger LOG = LoggerFactory.getLogger(FormExpressionImpl.class);
	private static final long serialVersionUID = 1L;

	@Nullable
	private transient String unparse;

	@Nonnull
	private final Node node;
	@Nonnull
	private final ImmutableList<IComment> comments;

	FormExpressionImpl(@Nonnull final Node node, @Nonnull final ImmutableList<IComment> comments) {
		this.node = node;
		this.comments = comments;
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nullable final IExternalContext ex)
			throws EvaluationException {
		ec.setExternalContext(ex);
		return EvaluateVisitor.evaluateCode(node, ec);
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nonnull final ObjectPool<IEvaluationContext> pool,
			@Nullable final IExternalContext ex) throws EvaluationException {
		final IEvaluationContext ec;
		try {
			ec = pool.borrowObject();
		}
		catch (final Exception exception) {
			throw new CannotAcquireEvaluationContextException(exception);
		}
		if (ec == null) {
			throw new CannotAcquireEvaluationContextException(new NullPointerException(CmnCnst.Error.EC_POOL_RETURNED_NULL));
		}
		try {
			return evaluate(ec, ex);
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

	@Nonnull
	@Override
	public String unparse(@Nullable final UnparseVisitorConfig config) {
		if (unparse != null) return unparse;
		return unparse = UnparseVisitor.unparse(node, comments,
				config != null ? config : UnparseVisitorConfig.getDefaultConfig());
	}

	@Override
	public ImmutableList<IComment> getComments() {
		return comments;
	}
}
