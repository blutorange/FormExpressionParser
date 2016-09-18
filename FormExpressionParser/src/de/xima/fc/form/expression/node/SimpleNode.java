/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import java.io.Serializable;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;

public abstract class SimpleNode implements Node, Serializable {

	/** The id of this node. Can be anything as long as it is unique. Does not need to be the same
	 * for multiple runs of the program. */
	protected final int id;
	protected transient Node parent;
	protected transient Node[] children;
	protected transient Object value;
	protected transient FormExpressionParser parser;

	public SimpleNode(final int i) {
		id = i;
	}

	public SimpleNode(final FormExpressionParser p, final int i) {
		this(i);
		parser = p;
	}

	@Override
	public void jjtOpen() {
	}

	@Override
	public void jjtClose() {
	}

	@Override
	public void jjtSetParent(final Node n) { parent = n; }
	@Override
	public Node jjtGetParent() { return parent; }

	@Override
	public void jjtAddChild(final Node n, final int i) {
		if (children == null)
			children = new Node[i + 1];
		else if (i >= children.length) {
			final Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	@Override
	public Node jjtGetChild(final int i) {
		return children[i];
	}

	@Override
	public int jjtGetNumChildren() {
		return children == null ? 0 : children.length;
	}

	public void jjtSetValue(final Object value) { this.value = value; }
	public Object jjtGetValue() { return value; }

	/* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

	@Override
	public String toString() { return FormExpressionParserTreeConstants.jjtNodeName[id]; }
	public String toString(final String prefix) { return prefix + toString(); }

	@Override
	public void dump(final String prefix) {
		System.out.println(toString(prefix));
		if (children != null)
			for (int i = 0; i < children.length; ++i) {
				final SimpleNode n = (SimpleNode)children[i];
				if (n != null)
					n.dump(prefix + "  ");
			}
	}
}