package de.xima.fc.form.expression.iface.parse;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.ELangObjectType;

@Immutable
public interface IVariableType {
	public boolean equalsType(@Nonnull IVariableType other);
	/**
	 * @return The basic type, eg. <code>array</code> for <code>array&lt;string&gt;</code>.
	 */
	@Nonnull
	public ELangObjectType getBasicLangType();
	public int getGenericCount();
	@Nonnull
	public IVariableType getGeneric(int i) throws ArrayIndexOutOfBoundsException;
	/**
	 * @param typpe Type to check this type with.
	 * @return The merged type. When the types are not mutually compatible, it is of type {@link ELangObjectType#OBJECT}.
	 */
	@Nonnull
	public IVariableType union(@Nonnull IVariableType type);
	
	public static Comparator<IVariableType> COMPARATOR = new Comparator<IVariableType>() {
		@Override
		public int compare(final IVariableType o1, final IVariableType o2) {
			if (o1 == o2)
				return 0;
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