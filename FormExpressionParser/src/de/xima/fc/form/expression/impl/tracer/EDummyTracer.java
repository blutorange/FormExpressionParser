package de.xima.fc.form.expression.impl.tracer;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.evaluate.ITracer;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * A tracer that is not tracing anything. Use this when you
 * do not need tracing functionality.
 *
 * @author mad_gaksha
 *
 */
public enum EDummyTracer implements ITracer<Node> {
	INSTANCE;
	@Override
	public void setCurrentlyProcessed(final Node object) {
	}

	@Override
	public Node getCurrentlyProcessed() {
		return null;
	}

	@Override
	public void descend() {
	}

	@Override
	public void ascend() {
	}

	@Override
	public Node[] getStackTrace() {
		return CmnCnst.NonnullConstant.EMPTY_NODE_ARRAY;
	}

	@Override
	public void reset() {
	}

	@Override
	public void appendWarning(final IEvaluationWarning warning) {
	}

	@Override
	public ImmutableList<IEvaluationWarning> buildWarnings() {
		return NullUtil.checkNotNull(ImmutableList.<IEvaluationWarning>of());
	}

	@Override
	public void enableWarnings() {
	}

	@Override
	public void disableWarnings() {
	}

	@Override
	public boolean isWarningsEnabled() {
		return false;
	}
}