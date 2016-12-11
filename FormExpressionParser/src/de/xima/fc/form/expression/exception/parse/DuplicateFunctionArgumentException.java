package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class DuplicateFunctionArgumentException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public DuplicateFunctionArgumentException(@Nonnull final String name, @Nonnull final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.DUPLICATE_FUNCTION_ARGUMENT, name), node);
		this.duplicateArgumentName = name;
	}
	public final String duplicateArgumentName;
}
