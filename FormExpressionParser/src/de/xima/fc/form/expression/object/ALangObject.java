package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.error.CoercionException;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.error.NoSuchFunctionException;

public abstract class ALangObject {

	private final Type type;
	private final long id;
	private static long idCounter = 0L;

	public static enum Type {
		STRING(StringLangObject.class),
		NUMBER(NumberLangObject.class),
		ARRAY(ArrayLangObject.class),
		HASH(HashLangObject.class),
		NULL(NullLangObject.class),
		BOOLEAN(BooleanLangObject.class);

		public final Class<? extends ALangObject> clazz;

		private Type(final Class<? extends ALangObject> clazz) {
			this.clazz = clazz;
		}
	}

	public abstract ALangObject shallowClone();

	public abstract ALangObject deepClone();

	/**
	 * @return An expression that evaluates to this object. Eg. for a String
	 *         <code>"</code>, this would return <code>"\""</code>
	 */
	@Override
	public abstract String toString();

	/**
	 * @param ec
	 *            Current evaluation context.
	 * @return The coerced object. Must return <code>this</code> when this
	 *         object is already of the correct type.
	 * @throws CoercionException
	 *             When this object cannot be coerced to the given type.
	 */
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.STRING)
			return (StringLangObject) this;
		throw new CoercionException(this, Type.STRING, ec);
	}

	public ArrayLangObject coerceArray(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.ARRAY)
			return (ArrayLangObject) this;
		throw new CoercionException(this, Type.ARRAY, ec);
	}

	public HashLangObject coerceHash(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.HASH)
			return (HashLangObject) this;
		throw new CoercionException(this, Type.HASH, ec);
	}

	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.NUMBER)
			return (NumberLangObject) this;
		throw new CoercionException(this, Type.NUMBER, ec);
	}

	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.BOOLEAN)
			return (BooleanLangObject) this;
		throw new CoercionException(this, Type.BOOLEAN, ec);
	}

	/**
	 * Convenience method when the caller does not need the result of the
	 * correct class.
	 *
	 * @param type
	 *            Type to which this object should be coerced.
	 * @param ec
	 *            Current evaluation context.
	 * @return The coerced object. Can be cast to {@link Type#clazz} safely.
	 * @throws CoercionException
	 *             When this object cannot be coerced to the given type.
	 */
	public final ALangObject coerce(final Type type, final IEvaluationContext ec) throws CoercionException {
		if (type == getType())
			return this;
		switch (type) {
		case ARRAY:
			return coerceArray(ec);
		case HASH:
			return coerceHash(ec);
		case NULL:
			throw new CoercionException(this, Type.NULL, ec);
		case NUMBER:
			return coerceNumber(ec);
		case STRING:
			return coerceString(ec);
		case BOOLEAN:
			return coerceBoolean(ec);
		default:
			throw new RuntimeException("NOT_IMPLEMENTED: add case for enum " + type);
		}
	}

	/**
	 * @param type
	 *            Type to check against.
	 * @return Whether this object is of the given type.
	 */
	public boolean is(final Type type) {
		return this.type == type;
	}

	public ALangObject(final Type type) {
		this.type = type;
		id = ++idCounter;
	}

	public long getId() {
		return id;
	}

	protected final <T extends ALangObject> ALangObject evaluateMethod(final T thisContext,
			final INamedFunction<T> function, final String name, final IEvaluationContext ec, final ALangObject... args)
					throws EvaluationException {
		if (function == null)
			throw new NoSuchFunctionException(name, thisContext, ec);
		return function.evaluate(ec, thisContext, args);
	}

	public abstract ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException;

	public abstract ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec)
			throws EvaluationException;

	@Override
	public int hashCode() {
		return (int) (id ^ id >>> 32);
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof ALangObject))
			return false;
		return id == ((ALangObject) o).id;
	}

	/**
	 * May be overridden for specific objects.
	 *
	 * @return Details on the object, such as its class and its fields.
	 */
	public String inspect() {
		return "ALangObject@" + id;
	}

	public static ALangObject create(final ALangObject value) {
		if (value == null)
			return NullLangObject.getInstance();
		return value;
	}

	public final Type getType() {
		return type;
	}
}
