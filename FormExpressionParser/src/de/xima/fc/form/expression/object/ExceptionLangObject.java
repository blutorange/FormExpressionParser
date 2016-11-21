package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.CatchableEvaluationException;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.CustomRuntimeException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

public class ExceptionLangObject extends ALangObject {
	@Nonnull
	private final CatchableEvaluationException value;

	private ExceptionLangObject(@Nonnull final CatchableEvaluationException value) {
		super(Type.EXCEPTION);
		this.value = value;
	}

	@Override
	public ALangObject shallowClone() {
		return new ExceptionLangObject(value);
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(Syntax.ERROR).append(Syntax.PAREN_OPEN);
		StringLangObject.toExpression(value.getMessage(), builder);
		builder.append(Syntax.PAREN_CLOSE);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_EXCEPTION_LANG_OBJECT).append('(')
				.append(value.getMessage()).append(')'));
	}

	@Override
	public IFunction<ExceptionLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodException(method);
	}
	@Override
	public IFunction<ExceptionLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorException(object, accessedViaDot);
	}

	@Override
	public IFunction<ExceptionLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerException(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodException(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorException(object, accessedViaDot), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerException(object, accessedViaDot), object, accessedViaDot, value, ec);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ExceptionLangObject)) return false;
		return value.getMessage().equals(((ExceptionLangObject)o).value.getMessage());
	}

	@Override
	public int compareToSameType(final ALangObject o) {
		return value.getMessage().compareTo(((ExceptionLangObject)o).value.getMessage());
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	protected boolean isSingletonLike() {
		return false;
	}

	// Coercion
	@Nonnull
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return StringLangObject.create(value.getMessage());
	}
	@Nonnull
	@Override
	public ExceptionLangObject coerceException(final IEvaluationContext ec) throws CoercionException {
		return this;
	}

	@Nonnull
	public EvaluationException exceptionValue() {
		return value;
	}

	@Nonnull
	public static ExceptionLangObject create(@Nullable final CatchableEvaluationException value,
			@Nonnull final IEvaluationContext ec) {
		if (value == null || value.getMessage() == null)
			return new ExceptionLangObject(new CustomRuntimeException(CmnCnst.NonnullConstant.STRING_EMPTY, ec));
		return new ExceptionLangObject(value);
	}

	@Nonnull
	public static ExceptionLangObject create(@Nullable final String message, @Nonnull final IEvaluationContext ec) {
		if (message == null)
			return new ExceptionLangObject(new CustomRuntimeException(CmnCnst.NonnullConstant.STRING_EMPTY, ec));
		return new ExceptionLangObject(new CustomRuntimeException(message, ec));
	}
}
