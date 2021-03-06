package de.xima.fc.form.expression.exception.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class UnhandledEnumException extends SemanticsException {
	private static final long serialVersionUID = 1L;
	public UnhandledEnumException(final Enum<?> unhandledEnum, final Node node) {
		super(NullUtil.messageFormat(CmnCnst.Error.UNHANDLED_ENUM, unhandledEnum), node);
		this.unhandledEnum = unhandledEnum;
	}
	public final Enum<?> unhandledEnum;
}