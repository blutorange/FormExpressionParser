package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class MissingVariableTypeDeclarationException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public <T extends Node & ISourceResolvable> MissingVariableTypeDeclarationException(final T node) {
		this(node, node);
	}

	public MissingVariableTypeDeclarationException(final ISourceResolvable resolvable, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.MISSING_TYPE_DECLARATION, resolvable.getVariableName()), node);
		this.variableName = resolvable.getVariableName();
	}

	public final String variableName;
}