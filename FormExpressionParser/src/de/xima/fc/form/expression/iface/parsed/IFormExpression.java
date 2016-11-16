package de.xima.fc.form.expression.iface.parsed;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.pool2.ObjectPool;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IExternalContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public interface IFormExpression extends Serializable {
	@Nonnull
	public ALangObject evaluate(@Nonnull final ObjectPool<IEvaluationContext> pool,
			@Nullable final IExternalContext externalContext) throws EvaluationException;

	@Nonnull
	public ALangObject evaluate(@Nonnull final IEvaluationContext ec, @Nullable final IExternalContext ex)
			throws EvaluationException;

	/**
	 *
	 * @param config When <code>null</code>, uses some default configuration.
	 * @return
	 */
	@Nonnull
	public String unparse(@Nullable UnparseVisitorConfig config);

	@Nonnull
	public ImmutableList<IComment> getComments();

}
