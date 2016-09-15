package de.xima.fc.form.expression.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

/**
 * Keeps a list for each nesting level separately for
 * each variable. Creates new list only when necessary.
 * May not perform well when most variables are set in each scope.
 * May perform well when there are many changes in scope and only a few different variables are set.
 *
 * @author madgaksha
 */
public class QueueBinding implements IBinding {

	private final Map<String, List<ALangObject>> map;
	private final int initialStackSize;
	private int level;

	public QueueBinding() {
		this(16, 8);
	}

	public QueueBinding(final int initialCapacity, final int initialStackSize) {
		map = new HashMap<>(initialCapacity);
		this.initialStackSize = initialStackSize;
		level = 0;
	}

	@Override
	public final ALangObject getVariable(final String name) throws EvaluationException {
		final List<ALangObject> list = map.get(name);
		if (list == null) return getDefaultValue(name);
		return list.get(list.size()-1);
	}

	/**
	 * Value returned when variable is not in scope. May be overridden to provide other default values.
	 * @param name Name of the form field. Defaults to {@link NullLangObject} unless overridden.
	 */
	protected ALangObject getDefaultValue(final String name) {
		return NullLangObject.getInstance();
	}

	@Override
	public final void setVariable(final String name, final ALangObject value) throws EvaluationException {
		// Create new stack when variable is set for the first time.
		List<ALangObject> list = map.get(name);
		if (list == null) {
			list = new ArrayList<>(initialStackSize);
			map.put(name, list);
		}
		// Set variable at the appropriate nesting level.
		final int size = list.size();
		if (level == size) list.add(value);
		else if (level > size) {
			final ALangObject object = size == 0 ? NullLangObject.getInstance() : list.get(size-1);
			for (int i = level-size-1; i > 0; --i)
				list.add(object);
		}
		else list.set(level, value);
	}

	@Override
	public final IBinding nest() {
		++level;
		return this;
	}

	@Override
	public final IBinding unnest() {
		--level;
		return this;
	}

	@Override
	public void reset() {
		map.clear();
	}
}