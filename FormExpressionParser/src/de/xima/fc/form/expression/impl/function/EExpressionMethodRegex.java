package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;

public enum EExpressionMethodRegex implements IMethod2Function<RegexLangObject> {
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.LOGICAL_EQUALITY),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is the same object as the argument.
	 */
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.OBJECT_EQUALITY),
	;
	private final EMethod method;
	private final IFunction<RegexLangObject> function;

	private EExpressionMethodRegex(final EMethod method, final IFunction<RegexLangObject> function) {
		this.method = method;
		this.function = function;
	}

	@Override
	public EMethod getMethod() {
		return method;
	}

	@Override
	public IFunction<RegexLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<RegexLangObject> {
		LOGICAL_EQUALITY(null, "comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}},
		OBJECT_EQUALITY(null, "comparand"){
				@Override
				public ALangObject evaluate(final IEvaluationContext ec, final RegexLangObject thisContext, final ALangObject... args)
						throws EvaluationException {
					return BooleanLangObject.create(thisContext == args[0]);
				}},
		;

		private final String[] argList;
		private String optionalArgumentsName;

		private Impl(final String optArg, final String... argList) {
			this.argList = argList;
			this.optionalArgumentsName = optArg;
		}

		@Override
		public String getVarArgsName() {
			return optionalArgumentsName;
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
			return Type.REGEX;
		}

		@Override
		public Node getNode() {
			return null;
		}
	}
}
