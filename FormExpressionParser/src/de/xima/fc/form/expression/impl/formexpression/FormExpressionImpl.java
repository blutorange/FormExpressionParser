package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IComment;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextProvider;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

class FormExpressionImpl<T extends IExternalContext> implements IFormExpression<T> {
	private static final long serialVersionUID = 1L;

	@Nullable
	private transient String unparse;

	@Nonnull
	private final IEvaluationContextProvider<T> factory;
	@Nonnull
	private final Node node;
	@Nonnull
	private final ImmutableList<IComment> comments;

	FormExpressionImpl(@Nonnull final Node node, @Nonnull final ImmutableList<IComment> comments, @Nonnull final IEvaluationContextProvider<T> factory) {
		this.node = node;
		this.comments = comments;
		this.factory = factory;
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nullable final T ex)
			throws EvaluationException {
		final IEvaluationContext ec = factory.getContextWithExternal(ex);
		return EvaluateVisitor.evaluateCode(node, ec);
	}

	@Nonnull
	@Override
	public String unparse(@Nullable final UnparseVisitorConfig config) {
		if (unparse != null)
			return unparse;
		return unparse = UnparseVisitor.unparse(node, comments,
				config != null ? config : UnparseVisitorConfig.getDefaultConfig());
	}

	@Override
	public ImmutableList<IComment> getComments() {
		return comments;
	}

	@Override
	public IEvaluationContextProvider<T> getFactory() {
		return factory;
	}
}
