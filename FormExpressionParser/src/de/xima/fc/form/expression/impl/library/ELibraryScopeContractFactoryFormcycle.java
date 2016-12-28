package de.xima.fc.form.expression.impl.library;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.factory.ILibraryScopeContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.warning.MissingFormFieldWarning;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.CustomScope;

@ParametersAreNonnullByDefault
public enum ELibraryScopeContractFactoryFormcycle implements ILibraryScopeContractFactory<Formcycle> {
	FORM_FIELD(FetchImpl.FORM_FIELD) {
		// Restrict to valid form field names, letters and underscores.
		@Override
		public boolean isProviding(final String variableName) {
			return CmnCnst.PATTERN_FORM_FIELD_NAME.matcher(variableName).matches();
		}

		@Nullable
		@Override
		public IVariableType getVariableType(final String variableName) {
			return SimpleVariableType.STRING;
		}

		@Override
		public String getScopeName() {
			return CustomScope.FORM_FIELD;
		}
	},
	FC_SYSTEM(FetchImpl.FORM_FIELD) {
		private final IVariableType type = GenericVariableType.forArray(GenericVariableType.forHash(SimpleVariableType.STRING, SimpleVariableType.STRING));

		@Override
		public boolean isProviding(final String variableName) {
			return variableName.equals(CmnCnst.CustomScope.ACTION_VARIABLE_NAME_RESULT);
		}

		@Nullable
		@Override
		public IVariableType getVariableType(final String variableName) {
			return type;
		}

		@Override
		public String getScopeName() {
			return CustomScope.FC_SYSTEM;
		}
	},
	;

	protected final FetchImpl impl;

	private ELibraryScopeContractFactoryFormcycle(final FetchImpl impl) {
		this.impl = impl;
	}

	@Override
	public ILibraryScope<Formcycle> make() {
		return new LibImpl();
	}

	@Override
	public EVariableSource getSource() {
		return EVariableSource.EXTERNAL_CONTEXT;
	}

	private static enum FetchImpl {
		FORM_FIELD {
			@Override
			public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec) {
				String value = formcycle.getByAlias(name);
				if (value == null)
					value = formcycle.getByName(name);
				if (value != null)
					return StringLangObject.create(value);
				ec.getTracer().appendWarning(new MissingFormFieldWarning(name, ec));
				return StringLangObject.getEmptyInstance();
			}
		},
		// Dummy for illustration, provides the action::RESULT[index].key object
		FC_SYSTEM {
			@Override
			public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec)
					throws EvaluationException {
				return formcycle.getActionResultAsLangObject();
			}
		};

		public abstract ALangObject fetch(String name, Formcycle formcycle, final IEvaluationContext ec)
				throws EvaluationException;
	}

	private final class LibImpl implements ILibraryScope<Formcycle> {
		private final Map<String, ALangObject> map = new HashMap<>();
		protected LibImpl() {}
		@Override
		public void reset() {
			map.clear();
		}
		@Override
		public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec)
				throws EvaluationException {
			ALangObject o = map.get(name);
			if (o == null)
				map.put(name, o = impl.fetch(name, formcycle, ec));
			return o;
		}
	}
}