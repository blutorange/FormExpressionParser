package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

public class BooleanLangObject extends ALangObject {
	private final boolean value;

	private BooleanLangObject(final boolean value) {
		super();
		this.value = value;
	}

	@Override
	public ILangObjectClass getType() {
		return ELangObjectType.BOOLEAN;
	}

	private static class InstanceHolder {
		@Nonnull private static BooleanLangObject TRUE = new BooleanLangObject(true);
		@Nonnull private static BooleanLangObject FALSE = new BooleanLangObject(false);
	}

	public boolean booleanValue() {
		return value;
	}

	@Nonnull
	public BooleanLangObject or(final BooleanLangObject other) {
		return BooleanLangObject.create(value||other.value);
	}
	@Nonnull
	public BooleanLangObject and(final BooleanLangObject other) {
		return BooleanLangObject.create(value&&other.value);
	}
	@Nonnull
	public BooleanLangObject xor(final BooleanLangObject other) {
		return BooleanLangObject.create(value^other.value);
	}

	@Nonnull
	public BooleanLangObject not() {
		return BooleanLangObject.create(!value);
	}

	// Coercion
	@Nonnull
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return value ? StringLangObject.getTrueInstance() : StringLangObject.getFalseInstance();
	}
	@Nonnull
	@Override
	public NumberLangObject coerceNumber(final IEvaluationContext ec) throws CoercionException {
		return value ? NumberLangObject.getOneInstance() : NumberLangObject.getZeroInstance();
	}
	@Nonnull
	@Override
	public RegexLangObject coerceRegex(final IEvaluationContext ec) throws CoercionException {
		return value ? RegexLangObject.getAllMatchingInstance() : RegexLangObject.getUnmatchableInstance();
	}

	/**
	 *  @return <code>this</code>, as only one object exists for true and false.
	 */
	@Override
	public ALangObject shallowClone() {
		return this;
	}

	/**
	 *  @return <code>this</code>, as only one object exists for true and false.
	 */
	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public IFunction<BooleanLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethod(method, this);
	}

	@Override
	public IFunction<BooleanLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessor(object, accessedViaDot, this);
	}

	@Override
	public IFunction<BooleanLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssigner(name, accessedViaDot, this);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethod(method, this), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessor(object, accessedViaDot, this), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssigner(object, accessedViaDot, this), object, accessedViaDot, value, ec);
	}

	@Override
	public int hashCode() {
		return value ? 0 : 1;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof BooleanLangObject)) return false;
		return value == ((BooleanLangObject)o).value;
	}

	/**
	 * @return 0 iff both objects are <code>true</code> or both are <code>false</code>.
	 * -1 iff this object is <code>false</code> and the other is <code>true</code>,
	 * +1 iff this object is <code>true</code> and the other is <code>false</code>.
	 * Therefore the ordering is <code>false (0)</code> before <code>true (1)</code>.
	 */
	@Override
	protected int compareToSameType(final ALangObject o) {
		return ((BooleanLangObject)o).value==value ? 0 : value ? 1 : -1;
	}

	@Override
	protected boolean isSingletonLike() {
		return true;
	}


	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_BOOLEAN_LANG_OBJECT).append('(')
				.append(value).append(')'));
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(toExpression(value));
	}

	@Nonnull
	public static String toExpression(final boolean value) {
		return value ? Syntax.TRUE : Syntax.FALSE;
	}

	@Nonnull
	public static BooleanLangObject create(final boolean b) {
		return b ? getTrueInstance() : getFalseInstance();
	}

	@Nonnull
	public static BooleanLangObject getTrueInstance() {
		return InstanceHolder.TRUE;
	}

	@Nonnull
	public static BooleanLangObject getFalseInstance() {
		return InstanceHolder.FALSE;
	}

}
