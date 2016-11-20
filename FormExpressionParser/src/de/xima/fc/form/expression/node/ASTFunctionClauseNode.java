package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTFunctionClauseNode extends ANode {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private String variableName = CmnCnst.NonnullConstant.EMPTY_STRING;
	@Nullable
	private String scope;

	public ASTFunctionClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		super.init(method);
		final ASTVariableNode var = getNthChildAs(0, ASTVariableNode.class);
		scope = var.getScope();
		variableName = var.getVariableName();
	}

	@Nonnull
	public Node getFunctionNameNode() {
		return jjtGetChild(0);
	}
	
	@Nonnull
	public Node getBodyNode() {
		return jjtGetChild(jjtGetNumChildren()-1);
	}
	
	public int getArgumentCount() {
		return jjtGetNumChildren()-2;
	}
	
	@Nonnull
	public String getCanonicalName() {
		return scope != null ? scope + Syntax.SCOPE_SEPARATOR + variableName : variableName;
	}

	@Nullable
	public String getScope() {
		return scope;
	}
	
	@Nonnull
	public String getVariableName() {
		return variableName;
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

	public Node getArgumentNode(final int i) throws ArrayIndexOutOfBoundsException {
		if (i>=getArgumentCount())
			throw new ArrayIndexOutOfBoundsException(NullUtil.format(CmnCnst.Error.FUNCTION_NODE_ARGUMENT_OUT_OF_BOUNDS, i, getArgumentCount()));
		return jjtGetChild(i+1);
	}
}
