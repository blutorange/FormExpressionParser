package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.util.CmnCnst;

public class ASTVariableTypeNode extends ANode {
	
	private static final long serialVersionUID = 1L;
	
	@Nonnull
	private ILangObjectClass variableType = ELangObjectType.OBJECT;

	public ASTVariableTypeNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}
	
	public ASTVariableTypeNode(@Nonnull final Node prototype, @Nonnull final ILangObjectClass variableType) {
		super(prototype, FormExpressionParserTreeConstants.JJTVARIABLETYPENODE);
		this.variableType = variableType;
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
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return new ASTVariableTypeNode(jjtGetChild(i), ELangObjectType.OBJECT);
	}
	
	@Override
	public void additionalToStringFields(final StringBuilder sb) {
		sb.append(variableType).append(',');
	}
	
	public void init(final EMethod method, @Nonnull final ILangObjectClass variableType) throws ParseException {
		assertNonNull(variableType, CmnCnst.Error.NULL_VARIABLE_TYPE);
		super.init(method);
		this.variableType  = variableType;
	}

	@Nonnull
	public ILangObjectClass getVariableType() {
		return variableType;
	}

	public boolean hasGenerics() {
		return jjtGetNumChildren()>0;
	}
	
	public int getGenericsCount() {
		return jjtGetNumChildren();
	}
	
	@Nonnull
	public Node getGenericsNode(final int i) {
		return jjtGetChild(i);
	}
}