package de.xima.fc.form.expression.util;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NullUtil {
	private NullUtil() {}
	@Nonnull
	public static String toString(@Nullable final Object o) {
		return checkNotNull(String.valueOf(o));
	}

	@Nonnull
	public static <T> T checkNotNull(final T o) {
		if (o == null)
			throw new NullPointerException();
		return o;
	}

	public static void checkItemsNotNull(final Object[] objects) {
		for (final Object o : objects)
			if (o == null)
				throw new NullPointerException();
	}

	/**
	 * Same as {@link String#format(String, Object...)}, but never returns <code>null</code>.
	 * @param pattern Pattern for formatting.
	 * @param arguments Argument inserted into the pattern.
	 * @return The formatted string.
	 * @see String#format(String, Object...)
	 */
	@Nonnull
	public static String stringFormat(@Nonnull final String format, @Nonnull final Object... params) {
		return checkNotNull(String.format(format, params));
	}

	@Nonnull
	public static String orEmpty(@Nullable final String string) {
		return string != null ? string : CmnCnst.NonnullConstant.STRING_EMPTY;
	}

	@Nonnull
	public static <T> T or(@Nullable final T value, @Nonnull final T orValue) {
		return value != null ? value : orValue;
	}

	public static void main(final String[] args) {
		final char c = '3';
		System.out.println(toString(c));
	}

	/**
	 * Same as {@link MessageFormat#format(String, Object...)}, but never returns <code>null</code>.
	 * @param pattern Pattern for formatting.
	 * @param arguments Argument inserted into the pattern.
	 * @return The formatted string.
	 * @see MessageFormat#format(String, Object...)
	 */
	@Nonnull
	public static String messageFormat(@Nonnull final String pattern, @Nonnull final Object... arguments) {
		final String fmt = MessageFormat.format(pattern, arguments);
		if (fmt != null)
			return fmt;
		throw new NullPointerException();
	}
	
	@Nonnull
	public static <T> Iterator<T> iterator(final Iterable<T> iterable) {
		final Iterator<T> it = iterable.iterator();
		if (it != null)
			return it;
		throw new NullPointerException();
	}
}