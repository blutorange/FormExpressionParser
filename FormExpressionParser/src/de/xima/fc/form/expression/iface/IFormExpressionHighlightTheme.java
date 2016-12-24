package de.xima.fc.form.expression.iface;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.highlight.Color;
import de.xima.fc.form.expression.highlight.Style;

@ParametersAreNonnullByDefault
public interface IFormExpressionHighlightTheme {
	/**
	 * Returns the token for the given token type. See {@link FormExpressionParserConstants}
	 * for a list of token type IDs. All tokens are in camel-case, constants all in
	 * upper case such as {@link FormExpressionParserConstants#CODE} are lexical states,
	 * you do not need to provide a color for these.
	 * @param tokenType The token type.
	 * @return The color to be used for the token type. When <code>null</code>,
	 * some default style will be used.
	 */
	@Nullable
	public Style getStyleForToken(int tokenType);

	/** @return Color for the background. May not be possible with some highlighters. */
	public Color getColorForBackground();
}