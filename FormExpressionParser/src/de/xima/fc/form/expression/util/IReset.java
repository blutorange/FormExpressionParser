package de.xima.fc.form.expression.util;

public interface IReset<T> {
	/** Reset the entity to its initial state and returns the reset entity, which
	 * may be a different object. */
	public T reset();
}
