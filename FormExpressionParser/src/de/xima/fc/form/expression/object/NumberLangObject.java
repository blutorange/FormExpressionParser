package de.xima.fc.form.expression.object;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.CoercionException;
import de.xima.fc.form.expression.error.EvaluationException;

public class NumberLangObject extends ALangObject {
	private final BigDecimal value;
	public final static MathContext MATH_CONTEXT = new MathContext(9, RoundingMode.HALF_UP);

	private NumberLangObject(final BigDecimal value) {
		super(Type.NUMBER);
		this.value = value;
	}

	public BigDecimal bigDecimalValue() {
		return value;
	}

	public static NumberLangObject create(final int value) {
		return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
	}

	public static NumberLangObject create(final long value) {
		return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
	}

	public static NumberLangObject create(final float value) {
		try {
			return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return NumberLangObject.create(0);
		}
	}

	public static NumberLangObject create(final double value) {
		try {
			return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return NumberLangObject.create(0);
		}
	}

	public static ALangObject create(final Integer value) {
		if (value == null)
			return NullLangObject.getInstance();
		return create(value.intValue());
	}

	public static ALangObject create(final Long value) {
		if (value == null)
			return NullLangObject.getInstance();
		return create(value.longValue());
	}

	public static NumberLangObject create(final Float value) {
		if (value == null)
			return NumberLangObject.create(0);
		return create(value.floatValue());
	}

	public static NumberLangObject create(final Double value) {
		if (value == null)
			return NumberLangObject.create(0);
		return create(value.doubleValue());
	}

	public static NumberLangObject create(final String value) {
		if (value == null)
			return NumberLangObject.create(0);
		try {
			return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return NumberLangObject.create(0);
		}
	}

	public static NumberLangObject create(final BigDecimal value) {
		if (value == null)
			return NumberLangObject.create(0);
		if (value.precision() == MATH_CONTEXT.getPrecision())
			return new NumberLangObject(value);
		return new NumberLangObject(new BigDecimal(value.toString(), MATH_CONTEXT));
	}

	public static NumberLangObject create(final BigInteger value) {
		if (value == null)
			return NumberLangObject.create(0);
		return new NumberLangObject(new BigDecimal(value, MATH_CONTEXT));
	}

	// Coercion
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return StringLangObject.create(value);
	}
	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return value.compareTo(BigDecimal.ZERO) == 0 ? BooleanLangObject.getFalseInstance()
				: BooleanLangObject.getTrueInstance();
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public ALangObject shallowClone() {
		return new NumberLangObject(value);
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args)
			throws EvaluationException {
		return this.evaluateMethod(this, ec.getNamespace().instanceMethodNumber(name), name, ec, args);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof NumberLangObject))
			return false;
		final NumberLangObject other = (NumberLangObject) o;
		return value.equals(other.value);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateMethod(this, ec.getNamespace().attrAccessorNumber(name), name, ec);
	}

	@Override
	public String inspect() {
		return "NumberLangObject(" + value.toString() + ")";
	}

	@Override
	public String toString() {
		return value.toPlainString();
	}

}