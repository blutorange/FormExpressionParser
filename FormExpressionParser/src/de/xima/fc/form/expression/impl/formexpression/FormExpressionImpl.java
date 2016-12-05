package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IComment;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.SimulateVisitor;
import de.xima.fc.form.expression.visitor.UnusedVariableCheckVisitor;

class FormExpressionImpl<T extends IExternalContext> implements IFormExpression<T> {
	private static final long serialVersionUID = 1L;

	private final int symbolTableSize;
	@Nonnull
	private final IScopeDefinitions scopeDefs;
	@Nonnull
	private final IEvaluationContextContractFactory<T> specs;
	@Nonnull
	private final Node node;
	@Nonnull
	private final ImmutableList<IComment> comments;

	FormExpressionImpl(@Nonnull final Node node, @Nonnull final ImmutableList<IComment> comments,
			@Nonnull final IScopeDefinitions scopeDefs, @Nonnull final IEvaluationContextContractFactory<T> specs,
			final int heapSize) {
		this.node = node;
		this.comments = comments;
		this.specs = specs;
		this.scopeDefs = scopeDefs;
		this.symbolTableSize = heapSize;
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nonnull final T ex) throws EvaluationException {
		Preconditions.checkNotNull(ex, CmnCnst.Error.NULL_EXTERNAL_CONTEXT);
		final IEvaluationContext ec = specs.getContextWithExternal(ex);
		ec.createSymbolTable(symbolTableSize);
		final ALangObject result = EvaluateVisitor.evaluateCode(node, scopeDefs, ec);
		ec.reset();
		return result;
	}
	@Override
	public ImmutableList<IComment> getComments() {
		return comments;
	}
	
	@Override
	public IEvaluationContextContractFactory<T> getSpecs() {
		return specs;
	}

	@Override
	public ImmutableCollection<IEvaluationWarning> analyze(final T ex) throws EvaluationException {
		Preconditions.checkNotNull(ex, CmnCnst.Error.NULL_EXTERNAL_CONTEXT);
		final IEvaluationContext ec = specs.getContextWithExternal(ex);
		final ImmutableCollection<IEvaluationWarning> result;
		try {
			SimulateVisitor.simulate(node, scopeDefs, ec);
			UnusedVariableCheckVisitor.check(node, scopeDefs, symbolTableSize, ec);
			result = ImmutableList.copyOf(ec.getTracer().getWarnings());
		}
		finally {
			ec.reset();
		}
		return result;
	}
}