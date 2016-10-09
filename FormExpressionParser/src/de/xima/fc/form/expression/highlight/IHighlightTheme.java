package de.xima.fc.form.expression.highlight;

import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;

public interface IHighlightTheme {
	/**
	 * Returns the token for the given token type. See {@link FormExpressionParserConstants}
	 * for a list of token type IDs. All tokens are in camel-case, constants all in
	 * upper case such as {@link FormExpressionParserConstants#CODE} are lexical states,
	 * you do not need to provide a color for these.
	 * @param tokenType The token type.
	 * @return The color to be used for the token type. When <code>null</code>,
	 * some default color will be used.
	 */
	public Style getStyleForToken(int tokenType);
	
	public Color getColorForBackground();
}