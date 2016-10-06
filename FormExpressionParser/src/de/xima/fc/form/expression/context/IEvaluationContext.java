package de.xima.fc.form.expression.context;

import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.util.IReset;

/**
 * An evaluation context is made up of the following parts:
 * <ul>
 *   <li>{@link IBinding} The binding is responsible for keeping track of local variables and handle nesting (for-loop, function, etc).</li>
 *   <li>{@link IScope} The scope is responsible for handling queries to qualified variables from a scope, eg. <code>fields::alias</code>.
 *   <li>{@link INamespace} The namespace contains methods and attribute accessors/assigners for all the different language object. Instance methods are attributes of the type {@link FunctionLangObject}.</li>
 *   <li>{@link ILogger} The logger is an object that knows how to log messages logged by the interpreted program. It is not for logging messages of the interpreter itself.</li>
 *   <li>{@link ITracer} The tracer keeps track of the current position in the program and is used to build stack traces when exception occur.</li>
 *   <li>{@link IEmbedment} The embedment contains information on how to handle different embedment contexts such as <code>[%%$]...[%]</code>.
 *   <li>{@link IExternalContext} The external context provides several hooks that can be used to alter how certain expressions are evaluated.</li>
 * </ul>
 * @author mad_gaksha
 */
public interface IEvaluationContext extends IReset<Void> {

	public IScope getScope();
	public IBinding getBinding();
	public INamespace getNamespace();
	public ILogger getLogger();
	public ITracer<Node> getTracer();
	public IEmbedment getEmbedment();
	@Nullable
	public IExternalContext getExternalContext();

	public void setExternalContext(@Nullable IExternalContext externalContext);

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
