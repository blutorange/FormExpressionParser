package de.xima.fc.form.expression.impl.variable;

import java.util.List;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class GenericVariableType implements IVariableType {

	@Nonnull
	private final ELangObjectType type;
	@Nonnull
	private final IVariableType[] list;
	
	public GenericVariableType(@Nonnull final ELangObjectType type, @Nonnull final List<IVariableType> list) {
		if (!type.isCompound()) {
			throw new IllegalStateException(NullUtil.format(CmnCnst.Error.NOT_A_COMPOUND_TYPE, type));
		}
		this.type = type;
		this.list = NullUtil.checkNotNull(list.toArray(new IVariableType[list.size()]));
	}

	@Override
	public boolean equalsType(final IVariableType other) {
		if (type != other.getBasicLangType())
			return false;
		for (int i = other.getGenericCount(); i --> 0;)
			if (!list[i].equalsType(other.getGeneric(i)))
				return false;
		return true;
	}

	@Override
	public ELangObjectType getBasicLangType() {
		return type;
	}

	@Override
	public int getGenericCount() {
		return list.length;
	}

	@SuppressWarnings("null")
	@Override
	public IVariableType getGeneric(final int i) throws ArrayIndexOutOfBoundsException {
		return list[i];
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(type.toString());
		sb.append('<');
		for (int i = 0; i < list.length; ++i) {
			sb.append(list[i].toString());
			if (list.length != 1 && i < list.length -1)
				sb.append(',');
		}
		sb.append('>');
		return sb.toString();
	}
}
