package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NestingLevelTooDeepException;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.object.ALangObject;

public class LookUpBinding implements IBinding {

	/** Subject to change. Do not rely on this being set to a certain value. */
	public final static int DEFAULT_NESTING_DEPTH = 128;
	
	protected Map<String, ALangObject>[] mapArray;
	protected int currentDepth;
	
	public LookUpBinding() {
		this(DEFAULT_NESTING_DEPTH);
	}
	
	@SuppressWarnings("unchecked")
	public LookUpBinding(int nestingDepth) {
		if (nestingDepth < 1) nestingDepth = 1;
		mapArray = new Map[nestingDepth];
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
	public ALangObject getVariable(String name) throws EvaluationException {
		for (int i = currentDepth; i >= 0; --i) {
			final ALangObject object = mapArray[currentDepth].get(name);
			if (object != null) return object;
		}
		return getDefaultValue(name);
	}

	/**
	 * Value returned when variable is not in scope. May be overridden to provide other default values.
	 * @param name Name of the form field. Defaults to throwing a {@link VariableNotDefinedException} when not overridden.
	 */
	protected ALangObject getDefaultValue(final String name) throws VariableNotDefinedException {
		throw new VariableNotDefinedException(name, this);
	}

	
	@Override
	public void setVariable(String name, ALangObject value) throws EvaluationException {
		mapArray[currentDepth].put(name, value);
	}

	@Override
	public IBinding nest() {
		if (currentDepth >= mapArray.length - 1) throw new NestingLevelTooDeepException(currentDepth, this);
		++currentDepth;
		return this;
	}

	@Override
	public IBinding unnest() {
		if (currentDepth <= 0) throw new UncatchableEvaluationException(this, "Cannot unnest global binding, this may be an error in the parser. Contact support.");
		return this;
	}
	
}
