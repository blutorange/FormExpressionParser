package de.xima.fc.form.expression.util;

import java.util.Comparator;
import java.util.Map.Entry;

import de.xima.fc.form.expression.object.ALangObject;

public enum ComparatorEntryByKey implements Comparator<Entry<ALangObject, ?>> {
	INSTANCE;

	@Override
	public int compare(final Entry<ALangObject, ?> o1, final Entry<ALangObject, ?> o2) {
		return o1.getKey().compareTo(o2.getKey());
	}
}
