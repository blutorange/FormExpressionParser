package de.xima.fc.form.expression.object;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.InvalidRegexPatternException;

public class RegexLangObject extends ALangObject {

	private final static class InstanceHolder {
		public final static RegexLangObject UNMATCHABLE = new RegexLangObject(Pattern.compile("(?!)"));
		public final static RegexLangObject ALL_MATCHING = new RegexLangObject(Pattern.compile(""));
	}

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

	/**
	 * Sorts regex first by their pattern, then by their flags.
	 * @param o Object to compare this object to.
	 * @return The result of the comparison.
	 */
	@Override
	public int compareToSameType(ALangObject o) {
		final Pattern v = ((RegexLangObject)o).value;
		final int res = value.pattern().compareTo(v.pattern());
		return res != 0 ? res : Integer.compare(value.flags(), v.flags());
	}
	
	@Override
	protected boolean isSingletonLike() {
		return false;
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
		if ((value.flags() & Pattern.MULTILINE) != 0)
			builder.append('m');
		if ((value.flags() & Pattern.DOTALL) != 0)
			builder.append('s');
	}

	public static void toExpression(final Pattern value, final Writer writer) throws IOException {
		writer.write('#');
		writer.write(value.pattern());
		writer.write('#');
		if ((value.flags() & Pattern.CASE_INSENSITIVE) != 0)
			writer.write('i');
		if ((value.flags() & Pattern.MULTILINE) != 0)
			writer.write('m');
		if ((value.flags() & Pattern.DOTALL) != 0)
			writer.write('s');
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

	/**
	 * @param string String to matches. When null, the empty string is used.
	 * @return A regex that matches the literal characters of the given string, at any position.
	 */
	public static RegexLangObject createForString(String string) {
		if (string == null) string = StringUtils.EMPTY;
		return new RegexLangObject(Pattern.compile(Pattern.quote(string)));
	}

	/**
	 * @param value The pattern for the regex.
	 * @param ec Current evaluation context.
	 * @return A regex with the given pattern.
	 * @throws InvalidRegexPatternException When the pattern is invalid.
	 */
	public static RegexLangObject create(final String value, final IEvaluationContext ec) throws InvalidRegexPatternException {
		return create(value, 0, ec);
	}

	public static RegexLangObject create(final String value, final int flags, final IEvaluationContext ec) throws InvalidRegexPatternException {
		Pattern pattern;
		try {
			pattern = Pattern.compile(value, flags);
		}
		catch (final IllegalArgumentException e) {
			throw new InvalidRegexPatternException(value, flags, ec);
		}
		return new RegexLangObject(pattern);
	}

	public static RegexLangObject getUnmatchableInstance() {
		return InstanceHolder.UNMATCHABLE;
	}
	public static RegexLangObject getAllMatchingInstance() {
		return InstanceHolder.ALL_MATCHING;
	}
}
