package de.xima.fc.form.expression.impl.externalcontext;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.object.ALangObject;

public abstract class AGenericExternalContext implements IExternalContext {
	@Override
	public final ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec) throws EvaluationException {
		throw new VariableNotDefinedException(scope, name, ec);
	}
}
