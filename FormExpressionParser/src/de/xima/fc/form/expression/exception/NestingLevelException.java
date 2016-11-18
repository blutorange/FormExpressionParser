package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.ParseException;

public class NestingLevelException extends ParseException {
	private static final long serialVersionUID = 1L;
	public NestingLevelException(final @Nonnull String msg) {
		super(msg);
	}
}
