package de.xima.fc.form.expression.exception.evaluation;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class UnresolvedVariableSourceException extends UncatchableEvaluationException {
	private static final long serialVersionUID = 1L;

	public UnresolvedVariableSourceException(@Nullable final String scope, final String name,
			final IEvaluationContext ec) {
		super(ec, format(scope, name));
		this.scope = scope;
		this.name = name;
	}

	private static String format(@Nullable final String scope, final String name) {
		if (scope == null)
			return NullUtil.messageFormat(CmnCnst.Error.UNSCOPED_VARIABLE_NOT_RESOLVED, name);
		return NullUtil.messageFormat(CmnCnst.Error.SCOPED_VARIABLE_NOT_RESOLVED, scope, name);
	}

	@Nullable
	public final String scope;
	public final String name;
}