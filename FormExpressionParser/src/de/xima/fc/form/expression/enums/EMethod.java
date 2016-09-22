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
	DOUBLE_PLUS_PREFIX("++p"), // ++i
	DOUBLE_PLUS_SUFFIX("++s"), // i++
	DASH("-"), // -
	DASH_UNARY("-"), // -
	DOUBLE_DASH_PREFIX("--p"), // --i
	DOUBLE_DASH_SUFFIX("--s"), // i--

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

	PLUS_EQUAL("+="), // +=
	DASH_EQUAL("-="), // -=
	STAR_EQUAL("*="), // *=
	DOUBLE_STAR_EQUAL("**="), // **=
	SLASH_EQUAL("/="), // /=
	PERCENT_EQUAL("%="), // %=
	AMPERSAND_EQUAL("&="), // &=
	DOUBLE_ANGLE_OPEN_EQUAL("<<="), // <<=
	DOUBLE_ANGLE_CLOSE_EQUAL(">>="), // >>=
	DOUBLE_AMPERSAND_EQUAL("&&="), // &&=
	BAR_EQUAL("|="), // |=
	DOUBLE_BAR_EQUAL("||="), // ||=
	TILDE_EQUAL("~="), // ~=
	EQUAL_TILDE("=~"), // =~
	CIRCUMFLEX_EQUAL("^="), // ^=

	EQUAL("="), // =
	DOUBLE_EQUAL("=="), // ==
	TRIPLE_EQUAL("==="), // ===

	EXCLAMATION("!"), // !
	EXCLAMATION_EQUAL("!="), // !=
	EXCLAMATION_DOUBLE_EQUAL("!=="), // !==

	ANGLE_OPEN("<"), // <
	DOUBLE_ANGLE_OPEN("<<"), // <<
	ANGLE_CLOSE(">"), // >
	DOUBLE_ANGLE_CLOSE(">>"), // >>
	ANGLE_OPEN_EQUAL("<="),// <=
	ANGLE_CLOSE_EQUAL(">="), // >=
	COERCE("=>"),

	//Special do not use
	SWITCHCASE("SWITCHCASE"),
	SWITCHDEFAULT("SWITCHDEFAULT"),
	SWITCHCLAUSE("SWITCHCLAUSE")
	;

	public final String name;
	private EMethod(final String name) {
		this.name = name;
	}
}
