package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IArgumentResolvableNode;

public interface IFunctionNode extends IArgumentResolvableNode, IVariableTyped, ICallResolvable {
	boolean hasReturnType();
	@Nonnull public Node getReturnTypeNode();
}