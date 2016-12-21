package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableCollection;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;

@ParametersAreNonnullByDefault
public enum VoidClass implements ILangObjectClass {
	INSTANCE;
	private final static Integer ID = new Integer(255);

	@Override
	public Integer getClassId() {
		return ID;
	}

	@Nullable
	@Override
	public ILangObjectClass getSuperClass() {
		return null;
	}

	@Nullable
	@Override
	public IVariableType getSuperType(final IVariableType type) {
		return null;
	}

	@Override
	public Class<? extends ALangObject> getLangObjectClass() {
		return NullLangObject.class;
	}

	@Override
	public String getSyntacticalTypeName() {
		return CmnCnst.Syntax.VOID;
	}

	@Override
	public boolean isIterable() {
		return false;
	}

	@Override
	public boolean allowsGenericsCountAndFlags(final int count, final ImmutableCollection<EVariableTypeFlag> flags) {
		return count == 0 && flags.isEmpty();
	}

	@Override
	public IVariableType getIterableItemType(final IVariableType[] generics)
			throws IllegalVariableTypeException, ArrayIndexOutOfBoundsException {
		throw new IllegalVariableTypeException("not iterable");
	}

	@Override
	public IVariableType getSimpleVariableType() throws IllegalVariableTypeException {
		return VoidType.INSTANCE;
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean equalsClass(final ILangObjectClass clazz) {
		return false;
	}

	@Override
	public boolean isSuperClassOf(final ILangObjectClass that) {
		return false;
	}

	@Override
	public String toString() {
		return CmnCnst.Name.VOID_TYPE;
	}

	@Override
	public boolean supportsBasicCoercion() {
		return true;
	}

	@Override
	public ALangObject coerce(final ALangObject object, final IEvaluationContext ec) throws CoercionException {
		throw new CoercionException(NullLangObject.getInstance(), VoidClass.INSTANCE, ec);
	}
}