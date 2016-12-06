package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@Immutable
public class SimpleVariableType implements IVariableType {
	@Nonnull
	private final ELangObjectType type;
	
	public SimpleVariableType(@Nonnull final ELangObjectType type) throws IllegalVariableTypeException {
		if (!type.allowsGenericsCount(0))
			throw new IllegalVariableTypeException(NullUtil.stringFormat(CmnCnst.Error.NOT_A_SIMPLE_TYPE, type));
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
		return true;
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
	@Override
	public IVariableType union(final IVariableType otherType) throws IllegalVariableTypeException {
		if (type == otherType.getBasicLangType() || otherType.getBasicLangType() == ELangObjectType.NULL)
			return this;
		if (type == ELangObjectType.NULL)
			return otherType;
		if (otherType.getGenericCount() != 0)
			throw new IllegalVariableTypeException(NullUtil.messageFormat(CmnCnst.Error.NON_GENERIC_INCOMPATIBLE_WITH_GENERIC_TYPE, this.toString(), type.toString()));
		throw new IllegalVariableTypeException(CmnCnst.Error.INCOMPATIBLE_SIMPLE_TYPES);
	}
}