package de.xima.fc.form.expression.impl.variable;

import java.io.Serializable;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;

public class GenericSourceResolvable implements ISourceResolvable, Serializable {
	private static final long serialVersionUID = 1L;
	@Nonnull
	private final String name;
	private int source = EVariableSource.ID_UNRESOLVED;
	public GenericSourceResolvable(@Nonnull final String name) {
		this.name = name;
	}

	@Override
	public void resolveSource(final int source) {
		if (this.source == EVariableSource.ID_UNRESOLVED)
			this.source = source;
	}
	
	@Override
	public int getSource() {
		return source;
	}
	@Override
	public boolean isResolved() {
		return source != EVariableSource.ID_UNRESOLVED;
	}
	@Override
	public String getVariableName() {
		return name;
	}
}