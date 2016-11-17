package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.ParseException;

public class SemanticsException extends ParseException {
	private static final long serialVersionUID = 1L;
	public SemanticsException(@Nonnull final String message, final int beginLine, final int beginColumn) {
		super(String.format("Error occurred at line %d, column %d: %s", beginLine, beginColumn, message));
		this.beginColumn = beginColumn;
		this.beginLine = beginLine;
	}
	public final int beginLine, beginColumn;
}
