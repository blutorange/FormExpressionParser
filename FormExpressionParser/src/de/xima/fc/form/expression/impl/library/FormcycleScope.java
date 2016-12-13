package de.xima.fc.form.expression.impl.library;
import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.warning.MissingFormFieldWarning;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum FormcycleScope implements ILibraryScope<Formcycle>, ILibraryScopeContractFactory<Formcycle> {
	FORM_FIELD {
		@Override
		public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec) {
			String value = formcycle.getByAlias(name);
			if (value == null)
				value = formcycle.getByName(name);
			if (value == null) {
				if (ec.getTracer().isWarningsEnabled())
					ec.getTracer().appendWarning(new MissingFormFieldWarning(name, ec));
				return StringLangObject.getEmptyInstance();
			}
			return StringLangObject.create(value);
		}

		// Restrict to valid form field names, letters and underscores.
		@Override
		public boolean isProviding(final String variableName) {
			return CmnCnst.PATTERN_FORM_FIELD_NAME.matcher(variableName).matches();
		}

		@Override
		public String getScopeName() {
			return CustomScope.FORM_FIELD;
		}

		@Override
		public ILibraryScope<Formcycle> makeScope() {
			return this;
		}

		@Override
		public IVariableType getVariableType(final String variableName) {
			return SimpleVariableType.STRING;
		}
	};

	@Override
	public abstract ALangObject fetch(@Nonnull String name, @Nonnull Formcycle formcycle,
			@Nonnull final IEvaluationContext ec) throws EvaluationException;

	@Override
	public EVariableSource getSource() {
		return EVariableSource.EXTERNAL_CONTEXT;
	}
}