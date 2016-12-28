package de.xima.fc.form.expression.impl.warning;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;

@NonNullByDefault
public class GenericWarning implements IEvaluationWarning {
	private final String message;
	private final int startLine, startColumn;
	private final int endLine, endColumn;

	public GenericWarning(final String message, final Node node) {
		this.message = message;
		startColumn = node.getBeginColumn();
		startLine = node.getBeginLine();
		endColumn = node.getEndColumn();
		endLine = node.getEndLine();
	}

	public GenericWarning(final String message, final IEvaluationContext ec) {
		this.message = message;
		final Node node = ec.getTracer().getCurrentlyProcessed();
		if (node != null) {
			startColumn = node.getBeginColumn();
			startLine = node.getBeginLine();
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
	public final int getBeginLine() {
		return startLine;
	}

	@Override
	public final int getBeginColumn() {
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