package de.xima.fc.form.expression.object;

import java.util.Iterator;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IterationNotSupportedException;
import de.xima.fc.form.expression.exception.NoSuchAttrAccessorException;
import de.xima.fc.form.expression.exception.NoSuchMethodException;

public abstract class ALangObject implements Iterable<ALangObject> {
	private final static Logger LOG = Logger.getLogger(ALangObject.class.getCanonicalName());

	private final Type type;
	private final long id;
	private static long idCounter = 0L;

	public static enum Type {
		STRING(StringLangObject.class),
		NUMBER(NumberLangObject.class),
		ARRAY(ArrayLangObject.class),
		HASH(HashLangObject.class),
		NULL(NullLangObject.class),
		BOOLEAN(BooleanLangObject.class),
		FUNCTION(FunctionLangObject.class),
		EXCEPTION(ExceptionLangObject.class);

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
	public abstract void toExpression(StringBuilder builder);

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

	public ExceptionLangObject coerceException(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.EXCEPTION)
			return (ExceptionLangObject) this;
		throw new CoercionException(this, Type.EXCEPTION, ec);
	}

	public FunctionLangObject coerceFunction(final IEvaluationContext ec) throws CoercionException {
		if (getType() == Type.FUNCTION)
			return (FunctionLangObject) this;
		throw new CoercionException(this, Type.FUNCTION, ec);
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
	@SuppressWarnings("unchecked")
	public final <T extends ALangObject> T coerce(final Type type, final Class<T> clazz, final IEvaluationContext ec) throws CoercionException, EvaluationException {
		if (clazz != type.clazz) throw new EvaluationException(ec, "Argument type and return class do not match");
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
		default:
			// Try to coerce object with the special coerce method, when defined.
			LOG.info("Enum might not be implemented: " + type);
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

	@NotNull
	protected final <T extends ALangObject> ALangObject evaluateExpressionMethod(final T thisContext,
			final IFunction<T> function, final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws NoSuchMethodException, EvaluationException {
		if (function == null)
			throw new NoSuchMethodException(method.name, thisContext, ec);
		return ALangObject.create(function.evaluate(ec, thisContext, args));
	}

	@NotNull
	protected final <T extends ALangObject> ALangObject evaluateAttrAccessor(final T thisContext,
			final IFunction<T> function, final ALangObject accessor, final IEvaluationContext ec)
					throws NoSuchAttrAccessorException, EvaluationException {
		if (function == null)
			throw new NoSuchAttrAccessorException(accessor.toString(), thisContext, ec);
		return ALangObject.create(function.evaluate(ec, thisContext, accessor));
	}

	public abstract IFunction<? extends ALangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec)
			throws EvaluationException;

	public abstract IFunction<? extends ALangObject> expressionMethod(final EMethod method, IEvaluationContext ec)
			throws EvaluationException;

	public abstract ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec)
			throws EvaluationException;

	public abstract ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException;

	@Override
	@NotNull
	public Iterator<ALangObject> iterator() throws EvaluationException {
		throw new IterationNotSupportedException(this);
	}



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
