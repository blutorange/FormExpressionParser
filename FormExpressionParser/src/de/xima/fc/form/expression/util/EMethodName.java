package de.xima.fc.form.expression.util;

/**
 * Method names for operators internal to the language.
 * <br><br>
 * For example, <code>5 + 3</code> is syntactic sugar for <code>5.__PLUS(3)</code>
 * @author madgaksha
 */
public enum EMethodName {
	DASH("__DASH"), // -
	PLUS("__PLUS"), // +
	STAR("__STAR"), // *
	SLASH("__SLASH"), // /
	PERCENT("__PERCENT"), // %
	AMPERSAND("__AMPERSAND"), // &
	DOUBLE_AMPERSAND("__DOUBLE_AMPERSAND"), // &&
	BAR("__BAR"), // |
	DOUBLE_BAR("__DOUBLE_BAR"), // ||
	EXCLAMATION("__EXCLAMATION"), // !
	CIRCUMFLEX("_CIRCUMFLEX"), // ^
	TILDE("__TILDE"), // ~
	DOUBLE_EQUALS("__DOUBLE_EQUALS"), // ==
	TRIPLE_EQUALS("__TRIPLE_EQUALS"), // ===
	ANGLE_OPEN("__ANGLE_OPEN"), // <
	ANGLE_CLOSE("__ANGLE_CLOSE") // >
	;

	public final String name;
	private EMethodName(final String name) {
		this.name = name;
	}
}
