package de.xima.fc.form.expression.iface.evaluate;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
public interface IEvaluationResult {
	public ALangObject getObject();
	public ImmutableList<IEvaluationWarning> getWarnings();
}