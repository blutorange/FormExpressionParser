package de.xima.fc.form.expression.iface.parse;

import java.util.Comparator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;

@Immutable
@ParametersAreNonnullByDefault
public interface IVariableType {
	public boolean equalsType(IVariableType other);

	/**
	 * @return The basic type, eg. <code>array</code> for
	 *         <code>array&lt;string&gt;</code>.
	 */
	public ELangObjectType getBasicLangType();

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
	 *         is of type {@link ELangObjectType#OBJECT}.
	 */
	public IVariableType union(IVariableType type);

	/**
	 * <p>
	 * Determines if the class or interface represented by this type is either
	 * the same as, or is a super type, the class or interface represented by
	 * the specified type parameter. It returns true if so; otherwise it returns
	 * false.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the
	 * specified type parameter can be converted to this type.
	 * </p>
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
	 * A comparator for objects of {@link IVariableType}, supporting <code>null</code>.
	 * <code>null</code> is sorted first. Sorts similar to string, with the
	 * {@link #getBasicLangType()} corresponding to the {@link String}'s first character;
	 * and all the {@link #getGeneric(int)} types to the next characters.
	 * The types themselves are compared via {@link ELangObjectType#order}.
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
			final int l1 = o1.getBasicLangType().order;
			final int l2 = o2.getBasicLangType().order;
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