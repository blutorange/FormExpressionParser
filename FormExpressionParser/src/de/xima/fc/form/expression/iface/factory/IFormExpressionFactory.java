package de.xima.fc.form.expression.iface.factory;

import java.io.IOException;
import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.iface.IFormExpressionHighlighter;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IToken;

@ParametersAreNonnullByDefault
public interface IFormExpressionFactory {
	/**
	 * Parses the given string and returns the top level node of the parse
	 * tree.
	 *
	 * @return Top level node of the parse tree.
	 * @throws ParseException
	 *             When the code is not a valid program. Specifically, when
	 *             the tokens cannot be parsed as a valid program.
	 * @throws TokenMgrError
	 *             When the code is not a valid program. Specifically, when
	 *             the code cannot be parsed into valid tokens.
	 * @throws SemanticsException
	 *             When the code did not technically fail to parse, but is
	 *             semantically invalid. This is a subclass of
	 *             {@link ParseException}.
	 */
	public <T> IFormExpression<T> parse(String code,
			IEvaluationContextContract<T> factory, ISeverityConfig config)
					throws ParseException, TokenMgrError;

	/**
	 * Formats the given code. Formatting can be controlled with a {@link IUnparseConfig}.
	 * @param code Code to format.
	 * @param config Configuration for formatting.
	 * @return The formatted code.
	 * @throws ParseException
	 *             When the code is not a valid program. Specifically, when
	 *             the tokens cannot be parsed as a valid program.
	 * @throws TokenMgrError
	 *             When the code is not a valid program. Specifically, when
	 *             the code cannot be parsed into valid tokens.
	 */
	public String format(String code, IUnparseConfig config)
			throws ParseException, TokenMgrError;
	
	public void highlight(String code, IFormExpressionHighlighter highlighter, IFormExpressionHighlightTheme theme)
			throws ParseException, IOException;

	/**
	 * @param code
	 *            Code to parse.
	 * @return An iterator for the tokens the code consists of.
	 * @throws TokenMgrError When the code is invalid.
	 */
	public Iterator<IToken> asTokenStream(String code) throws TokenMgrError;
}