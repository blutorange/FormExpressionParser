package de.xima.fc.form.expression.iface.parse;

import java.io.Reader;
import java.util.Iterator;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.FormExpressionParserTokenManager;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public interface IFormExpressionFactory {

	@Nonnull
	Node asNode(@Nonnull String code) throws ParseException, TokenMgrError;

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
	@Nonnull
	<T extends IExternalContext> IFormExpression<T> parse(@Nonnull String code,
			@Nonnull IEvaluationContextContractFactory<T> factory, boolean strictMode)
			throws ParseException, TokenMgrError;

	/**
	 * Formats the given code. Formatting can be controlled with a {@link UnparseVisitorConfig}.
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
	@Nonnull
	String format(@Nonnull String code, @Nonnull UnparseVisitorConfig config)
			throws ParseException, TokenMgrError;

	/**
	 * @param code
	 *            Code to parse.
	 * @return An iterator for the tokens the code consists of.
	 * @throws TokenMgrError When the code is invalid.
	 */
	@Nonnull
	Iterator<Token> asTokenStream(@Nonnull Reader reader) throws TokenMgrError;

	@Nonnull
	Token[] asTokenArray(@Nonnull String code) throws TokenMgrError;

	/**
	 * @param reader
	 *            Reader to read from.
	 * @return A token manager for tokenizing the stream.
	 */
	@Nonnull
	FormExpressionParserTokenManager asTokenManager(@Nonnull Reader reader);

}