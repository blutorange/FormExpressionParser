package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.AEvaluationVisitor;

public interface IEvaluationContext {

	public IScope getScope();
	public IBinding getBinding();
	public INamespace getNamespace();
	public ILogger getLogger();
	
	public void setEvaluationVisitor(AEvaluationVisitor<?,?> visitor);
	public AEvaluationVisitor<?,?> getEvaluationVisitor();
	
	public void setBinding(IBinding binding);
	public int getRecursionLimit();
	/**
	 * When reading an unqualified variable, this method must resolve
	 * the variable either to a local variable or a variable from
	 * some scope.
	 * @param name Name of the variable to retrieve.
	 * @return Value of the variable.
	 * @throws VariableNotDefinedException When the variable cannot be found anywhere.
	 * @throws EvaluationException When the variable cannot be retrieved for any other reason.
	 */
	public ALangObject getUnqualifiedVariable(String name) throws EvaluationException;

	/**
	 * When writing to an unqualified variable, this method must resolve the
	 * variable either to some local variable or to a variable from some scope.
	 * @param name Name of the variable.
	 * @param value Value to be set.
	 * @throws EvaluationException When the variable cannot be set for any reason.
	 */
	public void setUnqualifiedVariable(String name, ALangObject value) throws EvaluationException;

	/**
	 * Called at the beginning of a scope block:
	 * <pre>
	 * with scope foobar {
	 *   ...
	 * }
	 * </pre>
	 * @param scope Name of the scope to be added to the list of default lookup scopes for unqualified variables.
	 */
	public void beginDefaultScope(String scope);
	/**
	 * Called at the end of a scope block and remove the scope added most recently.
	 * <pre>
	 * with scope foobar {
	 *   ...
	 * }
	 * </pre>
	 */
	public void endDefaultScope();

	/**
	 * Must be an equivalence relation.
	 * @param name1
	 *            Name of one variable.
	 * @param name2
	 *            Name of another variable.
	 * @return Whether two variable names are equivalent and refer to the same
	 *         variable. Default could be {@link String#equals(Object)}
	 */
	public boolean variableNameEquals(String name1, String name2);
}
