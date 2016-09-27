package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.CoercionException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;

public class ExceptionLangObject extends ALangObject {

	private final EvaluationException value;

	private ExceptionLangObject(final EvaluationException value) {
		super(Type.EXCEPTION);
		this.value = value;
	}

	@Override
	public ALangObject shallowClone() {
		return ExceptionLangObject.create(value);
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append("§exception(");
		StringLangObject.toExpression(value.getMessage(), builder);
		builder.append(")");
	}

	@Override
	public String inspect() {
		return "ExpressionLangObject(" + value.getMessage() + ")";
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
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodException(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorException(object, accessedViaDot), object, ec);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ExceptionLangObject)) return false;
		return value.equals(((ExceptionLangObject)o).value);
	}
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	// Coercion
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) throws CoercionException {
		return StringLangObject.create(value.getMessage());
	}

	@Override
	public BooleanLangObject coerceBoolean(final IEvaluationContext ec) throws CoercionException {
		return BooleanLangObject.getTrueInstance();
	}
	@Override
	public ExceptionLangObject coerceException(final IEvaluationContext ec) throws CoercionException {
		return this;
	}

	public EvaluationException exceptionValue() {
		return value;
	}

	public static ALangObject create(final EvaluationException value) {
		if (value == null) return NullLangObject.getInstance();
		return new ExceptionLangObject(value);
	}

	public static ExceptionLangObject create(final String message) {
		return new ExceptionLangObject(new CustomRuntimeException(message));
	}
}
