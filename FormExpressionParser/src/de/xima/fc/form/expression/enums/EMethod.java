package de.xima.fc.form.expression.enums;

import java.util.EnumMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

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
	TILDE_EQUAL("~="), // ~=
	EQUAL_TILDE("=~"), // =~

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

	/**
	 * Maps between equal method and their plain methods, eg. <code>+=</code> to <code>+</code>.
	 */
	public final static ImmutableMap<EMethod, EMethod> equalTypeMap;
	static {
		final EnumMap<EMethod, EMethod> tmp = new EnumMap<>(EMethod.class);
		tmp.put(PLUS_EQUAL, PLUS);
		tmp.put(DASH_EQUAL, DASH);
		tmp.put(STAR_EQUAL, STAR);
		tmp.put(DOUBLE_STAR_EQUAL, DOUBLE_STAR);
		tmp.put(SLASH_EQUAL, SLASH);
		tmp.put(PERCENT_EQUAL, PERCENT);
		tmp.put(AMPERSAND_EQUAL, AMPERSAND);
		tmp.put(DOUBLE_AMPERSAND_EQUAL, DOUBLE_AMPERSAND);
		tmp.put(BAR_EQUAL, BAR);
		tmp.put(DOUBLE_BAR_EQUAL, DOUBLE_BAR);
		tmp.put(DOUBLE_ANGLE_OPEN_EQUAL, DOUBLE_ANGLE_OPEN);
		tmp.put(DOUBLE_ANGLE_CLOSE_EQUAL, DOUBLE_ANGLE_CLOSE);
		tmp.put(CIRCUMFLEX, CIRCUMFLEX);
		equalTypeMap = Maps.immutableEnumMap(tmp);
	}
}
