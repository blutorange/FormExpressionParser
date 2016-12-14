package de.xima.fc.form.expression.iface.factory;

import java.io.Reader;
import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTokenManager;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;

@ParametersAreNonnullByDefault
public interface IFormExpressionFactory {
	Node asNode(String code) throws ParseException, TokenMgrError;

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
	<T> IFormExpression<T> parse(String code,
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
	String format(String code, IUnparseConfig config)
			throws ParseException, TokenMgrError;

	/**
	 * @param code
	 *            Code to parse.
	 * @return An iterator for the tokens the code consists of.
	 * @throws TokenMgrError When the code is invalid.
	 */
	Iterator<Token> asTokenStream(Reader reader) throws TokenMgrError;

	Token[] asTokenArray(String code) throws TokenMgrError;

	/**
	 * @param reader
	 *            Reader to read from.
	 * @return A token manager for tokenizing the stream.
	 */
	FormExpressionParserTokenManager asTokenManager(Reader reader);
}