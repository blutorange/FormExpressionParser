package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class VariableNotResolvableException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public VariableNotResolvableException(@Nullable final String scope, @Nonnull final String name, @Nonnull final Node node) {
		super(NullUtil.stringFormat(CmnCnst.Error.VARIABLE_NOT_RESOLVABLE, varToString(scope,name)), node);
		this.name = name;
		this.scope = scope;
		
	}
	public VariableNotResolvableException(@Nonnull final ASTVariableNode node) {
		this(node.getScope(), node.getVariableName(), node);
	}

	@Nonnull
	private static String varToString(@Nullable final String scope, @Nonnull final String name) {
		if (scope != null)
			return scope + "::" + name;
		return name;
	}

	public final String name;
	public final String scope;

}
