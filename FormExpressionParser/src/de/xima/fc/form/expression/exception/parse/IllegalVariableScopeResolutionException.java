package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IllegalVariableScopeResolutionException extends IllegalVariableResolutionException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableScopeResolutionException(final IScopedSourceResolvable resolvable, final Node node,
			final String newScope) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_SCOPE_ALREADY_RESOLVED, resolvable.getVariableName(),
				newScope, resolvable.getScope()), resolvable, node);
		this.newScope = newScope;
		this.oldScope = NullUtil.orEmpty(resolvable.getScope());
	}

	public final String oldScope;
	public final String newScope;
}