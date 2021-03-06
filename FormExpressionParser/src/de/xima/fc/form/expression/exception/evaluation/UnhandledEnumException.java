package de.xima.fc.form.expression.exception.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class UnhandledEnumException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;
	public UnhandledEnumException(final Enum<?> unhandledEnum, final IEvaluationContext ec) {
		super(ec, NullUtil.messageFormat(CmnCnst.Error.UNHANDLED_ENUM, unhandledEnum));
		this.unhandledEnum = unhandledEnum;
	}
	public final Enum<?> unhandledEnum;
}