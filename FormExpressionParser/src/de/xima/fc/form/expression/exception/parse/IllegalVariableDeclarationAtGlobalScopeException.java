package de.xima.fc.form.expression.exception.parse;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalVariableDeclarationAtGlobalScopeException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ISourceResolvable & Node> IllegalVariableDeclarationAtGlobalScopeException(final T node) {
		super(NullUtil.stringFormat(CmnCnst.Error.VARIABLE_DECLARATION_AT_GLOBAL_SCOPE, node.getVariableName()), node);
		name = node.getVariableName();
	}
	public final String name;
}
