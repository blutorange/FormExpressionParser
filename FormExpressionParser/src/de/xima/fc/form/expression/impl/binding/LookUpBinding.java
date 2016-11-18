package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.CannotUnnestGlobalNestingException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IBinding;

/**
 * A binding that creates a {@link Map} when instantiated for each nesting
 * level. Upon nesting, it increments the pointer to the current map. Throws a
 * {@link NestingLevelTooDeepException} when the nesting level is deeper than
 * the number of maps. Variable lookup proceeds at the current nesting level,
 * iteratively inspecting parent maps when no variable can be found. <br>
 * <br>
 * Retrieving variables: The current nesting level is tried, then parent nesting
 * levels, unless nesting was done via {@link #nestLocal()}. When no variable
 * could be found, the top level is tried. <br>
 * <br>
 * Setting variables: first searches for a variable that may exist on higher
 * nesting levels, respecting breakpoints set by {@link #nestLocal()}. When no
 * variable could be found, creates a new variable at the current nesting level.
 *
 * @author madgaksha
 *
 */
public class LookUpBinding<T> implements IBinding<T> {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 128;

	protected boolean[] breakpointArray;
	protected Map<String, T>[] mapArray;
	protected int currentDepth;

	public LookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}

	@SuppressWarnings("unchecked")
	public LookUpBinding(int nestingDepth) {
		if (nestingDepth < 1)
			nestingDepth = 1;
		mapArray = new Map[nestingDepth];
		breakpointArray = new boolean[nestingDepth];
		for (int i = 0; i < nestingDepth; ++i) {
			mapArray[i] = new HashMap<String, T>();
		}
		currentDepth = 0;
	}

	@Override
	public void reset() {
		for (int i = 0; i < mapArray.length; ++i)
			mapArray[i].clear();
		currentDepth = 0;
	}

	@Override
	public T getVariable(final String name) {
		for (int i = currentDepth; i > 0 && !breakpointArray[i]; --i) {
			final T o = mapArray[i].get(name);
			if (o != null)
				return o;
		}
		return null;
	}

	@Override
	public void nest() throws NestingLevelTooDeepException {
		if (currentDepth >= mapArray.length - 1)
			throw new NestingLevelTooDeepException(currentDepth+1);
		++currentDepth;
	}

	@Override
	public void unnest() throws CannotUnnestGlobalNestingException {
		mapArray[currentDepth].clear();
		if (currentDepth <= 0)
			throw new CannotUnnestGlobalNestingException();
		--currentDepth;
		breakpointArray[currentDepth] = false;
	}

	@Override
	public void nestLocal() throws NestingLevelTooDeepException {
		breakpointArray[currentDepth] = true;
		nest();
	}

	@Override
	public int getNestingLimit() {
		return mapArray.length - 1;
	}

	@Override
	public boolean isAtMaximumNestingLimit() {
		return currentDepth >= getNestingLimit();
	}

	@Override
	public boolean isGlobal() {
		return currentDepth <= 0;
	}

	@Override
	public boolean hasVariableAtCurrentLevel(final String name) {
		return mapArray[currentDepth].containsKey(name);
	}

	@Override
	public void defineVariable(final String name, final T object) {
		mapArray[currentDepth].put(name, object);
	}
}
