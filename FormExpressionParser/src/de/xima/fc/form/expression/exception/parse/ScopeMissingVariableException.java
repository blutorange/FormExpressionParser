package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ScopeMissingVariableException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public ScopeMissingVariableException(@Nonnull final String scope, @Nonnull final String name, @Nonnull final Node node) {
		super(NullUtil.stringFormat(CmnCnst.Error.SCOPE_MISSING_VARIABLE, scope, name), node);
		this.name = name;
		this.scope = scope;
		
	}

	public final String name;
	public final String scope;

}
