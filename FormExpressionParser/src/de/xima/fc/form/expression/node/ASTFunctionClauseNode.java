package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.variable.GenericSourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTFunctionClauseNode extends AScopedSourceResolvableNode implements IArgumentResolvable {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final GenericSourceResolvable thisResolvable = new GenericSourceResolvable(CmnCnst.Name.VARIABLE_THIS);
	@Nonnull
	private final GenericSourceResolvable argumentsResolvable = new GenericSourceResolvable(
			CmnCnst.Name.VARIABLE_ARGUMENTS);
	@Nonnull
	private GenericSourceResolvable[] argResolvable = CmnCnst.NonnullConstant.EMPTY_GENERIC_SOURCE_RESOLVABLE_ARRAY;

	public ASTFunctionClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		final ASTVariableNode var = getNthChildAs(0, ASTVariableNode.class);
		final String scope = var.getScope();
		final String variableName = var.getVariableName();
		super.init(method, scope, variableName);
		argResolvable = ASTFunctionNode.getArgs(this, 1);
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i == 0)
			throw new ArrayIndexOutOfBoundsException();
		if (i != jjtGetNumChildren() -1)
			return null;
		return nullNode();
	}
	
	@Nonnull
	public Node getFunctionNameNode() {
		return jjtGetChild(0);
	}

	@Override
	@Nonnull
	public Node getBodyNode() {
		return jjtGetChild(jjtGetNumChildren() - 1);
	}

	@Nonnull
	public String getCanonicalName() {
		return getScope() != null ? getScope() + Syntax.SCOPE_SEPARATOR + getVariableName() : getVariableName();
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor,
			final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data)
			throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Override
	public final int getArgumentCount() {
		return argResolvable.length;
	}
	
	@Override
	public Node getArgumentNode(final int i) {
		return jjtGetChild(i+1);
	}

	@SuppressWarnings("null")
	@Override
	public final ISourceResolvable getArgResolvable(final int i) {
		return argResolvable[i];
	}

	@Override
	public ISourceResolvable getThisResolvable() {
		return thisResolvable;
	}

	@Override
	public ISourceResolvable getArgumentsResolvable() {
		return argumentsResolvable;
	}

	@Nonnull
	public ASTVariableNode getVariableNode() {
		return (ASTVariableNode) jjtGetChild(0);
	}

	public void supplyScope(@Nullable final String scope) {
		if (scope != null)
			setScope(scope);
	}
}