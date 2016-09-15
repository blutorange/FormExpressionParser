package de.xima.fc.form.expression.util;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.error.IllegalArgumentTypeException;
import de.xima.fc.form.expression.error.IllegalNumberOfArgumentsException;
import de.xima.fc.form.expression.object.ALangObject;

public final class NamedFunctionUtils {
	public static void assertArgs(final Enum<?> name, final int count, final Object[] args, final IEvaluationContext ec)
			throws IllegalNumberOfArgumentsException {
		if (args.length != count)
			throw new IllegalNumberOfArgumentsException(name.toString(), count, args.length, ec);
	}

	public static void assertArgs(final Enum<?> name, final int min, final int max, final Object[] args,
			final IEvaluationContext ec) throws IllegalNumberOfArgumentsException {
		if (args.length < min || args.length > max)
			throw new IllegalNumberOfArgumentsException(name.toString(), min, max, args.length, ec);
	}

	public static <T extends ALangObject> T getArgOrNull(final Enum<?> name, final int idx, final ALangObject[] args,
			final Class<T> clazz, final IEvaluationContext ec) throws EvaluationException {
		if (idx >= args.length)
			return null;
		final ALangObject o = args[idx];
		if (!clazz.isAssignableFrom(o.getClass()))
			throw new IllegalArgumentTypeException(name.toString(), idx, o.getClass(), clazz, ec);
		return clazz.cast(o);
	}
}
