package de.xima.fc.form.expression.impl.tracer;

import org.apache.commons.lang3.ArrayUtils;

import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;

/**
 * Thread-safe instance.
 * 
 * @author mad_gaksha
 *
 */
public enum DummyTracer implements ITracer<Node> {
	INSTANCE;
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
		return (Node[]) ArrayUtils.EMPTY_OBJECT_ARRAY;
	}

}