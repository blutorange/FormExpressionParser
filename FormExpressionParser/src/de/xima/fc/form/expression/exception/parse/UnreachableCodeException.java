package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class UnreachableCodeException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public UnreachableCodeException(@Nonnull final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.UNREACHABLE_CODE), node);
	}
}