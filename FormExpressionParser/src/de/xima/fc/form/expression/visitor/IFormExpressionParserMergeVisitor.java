package de.xima.fc.form.expression.visitor;

@SuppressWarnings("all")
public interface IFormExpressionParserMergeVisitor<R,T> extends IFormExpressionParserVisitor<R, T>
{
	public R reduceEmpty();
	public R reduce(R left, R right);
}
