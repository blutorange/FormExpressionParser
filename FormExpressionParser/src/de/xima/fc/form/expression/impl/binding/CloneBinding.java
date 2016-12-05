package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.parse.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.parse.NestingLevelException;
import de.xima.fc.form.expression.exception.parse.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.evaluate.IBinding;

/**
 * Create a new binding instance with a new map for each nesting level. May not
 * perform well when there are many variables in scope. May perform well when
 * most variables are used in each scope.
 *
 * @author madgaksha
 */
public class CloneBinding<T> implements IBinding<T> {
	private Impl<T> impl;
	private int level = 0;

	public CloneBinding() {
		impl = new Impl<T>();
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}

	@Override
	public void reset() {
		impl.reset();
		level = 0;
	}

	@Override
	public T getVariable(final String name) {
		return impl.getVariable(name);
	}


	@Override
	public void defineVariable(final String name, final T object) {
		impl.defineVariable(name, object);
	}

	@Override
	public void nest() throws NestingLevelTooDeepException {
		impl = impl.nest();
		++level;
	}

	@Override
	public void nestLocal() {
		impl = impl.nestLocal();
		++level;
	}

	@Override
	public void unnest() throws CannotUnnestGlobalNestingException {
		impl = impl.unnest();
		--level;
	}

	@Override
	public boolean isGlobal() {
		return impl.isGlobal();
	}

	@Override
	public boolean isAtMaximumNestingLimit() {
		return false;
	}

	@Override
	public int getBookmark() {
		return level;
	}

	@Override
	public void gotoBookmark(final int bookmark) throws NestingLevelException {
		while (bookmark != level) {
			if (bookmark > level)
				nest();
			else
				unnest();
		}
	}
	
	private static class Impl<T> {
		private final Map<String, T> map;
		@Nullable private final Impl<T> parent;
		private final boolean isBreakpoint;

		public Impl() {
			this(16);
		}

		public Impl(final int initialCapacity) {
			map = new HashMap<>(initialCapacity);
			parent = null;
			isBreakpoint = true;
		}

		private Impl(@Nonnull final Impl<T> parent, final boolean isBreakpoint) {
			map = new HashMap<>();
			this.parent = parent;
			this.isBreakpoint = isBreakpoint;
		}

		@Nullable
		public final T getVariable(@Nonnull final String name) {
			final T res = map.get(name);
			if (res != null)
				return res;
			final Impl<T> p = parent;
			return isBreakpoint || p == null ? res : p.getVariable(name);
		}

		public final void defineVariable(@Nonnull final String name, @Nonnull final T object) {
			map.put(name, object);
		}

		@Nonnull
		public final Impl<T> nest() throws NestingLevelTooDeepException {
			return new Impl<T>(this, false);
		}

		@Nonnull
		public final Impl<T> unnest() throws CannotUnnestGlobalNestingException {
			final Impl<T> p = parent;
			if (p == null)
				throw new CannotUnnestGlobalNestingException();
			map.clear();
			return p;
		}

		@Nonnull
		public Impl<T> reset() {
			map.clear();
			return parent != null ? parent.reset() : this;
		}

		@Nonnull
		public Impl<T> nestLocal() {
			return new Impl<T>(this, true);
		}

		public boolean isGlobal() {
			return parent == null;
		}

		public boolean hasVariableAtCurrentLevel(final String name) {
			return map.get(name) != null;
		}
	}

	@Override
	public boolean hasVariableAtCurrentLevel(final String name) {
		return impl.hasVariableAtCurrentLevel(name);
	}
}
