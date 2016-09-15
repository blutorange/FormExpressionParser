package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;

public abstract class MySimpleNode extends SimpleNode {
	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];

	public MySimpleNode(final int id) {
		super(id);
	}

	public MySimpleNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	/**
	 * @return The string value of this node. Null when node cannot be evaluated.
	 * @throws ParseException
	 */
	public abstract ALangObject evaluate(IEvaluationContext ec) throws EvaluationException;

	public <T extends MySimpleNode> T getSingleChild(final Class<T> clazz) throws ParseException {
		if (jjtGetNumChildren() != 1) throw new ParseException("node does not have a single child");
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException("node not the correct type: " + n.getClass());
		return clazz.cast(n);
	}

	public MySimpleNode getSingleChild() throws ParseException {
		return getSingleChild(MySimpleNode.class);
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
