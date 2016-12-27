package de.xima.fc.form.expression.object;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
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
		super();
		this.value = value;
	}

	@Nonnull
	public String stringValue() {
		return value;
	}

	@Override
	public ILangObjectClass getObjectClass() {
		return ELangObjectClass.STRING;
	}

	@Override
	public ALangObject shallowClone() {
		return this;
	}

	@Override
	public ALangObject deepClone() {
		return this;
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
		builder.append(Syntax.QUOTE_DOUBLE);
		escape(value, '"', builder);
		builder.append(Syntax.QUOTE_DOUBLE);
	}

	public static void toExpression(final String value, final Writer writer) throws IOException {
		writer.write(Syntax.QUOTE_DOUBLE);
		writer.write(escape(value, '"'));
		writer.write(Syntax.QUOTE_DOUBLE);
	}

	// Coercion
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return NumberLangObject.create(value);
	}

	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		return RegexLangObject.createForString(value);
	}

	@Override
	public INonNullIterable<ALangObject> getIterable(final IEvaluationContext ec) {
		return this;
	}

	@Override
	public INonNullIterator<ALangObject> iterator() {
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
	public static StringLangObject create(final Object object) {
		if (object == null) return StringLangObject.getEmptyInstance();
		return new StringLangObject(NullUtil.toString(object));
	}

	@Nonnull
	public static StringLangObject create(final char value) {
		final String s = String.valueOf(value);
		return new StringLangObject(s == null ? CmnCnst.NonnullConstant.STRING_EMPTY : s);
	}

	@Nonnull
	public static StringLangObject create(final BigDecimal value) {
		return StringLangObject.create(value.toPlainString());
	}

	@Nonnull
	public static StringLangObject create(final Number value) {
		return StringLangObject.create(NumberLangObject.NUMBER_FORMAT.get().format(value.doubleValue()));
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

	private class Itr implements INonNullIterator<ALangObject> {
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

	@SuppressWarnings("null")
	@Nonnull
	public static String escape(final String value, final char delimiter) {
		final StringBuilder sb = new StringBuilder(value.length());
		escape(value, delimiter, sb);
		return sb.toString();
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String unescape(final CharSequence value, final char delimiter) throws IllegalArgumentException {
		final StringBuilder sb = new StringBuilder(2*value.length());
		unescape(value, delimiter, sb);
		return sb.toString();
	}

	public static void escape(final String value, final char delimiter, final StringBuilder sb) {
		final int len = value.length();
		char c;
		for (int i = 0; i< len; ++i) {
			switch (c = value.charAt(i)) {
			case '\\':
				sb.append('\\').append('\\');
				break;
			case '$':
				// template literal
				if (delimiter == '`')
					sb.append('\\');
				sb.append('$');
				break;
			default:
				if (c < 32 | c >= 127) {
					sb.append('\\').append('u');
					sb.append(escapeHex(sb, (c & 0b1111000000000000) >> 12));
					sb.append(escapeHex(sb, (c & 0b0000111100000000) >> 8));
					sb.append(escapeHex(sb, (c & 0b0000000011110000) >> 4));
					sb.append(escapeHex(sb, c & 0b0000000000001111));
				}
				else if (c == delimiter)
					sb.append('\\').append(c);
				else
					sb.append(c);
			}
		}		
	}

	public static void unescape(final CharSequence value, final char delimiter, final StringBuilder sb) throws IllegalArgumentException {
		final int len = value.length();
		final char[] buffer = new char[2];
		int i = 0;
		char c, c2;
		while (i< len) {
			switch (c = value.charAt(i)) {
			case '\\':
				if (i >= len - 1)
					throw new IllegalArgumentException(NullUtil.messageFormat(CmnCnst.Error.STRING_ENDS_ON_BACKSLASH));
				switch (c2 = value.charAt(++i)) {
				case 'u':
				case 'U': {
					if (i+4>=len)
						throw new IllegalArgumentException(NullUtil.messageFormat(CmnCnst.Error.STRING_UNFINISHED_UNICODE_ESCAPE));
					final int hex = (unescapeHex(value.charAt(++i))<<12)|(unescapeHex(value.charAt(++i))<<8)|(unescapeHex(value.charAt(++i))<<4)|unescapeHex(value.charAt(++i));
					sb.append((char)hex);
					break;
				}
				case 's':
				case 'S': {
					if (i+8>=len)
						throw new IllegalArgumentException(NullUtil.messageFormat(CmnCnst.Error.STRING_UNFINISHED_UNICODE_ESCAPE));
					final int hex = (unescapeHex(value.charAt(++i)) << 12) | (unescapeHex(value.charAt(++i)) << 24)
							| (unescapeHex(value.charAt(++i)) << 20) | (unescapeHex(value.charAt(++i)) << 16)
							| (unescapeHex(value.charAt(++i)) << 12) | (unescapeHex(value.charAt(++i)) << 8)
							| (unescapeHex(value.charAt(++i)) << 4) | unescapeHex(value.charAt(++i));
					Character.toChars(hex, buffer, 0);
					sb.append(buffer[0]);
					if (buffer.length>1)
							sb.append(buffer[1]);
					break;
				}
				default:
					sb.append(c2);
				}
				break;
			case '$':
			if (delimiter == '`')
				throw new IllegalArgumentException(
						NullUtil.messageFormat(CmnCnst.Error.TEMPLATE_LITERAL_CONTAINS_DOLLAR, i));
				sb.append('$');
				break;
			default:
				if (c == delimiter)
					throw new IllegalArgumentException(
							NullUtil.messageFormat(CmnCnst.Error.STRING_CONTAINS_DELIMITER, delimiter, i));
				sb.append(c);
			}
			++i;
		}
	}

	private static char escapeHex(final StringBuilder sb, final int i) {
		if (i<10)
			return (char)('0'+i);
		return (char)('a'+i-10);
	}

	private static int unescapeHex(final char charAt) {
		if (charAt >= '0' && charAt <= '9')
			return charAt - '0';
		if (charAt >= 'A' && charAt <= 'F')
			return charAt - 'A' + 10;
		if (charAt >= 'a' && charAt <= 'f')
			return charAt - 'a' + 10;
		throw new IllegalArgumentException(NullUtil.messageFormat(CmnCnst.Error.STRING_INVALID_UNICODE_HEX, charAt));
	}
}