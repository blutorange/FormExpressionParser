package de.xima.fc.form.expression.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.ComparatorEntryByKey;
import de.xima.fc.form.expression.util.NullUtil;

public class HashLangObject extends ALangObject {

	@Nonnull
	private final Map<ALangObject, ALangObject> value;

	private HashLangObject(@Nonnull final Map<ALangObject, ALangObject> value) {
		super(Type.HASH);
		this.value = value;
	}

	// Coercion
	@Override
	public HashLangObject coerceHash(final IEvaluationContext ec) throws CoercionException {
		return this;
	}
	@Override
	public ArrayLangObject coerceArray(final IEvaluationContext ec) throws CoercionException {
		return ArrayLangObject.create(value);
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
		builder.append(Syntax.BRACE_OPEN);
		for (final Entry<ALangObject, ALangObject> v : value.entrySet())
			builder.append(v.getKey().toString()).append(Syntax.COLON).append(v.getValue().toString()).append(Syntax.COMMA);
		if (builder.length() > 1) builder.setLength(builder.length()-1);
		builder.append(Syntax.BRACE_CLOSE);
	}

	@Override
	public String inspect() {
		final StringBuilder sb = new StringBuilder();
		sb.append(CmnCnst.ToString.INSPECT_HASH_LANG_OBJECT).append('{');
		for (final Entry<ALangObject, ALangObject> v : value.entrySet())
			sb.append(v.getKey().inspect()).append(':').append(v.getValue().inspect()).append(',');
		if (sb.length() > 15) sb.setLength(sb.length()-1);
		sb.append('}');
		return NullUtil.toString(sb);
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
	public IFunction<HashLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerHash(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodHash(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorHash(object, accessedViaDot), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerHash(object, accessedViaDot), object, accessedViaDot, value, ec);
	}

	/**
	 * @param key The key for which to retrieve the mapped value.
	 * @return The value for the key, or null when it has not been set yet.
	 */
	@Nonnull
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
	protected boolean isSingletonLike() {
		return false;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof HashLangObject)) return false;
		final HashLangObject other = (HashLangObject)o;
		return value.equals(other.value);
	}

	@Override
	public int compareToSameType(final ALangObject o) {
		// Retrieve keys.
		final Map<ALangObject, ALangObject> v = ((HashLangObject)o).value;
		final List<Entry<ALangObject,ALangObject>> s1 = new ArrayList<>(value.entrySet());
		final List<Entry<ALangObject,ALangObject>> s2 = new ArrayList<>(v.entrySet());
		Collections.sort(s1, ComparatorEntryByKey.INSTANCE);
		Collections.sort(s2, ComparatorEntryByKey.INSTANCE);
		// Compare by keys.
		final int len = Math.min(s1.size(), s2.size());
		int res2 = 0;
		for (int i = 0; i < len; ++i) {
			final int res = (s1.get(i).getKey().compareTo(s2.get(i).getKey()));
			if (res != 0) return res;
			if (res2 == 0) res2 = (s1.get(i).getValue().compareTo(s2.get(i).getValue()));
		}
		if (s1.size() != s2.size()) return Integer.compare(value.size(), v.size());
		return res2;
	}

	@Override
	public NonNullIterable<ALangObject> getIterable(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public NonNullIterator<ALangObject> iterator() {
		return new Itr(value.keySet().iterator());
	}

	@Nonnull
	public static ALangObject create(final Map<ALangObject, ALangObject> value) {
		if (value == null) return NullLangObject.getInstance();
		return new HashLangObject(value);
	}

	/** @return An empty hash. */
	@Nonnull
	public static HashLangObject create() {
		return new HashLangObject(new HashMap<ALangObject, ALangObject>());
	}

	/**
	 * @param value An array with an even number of entries, each pair of two representing a key-value pair.
	 * @return The hash map.
	 */
	@Nonnull
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
	@Nonnull
	public static HashLangObject create(final List<ALangObject> value) {
		if (value == null)
			return new HashLangObject(new HashMap<ALangObject, ALangObject>());
		final int len = value.size() - value.size()%2;
		final Map<ALangObject, ALangObject> map = new HashMap<>(len/2);
		for (int i = 0 ; i != len; i += 2)
			map.put(value.get(i), value.get(i+1));
		return new HashLangObject(map);
	}

	@Nonnull
	public Map<ALangObject, ALangObject> hashValue() {
		return value;
	}

	public int length() {
		return value.size();
	}

	private class Itr implements NonNullIterator<ALangObject> {
		private final Iterator<ALangObject> it;
		public Itr(final Iterator<ALangObject> it) {
			this.it = it;
		}
		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public ALangObject next() {
			return ALangObject.create(it.next());
		}

		@Override
		public void remove() {
			it.remove();
		}
	}

}
