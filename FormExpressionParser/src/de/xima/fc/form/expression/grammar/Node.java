package de.xima.fc.form.expression.grammar;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.ITraceElement;

/* All AST nodes must implement this interface.  It provides basic
   machinery for constructing the parent and child relationships
   between nodes. */

public interface Node extends Serializable, ITraceElement {

	/**
	 * This method is called after the node has been made the current node. It
	 * indicates that child nodes can now be added to it.
	 */
	public void jjtOpen();

	/**
	 * This method is called after all the child nodes have been added.
	 */
	public void jjtClose();

	/**
	 * This pair of methods are used to inform the node of its parent.
	 */
	public void jjtSetParent(Node n);

	public Node jjtGetParent();

	/**
	 * This method tells the node to add its argument to the node's list of
	 * children.
	 * @throws ParseException
	 */
	public void jjtAddChild(@Nonnull Node n, int i);

	/**
	 * This method returns a child node. The children are numbered from zero,
	 * left to right.
	 */
	@Nonnull
	public Node jjtGetChild(int i);

	/** Return the number of children the node has. */
	public int jjtGetNumChildren();

	/**
	 * @param <T> The class to which to cast the child nodes.
	 * @param clazz Class to which all nodes will be casted.
	 * @param start The returned array will contain all children starting at this index.
	 * @return The array of children, cast to the specified type.
	 * @throws ParseException When any child cannot be cast to the specified type.
	 */
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, int start) throws ParseException;

	/**
	 * @param <T> The class to which to cast the child nodes.
	 * @param clazz Class to which all nodes will be casted.
	 * @param start The returned array will contain all children starting at this index.
	 * @param end The returned array will contain all children up to this index (inclusive).
	 * @return The array of children, cast to the specified type.
	 * @throws ParseException When any child cannot be cast to the specified type.
	 */
	public <T extends Node> T[] getChildArrayAs(final Class<T> clazz, int start, int end) throws ParseException;


	/**
	 * @param clazz
	 *            Class to which the node will be casted.
	 * @return The child, cast to the specified type.
	 * @throws ParseException
	 *             When the specified child cannot be cast to the specified
	 *             type.
	 * @throws ArrayIndexOutOfBoundsException
	 *             Use {@link #assertChildrenExactly(int)} or
	 *             {@link #assertChildrenBetween(int, int)} to check before
	 *             calling this method.
	 * @param <T> The class to which to cast the child node.
	 */
	public <T extends Node> T getNthChildAs(int index, final Class<T> clazz) throws ParseException;

	public <T extends Node> T getNthChildAsOrNull(int index, @Nonnull Class<T> clazz);


	/**
	 * @param atLeast The minimum number of children this node can have.
	 * @param atMost The maximum number of children this node can have.
	 * @throws ParseException When the number of children is not inside the specified range.
	 */
	public void assertChildrenBetween(final int atLeast, final int atMost) throws ParseException;

	@Nonnull
	public <T> T assertNonNull(@Nullable T param, @Nonnull String errorMessage) throws ParseException;

	/**
	 * @param count The number of children this node must have.
	 * @throws ParseException When the number of children is not count.
	 */
	public void assertChildrenExactly(final int count) throws ParseException;

	public void assertChildrenExactlyOneOf(int count1, int count2) throws ParseException;

	public void assertChildOfType(int i, Class<? extends Node> clazz) throws ParseException;

	/**
	 * @param count The number of children this node must have at least.
	 * @throws ParseException When the number of children is less than the specified number
	 */
	void assertChildrenAtLeast(int count) throws ParseException;

	/**
	 * @param count The number of children this node can have at most.
	 * @throws ParseException When the number of children is larger than the specified number.
	 */
	void assertChildrenAtMost(int count) throws ParseException;

	/** @throws ParseException When the number of children is not even. */
	void assertChildrenEven() throws ParseException;

	/** @throws ParseException When the number of children is not odd. */
	void assertChildrenOdd() throws ParseException;

	/**
	 * @param visitor
	 *            Visitor to be accepted.
	 * @param data
	 *            Data for the visitor.
	 * @return The return value of the visitor.
	 */
	@Nonnull
	public <R, T, E extends Throwable> R jjtAccept(@Nonnull final IFormExpressionReturnDataVisitor<R, T, E> visitor, @Nonnull final T data)
			throws E;
	@Nonnull
	public <R, E extends Throwable> R jjtAccept(@Nonnull final IFormExpressionReturnVoidVisitor<R,E> visitor)
			throws E;
	public <T, E extends Throwable> void jjtAccept(@Nonnull final IFormExpressionVoidDataVisitor<T, E> visitor, @Nonnull final T data)
			throws E;
	public <E extends Throwable> void jjtAccept(@Nonnull final IFormExpressionVoidVoidVisitor<E> visitor)
			throws E;

	public int getId();

	/**
	 * @return The method by which this node is linked to the previous sibling.
	 */
	@Nonnull
	public EMethod getSiblingMethod();

	@Nonnull
	public Node[] getChildArray();

	@Nullable
	public Node getLastChildOrNull();
	@Nullable
	public Node getFirstChildOrNull();

	@Nonnull
	public Node getLastChild() throws ParseException;
	@Nonnull
	public Node getFirstChild() throws ParseException;

	/**
	 * @return The id for this TYPE of node.
	 * @see FormExpressionParserTreeConstants
	 */
	public int jjtGetNodeId();

	/**
	 * @return The current embedment. <code>null</code> when not a template program.
	 */
	@Nullable
	public String getEmbedment();

	public void jjtSetFirstToken(Token token);
	public void jjtSetLastToken(Token token);

	/**
	 * Detaches this node from the parent. Does nothing if this is the top-level
	 * node.
	 *
	 * @return The detached node.
	 * @throws ParseException
	 *             When the syntax tree is bad and the parent does not contain
	 *             this node as one of its children.
	 */
	@Nonnull
	public Node detach() throws ParseException;

	/**
	 * Removes the child at the given index. Does nothing if the index is out-of-bounds.
	 * @param i Index of the child to remove.
	 */
	public void clearChild(int i);

	/**
	 * @param clazz Class to cast this node to.
	 * @return T Casted node, or null when it cannot be casted.
	 */
	@Nullable
	public <T extends Node> T getAsOrNull(@Nonnull Class<T> clazz);

	@Nonnull
	public String getNodeName();

	/**
	 * @param nodeType Type to check against.
	 * @return Whether this node is of the specified type.
	 * @see FormExpressionParserTreeConstants
	 */
	public boolean isA(int jjtvariablenode);

	/**
	 * @return Whether this node is a leaf node, ie. without any children.
	 */
	public boolean isLeaf();
}