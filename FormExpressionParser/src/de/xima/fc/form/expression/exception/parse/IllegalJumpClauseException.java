package de.xima.fc.form.expression.exception.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EJump;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;

public class IllegalJumpClauseException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public IllegalJumpClauseException(@Nonnull final EJump jump, @Nonnull final Node node) {
		super(getMessage(jump), node);
		this.jump = jump;
	}
	@Nonnull
	private static String getMessage(@Nonnull final EJump jump) {
		switch(jump) {
		case BREAK:
			return CmnCnst.Error.BREAK_CLAUSE;
		case CONTINUE:
			return CmnCnst.Error.CONTINUE_CLAUSE;
		case RETURN:
			return CmnCnst.Error.RETURN_CLAUSE;
		default:
			return "Jump clause used without label or enclosing function.";
		}
	}
	public final EJump jump;
}
