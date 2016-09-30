package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * A binding that creates a {@link Map} when instantiated for each nesting level. Upon nesting,
 * it increments the pointer to the current map. Throws a {@link NestingLevelTooDeepException}
 * when the nesting level is deeper than the number of maps. Variable lookup proceeds at the
 * current nesting level, iteratively inspecting parent maps when no variable can be found.
 *
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
	public void reset() {
		for (int i = currentDepth; i < mapArray.length; ++i) mapArray[i].clear();
		currentDepth = 0;
	}

	@Override
	public ALangObject getVariable(final String name) throws EvaluationException {
		for (int i = currentDepth; i >= 0 && !breakpointArray[i]; --i) {
			final ALangObject o = mapArray[currentDepth].get(name);
			if (o != null) return o;
		}
		return null;
	}

	@Override
	public void setVariable(final String name, final ALangObject value) throws EvaluationException {
		mapArray[currentDepth].put(name, value);
	}

	@Override
	public IBinding nest() {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth, this);
		++currentDepth;
		mapArray[currentDepth].clear();
		return this;
	}

	@Override
	public IBinding unnest() {
		if (currentDepth <= 0) throw new UncatchableEvaluationException(this, "Cannot unnest global binding. This may be an error in the parser. Contact support.");
		--currentDepth;
		breakpointArray[currentDepth] = false;
		return this;
	}

	@Override
	public IBinding nestLocal() {
		breakpointArray[currentDepth] = true;
		return nest();
	}

	@Override
	public int getNestingLimit() {
		return mapArray.length-1;
	}
	
}
