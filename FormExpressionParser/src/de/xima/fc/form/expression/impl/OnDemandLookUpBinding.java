package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * Same as {@link LookUpBinding}, but creates maps only when it reaches that
 * nesting level.
 *
 * @see LookUpBinding
 *
 * @author madgaksha
 *
 */
public class OnDemandLookUpBinding extends LookUpBinding {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 1024;

	public OnDemandLookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}

	public OnDemandLookUpBinding(final int nestingDepth) {
		this(nestingDepth, new HashMap<String, ALangObject>());
	}

	@SuppressWarnings("unchecked")
	protected OnDemandLookUpBinding(int nestingDepth, final Map<String, ALangObject> globalVariables) {
		if (nestingDepth < 1) nestingDepth = 1;
		breakpointArray = new boolean[nestingDepth];
		mapArray = new Map[nestingDepth];
		mapArray[0] = globalVariables;
		currentDepth = 0;
	}

	@Override
	public IBinding nest() {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth, this);
		++currentDepth;
		if (mapArray[currentDepth] == null) mapArray[currentDepth] = new HashMap<String, ALangObject>();
		else mapArray[currentDepth].clear();
		return this;
	}
}
