package de.xima.fc.form.expression.object;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
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
		return value.size() == 0 ? BooleanLangObject.getFalseInstance()
				: BooleanLangObject.getTrueInstance();
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
	public INamedFunction<ArrayLangObject> instanceMethod(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().instanceMethodArray(name);
	}
	@Override
	public INamedFunction<ArrayLangObject> attrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorArray(name);
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return this.evaluateMethod(this, ec.getNamespace().instanceMethodArray(name), name, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorArray(name), name, ec);
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


	public List<ALangObject> listValue() {
		return value;
	}

}
