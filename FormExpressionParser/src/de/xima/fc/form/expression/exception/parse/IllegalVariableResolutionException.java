package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;

public class IllegalVariableResolutionException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableResolutionException(@Nonnull final String message,
			@Nonnull final ISourceResolvable resolvable, @Nonnull final Node node) {
		super(message, node);
		this.variableName = resolvable.getVariableName();
	}

	@Nonnull
	public final String variableName;
}