package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public abstract class MySimpleNode extends SimpleNode {
	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];

	public MySimpleNode(final int id) {
		super(id);
	}

	public MySimpleNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	/**
	 * @return The evaluated value of this node. May return {@link NullLangObject}.
	 * @throws ParseException
	 */
	@NotNull
	public abstract ALangObject evaluate(@NotNull IEvaluationContext ec) throws EvaluationException;

	public final void toGraphviz(final StringBuilder builder, final String title) {
		builder.append("digraph G {").append(System.lineSeparator());
		final int hash = hashCode();
		builder.append("ROOT->").append(hash).append(System.lineSeparator()).append("ROOT").append(" [label=\"").append(StringEscapeUtils.escapeHtml4(title)).append("\"]").append(System.lineSeparator());
		MySimpleNode.recursiveGraphviz(this, hash, builder);
		builder.append("}");
	}

	/**
	 * @return The node graph as a Graphviz dot file for visualization.
	 */
	public final String toGraphviz(final String title) {
		final StringBuilder builder = new StringBuilder();
		toGraphviz(builder, title);
		return builder.toString();
	}

	private final static void recursiveGraphviz(final Node node, final int hash, final StringBuilder builder) {
		final int len = node.jjtGetNumChildren();
		final String label = StringEscapeUtils.escapeHtml4(node.toString());
		builder.append(hash).append(" [label=\"").append(label).append("\"]").append(System.lineSeparator());
		for (int i = 0; i != len ; ++i) {
			final Node child = node.jjtGetChild(i);
			final int childHash = child.hashCode();
			builder.append(hash).append("->").append(childHash).append(System.lineSeparator());
			recursiveGraphviz(child, childHash, builder);
		}
	}

	public <T extends MySimpleNode> T getSingleChild(final Class<T> clazz) throws ParseException {
		if (jjtGetNumChildren() != 1) throw new ParseException("node does not have a single child");
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException("node not the correct type: " + n.getClass());
		return clazz.cast(n);
	}

	public MySimpleNode getSingleChild() throws ParseException {
		return getSingleChild(MySimpleNode.class);
	}

	public MySimpleNode getSingleOrNoChild() throws ParseException {
		if (jjtGetNumChildren() == 0) return null;
		return getSingleChild();
	}

	public <T extends MySimpleNode> T[] getChildArray(final Class<T> clazz) throws ParseException {
		if (children == null) children = EMPTY_NODE_ARRAY;
		@SuppressWarnings("unchecked")
		final T[] args = (T[])Array.newInstance(clazz, children.length);
		for (int i = 0; i < args.length; ++i) {
			final Node n = children[i];
			if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException("node not the correct type: " + n.getClass());
			args[i] = clazz.cast(n);
		}
		return args;
	}

	public MySimpleNode[] getChildArray() throws ParseException {
		return getChildArray(MySimpleNode.class);
	}

}
