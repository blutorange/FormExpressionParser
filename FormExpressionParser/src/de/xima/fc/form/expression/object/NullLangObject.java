package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.NullObjectAccessException;
import de.xima.fc.form.expression.exception.NullObjectAssignException;
import de.xima.fc.form.expression.exception.NullObjectMethodException;
import de.xima.fc.form.expression.util.CmnCnst;

public class NullLangObject extends ALangObject {
	private NullLangObject() {
		super(Type.NULL);
	}

	private static class InstanceHolder {
		@Nonnull private static NullLangObject INSTANCE = new NullLangObject();
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

	@Nonnull
	public static NullLangObject getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public IFunction<NumberLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectMethodException(method, ec);
	}
	@Override
	public IFunction<NumberLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(object, accessedViaDot, ec);
	}
	@Override
	public IFunction<NumberLangObject> attrAssigner(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAssignException(object, accessedViaDot, ec);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		throw new NullObjectMethodException(method, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAssignException(object, accessedViaDot, ec);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(object, accessedViaDot, ec);
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

	/**
	 * @param o The comparand to compare this object to.
	 * @return 0. There is only one <code>null</code> object, and it is equal to itself.
	 */
	@Override
	protected int compareToSameType(final ALangObject o) {
		return 0;
	}

	@Override
	public String inspect() {
		return CmnCnst.ToString.INSPECT_NULL_LANG_OBJECT;
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(CmnCnst.SYNTAX_NULL);
	}

	public static String toExpression() {
		return CmnCnst.SYNTAX_NULL;
	}

	@Override
	protected boolean isSingletonLike() {
		return true;
	}

	// Coercion
	@Nonnull
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) {
		return NumberLangObject.getZeroInstance();
	}

	@Nonnull
	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) {
		return RegexLangObject.getUnmatchableInstance();
	}

	@Nonnull
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return StringLangObject.getEmptyInstance();
	}

	@Nonnull
	@Override
	public ArrayLangObject coerceArray(final IEvaluationContext ec) {
		return ArrayLangObject.create();
	}

	@Nonnull
	@Override
	public HashLangObject coerceHash(final IEvaluationContext ec) {
		return HashLangObject.create();
	}

	@Nonnull
	@Override
	public ExceptionLangObject coerceException(final IEvaluationContext ec) {
		return ExceptionLangObject.create(StringUtils.EMPTY, ec);
	}

	@Nonnull
	@Override
	public FunctionLangObject coerceFunction(final IEvaluationContext ec) {
		return FunctionLangObject.getNoOpInstance();
	}
}