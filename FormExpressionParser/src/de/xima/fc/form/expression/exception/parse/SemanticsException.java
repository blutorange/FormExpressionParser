package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public class SemanticsException extends ParseException {
	private static final long serialVersionUID = 1L;
	public SemanticsException(@Nonnull final String message, @Nonnull final Node node) {
		this(message, node.getStartLine(), node.getStartColumn());
	}
	public SemanticsException(@Nonnull final String message, final int beginLine, final int beginColumn) {
		super(String.format("Error occurred at line %d, column %d: %s", beginLine, beginColumn, message));
		this.beginColumn = beginColumn;
		this.beginLine = beginLine;
	}
	public final int beginLine, beginColumn;
}
