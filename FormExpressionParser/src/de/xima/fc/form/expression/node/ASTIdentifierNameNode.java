package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTIdentifierNameNode extends SimpleNode {

	private String name;
	
	public ASTIdentifierNameNode(int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T> R jjtAccept(IFormExpressionParserVisitor<R, T> visitor, T data) throws EvaluationException {
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
