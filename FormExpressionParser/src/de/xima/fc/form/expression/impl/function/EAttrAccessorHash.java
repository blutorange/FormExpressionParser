package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorHash implements IFunction<HashLangObject> {
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

	private final FunctionLangObject impl;
	private final boolean evalImmediately;
	private final String[] argList;

	private EAttrAccessorHash(final Impl impl) {
		this.impl = FunctionLangObject.create(impl);
		argList = impl.getDeclaredArgumentList();
		evalImmediately = argList.length == 0;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		if (!evalImmediately)
			return impl;
		return impl.functionValue().evaluate(ec, thisContext, args);
	}

	@Override
	public String getDeclaredName() {
		return toString();
	}

	@Override
	public String[] getDeclaredArgumentList() {
		return argList;
	}

	@Override
	public Type getThisContextType() {
		return Type.HASH;
	}

	@Override
	public Node getNode() {
		return null;
	}

	private static enum Impl implements IFunction<HashLangObject> {
		get("key") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.hashValue().get(args.length == 0 ? NullLangObject.getInstance() : args[0]);
			}
		},
		contains("key") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject
						.create(thisContext.contains(args.length == 0 ? NullLangObject.getInstance() : args[0]));
			}
		},
		length() {
			@Override
			public ALangObject evaluate(IEvaluationContext ec, HashLangObject thisContext, ALangObject... args)
					throws EvaluationException {
				return NumberLangObject.create(thisContext.length());
			}
		}
		;

		private String[] argList;

		private Impl(String... argList) {
			this.argList = argList;
		}

		@Override
		public String[] getDeclaredArgumentList() {
			return argList;
		}

		@Override
		public String getDeclaredName() {
			return toString();
		}

		@Override
		public Type getThisContextType() {
			return Type.HASH;
		}

		@Override
		public Node getNode() {
			return null;
		}

		@Override
		public abstract ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext,
				final ALangObject... args) throws EvaluationException;
	}
}