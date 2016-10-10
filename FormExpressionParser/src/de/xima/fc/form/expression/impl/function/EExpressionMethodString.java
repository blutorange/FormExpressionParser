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
import de.xima.fc.form.expression.object.StringLangObject;

public enum EExpressionMethodString implements IMethod2Function<StringLangObject> {
	/**
	 * @param stringToJoin {@link StringLangObject} The string to be concatenated to this string.
	 * @return {@link StringLangObject} The concatenation between this string and the argument.
	 */
	PLUS(EMethod.PLUS, Impl.PLUS),
	ANGLE_OPEN(EMethod.ANGLE_OPEN, Impl.LESSER),
	ANGLE_CLOSE(EMethod.ANGLE_CLOSE, Impl.GREATER),
	ANGLE_OPEN_EQUAL(EMethod.ANGLE_OPEN_EQUAL, Impl.LESSER_OR_EQUAL),
	ANGLE_CLOSE_EQUAL(EMethod.ANGLE_CLOSE_EQUAL, Impl.GREATER_OR_EQUAL),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is of the same {@link Type} as the argument and is logically equivalent.
	 */
	DOUBLE_EQUAL(EMethod.DOUBLE_EQUAL, Impl.LOGICAL_IDENTITY),
	/**
	 * @param comparand {@link ALangObject}. Object to compare this object to.
	 * @return {@link BooleanLangObject}. True iff this object is the same object as the argument.
	 */
	TRIPLE_EQUAL(EMethod.TRIPLE_EQUAL, Impl.OBJECT_IDENTITY),

	;
	private final EMethod method;
	private final IFunction<StringLangObject> function;
	private EExpressionMethodString(final EMethod method, final IFunction<StringLangObject> function) {
		this.method = method;
		this.function = function;
	}
	@Override
	public EMethod getMethod() {
		return method;
	}
	@Override
	public IFunction<StringLangObject> getFunction() {
		return function;
	}

	private static enum Impl implements IFunction<StringLangObject> {
		PLUS(null, "stringToJoin") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return thisContext.concat(args[0].coerceString(ec));
			}
		},
		LESSER(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject.create(thisContext.stringValue().compareTo(args[0].coerceString(ec).stringValue())<0);
			}
		},
		LESSER_OR_EQUAL(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject.create(thisContext.stringValue().compareTo(args[0].coerceString(ec).stringValue())<=0);
			}
		},
		GREATER(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject.create(thisContext.stringValue().compareTo(args[0].coerceString(ec).stringValue())>0);
			}
		},
		GREATER_OR_EQUAL(null, "comparand") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return BooleanLangObject.create(thisContext.stringValue().compareTo(args[0].coerceString(ec).stringValue())>=0);
			}
		},
		LOGICAL_IDENTITY(null, "comparand"){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return BooleanLangObject.create(thisContext.equals(args[0]));
			}},
		OBJECT_IDENTITY(null, "comparand"){
				@Override
				public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
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
			return Type.STRING;
		}
		@Override
		public Node getNode() {
			return null;
		}
	}
}
