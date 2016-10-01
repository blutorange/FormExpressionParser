package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTAssignmentExpressionNode extends SimpleNode {	
	public ASTAssignmentExpressionNode(final int id) {
		super(id);
	}

	public void init(final EMethod method, FormExpressionParser parser) throws ParseException {
		assertChildrenAtLeast(2);
		final Node[] children = getChildArray();
		for (int i = children.length-2; i != -1; --i) {
			if (children[i] instanceof ASTVariableNode) {
				((ASTVariableNode)children[i]).siblingMethod = EMethod.ASSIGNMENT_DIRECT;
			}
			else if (children[i] instanceof ASTPropertyExpressionNode) {
				((ASTPropertyExpressionNode)children[i]).siblingMethod = EMethod.ASSIGNMENT_PROPERTY;				
			}
			else {
				@SuppressWarnings("boxing")
				final String msg = String.format("Illegal LVALUE %s for assignment at line %s, column %s.", children[i], parser.token.beginLine, parser.token.beginColumn);
				throw new ParseException(msg);
			}
		}
		siblingMethod = method;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
