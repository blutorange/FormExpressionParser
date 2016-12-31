package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabeled;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class DuplicateLabelException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ILabeled & Node> DuplicateLabelException(final T node) {
		super(NullUtil.messageFormat(CmnCnst.Error.DUPLICATE_LABEL, node.getLabel()), node);
		this.label = NullUtil.orEmpty(node.getLabel());
	}
	public final String label;
}