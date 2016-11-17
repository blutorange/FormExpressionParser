package de.xima.fc.form.expression.object;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IterationNotSupportedException;
import de.xima.fc.form.expression.exception.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.exception.NoSuchAttrAssignerException;
import de.xima.fc.form.expression.exception.NoSuchMethodException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * <h1>Coercion rules</h1>
 * Right: Object to be coerced.
 * Down: Coercion target.
 * <table style="text-align:center;">
 *   <th>
 *     <td>{@link NullLangObject}</td>
 *     <td>{@link BooleanLangObject}</td>
 *     <td>{@link NumberLangObject}</td>
 *     <td>{@link StringLangObject}</td>
 *     <td>{@link RegexLangObject}</td>
 *     <td>{@link ArrayLangObject}</td>
 *     <td>{@link HashLangObject}</td>
 *     <td>{@link ExceptionLangObject}</td>
 *     <td>{@link FunctionLangObject}</td>
 *   <th>
 *   <tr>
 *     <td>{@link NullLangObject}</td>
 *     <td>-</td>
 *     <td><code>false</code></td>
 *     <td><code>0</code></td>
 *     <td><code>""</code></td>
 *     <td><code>A regex that matches nothing.</code></td>
 *     <td><code>[]</code></td>
 *     <td><code>{}</code></td>
 *     <td><code>exception("")</code></td>
 *     <td>{@link FunctionLangObject#getNoOpInstance()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link BooleanLangObject}</td>
 *     <td>-</td>
 *     <td>-</td>
 *     <td><code>0</code> / <code>1</code></td>
 *     <td><code>false</code> / <code>true</code></td>
 *     <td>A regex that matches nothing or everything.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link NumberLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>-</td>
 *     <td>Regex that matches {@link NumberLangObject#toString()}</td>
 *     <td>Decimal representation of the number, eg <code>1.0</code>.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link StringLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>Interpreted as decimal; or <code>0</code> when not a valid decimal number.</td>
 *     <td>-</td>
 *     <td>Regex that matches this string.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link RegexLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>{@link CoercionException}</td>
 *     <td>As a regex literal, eg. <code>#0x\d+#i</code></td>
 *     <td>-</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ArrayLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link ArrayLangObject#toExpression(StringBuilder)}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>-</td>
 *     <td>Each pair is interpreted as a key-value pair. When it contains an odd number of entries, the last key will be mapped to <code>null</code>.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link HashLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link HashLangObject#toExpression(StringBuilder)}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>An array twice the size of the hash, with each key as the <code>2n</code>-th entry and the corresponding value as the <code>2n+1</code>-th entry.</td>
 *     <td>-</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ExceptionLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>{@link CoercionException}</td>
 *     <td>Message of the exception.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>-</td>
 *     <td>{@link CoercionException}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link FunctionLangObject}</td>
 *     <td>-</td>
 *     <td>true</td>
 *     <td>{@link CoercionException}</td>
 *     <td>Declared name of the function. Empty string when anonymous function.</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>{@link CoercionException}</td>
 *     <td>-</td>
 *   </tr>
 * </table>
 * @author mad_gaksha
 *
 */
public abstract class ALangObject implements NonNullIterable<ALangObject>, Comparable<ALangObject> {
	private final static Logger LOG = Logger.getLogger(ALangObject.class.getCanonicalName());

	private final Type type;
	private final long id;
	private static AtomicLong ID_COUNTER = new AtomicLong();

	public static enum Type {
		NULL(NullLangObject.class, 0),
		BOOLEAN(BooleanLangObject.class, 1),
		NUMBER(NumberLangObject.class, 2),
		STRING(StringLangObject.class, 3),
		REGEX(RegexLangObject.class, 4),
		FUNCTION(FunctionLangObject.class, 5),
		EXCEPTION(ExceptionLangObject.class, 6),
		ARRAY(ArrayLangObject.class, 7),
		HASH(HashLangObject.class, 8),
		;

		public final Class<? extends ALangObject> clazz;
		public final int order;

		private Type(final Class<? extends ALangObject> clazz, final int order) {
			this.clazz = clazz;
			this.order = order;
		}
	}

	@Nonnull
	public abstract ALangObject shallowClone();

	@Nonnull
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
		return sb.toString();
	}

	/**
	 * @param bulder String builder used to build the string.
	 * @return An expression that evaluates to this object. Eg. for a String
	 *         <code>"</code>, this would return <code>"\""</code>
	 * @see #toString()
	 */
	public abstract void toExpression(@Nonnull StringBuilder builder);

	/**
	 * Override this for different conversions.
	 * @param ec Context to be used.
	 */
	@Nonnull
	public StringLangObject coerceString(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.STRING)
			return (StringLangObject) this;
		return StringLangObject.create(toString());
	}

	@Nonnull
	public ArrayLangObject coerceArray(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.ARRAY)
			return (ArrayLangObject) this;
		throw new CoercionException(this, Type.ARRAY, ec);
	}

	@Nonnull
	public HashLangObject coerceHash(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.HASH)
			return (HashLangObject) this;
		throw new CoercionException(this, Type.HASH, ec);
	}

	@Nonnull
	public NumberLangObject coerceNumber(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.NUMBER)
			return (NumberLangObject) this;
		throw new CoercionException(this, Type.NUMBER, ec);
	}

	/**
	 * Can be overridden, but I recommend not to. By default, only
	 * <code>null</code> and <code>false</code> is coerced to <code>false</code>,
	 * everything else to <code>true</code>.
	 * @param ec Context to use.
	 * @return Coerced object.
	 * @throws CoercionException When the object cannot be coerced.
	 */
	@Nonnull
	public BooleanLangObject coerceBoolean(@Nonnull final IEvaluationContext ec) throws CoercionException {
		switch (getType()) {
		case BOOLEAN:
			return (BooleanLangObject)this;
		case NULL:
			return BooleanLangObject.getFalseInstance();
			//$CASES-OMITTED$
		default:
			return BooleanLangObject.getTrueInstance();
		}
	}

	@Nonnull
	public ExceptionLangObject coerceException(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.EXCEPTION)
			return (ExceptionLangObject) this;
		throw new CoercionException(this, Type.EXCEPTION, ec);
	}

	@Nonnull
	public FunctionLangObject coerceFunction(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.FUNCTION)
			return (FunctionLangObject) this;
		throw new CoercionException(this, Type.FUNCTION, ec);
	}

	@Nonnull
	public RegexLangObject coerceRegex(@Nonnull final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.REGEX)
			return (RegexLangObject) this;
		throw new CoercionException(this, Type.REGEX, ec);
	}

	/**
	 * Convenience method when the caller does not need the result of the
	 * correct class.
	 *
	 * @param type
	 *            Type to which this object should be coerced.
	 * @param clazz Expected return class of the coercion. Must match the type.
	 * @param ec
	 *            Current evaluation context.
	 * @return The coerced object.
	 * @throws CoercionException
	 *             When this object cannot be coerced to the given type.
	 * @throws EvaluationException When clazz and type do not match.
	 */
	@Nonnull
	@SuppressWarnings("unchecked")
	public final <T extends ALangObject> T coerce(@Nonnull final Type type, @Nonnull final Class<T> clazz,
			@Nonnull final IEvaluationContext ec) throws CoercionException, EvaluationException {
		// This error can happen only if a subclass is constructed with the wrong type, or the Type enum contains the wrong class.
		if (clazz != type.clazz) throw new EvaluationException(ec, CmnCnst.Error.COERCION_TYPE_NOT_MATCHING);
		if (type == getType())
			return (T)this;
		switch (type) {
		case ARRAY:
			return (T)coerceArray(ec);
		case HASH:
			return (T)coerceHash(ec);
		case NULL:
			throw new CoercionException(this, Type.NULL, ec);
		case NUMBER:
			return (T)coerceNumber(ec);
		case STRING:
			return (T)coerceString(ec);
		case BOOLEAN:
			return (T)coerceBoolean(ec);
		case EXCEPTION:
			return (T)coerceException(ec);
		case FUNCTION:
			return (T)coerceFunction(ec);
		case REGEX:
			return (T)coerceRegex(ec);
		default:
			// Try to coerce object with the special coerce method, when defined.
			LOG.info("Enum might not be implemented: " + type); //$NON-NLS-1$
			try {
				return (T)evaluateExpressionMethod(EMethod.COERCE, ec, StringLangObject.create(type.name()));
			}
			catch (final EvaluationException e) {
				throw new CoercionException(this, type, ec);
			}
			catch (final ClassCastException e) {
				throw new CoercionException(this, type, ec);
			}
		}
	}

	/**
	 * @param type
	 *            Type to check against.
	 * @return Whether this object is of the given type.
	 */
	public boolean is(@Nonnull final Type type) {
		return this.type == type;
	}

	@Nonnull
	protected ALangObject(@Nonnull final Type type) {
		this.type = type;
		id = ID_COUNTER.incrementAndGet();
	}

	public long getId() {
		return id;
	}

	@Nonnull
	protected final static <T extends ALangObject> ALangObject evaluateExpressionMethod(@Nonnull final T thisContext,
			@Nullable final IFunction<T> function, @Nonnull final EMethod method, @Nonnull final IEvaluationContext ec,
			@Nonnull final ALangObject... args) throws NoSuchMethodException, EvaluationException {
		if (function == null)
			throw new NoSuchMethodException(method, thisContext, ec);
		return ALangObject.create(function.evaluate(ec, thisContext, args));
	}

	@Nonnull
	protected final static <T extends ALangObject> ALangObject evaluateAttrAccessor(@Nonnull final T thisContext,
			@Nullable final IFunction<T> function, @Nonnull final ALangObject accessor, final boolean accessedViaDot,
			@Nonnull final IEvaluationContext ec) throws NoSuchAttrAccessorException, EvaluationException {
		if (function == null)
			throw new NoSuchAttrAccessorException(NullUtil.toString(accessor), thisContext, ec);
		return function.evaluate(ec, thisContext, accessor, BooleanLangObject.create(accessedViaDot));
	}

	protected final static <T extends ALangObject> void executeAttrAssigner(@Nonnull final T thisContext,
			@Nullable final IFunction<T> function, @Nonnull final ALangObject accessor, final boolean accessedViaDot,
			@Nonnull final ALangObject value, @Nonnull final IEvaluationContext ec)
					throws NoSuchAttrAccessorException, EvaluationException {
		if (function == null)
			throw new NoSuchAttrAssignerException(NullUtil.toString(accessor), thisContext, ec);
		function.evaluate(ec, thisContext, accessor, BooleanLangObject.create(accessedViaDot), value);
	}

	@Nullable
	public abstract IFunction<? extends ALangObject> attrAccessor(@Nonnull final ALangObject object,
			final boolean accessedViaDot, @Nonnull final IEvaluationContext ec) throws EvaluationException;

	@Nullable
	public abstract IFunction<? extends ALangObject> attrAssigner(@Nonnull final ALangObject object,
			final boolean accessedViaDot, @Nonnull final IEvaluationContext ec) throws EvaluationException;

	@Nullable
	public abstract IFunction<? extends ALangObject> expressionMethod(@Nonnull final EMethod method,
			@Nonnull IEvaluationContext ec) throws EvaluationException;

	@Nonnull
	public abstract ALangObject evaluateAttrAccessor(@Nonnull final ALangObject object, final boolean accessedViaDot,
			@Nonnull final IEvaluationContext ec) throws EvaluationException;

	public abstract void executeAttrAssigner(@Nonnull final ALangObject object, final boolean accessedViaDot,
			@Nonnull final ALangObject value, @Nonnull final IEvaluationContext ec) throws EvaluationException;

	@Nonnull
	public abstract ALangObject evaluateExpressionMethod(final @Nonnull EMethod method,
			@Nonnull final IEvaluationContext ec, @Nonnull final ALangObject... args) throws EvaluationException;

	@Nonnull
	public NonNullIterable<ALangObject> getIterable(@Nonnull final IEvaluationContext ec) throws IterationNotSupportedException {
		throw new IterationNotSupportedException(this, ec);
	}

	/** @deprecated Use {@link #getIterable(IEvaluationContext)}. */
	@Override
	@Nonnull
	@Deprecated
	public NonNullIterator<ALangObject> iterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(CmnCnst.Error.DEPRECATED_ALANGOBJECT_ITERATOR);
	}

	@Override
	public int hashCode() {
		return (int) (id ^ id >>> 32);
	}

	@Override
	public abstract boolean equals(final Object o);

	/**
	 * <pre>The natural ordering for a class C is said to be consistent with equals if and only
	 * if e1.compareTo(e2) == 0 has the same boolean value as e1.equals(e2) for every e1
	 * and e2 of class C. Note that null is not an instance of any class, and e.compareTo(null)
	 * should throw a NullPointerException even though e.equals(null) returns false.</pre>
	 *
	 * <p>This holds true. When the comparand is null <code>o.type</code> throws a
	 * NullPointerException. When the two objects are of different type, they are ordered
	 * according to {@link Type#order} and cannot be equal. When the two objects are
	 * of the same type, the abstract method {@link ALangObject#compareToSameType(ALangObject)}
	 * is called. Subclasses are required to adhere to the above contract.
	 * </p>
	 *
	 */
	@Override
	public final int compareTo(final ALangObject o) {
		if (type != o.type) return type.order - o.type.order;
		return compareToSameType(o);
	}

	public final int compareById(@Nonnull final ALangObject o) {
		return Long.compare(id,  o.id);
	}

	public boolean equalsSameObject(@Nonnull final ALangObject o) {
		return isSingletonLike() ? equals(o) : id == o.id;
	}

	protected abstract boolean isSingletonLike();

	/**
	 * Needs to adhere to the contract of {@link Comparable}.
	 *
	 * @param o
	 * @return Guaranteed to be be non-<code>null</code>. An object
	 *            of the same type as the subclass. It may be cast safely.
	 */
	protected abstract int compareToSameType(@Nonnull ALangObject o);

	/**
	 * May be overridden for specific objects.
	 *
	 * @return Details on the object, such as its class and its fields.
	 */
	@Nonnull
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_A_LANG_OBJECT).append(id));
	}

	@Nonnull
	public static ALangObject create(@Nullable final ALangObject value) {
		if (value == null)
			return NullLangObject.getInstance();
		return value;
	}

	public final Type getType() {
		return type;
	}
	public final boolean isArray() {
		return type == Type.ARRAY;
	}
	public final boolean isNumber() {
		return type == Type.NUMBER;
	}
	public final boolean isString() {
		return type == Type.STRING;
	}
	public final boolean isHash() {
		return type == Type.HASH;
	}
	public final boolean isBoolean() {
		return type == Type.BOOLEAN;
	}
	public final boolean isFunction() {
		return type == Type.FUNCTION;
	}
	public final boolean isException() {
		return type == Type.EXCEPTION;
	}
	public final boolean isRegex() {
		return type == Type.REGEX;
	}
	public final boolean isNull() {
		return type == Type.NULL;
	}
}
