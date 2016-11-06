package de.xima.fc.form.expression.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NullUtil {
	private NullUtil() {}
	@Nonnull
	public static String toString(@Nullable final Object o) {
		final String s = String.valueOf(o);
		return s != null ? s : CmnCnst.EMPTY_STRING;
	}

	public static void main(final String[] args) {
		final char c = '3';
		System.out.println(toString(c));
	}
}
