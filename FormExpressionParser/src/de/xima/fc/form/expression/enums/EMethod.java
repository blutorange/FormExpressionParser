package de.xima.fc.form.expression.enums;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.UncatchableEvaluationException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * Method names for operators internal to the language.
 * <br><br>
 * For example, <code>5 + 3</code> is syntactic sugar for <code>5.__PLUS(3)</code>
 * @author madgaksha
 */
public enum EMethod {
	PLUS(CmnCnst.EMETHOD_PLUS), // +
	PLUS_UNARY(CmnCnst.EMETHOD_PLUS), // +
	DOUBLE_PLUS_PREFIX(CmnCnst.EMETHOD_DOUBLE_PLUS), // ++i
	DASH(CmnCnst.EMETHOD_DASH), // -
	DASH_UNARY(CmnCnst.EMETHOD_DASH), // -
	DOUBLE_DASH_PREFIX(CmnCnst.EMETHOD_DOUBLE_DASH), // --i

	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_PLUS_PREFIX} */
	DOUBLE_PLUS_SUFFIX(CmnCnst.EMETHOD_DOUBLE_PLUS), // i++
	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_DASH_PREFIX} */
	DOUBLE_DASH_SUFFIX(CmnCnst.EMETHOD_DASH), // i--

	STAR(CmnCnst.EMETHOD_STAR), // *
	DOUBLE_STAR(CmnCnst.EMETHOD_DOUBLE_STAR), // **
	SLASH(CmnCnst.EMETHOD_SLASH), // /
	PERCENT(CmnCnst.EMETHOD_PERCENT), // %

	AMPERSAND(CmnCnst.EMETHOD_AMPERSAND), // &
	DOUBLE_AMPERSAND(CmnCnst.EMETHOD_DOUBLE_AMPERSAND), // &&

	BAR(CmnCnst.EMETHOD_BAR), // |
	DOUBLE_BAR(CmnCnst.EMETHOD_DOUBLE_BAR), // ||

	CIRCUMFLEX(CmnCnst.EMETHOD_CIRCUMFLEX), // ^

	TILDE(CmnCnst.EMETHOD_TILDE), // ~
	EQUAL_TILDE(CmnCnst.EMETHOD_EQUAL_TILDE), // =~
	EXCLAMATION_TILDE(CmnCnst.EMETHOD_EXCLAMATION_TILDE), // !~

	// These cannot be overridden, these operators use the non-equal
	// versions during evaluation.
	// Eg. a+=b is evaluated as if it were a=a+b
	PLUS_EQUAL(CmnCnst.EMETHOD_PLUS_EQUAL), // +=
	DASH_EQUAL(CmnCnst.EMETHOD_DASH_EQUAL), // -=
	STAR_EQUAL(CmnCnst.EMETHOD_STAR_EQUAL), // *=
	DOUBLE_STAR_EQUAL(CmnCnst.EMETHOD_DOUBLE_STAR_EQUAL), // **=
	SLASH_EQUAL(CmnCnst.EMETHOD_SLASH_EQUAL), // /=
	PERCENT_EQUAL(CmnCnst.EMETHOD_PERCENT_EQUAL), // %=
	AMPERSAND_EQUAL(CmnCnst.EMETHOD_AMPERSAND_EQUAL), // &=
	DOUBLE_ANGLE_OPEN_EQUAL(CmnCnst.EMETHOD_DOUBLE_ANGLE_OPEN_EQUAL), // <<=
	TRIPLE_ANGLE_OPEN_EQUAL(CmnCnst.EMETHOD_TRIPLE_ANGLE_OPEN_EQUAL), // <<<=
	DOUBLE_ANGLE_CLOSE_EQUAL(CmnCnst.EMETHOD_DOUBLE_ANGLE_CLOSE_EQUAL), // >>=
	TRIPLE_ANGLE_CLOSE_EQUAL(CmnCnst.EMETHOD_TRIPLE_ANGLE_CLOSE_EQUAL), // >>>=
	DOUBLE_AMPERSAND_EQUAL(CmnCnst.EMETHOD_DOUBLE_AMPERSAND_EQUAL), // &&=
	BAR_EQUAL(CmnCnst.EMETHOD_BAR_EQUAL), // |=
	DOUBLE_BAR_EQUAL(CmnCnst.EMETHOD_DOUBLE_BAR_EQUAL), // ||=
	CIRCUMFLEX_EQUAL(CmnCnst.EMETHOD_AMPERSAND_EQUAL), // ^=

	EQUAL(CmnCnst.EMETHOD_EQUAL), // =
	EXCLAMATION(CmnCnst.EMETHOD_EXCLAMATION), // !

	// These cannot be overridden, they use ALangObject#equals
	DOUBLE_EQUAL(CmnCnst.EMETHOD_DOUBLE_EQUAL), // ==
	TRIPLE_EQUAL(CmnCnst.EMETHOD_TRIPLE_EQUAL), // ===
	EXCLAMATION_EQUAL(CmnCnst.EMETHOD_EXCLAMATION_EQUAL), // !=
	EXCLAMATION_DOUBLE_EQUAL(CmnCnst.EMETHOD_EXCLAMATION_DOUBLE_EQUAL), // !==

	// These cannot be overridden, they use ALangObject#compareTo
	ANGLE_OPEN(CmnCnst.EMETHOD_ANGLE_OPEN), // <
	ANGLE_CLOSE(CmnCnst.EMETHOD_ANGLE_CLOSE), // >
	ANGLE_OPEN_EQUAL(CmnCnst.EMETHOD_ANGLE_OPEN_EQUAL),// <=
	ANGLE_CLOSE_EQUAL(CmnCnst.EMETHOD_ANGLE_CLOSE_EQUAL), // >=

	DOUBLE_ANGLE_OPEN(CmnCnst.EMETHOD_DOUBLE_ANGLE_OPEN), // <<
	TRIPLE_ANGLE_OPEN(CmnCnst.EMETHOD_TRIPLE_ANGLE_OPEN), // <<<
	DOUBLE_ANGLE_CLOSE(CmnCnst.EMETHOD_DOUBLE_ANGLE_CLOSE), // >>
	TRIPLE_ANGLE_CLOSE(CmnCnst.EMETHOD_TRIPLE_ANGLE_CLOSE), // >>>

	COERCE(CmnCnst.EMETHOD_COERCE),

	DOT(CmnCnst.EMETHOD_DOT),
	BRACKET(CmnCnst.EMETHOD_BRACKET),
	PARENTHESIS(CmnCnst.EMETHOD_PARENTHESIS),

	//Special do not use
	SWITCHCASE(CmnCnst.EMETHOD_SWITCHCASE),
	SWITCHDEFAULT(CmnCnst.EMETHOD_SWITCHDEFAULT),
	SWITCHCLAUSE(CmnCnst.EMETHOD_SWITCHCLAUSE),
	NONE(CmnCnst.EMPTY_STRING),
	;

	@Nonnull public final String methodName;
	private EMethod(@Nonnull final String name) {
		this.methodName = name;
	}

	@Nonnull
	public EMethod equalMethod(@Nonnull final IEvaluationContext ec) {
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
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.INVALID_EQUAL_METHOD, this));
		}
	}

	@Nonnull
	public EMethod comparisonMethod(@Nonnull final IEvaluationContext ec) {
		switch (this) {
		case TRIPLE_EQUAL:
		case EXCLAMATION_DOUBLE_EQUAL: return EMethod.TRIPLE_EQUAL;
		case DOUBLE_EQUAL:
		case EXCLAMATION_EQUAL: return EMethod.DOUBLE_EQUAL;
		case EQUAL_TILDE:
		case EXCLAMATION_TILDE: return EMethod.EQUAL_TILDE;
		//$CASES-OMITTED$
		default:
			throw new UncatchableEvaluationException(ec, NullUtil.format(CmnCnst.Error.INVALID_COMPARISON_METHOD, this));
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
}
