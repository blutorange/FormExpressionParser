package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
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
	private final CloneBinding parent;
	private final boolean isBreakpoint;

	public CloneBinding() {
		this(16);
	}

	public CloneBinding(final int initialCapacity) {
		map = new HashMap<>(initialCapacity);
		parent = null;
		isBreakpoint = true;
	}

	private CloneBinding(final CloneBinding parent, boolean isBreakpoint) {
		map = new HashMap<>(parent.map);
		this.parent = parent;
		this.isBreakpoint = isBreakpoint;
	}

	@Override
	public final ALangObject getVariable(final String name) throws EvaluationException {
		final ALangObject res = getVariableInternal(name);
		return res == null ? map.get(name) : res;
	}
	
	private ALangObject getVariableInternal(final String name) throws EvaluationException {
		return isBreakpoint ? map.get(name) : parent.getVariable(name);
	}

	@Override
	public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
		if (setVariableInternal(name, value)) return;
		map.put(name, value);
	}
	
	private boolean setVariableInternal(String name, final ALangObject value) throws EvaluationException {
		if (isBreakpoint) {
			if (map.containsKey(name)) {
				map.put(name, value);
				return true;
			}
		}
		return parent.setVariableInternal(name, value);
	}

	@Override
	public final IBinding nest() throws NestingLevelTooDeepException {
		return new CloneBinding(this, true);
	}

	@Override
	public final IBinding unnest() {
		if (parent == null) throw new UncatchableEvaluationException(this, "Cannot unnest global binding. This may be an error in the parser. Contact support.");
		return parent;
	}

	@Override
	public IBinding reset() {
		map.clear();
		return parent != null ? parent.reset() : this;
	}

	@Override
	public IBinding nestLocal() {
		return new CloneBinding(this, false);
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}		
}
