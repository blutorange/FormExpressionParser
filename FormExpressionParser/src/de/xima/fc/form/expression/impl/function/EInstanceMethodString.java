package de.xima.fc.form.expression.impl.function;

import java.util.Locale;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.NamedFunctionUtils;

public enum EInstanceMethodString implements INamedFunction<StringLangObject> {
	UPPERCASE {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext,
				final ALangObject... args) throws EvaluationException {
			Locale locale = Locale.ROOT;
			final StringLangObject l = NamedFunctionUtils.getArgOrNull(this, 0, args, StringLangObject.class, ec);
			locale = l == null ? Locale.ROOT : Locale.forLanguageTag(l.stringValue());
			return StringLangObject.create(thisContext.stringValue().toUpperCase(locale));
		}
	},
	__PLUS {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			final StringLangObject s = NamedFunctionUtils.getArgOrNull(this, 0, args, StringLangObject.class, ec);
			return StringLangObject.create(thisContext.stringValue() + s.stringValue());
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
