package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.IReset;
import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.object.FunctionLangObject;

/**
 * An evaluation context is made up of the following parts:
 * <ul>
 *   <li>{@link IBinding} The binding is responsible for keeping track of local variables and handle nesting (for-loop, function, etc).</li>
 *   <li>{@link IExternalScope} The scope is responsible for handling queries to qualified variables from a scope, eg. <code>fields::alias</code>.
 *   <li>{@link INamespace} The namespace contains methods and attribute accessors/assigners for all the different language object. Instance methods are attributes of the type {@link FunctionLangObject}.</li>
 *   <li>{@link ILogger} The logger is an object that knows how to log messages logged by the interpreted program. It is not for logging messages of the interpreter itself.</li>
 *   <li>{@link ITracer} The tracer keeps track of the current position in the program and is used to build stack traces when exception occur.</li>
 *   <li>{@link IEmbedment} The embedment contains information on how to handle different embedment contexts such as <code>[%%$]...[%]</code>.
 *   <li>{@link IExternalContext} The external context provides several hooks that can be used to alter how certain expressions are evaluated.</li>
 * </ul>
 * @author mad_gaksha
 */
public interface IEvaluationContext extends IReset {
	@Nonnull
	public IExternalScope getScope();
	@Nonnull
	public INamespace getNamespace();
	@Nonnull
	public ILogger getLogger();
	@Nonnull
	public ITracer<Node> getTracer();
	@Nonnull
	public IEmbedment getEmbedment();

	@Nullable
	public IExternalContext getExternalContext();
	public void setExternalContext(@Nullable IExternalContext externalContext);

	/**
	 * Must be an equivalence relation.
	 * @param name1
	 *            Name of one variable.
	 * @param name2
	 *            Name of another variable.
	 * @return Whether two variable names are equivalent and refer to the same
	 *         variable. Default could be {@link String#equals(Object)}
	 */
	public boolean variableNameEquals(@Nonnull String name1, @Nonnull String name2);
	public void createSymbolTable(int symbolTableSize);
	@Nonnull
	public IVariableReference[] getSymbolTable();
}