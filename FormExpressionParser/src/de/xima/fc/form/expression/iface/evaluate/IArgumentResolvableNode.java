package de.xima.fc.form.expression.iface.evaluate;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IArgumentResolvable;

@NonNullByDefault
public interface IArgumentResolvableNode extends IArgumentResolvable, Node {
	public Node getArgumentNode(final int i);
}