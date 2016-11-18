package de.xima.fc.form.expression.object;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

public class StringLangObject extends ALangObject {
	@Nonnull private final String value;

	private static class InstanceHolder {
		@Nonnull public final static StringLangObject EMPTY = StringLangObject.create(StringUtils.EMPTY);
		@Nonnull public final static StringLangObject SPACE = StringLangObject.create(StringUtils.SPACE);
		@Nonnull public final static StringLangObject LF = StringLangObject.create(StringUtils.LF);
		@Nonnull public final static StringLangObject CR = StringLangObject.create(StringUtils.CR);
		@Nonnull public final static StringLangObject TRUE = StringLangObject.create(Syntax.TRUE);
		@Nonnull public final static StringLangObject FALSE = StringLangObject.create(Syntax.FALSE);
		@Nonnull public final static StringLangObject NULL = StringLangObject.create(Syntax.NULL);
	}

	private StringLangObject(@Nonnull final String value) {
		super(Type.STRING);
		this.value = value;
	}

	@Nonnull
	public String stringValue() {
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
	public IFunction<StringLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodString(method);
	}
	@Override
	public IFunction<StringLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorString(object, accessedViaDot);
	}

	@Override
	public IFunction<StringLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerString(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodString(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorString(object, accessedViaDot), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerString(object, accessedViaDot), object, accessedViaDot, value, ec);
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
		if (!(o instanceof StringLangObject)) return false;
		final StringLangObject other = (StringLangObject)o;
		return value.equals(other.value);
	}

	@Override
	public int compareToSameType(final ALangObject o) {
		return value.compareTo(((StringLangObject)o).value);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_STRING_LANG_OBJECT).append('(')
				.append(value).append(')'));
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		StringLangObject.toExpression(value, builder);
	}

	public static void toExpression(final String value, final StringBuilder builder) {
		builder.append(Syntax.QUOTE).append(StringEscapeUtils.escapeJava(value)).append(Syntax.QUOTE);
	}

	public static void toExpression(final String value, final Writer writer) throws IOException {
		writer.write('"');
		writer.write(StringEscapeUtils.escapeJava(value));
		writer.write('"');
	}

	// Coercion
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return NumberLangObject.create(value);
	}

	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return this;
	}

	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		return RegexLangObject.createForString(value);
	}

	@Override
	public NonNullIterable<ALangObject> getIterable(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public NonNullIterator<ALangObject> iterator() {
		return new Itr();
	}

	@Nonnull
	public ALangObject concat(final StringLangObject other) {
		return StringLangObject.create(value.concat(other.value));
	}

	@Nonnull
	public ALangObject toUpperCase(final Locale locale) {
		return StringLangObject.create(value.toUpperCase(locale));
	}

	@Nonnull
	public ALangObject toLowerCase(final Locale locale) {
		return StringLangObject.create(value.toLowerCase(locale));
	}

	/**
	 * Factory method for creating instances.
	 * @param value String, the data.
	 * @return {@link ALangObject} representing the parameter string best, may not be an instance of {@link StringLangObject}.
	 */
	@Nonnull
	public static StringLangObject create(final String value) {
		if (value == null) return StringLangObject.getEmptyInstance();
		return new StringLangObject(value);
	}

	@Nonnull
	public static StringLangObject create(final char value) {
		final String s = String.valueOf(value);
		return new StringLangObject(s == null ? CmnCnst.NonnullConstant.EMPTY_STRING : s);
	}

	@Nonnull
	public static StringLangObject createFromEscapedJava(final String value) {
		if (value == null) return StringLangObject.create(StringUtils.EMPTY);
		return StringLangObject.create(StringEscapeUtils.unescapeJava(value));
	}

	@Nonnull
	public static StringLangObject createFromEscapedEcmaScript(final String value) {
		if (value == null) return StringLangObject.create(StringUtils.EMPTY);
		return StringLangObject.create(StringEscapeUtils.unescapeEcmaScript(value));
	}

	@Nonnull
	public static StringLangObject create(final BigDecimal value) {
		return StringLangObject.create(value.toPlainString());
	}

	@Nonnull
	public static StringLangObject create(final float value) {
		return StringLangObject.create(NumberLangObject.NUMBER_FORMAT.get().format(value));
	}

	@Nonnull
	public static StringLangObject create(final double value) {
		return StringLangObject.create(NumberLangObject.NUMBER_FORMAT.get().format(value));
	}

	@Nonnull
	public static StringLangObject create(final boolean value) {
		return StringLangObject.create(value ? Syntax.TRUE : Syntax.FALSE);
	}

	@Nonnull
	public static StringLangObject getNullInstance() {
		return InstanceHolder.NULL;
	}

	@Nonnull
	public static StringLangObject getEmptyInstance() {
		return InstanceHolder.EMPTY;
	}

	@Nonnull
	public static StringLangObject getSpaceInstance() {
		return InstanceHolder.SPACE;
	}

	@Nonnull
	public static StringLangObject getLineFeedInstance() {
		return InstanceHolder.LF;
	}

	@Nonnull
	public static StringLangObject getCarriageReturnInstance() {
		return InstanceHolder.CR;
	}

	@Nonnull
	public static StringLangObject getTrueInstance() {
		return InstanceHolder.TRUE;
	}

	@Nonnull
	public static StringLangObject getFalseInstance() {
		return InstanceHolder.FALSE;
	}

	public int length() {
		return value.length();
	}

	private class Itr implements NonNullIterator<ALangObject> {
		private int i = 0;
		@Override
		public boolean hasNext() {
			return i < value.length();
		}
		@Override
		public ALangObject next() {
			final ALangObject res = StringLangObject.create(value.charAt(i));
			++i;
			return res;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException(CmnCnst.Error.STRING_ITERATOR_DOES_NOT_SUPPORT_REMOVAL);
		}
	}
}
