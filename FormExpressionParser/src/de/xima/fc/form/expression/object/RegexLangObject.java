package de.xima.fc.form.expression.object;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.InvalidRegexPatternException;

public class RegexLangObject extends ALangObject {
	
	private final Pattern value;

	private RegexLangObject(final Pattern value) {
		super(Type.REGEX);
		this.value = value;
	}

	public Pattern patternValue() {
		return value;
	}

	@Override
	public ALangObject shallowClone() {
		return this;
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public IFunction<RegexLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodRegex(method);
	}
	@Override
	public IFunction<RegexLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorRegex(object, accessedViaDot);
	}

	@Override
	public IFunction<RegexLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerRegex(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodRegex(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorRegex(object, accessedViaDot), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerRegex(object, accessedViaDot), object, accessedViaDot, value, ec);
	}

	@Override
	public int hashCode() {
		return 37*value.pattern().hashCode() + value.flags();
	}

	/**
	 * @param The object to compare this object to.
	 * @return True iff the object is a {@link RegexLangObject} and both its pattern and the flags are the same. Note that this does not test whether two regex's would match the same strings.
	 */
	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof RegexLangObject)) return false;
		final RegexLangObject other = (RegexLangObject)o;
		return value.flags() == other.value.flags() && value.pattern().equals(other.value.pattern());
	}

	@Override
	public String inspect() {
		return "RegexLangObject(" + value + ")";
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		RegexLangObject.toExpression(value, builder);
	}

	public static void toExpression(final Pattern value, final StringBuilder builder) {
		builder.append('#').append(value.pattern()).append('#');
		if ((value.flags() & Pattern.CASE_INSENSITIVE) != 0)
			builder.append('i');
		if ((value.flags() & Pattern.DOTALL) != 0)
			builder.append('s');
		if ((value.flags() & Pattern.MULTILINE) != 0)
			builder.append('m');
	}

	// Coercion
	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		return this;
	}

	/**
	 * @param value Pattern to compile.
	 * @return {@link ALangObject} representing the parameter string best, may not be an instance of {@link RegexLangObject}.
	 */
	public static RegexLangObject create(final Pattern value) {
		if (value == null) return new RegexLangObject(Pattern.compile(StringUtils.EMPTY));
		return new RegexLangObject(value);
	}

	public static RegexLangObject create(String value, IEvaluationContext ec) throws InvalidRegexPatternException {
		return create(value, 0, ec);
	}
	
	public static RegexLangObject create(String value, int flags, IEvaluationContext ec) throws InvalidRegexPatternException {
		Pattern pattern;
		try {
			pattern = Pattern.compile(value, flags);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidRegexPatternException(value, flags, ec);
		}
		return new RegexLangObject(pattern);
	}
}
