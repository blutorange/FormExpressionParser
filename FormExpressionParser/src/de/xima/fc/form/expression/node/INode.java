package de.xima.fc.form.expression.node;

import org.jetbrains.annotations.NotNull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

/* All AST nodes must implement this interface.  It provides basic
   machinery for constructing the parent and child relationships
   between nodes. */

public interface INode {

	/** This method is called after the node has been made the current
    node.  It indicates that child nodes can now be added to it. */
	public void jjtOpen();

	/** This method is called after all the child nodes have been
    added. */
	public void jjtClose();

	/** This pair of methods are used to inform the node of its
    parent. */
	public void jjtSetParent(INode n);
	public INode jjtGetParent();

	/** This method tells the node to add its argument to the node's
    list of children.  */
	public void jjtAddChild(INode n, int i);

	/** This method returns a child node.  The children are numbered
     from zero, left to right. */
	public INode jjtGetChild(int i);

	/** Return the number of children the node has. */
	public int jjtGetNumChildren();

	/** A dump of this node (and possibly its children). */
	public void dump(final String prefix);

	/**
	 * @return The evaluated value of this node. May return {@link NullLangObject}.
	 * @throws ParseException
	 */
	@NotNull
	public ALangObject evaluate(@NotNull IEvaluationContext ec) throws EvaluationException;

	public <T extends MySimpleNode> T[] getChildArray(final Class<T> clazz) throws ParseException;

	/**
	 *
	 * @param clazz
	 * @return
	 * @throws ParseException When this node does not have exactly one child, or it is not compatible with the specified class.
	 */
	public <T extends MySimpleNode> T getSingleChild(final Class<T> clazz) throws ParseException;

	public <T extends MySimpleNode> T getNthChild(int index, final Class<T> clazz) throws ParseException;

	public void toGraphviz(final StringBuilder builder, final String title);

	public String toGraphviz(final String title);
}
