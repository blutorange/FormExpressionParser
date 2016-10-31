package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;

public interface NonNullIterable<T> extends Iterable<T> {
	@Override
	@Nonnull public NonNullIterator<T> iterator();
}
