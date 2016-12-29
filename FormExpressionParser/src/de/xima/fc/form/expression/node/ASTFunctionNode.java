package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IFunctionNode;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.VoidClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ASTFunctionNode extends ANode implements IFunctionNode {
	private static final long serialVersionUID = 1L;

	private boolean hasVarArgs;
	private boolean hasType;
	private Integer callId = Integer.valueOf(-1);
	private int closureTableSize = -1;

	public ASTFunctionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	/**
	 * Creates a new function node corresponding to an
	 * anonymous void function <code>()=>void{}</code>.
	 * @param prototype Prototype for basic properties.
	 */
	public ASTFunctionNode(final Node prototype) {
		super(prototype, FormExpressionParserTreeConstants.JJTFUNCTIONNODE);
		jjtAddChild(new ASTVariableTypeNode(prototype, VoidClass.INSTANCE), 0);
		jjtAddChild(new ASTEmptyNode(prototype), 1);
		hasVarArgs = false;
		hasType = true;
	}

	public void init(final EMethod method, final boolean hasVarArgs, final boolean hasType) throws ParseException {
		assertChildrenAtLeast(hasType ? 2 : 1);
		if (hasVarArgs && jjtGetNumChildren() == (hasType ? 2 : 1))
			throw new ParseException(CmnCnst.Error.VAR_ARGS_WITHOUT_ARGUMENTS);
		super.init(method);
		this.hasVarArgs = hasVarArgs;
		this.hasType = hasType;
	}

	@Nullable
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (hasType && i == 0)
			return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectClass.NULL);
		return i == jjtGetNumChildren() - 1 ? nullNode() : null;
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
	public final int getArgumentCount() {
		return jjtGetNumChildren() - (hasType ? 2 : 1);
	}

	@Override
	public Node getArgumentNode(final int i) {
		return jjtGetChild(i);
	}

	@Override
	public final ASTFunctionArgumentNode getArgResolvable(final int i) {
		return (ASTFunctionArgumentNode)jjtGetChild(i);
	}

	@Override
	@Nonnull
	public Node getBodyNode() {
		return jjtGetChild(jjtGetNumChildren()-1);
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
		return jjtGetChild(jjtGetNumChildren()-2);
	}

	@Override
	public void additionalToStringFields(final StringBuilder sb) {
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
		return closureTableSize >= 0;
	}
}