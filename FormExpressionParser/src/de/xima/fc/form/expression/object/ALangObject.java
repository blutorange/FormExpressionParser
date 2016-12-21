package de.xima.fc.form.expression.object;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IterationNotSupportedException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.exception.evaluation.NoSuchMethodException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * @formatter:off
 * <h1>Coercion rules</h1> Right: Object to be coerced. Down: Coercion target.
 * <table style="text-align:center;">
 * <th>
 * <td>{@link NullLangObject}</td>
 * <td>{@link BooleanLangObject}</td>
 * <td>{@link NumberLangObject}</td>
 * <td>{@link StringLangObject}</td>
 * <td>{@link RegexLangObject}</td>
 * <td>{@link ArrayLangObject}</td>
 * <td>{@link HashLangObject}</td>
 * <td>{@link ExceptionLangObject}</td>
 * <td>{@link FunctionLangObject}</td>
 * <th>
 * <tr>
 * <td>{@link NullLangObject}</td>
 * <td>-</td>
 * <td><code>false</code></td>
 * <td><code>0</code></td>
 * <td><code>""</code></td>
 * <td><code>A regex that matches nothing.</code></td>
 * <td><code>[]</code></td>
 * <td><code>{}</code></td>
 * <td><code>exception("")</code></td>
 * <td>{@link FunctionLangObject#getNoOpNull()}</td>
 * </tr>
 * <tr>
 * <td>{@link BooleanLangObject}</td>
 * <td>-</td>
 * <td>-</td>
 * <td><code>0</code> / <code>1</code></td>
 * <td><code>false</code> / <code>true</code></td>
 * <td>A regex that matches nothing or everything.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link NumberLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>-</td>
 * <td>Regex that matches {@link NumberLangObject#toString()}</td>
 * <td>Decimal representation of the number, eg <code>1.0</code>.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link StringLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>Interpreted as decimal; or <code>0</code> when not a valid decimal
 * number.</td>
 * <td>-</td>
 * <td>Regex that matches this string.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link RegexLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>{@link CoercionException}</td>
 * <td>As a regex literal, eg. <code>#0x\d+#i</code></td>
 * <td>-</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link ArrayLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link ArrayLangObject#toExpression(StringBuilder)}</td>
 * <td>{@link CoercionException}</td>
 * <td>-</td>
 * <td>Each pair is interpreted as a key-value pair. When it contains an odd
 * number of entries, the last key will be mapped to <code>null</code>.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link HashLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link HashLangObject#toExpression(StringBuilder)}</td>
 * <td>{@link CoercionException}</td>
 * <td>An array twice the size of the hash, with each key as the
 * <code>2n</code>-th entry and the corresponding value as the
 * <code>2n+1</code>-th entry.</td>
 * <td>-</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link ExceptionLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>{@link CoercionException}</td>
 * <td>Message of the exception.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>-</td>
 * <td>{@link CoercionException}</td>
 * </tr>
 * <tr>
 * <td>{@link FunctionLangObject}</td>
 * <td>-</td>
 * <td>true</td>
 * <td>{@link CoercionException}</td>
 * <td>Declared name of the function. Empty string when anonymous function.</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>{@link CoercionException}</td>
 * <td>-</td>
 * </tr>
 * </table>
 * @formatter:on
 *
 * @author mad_gaksha
 *
 */
@ParametersAreNonnullByDefault
public abstract class ALangObject implements INonNullIterable<ALangObject>, Comparable<ALangObject> {
	private static AtomicLong ID_COUNTER = new AtomicLong();

	private final long id;

	@Nonnull
	protected ALangObject() {
		id = ID_COUNTER.incrementAndGet();
	}

	public abstract ALangObject shallowClone();

	public abstract ALangObject deepClone();

	/**
	 * @return An expression that evaluates to this object. Eg. for a String
	 *         <code>"</code>, this would return <code>"\""</code>
	 * @see #toExpression(StringBuilder)
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toExpression(sb);
		return NullUtil.orEmpty(sb.toString());
	}

	/**
	 * @param bulder
	 *            String builder used to build the string.
	 * @return An expression that evaluates to this object. Eg. for a String
	 *         <code>"</code>, this would return <code>"\""</code>
	 * @see #toString()
	 */
	public abstract void toExpression(StringBuilder builder);

	/**
	 * Override this for different conversions.
	 *
	 * @param ec
	 *            Context to be used.
	 */
	public StringLangObject coerceString(final IEvaluationContext ec) {
		if (getObjectClass() == ELangObjectClass.STRING)
			return (StringLangObject) this;
		return StringLangObject.create(toString());
	}

	public ArrayLangObject coerceArray(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.ARRAY)
			return (ArrayLangObject) this;
		throw new CoercionException(this, ELangObjectClass.ARRAY, ec);
	}

	public HashLangObject coerceHash(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.HASH)
			return (HashLangObject) this;
		throw new CoercionException(this, ELangObjectClass.HASH, ec);
	}

	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.NUMBER)
			return (NumberLangObject) this;
		throw new CoercionException(this, ELangObjectClass.NUMBER, ec);
	}

	/**
	 * Can be overridden, but I recommend not to. By default, only
	 * <code>null</code> and <code>false</code> is coerced to
	 * <code>false</code>, everything else to <code>true</code>.
	 *
	 * @param ec
	 *            Context to use.
	 * @return Coerced object.
	 * @throws CoercionException
	 *             When the object cannot be coerced.
	 */
	public final BooleanLangObject coerceBoolean(final IEvaluationContext ec) {
		if (getObjectClass().equalsClass(ELangObjectClass.BOOLEAN))
			return (BooleanLangObject) this;
		if (getObjectClass().equalsClass(ELangObjectClass.NULL))
			return BooleanLangObject.getFalseInstance();
		return BooleanLangObject.getTrueInstance();
	}

	public ExceptionLangObject coerceException(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.EXCEPTION)
			return (ExceptionLangObject) this;
		throw new CoercionException(this, ELangObjectClass.EXCEPTION, ec);
	}

	public FunctionLangObject coerceFunction(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.FUNCTION)
			return (FunctionLangObject) this;
		throw new CoercionException(this, ELangObjectClass.FUNCTION, ec);
	}

	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		if (getObjectClass() == ELangObjectClass.REGEX)
			return (RegexLangObject) this;
		throw new CoercionException(this, ELangObjectClass.REGEX, ec);
	}

	@SuppressWarnings("unchecked")
	public final <T extends ALangObject> T coerce(final ILangObjectClass type, final IEvaluationContext ec)
			throws CoercionException {
		if (type.supportsBasicCoercion())
			return (T) type.coerce(this, ec);
		// Try to coerce object with the special coerce method, when defined.
		try {
			return (T) evaluateExpressionMethod(EMethod.COERCE, ec, StringLangObject.create(type.getSyntacticalTypeName()));
		}
		catch (final EvaluationException e) {
			throw new CoercionException(this, type, ec);
		}
		catch (final ClassCastException e) {
			throw new CoercionException(this, type, ec);
		}
	}

	/**
	 * @param type
	 *            Type to check against.
	 * @return Whether this object is of the given type.
	 */
	public boolean is(final ILangObjectClass type) {
		return getObjectClass().getClassId().equals(type.getClassId());
	}

	/** @return The id of this object. */
	public long getId() {
		return id;
	}

	public final ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException {
		// The namespace returns a function such that the generics argument is
		// that of the current run-time class or some super class.
		// This is better than having to add all 10 methods to each subclass to
		// get the correct generics type.
		@SuppressWarnings({ "unchecked" })
		final IFunction<ALangObject> func = (IFunction<ALangObject>) expressionMethod(method, ec);
		if (func == null)
			throw new NoSuchMethodException(method, this, ec);
		return ALangObject.create(func.evaluate(ec, this, args));
	}

	public final ALangObject evaluateDotAccessor(final String property, final IEvaluationContext ec)
			throws EvaluationException {
		// The namespace returns a function such that the generics argument is
		// that of the current run-time class or some super class.
		// This is better than having to add all 10 methods to each subclass to
		// get the correct generics type.
		@SuppressWarnings("unchecked")
		final IFunction<ALangObject> func = (IFunction<ALangObject>) dotAccessor(property, ec);
		if (func == null)
			throw new NoSuchAttrAccessorException(property, this, true, ec);
		return func.evaluate(ec, this, StringLangObject.create(property));
	}

	public final ALangObject evaluateBracketAccessor(final ALangObject property, final IEvaluationContext ec)
			throws EvaluationException {
		// The namespace returns a function such that the generics argument is
		// that of the current run-time class or some super class.
		// This is better than having to add all 10 methods to each subclass to
		// get the correct generics type.
		@SuppressWarnings("unchecked")
		final IFunction<ALangObject> func = (IFunction<ALangObject>) bracketAccessor(ec);
		if (func == null)
			throw new NoSuchAttrAccessorException(NullUtil.toString(property), this, false, ec);
		return func.evaluate(ec, this, property);
	}

	public final void executeDotAssigner(final String property, final ALangObject value, final IEvaluationContext ec)
			throws EvaluationException {
		// The namespace returns a function such that the generics argument is
		// that of the current run-time class or some super class.
		// This is better than having to add all 10 methods to each subclass to
		// get the correct generics type.
		@SuppressWarnings("unchecked")
		final IFunction<ALangObject> func = (IFunction<ALangObject>) dotAssigner(property, ec);
		if (func == null)
			throw new NoSuchAttrAssignerException(property, this, true, ec);
		func.evaluate(ec, this, StringLangObject.create(property), value);
	}

	public final void executeBracketAssigner(final ALangObject property, final ALangObject value,
			final IEvaluationContext ec) throws EvaluationException {
		// The namespace returns a function such that the generics argument is
		// that of the current run-time class or some super class.
		// This is better than having to add all 10 methods to each subclass to
		// get the correct generics type.
		@SuppressWarnings("unchecked")
		final IFunction<ALangObject> func = (IFunction<ALangObject>) bracketAssigner(ec);
		if (func == null)
			throw new NoSuchAttrAssignerException(NullUtil.toString(property), this, false, ec);
		func.evaluate(ec, this, property, value);
	}

	@Nullable
	public final IFunction<? extends ALangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) {
		return ec.getNamespace().expressionMethod(getObjectClass(), method);
	}

	@Nullable
	public final IFunction<? extends ALangObject> dotAccessor(final String property, final IEvaluationContext ec) {
		return ec.getNamespace().dotAccessor(getObjectClass(), property);
	}

	@Nullable
	public final IFunction<? extends ALangObject> dotAssigner(final String property, final IEvaluationContext ec) {
		return ec.getNamespace().dotAssigner(getObjectClass(), property);
	}

	@Nullable
	public final IFunction<? extends ALangObject> bracketAccessor(final IEvaluationContext ec) {
		return ec.getNamespace().bracketAccessor(getObjectClass());
	}

	@Nullable
	public final IFunction<? extends ALangObject> bracketAssigner(final IEvaluationContext ec) {
		return ec.getNamespace().bracketAssigner(getObjectClass());
	}

	public INonNullIterable<ALangObject> getIterable(final IEvaluationContext ec)
			throws IterationNotSupportedException {
		throw new IterationNotSupportedException(this, ec);
	}

	/** @deprecated Use {@link #getIterable(IEvaluationContext)}. */
	@Override
	@Nonnull
	@Deprecated
	public INonNullIterator<ALangObject> iterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(CmnCnst.Error.DEPRECATED_ALANGOBJECT_ITERATOR);
	}

	@Override
	public int hashCode() {
		return (int) (id ^ id >>> 32);
	}

	@Override
	public abstract boolean equals(@Nullable final Object o);

	/**
	 * <pre>
	 * The natural ordering for a class C is said to be consistent with equals if and only
	 * if e1.compareTo(e2) == 0 has the same boolean value as e1.equals(e2) for every e1
	 * and e2 of class C. Note that null is not an instance of any class, and e.compareTo(null)
	 * should throw a NullPointerException even though e.equals(null) returns false.
	 * </pre>
	 *
	 * <p>
	 * This holds true. When the comparand is null <code>o.type</code> throws a
	 * NullPointerException. When the two objects are of different type, they
	 * are ordered according to {@link ELangObjectClass#id} and cannot be equal.
	 * When the two objects are of the same type, the abstract method
	 * {@link ALangObject#compareToSameType(ALangObject)} is called. Subclasses
	 * are required to adhere to the above contract.
	 * </p>
	 *
	 */
	@Override
	public final int compareTo(@Nullable final ALangObject o) {
		if (o == null)
			return -1;
		if (getObjectClass() != o.getObjectClass())
			return getObjectClass().getClassId() - o.getObjectClass().getClassId();
		return compareToSameType(o);
	}

	public final int compareById(final ALangObject o) {
		return Long.compare(id, o.id);
	}

	/**
	 * @param o
	 *            Object to check.
	 * @return Whether the given object is the same object as this object.
	 */
	public boolean equalsSameObject(final ALangObject o) {
		return isSingletonLike() ? equals(o) : id == o.id;
	}

	/**
	 * @return Whether two separate, but equal, instances should be treated as
	 *         the same object. Should be true for numbers and booleans.
	 */
	protected abstract boolean isSingletonLike();

	/**
	 * Needs to adhere to the contract of {@link Comparable}.
	 *
	 * @param o
	 * @return Guaranteed to be be non-<code>null</code>. An object of the same
	 *         type as the subclass. It may be cast safely.
	 */
	protected abstract int compareToSameType(ALangObject o);

	/**
	 * May be overridden for specific objects.
	 *
	 * @return Details on the object, such as its class and its fields.
	 */
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_A_LANG_OBJECT).append(id));
	}

	public static ALangObject create(@Nullable final ALangObject value) {
		if (value == null)
			return NullLangObject.getInstance();
		return value;
	}

	public abstract ILangObjectClass getObjectClass();

	public final boolean isArray() {
		return getObjectClass() == ELangObjectClass.ARRAY;
	}

	public final boolean isNumber() {
		return getObjectClass() == ELangObjectClass.NUMBER;
	}

	public final boolean isString() {
		return getObjectClass() == ELangObjectClass.STRING;
	}

	public final boolean isHash() {
		return getObjectClass() == ELangObjectClass.HASH;
	}

	public final boolean isBoolean() {
		return getObjectClass() == ELangObjectClass.BOOLEAN;
	}

	public final boolean isFunction() {
		return getObjectClass() == ELangObjectClass.FUNCTION;
	}

	public final boolean isException() {
		return getObjectClass() == ELangObjectClass.EXCEPTION;
	}

	public final boolean isRegex() {
		return getObjectClass() == ELangObjectClass.REGEX;
	}

	public final boolean isNull() {
		return getObjectClass() == ELangObjectClass.NULL;
	}
}