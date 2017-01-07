package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IDotAccessorFunction;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public enum EDotAccessorString implements IDotAccessorFunction<StringLangObject> {
	/**
	 * Uses the the English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	toUpperCase(Impl.toUpperCase),
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. When the argument
	 *            is {@link NullLangObject}, defaults to the English-like
	 *            {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	toLocaleUpperCase(Impl.toLocaleUpperCase),
	/**
	 * @param locale Uses the English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The lower-case version of the string.
	 */
	toLowerCase(Impl.toLowerCase),
	/**
	 * @param locale
	 *            {@link StringLangObject} (optional). The (IETF BCP 47) name of
	 *            the locale to be used for the conversion. When the argument
	 *            is {@link NullLangObject}, defaults to the English-like
	 *            {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The lower-case version of the string.
	 */
	toLocaleLowerCase(Impl.toLocaleLowerCase),
	/**
	 * @return {@link NumberLangObject}. The length of this string, >=0.
	 */
	length(Impl.length),
	/**
	 * @param patternToCheck <code>regex</code>
	 * @return <code>boolean</code> Whether this string matches the regex.
	 */
	matches(Impl.matches),
	;

	@Nullable private FunctionLangObject func;
	private final Impl impl;

	private EDotAccessorString(final Impl impl) {
		this.impl = impl;
		func = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs ? null : FunctionLangObject.createWithoutClosure(impl);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (func != null)
			return func.bind(thisContext, ec).evaluate(ec);
		return FunctionLangObject.createWithoutClosure(impl).bind(thisContext, ec);
	}

	@Override
	public String getDeclaredName() {
		return toString();
	}

	@SuppressWarnings("null")
	@Override
	public String getDeclaredArgument(final int i) {
		return impl.argList[i];
	}

	@Override
	public int getDeclaredArgumentCount() {
		return impl.argList.length;
	}

	@Override
	public ILangObjectClass getThisContextType() {
		return ELangObjectClass.STRING;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	@Override
	public ILangObjectClass getReturnClass() {
		return impl.getReturnClass();
	}

	@Override
	public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
		return impl.getReturnType(thisContext, dotGenerics);
	}

	@Override
	public boolean supportsGenerics(final IVariableType[] dotGenerics) {
		return impl.supportsGenerics(dotGenerics);
	}

	private static enum Impl implements IDotAccessorFunction<StringLangObject> {
		toLocaleUpperCase(false, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final Locale locale = args.length > 0 && !args[0].isNull()
						? Locale.forLanguageTag(args[0].coerceString(ec).stringValue()) : Locale.ROOT;
				return thisContext.toUpperCase(locale);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				// string.toLocaleUpperCase(locale) => string
				return GenericVariableType.forSimpleFunction(SimpleVariableType.STRING, SimpleVariableType.STRING);
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		toLocaleLowerCase(false, "locale") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final Locale locale = args.length > 0 && !args[0].isNull()
						? Locale.forLanguageTag(args[0].coerceString(ec).stringValue()) : Locale.ROOT;
				return thisContext.toLowerCase(locale);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forSimpleFunction(SimpleVariableType.STRING, SimpleVariableType.STRING);
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		toUpperCase(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toUpperCase(Locale.ROOT);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.STRING;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.STRING;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		toLowerCase(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.toLowerCase(Locale.ROOT);
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.STRING;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.STRING;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.stringValue().length());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return SimpleVariableType.NUMBER;
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.NUMBER;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		},
		matches(false, "patternToCheck") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				final RegexLangObject r = args.length > 0 ? args[0].coerceRegex(ec) : RegexLangObject.getUnmatchableInstance();
				return BooleanLangObject.create(r.patternValue().matcher(thisContext.stringValue()).matches());
			}

			@Override
			public IVariableType getReturnType(final IVariableType thisContext, final IVariableType[] dotGenerics) {
				return GenericVariableType.forSimpleFunction(SimpleVariableType.BOOLEAN, SimpleVariableType.REGEX);
			}

			@Override
			public ILangObjectClass getReturnClass() {
				return ELangObjectClass.FUNCTION;
			}

			@Override
			public boolean supportsGenerics(final IVariableType[] dotGenerics) {
				return dotGenerics.length == 0;
			}
		}
		;

		protected String[] argList;
		protected boolean hasVarArgs;

		private Impl(final boolean hasVarArgs, final String... argList) {
			NullUtil.checkItemsNotNull(argList);
			this.argList = argList;
			this.hasVarArgs = hasVarArgs;
		}

		@Override
		public boolean hasVarArgs() {
			return hasVarArgs;
		}


		@SuppressWarnings("null")
		@Override
		public String getDeclaredArgument(final int i) {
			return argList[i];
		}

		@Override
		public int getDeclaredArgumentCount() {
			return argList.length;
		}

		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ILangObjectClass getThisContextType() {
			return ELangObjectClass.STRING;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}
