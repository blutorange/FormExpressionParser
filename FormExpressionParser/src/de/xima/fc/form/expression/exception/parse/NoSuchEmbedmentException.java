package de.xima.fc.form.expression.exception.parse;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class NoSuchEmbedmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchEmbedmentException(final String embedment, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.UNDEFINED_EMBEDMENT, embedment), node);
		this.embedment = embedment;
	}
	final String embedment;
}