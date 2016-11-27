package de.xima.fc.form.expression.impl.warning;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IEvaluationWarning;

public class GenericWarning implements IEvaluationWarning {
	@Nonnull
	private final String message;
	private final int line, column;
	public GenericWarning(@Nonnull final String message, @Nonnull final Node node) {
		this.message = message;
		column = node.getStartColumn();
		line = node.getStartLine();
	}
	public GenericWarning(@Nonnull final String message, @Nonnull final IEvaluationContext ec) {
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