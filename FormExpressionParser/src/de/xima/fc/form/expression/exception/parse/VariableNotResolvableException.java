package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IScopedSourceResolvable;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class VariableNotResolvableException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public VariableNotResolvableException(@Nullable final String scope, final String name, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.VARIABLE_NOT_RESOLVABLE, varToString(scope, name)), node);
		this.name = name;
		this.scope = scope;
	}

	public VariableNotResolvableException(final ASTVariableNode node) {
		this(node.getScope(), node.getVariableName(), node);
	}

	public <T extends IScopedSourceResolvable & Node> VariableNotResolvableException(final T node) {
		this(node.getScope(), node.getVariableName(), node);
	}
	
	public VariableNotResolvableException(final ISourceResolvable resolvable, final Node node) {
		this(null, resolvable.getVariableName(), node);
	}

	public <T extends ISourceResolvable & Node> VariableNotResolvableException(final T node) {
		this(null, node.getVariableName(), node);
	}

	private static String varToString(@Nullable final String scope, final String name) {
		if (scope != null)
			return scope + "::" + name;
		return name;
	}

	public final String name;
	@Nullable public final String scope;
}