package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.node.ASTPropertyExpressionNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IllegalGenericTypeInDotPropertyAssignmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalGenericTypeInDotPropertyAssignmentException(final String property,
			final ASTPropertyExpressionNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_GENERIC_IN_DOT_PROPERTY_ASSIGNMENT, property), node);
		this.property = property;
	}

	public final String property;
}