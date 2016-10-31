package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTVariableTypeDeclarationNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	private Type type;

	public ASTVariableTypeDeclarationNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(type).append(',');
	}


	public void init(final EMethod method, final Type type) throws ParseException {
		super.init(method);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
