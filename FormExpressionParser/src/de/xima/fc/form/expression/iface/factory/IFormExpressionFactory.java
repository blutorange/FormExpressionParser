package de.xima.fc.form.expression.iface.factory;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.ABasicHighlightTheme;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter;
import de.xima.fc.form.expression.highlight.style.EHighlightThemePack;
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.iface.IFormExpressionHighlighter;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IToken;

@NonNullByDefault
public interface IFormExpressionFactory {
	/**
	 * Parses the given string and returns an object with methods to
	 * run the code. The returned object is also serializable, so the
	 * code does not need to be parsed twice. For small code, there
	 * is not much of a difference between deserialization and parsing,
	 * but large amounts of code may take longer to parse.
	 *
	 * @return An object representing the parsed code, to be used for
	 * evaluation.
	 * @throws TokenMgrError
	 *             When the code is not a valid program. Specifically, when
	 *             the code cannot be parsed into valid tokens.
	 * @throws ParseException
	 *             When the code is not a valid program. Specifically, when
	 *             the tokens cannot be parsed into a valid AST tree.
	 * @throws SemanticsException
	 *             When the code is not a valid program. Specifically, when
	 *             the code did not technically fail to parse, but is
	 *             semantically invalid. This is a subclass of
	 *             {@link ParseException}.
	 */
	public <T> IFormExpression<T> compile(String code,
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
	
	/**
	 * Syntax highlighting for the code.
	 * @param code Code to highlight.
	 * @param highlighter Highlighter to use. 
	 * @param theme Theme to use.
	 * @throws ParseException When the code is invalid.
	 * @throws TokenMgrError When the code is invalid.
	 * @throws IOException When the highlighter fails to write the output.
	 * @see HtmlHighlighter
	 * @see ABasicHighlightTheme
	 * @see EHighlightThemePack
	 */
	public void highlight(String code, IFormExpressionHighlighter highlighter, IFormExpressionHighlightTheme theme)
			throws ParseException, TokenMgrError, IOException;

	/**
	 * @param code
	 *            Code to parse.
	 * @return An iterator for the tokens the code consists of.
	 * @throws TokenMgrError When the code is invalid.
	 */
	public Iterator<IToken> asTokenStream(String code) throws TokenMgrError;
}