package de.xima.fc.form.expression.object;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.MathDivisionByZeroException;
import de.xima.fc.form.expression.exception.evaluation.MathException;
import de.xima.fc.form.expression.exception.evaluation.NumberTooLongForIntException;
import de.xima.fc.form.expression.exception.evaluation.NumberTooLongForLongException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Number objects are immutable. Performing any operations on a number object
 * will create a new object. <br>
 * <br>
 * Currently all numbers are implemented with <code<double</code>s. For
 * performance, this class could be subclassed to add implementations with
 * floats or ints. This class exposes only factory methods for object
 * construction, when subclasses are created, factory methods should be added to
 * this class.
 *
 * @author madgaksha
 */
public class NumberLangObject extends ALangObject {
	private final double value;

	final static ThreadLocal<NumberFormat> NUMBER_FORMAT = new ThreadLocal<NumberFormat>(){
		@Override protected NumberFormat initialValue() {
			final NumberFormat nf = new DecimalFormat("#0.#########", DecimalFormatSymbols.getInstance(Locale.ROOT)); //$NON-NLS-1$
			nf.setGroupingUsed(false);
			return nf;
		}
	};


	private static class InstanceHolder {
		@Nonnull public final static NumberLangObject ZERO = NumberLangObject.create(0);
		@Nonnull public final static NumberLangObject ONE = NumberLangObject.create(1);
		@Nonnull public static final NumberLangObject FOURTY_TWO = NumberLangObject.create(42);
		@Nonnull public static final NumberLangObject PI = NumberLangObject.create(Math.PI);
		@Nonnull public static final NumberLangObject E = NumberLangObject.create(Math.E);
	}

	private NumberLangObject(final double value) {
		super();
		this.value = value;
	}

	@Override
	public ELangObjectType getType() {
		return ELangObjectType.NUMBER;
	}
	
	public double doubleValue() {
		return value;
	}

	public long longValue(@Nonnull final IEvaluationContext ec) throws MathException {
		if (Double.isNaN(value) || value > 9007199254740993f || value < -9007199254740993f)
			throw new NumberTooLongForLongException(value, ec);
		return (long) value;
	}

	public int intValue(@Nonnull final IEvaluationContext ec) throws MathException {
		if (Double.isNaN(value) || value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
			throw new NumberTooLongForIntException(value, ec);
		return (int) value;
	}

	@Nonnull
	public static NumberLangObject create(final int value) {
		return new NumberLangObject(value);
	}

	@Nonnull
	public static NumberLangObject create(final long value) {
		return new NumberLangObject(value);
	}

	@Nonnull
	public static NumberLangObject create(final float value) {
		return new NumberLangObject(value);
	}

	@Nonnull
	public static NumberLangObject create(final double value) {
		return new NumberLangObject(value);
	}

	@Nonnull
	public static ALangObject create(final Integer value) {
		if (value == null)
			return NullLangObject.getInstance();
		return create(value.intValue());
	}

	@Nonnull
	public static ALangObject create(final Long value) {
		if (value == null)
			return NullLangObject.getInstance();
		return create(value.longValue());
	}

	@Nonnull
	public static NumberLangObject create(final Float value) {
		if (value == null)
			return NumberLangObject.getZeroInstance();
		return create(value.floatValue());
	}

	@Nonnull
	public static NumberLangObject create(final Double value) {
		if (value == null)
			return NumberLangObject.create(0);
		return create(value.doubleValue());
	}

	@Nonnull
	public static NumberLangObject create(final String value) {
		if (value == null)
			return NumberLangObject.getZeroInstance();
		try {
			return new NumberLangObject(Double.parseDouble(value));
		} catch (final NumberFormatException e) {
			return NumberLangObject.getZeroInstance();
		}
	}

	// Coercion
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return StringLangObject.create(value);
	}

	@Nonnull
	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		return RegexLangObject.createForString(NumberLangObject.toExpression(value));
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
	public int hashCode() {
		final long l = Double.doubleToLongBits(value);
		return (int) (l ^ (l >> 32));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof NumberLangObject))
			return false;
		return 0 == Double.compare(value, ((NumberLangObject)o).value);
	}

	@Override
	protected int compareToSameType(final ALangObject o) {
		return Double.compare(value, ((NumberLangObject)o).value);
	}

	@Override
	public IFunction<NumberLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().expressionMethodNumber(method);
	}

	@Override
	public IFunction<NumberLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorNumber(object, accessedViaDot);
	}

	@Override
	public IFunction<NumberLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerNumber(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodNumber(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorNumber(object, accessedViaDot), object,
				accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value,
			final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerNumber(object, accessedViaDot), object, accessedViaDot,
				value, ec);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_NUMBER_LANG_OBJECT).append('(')
				.append(value).append(')'));
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(toExpression(value));
	}

	public static String toExpression(final double value) {
		return NUMBER_FORMAT.get().format(value);
	}

	@Override
	public NonNullIterable<ALangObject> getIterable(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public NonNullIterator<ALangObject> iterator() {
		return new Itr();
	}

	@Override
	protected boolean isSingletonLike() {
		return true;
	}

	/**
	 * @param operand
	 *            Number to add.
	 * @return A new number, the sum of this number and the operand.
	 */
	@Nonnull
	public NumberLangObject add(final NumberLangObject operand) {
		return NumberLangObject.create(value + operand.value);
	}

	/**
	 * @param operand
	 *            Number to subtract.
	 * @return A new number, the difference of this number and the operand.
	 */
	@Nonnull
	public NumberLangObject subtract(final NumberLangObject operand) {
		return NumberLangObject.create(value - operand.value);
	}

	/**
	 * @param operand
	 *            Number to multiply by.
	 * @return A new number, the product of this number and the operand.
	 */
	@Nonnull
	public NumberLangObject multiply(final NumberLangObject operand) {
		return NumberLangObject.create(value * operand.value);
	}

	/**
	 * @param operand
	 *            Number to divide by.
	 * @return An new number, the quotient of this number and the operand.
	 * @throws MathDivisionByZeroException
	 *             When the operand is 0.
	 */
	@Nonnull
	public NumberLangObject divide(final NumberLangObject operand) {
		return NumberLangObject.create(value / operand.value);
	}

	public boolean isNaN() {
		return value != value;
	}

	public boolean isInfinite() {
		return value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY;
	}

	public boolean isFinite() {
		return value <= 0.0d ? -value <= Double.MAX_VALUE : value <= Double.MAX_VALUE;
	}

	@Nonnull
	public NumberLangObject abs() {
		return NumberLangObject.create(value < 0.0d ? -value : value);
	}

	@Nonnull
	public NumberLangObject signum() {
		return NumberLangObject.create(value > 0d ? 1 : value < 0d ? -1 : 0);
	}

	@Nonnull
	public NumberLangObject negate() {
		return NumberLangObject.create(-value);
	}

	/**
	 * @param other
	 * @return A number in the range [0...other).
	 */
	@Nonnull
	public NumberLangObject modulo(final NumberLangObject other) {
		final double res = Math.IEEEremainder(value, other.value);
		return NumberLangObject.create(res < 0d ? res + other.value : res);
	}

	@Nonnull
	public NumberLangObject remainder(final NumberLangObject other) {
		return NumberLangObject.create(value % other.value);
	}

	public NumberLangObject pow(final NumberLangObject operand) throws MathException {
		return NumberLangObject.create(Math.pow(value, operand.value));
	}

	@Nonnull
	public NumberLangObject log() throws MathException {
		return NumberLangObject.create(Math.log(value));
	}

	@Nonnull
	public NumberLangObject sin() throws MathException {
		return NumberLangObject.create(Math.sin(value));
	}

	@Nonnull
	public NumberLangObject cos() throws MathException {
		return NumberLangObject.create(Math.cos(value));
	}

	@Nonnull
	public static NumberLangObject getZeroInstance() {
		return InstanceHolder.ZERO;
	}

	@Nonnull
	public static NumberLangObject getOneInstance() {
		return InstanceHolder.ONE;
	}

	@Nonnull
	public static NumberLangObject getFourtyTwoInstance() {
		return InstanceHolder.FOURTY_TWO;
	}

	@Nonnull
	public static ALangObject getPiInstance() {
		return InstanceHolder.PI;
	}

	@Nonnull
	public static ALangObject getEInstance() {
		return InstanceHolder.E;
	}

	private class Itr implements NonNullIterator<ALangObject> {
		private double i = 0.0;
		@Override
		public boolean hasNext() {
			return i < value;
		}
		@Override
		public ALangObject next() {
			final ALangObject res = NumberLangObject.create(i);
			++i;
			return res;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException(CmnCnst.Error.NUMBER_ITERATOR_DOES_NOT_SUPPORT_REMOVAL);
		}
	}

}