package de.xima.fc.form.expression.object;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.InvalidRegexPatternException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class RegexLangObject extends ALangObject {
	private final static class InstanceHolder {
		@SuppressWarnings("null")
		@Nonnull public final static RegexLangObject UNMATCHABLE = new RegexLangObject(Pattern.compile("(?!)")); //$NON-NLS-1$
		@SuppressWarnings("null")
		@Nonnull public final static RegexLangObject ALL_MATCHING = new RegexLangObject(Pattern.compile("")); //$NON-NLS-1$
	}

	@Nonnull private final Pattern value;

	private RegexLangObject(@Nonnull final Pattern value) {
		super();
		this.value = value;
	}

	@Override
	public ILangObjectClass getObjectClass() {
		return ELangObjectType.REGEX;
	}

	@Nonnull
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
	public int hashCode() {
		return 37 * value.pattern().hashCode() + value.flags();
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
	public int compareToSameType(final ALangObject o) {
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
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_REGEX_LANG_OBJECT).append('(')
				.append(value).append(')'));
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
	@Nonnull
	public static RegexLangObject create(@Nullable final Pattern value) {
		if (value == null) return getAllMatchingInstance();
		return new RegexLangObject(value);
	}

	/**
	 * @param string String to matches. When null, the empty string is used.
	 * @return A regex that matches the literal characters of the given string, at any position.
	 */
	@Nonnull
	public static RegexLangObject createForString(@Nullable String string) {
		if (string == null) string = StringUtils.EMPTY;
		final Pattern p = Pattern.compile(Pattern.quote(string));
		return p != null ? new RegexLangObject(p) : getAllMatchingInstance();
	}

	/**
	 * @param value The pattern for the regex.
	 * @param ec Current evaluation context.
	 * @return A regex with the given pattern.
	 * @throws InvalidRegexPatternException When the pattern is invalid.
	 */
	@Nonnull
	public static RegexLangObject create(@Nonnull final String value, @Nonnull final IEvaluationContext ec)
			throws InvalidRegexPatternException {
		return create(value, 0, ec);
	}

	@Nonnull
	public static RegexLangObject create(@Nonnull final String value, final int flags,
			@Nonnull final IEvaluationContext ec) throws InvalidRegexPatternException {
		Pattern pattern;
		try {
			pattern = Pattern.compile(value, flags);
		}
		catch (final IllegalArgumentException e) {
			throw new InvalidRegexPatternException(value, flags, ec);
		}
		if (pattern == null)
			throw new InvalidRegexPatternException(value, flags, ec);
		return new RegexLangObject(pattern);
	}

	@Nonnull
	public static RegexLangObject getUnmatchableInstance() {
		return InstanceHolder.UNMATCHABLE;
	}
	@Nonnull
	public static RegexLangObject getAllMatchingInstance() {
		return InstanceHolder.ALL_MATCHING;
	}
}
