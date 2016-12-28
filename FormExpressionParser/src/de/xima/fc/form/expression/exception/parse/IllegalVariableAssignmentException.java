package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.node.ASTVariableNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IllegalVariableAssignmentException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public IllegalVariableAssignmentException(final ASTVariableNode node) {
		super(NullUtil.messageFormat(CmnCnst.Error.ASSIGNMENT_OF_UNASSIGNABLE_VARIABLE, node.getVariableName(),
				node.getSourceType()), node);
		this.sourceType = node.getSourceType();
		this.name = node.getVariableName();
	}

	public final EVariableSource sourceType;
	public final String name;
}