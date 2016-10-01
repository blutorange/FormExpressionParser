package de.xima.fc.form.expression.visitor;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.grammar.Node;

public abstract class AEvaluationVisitor<R,T> implements IFormExpressionParserVisitor<R, T> {
	protected Node currentlyProcessedNode;
	@Nullable
	public Node getCurrentlyProcessedNode() {
		return currentlyProcessedNode;
	}
}
