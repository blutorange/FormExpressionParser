package de.xima.fc.form.expression.visitor;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabelled;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class DuplicateLabelException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ILabelled & Node> DuplicateLabelException(@Nonnull final T node) {
		super(NullUtil.stringFormat(CmnCnst.Error.DUPLICATE_LABEL, node.getLabel()), node);
		this.label = NullUtil.orEmpty(node.getLabel());
	}
	@Nonnull public final String label;
}
