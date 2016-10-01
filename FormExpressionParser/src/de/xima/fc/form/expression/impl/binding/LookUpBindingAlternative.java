package de.xima.fc.form.expression.impl.binding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * Similar to {@link OnDemandLookUpBinding}, but uses a map of arrays (
 * <code>Map&lt;ALangObject[]&gt;</code>) instead. Try both and see which
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
	public IBinding reset() {
		map.clear();
		Arrays.fill(breakpoints, false);
		return this;
	}

	@Override
	public ALangObject getVariable(final String name) throws EvaluationException {
		final ALangObject[] array = map.get(name);
		if (array == null) return null;
		final int len = currentDepth < array.length ? currentDepth : array.length-1;
		for (int i = len; i > 0 && !breakpoints[i]; --i)
			if (array[i] != null) return array[i];
		return array[0];
	}

	@Override
	public void setVariable(final String name, final ALangObject value) throws EvaluationException {
		ALangObject[] array = map.get(name);
		if (array == null) {
			// Create array
			array = new ALangObject[2*currentDepth+1];
			map.put(name, array);
		}
		else if (currentDepth >= array.length) {
			// Enlarge array.
			final ALangObject[] newArray = new ALangObject[2*currentDepth+1];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
		// Set variable.
		final int len = currentDepth < array.length ? currentDepth : array.length-1;
		for (int i = len; i >= 0 && !breakpoints[i]; --i)
			if (array[i] != null) {
				array[i] = value;
				return;
			}
		array[currentDepth] = value;
	}

	@Override
	public IBinding nest(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		++currentDepth;
		for (final ALangObject[] values : map.values())
			if (currentDepth < values.length) values[currentDepth] = null;
		if (currentDepth >= breakpoints.length) {
			final boolean[] newArray = new boolean[2*currentDepth];
			System.arraycopy(breakpoints, 0, newArray, 0, breakpoints.length);
			breakpoints = newArray;
		}
		return this;
	}

	@Override
	public IBinding nestLocal(final IEvaluationContext ec) {
		if (currentDepth >= breakpoints.length) {
			final boolean[] newArray = new boolean[2*currentDepth];
			System.arraycopy(breakpoints, 0, newArray, 0, breakpoints.length);
			breakpoints = newArray;
		}
		breakpoints[currentDepth] = true;
		return nest(ec);
	}

	@Override
	public IBinding unnest(final IEvaluationContext ec) {
		if (currentDepth <= 0) throw new UncatchableEvaluationException(ec, "Cannot unnest global binding. This may be an error in the parser. Contact support.");
		--currentDepth;
		breakpoints[currentDepth] = false;
		return this;
	}

	@Override
	public int getNestingLimit() {
		return -1;
	}
}