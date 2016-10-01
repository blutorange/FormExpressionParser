package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
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
	private final CloneBinding top;
	private final boolean isBreakpoint;

	public CloneBinding() {
		this(16);
	}

	public CloneBinding(final int initialCapacity) {
		map = new HashMap<>(initialCapacity);
		parent = null;
		top = this;
		isBreakpoint = true;
	}

	private CloneBinding(final CloneBinding parent, final CloneBinding top, final boolean isBreakpoint) {
		map = new HashMap<>(parent.map);
		this.parent = parent;
		this.top = top;
		this.isBreakpoint = isBreakpoint;
	}

	@Override
	public final ALangObject getVariable(final String name) throws EvaluationException {
		final ALangObject res = getVariableInternal(name);
		return res != null ? res : top.map.get(name);
	}

	private ALangObject getVariableInternal(final String name) throws EvaluationException {
		final ALangObject res = map.get(name);
		if (res != null) return res;
		return isBreakpoint ? res : parent.getVariable(name);
	}

	@Override
	public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
		if (setVariableInternal(name, value)) return;
		map.put(name, value);
	}

	private boolean setVariableInternal(final String name, final ALangObject value) throws EvaluationException {
		if (isBreakpoint) {
			if (map.containsKey(name)) {
				map.put(name, value);
				return true;
			}
		}
		return parent.setVariableInternal(name, value);
	}

	@Override
	public final IBinding nest(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		return new CloneBinding(this, top, true);
	}

	@Override
	public final IBinding unnest(final IEvaluationContext ec) {
		if (parent == null) throw new UncatchableEvaluationException(ec, "Cannot unnest global binding. This may be an error in the parser. Contact support.");
		return parent;
	}

	@Override
	public IBinding reset() {
		map.clear();
		return parent != null ? parent.reset() : this;
	}

	@Override
	public IBinding nestLocal(final IEvaluationContext ec) {
		return new CloneBinding(this, top, false);
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}
}
