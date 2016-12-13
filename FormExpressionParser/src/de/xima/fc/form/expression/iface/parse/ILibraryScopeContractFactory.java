package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

/**
 * <p>
 * A contract factory for a custom scope providing information on the scope and
 * which variables it can provide.
 * </p>
 * <p>
 * To enable static code checking, all library scopes created by this contract
 * factory must always return an {@link ALangObject} for a given variable name,
 * and it must always be of the same type.
 * </p>
 * <p>
 * For example, this means that we cannot create a library scope
 * <code>field</code> that would return a value only for the form fields (eg
 * <code>field::tf1</code>, <code>field::lastName</code>), that exist in the
 * current form version. Instead, we must make the scope return
 * {@link NullLangObject} when there is no such field.
 * </p>
 * This also means that there can be no function <code>getRandomObject</code>
 * that randomly returns a number or a string. (Unless the variable type is
 * indicated as {@link ELangObjectType#OBJECT}).
 * </p>
 *
 * @author madgaksha
 */
@ParametersAreNonnullByDefault
public interface ILibraryScopeContractFactory<T> {
	/**
	 * @return A scope adhering to the specifications as indicates by this
	 *         contract factory.
	 */
	public ILibraryScope<T> makeScope();

	/**
	 * Indicates whether the library scope either always
	 * provides {@link ALangObject} for the the  given
	 * variable name; or never provides one.
	 * @param variableName
	 *            Name of the variable to check.
	 * @return Whether this scope can provide a value for the given variable
	 *         name.
	 */
	public boolean isProviding(String variableName);

	/**
	 * Indicates the type of variable that the library scope always returns for
	 * the given variable name.
	 *
	 * @param variableName
	 *            Variable to get info for.
	 * @return The variable type of the given variable. <code>null</code> when
	 *         {@link #isProviding(String)} returns <code>false</code>.
	 */
	@Nullable
	public IVariableType getVariableType(String variableName);

	/** @return The name of this scope. */
	public String getScopeName();

	/**
	 * @return Whether this scope is provided by an external context or from a
	 *         built-in library.
	 */
	public EVariableSource getSource();
}