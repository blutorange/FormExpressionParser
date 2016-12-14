package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;

public interface INonNullIterable<T> extends Iterable<T> {
	@Override
	@Nonnull public INonNullIterator<T> iterator();
}
