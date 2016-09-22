package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
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
	public INamedFunction<? extends ALangObject> instanceMethod(final String name, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().instanceMethodException(name);

	}

	@Override
	public INamedFunction<? extends ALangObject> attrAccessor(final String name, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().attrAccessorException(name);
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args)
			throws EvaluationException {
		return evaluateMethod(this, ec.getNamespace().instanceMethodException(name), name, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorException(name), name, ec);
	}

	@Override
	public boolean equals(final Object o) {
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
