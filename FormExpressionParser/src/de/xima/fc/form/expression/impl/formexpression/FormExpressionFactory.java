package de.xima.fc.form.expression.impl.formexpression;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.context.IFormExpression;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.FormExpressionParserTokenManager;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.SimpleCharStream;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.util.CmnCnst;

public final class FormExpressionFactory {
	private FormExpressionFactory() {
	}

	/**
	 * Methods for parsing code as one complete program.
	 *
	 * @author mad_gaksha
	 */
	public final static class Program {
		private Program() {
		}

		/**
		 * Parses the given string and returns the top level node of the parse
		 * tree.
		 *
		 * @return Top level node of the parse tree.
		 * @throws ParseException When the code is not a valid program. Specifically, when
		 * the tokens cannot be parsed as a valid program.
		 * @throws TokenMgrError When the code is not a valid program. Specifically, when
		 * the code cannot be parsed into valid tokens.
		 */
		@Nonnull
		public static IFormExpression parse(@Nonnull final String code) throws ParseException, TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final Node node = asParser(asTokenManager(reader)).CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return new FormExpression(node);
			}
		}

		/**
		 * @param reader Reader to read from.
		 * @return A token manager for tokenizing the stream.
		 */
		@Nonnull
		public static FormExpressionParserTokenManager asTokenManager(@Nonnull final Reader reader) {
			return tokenManagerForState(reader,	FormExpressionParserTokenManager.CODE);
		}

		/**
		 * @param code
		 *            Code to parse.
		 * @return An object that, for convenience, is both a {@link Iterable}
		 *         and {@link Iterator} and can only be iterated once, even when
		 *         calling {@link Iterable#iterator()} more than once.
		 * @throws TokenMgrError
		 */
		@Nonnull
		public static TokenIterator asTokenStream(@Nonnull final Reader reader) throws TokenMgrError {
			return new TokenIterator(asTokenManager(reader));
		}

		@Nonnull
		public static Token[] asTokenArray(@Nonnull final String code) throws TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				return tokenManagerToArray(asTokenManager(reader));
			}
		}
	}

	/**
	 * Methods for parsing code as a template file. Template files consist of
	 * block of literal text, interspersed with code sections. For example, the
	 * following prints a list of dynamic values of a form field:
	 *
	 * <pre>
	 *   &lt;html&gt;
	 *     &lt;head&gt;&lt;/head&gt;
	 *     &lt;body&gt;
	 *       &lt;ul&gt;
	 *         [% for (value : form::dyn("tf1")) { %]
	 *           &lt;li&gt;[%value%]&lt;/li&gt;
	 *         [% } %]
	 *       &lt;/ul&gt;
	 *     &lt;/body&gt;
	 *   &lt;/html&gt;
	 * </pre>
	 *
	 * This could also be written as:
	 *
	 * <pre>
	 *   &lt;html&gt;
	 *     &lt;head&gt;&lt;/head&gt;
	 *     &lt;body&gt;
	 *       &lt;ul&gt;
	 *         [%
	 *           for (value : form::dyn("tf1"))
	 *             system::writer.write("&lt;ul&gt;").write(value).write("&lt;/ul&gt;");
	 *         %]
	 *       &lt;/ul&gt;
	 *     &lt;/body&gt;
	 *   &lt;/html&gt;
	 * </pre>
	 *
	 * @author madgaksha
	 *
	 */
	public final static class Template {
		private Template() {
		}

		@Nonnull
		public static IFormExpression parse(@Nonnull final String code) throws ParseException, TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				parser.setLosAllowed(true);
				final Node node = parser.Template(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return new FormExpression(node);
			}
		}

		@Nonnull
		public static TokenIterator asTokenStream(@Nonnull final Reader reader) throws TokenMgrError {
			return new TokenIterator(asTokenManager(reader));
		}

		@Nonnull
		public static Token[] asTokenArray(@Nonnull final String code) throws TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				return tokenManagerToArray(asTokenManager(reader));
			}
		}

		@Nonnull
		public static FormExpressionParserTokenManager asTokenManager(@Nonnull final Reader reader) {
			return tokenManagerForState(reader,	FormExpressionParserTokenManager.LOS);
		}
	}

	private final static class TokenIterator implements Iterator<Token>, Iterable<Token> {
		private boolean hasNext = true;
		private final FormExpressionParserTokenManager tm;

		public TokenIterator(@Nonnull final FormExpressionParserTokenManager tm) {
			this.tm = tm;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Nonnull
		@Override
		public Token next() {
			final Token t = tm.getNextToken();
			hasNext = t.kind != FormExpressionParserConstants.EOF;
			return t;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(CmnCnst.Error.TOKEN_ITERATOR_DOES_NOT_SUPPORT_REMOVAL);
		}

		@Nonnull
		@Override
		public Iterator<Token> iterator() {
			return this;
		}
	}

	@Nonnull
	private static FormExpressionParserTokenManager tokenManagerForState(@Nonnull final Reader reader, final int state)
			throws TokenMgrError {
		final SimpleCharStream stream = new SimpleCharStream(reader);
		return new FormExpressionParserTokenManager(null, stream, state);
	}

	@Nonnull
	private static FormExpressionParser asParser(@Nonnull final FormExpressionParserTokenManager tm) {
		final FormExpressionParser parser = new FormExpressionParser(tm);
		tm.parser = parser;
		return parser;
	}

	@Nonnull
	private static Token[] tokenManagerToArray(@Nonnull final FormExpressionParserTokenManager tm) throws TokenMgrError {
		final List<Token> list = new ArrayList<>();
		for (Token token = tm.getNextToken(); token.kind != FormExpressionParserConstants.EOF; token = tm
				.getNextToken())
			list.add(token);
		final Token[] res = new Token[list.size()];
		list.toArray(res);
		return res;
	}
}