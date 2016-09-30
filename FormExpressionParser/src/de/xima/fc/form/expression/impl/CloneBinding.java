package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * Create a new binding instance with a new map for each nesting level.
 * May not perform well when there are many variables in scope.
 * May perform well when most variables are used in each scope.
 *
 * @author madgaksha
 */
public class CloneBinding implements IBinding {
	private final Map<String, ALangObject> map;
	private final IBinding parent;

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

	@Override
	public final ALangObject getVariable(final String name) throws EvaluationException {
		return map.get(name);
	}

	@Override
	public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
		map.put(name, value);
	}

	@Override
	public final IBinding nest() throws NestingLevelTooDeepException {
		return new CloneBinding(this);
	}

	@Override
	public final IBinding unnest() {
		return parent;
	}

	@Override
	public void reset() {
		map.clear();
	}

	@Override
	public IBinding nestLocal() {
		return new CloneBinding(map.size());
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}		
}
