package de.xima.fc.form.expression.impl.tracer;

import java.util.Collections;
import java.util.List;

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
public enum DummyTracer implements ITracer<Node> {
	INSTANCE;
	@Override
	public void setCurrentlyProcessed(final Node object) {
	}

	@Override
	public Node getCurrentlyProcessed() {
		return null;
	}

	@Override
	public void descend(final Node position) {
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
	public List<IEvaluationWarning> buildWarnings() {
		return NullUtil.checkNotNull(Collections.<IEvaluationWarning>emptyList());
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