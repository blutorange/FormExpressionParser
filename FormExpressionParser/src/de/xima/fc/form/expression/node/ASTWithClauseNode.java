package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTWithClauseNode extends ANode {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private String[] scopes = CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
	
	public ASTWithClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		super.init(method);
		setScopes();
	}
	
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i==0 && jjtGetNumChildren() < 2)
			throw new ArrayIndexOutOfBoundsException();
		return i == jjtGetNumChildren() - 1 ? nullNode() : null;
	}

	public int getScopeCount() {
		return scopes.length;
	}
	
	@Nonnull
	public Node getScopeNode(final int i) throws ArrayIndexOutOfBoundsException {
		if (i >= getScopeCount())
			throw new ArrayIndexOutOfBoundsException(
					NullUtil.format(CmnCnst.Error.WITH_CLAUSE_NODE_SCOPE_OUT_OF_BOUNDS, i, getScopeCount()));
		return jjtGetChild(i);
	}

	@Nonnull
	public Node getBodyNode() {
		return jjtGetChild(0);
	}
	
	private void setScopes() throws ParseException {
		scopes = new String[jjtGetNumChildren()-1];
		for (int i = 0; i < scopes.length; ++i)
			scopes[i] = getNthChildAs(i, ASTIdentifierNameNode.class).getName();
		for (int i = scopes.length; i --> 0;)
			clearChild(i);
	}

	public String getScope(final int i) {
		return scopes[i];
	}
}