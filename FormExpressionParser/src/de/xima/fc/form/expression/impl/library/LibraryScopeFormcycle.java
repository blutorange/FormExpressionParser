package de.xima.fc.form.expression.impl.library;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILibraryScope;
import de.xima.fc.form.expression.iface.parse.ILibraryScopeContractFactory;
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
public final class LibraryScopeFormcycle {
	private LibraryScopeFormcycle() {
	}

	public static ILibraryScopeContractFactory<Formcycle>[] getAll() {
		return FcImpl.values();
	}

	private static enum FcImpl implements ILibraryScope<Formcycle>, ILibraryScopeContractFactory<Formcycle> {
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

			@Nullable
			@Override
			public IVariableType getVariableType(final String variableName) {
				return SimpleVariableType.STRING;
			}
		},
		//Dummy for illustration, provides the action::RESULT[index].key object
		FC_SYSTEM {
			private final IVariableType type = new GenericVariableType(ELangObjectType.ARRAY,
					new GenericVariableType(ELangObjectType.HASH, SimpleVariableType.STRING,
							SimpleVariableType.STRING));
			@Override
			public String getScopeName() {
				return CustomScope.FC_SYSTEM;
			}

			@Override
			public ILibraryScope<Formcycle> makeScope() {
				return this;
			}

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
			public ALangObject fetch(final String name, final Formcycle formcycle, final IEvaluationContext ec)
					throws EvaluationException {
				return formcycle.getActionResultAsLangObject();
			}
		}
		;

		@Override
		public abstract ALangObject fetch(@Nonnull String name, @Nonnull Formcycle formcycle,
				@Nonnull final IEvaluationContext ec) throws EvaluationException;

		@Override
		public EVariableSource getSource() {
			return EVariableSource.EXTERNAL_CONTEXT;
		}
	}
}