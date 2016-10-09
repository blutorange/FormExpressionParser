package de.xima.fc.form.expression.util;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.FormExpressionParserTokenManager;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.SimpleCharStream;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;

public final class FormExpressionParsingUtil {
	private FormExpressionParsingUtil() {
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
		 * @throws ParseException
		 *             When the code is not a valid program.
		 */
		public static Node parse(final String code) throws ParseException, TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				return new FormExpressionParser(tokenManagerForState(reader, FormExpressionParserTokenManager.CODE))
						.CompleteProgram(null);
			}
		}

		/**
		 * @param code Code to parse.
		 * @return An object that, for convenience, is both a {@link Iterable} and {@link Iterator} and can only be iterated once, even when calling {@link Iterable#iterator()} more than once.
		 * @throws TokenMgrError
		 */
		public static TokenIterator asTokenStream(final Reader reader) throws TokenMgrError {
			final FormExpressionParserTokenManager tm = tokenManagerForState(reader,
					FormExpressionParserTokenManager.CODE);
			return new TokenIterator(tm);
	}
		
		public static Token[] asTokenArray(final String code) throws TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParserTokenManager tm = tokenManagerForState(reader,
						FormExpressionParserTokenManager.CODE);
				return tokenManagerToArray(tm);
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

		public static Node parse(final String code) throws ParseException, TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = new FormExpressionParser(
						tokenManagerForState(reader, FormExpressionParserTokenManager.LOS));
				parser.setLosAllowed(true);
				return parser.Template(null);
			}
		}

		public static TokenIterator asTokenStream(final Reader reader) throws TokenMgrError {
			final FormExpressionParserTokenManager tm = tokenManagerForState(reader,
					FormExpressionParserTokenManager.LOS);
			return new TokenIterator(tm);
		}
		
		public static Token[] asTokenArray(final String code) throws TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParserTokenManager tm = tokenManagerForState(reader,
						FormExpressionParserTokenManager.LOS);
				return tokenManagerToArray(tm);
			}
		}
	}

	private final static class TokenIterator implements Iterator<Token>, Iterable<Token> {
		private boolean hasNext = true;
		private final FormExpressionParserTokenManager tm;
		public TokenIterator(FormExpressionParserTokenManager tm) {
			this.tm = tm;
		}
		@Override
		public boolean hasNext() {
			return hasNext;
		}
		@Override
		public Token next() {
			final Token t = tm.getNextToken();
			hasNext = t.kind != FormExpressionParserConstants.EOF;
			return t;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Token iterator does not support removal.");
		}
		@Override
		public Iterator<Token> iterator() {
			return this;
		}		
	}	

	private static FormExpressionParserTokenManager tokenManagerForState(final Reader reader, final int state) throws TokenMgrError {
		final SimpleCharStream stream = new SimpleCharStream(reader);
		return new FormExpressionParserTokenManager(stream, state);
	}
	
	private static Token[] tokenManagerToArray(FormExpressionParserTokenManager tm) throws TokenMgrError {
		final List<Token> list = new ArrayList<>();
		for (Token token = tm.getNextToken(); token.kind != FormExpressionParserConstants.EOF; token = tm.getNextToken())
			list.add(token);
		return list.toArray(new Token[list.size()]);
	}
}