package de.xima.fc.form.expression.enums;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.util.CmnCnst;

public enum EVariableType {
	UNKNOWN(CmnCnst.Syntax.VAR),
	BOOLEAN(CmnCnst.Syntax.BOOLEAN),
	NUMBER(CmnCnst.Syntax.NUMBER),
	STRING(CmnCnst.Syntax.STRING),
	ARRAY(CmnCnst.Syntax.ARRAY),
	HASH(CmnCnst.Syntax.HASH),
	REGEX(CmnCnst.Syntax.REGEX),
	ERROR(CmnCnst.Syntax.ERROR),
	METHOD(CmnCnst.Syntax.METHOD);

	@Nonnull
	private final String syntacticalName;

	private EVariableType(@Nonnull final String syntacticalName) {
		this.syntacticalName = syntacticalName;
	}
	
	@Nonnull
	public String getSyntacticalName() {
		return syntacticalName;
	}
}
