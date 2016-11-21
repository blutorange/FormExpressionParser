package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.impl.variable.GenericSourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTFunctionNode extends ANode implements IArgumentResolvable {
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final GenericSourceResolvable thisResolvable = new GenericSourceResolvable(CmnCnst.Name.VARIABLE_THIS);
	@Nonnull
	private final GenericSourceResolvable argumentsResolvable = new GenericSourceResolvable(CmnCnst.Name.VARIABLE_ARGUMENTS);	
	@Nonnull
	private GenericSourceResolvable[] argResolvable = CmnCnst.NonnullConstant.EMPTY_GENERIC_SOURCE_RESOLVABLE_ARRAY;

	public ASTFunctionNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}
	
	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		super.init(method);
		argResolvable = ASTFunctionNode.getArgs(this);
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
		return argResolvable.length;
	}

	@SuppressWarnings("null")
	@Override
	public final ISourceResolvable getArgResolvable(final int i) {
		return argResolvable[i];
	}

	@Nonnull
	static GenericSourceResolvable[] getArgs(final Node node) throws ParseException {
		final int count = node.jjtGetNumChildren()-2;
		final GenericSourceResolvable[] argResolvable = new GenericSourceResolvable[count];
		for (int i = 0; i < count; ++i) {
			final ASTIdentifierNameNode argNode = node.getNthChildAs(i + 1, ASTIdentifierNameNode.class);
			argResolvable[i] = new GenericSourceResolvable(argNode.getName());
		}
		for (int i = count; i-- > 0;)
			node.removeChild(i);
		return argResolvable;
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
	public ISourceResolvable getArgumentsResolvable() {
		return argumentsResolvable;
	}
}