package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;

@ParametersAreNonnullByDefault
public interface IArgumentResolvableNode extends IArgumentResolvable, Node {
	public Node getArgumentNode(final int i);
}