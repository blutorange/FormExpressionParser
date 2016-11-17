package de.xima.fc.form.expression.impl.binding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IBinding;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

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
public class LookUpBindingAlternative implements IBinding {

	private final Map<String, ALangObject[]> map;
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
	public ALangObject getVariable(final String name) throws EvaluationException {
		ALangObject[] array = map.get(name);
		if (array == null) return null;
		// Enlarge breakpoint array
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
		// Enlarge array
		if (currentDepth >= array.length)
			map.put(name, array = Arrays.copyOf(array, 2*currentDepth+1));
		for (int i = currentDepth; i > 0 && !breakpoints[i]; --i)
			if (array[i] != null) return array[i];
		return array[0];
	}

	@Override
	public void setVariable(final String name, final ALangObject value) throws EvaluationException {
		ALangObject[] array = map.get(name);
		// Create array
		if (array == null)
			map.put(name, array = new ALangObject[2*currentDepth+1]);
		// Enlarge array
		else if (currentDepth >= array.length)
			map.put(name, array = Arrays.copyOf(array, 2*currentDepth+1));
		// Enlarge breakpoint array
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
		// Set variable.
		for (int i = currentDepth; i >= 0 && !breakpoints[i]; --i)
			if (array[i] != null) {
				array[i] = value;
				return;
			}
		array[currentDepth] = value;
	}

	@Override
	public void nest(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		++currentDepth;
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth);
	}

	@Override
	public void nestLocal(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		if (currentDepth >= breakpoints.length)
			breakpoints = Arrays.copyOf(breakpoints, 2*currentDepth+1);
		breakpoints[currentDepth] = true;
		nest(ec);
	}

	@Override
	public void unnest(final IEvaluationContext ec) throws CannotUnnestGlobalNestingException {
		for (final ALangObject[] values : map.values())
			if (currentDepth < values.length) values[currentDepth] = null;
		if (currentDepth <= 0) throw new CannotUnnestGlobalNestingException(ec);
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
}