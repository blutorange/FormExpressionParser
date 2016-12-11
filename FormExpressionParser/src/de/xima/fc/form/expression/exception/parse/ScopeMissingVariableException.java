package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class ScopeMissingVariableException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public ScopeMissingVariableException(final String scope, final String name, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.SCOPE_MISSING_VARIABLE, scope, name), node);
		this.name = name;
		this.scope = scope;

	}

	public final String name;
	public final String scope;
}