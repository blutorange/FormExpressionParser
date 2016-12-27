package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.IReset;
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
	
	public void closureStackPush(final IClosure closure);
	public void closureStackPop() throws EvaluationException;

	/**
	 * @return The element on the top, or <code>null</code> when there is no element at the top.
	 */
	@Nullable
	public IClosure closureStackPeek();
	/**
	 * @return The jump type, or {@link EJump#NONE} when there is no jump.
	 */
	public EJump getJumpType();
	/**
	 * @return The jump label if {@link #getJumpType()} is {@link EJump#CONTINUE}
	 * or {@link EJump#BREAK}, or the empty string otherwise.
	 */
	@Nullable
	public String getJumpLabel();
	public void unsetJump();
	public boolean hasJump();
	public void setJump(EJump jumpType, @Nullable String lable);
	/**
	 * @param label Label to check.
	 * @return <code>true</code> iff {@link #getJumpType()} is {@link EJump#CONTINUE}
	 * or {@link EJump#BREAK}; and {@link #getJumpLabel()} is either <code>null</code>
	 * or this label matches the given label.
	 */
	public boolean matchesNamedJump(@Nullable String label);
	//!(jumpLabel != null && !jumpLabel.equals(label))
}