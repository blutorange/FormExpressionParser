package de.xima.fc.form.expression.impl.scope;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.VariableNotDefinedException;
import de.xima.fc.form.expression.iface.context.ICustomScope;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;

public enum DummyScope implements ICustomScope, IScopeInfo {
	INSTANCE;
	@Override
	public boolean isProviding(final String variableName) {
		return false;
	}
	@Override
	public String getScopeName() {
		return CmnCnst.NonnullConstant.STRING_EMPTY;
	}
	@Override
	public ALangObject fetch(@Nonnull final String variableName, @Nonnull final IEvaluationContext ec) throws EvaluationException {
		throw new VariableNotDefinedException(variableName, ec);
	}
	@Override
	public EVariableSource getSource() {
		return EVariableSource.LIBRARY;
	}
}