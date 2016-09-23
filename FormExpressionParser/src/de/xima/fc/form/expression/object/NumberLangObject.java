package de.xima.fc.form.expression.object;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.MathDivisionByZeroException;
import de.xima.fc.form.expression.exception.MathException;

/**
 * Number objects are immutable. Performing any operations on a number object
 * will create a new object. <br>
 * <br>
 * Currently all numbers are implemented with {@link BigDecimal}. For
 * performance, this class can be subclassed to add implementations with floats
 * or ints. This class exposes only factory methods for object construction,
 * when subclasses are created, factory methods should be added to this class.
 *
 * @author madgaksha
 */
public class NumberLangObject extends ALangObject {
	private final BigDecimal value;
	public final static MathContext MATH_CONTEXT = new MathContext(9, RoundingMode.HALF_UP);

	private static class InstanceHolder {
		public final static NumberLangObject ZERO = NumberLangObject.create(0);
		public final static NumberLangObject ONE = NumberLangObject.create(1);
	}

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
		return BooleanLangObject.getTrueInstance();
	}

	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return this;
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
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorNumber(name), name, ec);
	}

	@Override
	public INamedFunction<NumberLangObject> instanceMethod(final String name, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().instanceMethodNumber(name);
	}

	@Override
	public INamedFunction<NumberLangObject> attrAccessor(final String name, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().attrAccessorNumber(name);
	}

	@Override
	public String inspect() {
		return "NumberLangObject(" + value.toString() + ")";
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(value.toPlainString());
	}

	@Override
	public Iterator<ALangObject> iterator() {
		return new Iterator<ALangObject>() {
			private BigDecimal i = BigDecimal.ZERO;
			@Override
			public boolean hasNext() {
				return i.compareTo(value) < 0;
			}
			@Override
			public ALangObject next() {
				final ALangObject res = StringLangObject.create(i);
				i = i.add(BigDecimal.ONE);
				return res;
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException("Removal not supported for NumberLangObject::iterator.");
			}			
		};
	}

	/**
	 * @param operand
	 *            Number to add.
	 * @return A new number, the sum of this number and the operand.
	 */
	public NumberLangObject add(final NumberLangObject operand) {
		return NumberLangObject.create(bigDecimalValue().add(operand.bigDecimalValue()));
	}

	/**
	 * @param operand
	 *            Number to subtract.
	 * @return A new number, the difference of this number and the operand.
	 */
	public NumberLangObject subtract(final NumberLangObject operand) {
		return NumberLangObject.create(bigDecimalValue().subtract(operand.bigDecimalValue()));
	}

	/**
	 * @param operand
	 *            Number to multiply by.
	 * @return A new number, the product of this number and the operand.
	 */
	public NumberLangObject multiply(final NumberLangObject operand) {
		return NumberLangObject.create(bigDecimalValue().multiply(operand.bigDecimalValue()));
	}

	/**
	 * @param operand
	 *            Number to divide by.
	 * @return An new number, the quotient of this number and the operand.
	 * @throws MathDivisionByZeroException When the operand is 0.
	 */
	public NumberLangObject divide(final NumberLangObject operand)
			throws MathDivisionByZeroException {
		try {
			return NumberLangObject.create(bigDecimalValue().divide(operand.bigDecimalValue(), MATH_CONTEXT));
		} catch (final ArithmeticException e) {
			throw new MathDivisionByZeroException(this, operand);
		}
	}

	public NumberLangObject abs() {
		return NumberLangObject.create(bigDecimalValue().abs(MATH_CONTEXT));
	}

	public NumberLangObject signum() {
		return NumberLangObject.create(bigDecimalValue().signum());
	}

	public NumberLangObject negate() {
		return NumberLangObject.create(bigDecimalValue().negate(MATH_CONTEXT));
	}

	public NumberLangObject round(final int precision) {
		return NumberLangObject.create(bigDecimalValue().round(new MathContext(precision, MATH_CONTEXT.getRoundingMode())));
	}

	public NumberLangObject pow(final NumberLangObject operand) throws MathException {
		final double x1 = bigDecimalValue().doubleValue();
		final double x2 = operand.bigDecimalValue().doubleValue();
		if (Double.isInfinite(x1)) throw new MathException("Base too large for double: " + inspect());
		if (Double.isInfinite(x2)) throw new MathException("Exponent too large for double: " + operand.inspect());
		return NumberLangObject.create(Math.pow(x1, x2));
	}

	public NumberLangObject log() throws MathException {
		final double x = bigDecimalValue().doubleValue();
		if (Double.isInfinite(x)) throw new MathException("Number too large for double: " + inspect());
		return NumberLangObject.create(Math.log(x));
	}

	public NumberLangObject sin() throws MathException {
		final double x = bigDecimalValue().doubleValue();
		if (Double.isInfinite(x)) throw new MathException("Number too large for double: " + inspect());
		return NumberLangObject.create(Math.sin(x));
	}

	public NumberLangObject cos() throws MathException {
		final double x = bigDecimalValue().doubleValue();
		if (Double.isInfinite(x)) throw new MathException("Number too large for double: " + inspect());
		return NumberLangObject.create(Math.cos(x));
	}

	public static NumberLangObject getZeroInstance() {
		return InstanceHolder.ZERO;
	}

	public static NumberLangObject getOneInstance() {
		return InstanceHolder.ONE;
	}
}