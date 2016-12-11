package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchScopeException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public NoSuchScopeException(final String scope, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.NO_SUCH_SCOPE, scope), node);
		this.scope = scope;
	}

	public final String scope;
}
