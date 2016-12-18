package de.xima.fc.form.expression.node;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.commons.lang3.ArrayUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public abstract class ANode implements Node {
	private static final long serialVersionUID = 1L;
	private final static AtomicInteger ID_PROVIDER = new AtomicInteger();

	/**
	 * The id of this node. Can be anything as long as it is unique for each
	 * node of a parse tree. Does not have to be unique for nodes of different
	 * parse trees. Does not need to be the same for multiple runs of the
	 * program.
	 */
	private final int uniqueId;

	/**
	 * The embedment type of this node. When using templates, code can embedded
	 * in a plain text document with different embedment contexts, eg. [%code%] or
	 * [%$$code%]. <code>null</code> when there is no embedment.
	 */
	@Nullable private final String embedment;

	/** ID of the node type. See {@link FormExpressionParserTreeConstants#jjtNodeName}. */
	private final int nodeId;

	/** Parent of this node. <code>null</code> iff this is the top level node. */
	@Nullable private Node parent;

	/** List of this node's children. */
	@Nonnull
	protected Node[] children = CmnCnst.NonnullConstant.EMPTY_NODE_ARRAY;

	/** Used during evaluation, stores operator information. */
	@Nonnull private EMethod siblingMethod = EMethod.NONE;

	/** Line and column numbers for tracing, debugging etc. */
	private int beginLine = 1;
	/** Line and column numbers for tracing, debugging etc. */
	private int beginColumn = 1;
	/** Line and column numbers for tracing, debugging etc. */
	private int endLine = 1;
	/** Line and column numbers for tracing, debugging etc. */
	private int endColumn = 1;

	/**
	 * Do not use, exists only internally for javacc.
	 * @param nodeId
	 *            Node id. Not needed (yet).
	 */
	protected ANode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		// This will always provide a unique ID for each node of a
		// parse tree, even if idProvider overflows and wraps around,
		// unless a parse tree contains more than 2^32 nodes, which
		// by itself would raise many other issues...
		uniqueId = ID_PROVIDER.incrementAndGet();
		this.nodeId = nodeId;
		this.embedment = parser.getCurrentEmbedmentContext();
	}

	protected ANode(@Nonnull final Node prototype, final int nodeId) {
		// This will always provide a unique ID for each node of a
		// parse tree, even if idProvider overflows and wraps around,
		// unless a parse tree contains more than 2^32 nodes, which
		// by itself would raise many other issues...
		this.uniqueId = ID_PROVIDER.incrementAndGet();
		this.nodeId = nodeId;
		this.embedment = prototype.getEmbedment();
		this.beginColumn = prototype.getStartColumn();
		this.beginLine = prototype.getStartLine();
		this.endColumn = prototype.getEndColumn();
		this.endLine = prototype.getEndLine();
		this.siblingMethod = prototype.getSiblingMethod();
	}


	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc.
	@Override
	public final void jjtOpen() {
	}

	// For performance, calls to this method may be removed.
	// However, note that the files that call this method are
	// generated automatically by javacc. @Override
	@Override
	public final void jjtClose() {
	}

	@Override
	public final void jjtSetParent(final Node n) {
		parent = n;
	}

	/**
	 * @return The id for this TYPE of node.
	 * @see FormExpressionParserTreeConstants
	 */
	@Override
	public final int jjtGetNodeId() {
		return nodeId;
	}

	@Override
	public final Node jjtGetParent() {
		return parent;
	}

	@Override
	public final void jjtAddChild(@Nonnull final Node n, final int i) {
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
	public final Node jjtGetChild(final int i) throws ArrayIndexOutOfBoundsException {
		return children[i];
	}

	@Override
	public final int jjtGetNumChildren() {
		return children.length;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getNodeName())
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
	public final int getId() {
		return uniqueId;
	}

	/**
	 * @deprecated Use {@link #jjtGetChild(int)} and {@link #jjtGetNumChildren()}
	 */
	@Nonnull
	@Override
	@Deprecated
	public final Node[] getChildArray() {
		return children;
	}

	@Override
	public final <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start) throws ParseException {
		return getChildArrayAs(clazz, start, children.length - 1);
	}

	@Override
	public final <T extends Node> T[] getChildArrayAs(final Class<T> clazz, final int start, int end) throws ParseException {
		if (end < start)
			end = start;
		@SuppressWarnings("unchecked")
		final T[] args = (T[]) Array.newInstance(clazz, end - start + 1);
		for (int i = start; i <= end; ++i) {
			final Node n = children[i];
			if (!clazz.isAssignableFrom(n.getClass()))
				throw new ParseException(NullUtil.messageFormat(
						CmnCnst.Error.NODE_INCORRECT_TYPE,
						n.getClass().getSimpleName(), clazz.getSimpleName()));
			args[i - start] = clazz.cast(n);
		}
		return args;
	}

	@Override
	public final <T extends Node> T getNthChildAs(final int index, final Class<T> clazz) throws ParseException {
		final Node n = children[index];
		if (!clazz.isAssignableFrom(n.getClass()))
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INCORRECT_TYPE, n.getClass().getSimpleName(),
					clazz.getSimpleName()));
		return clazz.cast(n);
	}

	@Override
	public final <T extends Node> T getNthChildAsOrNull(final int index, @Nonnull final Class<T> clazz) {
		return children[index].getAsOrNull(clazz);
	}

	@Override
	public final <T extends Node> T getAsOrNull(@Nonnull final Class<T> clazz) {
		if (!clazz.isAssignableFrom(getClass()))
			return null;
		return clazz.cast(this);
	}

	@Override
	@Nullable
	public final Node getLastChildOrNull() {
		if (children.length == 0)
			return null;
		return children[children.length - 1];
	}

	@Override
	@Nullable
	public final Node getFirstChildOrNull() {
		if (children.length == 0)
			return null;
		return children[0];
	}

	@SuppressWarnings("null") // already checked when setting children
	@Override
	public final Node getFirstChild() throws ParseException {
		if (children.length == 0)
			throw new ParseException(CmnCnst.Error.NODE_WITHOUT_CHILDREN);
		return children[0];
	}

	@SuppressWarnings("null") // already checked when setting children
	@Override
	public final Node getLastChild() throws ParseException {
		if (children.length == 0)
			throw new ParseException(CmnCnst.Error.NODE_WITHOUT_CHILDREN);
		return children[children.length-1];
	}

	@Override
	public final EMethod getSiblingMethod() {
		return siblingMethod;
	}

	@Override
	public final void assertChildrenBetween(final int atLeast, final int atMost) throws ParseException {
		if (children.length < atLeast || children.length > atMost)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_BETWEEN,
					new Integer(atLeast), new Integer(atMost), new Integer(children.length)));
	}

	@Override
	public final void assertChildrenExactly(final int count) throws ParseException {
		if (children.length != count)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_EXACTLY,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public final void assertChildrenExactlyOneOf(final int count1, final int count2) throws ParseException {
		if (children.length != count1 && children.length != count2)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_EXACTLY_ONE_OF,
					new Integer(count1), new Integer(count2), new Integer(children.length)));
	}

	@Override
	public final void assertChildrenAtLeast(final int count) throws ParseException {
		if (children.length < count)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_AT_LEAST,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public final void assertChildrenAtMost(final int count) throws ParseException {
		if (children.length > count)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_AT_MOST,
					new Integer(count), new Integer(children.length)));
	}

	@Override
	public final void assertChildrenEven() throws ParseException {
		if ((children.length & 1) != 0)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_NOT_EVEN,
					new Integer(children.length)));
	}

	@Override
	public final void assertChildrenOdd() throws ParseException {
		if ((children.length & 1) != 1)
			throw new ParseException(NullUtil.messageFormat(
					CmnCnst.Error.NODE_COUNT_NOT_ODD,
					new Integer(children.length)));
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
	public final void jjtSetFirstToken(final Token token) {
		beginLine = token.beginLine;
		beginColumn = token.beginColumn;
	}

	@Override
	public final void jjtSetLastToken(final Token token) {
		endLine = token.endLine;
		endColumn = token.endColumn;
	}

	@Nonnull
	@Override
	public final String getMethodName() {
		if (this instanceof ASTFunctionClauseNode) {
			return ((ASTFunctionClauseNode) this).getCanonicalName();
		} else if (this instanceof ASTFunctionNode) {
			return CmnCnst.TRACER_POSITION_NAME_ANONYMOUS_FUNCTION;
		}
		return parent != null ? parent.getMethodName() : CmnCnst.TRACER_POSITION_NAME_GLOBAL;
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
	protected void additionalToStringFields(@Nonnull final StringBuilder sb) {
	}

	@Override
	@Nonnull
	public String getNodeName() {
		return NullUtil.checkNotNull(getClass().getSimpleName());
	}

	/**
	 * @param from Child where to start.
	 * @param to Child where to end.
	 * @param assignType Type of assignment, for the error message.
	 * @throws ParseException When any children in the range [from,to) are not assignable.
	 */
	protected final void assertChildrenAssignable(final int from, final int to, final String assignType) throws ParseException {
		for (int i = from; i < to; ++i) {
			switch (children[i].jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				break;
			case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
				final Node pen = ((ASTPropertyExpressionNode)children[i]).getLastChildOrNull();
				if (pen == null || pen.getSiblingMethod() == EMethod.PARENTHESIS) {
					// Cannot do assignment a.foobar() = 42;
					final String msg = NullUtil.messageFormat(
							CmnCnst.Error.ILLEGAL_LVALUE_FUNCTION, children[i],
							assignType, new Integer(getStartLine()), new Integer(getStartColumn()));
					throw new ParseException(msg);
				}
				break;
			default:
				final String msg = NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_LVALUE,
						children[i].getClass().getSimpleName(), assignType, children[i].getStartLine(),
						children[i].getStartColumn());
				throw new ParseException(msg);
			}
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void init(@Nullable final EMethod method) throws ParseException {
		if (method != null)
			this.siblingMethod = method;
	}

	@Override
	@Nonnull
	public final <T> T assertNonNull(@Nullable final T object, @Nonnull final String errMessage) throws ParseException {
		if (object == null)
			throw new ParseException(errMessage);
		return object;
	}

	@Override
	public final Node detach() throws SemanticsException {
		final Node p = parent;
		if (p != null) {
			for (int i = p.jjtGetNumChildren(); i --> 0;) {
				if (p.jjtGetChild(i).getId() == uniqueId) {
					p.clearChild(i);
					parent = null;
					return this;
				}
			}
			throw new SemanticsException(CmnCnst.Error.BAD_AST_PARENT_CHILD, this);
		}
		return this;
	}

	/**
	 * Clears the child at the given index, either by removing it
	 * completely or replacing it with an appropriate empty node.
	 * Furthermore, other children may be removed or changed
	 * for some node types.
	 * @param i Child to clear.
	 */
	@SuppressWarnings("null") // ArrayUtils.remove does not return null.
	@Override
	public final void clearChild(final int i) {
		if (i >= 0 && i < children.length) {
			final Node replacement = replacementOnChildRemoval(i);
			if (replacement != null) {
				children[i] = replacement;
				children[i].jjtSetParent(this);
			}
			else {
				children[i].jjtSetParent(null);
				children = ArrayUtils.remove(children, i);
			}
		}
	}

	@Override
	public final boolean isA(final int nodeType) {
		return this.nodeId == nodeType;
	}

	/**
	 * Only to be used by subclasses overriding {@link #replacementOnChildRemoval(int)}
	 * when they need to remove other children as well (to prevent recursion).
	 * @param i Child to remove.
	 */
	@SuppressWarnings("null") // ArrayUtils.remove does not return null.
	protected final void removeChildUnconditionally(final int i) {
		if (i >= 0 && i < children.length) {
			children[i].jjtSetParent(null);
			children = ArrayUtils.remove(children, i);
		}
	}

	@Override
	public final boolean isLeaf() {
		return children.length == 0;
	}

	/** @return A newly created empty node. */
	protected final Node nullNode() {
		return new ASTNullNode(this);
	}

	/** @return A newly created empty node. */
	protected final Node emptyNode() {
		return new ASTEmptyNode(this);
	}

	/** @return A newly created string node for the empty string. */
	@Nonnull
	protected final Node emptyStringNode() {
		return new ASTStringNode(this);
	}

	/**
	 * Nodes may choose to replace the child with some other (empty) node
	 * instead of removing the child to keep when clearing a child.
	 * They may also have to remove or replace other children.
	 * @param i Child to remove/clear.
	 * @return Whether to replace the child with an empty node instead of removing it.
	 * @throws ArrayIndexOutOfBoundsException When removal is not allowed under any circumstances.
	 * @see #clearChild(int)
	 */
	@Nullable
	protected abstract Node replacementOnChildRemoval(int i) throws ArrayIndexOutOfBoundsException;
}