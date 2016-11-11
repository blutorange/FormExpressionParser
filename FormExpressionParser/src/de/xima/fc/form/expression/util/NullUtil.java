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

	public static void main(final String[] args) {
		final char c = '3';
		System.out.println(toString(c));
	}
}
