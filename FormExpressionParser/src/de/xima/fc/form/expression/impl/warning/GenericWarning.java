package de.xima.fc.form.expression.impl.warning;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;

@ParametersAreNonnullByDefault
public class GenericWarning implements IEvaluationWarning {
	private final String message;
	private final int startLine, startColumn;
	private final int endLine, endColumn;

	public GenericWarning(final String message, final Node node) {
		this.message = message;
		startColumn = node.getStartColumn();
		startLine = node.getStartLine();
		endColumn = node.getEndColumn();
		endLine = node.getEndLine();
	}

	public GenericWarning(final String message, final IEvaluationContext ec) {
		this.message = message;
		final Node node = ec.getTracer().getCurrentlyProcessed();
		if (node != null) {
			startColumn = node.getStartColumn();
			startLine = node.getStartLine();
			endColumn = node.getEndColumn();
			endLine = node.getEndLine();
		}
		else {
			startColumn = startLine = endColumn = endLine = 0;
		}
	}

	@Override
	public final String getMessage() {
		return message;
	}

	@Override
	public final int getStartLine() {
		return startLine;
	}

	@Override
	public final int getStartColumn() {
		return startColumn;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}
}