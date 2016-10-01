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
 * A binding that creates a {@link Map} when instantiated for each nesting level. Upon nesting,
 * it increments the pointer to the current map. Throws a {@link NestingLevelTooDeepException}
 * when the nesting level is deeper than the number of maps. Variable lookup proceeds at the
 * current nesting level, iteratively inspecting parent maps when no variable can be found.
 * <br><br>
 * Retrieving variables: The current nesting level is tried, then parent nesting levels,
 * unless nesting was done via {@link #nestLocal()}. When no variable could be found,
 * the top level is tried.
 * <br><br>
 * Setting variables: first searches for a variable that may exist on higher nesting
 * levels, respecting breakpoints set by {@link #nestLocal()}. When no variable could be found, creates a new variable
 * at the current nesting level.
 * @author madgaksha
 *
 */
public class LookUpBinding implements IBinding {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 128;

	protected boolean[] breakpointArray;
	protected Map<String, ALangObject>[] mapArray;
	protected int currentDepth;

	public LookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}

	@SuppressWarnings("unchecked")
	public LookUpBinding(int nestingDepth) {
		if (nestingDepth < 1) nestingDepth = 1;
		mapArray = new Map[nestingDepth];
		breakpointArray = new boolean[nestingDepth];
		for (int i = 0; i < nestingDepth; ++i) {
			mapArray[i] = new HashMap<String, ALangObject>();
		}
		currentDepth = 0;
	}

	@Override
	public IBinding reset() {
		for (int i = 0; i < mapArray.length; ++i) mapArray[i].clear();
		currentDepth = 0;
		return this;
	}

	@Override
	public ALangObject getVariable(final String name) throws EvaluationException {
		for (int i = currentDepth; i > 0 && !breakpointArray[i]; --i) {
			final ALangObject o = mapArray[i].get(name);
			if (o != null) return o;
		}
		return mapArray[0].get(name);
	}

	@Override
	public void setVariable(final String name, final ALangObject value) throws EvaluationException {
		for (int i = currentDepth; i >= 0 && !breakpointArray[i]; --i) {
			final ALangObject o = mapArray[i].get(name);
			if (o != null) {
				mapArray[i].put(name, value);
				return;
			}
		}
		mapArray[currentDepth].put(name, value);
	}

	@Override
	public IBinding nest(final IEvaluationContext ec) {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth, ec);
		++currentDepth;
		mapArray[currentDepth].clear();
		return this;
	}

	@Override
	public IBinding unnest(final IEvaluationContext ec) {
		if (currentDepth <= 0) throw new UncatchableEvaluationException(ec, "Cannot unnest global binding. This may be an error in the parser. Contact support.");
		--currentDepth;
		breakpointArray[currentDepth] = false;
		return this;
	}

	@Override
	public IBinding nestLocal(final IEvaluationContext ec) {
		breakpointArray[currentDepth] = true;
		return nest(ec);
	}

	@Override
	public int getNestingLimit() {
		return mapArray.length-1;
	}

}