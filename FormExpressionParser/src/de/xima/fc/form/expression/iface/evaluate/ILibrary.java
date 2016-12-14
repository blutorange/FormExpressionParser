package de.xima.fc.form.expression.iface.evaluate;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.IReset;
import de.xima.fc.form.expression.impl.library.ELibraryScopeContractFactoryVoid;
import de.xima.fc.form.expression.object.ALangObject;

/**
 * <p>
 * Interface for scopes. An scopes provides read-only
 * access to values and functions addressed by scope name
 * and variable name.
 * </p><p>
 * For example, the {@link ELibraryScopeContractFactoryVoid} provides several mathematical
 * constants and functions under the scope name <code>math</code>,
 * the scope <code>fields</code> provides access to form fields.
 * To access the value of pi, a program needs to require the scope
 * first:
 * <pre>
 *   require scope math;
 *   loginfo(math::pi);
 * </pre>
 * <p>
 * Usually, there is one {@link ILibraryScope} for each scope name
 * and the external scope simply manages a map between the scope
 * name the corresponding {@link ILibraryScope}.
 * </p>
 * @see LibraryImpl.Builder
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public interface ILibrary extends IReset {
	/**
	 * Retrieves a variable with the given scope and name.
	 * As semantic checks are performed before executing
	 * the program, an error must be thrown when a variable
	 * happens not to exist or cannot be retrieved.
	 *
	 * @param scope
	 *            Name of the scope.
	 * @param name
	 *            Name of the scoped variable.
	 * @return The value of the variable.
	 * @throws EvaluationException
	 *             When the variable cannot be retrieved, eg. when it does not exist.
	 */
	public ALangObject getVariable(String scope, String name, IEvaluationContext ec)
			throws EvaluationException;

	/** @return All scopes this library provides. */
	public Collection<String> getProvidedScopes();
}