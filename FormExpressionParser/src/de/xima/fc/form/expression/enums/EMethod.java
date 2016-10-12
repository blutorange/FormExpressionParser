package de.xima.fc.form.expression.enums;

/**
 * Method names for operators internal to the language.
 * <br><br>
 * For example, <code>5 + 3</code> is syntactic sugar for <code>5.__PLUS(3)</code>
 * @author madgaksha
 */
public enum EMethod {
	PLUS("+"), // +
	PLUS_UNARY("-"), // +
	DOUBLE_PLUS_PREFIX("++"), // ++i
	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_PLUS_PREFIX} */
	DOUBLE_PLUS_SUFFIX("++"), // i++
	DASH("-"), // -
	DASH_UNARY("-"), // -
	DOUBLE_DASH_PREFIX("--"), // --i
	/** During evaluation, this gets mapped to {@link #EMethod#DOUBLE_DASH_PREFIX} */
	DOUBLE_DASH_SUFFIX("--"), // i--

	STAR("*"), // *
	DOUBLE_STAR("**"), // **
	SLASH("/"), // /
	PERCENT("%"), // %

	AMPERSAND("&"), // &
	DOUBLE_AMPERSAND("&&"), // &&

	BAR("|"), // |
	DOUBLE_BAR("||"), // ||

	CIRCUMFLEX("^"), // ^

	TILDE("~"), // ~
	EQUAL_TILDE("=~"), // =~
	EXCLAMATION_TILDE("!~"), // !~

	PLUS_EQUAL("+="), // +=
	DASH_EQUAL("-="), // -=
	STAR_EQUAL("*="), // *=
	DOUBLE_STAR_EQUAL("**="), // **=
	SLASH_EQUAL("/="), // /=
	PERCENT_EQUAL("%="), // %=
	AMPERSAND_EQUAL("&="), // &=
	DOUBLE_ANGLE_OPEN_EQUAL("<<="), // <<=
	TRIPLE_ANGLE_OPEN_EQUAL("<<<="), // <<<=
	DOUBLE_ANGLE_CLOSE_EQUAL(">>="), // >>=
	TRIPLE_ANGLE_CLOSE_EQUAL(">>>="), // >>>=
	DOUBLE_AMPERSAND_EQUAL("&&="), // &&=
	BAR_EQUAL("|="), // |=
	DOUBLE_BAR_EQUAL("||="), // ||=
	CIRCUMFLEX_EQUAL("^="), // ^=

	EQUAL("="), // =
	DOUBLE_EQUAL("=="), // ==
	TRIPLE_EQUAL("==="), // ===

	EXCLAMATION("!"), // !
	EXCLAMATION_EQUAL("!="), // !=
	EXCLAMATION_DOUBLE_EQUAL("!=="), // !==

	ANGLE_OPEN("<"), // <
	DOUBLE_ANGLE_OPEN("<<"), // <<
	TRIPLE_ANGLE_OPEN("<<<"), // <<<
	ANGLE_CLOSE(">"), // >
	DOUBLE_ANGLE_CLOSE(">>"), // >>
	TRIPLE_ANGLE_CLOSE(">>>"), // >>>
	ANGLE_OPEN_EQUAL("<="),// <=
	ANGLE_CLOSE_EQUAL(">="), // >=
	COERCE("=>"),

	DOT("."),
	BRACKET("[]"),
	PARENTHESIS("()"),

	//Special do not use
	SWITCHCASE("SWITCHCASE"),
	SWITCHDEFAULT("SWITCHDEFAULT"),
	SWITCHCLAUSE("SWITCHCLAUSE"),
	;

	public final String methodName;
	private EMethod(final String name) {
		this.methodName = name;
	}

	public static EMethod equalMethod(final EMethod method) {
		switch (method) {
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

	public static EMethod comparisonMethod(final EMethod method) {
		switch (method) {
		case TRIPLE_EQUAL:
		case EXCLAMATION_DOUBLE_EQUAL: return EMethod.TRIPLE_EQUAL;
		case DOUBLE_EQUAL:
		case EXCLAMATION_EQUAL: return EMethod.DOUBLE_EQUAL;
		case EQUAL_TILDE:
		case EXCLAMATION_TILDE: return EMethod.EQUAL_TILDE;
		//$CASES-OMITTED$
		default:
			return null;
		}
	}

	public static boolean isNegate(final EMethod method) {
		switch (method) {
		case EXCLAMATION_EQUAL:
		case EXCLAMATION_DOUBLE_EQUAL:
		case EXCLAMATION_TILDE:
			return true;
			//$CASES-OMITTED$
		default:
			return false;
		}
	}

	public static boolean isAssigning(final EMethod method) {
		switch (method) {
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
}
