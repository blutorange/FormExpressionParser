package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.NullUtil;

public class MissingRequireScopeStatementException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public MissingRequireScopeStatementException(@Nonnull final String scope, @Nonnull final Node node) {
		super(NullUtil.stringFormat("Scope %s is provided by the evaluation context, but require scope statement is missing.", scope), node);
		this.scope = scope;
	}
	public final String scope;
}
