package de.xima.fc.form.expression.impl.formexpression;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.exception.parse.HeaderAssignmentNotCompileTimeConstantException;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.FormExpressionParserTokenManager;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.SimpleCharStream;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.iface.context.IExternalContext;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IFormExpressionFactory;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.CompileTimeConstantCheckVisitor;
import de.xima.fc.form.expression.visitor.JumpCheckVisitor;
import de.xima.fc.form.expression.visitor.ScopeCollectVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;
import de.xima.fc.form.expression.visitor.VariableHoistVisitor;
import de.xima.fc.form.expression.visitor.VariableResolveVisitor;

public final class FormExpressionFactory {
	private FormExpressionFactory() {
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
	 * @see #forProgram()
	 */
	@Nonnull
	public static IFormExpressionFactory forTemplate() {
		return TemplateImpl.INSTANCE;
	}

	/**
	 * Methods for parsing code as one complete program.
	 * 
	 * @see #forTemplate()
	 */
	@Nonnull
	public static IFormExpressionFactory forProgram() {
		return ProgramImpl.INSTANCE;
	}

	private static enum ProgramImpl implements IFormExpressionFactory {
		INSTANCE;

		@Override
		@Nonnull
		public <T extends IExternalContext> IFormExpression<T> parse(@Nonnull final String code,
				@Nonnull final IEvaluationContextContractFactory<T> factory, final boolean strictMode)
				throws ParseException, TokenMgrError, SemanticsException {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(factory);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				final Node node = parser.CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return postProcess(node, parser, factory, strictMode);
			}
		}

		@Override
		@Nonnull
		public String format(@Nonnull final String code, @Nonnull final UnparseVisitorConfig config) throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(config);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				final Node node = parser.CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return UnparseVisitor.unparse(node, parser.buildComments(), config);
			}
		}

		@Override
		@Nonnull
		public Node asNode(@Nonnull final String code) throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			try (final StringReader reader = new StringReader(code)) {
				final Node node = asParser(asTokenManager(reader)).CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return node;
			}
		}

		@Override
		@Nonnull
		public FormExpressionParserTokenManager asTokenManager(@Nonnull final Reader reader) {
			Preconditions.checkNotNull(reader);
			return tokenManagerForState(reader, FormExpressionParserTokenManager.CODE);
		}

		@Override
		@Nonnull
		public Iterator<Token> asTokenStream(@Nonnull final Reader reader) throws TokenMgrError {
			Preconditions.checkNotNull(reader);
			return new TokenIterator(asTokenManager(reader));
		}

		@Override
		@Nonnull
		public Token[] asTokenArray(@Nonnull final String code) throws TokenMgrError {
			Preconditions.checkNotNull(code);
			try (final StringReader reader = new StringReader(code)) {
				return tokenManagerToArray(asTokenManager(reader));
			}
		}
	}

	private static enum TemplateImpl implements IFormExpressionFactory {
		INSTANCE;
		@Override
		public Node asNode(final String code) throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				parser.setLosAllowed(true);
				final Node node = parser.Template(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return node;
			}
		}

		@Override
		public <T extends IExternalContext> IFormExpression<T> parse(final String code,
				final IEvaluationContextContractFactory<T> factory, final boolean strictMode)
				throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(factory);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				parser.setLosAllowed(true);
				final Node node = parser.Template(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return postProcess(node, parser, factory, strictMode);
			}
		}

		@Override
		public String format(final String code, final UnparseVisitorConfig config)
				throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(config);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				parser.setLosAllowed(true);
				final Node node = parser.Template(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return UnparseVisitor.unparse(node, parser.buildComments(), config);
			}
		}

		@Override
		public Iterator<Token> asTokenStream(final Reader reader) throws TokenMgrError {
			Preconditions.checkNotNull(reader);
			return new TokenIterator(asTokenManager(reader));
		}

		@Override
		public Token[] asTokenArray(final String code) throws TokenMgrError {
			Preconditions.checkNotNull(code);
			try (final StringReader reader = new StringReader(code)) {
				return tokenManagerToArray(asTokenManager(reader));
			}
		}

		@Override
		public FormExpressionParserTokenManager asTokenManager(final Reader reader) {
			Preconditions.checkNotNull(reader);
			return tokenManagerForState(reader, FormExpressionParserTokenManager.LOS);
		}
	}

	private final static class TokenIterator implements Iterator<Token> {
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
	private static Token[] tokenManagerToArray(@Nonnull final FormExpressionParserTokenManager tm)
			throws TokenMgrError {
		final List<Token> list = new ArrayList<>();
		for (Token token = tm.getNextToken(); token.kind != FormExpressionParserConstants.EOF; token = tm
				.getNextToken())
			list.add(token);
		final Token[] res = new Token[list.size()];
		list.toArray(res);
		return res;
	}

	@Nonnull
	private static <T extends IExternalContext> IFormExpression<T> postProcess(final @Nonnull Node node,
			@Nonnull final FormExpressionParser parser,
			@Nonnull final IEvaluationContextContractFactory<T> contractFactory, final boolean strictMode)
			throws ParseException {
		JumpCheckVisitor.check(node);
		final IScopeDefinitionsBuilder scopeDefBuilder = ScopeCollectVisitor.collect(node, strictMode);
		VariableHoistVisitor.hoist(node, scopeDefBuilder, contractFactory, strictMode);
		final int symbolTableSize = VariableResolveVisitor.resolve(node, scopeDefBuilder, contractFactory, strictMode);
		final IScopeDefinitions scopeDef = scopeDefBuilder.build();
		checkScopeDefsConstancy(scopeDef);
		return new FormExpressionImpl<T>(node, parser.buildComments(), scopeDef, contractFactory, symbolTableSize);
	}

	private static void checkScopeDefsConstancy(final IScopeDefinitions scopeDef)
			throws HeaderAssignmentNotCompileTimeConstantException {
		final CompileTimeConstantCheckVisitor visitor = new CompileTimeConstantCheckVisitor();
		checkScopeDefsConstancy(scopeDef.getGlobal(), visitor);
		for (final Collection<IHeaderNode> coll : scopeDef.getManual().values())
			checkScopeDefsConstancy(coll, visitor);
	}

	private static void checkScopeDefsConstancy(@Nullable final Collection<IHeaderNode> collection,
			@Nonnull final CompileTimeConstantCheckVisitor visitor)
			throws HeaderAssignmentNotCompileTimeConstantException {
		if (collection == null)
			return;
		for (final IHeaderNode hn : collection)
			if (hn.hasNode() && !hn.getNode().jjtAccept(visitor))
				throw new HeaderAssignmentNotCompileTimeConstantException(null, hn.getVariableName(), hn.getNode());
	}
}