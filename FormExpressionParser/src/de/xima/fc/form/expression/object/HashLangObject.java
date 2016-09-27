package de.xima.fc.form.expression.object;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;

public class HashLangObject extends ALangObject {

	private final Map<ALangObject, ALangObject> value;

	private HashLangObject(final Map<ALangObject, ALangObject> value) {
		super(Type.HASH);
		this.value = value;
	}

	// Coercion
	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return BooleanLangObject.getTrueInstance();
	}
	@Override
	public HashLangObject coerceHash(final IEvaluationContext ec) throws CoercionException {
		return this;
	}

	@Override
	public ALangObject shallowClone() {
		return HashLangObject.create(new HashMap<>(value));
	}

	@Override
	public ALangObject deepClone() {
		final Map<ALangObject, ALangObject> map = new HashMap<>(value.size());
		for (final Entry<ALangObject, ALangObject> v : value.entrySet())
			map.put(v.getKey().deepClone(), v.getValue().deepClone());
		return HashLangObject.create(map);
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append("{");
		for (final Entry<ALangObject, ALangObject> v : value.entrySet())
			builder.append(v.getKey().toString()).append(":").append(v.getValue().toString()).append(",");
		if (builder.length() > 1) builder.setLength(builder.length()-1);
		builder.append("}");
	}

	@Override
	public String inspect() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HashLangObject{");
		for (final Entry<ALangObject, ALangObject> v : value.entrySet())
			sb.append(v.getKey().inspect()).append(":").append(v.getValue().inspect()).append(",");
		if (sb.length() > 15) sb.setLength(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}


	@Override
	public IFunction<HashLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodHash(method);
	}
	@Override
	public IFunction<HashLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorHash(object, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodHash(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorHash(object, accessedViaDot), object, ec);
	}

	/**
	 * @param key The key for which to retrieve the mapped value.
	 * @return The value for the key, or null when it has not been set yet.
	 */
	public ALangObject get(final ALangObject key) {
		return ALangObject.create(value.get(key));
	}

	/**
	 * @param key Key to which an object should be mapped.
	 * @param val Object to be mapped.
	 * @return The previous value mapped to the key, or null otherwise.
	 */
	public ALangObject put(final ALangObject key, final ALangObject val) {
		return value.put(key, val);
	}

	/**
	 * @param key
	 * @return Whether this hash contains a mapping for the given key.
	 */
	public BooleanLangObject contains(final ALangObject key) {
		return BooleanLangObject.create(value.containsKey(key));
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof HashLangObject)) return false;
		final HashLangObject other = (HashLangObject)o;
		return value.equals(other.value);
	}

	@Override
	public Iterator<ALangObject> iterator() {
		return value.keySet().iterator();
	}


	public static ALangObject create(final Map<ALangObject, ALangObject> value) {
		if (value == null) return NullLangObject.getInstance();
		return new HashLangObject(value);
	}

	/**
	 * @param value An array with an even number of entries, each pair of two representing a key-value pair.
	 * @return The hash map.
	 */
	public static ALangObject create(final ALangObject... value) {
		if (value == null) return NullLangObject.getInstance();
		final int len = value.length - value.length%2;
		final Map<ALangObject, ALangObject> map = new HashMap<>(len/2);
		for (int i = 0 ; i != len; i += 2)
			map.put(value[i], value[i+1]);
		return new HashLangObject(map);
	}

	/**
	 * @param value A list with an even number of entries, each pair of two representing a key-value pair.
	 * @return The hash map.
	 */
	public static ALangObject create(final List<ALangObject> value) {
		if (value == null) return NullLangObject.getInstance();
		final int len = value.size() - value.size()%2;
		final Map<ALangObject, ALangObject> map = new HashMap<>(len/2);
		for (int i = 0 ; i != len; i += 2)
			map.put(value.get(i), value.get(i+1));
		return new HashLangObject(map);
	}

	public Map<ALangObject, ALangObject> hashValue() {
		return value;
	}
}
