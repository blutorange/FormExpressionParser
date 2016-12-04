package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;

public interface IVariableTyped {
	boolean hasType();
	@Nonnull public Node getTypeNode();
}
