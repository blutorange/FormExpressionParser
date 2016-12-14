package de.xima.fc.form.expression.impl.function;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IAttrAccessorFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum EAttrAccessorHash implements IAttrAccessorFunction<HashLangObject> {
	/**
	 * @param key
	 *            {@link ALangObject} The key. When not specified,
	 *            {@link NullLangObject} is assumed.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 * @NullLangObject When the map does not contain any value for the key, or
	 *                 the key is mapped to {@link NullLangObject}. Use
	 *                 {@link EAttrAccessorHash#contains} to check.
	 */
	get(Impl.get),
	/**
	 * @param key
	 *            {@link ALangObject}. The key to check for. When not specified,
	 *            {@link NullLangObject} is assumed.
	 * @return {@link BooleanLangObject}. Whether this hash contains a mapping
	 *         for the given key.
	 */
	contains(Impl.contains),
	/**
	 * @return {@link NumberLangObject}. The number of entries in this hash, >=0.
	 */
	length(Impl.length),
	;

	private final FunctionLangObject func;
	private final Impl impl;
	private final boolean deferEvaluation;

	private EAttrAccessorHash(final Impl impl) {
		this.func = FunctionLangObject.create(impl);
		this.impl = impl;
		deferEvaluation = impl.getDeclaredArgumentCount() != 0 || impl.hasVarArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		return deferEvaluation ? func : func.functionValue().evaluate(ec, thisContext, args);
	}

	@SuppressWarnings("null")
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
		return ELangObjectType.HASH;
	}

	@Override
	public boolean hasVarArgs() {
		return impl.hasVarArgs;
	}

	private static enum Impl implements IAttrAccessorFunction<HashLangObject> {
		get(false, "key") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.get(args.length == 0 ? NullLangObject.getInstance() : args[0]);
			}
		},
		contains(false, "key") { //$NON-NLS-1$
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject
						.create(thisContext.contains(args.length == 0 ? NullLangObject.getInstance() : args[0]));
			}
		},
		length(false) {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
			}
		}
		;

		private String[] argList;
		private boolean hasVarArgs;

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

		@SuppressWarnings("null")
		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public ILangObjectClass getThisContextType() {
			return ELangObjectType.HASH;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}