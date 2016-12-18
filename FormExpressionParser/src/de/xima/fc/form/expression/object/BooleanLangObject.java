package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.CoercionException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class BooleanLangObject extends ALangObject {
	private final boolean value;

	private BooleanLangObject(final boolean value) {
		super();
		this.value = value;
	}

	@Override
	public ILangObjectClass getObjectClass() {
		return ELangObjectClass.BOOLEAN;
	}

	private static class InstanceHolder {
		private static BooleanLangObject TRUE = new BooleanLangObject(true);
		private static BooleanLangObject FALSE = new BooleanLangObject(false);
	}

	public boolean booleanValue() {
		return value;
	}


	public BooleanLangObject or(final BooleanLangObject other) {
		return BooleanLangObject.create(value||other.value);
	}

	public BooleanLangObject and(final BooleanLangObject other) {
		return BooleanLangObject.create(value&&other.value);
	}

	public BooleanLangObject xor(final BooleanLangObject other) {
		return BooleanLangObject.create(value^other.value);
	}

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
	public int hashCode() {
		return value ? 0 : 1;
	}

	@Override
	public boolean equals(@Nullable final Object o) {
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

	public static String toExpression(final boolean value) {
		return value ? Syntax.TRUE : Syntax.FALSE;
	}

	public static BooleanLangObject create(final boolean b) {
		return b ? InstanceHolder.TRUE : InstanceHolder.FALSE;
	}

	public static BooleanLangObject getTrueInstance() {
		return InstanceHolder.TRUE;
	}

	public static BooleanLangObject getFalseInstance() {
		return InstanceHolder.FALSE;
	}
}