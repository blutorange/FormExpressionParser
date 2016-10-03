package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTAssignmentExpressionNode extends SimpleNode {
	public ASTAssignmentExpressionNode(final int id) {
		super(id);
	}

	public void init(final EMethod method, final FormExpressionParser parser) throws ParseException {
		assertChildrenAtLeast(2);
		final Node[] children = getChildArray();
		for (int i = children.length-2; i != -1; --i) {
			switch (children[i].jjtGetNodeId()) {
			case FormExpressionParserTreeConstants.JJTVARIABLENODE:
				break;
			case FormExpressionParserTreeConstants.JJTPROPERTYEXPRESSIONNODE:
				final ASTPropertyExpressionNode n = (ASTPropertyExpressionNode)children[i];
				if (n.getLastChild().getSiblingMethod() == EMethod.PARENTHESIS) {
					// Cannot do assignment a.foobar() = 42;
					@SuppressWarnings("boxing")
					final String msg = String.format("Illegal LVALUE (function call) %s for assignment at line %s, column %s.", children[i], parser.token.beginLine, parser.token.beginColumn);
					throw new ParseException(msg);
				}
				break;
			default:
				@SuppressWarnings("boxing")
				final String msg = String.format("Illegal LVALUE %s for assignment at line %s, column %s.", children[i], parser.token.beginLine, parser.token.beginColumn);
				throw new ParseException(msg);
			}
		}
		siblingMethod = method;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
