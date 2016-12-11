package de.xima.fc.form.expression.impl.warning;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;

@ParametersAreNonnullByDefault
public class GenericWarning implements IEvaluationWarning {
	private final String message;
	private final int line, column;

	public GenericWarning(final String message, final Node node) {
		this.message = message;
		column = node.getStartColumn();
		line = node.getStartLine();
	}

	public GenericWarning(final String message, final IEvaluationContext ec) {
		this.message = message;
		final Node node = ec.getTracer().getCurrentlyProcessed();
		if (node != null) {
			column = node.getStartColumn();
			line = node.getStartLine();
		}
		else {
			column = line = 0;
		}
	}

	@Override
	public final String getMessage() {
		return message;
	}

	@Override
	public final int getLine() {
		return line;
	}

	@Override
	public final int getColumn() {
		return column;
	}
}