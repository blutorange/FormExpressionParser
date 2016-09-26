package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
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
		return value ? StringLangObject.getTrueInstance() : StringLangObject.getFalseInstance();
	}
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return NumberLangObject.create(value ? 1 : 0);
	}
	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return this;
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
	public IFunction<BooleanLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodBoolean(method);
	}

	@Override
	public IFunction<BooleanLangObject> attrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorBoolean(name);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodBoolean(method), method, ec, args);
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
