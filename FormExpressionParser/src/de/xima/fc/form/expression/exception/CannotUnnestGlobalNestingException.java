package de.xima.fc.form.expression.exception;

import de.xima.fc.form.expression.util.CmnCnst;

public class CannotUnnestGlobalNestingException extends NestingLevelException {
	private static final long serialVersionUID = 1L;

	public CannotUnnestGlobalNestingException() {
		super(CmnCnst.Error.CANNOT_UNNEST_GLOBAL_BINDING);
	}

}
