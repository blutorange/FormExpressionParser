package de.xima.fc.form.expression.util;

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

	@Nonnull
	public static String format(@Nonnull final String format, @Nonnull final Object... params) {
		return checkNotNull(String.format(format, params));
	}

	@Nonnull
	public static String orEmpty(@Nullable final String string) {
		return string != null ? string : CmnCnst.NonnullConstant.STRING_EMPTY;
	}

	@Nonnull
	public static String or(@Nullable final String string, @Nonnull final String orString) {
		return string != null ? string : orString;
	}

	
	public static void main(final String[] args) {
		final char c = '3';
		System.out.println(toString(c));
	}
}
