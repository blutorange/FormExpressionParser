package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

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

	@Override
	public boolean isAssignableFrom(final IVariableType otherType) {
		// this    null object string     ...
		// null     o      x     x     x  ...
		// object   o      o     o     o  ...
		// string   o      x     o     x   x
		//   .      o      x     x     o   x
		//   .      o      x     x     x   o
		//   .      o      x     x     x   x
		if (type == otherType.getBasicLangType() || otherType.getBasicLangType() == ELangObjectType.NULL
				|| type == ELangObjectType.OBJECT)
			return true;
		return false;
	}

	@Override
	public boolean isIterable() {
		return getBasicLangType().isIterable();
	}

	@Override
	public IVariableType getIterableItemType() {
		return type.getIterableItemType(CmnCnst.NonnullConstant.EMPTY_VARIABLE_TYPE_ARRAY);
	}
}