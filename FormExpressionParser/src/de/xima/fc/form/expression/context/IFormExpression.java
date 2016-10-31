package de.xima.fc.form.expression.context;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.pool2.ObjectPool;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public interface IFormExpression extends Serializable {
	public ALangObject evaluate(@Nonnull final ObjectPool<IEvaluationContext> pool,
			@Nonnull final IExternalContext externalContext) throws EvaluationException;

	/**
	 *
	 * @param config When <code>null</code>, uses some default configuration.
	 * @return
	 */
	@Nonnull
	public String unparse(@Nullable UnparseVisitorConfig config);

	@Deprecated
	public Node getRootNode();
}
