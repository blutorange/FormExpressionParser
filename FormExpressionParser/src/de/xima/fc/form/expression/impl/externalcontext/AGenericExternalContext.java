package de.xima.fc.form.expression.impl.externalcontext;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.impl.ec.EvaluationContextContract;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * An external context providing scoped variables, to be used with the
 * {@link EvaluationContextContract}. As the
 * {@link #fetchScopedVariable(String, String, IEvaluationContext)} is final,
 * subclasses cannot provided variables either.
 *
 * @author mad_gaksha
 */
public abstract class AGenericExternalContext implements IExternalContext {
	@Override
	public final ALangObject fetchScopedVariable(final String scope, final String name, final IEvaluationContext ec)
			throws EvaluationException {
		throw new VariableNotDefinedException(scope, name, ec);
	}
}
