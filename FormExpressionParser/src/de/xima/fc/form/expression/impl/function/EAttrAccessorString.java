package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorString implements IFunction<StringLangObject> {
	/**
	 * @param locale {@link StringLangObject} (optional). The (IETF BCP 47) name of the locale to be used for the conversion. Defaults to the English-like {@link Locale#ROOT}.
	 * @return {@link StringLangObject} The upper-case version of the string.
	 */
	upcase("locale") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			return thisContext.toUpperCase(args.length == 0 ? Locale.ROOT : Locale.forLanguageTag(args[0].coerceString(ec).stringValue()));
		}
	},
	/**
	 * @return {@link NumberLangObject}. The length of this string, >=0.
	 */
	length() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NumberLangObject.create(thisContext.stringValue().length());
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorString(final String... argList) {
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

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
