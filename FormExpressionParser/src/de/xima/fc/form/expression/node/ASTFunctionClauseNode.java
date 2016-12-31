package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IFunctionNode;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ASTFunctionClauseNode extends ANode implements IScopedSourceResolvable, IFunctionNode, IHeaderNode {
	private static final long serialVersionUID = 1L;

	private boolean hasVarArgs;
	private boolean hasType;
	private Integer callId = Integer.valueOf(-1);
	private int closureTableSize = -1;

	public ASTFunctionClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method, final boolean hasVarArgs, final boolean hasType) throws ParseException {
		assertChildrenAtLeast(2);
		if (hasVarArgs && jjtGetNumChildren() == (hasType ? 3 : 2))
			throw new ParseException(CmnCnst.Error.VAR_ARGS_WITHOUT_ARGUMENTS);
		assertChildOfType(hasType ? 1 : 0, ASTVariableNode.class);
		super.init(method);
		this.hasVarArgs = hasVarArgs;
		this.hasType = hasType;
	}

	@Nullable
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

	public ASTVariableNode getVariableNode() {
		return (ASTVariableNode) jjtGetChild(hasType ? 1 : 0);
	}

	public void supplyScope(@Nullable final String scope) {
		if (scope != null)
			getVariableNode().setScope(scope);
	}

	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}

	@Override
	public boolean hasType() {
		return true;
	}

	@Override
	@Nonnull
	public Node getTypeNode() {
		return this;
	}

	@Override
	public boolean hasReturnType() {
		return hasType;
	}

	@Override
	@Nonnull
	public Node getReturnTypeNode() {
		return jjtGetChild(0);
	}

	@Override
	public void additionalToStringFields(final StringBuilder sb) {
		super.additionalToStringFields(sb);
		sb.append(hasVarArgs).append(',');
	}

	@Override
	public Integer getFunctionId() {
		return callId;
	}

	@Override
	public void resolveFunctionId(final Integer callId) {
		if (callId.intValue() < 0)
			throw new FormExpressionException(
					NullUtil.messageFormat(CmnCnst.Error.NEGATIVE_CALL_ID, callId));
		this.callId = callId;
	}

	@Override
	public boolean isFunctionIdResolved() {
		return callId.intValue() >= 0;
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

	@Override
	public Node getHeaderValueNode() {
		return this;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public Node getHeaderDeclarationNode() {
		return this;
	}

	@Override
	public void resolveSource(final int source, final EVariableSource type) throws IllegalVariableSourceResolutionException {
		getVariableNode().resolveSource(source, type);
	}

	@Override
	public void resolveClosureSource(final int source) throws IllegalVariableSourceResolutionException {
		getVariableNode().resolveClosureSource(source);
	}

	@Override
	public int getBasicSource() {
		return getVariableNode().getBasicSource();
	}

	@Override
	public int getClosureSource() {
		return getVariableNode().getClosureSource();
	}

	@Override
	public EVariableSource getSourceType() {
		return getVariableNode().getSourceType();
	}

	@Override
	public boolean isBasicSourceResolved() {
		return getVariableNode().isBasicSourceResolved();
	}

	@Override
	public boolean isClosureSourceResolved() {
		return getVariableNode().isClosureSourceResolved();
	}

	@Override
	public String getVariableName() {
		return getVariableNode().getVariableName();
	}

	@Override
	public void convertEnvironmentalToClosure() throws IllegalVariableSourceResolutionException {
		getVariableNode().convertEnvironmentalToClosure();
	}

	@Override
	public void resolveSource(final int source, final EVariableSource sourceType, final String scope)
			throws IllegalVariableSourceResolutionException {
		getVariableNode().resolveSource(source, sourceType, scope);
	}

	@Nullable
	@Override
	public String getScope() {
		return getVariableNode().getScope();
	}

	@Override
	public boolean hasScope() {
		return getVariableNode().hasScope();
	}
}