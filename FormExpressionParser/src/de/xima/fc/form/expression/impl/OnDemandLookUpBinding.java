package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.object.ALangObject;

public class OnDemandLookUpBinding extends LookUpBinding {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 1024;
	
	public OnDemandLookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}
	
	@SuppressWarnings("unchecked")
	public OnDemandLookUpBinding(int nestingDepth) {
		if (nestingDepth < 1) nestingDepth = 1;
		mapArray = new Map[nestingDepth];
		mapArray[0] = new HashMap<String, ALangObject>();
		currentDepth = 0;
	}
	
	@Override
	public IBinding nest() {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth, this);
		++currentDepth;
		if (mapArray[currentDepth] == null) mapArray[currentDepth] = new HashMap<String, ALangObject>();
		return this;
	}	
}
