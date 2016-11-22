package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.ILabelled;
import de.xima.fc.form.expression.node.ASTReturnClauseNode;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class IllegalJumpClauseException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public <T extends ILabelled & Node> IllegalJumpClauseException(@Nonnull final EJump jump, @Nonnull final T node) {
		super(getMessage(jump, node.getLabel()), node);
		this.jump = jump;
	}
	public IllegalJumpClauseException(@Nonnull final EJump jump, @Nonnull final ASTReturnClauseNode node) {
		super(getMessage(jump, null), node);
		this.jump = jump;
	}

	@Nonnull
	private static String getMessage(@Nonnull final EJump jump, @Nullable final String label) {
		switch(jump) {
		case BREAK:
			return NullUtil.format(CmnCnst.Error.BREAK_CLAUSE, NullUtil.orEmpty(label));
		case CONTINUE:
			return NullUtil.format(CmnCnst.Error.CONTINUE_CLAUSE, NullUtil.orEmpty(label));
		case RETURN:
			return CmnCnst.Error.RETURN_CLAUSE;
		default:
			return "Jump clause used without label or enclosing function.";
		}
	}
	public final EJump jump;
}
