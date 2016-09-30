package de.xima.fc.form.expression.impl;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.ILogger;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.impl.logger.SystemLogger;
import de.xima.fc.form.expression.impl.logger.SystemLogger.Level;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * Optionally, a scope may be specified for a variable by prepending
 * it separated with to columns, eg. <code>fields::tf1</code>. Variables
 * qualified with a scope never nest.
 * <br><br>
 * Scopes may be imported into the local scope via
 * <pre>
 *   with scope fields {
 *     tf1; // refers to fields::tf1
 *   }
 * </pre>
 * <br><br>
 * The following rules apply to variable lookup, depending on whether
 * a scope was specified.
 * 
 * <table>
 *   <th>
 *     <td><b>Reading</b></td>
 *     <td><b>Writing</b></td>
 *   </th>
 *   <tr>
 *     <td>without scope</td>
 *     <td>looked up in the local scope first, then in imported scopes</td>
 *     <td>looked up only in the local scope, never in any other scopes</td>
 *   </tr>
 *   <tr>
 *     <td>with scope</td>
 *     <td>looked up in the specified scope, and nowhere else</td>
 *     <td>looked up in the specified scope, and nowhere else</td>
 *   </tr> * </table>
 * @author mad_gaksha
 *
 */
public class ReadScopedEvaluationContext extends GenericEvaluationContext {

	private final List<String> defaultScopeList = new ArrayList<>(16);
	
	public ReadScopedEvaluationContext(final IBinding binding, final IScope scope, final INamespace namespace, final ILogger logger,
			final int recursionLimit) {
		super(binding, scope, namespace, logger, recursionLimit);
	}
	
	/**
	 * A new evaluation context with the given binding and namespace, a
	 * {@link SystemLogger} at {@link Level#INFO} and a recursion limit of 10.
	 * 
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 */
	public ReadScopedEvaluationContext(final IBinding binding, final IScope scope, final INamespace namespace) {
		this(binding, scope, namespace, SystemLogger.getInfoLogger(), 10);
	}

	/**
	 * A new evaluation context with the given binding and namespace, and a
	 * recursion limit of 10.
	 * 
	 * @param binding
	 *            Binding to use.
	 * @param namespace
	 *            Namespace to use.
	 * @param logger
	 *            The logger used for logging.
	 */
	public ReadScopedEvaluationContext(final IBinding binding, IScope scope, final INamespace namespace, ILogger logger) {
		this(binding, scope, namespace, logger, 10);
	}
	
	@Override
	public ALangObject getUnqualifiedVariable(String name) throws EvaluationException {
		final ALangObject loc = getBinding().getVariable(name);
		if (loc != null) return loc;
		for (int i = defaultScopeList.size() - 1; i >= 0; --i) {
			final ALangObject scp = getScope().getVariable(defaultScopeList.get(i), name);
			if (scp != null) return scp;
		}
		throw new VariableNotDefinedException(name, this);
	}

	@Override
	public void setUnqualifiedVariable(String name, ALangObject value) throws EvaluationException {
		getBinding().setVariable(name, value);
	}

	@Override
	public void beginDefaultScope(String scope) {
		defaultScopeList.add(scope);
	}

	@Override
	public void endDefaultScope() {
		defaultScopeList.remove(defaultScopeList.size() - 1);
	}

}
