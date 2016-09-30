package de.xima.fc.form.expression.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <b>Not thread-safe!</b>
 * @author mad_gaksha
 */
public class GenericScope implements IScope {
	private final MultiKeyMap<String, ALangObject> map;
	private final Map<String, ICustomScope> custom;	
	
	private GenericScope(MultiKeyMap<String, ALangObject> map, Map<String, ICustomScope> custom) {
		this.map = map;
		this.custom = custom;
	}
	
	@Override
	public ALangObject getVariable(String scope, String name) throws EvaluationException {
		final ALangObject value = map.get(scope, name);
		if (custom == null || value != null) return value;
		final ICustomScope customScope = custom.get(scope);
		return customScope == null ? null : customScope.fetch(name);
	}

	@Override
	public void setVariable(String scope, String name, ALangObject value) throws EvaluationException {
		map.put(scope, name, value);
	}
	
	public final static class Builder {
		private final MultiKeyMap<String, ALangObject> map;
		private final Map<String, ICustomScope> custom;
		private boolean useCustomScope = false;
		
		public Builder() {
			this(16);
		}
		public Builder(int initialSize) {
			map = MultiKeyMap.multiKeyMap(new HashedMap<MultiKey<? extends String>, ALangObject>(initialSize));
			custom = new HashMap<>(initialSize);
		}
		
		public void addObjects(String scope, Map<String, ALangObject> objects) {
			for (Entry<String, ALangObject> entry : objects.entrySet())
				map.put(scope, entry.getKey(), entry.getValue());
		}
		
		public void addCustomScope(ICustomScope customScope) {
			if (customScope == null ) return;
			useCustomScope = true;
			custom.put(customScope.getScopeName(), customScope);
		}
				
		public IScope build() {
			return new GenericScope(map, useCustomScope ? custom : null);
		}
	}
}