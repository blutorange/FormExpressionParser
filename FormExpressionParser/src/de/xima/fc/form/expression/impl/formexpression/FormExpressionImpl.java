package de.xima.fc.form.expression.impl.formexpression;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.parse.IComment;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.SimulateVisitor;
import de.xima.fc.form.expression.visitor.UnusedVariableCheckVisitor;

@ParametersAreNonnullByDefault
class FormExpressionImpl<T> implements IFormExpression<T> {
	private static final long serialVersionUID = 1L;

	private final int symbolTableSize;
	private final IScopeDefinitions scopeDefs;
	private final IEvaluationContextContractFactory<T> ecFactory;
	private final Node node;
	private final ImmutableList<IComment> comments;

	FormExpressionImpl(final Node node, final ImmutableList<IComment> comments,
			final IScopeDefinitions scopeDefs, final IEvaluationContextContractFactory<T> specs,
			final int heapSize) {
		this.node = node;
		this.comments = comments;
		this.ecFactory = specs;
		this.scopeDefs = scopeDefs;
		this.symbolTableSize = heapSize;
	}

	@Override
	@Nonnull
	public ALangObject evaluate(@Nonnull final T object) throws EvaluationException {
		Preconditions.checkNotNull(object, CmnCnst.Error.NULL_EXTERNAL_CONTEXT);
		final IEvaluationContext ec = makeEc(object);
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
		return ecFactory;
	}

	@Override
	public ImmutableCollection<IEvaluationWarning> analyze(final T object) throws EvaluationException {
		Preconditions.checkNotNull(object, CmnCnst.Error.NULL_EXTERNAL_CONTEXT);
		final IEvaluationContext ec = makeEc(object);
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

	private IEvaluationContext makeEc(final T object) {
		final IEvaluationContext ec = new EvaluationContextImpl(ecFactory);
		ec.setExternalContext(ecFactory.getExternalFactory().makeExternalContext(object));
		return ec;
	}
}