package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.util.NamedFunctionUtils;

public enum EInstanceMethodNumber implements INamedFunction<NumberLangObject> {
	__PLUS {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			final NumberLangObject operand = NamedFunctionUtils.getCoercedArg(Type.NUMBER, NumberLangObject.class, 0, args, this, ec);
			return thisContext.add(operand);
		}
	},
	__STAR {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			final NumberLangObject operand = NamedFunctionUtils.getCoercedArg(Type.NUMBER, NumberLangObject.class, 0, args, this, ec);
			return thisContext.multiply(operand);
		}
	}
	;

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
