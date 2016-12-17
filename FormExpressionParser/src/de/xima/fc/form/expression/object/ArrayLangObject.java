package de.xima.fc.form.expression.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

public class ArrayLangObject extends ALangObject {
	@Nonnull private final List<ALangObject> value;
	private ArrayLangObject(@Nonnull final List<ALangObject> value) {
		super();
		this.value = value;
	}

	@Override
	public ILangObjectClass getType() {
		return ELangObjectType.ARRAY;
	}

	@Nonnull
	public static ArrayLangObject create() {
		return new ArrayLangObject(new ArrayList<ALangObject>());
	}


	@Nonnull
	public static <T extends ALangObject> ArrayLangObject create(final IVariableProvider<T>[] value, final int startIndex) {
		final List<ALangObject> list = new ArrayList<>(value.length-startIndex+1);
		for (int i = startIndex; i < value.length; ++i)
			list.add(value[i].make());
		return new ArrayLangObject(list);
	}

	@Nonnull
	public static ArrayLangObject create(final Map<ALangObject, ALangObject> value) {
		final List<ALangObject> list = new ArrayList<>(2*value.size());
		for (final Entry<ALangObject, ALangObject> entry : value.entrySet()) {
			list.add(entry.getKey());
			list.add(entry.getValue());
		}
		return new ArrayLangObject(list);
	}

	@Nonnull
	public static ALangObject create(@Nonnull final ALangObject[] value, final int startIndex) {
		final List<ALangObject> list = new ArrayList<>(value.length-startIndex+1);
		for (int i = startIndex; i < value.length; ++i)
			list.add(value[i]);
		return new ArrayLangObject(list);
	}

	@Nonnull
	public static ALangObject create(final ALangObject value) {
		if (value == null) return NullLangObject.getInstance();
		final List<ALangObject> list = new ArrayList<>(1);
		list.add(value);
		return new ArrayLangObject(list);
	}

	@Nonnull
	public static ALangObject create(final ALangObject... value) {
		if (value == null) return NullLangObject.getInstance();
		final List<ALangObject> list = new ArrayList<>(value.length);
		for (final ALangObject o : value) list.add(o);
		return new ArrayLangObject(list);
	}

	@Nonnull
	public static ALangObject create(final List<ALangObject> value) {
		if (value == null) return NullLangObject.getInstance();
		return new ArrayLangObject(value);
	}

	// Coercion
	@Override
	public ArrayLangObject coerceArray(final IEvaluationContext ec) throws CoercionException {
		return this;
	}
	@Override
	public HashLangObject coerceHash(final IEvaluationContext ec) throws CoercionException {
		return HashLangObject.create(value);
	}

	@Override
	public ALangObject shallowClone() {
		final List<ALangObject> clone = new ArrayList<>(value.size());
		clone.addAll(value);
		return new ArrayLangObject(clone);
	}

	@Override
	public ALangObject deepClone() {
		final List<ALangObject> clone = new ArrayList<>(value.size());
		for (final ALangObject o : value) clone.add(o.deepClone());
		return new ArrayLangObject(clone);
	}

	@Override
	public String inspect() {
		final StringBuilder sb = new StringBuilder();
		sb.append(CmnCnst.ToString.INSPECT_ARRAY_LANG_OBJECT).append('[');
		for (final ALangObject o : value) sb.append(o.inspect()).append(',');
		if (sb.length() > 16) sb.setLength(sb.length()-1);
		sb.append(']');
		return NullUtil.toString(sb);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ArrayLangObject)) return false;
		return value.equals(((ArrayLangObject)o).value);
	}

	/**
	 * Sorts arrays like strings, ie. it compares each element starting at the
	 * first. When all elements at common indices are equals, the smaller array
	 * comes before the larger array.
	 * <ul>
	 * <li>[1,2,3] < [2,1]</li>
	 * <li>[1,2,3] < [1,2,3,4]</li>
	 * <li>[] < [null]</li>
	 * <li>[42] == [42]</li>
	 * </ul>
	 * @return Whether this object is less, equal, or greater than the given object.
	 */
	@Override
	public int compareToSameType(final ALangObject o) {
		final List<ALangObject> v = ((ArrayLangObject)o).value;
		final int len = Math.min(value.size(), v.size());
		for (int i = 0; i < len; ++i) {
			final int res = (value.get(i).compareTo(v.get(i)));
			if (res != 0) return res;
		}
		return Integer.compare(value.size(), v.size());
	}


	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(Syntax.BRACKET_OPEN);
		for (final ALangObject o : value) builder.append(o.toString()).append(Syntax.COMMA);
		if (builder.length() > 1) builder.setLength(builder.length()-1);
		builder.append(Syntax.BRACKET_CLOSE);
	}

	@Override
	public INonNullIterable<ALangObject> getIterable(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public INonNullIterator<ALangObject> iterator() {
		return new Itr();
	}

	public List<ALangObject> listValue() {
		return value;
	}

	/**
	 * @param other Object to be added to the end of this array.
	 */
	public void add(final ALangObject other) {
		value.add(other);
	}

	/**
	 * Removes all instances of the parameter from this array.
	 * @param other Object to be removed.
	 */
	public void remove(final ALangObject other) {
		value.removeAll(Collections.singleton(other));
	}

	public void addAll(final ArrayLangObject other) {
		value.addAll(other.value);
	}

	public void removeAll(final ArrayLangObject other) {
		value.removeAll(other.value);
	}

	/** @return The number of entries in this array. */
	public int length() {
		return value.size();
	}

	@Nonnull
	public ALangObject get(final int index) throws ArrayIndexOutOfBoundsException {
		return ALangObject.create(value.get(index));
	}

	/**
	 * @param index Index of the element to replace
	 * @param val Element to be stored at the specified position
	 * @return The element previously at the specified position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public ALangObject set(final int index, final ALangObject val) throws ArrayIndexOutOfBoundsException {
		return value.set(index, val);
	}

	public void setLength(final int len) {
		if (len == value.size()) return;
		if (len < 0) {
			value.clear();
			return;
		}
		if (len > value.size())
			for (int i = len - value.size(); i-->0;)
				value.add(NullLangObject.getInstance());
		else
			for (int i = value.size(); i-->len;)
				value.remove(i);
	}

	/** Sorts the array according to the natural ordering of {@link ALangObject */
	public void sort() {
		Collections.sort(value);
	}

	public void sort(final Comparator<ALangObject> comparator) {
		Collections.sort(value, comparator);
	}

	@SuppressWarnings("null")
	@Nonnull
	public ALangObject[] toArray() {
		return value.toArray(new ALangObject[value.size()]);
	}

	@Override
	protected boolean isSingletonLike() {
		return false;
	}

	private class Itr implements INonNullIterator<ALangObject> {
		private int cursor;       // index of next element to return
		private int lastRet = -1; // index of last element returned; -1 if no such
		@Override
		public boolean hasNext() {
			return cursor != value.size();
		}

		@Override
		public ALangObject next() {
			final int i = cursor;
			if (i >= value.size())
				throw new NoSuchElementException();
			cursor = i + 1;
			return ALangObject.create(value.get(lastRet = i));
		}

		@Override
		public void remove() {
			if (lastRet < 0)
				throw new FormExpressionException();
			try {
				value.remove(lastRet);
				cursor = lastRet;
				lastRet = -1;
			} catch (final IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
