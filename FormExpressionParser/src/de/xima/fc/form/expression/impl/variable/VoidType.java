package de.xima.fc.form.expression.impl.variable;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSortedSet;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum VoidType implements IVariableType {
	INSTANCE;

	@Override
	public boolean equalsType(final IVariableType that) {
		return that.getBasicLangClass().getClassId().equals(getBasicLangClass().getClassId());
	}

	@Override
	public ILangObjectClass getBasicLangClass() {
		return VoidClass.INSTANCE;
	}

	@Override
	public int getGenericCount() {
		return 0;
	}

	@Override
	public IVariableType getGeneric(final int i) throws ArrayIndexOutOfBoundsException {
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public IVariableType union(final IVariableType type) {
		return this;
	}

	@Override
	public boolean isAssignableFrom(final IVariableType type) {
		return false;
	}

	@Override
	public boolean isIterable() {
		return false;
	}

	@Override
	public IVariableType getIterableItemType() {
		throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.TYPE_NOT_ITERABLE, toString()));
	}

	@Override
	public boolean isA(final ILangObjectClass baseClass) {
		return baseClass.getClassId().equals(VoidClass.INSTANCE.getClassId());
	}

	@Override
	public boolean hasFlag(final EVariableTypeFlag flag) {
		return false;
	}

	@Override
	public ImmutableCollection<EVariableTypeFlag> getFlags() {
		return ImmutableSortedSet.of();
	}

	@Override
	public IVariableType upconvert(final ILangObjectClass superClass) {
		return VoidType.INSTANCE;
	}

	@Override
	public String toString() {
		return CmnCnst.Name.VOID_TYPE;
	}
}