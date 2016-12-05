package de.xima.fc.form.expression.impl.scope;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IParametrizedCustomScope;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext.Formcycle;
import de.xima.fc.form.expression.impl.warning.MissingFormFieldWarning;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum FormcycleScope implements IParametrizedCustomScope<Formcycle>, IScopeInfo {
	FORM_FIELD {
		@Override
		public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec) {
			String value = formcycle.getByAlias(name);
			if (value == null) value = formcycle.getByName(name);
			if (value == null) {
				if (ec.getTracer().isWarningsEnabled())
					ec.getTracer().appendWarning(new MissingFormFieldWarning(name, ec));
				return StringLangObject.getEmptyInstance();
			}
			return StringLangObject.create(value);
		}
		// Forms fields could have any name.
		@Override
		public boolean isProviding(final String variableName) {
			return CmnCnst.PATTERN_FORM_FIELD_NAME.matcher(variableName).matches();
		}
		@Override
		public String getScopeName() {
			return CustomScope.FORM_FIELD;
		}
	};
	@Override
	public abstract ALangObject fetch(@Nonnull String name, @Nonnull Formcycle formcycle, @Nonnull final IEvaluationContext ec) throws EvaluationException;
	@Override
	public EVariableSource getSource() {
		return EVariableSource.EXTERNAL_CONTEXT;
	}
}