package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
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
	private final ILangObjectClass clazz;

	private SimpleVariableType(@Nonnull final ILangObjectClass type) {
		if (!type.allowsGenericsCount(0))
			throw new FormExpressionException();
		this.clazz = type;
	}

	@Override
	public ILangObjectClass getBasicLangClass() {
		return clazz;
	}

	@Override
	public boolean equalsType(final IVariableType other) {
		if (clazz != other.getBasicLangClass())
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
		return clazz.toString();
	}

	@Override
	public IVariableType union(final IVariableType otherType) {
		if (clazz == otherType.getBasicLangClass() || otherType.getBasicLangClass() == ELangObjectType.NULL
				|| clazz == ELangObjectType.OBJECT)
			return this;
		if (clazz == ELangObjectType.NULL || otherType.getBasicLangClass() == ELangObjectType.OBJECT)
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
		if (clazz == otherType.getBasicLangClass() || otherType.getBasicLangClass() == ELangObjectType.NULL
				|| clazz == ELangObjectType.OBJECT)
			return true;
		return false;
	}

	@Override
	public boolean isIterable() {
		return getBasicLangClass().isIterable();
	}

	@Override
	public IVariableType getIterableItemType() {
		return clazz.getIterableItemType(CmnCnst.NonnullConstant.EMPTY_VARIABLE_TYPE_ARRAY);
	}
}