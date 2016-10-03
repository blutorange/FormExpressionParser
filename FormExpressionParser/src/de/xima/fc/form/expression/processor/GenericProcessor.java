package de.xima.fc.form.expression.processor;

import de.xima.fc.form.expression.grammar.Node;

public abstract class GenericProcessor<R, T, E extends Exception> {
	private final SimpleStack<Node> nodeStack = new SimpleStack<>();
	private final SimpleStack<IProcessorTask<R, T, E>> taskStack = new SimpleStack<>();
	final SimpleStack<R> returnStack = new SimpleStack<>();
	final SimpleStack<Object> argObjectStack = new SimpleStack<>();
	final T object;
	Node currentNode;
	Node[] currentChildren;

	protected final IProcessorTask<R, T, E> tasksForNode[];

	protected GenericProcessor(final T object, final IProcessorTask<R, T, E> tasksForNode[]) {
		this.object = object;
		this.tasksForNode = tasksForNode;
	}

	public R process(final Node initialNode) throws E {
		nodeStack.clear();
		taskStack.clear();
		returnStack.clear();
		argObjectStack.clear();
		queueNode(initialNode);
		return processInternal();
	}

	private R processInternal() throws E {
		do {
			currentNode = nodeStack.peek();
			currentChildren = currentNode.getChildArray();
			taskStack.pop().process(this);
		} while(!taskStack.isEmpty());

		if (returnStack.size() != 1) throw new RuntimeException();
		return returnStack.pop();
	}


	void queueNode(final Node node) {
		nodeStack.push(node);
		taskStack.push(tasksForNode[node.jjtGetNodeId()]);
	}

	void queueNode(final Node node, final IProcessorTask<R, T, E> taskAfterwards) {
		nodeStack.push(node);
		taskStack.push(taskAfterwards);
		taskStack.push(tasksForNode[node.jjtGetNodeId()]);
	}

	void queueChild(final int index) {
		nodeStack.push(currentChildren[index]);
		taskStack.push(tasksForNode[currentChildren[index].jjtGetNodeId()]);
	}

	void queueChild(final int index, final IProcessorTask<R, T, E> taskAfterwards) {
		nodeStack.push(currentChildren[index]);
		taskStack.push(taskAfterwards);
		taskStack.push(tasksForNode[currentChildren[index].jjtGetNodeId()]);
	}

	/**
	 * Evaluates the child and returns the return value of that child as the
	 * return value of the currently processed node. This assumes that this
	 * node is still on the stack. Otherwise, just use {@link #queueChild(int)}.
	 *
	 * @param index Child to evaluate.
	 */
	void tailCallChild(final int index) {
		nodeStack.pop();
		nodeStack.push(currentChildren[index]);
		taskStack.push(tasksForNode[currentChildren[index].jjtGetNodeId()]);
	}

	void endNode() {
		nodeStack.pop();
	}
}
