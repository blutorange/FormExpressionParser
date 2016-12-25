package de.xima.fc.form.expression.iface.evaluate;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.object.ALangObject;

@ParametersAreNonnullByDefault
public interface IEvaluationResult {
	public ALangObject getObject();
	public List<IEvaluationWarning> getWarnings();
}