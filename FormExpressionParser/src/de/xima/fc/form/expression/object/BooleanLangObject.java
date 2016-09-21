package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;

public class BooleanLangObject extends ALangObject {
	private final boolean value;

	private BooleanLangObject(final boolean value) {
		super(Type.BOOLEAN);
		this.value = value;
	}

	private static class InstanceHolder {
		private static BooleanLangObject TRUE = new BooleanLangObject(true);
		private static BooleanLangObject FALSE = new BooleanLangObject(false);
	}

	public boolean booleanValue() {
		return value;
	}

	// Coercion
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return StringLangObject.create(value);
	}
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return NumberLangObject.create(value ? 1 : 0);
	}



	/**
	 *  @return <code>this</code>, as only one object exists for true and false.
	 */
	@Override
	public ALangObject shallowClone() {
		return this;
	}

	/**
	 *  @return <code>this</code>, as only one object exists for true and false.
	 */
	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public INamedFunction<BooleanLangObject> instanceMethod(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().instanceMethodBoolean(name);
	}
	@Override
	public INamedFunction<BooleanLangObject> attrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorBoolean(name);
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args)
			throws EvaluationException {
		return this.evaluateMethod(this, ec.getNamespace().instanceMethodBoolean(name), name, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorBoolean(name), name, ec);
	}

	@Override
	public int hashCode() {
		return value ? 0 : 1;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof BooleanLangObject)) return false;
		return value == ((BooleanLangObject)o).value;
	}

	@Override
	public String inspect() {
		return "BooleanLangObject(" + value + ")";
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(value ? "§true" : "§false");
	}

	/** @deprecated Use {@link #getTrueInstance()} or {@link #getFalseInstance()} */
	@Deprecated
	public static ALangObject create(final boolean b) {
		return b ? getTrueInstance() : getFalseInstance();
	}

	public static BooleanLangObject getTrueInstance() {
		return InstanceHolder.TRUE;
	}

	public static BooleanLangObject getFalseInstance() {
		return InstanceHolder.FALSE;
	}

}
