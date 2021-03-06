package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class UnhandledNodeTypeException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public UnhandledNodeTypeException(final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.UNHANDLED_NODE_TYPE, Integer.valueOf(node.jjtGetNodeId()),
				node.getClass().getCanonicalName()), node);
		this.nodeId = node.jjtGetNodeId();
		this.nodeClass = NullUtil.checkNotNull(node.getClass());
	}

	public final int nodeId;
	public final Class<? extends Node> nodeClass;
}