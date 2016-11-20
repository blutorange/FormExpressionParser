package de.xima.fc.form.expression.impl.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EScopeSource;
import de.xima.fc.form.expression.iface.context.IParametrizedCustomScope;
import de.xima.fc.form.expression.iface.parse.IScopeInfo;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext.Formcycle;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

public enum FormcycleScope implements IParametrizedCustomScope<Formcycle>, IScopeInfo {
	FORM_FIELD {
		@Override
		public ALangObject fetch(final String name, final Formcycle formcycle) {
			@Nullable final String value = formcycle.getByAlias(name);
			if (value != null) return StringLangObject.create(value);
			return StringLangObject.create(formcycle.getByName(name));
		}
		@Override
		public boolean isProviding(final String variableName) {
			return true;
		}
		@Override
		public String getScopeName() {
			return CustomScope.FORM_FIELD;
		}
	};
	@Override
	public abstract ALangObject fetch(@Nonnull String name, @Nonnull Formcycle formcycle);
	@Override
	public EScopeSource getSource() {
		return EScopeSource.EXTERNAL_CONTEXT;
	}
}