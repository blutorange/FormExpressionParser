package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class SimpleVariableType implements IVariableType {
	@Nonnull
	private final ELangObjectType type;
	public SimpleVariableType(@Nonnull final ELangObjectType type) throws IllegalStateException {
		if (type.isCompound())
			throw new IllegalStateException(NullUtil.format(CmnCnst.Error.NOT_A_SIMPLE_TYPE, type));
		this.type = type;
	}
	@Override
	public ELangObjectType getBasicLangType() {
		return type;
	}
	@Override
	public boolean equalsType(final IVariableType other) {
		if (type != other.getBasicLangType())
			return false;
		if (other.getGenericCount() != 0)
			return false;
		return false;
	}
	@Override
	public int getGenericCount() {
		return 0;
	}
	@Override
	public IVariableType getGeneric(final int i) throws ArrayIndexOutOfBoundsException {
		throw new ArrayIndexOutOfBoundsException(i);
	}

	@Override
	public String toString() {
		return type.toString();
	}
}