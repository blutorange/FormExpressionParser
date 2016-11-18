package de.xima.fc.form.expression.impl.tracer;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.ITracer;
import de.xima.fc.form.expression.util.CmnCnst;

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
}