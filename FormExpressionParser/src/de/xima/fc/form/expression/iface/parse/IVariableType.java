package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;

import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;

@Immutable
@NonNullByDefault
public interface IVariableType extends Serializable {
	public boolean equalsType(IVariableType other);

	/**
	 * @return The basic type, eg. <code>array</code> for
	 *         <code>array&lt;string&gt;</code>.
	 */
	public ILangObjectClass getBasicLangClass();

	/**
	 * @return The number of generics of this type. For example an
	 *         <code>array&lt;string&gt;</code> would return <code>1</code>.
	 * @see #getGeneric(int)
	 */
	public int getGenericCount();

	/**
	 * For example an <code>map&lt;string,number&gt;</code> would return
	 * <code>string</code> for the index <code>0</code>, and <code>number</code>
	 * for the index <code>1</code>.
	 *
	 * @param i
	 *            Index of the generics type to return.
	 * @return The generics type.
	 * @throws ArrayIndexOutOfBoundsException
	 *             When the given index is outside the range
	 *             [0,{@link #getGenericCount()}).
	 * @see #getGenericCount()
	 */
	public IVariableType getGeneric(int i) throws ArrayIndexOutOfBoundsException;

	/**
	 * @param typpe
	 *            Type to check this type with.
	 * @return The merged type. When the types are not mutually compatible, it
	 *         is of type {@link ELangObjectClass#OBJECT}.
	 */
	public IVariableType union(IVariableType type);

	/**
	 * <p>
	 * Determines if the class represented by this type is either
	 * the same as, or is a super type, the class or interface represented by
	 * the specified type parameter. It returns true if so; otherwise it returns
	 * false.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the
	 * specified type parameter can be converted to this type.
	 * </p>
	 * @param type Type to check.
	 */
	public boolean isAssignableFrom(IVariableType type);

	/**
	 * @return Whether this object support iteration via
	 *         <code>for (variable : thisObject) { ... } </code>
	 */
	public boolean isIterable();

	/**
	 * @return The variable type of the items when iterating. Eg. <code>string</code> for <code>array&lt;string&gt;</code>.
	 */
	public IVariableType getIterableItemType();

	/**
	 * Whether this type is an of the given class.
	 * Return <code>false</code> when it is a super or subtype.
	 * @param baseClass Class to check.
	 * @return Whether this is of the given class.
	 */
	public boolean isA(ILangObjectClass baseClass);

	/**
	 * @param flag Flag to check for.
	 * @return Whether the flag is set for this variable type.
	 */
	public boolean hasFlag(EVariableTypeFlag flag);

	public ImmutableCollection<EVariableTypeFlag> getFlags();

	/**
	 * Checks whether this type equals the given type, allowing generics
	 * that are <code>null</code> as a wildcard.
	 * <ul>
	 *   <li>string.equalsOverNull(number) => false</li>
	 *   <li>string.equalsOverNull(null) => true</li>
	 *   <li>null.equalsOverNull(string) => true</li>
	 *   <li>null.equalsOverNull(null) => true</li>
	 *   <li>array&lt;string&gt;.equalsOverNull(array&lt;number&gt;) => false</li>
	 *   <li>array&lt;string&gt;.equalsOverNull(array&lt;null&gt;) => true</li>
	 *   <li>method&lt;string, null&gt;.equalsOverNull(array&lt;string&gt;) => false</li>
	 * </ul>
	 * @param thatType Type to check this type against.
	 * @return Whether this is the same as the given type, allowing for wildcards.
	 */
	public boolean equalsOverNull(IVariableType thatType);

	/**
	 * @param superClass Super class to convert this type to.
	 * @return This type in terms of the super class.
	 */
	public IVariableType upconvert(ILangObjectClass superClass);

	/**
	 * A comparator for objects of {@link IVariableType}, supporting <code>null</code>.
	 * <code>null</code> is sorted first. Sorts similar to string, with the
	 * {@link #getBasicLangClass()} corresponding to the {@link String}'s first character;
	 * and all the {@link #getGeneric(int)} types to the next characters.
	 * The types themselves are compared via {@link ELangObjectClass#id}.
	 */
	public static Comparator<IVariableType> COMPARATOR = new Comparator<IVariableType>() {
		@Override
		public int compare(@Nullable final IVariableType o1, @Nullable final IVariableType o2) {
			if (o1 == o2)
				return 0;
			if (o1 == null)
				return -1;
			if (o2 == null)
				return 1;
			final int l1 = o1.getBasicLangClass().getClassId().intValue();
			final int l2 = o2.getBasicLangClass().getClassId().intValue();
			if (l1 != l2)
				return l1 - l2;
			final int s = Math.min(o1.getGenericCount(), o2.getGenericCount());
			for (int i = 0; i < s; ++i) {
				final int res = compare(o1.getGeneric(i), o2.getGeneric(i));
				if (res != 0)
					return res;
			}
			return o1.getGenericCount() - o2.getGenericCount();
		}
	};
}