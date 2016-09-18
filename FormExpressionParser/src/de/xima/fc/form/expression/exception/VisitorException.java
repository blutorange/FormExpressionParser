package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class VisitorException extends EvaluationException {

	public VisitorException(final IFormExpressionParserVisitor<?,?> visitor, final Object object, final Object data, final Throwable throwable) {
		super(String.format("Visitor %s could not visit object %s with data %s.", visitor, object, data), throwable);
		this.visitor = visitor;
		this.object = object;
		this.data = data;
	}

	public final IFormExpressionParserVisitor<?, ?> visitor;
	public final Object object;
	public final Object data;
}
