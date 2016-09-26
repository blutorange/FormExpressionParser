package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.IMethod2Function;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NamedFunctionUtils;

public enum EInstanceMethodString implements IMethod2Function<StringLangObject> {
	PLUS(EMethod.PLUS, Func.PLUS),
	;
	private final EMethod method;
	private final IFunction<StringLangObject> function;
	private EInstanceMethodString(final EMethod method, final IFunction<StringLangObject> function) {
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

	private static enum Func implements IFunction<StringLangObject> {
		//		upcase {
		//			@Override
		//			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
		//					final ALangObject... args) throws EvaluationException {
		//				Locale locale = Locale.ROOT;
		//				final StringLangObject l = NamedFunctionUtils.getArgOrNull(this, 0, args, StringLangObject.class, ec);
		//				locale = l == null ? Locale.ROOT : Locale.forLanguageTag(l.stringValue());
		//				return StringLangObject.create(thisContext.stringValue().toUpperCase(locale));
		//			}
		//		},
		PLUS("stringToJoin") {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				final StringLangObject s = NamedFunctionUtils.getArgOrNull(this, 0, args, StringLangObject.class, ec);
				return s == null ? thisContext.coerceNumber(ec)
						: StringLangObject.create(thisContext.stringValue() + s.stringValue());
			}
		};

		private final String[] argList;

		private Func(final String... argList) {
			this.argList = argList;
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
