/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.util.CmnCnst;

public abstract class SimpleNode implements Node {

	private final static Node[] EMPTY_NODE_ARRAY = new Node[0];
	private final static AtomicInteger ID_PROVIDER = new AtomicInteger();

	/**
	 * The id of this node. Can be anything as long as it is unique for each
	 * node of a parse tree. Does not have to be unique for nodes of different
	 * parse trees. Does not need to be the same for multiple runs of the program.
	 */
	protected final int uniqueId;

	private transient Token startToken;
	protected int nodeId;
	protected Node parent;
	protected Node[] children = EMPTY_NODE_ARRAY;
	protected EMethod siblingMethod;
	private int startLine, endLine, startColumn, endColumn;

	/**
	 * @param nodeId Node id. Not needed (yet).
	 */
	public SimpleNode(final int nodeId) {
		// This will always provide a unique ID for each node of a
		// parse tree, even if idProvider overflows and wraps around,
		// unless a parse tree contains more than 2^32 nodes, which
		// by itself would raise many other issues...
		uniqueId = ID_PROVIDER.incrementAndGet();
		this.nodeId = nodeId;
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
		parent = n;
	}

	/**
	 * @return The id for this TYPE of node.
	 * @see FormExpressionParserTreeConstants
	 */
	@Override
	public int jjtGetNodeId() {
		return nodeId;
	}

	@Override
	public Node jjtGetParent() {
		//throw new UnsupportedOperationException("Getting parents has not been neccessary as of now, uncomment to enable.");
		return parent;
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
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(nodeName());
		sb.append("(");
		sb.append(siblingMethod);
		sb.append(",");
		sb.append(startLine);
		sb.append(":");
		sb.append(startColumn);
		sb.append("-");
		sb.append(endLine);
		sb.append(":");
		sb.append(endColumn);
		sb.append(",");
		additionalToStringFields(sb);
		sb.setLength(sb.length()-1);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @return A unique ID for this node.
	 */
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
	@Nullable
	public Node getLastChild() {
		if (children.length == 0) return null;
		return children[children.length-1];
	}

	@Override
	@Nullable
	public Node getFirstChild() {
		if (children.length == 0) return null;
		return children[0];
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

	@Override
	public final void setStartPosition(final Token t) {
		startLine = t.beginLine;
		startColumn = t.beginColumn;
		startToken = t;
	}
	@Override
	public final void setEndPosition(final Token t) {
		final Token next = startToken == null ? null : startToken.next;
		if (next != null) {
			startLine = next.beginLine;
			startColumn = next.beginColumn;
		}
		startToken = null;
		endLine = t.endLine;
		endColumn = t.endColumn;
	}
	@Override
	public final int getStartLine() {
		return startLine;
	}
	@Override
	public final int getStartColumn() {
		return startColumn;
	}
	@Override
	public final int getEndLine() {
		return endLine;
	}
	@Override
	public final int getEndColumn() {
		return endColumn;
	}

	@Override
	public String getMethodName() {
		if (this instanceof ASTFunctionClauseNode) {
			return ((ASTFunctionClauseNode)this).getFunctionName();
		}
		else if (this instanceof ASTFunctionNode) {
			return CmnCnst.TRACER_POSITION_NAME_ANONYMOUS_FUNCTION;
		}
		return parent == null ? CmnCnst.TRACER_POSITION_NAME_GLOBAL : parent.getMethodName();
	}

	/**
	 * Subclasses may add additional info for {@link #toString()}.
	 * @param sb String builder to use.
	 */
	protected void additionalToStringFields(final StringBuilder sb) {
	}

	protected String nodeName() {
		return getClass().getSimpleName();
	}
}