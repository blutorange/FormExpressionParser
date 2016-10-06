package de.xima.fc.form.expression.impl.tracer;

import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;

/**
 * A tracer that is not tracing anything. Use this when you
 * do not need tracing functionality.
 * 
 * @author mad_gaksha
 *
 */
public enum DummyTracer implements ITracer<Node> {
	INSTANCE;
	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];
	@Override
	public void setCurrentlyProcessed(Node object) {
	}

	@Override
	public Node getCurrentlyProcessed() {
		return null;
	}

	@Override
	public void descend(Node position) {
	}

	@Override
	public void ascend() {
	}

	@Override
	public Node[] getStackTrace() {
		return EMPTY_NODE_ARRAY;
	}

	@Override
	public Void reset() {
		return null;
	}
}