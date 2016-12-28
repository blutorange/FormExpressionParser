package de.xima.fc.form.expression.impl.formexpression;

import java.util.List;

import javax.annotation.Nonnull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationResult;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.parse.IComment;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.SimulateVisitor;
import de.xima.fc.form.expression.visitor.UnusedVariableCheckVisitor;

@NonNullByDefault
class FormExpressionImpl<T> implements IFormExpression<T> {
	private static final long serialVersionUID = 1L;

	private final int symbolTableSize;
	private final IScopeDefinitions scopeDefs;
	private final IEvaluationContextContract<T> ecFactory;
	private final Node node;
	private final ImmutableList<IComment> comments;
	private ELogLevel logLevel;
	private String logName;

	FormExpressionImpl(final Node node, final ImmutableList<IComment> comments,
			final IScopeDefinitions scopeDefs, final IEvaluationContextContract<T> specs,
			final int heapSize) {
		this.node = node;
		this.comments = comments;
		this.ecFactory = specs;
		this.scopeDefs = scopeDefs;
		this.symbolTableSize = heapSize;
		this.logLevel = ELogLevel.WARN;
		this.logName = NullUtil.stringFormat(CmnCnst.Name.DEFAULT_LOGGER_NAME, Integer.valueOf(hashCode()));
	}

	@Override
	@Nonnull
	public IEvaluationResult evaluate(@Nonnull final T object) throws EvaluationException {
		Preconditions.checkNotNull(object, CmnCnst.Error.NULL_EXTERNAL_CONTEXT_OBJECT);
		final IEvaluationContext ec = makeEc(object);
		@Nonnull final List<IEvaluationWarning> warnings;
		final ALangObject result;
		try {
			result = EvaluateVisitor.evaluateCode(node, scopeDefs, symbolTableSize, ec);
			warnings = ec.getTracer().buildWarnings();
		}
		finally {
			ec.reset();
		}
		return new ResImpl(result, warnings);
	}

	@Override
	public ImmutableList<IComment> getComments() {
		return comments;
	}

	@Override
	public IEvaluationContextContract<T> getSpecs() {
		return ecFactory;
	}

	@Override
	public List<IEvaluationWarning> analyze(final T object) throws EvaluationException {
		Preconditions.checkNotNull(object, CmnCnst.Error.NULL_EXTERNAL_CONTEXT_OBJECT);
		final IEvaluationContext ec = makeEc(object);
		@Nonnull final List<IEvaluationWarning> result;
		try {
			SimulateVisitor.simulate(node, scopeDefs, ec);
			UnusedVariableCheckVisitor.check(node, scopeDefs, symbolTableSize, ec);
			result = ec.getTracer().buildWarnings();
		}
		finally {
			ec.reset();
		}
		return result;
	}

	private IEvaluationContext makeEc(final T object) {
		final IEvaluationContext ec = new EvaluationContextImpl(ecFactory, logName, logLevel);
		ec.setExternalContext(ecFactory.getExternalFactory().make(object));
		return ec;
	}

	private class ResImpl implements IEvaluationResult {
		private final ALangObject object;
		private final List<IEvaluationWarning> warnings;
		public ResImpl(final ALangObject object, final List<IEvaluationWarning> warnings){
			this.object = object;
			this.warnings = warnings;
		}
		@Override
		public ALangObject getObject() {
			return object;
		}
		@Override
		public List<IEvaluationWarning> getWarnings() {
			return warnings;
		}
	}

	@Override
	public IFormExpression<T> setLogLevel(final ELogLevel logLevel) {
		this.logLevel = NullUtil.or(logLevel, this.logLevel);
		return this;
	}

	@Override
	public IFormExpression<T> setLogName(final String logName) {
		this.logName = NullUtil.or(logName, this.logName);
		return this;
	}
}