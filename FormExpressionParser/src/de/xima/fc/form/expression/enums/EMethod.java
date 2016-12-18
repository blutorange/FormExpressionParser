package de.xima.fc.form.expression.enums;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.ExpressionMethod;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Method names for operators internal to the language.
 * <br><br>
 * For example, <code>5 + 3</code> is syntactic sugar for <code>5.__PLUS(3)</code>
 * @author madgaksha
 */
public enum EMethod {
	PLUS(ExpressionMethod.PLUS), // +
	PLUS_UNARY(ExpressionMethod.PLUS), // +
	DOUBLE_PLUS_PREFIX(ExpressionMethod.DOUBLE_PLUS), // ++i
	DASH(ExpressionMethod.DASH), // -
	DASH_UNARY(ExpressionMethod.DASH), // -
	DOUBLE_DASH_PREFIX(ExpressionMethod.DOUBLE_DASH), // --i

	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_PLUS_PREFIX} */
	DOUBLE_PLUS_SUFFIX(ExpressionMethod.DOUBLE_PLUS), // i++
	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_DASH_PREFIX} */
	DOUBLE_DASH_SUFFIX(ExpressionMethod.DASH), // i--

	STAR(ExpressionMethod.STAR), // *
	DOUBLE_STAR(ExpressionMethod.DOUBLE_STAR), // **
	SLASH(ExpressionMethod.SLASH), // /
	PERCENT(ExpressionMethod.PERCENT), // %

	AMPERSAND(ExpressionMethod.AMPERSAND), // &
	DOUBLE_AMPERSAND(ExpressionMethod.DOUBLE_AMPERSAND), // &&

	BAR(ExpressionMethod.BAR), // |
	DOUBLE_BAR(ExpressionMethod.DOUBLE_BAR), // ||

	CIRCUMFLEX(ExpressionMethod.CIRCUMFLEX), // ^

	TILDE(ExpressionMethod.TILDE), // ~
	EQUAL_TILDE(ExpressionMethod.EQUAL_TILDE), // =~
	EXCLAMATION_TILDE(ExpressionMethod.EXCLAMATION_TILDE), // !~

	// These cannot be overridden, these operators use the non-equal
	// versions during evaluation.
	// Eg. a+=b is evaluated as if it were a=a+b
	PLUS_EQUAL(ExpressionMethod.PLUS_EQUAL), // +=
	DASH_EQUAL(ExpressionMethod.DASH_EQUAL), // -=
	STAR_EQUAL(ExpressionMethod.STAR_EQUAL), // *=
	DOUBLE_STAR_EQUAL(ExpressionMethod.DOUBLE_STAR_EQUAL), // **=
	SLASH_EQUAL(ExpressionMethod.SLASH_EQUAL), // /=
	PERCENT_EQUAL(ExpressionMethod.PERCENT_EQUAL), // %=
	AMPERSAND_EQUAL(ExpressionMethod.AMPERSAND_EQUAL), // &=
	DOUBLE_ANGLE_OPEN_EQUAL(ExpressionMethod.DOUBLE_ANGLE_OPEN_EQUAL), // <<=
	TRIPLE_ANGLE_OPEN_EQUAL(ExpressionMethod.TRIPLE_ANGLE_OPEN_EQUAL), // <<<=
	DOUBLE_ANGLE_CLOSE_EQUAL(ExpressionMethod.DOUBLE_ANGLE_CLOSE_EQUAL), // >>=
	TRIPLE_ANGLE_CLOSE_EQUAL(ExpressionMethod.TRIPLE_ANGLE_CLOSE_EQUAL), // >>>=
	DOUBLE_AMPERSAND_EQUAL(ExpressionMethod.DOUBLE_AMPERSAND_EQUAL), // &&=
	BAR_EQUAL(ExpressionMethod.BAR_EQUAL), // |=
	DOUBLE_BAR_EQUAL(ExpressionMethod.DOUBLE_BAR_EQUAL), // ||=
	CIRCUMFLEX_EQUAL(ExpressionMethod.AMPERSAND_EQUAL), // ^=

	EQUAL(ExpressionMethod.EQUAL), // =
	EXCLAMATION(ExpressionMethod.EXCLAMATION), // !

	// These cannot be overridden, they use ALangObject#equals
	DOUBLE_EQUAL(ExpressionMethod.DOUBLE_EQUAL), // ==
	TRIPLE_EQUAL(ExpressionMethod.TRIPLE_EQUAL), // ===
	EXCLAMATION_EQUAL(ExpressionMethod.EXCLAMATION_EQUAL), // !=
	EXCLAMATION_DOUBLE_EQUAL(ExpressionMethod.EXCLAMATION_DOUBLE_EQUAL), // !==

	// These cannot be overridden, they use ALangObject#compareTo
	ANGLE_OPEN(ExpressionMethod.ANGLE_OPEN), // <
	ANGLE_CLOSE(ExpressionMethod.ANGLE_CLOSE), // >
	ANGLE_OPEN_EQUAL(ExpressionMethod.ANGLE_OPEN_EQUAL),// <=
	ANGLE_CLOSE_EQUAL(ExpressionMethod.ANGLE_CLOSE_EQUAL), // >=

	DOUBLE_ANGLE_OPEN(ExpressionMethod.DOUBLE_ANGLE_OPEN), // <<
	TRIPLE_ANGLE_OPEN(ExpressionMethod.TRIPLE_ANGLE_OPEN), // <<<
	DOUBLE_ANGLE_CLOSE(ExpressionMethod.DOUBLE_ANGLE_CLOSE), // >>
	TRIPLE_ANGLE_CLOSE(ExpressionMethod.TRIPLE_ANGLE_CLOSE), // >>>

	COERCE(ExpressionMethod.COERCE),

	DOT(ExpressionMethod.DOT),
	BRACKET(ExpressionMethod.BRACKET),
	PARENTHESIS(ExpressionMethod.PARENTHESIS),

	//Special do not use
	SWITCHCASE(ExpressionMethod.SWITCHCASE),
	SWITCHDEFAULT(ExpressionMethod.SWITCHDEFAULT),
	SWITCHCLAUSE(ExpressionMethod.SWITCHCLAUSE),
	NONE(CmnCnst.NonnullConstant.STRING_EMPTY),
	;

	@Nonnull public final String methodName;
	private EMethod(@Nonnull final String name) {
		this.methodName = name;
	}


	@Nullable
	private EMethod equalMethod() {
		switch (this) {
		case DOUBLE_PLUS_PREFIX: return EMethod.DOUBLE_PLUS_PREFIX;
		case DOUBLE_DASH_PREFIX: return EMethod.DOUBLE_DASH_PREFIX;
		case DOUBLE_PLUS_SUFFIX: return EMethod.DOUBLE_PLUS_PREFIX;
		case DOUBLE_DASH_SUFFIX: return EMethod.DOUBLE_DASH_PREFIX;
		case PLUS_EQUAL: return EMethod.PLUS;
		case DASH_EQUAL: return EMethod.DASH;
		case STAR_EQUAL: return EMethod.STAR;
		case DOUBLE_STAR_EQUAL: return EMethod.DOUBLE_STAR;
		case SLASH_EQUAL: return EMethod.SLASH;
		case PERCENT_EQUAL: return EMethod.PERCENT;
		case AMPERSAND_EQUAL: return EMethod.AMPERSAND;
		case DOUBLE_AMPERSAND_EQUAL: return EMethod.DOUBLE_AMPERSAND;
		case BAR_EQUAL: return EMethod.BAR;
		case DOUBLE_BAR_EQUAL: return EMethod.DOUBLE_BAR;
		case DOUBLE_ANGLE_OPEN_EQUAL: return EMethod.DOUBLE_ANGLE_OPEN;
		case DOUBLE_ANGLE_CLOSE_EQUAL: return EMethod.DOUBLE_ANGLE_CLOSE;
		case TRIPLE_ANGLE_OPEN_EQUAL: return EMethod.TRIPLE_ANGLE_OPEN;
		case TRIPLE_ANGLE_CLOSE_EQUAL: return EMethod.TRIPLE_ANGLE_CLOSE;
		case CIRCUMFLEX_EQUAL: return EMethod.CIRCUMFLEX;
		//$CASES-OMITTED$
		default:
			return null;
		}
	}

	@Nonnull
	public EMethod equalMethod(@Nonnull final IEvaluationContext ec) throws UncatchableEvaluationException {
		final EMethod m = equalMethod();
		if (m != null) return m;
		throw new UncatchableEvaluationException(ec, NullUtil.messageFormat(CmnCnst.Error.INVALID_EQUAL_METHOD, this));
	}

	@Nonnull
	public EMethod equalMethod(@Nonnull final Node node) throws SemanticsException {
		final EMethod m = equalMethod();
		if (m != null) return m;
		throw new SemanticsException(NullUtil.messageFormat(CmnCnst.Error.INVALID_EQUAL_METHOD, this), node);
	}


	@Nonnull
	public EMethod comparisonMethod(@Nonnull final IEvaluationContext ec) throws UncatchableEvaluationException {
		switch (this) {
		case TRIPLE_EQUAL:
		case EXCLAMATION_DOUBLE_EQUAL: return EMethod.TRIPLE_EQUAL;
		case DOUBLE_EQUAL:
		case EXCLAMATION_EQUAL: return EMethod.DOUBLE_EQUAL;
		case EQUAL_TILDE:
		case EXCLAMATION_TILDE: return EMethod.EQUAL_TILDE;
		//$CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec, NullUtil.messageFormat(CmnCnst.Error.INVALID_COMPARISON_METHOD, this));
		}
	}

	public boolean isAssigning() {
		switch (this) {
		case EQUAL:
		case DOUBLE_PLUS_PREFIX:
		case DOUBLE_PLUS_SUFFIX:
		case DOUBLE_DASH_PREFIX:
		case DOUBLE_DASH_SUFFIX:
		case PLUS_EQUAL:
		case DASH_EQUAL:
		case STAR_EQUAL:
		case DOUBLE_STAR_EQUAL:
		case SLASH_EQUAL:
		case PERCENT_EQUAL:
		case AMPERSAND_EQUAL:
		case DOUBLE_AMPERSAND_EQUAL:
		case BAR_EQUAL:
		case DOUBLE_BAR_EQUAL:
		case DOUBLE_ANGLE_OPEN_EQUAL:
		case DOUBLE_ANGLE_CLOSE_EQUAL:
		case TRIPLE_ANGLE_OPEN_EQUAL:
		case TRIPLE_ANGLE_CLOSE_EQUAL:
		case CIRCUMFLEX_EQUAL:
			return true;
			//$CASES-OMITTED$
		default:
			return false;
		}
	}

	//    -1 0 1
	// >   f f t
	// <   t f f
	// >=  f t t
	// <=  t t f
	/**
	 * Takes the result of a Java-comparison {@link Comparable#compareTo(Object)} and checks
	 * the result when compared with this comparison method. For example, assume the two
	 * objects are <code>o1 = 2</code> and <code>o2 = 1</code>. The Java-comparison yields
	 * the result <code>1</code>. Then the comparison method {@link #ANGLE_OPEN} returns
	 * <code>true</code> because <code>2 > 1 == true</code>; the comparison method {@link #ANGLE_CLOSE}
	 * returns <code>false</code> because <code>2 < 1 == false</code>;
	 * @param comp Result of the comparison of two object, eg. <code>o1.compareTo(o2)</code>
	 * @return <code>true</code> iff the are in the order as specified by this comparison method.
	 */
	public boolean checkComparison(final int comp) {
		switch (this) {
		case ANGLE_OPEN: return comp < 0;
		case ANGLE_CLOSE: return comp > 0;
		case ANGLE_OPEN_EQUAL: return comp <= 0;
		case ANGLE_CLOSE_EQUAL: return comp >= 0;
		//$CASES-OMITTED$
		default: return false;
		}
	}

	@Override
	public String toString() {
		return NullUtil.stringFormat(CmnCnst.ToString.E_METHOD, super.toString(), methodName);
	}
}
