package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IFunctionNode;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;

public class ASTFunctionClauseNode extends AScopedSourceResolvableNode implements IFunctionNode {
	private static final long serialVersionUID = 1L;
	
	private boolean hasVarArgs;
	private boolean hasType;
	private int callId = -1;
	private int closureTableSize = -1;

	public ASTFunctionClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method, final boolean hasVarArgs, final boolean hasType) throws ParseException {
		assertChildrenAtLeast(2);
		if (hasVarArgs && jjtGetNumChildren() == (hasType ? 3 : 2))
			throw new ParseException(CmnCnst.Error.VAR_ARGS_WITHOUT_ARGUMENTS);
		final ASTVariableNode var = getNthChildAs(hasType ? 1 : 0, ASTVariableNode.class);
		final String scope = var.getScope();
		final String variableName = var.getVariableName();
		super.init(method, scope, variableName);
		this.hasVarArgs = hasVarArgs;
		this.hasType = hasType;
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i == (hasType ? 1 : 0))
			throw new ArrayIndexOutOfBoundsException();
		if (i == 0 && hasType)
			return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectClass.NULL);
		if (i != jjtGetNumChildren() - 1)
			return null;
		return nullNode();
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
		return jjtGetNumChildren() - (hasType ? 3 : 2);
	}

	@Override
	public Node getArgumentNode(final int i) {
		return jjtGetChild(i + (hasType ? 2 : 1));
	}

	@Override
	public final ASTFunctionArgumentNode getArgResolvable(final int i) {
		return (ASTFunctionArgumentNode) jjtGetChild(i + (hasType ? 2 : 1));
	}

	@Nonnull
	public ASTVariableNode getVariableNode() {
		return (ASTVariableNode) jjtGetChild(hasType ? 1 : 0);
	}

	public void supplyScope(@Nullable final String scope) {
		if (scope != null)
			setScope(scope);
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@Override
	public boolean hasType() {
		return hasType;
	}

	@Override
	@Nonnull
	public Node getTypeNode() {
		return jjtGetChild(0);
	}

	@Override
	public void additionalToStringFields(final StringBuilder sb) {
		super.additionalToStringFields(sb);
		sb.append(hasVarArgs).append(',');
	}

	@Override
	public int getFunctionId() {
		return callId;
	}

	@Override
	public void resolveFunctionId(final int callId) {
		if (callId < 0)
			throw new FormExpressionException("Call ID must be non-negative, but it is " + callId);
		this.callId = callId;
	}

	@Override
	public boolean isFunctionIdResolved() {
		return callId >= 0;
	}
	
	@Override
	public void resolveClosureTableSize(final int closureTableSize) {
		this.closureTableSize = closureTableSize;
	}

	@Override
	public int getClosureTableSize() {
		return closureTableSize ;
	}

	@Override
	public boolean isClosureTableSizeResolved() {
		return closureTableSize  >= 0;
	}
}