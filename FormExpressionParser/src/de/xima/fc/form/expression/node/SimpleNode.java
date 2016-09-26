/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public abstract class SimpleNode implements Node {

	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];
	private final static AtomicInteger ID_PROVIDER = new AtomicInteger();

	/**
	 * The id of this node. Can be anything as long as it is unique for each
	 * node of a parse tree. Does not have to be unique for nodes of different
	 * parse trees. Does not need to be the same for multiple runs of the program.
	 */
	protected final int uniqueId;

	protected Node[] children = EMPTY_NODE_ARRAY;
	protected EMethod siblingMethod;

	/**
	 * @param nodeId Node id. Not needed (yet).
	 */
	public SimpleNode(final int nodeId) {
		// This will always provide a unique ID for each node of a
		// parse tree, even if idProvider overflows and wraps around,
		// unless a parse tree contains more than 2^32 nodes, which
		// by itself would raise many other issues...
		uniqueId = ID_PROVIDER.incrementAndGet();
	}

	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc.
	@Override
	public void jjtOpen() {
	}

	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc.	@Override
	@Override
	public void jjtClose() {
	}

	@Override
	public void jjtSetParent(final Node n) {
		//parent = n;
	}

	@Override
	public Node jjtGetParent() {
		throw new UnsupportedOperationException("Getting parents has not been neccessary as of now, uncomment to enable.");
		//return parent;
	}

	@Override
	public void jjtAddChild(final Node n, final int i) {
		if (i >= children.length) {
			final Node c[] = new Node[i+1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	@Override
	public Node jjtGetChild(final int i) throws ArrayIndexOutOfBoundsException {
		return children[i];
	}

	@Override
	public int jjtGetNumChildren() {
		return children.length;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public int getId() {
		return uniqueId;
	}

	@Override
	public Node[] getChildArray() {
		return children;
	}

	@Override
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start) throws ParseException {
		return getChildArrayAs(clazz, start, children.length-1);
	}

	@Override
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start, int end) throws ParseException {
		if (end < start) end = start;
		@SuppressWarnings("unchecked")
		final T[] args = (T[])Array.newInstance(clazz, end-start+1);
		for (int i = start; i <= end; ++i) {
			final Node n = children[i];
			if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException(String.format("Node type is %s, expected %s.", n.getClass().getSimpleName(), clazz.getSimpleName()));
			args[i-start] = clazz.cast(n);
		}
		return args;
	}

	@Override
	public <T extends Node> T getNthChildAs(final int index, final Class<T> clazz) throws ParseException {
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass())) throw new ParseException("node not the correct type: " + n.getClass());
		return clazz.cast(n);
	}

	@Override
	public Node getLastChild() throws ParseException {
		if (children.length == 0) throw new ParseException("Node does not contain any children.");
		return children[children.length-1];
	}

	@Override
	public EMethod getSiblingMethod() {
		return siblingMethod;
	}

	@Override
	public void assertChildrenBetween(final int atLeast, final int atMost) throws ParseException {
		if (children.length < atLeast || children.length > atMost)
			throw new ParseException(
					"Node must have between " + atLeast + " and " + atMost + " children, but has " + children.length);
	}

	@Override
	public void assertChildrenExactly(final int count) throws ParseException {
		if (children.length != count)
			throw new ParseException("Node must have exactly " + count + " children, not " + children.length);
	}

	@Override
	public void assertChildrenAtLeast(final int count) throws ParseException {
		if (children.length < count)
			throw new ParseException("Node must have at least " + count + " children, but has " + children.length);
	}

	@Override
	public void assertChildrenAtMost(final int count) throws ParseException {
		if (children.length > count)
			throw new ParseException("Node can have at most " + count + " children, but has " + children.length);
	}

	@Override
	public void assertChildrenEven() throws ParseException {
		if ((children.length & 1) != 0) throw new ParseException("Node count is not even: " + children.length);
	}

	@Override
	public void assertChildrenOdd() throws ParseException {
		if ((children.length & 1) != 1) throw new ParseException("Node count is not odd: " + children.length);
	}
}