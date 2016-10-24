package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionNode extends SimpleNode {

	private boolean hasReturnTypeDeclaration;

	public ASTFunctionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method, final boolean hasReturnTypeDeclaration) throws ParseException {
		assertChildrenAtLeast(hasReturnTypeDeclaration ? 2 : 1);
		super.init(method);
		this.hasReturnTypeDeclaration = hasReturnTypeDeclaration;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public boolean isHasReturnTypeDeclaration() {
		return hasReturnTypeDeclaration;
	}
}
