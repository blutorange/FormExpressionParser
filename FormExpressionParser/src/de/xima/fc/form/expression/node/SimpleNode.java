package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.util.CmnCnst;

public abstract class SimpleNode implements Node {
	private static final long serialVersionUID = 1L;
	private final static AtomicInteger ID_PROVIDER = new AtomicInteger();

	/**
	 * The id of this node. Can be anything as long as it is unique for each
	 * node of a parse tree. Does not have to be unique for nodes of different
	 * parse trees. Does not need to be the same for multiple runs of the
	 * program.
	 */
	protected final int uniqueId;

	/** Reference to the list of all comments. It is set when
	 * the node is created, but might be filled later, but it will
	 * be filled once parsing is done.
	 */
	private final List<Token> comments;
	protected String embedment;
	/** ID of the node type. See {@link FormExpressionParserTreeConstants#jjtNodeName}. */
	protected int nodeId;
	/** Parent of this node, or <code>null</code>. */
	protected Node parent;
	/** Children of this node, non-null. */
	@Nonnull
	protected Node[] children = CmnCnst.EMPTY_NODE_ARRAY;
	/** Used during evaluation. */
	private EMethod siblingMethod;
	/** Line numbers for tracing etc. */
	private int beginLine=1,beginColumn=1,endLine=1,endColumn=1;

	/**
	 * @param nodeId
	 *            Node id. Not needed (yet).
	 */
	public SimpleNode(final FormExpressionParser parser, final int nodeId) {
		// This will always provide a unique ID for each node of a
		// parse tree, even if idProvider overflows and wraps around,
		// unless a parse tree contains more than 2^32 nodes, which
		// by itself would raise many other issues...
		uniqueId = ID_PROVIDER.incrementAndGet();
		this.nodeId = nodeId;
		this.comments = parser.getComments();
		this.embedment = parser.getCurrentEmbedmentContext();
	}

	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc.
	@Override
	public void jjtOpen() {
	}

	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc. @Override
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
		// throw new UnsupportedOperationException("Getting parents has not been
		// neccessary as of now, uncomment to enable.");
		return parent;
	}

	@Override
	public void jjtAddChild(final Node n, final int i) throws IndexOutOfBoundsException {
		if (n == null) throw new IndexOutOfBoundsException(CmnCnst.Error.NULL_CHILD_NODE);
		if (i >= children.length) {
			final Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	@SuppressWarnings("null") // checked in method addChild
	@Nonnull
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
		sb.append(nodeName())
		.append('(')
		.append(embedment)
		.append(',')
		.append(siblingMethod)
		.append(',')
		.append(getStartLine()).append(':')
		.append(getStartColumn())
		.append('-')
		.append(getEndLine())
		.append(':')
		.append(getEndColumn())
		.append(',')
		.append(',');
		additionalToStringFields(sb);
		sb.setLength(sb.length() - 1);
		sb.append(')');
		return sb.toString();
	}

	/**
	 * @return A unique ID for this node.
	 */
	@Override
	public int getId() {
		return uniqueId;
	}

	@Nonnull
	@Override
	public Node[] getChildArray() {
		return children;
	}

	@Override
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start) throws ParseException {
		return getChildArrayAs(clazz, start, children.length - 1);
	}

	@Override
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start, int end) throws ParseException {
		if (end < start)
			end = start;
		@SuppressWarnings("unchecked")
		final T[] args = (T[]) Array.newInstance(clazz, end - start + 1);
		for (int i = start; i <= end; ++i) {
			final Node n = children[i];
			if (!clazz.isAssignableFrom(n.getClass()))
				throw new ParseException(String.format(
						CmnCnst.Error.NODE_INCORRECT_TYPE,
						n.getClass().getSimpleName(), clazz.getSimpleName()));
			args[i - start] = clazz.cast(n);
		}
		return args;
	}

	@Override
	public <T extends Node> T getNthChildAs(final int index, final Class<T> clazz) throws ParseException {
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass()))
			throw new ParseException(String.format(CmnCnst.Error.NODE_INCORRECT_TYPE, n.getClass().getSimpleName(), clazz.getSimpleName()));
		return clazz.cast(n);
	}

	@Override
	public <T extends Node> T getNthChildAsOrNull(final int index, final Class<T> clazz) {
		final Node n = children[0];
		if (!clazz.isAssignableFrom(n.getClass()))
			return null;
		return clazz.cast(n);
	}

	@Override
	@Nullable
	public Node getLastChild() {
		if (children.length == 0)
			return null;
		return children[children.length - 1];
	}

	@Override
	@Nullable
	public Node getFirstChild() {
		if (children.length == 0)
			return null;
		return children[0];
	}

	@Override
	public EMethod getSiblingMethod() {
		return siblingMethod;
	}

	@Override
	public void assertChildrenBetween(final int atLeast, final int atMost) throws ParseException {
		if (children.length < atLeast || children.length > atMost)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_BETWEEN,
					new Integer(atLeast), new Integer(atMost), new Integer(children.length)));
	}

	@Override
	public void assertChildrenExactly(final int count) throws ParseException {
		if (children.length != count)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_EXACTLY,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public void assertChildrenExactlyOneOf(final int count1, final int count2) throws ParseException {
		if (children.length != count1 && children.length != count2)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_EXACTLY_ONE_OF,
					new Integer(count1), new Integer(count2), new Integer(children.length)));
	}

	@Override
	public void assertChildrenAtLeast(final int count) throws ParseException {
		if (children.length < count)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_AT_LEAST,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public void assertChildrenAtMost(final int count) throws ParseException {
		if (children.length > count)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_AT_MOST,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public void assertChildrenEven() throws ParseException {
		if ((children.length & 1) != 0)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_NOT_EVEN,
					new Integer(children.length)));
	}

	@Override
	public void assertChildrenOdd() throws ParseException {
		if ((children.length & 1) != 1)
			throw new ParseException(String.format(
					CmnCnst.Error.NODE_COUNT_NOT_ODD,
					new Integer(children.length)));
	}

	@Override
	public final List<Token> getComments() {
		return comments;
	}

	@Override
	public final int getStartLine() {
		return beginLine;
	}

	@Override
	public final int getStartColumn() {
		return beginColumn;
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
	public void jjtSetFirstToken(final Token token) {
		beginLine = token.beginLine;
		beginColumn = token.beginColumn;
	}

	@Override
	public void jjtSetLastToken(final Token token) {
		endLine = token.endLine;
		endColumn = token.endColumn;
	}

	@Nonnull
	@Override
	public String getMethodName() {
		if (this instanceof ASTFunctionClauseNode) {
			return ((ASTFunctionClauseNode) this).getFunctionName();
		} else if (this instanceof ASTFunctionNode) {
			return CmnCnst.TRACER_POSITION_NAME_ANONYMOUS_FUNCTION;
		}
		return parent == null ? CmnCnst.TRACER_POSITION_NAME_GLOBAL : parent.getMethodName();
	}

	@Override
	@Nullable
	public final String getEmbedment() {
		return embedment;
	}

	/**
	 * Subclasses may add additional info for {@link #toString()}.
	 *
	 * @param sb
	 *            String builder to use.
	 */
	protected void additionalToStringFields(final StringBuilder sb) {
	}

	protected String nodeName() {
		return getClass().getSimpleName();
	}

	/**
	 * @param from Child where to start.
	 * @param to Child where to end.
	 * @param assignType Type of assignment, for the error message.
	 * @throws ParseException When any children in the range [from,to) are not assignable.
	 */
	protected void assertChildrenAssignable(final int from, final int to, final String assignType) throws ParseException {
		for (int i = from; i < to; ++i) {
			switch (children[i].jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				break;
			case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
				final Node pen = ((ASTPropertyExpressionNode)children[i]).getLastChild();
				if (pen == null || pen.getSiblingMethod() == EMethod.PARENTHESIS) {
					// Cannot do assignment a.foobar() = 42;
					final String msg = String.format(
							CmnCnst.Error.ILLEGAL_LVALUE_FUNCTION, children[i],
							assignType, new Integer(getStartLine()), new Integer(getStartColumn()));
					throw new ParseException(msg);
				}
				break;
			default:
				final String msg = String.format(CmnCnst.Error.ILLEGAL_LVALUE,
						children[i].getClass().getSimpleName(), assignType, new Integer(getStartLine()),
						new Integer(getStartColumn()));
				throw new ParseException(msg);
			}
		}
	}

	public void init(final EMethod method) throws ParseException {
		this.siblingMethod = method;
	}
}