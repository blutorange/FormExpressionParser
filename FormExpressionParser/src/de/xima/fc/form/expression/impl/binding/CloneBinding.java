package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

/**
 * Create a new binding instance with a new map for each nesting level. May not
 * perform well when there are many variables in scope. May perform well when
 * most variables are used in each scope.
 *
 * @author madgaksha
 */
public class CloneBinding implements IBinding {
	private Impl impl;

	public CloneBinding() {
		impl = new Impl();
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}

	@Override
	public void reset() {
		impl.reset();
	}

	@Override
	public ALangObject getVariable(final String name) throws EvaluationException {
		return impl.getVariable(name);
	}

	@Override
	public void setVariable(final String name, final ALangObject value) throws EvaluationException {
		impl.setVariable(name, value);
	}

	@Override
	public void nest(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		impl = impl.nest();
	}

	@Override
	public void nestLocal(final IEvaluationContext ec) {
		impl = impl.nestLocal();
	}

	@Override
	public void unnest(final IEvaluationContext ec) {
		impl = impl.unnest(ec);
	}

	private class Impl {
		private final Map<String, ALangObject> map;
		private final Impl parent;
		private final Impl top;
		private final boolean isBreakpoint;

		public Impl() {
			this(16);
		}

		public Impl(final int initialCapacity) {
			map = new HashMap<>(initialCapacity);
			parent = null;
			top = this;
			isBreakpoint = true;
		}

		private Impl(final Impl parent, final Impl top, final boolean isBreakpoint) {
			map = new HashMap<>(parent.map);
			this.parent = parent;
			this.top = top;
			this.isBreakpoint = isBreakpoint;
		}

		public final ALangObject getVariable(final String name) throws EvaluationException {
			final ALangObject res = getVariableInternal(name);
			return res != null ? res : top.map.get(name);
		}

		private ALangObject getVariableInternal(final String name) throws EvaluationException {
			final ALangObject res = map.get(name);
			if (res != null)
				return res;
			return isBreakpoint ? res : parent.getVariable(name);
		}

		public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
			if (!setVariableInternal(name, value))
				map.put(name, value);
		}

		private boolean setVariableInternal(final String name, final ALangObject value) throws EvaluationException {
			if (isBreakpoint) {
				if (map.containsKey(name)) {
					map.put(name, value);
					return true;
				}
				return false;
			}
			return parent.setVariableInternal(name, value);
		}

		public final Impl nest() throws NestingLevelTooDeepException {
			return new Impl(this, top, false);
		}

		public final Impl unnest(final IEvaluationContext ec) {
			if (parent == null)
				throw new UncatchableEvaluationException(ec, CmnCnst.Error.CANNOT_UNNEST_GLOBAL_BINDING);
			map.clear();
			return parent;
		}

		public Impl reset() {
			map.clear();
			return parent != null ? parent.reset() : this;
		}

		public Impl nestLocal() {
			return new Impl(this, top, true);
		}
	}
}
