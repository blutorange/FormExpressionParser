package de.xima.fc.form.expression.impl.binding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IBinding;

/**
 * Similar to {@link OnDemandLookUpBinding}, but uses a map of arrays (
 * <code>Map&lt;T[]&gt;</code>) instead. Try both and see which
 * performs better. Probably performs better when most variables are non-local
 * and need to be looked up at lower nesting depths. Since <code>with (...) {}</code>
 * is deprecated, I suspect {@link OnDemandLookUpBinding} will perform better
 * in most cases.
 *
 * @author mad_gaksha
 *
 */
public class LookUpBindingAlternative<T> implements IBinding<T> {

	private final Map<String, T[]> map;
	private boolean[] breakpoints;
	private int currentDepth;

	public LookUpBindingAlternative() {
		map = new HashMap<>();
		breakpoints = new boolean[16];
		currentDepth = 0;
	}

	@Override
	public void reset() {
		map.clear();
		Arrays.fill(breakpoints, false);
	}

	@Override
	public T getVariable(final String name) {
		T[] array = map.get(name);
		if (array == null) return null;
		// Enlarge breakpoint array
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
		// Enlarge array
		if (currentDepth >= array.length)
			map.put(name, array = Arrays.copyOf(array, 2*currentDepth+1));
		for (int i = currentDepth; i > 0 && !breakpoints[i]; --i)
			if (array[i] != null) return array[i];
		return null;
	}

	@Override
	public void nest() throws NestingLevelTooDeepException {
		++currentDepth;
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
	}

	@Override
	public void nestLocal() throws NestingLevelTooDeepException {
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth+1);
		breakpoints[currentDepth] = true;
		nest();
	}

	@Override
	public void unnest() throws CannotUnnestGlobalNestingException {
		for (final T[] values : map.values())
			if (currentDepth < values.length) values[currentDepth] = null;
		if (currentDepth <= 0) throw new CannotUnnestGlobalNestingException();
		--currentDepth;
		breakpoints[currentDepth] = false;
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}

	@Override
	public boolean isAtMaximumNestingLimit() {
		return false;
	}

	@Override
	public boolean isGlobal() {
		return currentDepth <= 0;
	}

	@Override
	public boolean hasVariableAtCurrentLevel(final String name) {
		final T[] array = map.get(name);
		if (array == null)
			return false;
		return array[currentDepth] != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void defineVariable(final String name, final T object) {
		T[] array = map.get(name);
		if (array == null)
			array = (T[])new Object[2*currentDepth+1];
		// Enlarge breakpoint array
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
		// Enlarge array
		if (currentDepth >= array.length)
			map.put(name, array = Arrays.copyOf(array, 2*currentDepth+1));
		array[currentDepth] = object;
	}
}