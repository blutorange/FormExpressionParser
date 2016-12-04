package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.iface.parse.IVariableTyped;
import de.xima.fc.form.expression.impl.variable.GenericSourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTFunctionNode extends ANode implements IArgumentResolvable, IVariableTyped {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final GenericSourceResolvable thisResolvable = new GenericSourceResolvable(CmnCnst.Name.VARIABLE_THIS);

	private boolean hasVarArgs;
	private boolean hasType;

	public ASTFunctionNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}
	
	public void init(final EMethod method, final boolean hasVarArgs, final boolean hasType) throws ParseException {
		assertChildrenAtLeast(hasType ? 2 : 1);
		if (hasVarArgs && jjtGetNumChildren() == (hasType ? 2 : 1))
			throw new ParseException(CmnCnst.Error.VAR_ARGS_WITHOUT_ARGUMENTS);
		super.init(method);
		this.hasVarArgs = hasVarArgs;
		this.hasType = hasType;
	}
	
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (hasType && i == 0)
			return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectType.NULL);
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
	
	public Node getArgumentNode(final int i) {
		return jjtGetChild(i + (hasType ? 1 : 0));
	}

	@Override
	public final ASTFunctionArgumentNode getArgResolvable(final int i) {
		return (ASTFunctionArgumentNode)jjtGetChild(i + (hasType ? 1 : 0));
	}

	@Override
	@Nonnull
	public Node getBodyNode() {
		return jjtGetChild(jjtGetNumChildren()-1);
	}
	
	@Override
	public ISourceResolvable getThisResolvable() {
		return thisResolvable;
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
		sb.append(hasVarArgs).append(',');
	}
}