package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.IReset;
import de.xima.fc.form.expression.iface.parse.IVariableReference;
import de.xima.fc.form.expression.object.FunctionLangObject;

/**
 * @formatter:off
 * An evaluation context is made up of the following parts:
 * <ul>
 *   <li>{@link IBinding} The binding is responsible for keeping track of local variables and handle nesting (for-loop, function, etc).</li>
 *   <li>{@link ILibrary} The library is responsible for providing access to scope variables from a system library, eg <code>fields::alias</code> or <code>math::pi</code>.
 *   <li>{@link INamespace} The namespace contains methods and attribute accessors/assigners for all the different language object. Instance methods are attributes of the type {@link FunctionLangObject}.</li>
 *   <li>{@link ILogger} The logger is an object that knows how to log messages logged by the interpreted program. It is not for logging messages of the interpreter itself.</li>
 *   <li>{@link ITracer} The tracer keeps track of the current position in the program and is used to build stack traces when exception occur.</li>
 *   <li>{@link IEmbedment} The embedment contains information on how to handle different embedment contexts such as <code>[%%$]...[%]</code>.
 *   <li>{@link IExternalContext} The external context provides several hooks that can be used to alter how certain expressions are evaluated.</li>
 * </ul>
 * @formatter:on
 * @author mad_gaksha
 */
@ParametersAreNonnullByDefault
public interface IEvaluationContext extends IReset {
	public ILibrary getLibrary();
	public INamespace getNamespace();
	public ILogger getLogger();
	public ITracer<Node> getTracer();
	public IEmbedment getEmbedment();

	@Nullable public IExternalContext getExternalContext();
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
	public boolean variableNameEquals(String name1, String name2);
	public void createSymbolTable(int symbolTableSize);
	public IVariableReference[] getSymbolTable();
}