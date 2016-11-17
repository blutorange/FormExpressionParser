package de.xima.fc.form.expression.impl.binding;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.object.ALangObject;

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
public class OnDemandLookUpBinding extends LookUpBinding {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 512;

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
	public void reset() {
		for (int i = 0; i < mapArray.length; ++i) {
			if (mapArray[i] == null) break;
			mapArray[i].clear();
		}
		currentDepth = 0;
	}

	@Override
	public void nest(final IEvaluationContext ec) throws NestingLevelTooDeepException {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth+1, ec);
		++currentDepth;
		if (mapArray[currentDepth] == null) mapArray[currentDepth] = new HashMap<String, ALangObject>();
	}
}
