package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NamedFunctionUtils;

public enum EAttrAccessorString implements IFunction<StringLangObject> {
	upcase {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			Locale locale = Locale.ROOT;
			final StringLangObject l = NamedFunctionUtils.getArgOrNull(this, 0, args, StringLangObject.class, ec);
			locale = l == null ? Locale.ROOT : Locale.forLanguageTag(l.stringValue());
			return StringLangObject.create(thisContext.stringValue().toUpperCase(locale));
		}
	},
	length {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			NamedFunctionUtils.assertArgs(this, 0, args, ec);
			return NumberLangObject.create(thisContext.stringValue().length());
		}
	}
	;

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
