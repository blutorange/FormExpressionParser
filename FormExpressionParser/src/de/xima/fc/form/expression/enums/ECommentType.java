package de.xima.fc.form.expression.enums;

public enum ECommentType {
	/**
	 * Single-line comment, for example:
	 * <pre>
	 * // I am a comment.
	 * // Me too.
	 * </pre>
	 */
	SINGLE_LINE,
	/**
	 * Multi-line comment, for example:
	 * <pre>
	 * /*
	 *   I am a multi-line comment.
	 *   I am still the same comment.
	 * &#x2a;/
	 * </pre>
	 */
	MULTI_LINE;
}
