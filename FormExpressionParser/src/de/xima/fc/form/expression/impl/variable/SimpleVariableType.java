package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;

@Immutable
public enum SimpleVariableType implements IVariableType {
	OBJECT(ELangObjectType.OBJECT),
	NULL(ELangObjectType.NULL),
	BOOLEAN(ELangObjectType.BOOLEAN),
	NUMBER(ELangObjectType.NUMBER),
	STRING(ELangObjectType.STRING),
	REGEX(ELangObjectType.REGEX),
	EXCEPTION(ELangObjectType.EXCEPTION),
	;
	
	@Nonnull
	private final ELangObjectType type;
	
	private SimpleVariableType(@Nonnull final ELangObjectType type) {
		if (!type.allowsGenericsCount(0))
			throw new RuntimeException();
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
	public IVariableType union(final IVariableType otherType) {
		if (type == otherType.getBasicLangType() || otherType.getBasicLangType() == ELangObjectType.NULL
				|| type == ELangObjectType.OBJECT)
			return this;
		if (type == ELangObjectType.NULL || otherType.getBasicLangType() == ELangObjectType.OBJECT)
			return otherType;
		return SimpleVariableType.OBJECT;
	}
}