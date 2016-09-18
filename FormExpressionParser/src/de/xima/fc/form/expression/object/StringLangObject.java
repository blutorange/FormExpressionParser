package de.xima.fc.form.expression.object;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;

public class StringLangObject extends ALangObject {
	private final static String TRUE = "true";
	private final static String FALSE = "false";

	private final String value;

	private StringLangObject(final String value) {
		super(Type.STRING);
		this.value = value;
	}

	public String stringValue() {
		return value;
	}

	@Override
	public ALangObject shallowClone() {
		return new StringLangObject(value);
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public INamedFunction<StringLangObject> instanceMethod(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().instanceMethodString(name);
	}
	@Override
	public INamedFunction<StringLangObject> attrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorString(name);
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return this.evaluateMethod(this, ec.getNamespace().instanceMethodString(name), name, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorString(name), name, ec);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof StringLangObject)) return false;
		final StringLangObject other = (StringLangObject)o;
		return value.equals(other.value);
	}

	@Override
	public String inspect() {
		return "StringLangObject(" + value + ")";
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append('"').append(StringEscapeUtils.escapeJava(value)).append('"').toString();
	}

	// Coercion
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		try {
			return NumberLangObject.create(new BigDecimal(value, NumberLangObject.MATH_CONTEXT));
		}
		catch (final NumberFormatException e) {
			return NumberLangObject.create(0);
		}
	}
	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return value.length() == 0 ? BooleanLangObject.getFalseInstance() : BooleanLangObject.getTrueInstance();
	}


	/**
	 * Factory method for creating instances.
	 * @param value String, the data.
	 * @return {@link ALangObject} representing the parameter string best, may not be an instance of {@link StringLangObject}.
	 */
	public static StringLangObject create(final String value) {
		if (value == null) return StringLangObject.create(StringUtils.EMPTY);
		return new StringLangObject(value);
	}

	public static StringLangObject createFromEscapedJava(final String value) {
		if (value == null) return StringLangObject.create(StringUtils.EMPTY);
		return StringLangObject.create(StringEscapeUtils.unescapeJava(value));
	}

	public static StringLangObject createFromEscapedEcmaScript(final String value) {
		if (value == null) return StringLangObject.create(StringUtils.EMPTY);
		return StringLangObject.create(StringEscapeUtils.unescapeEcmaScript(value));
	}

	public static StringLangObject create(final BigDecimal value) {
		return StringLangObject.create(value.toPlainString());
	}

	public static StringLangObject create(final boolean value) {
		return StringLangObject.create(value ? TRUE : FALSE);
	}

	public static ALangObject best(final String value) {
		if (value == null) return NullLangObject.getInstance();
		return new StringLangObject(value);
	}
}
