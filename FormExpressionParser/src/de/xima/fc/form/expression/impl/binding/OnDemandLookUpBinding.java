package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.parse.NestingLevelTooDeepException;

/**
 * Same as {@link LookUpBinding}, but creates maps only when it reaches that
 * nesting level. This way, the nesting level can be effectively unlimited, while
 * still imposing a limit to prevent bad code from using too much memory.
 *
 * @see LookUpBinding
 *
 * @author madgaksha
 *
 */
public class OnDemandLookUpBinding<T> extends LookUpBinding<T> {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 512;

	public OnDemandLookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}

	@SuppressWarnings("unchecked")
	public OnDemandLookUpBinding(int nestingDepth) {
		if (nestingDepth < 1)
			nestingDepth = 1;
		breakpointArray = new boolean[nestingDepth];
		mapArray = new Map[nestingDepth];
		mapArray[0] = new HashMap<String, T>();
		currentDepth = 0;
	}

	@Override
	public void reset() {
		for (int i = 0; i < mapArray.length; ++i) {
			if (mapArray[i] == null) break;
			mapArray[i].clear();
		}
		currentDepth = 0;
	}

	@Override
	public void nest() throws NestingLevelTooDeepException {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth+1);
		++currentDepth;
		if (mapArray[currentDepth] == null) mapArray[currentDepth] = new HashMap<String, T>();
	}
}
