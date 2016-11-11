package de.xima.fc.form.expression.impl.tracer;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.grammar.Node;

public class GenericTracer implements ITracer<Node> {
	@Nullable private Node processed;
	@Nonnull private Node[] stackTrace;
	private int pos;
	private final int initialSize;

	public GenericTracer() {
		this(16);
	}

	public GenericTracer(int initialSize) {
		if (initialSize < 1)
			initialSize = 1;
		stackTrace = new Node[initialSize];
		pos = -1;
		this.initialSize = initialSize;
	}

	@Override
	public void setCurrentlyProcessed(@Nullable final Node object) {
		processed = object;
	}

	@Override
	public Node getCurrentlyProcessed() {
		return processed;
	}

	@SuppressWarnings("null")
	@Override
	public void descend(final Node node) {
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
	public void reset() {
		Arrays.fill(stackTrace, null);
		if (stackTrace.length != initialSize)
			stackTrace = new Node[initialSize];
		pos = -1;
	}
}
