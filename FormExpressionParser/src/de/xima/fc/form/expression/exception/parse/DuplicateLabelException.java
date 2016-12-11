package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabelled;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class DuplicateLabelException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ILabelled & Node> DuplicateLabelException(final T node) {
		super(NullUtil.messageFormat(CmnCnst.Error.DUPLICATE_LABEL, node.getLabel()), node);
		this.label = NullUtil.orEmpty(node.getLabel());
	}
	public final String label;
}