package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabeled;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class IllegalJumpClauseException extends SemanticsException {
	private static final long serialVersionUID = 1L;

	public <T extends ILabeled & Node> IllegalJumpClauseException(final EJump jump, final T node) {
		this(jump, node.getLabel(), node);
	}

	public IllegalJumpClauseException(final EJump jump, final ASTReturnClauseNode node) {
		this(jump, null, node);
	}
	
	public IllegalJumpClauseException(final EJump jump, @Nullable final String label, final Node node) {
		super(getMessage(jump, label), node);
		this.jump = jump;
	}

	private static String getMessage(final EJump jump, @Nullable final String label) {
		switch (jump) {
		case BREAK:
			return NullUtil.messageFormat(CmnCnst.Error.BREAK_CLAUSE, NullUtil.or(label, CmnCnst.Name.EMPTY_LABEL));
		case CONTINUE:
			return NullUtil.messageFormat(CmnCnst.Error.CONTINUE_CLAUSE, NullUtil.or(label, CmnCnst.Name.EMPTY_LABEL));
		case RETURN:
			return CmnCnst.Error.RETURN_CLAUSE;
		case NONE:
		default:
			return CmnCnst.Error.JUMP_WITHOUT_MATCHING_LABEL_OR_FUNCTION;
		}
	}

	public final EJump jump;
}