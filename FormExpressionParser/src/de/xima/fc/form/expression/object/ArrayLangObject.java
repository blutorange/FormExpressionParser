package de.xima.fc.form.expression.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;

public class ArrayLangObject extends ALangObject {

	private final List<ALangObject> value;

	private ArrayLangObject(final List<ALangObject> value) {
		super(Type.ARRAY);
		this.value = value;
	}


	public static ALangObject create(final ALangObject... value) {
		if (value == null) return NullLangObject.getInstance();
		final List<ALangObject> list = new ArrayList<>(value.length);
		for (final ALangObject o : value) list.add(o);
		return new ArrayLangObject(list);
	}

	public static ALangObject create(final List<ALangObject> value) {
		if (value == null) return NullLangObject.getInstance();
		return new ArrayLangObject(value);
	}

	// Coercion
	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return BooleanLangObject.getTrueInstance();
	}
	@Override
	public ArrayLangObject coerceArray(final IEvaluationContext ec) throws CoercionException {
		return this;
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
	public IFunction<ArrayLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodArray(method);
	}

	@Override
	public IFunction<ArrayLangObject> attrAccessor(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorArray(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodArray(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorArray(object, accessedViaDot), object, ec);
	}

	@Override
	public String inspect() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ArrayLangObject[");
		for (final ALangObject o : value) sb.append(o.inspect()).append(",");
		if (sb.length() > 16) sb.setLength(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ArrayLangObject)) return false;
		final ArrayLangObject other = (ArrayLangObject)o;
		return value.equals(other.value);
	}


	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append("[");
		for (final ALangObject o : value) builder.append(o.toString()).append(",");
		if (builder.length() > 1) builder.setLength(builder.length()-1);
		builder.append("]");
	}

	@Override
	public Iterator<ALangObject> iterator() {
		return value.iterator();
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

	public ALangObject get(final int index) throws ArrayIndexOutOfBoundsException {
		return value.get(index);
	}

}
