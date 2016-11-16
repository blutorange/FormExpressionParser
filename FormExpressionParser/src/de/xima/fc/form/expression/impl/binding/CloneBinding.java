package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
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
		@Nullable private final Impl parent;
		@Nonnull private final Impl top;
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

		private Impl(@Nonnull final Impl parent, @Nonnull final Impl top, final boolean isBreakpoint) {
			map = new HashMap<>(parent.map);
			this.parent = parent;
			this.top = top;
			this.isBreakpoint = isBreakpoint;
		}

		@Nullable
		public final ALangObject getVariable(@Nonnull final String name) throws EvaluationException {
			final ALangObject res = getVariableInternal(name);
			return res != null ? res : top.map.get(name);
		}

		@Nullable
		private ALangObject getVariableInternal(@Nonnull final String name) throws EvaluationException {
			final ALangObject res = map.get(name);
			if (res != null)
				return res;
			final Impl p = parent;
			return isBreakpoint || p == null ? res : p.getVariable(name);
		}

		public final void setVariable(@Nonnull final String name, @Nonnull final ALangObject value) throws EvaluationException {
			if (!setVariableInternal(name, value))
				map.put(name, value);
		}

		private boolean setVariableInternal(@Nonnull final String name, @Nonnull final ALangObject value) throws EvaluationException {
			final Impl p = parent;
			if (isBreakpoint || p == null) {
				if (map.containsKey(name)) {
					map.put(name, value);
					return true;
				}
				return false;
			}
			return p.setVariableInternal(name, value);
		}

		@Nonnull
		public final Impl nest() throws NestingLevelTooDeepException {
			return new Impl(this, top, false);
		}

		@Nonnull
		public final Impl unnest(@Nonnull final IEvaluationContext ec) {
			final Impl p = parent;
			if (p == null)
				throw new UncatchableEvaluationException(ec, CmnCnst.Error.CANNOT_UNNEST_GLOBAL_BINDING);
			map.clear();
			return p;
		}

		@Nonnull
		public Impl reset() {
			map.clear();
			return parent != null ? parent.reset() : this;
		}

		@Nonnull
		public Impl nestLocal() {
			return new Impl(this, top, true);
		}
	}
}
