package de.xima.fc.form.expression.object;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NullObjectAccessException;

public class NullLangObject extends ALangObject {
	private NullLangObject() {
		super(Type.NULL);
	}

	private static class InstanceHolder {
		private static NullLangObject INSTANCE = new NullLangObject();
	}

	public Object nullValue() {
		return null;
	}

	/** @deprecated Use {@link #getInstance()} */
	@Deprecated
	public static ALangObject create() {
		return getInstance();
	}

	/**
	 * @return <code>this</code>, as only one object may ever exist.
	 */
	@Override
	public ALangObject shallowClone() {
		return this;
	}

	/**
	 * @return <code>this</code>, as only one object may ever exist.
	 */
	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	public static NullLangObject getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public IFunction<NumberLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}
	@Override
	public IFunction<NumberLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof NullLangObject)) return false;
		return true;
	}

	@Override
	public String inspect() {
		return "NullLangObject";
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append("null");
	}

	// Coercion
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) {
		return NumberLangObject.getZeroInstance();
	}

	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return StringLangObject.getEmptyInstance();
	}

	@Override
	public ArrayLangObject coerceArray(final IEvaluationContext ec) {
		return ArrayLangObject.create();
	}

	@Override
	public HashLangObject coerceHash(final IEvaluationContext ec) {
		return HashLangObject.create();
	}

	@Override
	public ExceptionLangObject coerceException(final IEvaluationContext ec) {
		return ExceptionLangObject.create(StringUtils.EMPTY, ec);
	}

	@Override
	public FunctionLangObject coerceFunction(final IEvaluationContext ec) {
		return FunctionLangObject.getNoOpInstance();
	}	
}