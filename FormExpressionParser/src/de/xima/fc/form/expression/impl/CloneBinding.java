package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

/**
 * Create a new binding instance with a new map for each nesting level.
 * May not perform well when there are many variables in scope.
 * May perform well when most variables are used in each scope.
 *
 * @author madgaksha
 */
public class CloneBinding implements IBinding {
	private final Map<String, ALangObject> map;
	private IBinding parent;

	public CloneBinding() {
		this(16);
	}

	public CloneBinding(final int initialCapacity) {
		map = new HashMap<>(initialCapacity);
		parent = this;
	}

	private CloneBinding(final CloneBinding parent) {
		map = new HashMap<>(parent.map);
		this.parent = parent;
	}

	/**
	 * Value returned when variable is not in scope. May be overridden to provide other default values.
	 * @param name Name of the form field. Defaults to {@link NullLangObject} unless overridden.
	 */
	protected ALangObject getDefaultValue(final String name) {
		return NullLangObject.getInstance();
	}

	@Override
	public final ALangObject getVariable(final String name) throws EvaluationException {
		final ALangObject o = map.get(name);
		return o == null ? getDefaultValue(name) : o;
	}

	@Override
	public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
		map.put(name, value);
	}

	@Override
	public final IBinding nest() {
		return new CloneBinding(this);
	}

	@Override
	public final IBinding unnest() {
		return parent;
	}

	@Override
	public void reset() {
		map.clear();
		parent = null;
	}
}
