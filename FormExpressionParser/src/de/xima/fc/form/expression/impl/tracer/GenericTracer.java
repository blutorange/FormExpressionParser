package de.xima.fc.form.expression.impl.tracer;

import java.util.Arrays;

import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;

public class GenericTracer implements ITracer<Node> {
	private Node processed;
	private Node[] stackTrace;
	private int pos;

	public GenericTracer() {
		this(16);
	}

	public GenericTracer(int initialSize) {
		if (initialSize < 1)
			initialSize = 1;
		stackTrace = new Node[initialSize];
		pos = -1;
	}

	@Override
	public void setCurrentlyProcessed(Node object) {
		processed = object;
	}

	@Override
	public Node getCurrentlyProcessed() {
		return processed;
	}

	@Override
	public void descend(Node node) {
		if (pos >= stackTrace.length - 1)
			stackTrace = Arrays.copyOf(stackTrace, pos * 2);
		stackTrace[++pos] = node;
	}

	@Override
	public void ascend() {
		--pos;
	}

	@Override
	public Node[] getStackTrace() {
		final Node[] newArray = new Node[pos+1];
		for (int i = pos; i >= 0; --i)
			newArray[pos-i] = stackTrace[i];
		return newArray;
	}

	@Override
	public Void reset() {
		pos = -1;
		return null;
	}	
}
