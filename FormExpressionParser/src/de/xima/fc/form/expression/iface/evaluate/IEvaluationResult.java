package de.xima.fc.form.expression.iface.evaluate;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@NonNullByDefault
public interface IEvaluationResult {
	public ALangObject getObject();
	public List<IEvaluationWarning> getWarnings();
}