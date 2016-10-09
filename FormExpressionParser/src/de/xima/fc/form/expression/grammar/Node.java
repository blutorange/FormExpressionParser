package de.xima.fc.form.expression.grammar;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.context.ITraceElement;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

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
	 */
	public void jjtAddChild(Node n, int i);

	/**
	 * This method returns a child node. The children are numbered from zero,
	 * left to right.
	 */
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

	/**
	 * @param atLeast The minimum number of children this node can have.
	 * @param atMost The maximum number of children this node can have.
	 * @throws ParseException When the number of children is not inside the specified range.
	 */
	public void assertChildrenBetween(final int atLeast, final int atMost) throws ParseException;

	/**
	 * @param count The number of children this node must have.
	 * @throws ParseException When the number of children is not count.
	 */
	public void assertChildrenExactly(final int count) throws ParseException;

	public void assertChildrenExactlyOneOf(int count1, int count2) throws ParseException;

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
	public <R, T, E extends Throwable> R jjtAccept(@Nonnull final IFormExpressionParserVisitor<R, T, E> visitor, @Nonnull final T data)
			throws E;

	public int getId();

	/**
	 * @return The method by which this node is linked to the previous sibling, or null.
	 */
	public EMethod getSiblingMethod();

	public Node[] getChildArray();

	@Nullable
	public Node getLastChild();
	@Nullable
	public Node getFirstChild();

	public void setStartPosition(Token token);
	public void setEndPosition(Token token);

	/**
	 * @return The id for this TYPE of node.
	 * @see FormExpressionParserTreeConstants
	 */
	public int jjtGetNodeId();

	public String getEmbedment();

}