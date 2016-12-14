package de.xima.fc.form.expression.object;

import java.util.Iterator;

import javax.annotation.Nonnull;

public interface INonNullIterator<E> extends Iterator<E> {
	@Override
	@Nonnull
	public E next();
}
