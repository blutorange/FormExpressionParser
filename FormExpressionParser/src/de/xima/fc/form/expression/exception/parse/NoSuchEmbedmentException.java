package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class NoSuchEmbedmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public NoSuchEmbedmentException(@Nonnull final String embedment, @Nonnull final Node node) {
		super(NullUtil.stringFormat(CmnCnst.Error.UNDEFINED_EMBEDMENT, embedment), node);
		this.embedment = embedment;
	}
	@Nonnull final String embedment;
}
