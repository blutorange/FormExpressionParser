package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalVariableScopeResolutionException extends IllegalVariableResolutionException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableScopeResolutionException(@Nonnull final IScopedSourceResolvable resolvable, @Nonnull final Node node,
			@Nonnull final String newScope) {
		super(NullUtil.format(CmnCnst.Error.VARIABLE_SCOPE_ALREADY_RESOLVED, resolvable.getVariableName(), newScope,
				resolvable.getScope()), resolvable, node);
		this.newScope = newScope;
		this.oldScope = NullUtil.orEmpty(resolvable.getScope());
	}

	@Nonnull
	public final String oldScope;
	@Nonnull
	public final String newScope;
}