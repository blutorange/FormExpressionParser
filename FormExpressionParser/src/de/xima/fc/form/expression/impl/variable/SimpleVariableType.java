package de.xima.fc.form.expression.impl.variable;

import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;

@Immutable
@NonNullByDefault
public enum SimpleVariableType implements IVariableType {
	OBJECT(ELangObjectClass.OBJECT),
	NULL(ELangObjectClass.NULL),
	BOOLEAN(ELangObjectClass.BOOLEAN),
	NUMBER(ELangObjectClass.NUMBER),
	STRING(ELangObjectClass.STRING),
	REGEX(ELangObjectClass.REGEX),
	EXCEPTION(ELangObjectClass.EXCEPTION),
	;

	private static final long serialVersionUID = 1L;
	private final ILangObjectClass clazz;

	private SimpleVariableType(final ILangObjectClass type) {
		if (!type.allowsGenericsCountAndFlags(0, ImmutableSet.<EVariableTypeFlag>of()))
			throw new FormExpressionException();
		this.clazz = type;
	}

	@Override
	public ILangObjectClass getBasicLangClass() {
		return clazz;
	}

	@Override
	public boolean equalsType(final IVariableType that) {
		return GenericVariableType.equalsType(this, that);
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
		return clazz.getSyntacticalTypeName();
	}

	@Override
	public IVariableType union(final IVariableType that) {
		return GenericVariableType.union(this, that);
	}

	@Override
	public boolean isAssignableFrom(final IVariableType that) {
		return GenericVariableType.isAssignableFrom(this, that);
	}

	@Override
	public boolean isIterable() {
		return getBasicLangClass().isIterable();
	}

	@Override
	public IVariableType getIterableItemType() {
		return clazz.getIterableItemType(CmnCnst.NonnullConstant.EMPTY_VARIABLE_TYPE_ARRAY);
	}

	@Override
	public boolean isA(final ILangObjectClass baseClass) {
		return clazz.equalsClass(baseClass);
	}

	@Override
	public boolean hasFlag(final EVariableTypeFlag flag) {
		return false;
	}

	@Override
	public ImmutableCollection<EVariableTypeFlag> getFlags() {
		return ImmutableSet.<EVariableTypeFlag>of();
	}

	@Override
	public IVariableType upconvert(final ILangObjectClass superClass) {
		return GenericVariableType.upconvert(this, superClass);
	}
}