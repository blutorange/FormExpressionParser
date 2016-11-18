package de.xima.fc.form.expression.exception;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.NullUtil;

public class NoSuchScopeException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchScopeException(@Nonnull final String scope, @Nonnull final Node node) {
		super(NullUtil.format("No such scope %s.", scope), node);
		this.scope = scope;
	}
	public final String scope;
}
