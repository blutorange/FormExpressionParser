package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTIdentifierNameNode extends SimpleNode {

	private String name;
	
	public ASTIdentifierNameNode(FormExpressionParser parser, int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(EMethod method, String name) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	protected void additionalToStringFields(StringBuilder sb) {
		sb.append(name).append(",");
	}
}
