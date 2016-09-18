package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public abstract class MySimpleNode extends SimpleNode {
	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];

	public MySimpleNode(final int id) {
		super(id);
	}

	public MySimpleNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public <T extends MySimpleNode> T getSingleChild(final Class<T> clazz) throws ParseException {
		if (jjtGetNumChildren() != 1) throw new ParseException("node does not have a single child");
		return getNthChild(0, clazz);
	}

	@Override
	public <T extends MySimpleNode> T[] getChildArray(final Class<T> clazz) throws ParseException {
		if (children == null) children = EMPTY_NODE_ARRAY;
		@SuppressWarnings("unchecked")
		final T[] args = (T[])Array.newInstance(clazz, children.length);
		for (int i = 0; i < args.length; ++i) {
			final Node n = children[i];
			if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException(String.format("Node type is %s, expected %s.", n.getClass().getSimpleName(), clazz.getSimpleName()));
			args[i] = clazz.cast(n);
		}
		return args;
	}

	@Override
	public <T extends MySimpleNode> T getNthChild(final int index, final Class<T> clazz) throws ParseException {
		if (index >= jjtGetNumChildren()) throw new ParseException("Node does not have at least " + (index+1) + "children.");
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException("node not the correct type: " + n.getClass());
		return clazz.cast(n);
	}

	@Override
	public Node getSingleChild() throws ParseException {
		if (children == null || children.length != 1) throw new ParseException("Node does not have a single child child: " + children);
		return children[0];
	}

	@Override
	public Node getFirstChild() throws ParseException {
		if (jjtGetNumChildren() < 1) throw new ParseException("Node must contain at least one child");
		return children[0];
	}

	@Override
	public Node[] getChildArray() {
		if (children == null) return EMPTY_NODE_ARRAY;
		return children;
	}


	//	private final static void recursiveGraphviz(final Node node, final int hash, final StringBuilder builder) {
	//		final int len = node.jjtGetNumChildren();
	//		final String label = StringEscapeUtils.escapeHtml4(node.toString());
	//		builder.append(hash).append(" [label=\"").append(label).append("\"]").append(System.lineSeparator());
	//		for (int i = 0; i != len ; ++i) {
	//			final Node child = node.jjtGetChild(i);
	//			final int childHash = child.hashCode();
	//			builder.append(hash).append("->").append(childHash).append(System.lineSeparator());
	//			recursiveGraphviz(child, childHash, builder);
	//		}
	//	}
}
