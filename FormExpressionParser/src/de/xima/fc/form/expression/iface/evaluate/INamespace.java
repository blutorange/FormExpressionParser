package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.IReset;

/**
 * <p>
 * A namespace that contains all available methods, attribute accessors and assigners
 * for each type of language object.
 * </p><p>
 * For example, an array provides methods such as <code>[1,2] + [3,4]</code>; dot attribute
 * accessors such as <code>length</code> or <code>sort</code>; and bracket attribute accessor
 * such as <code>array[i]</code>. It also provides dot attribute assigners such as
 * <code>array.length = 5</code> and bracket attribute assigners such as <code>array[0] = 5/<code>.
 * </p><p>
 * <b>Must be immutable.</b>
 * @author madgaksha
 */
@NonNullByDefault
public interface INamespace extends IReset{
	/**
	 * Expression method for the given object type. For example
	 * the thisContext for <code>"foo" * 3</code> is <code>string</code>.
	 * @param method Method to get.
	 * @param thisContext Type for which to get the method.
	 * @return The expression method for the given type, or <code>null</code>
	 * when there is no such method. Must also look it up in any supertypes.
	 */
	@Nullable
	public IFunction<?> expressionMethod(ILangObjectClass thisContext, EMethod method);

	/**
	 * For example <code>hash["key"]</code>.
	 * @param thisContext Type for which to get the accessor.
	 * @param property Property to access.
	 * @return The attribute accessor for the given type, or <code>null</code>
	 * when there is no such accessor. Must also look it up in any supertypes.
	 */
	@Nullable
	public IFunction<?> bracketAccessor(ILangObjectClass thisContext);

	/**
	 * For example <code>array.length</code>.
	 * @param thisContext Type for which to get the accessor.
	 * @param property Property to access.
	 * @return The attribute accessor for the given type, or <code>null</code>
	 * when there is no such accessor. Must also look it up in any supertypes.
	 */
	@Nullable
	public IFunction<?> dotAccessor(ILangObjectClass thisContext, String property);

	/**
	 * @param method Bracket assigner to get, eg. <code>array[0] = 8</code>.
	 * @param type Type for which to get the assigner.
	 * @return The attribute assigner for the given type, or <code>null</code>
	 * when there is no such assigner. Must also look it up in any supertypes.
	 */
	@Nullable
	public IFunction<?> bracketAssigner(ILangObjectClass thisContext);

	/**
	 * For example <code>array.length = 8</code>.
	 * @param thisContext Type for which to get the assigner.
	 * @param property Dot assigner to get.
	 * @return The attribute assigner for the given type, or <code>null</code>
	 * when there is no such assigner. Must also look it up in any supertypes.
	 */
	@Nullable
	public IFunction<?> dotAssigner(ILangObjectClass thisContext, String property);
}