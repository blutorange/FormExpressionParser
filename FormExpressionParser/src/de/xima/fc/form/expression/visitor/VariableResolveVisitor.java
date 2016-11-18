package de.xima.fc.form.expression.visitor;

import de.xima.fc.form.expression.exception.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;

public class VariableResolveVisitor extends FormExpressionVoidVoidVisitorAdapter<SemanticsException> {

	public static void resolve(final Node node, final IScopeDefinitions scopeDef) {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}
}
