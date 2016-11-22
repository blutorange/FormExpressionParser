package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class HeaderAssignmentNotCompileTimeConstantException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public HeaderAssignmentNotCompileTimeConstantException(@Nullable final String scope,
			@Nonnull final String variableName, @Nonnull final Node node) {
		super(NullUtil.format(CmnCnst.Error.HEADER_ASSIGNMENT_NOT_COMPILE_TIME_CONSTANT,
				formatName(scope, variableName)), node);
	}

	@Nonnull
	private static String formatName(@Nullable final String scope, @Nonnull final String variableName) {
		return scope == null ? variableName : scope + CmnCnst.Syntax.SCOPE_SEPARATOR + variableName;
	}
}