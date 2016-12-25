package de.xima.fc.form.expression.impl.formexpression;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.enums.ESeverityOption;
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
import de.xima.fc.form.expression.iface.IFormExpressionHighlightTheme;
import de.xima.fc.form.expression.iface.IFormExpressionHighlighter;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.config.IUnparseConfig;
import de.xima.fc.form.expression.iface.factory.IFormExpressionFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitions;
import de.xima.fc.form.expression.iface.parse.IScopeDefinitionsBuilder;
import de.xima.fc.form.expression.iface.parse.IToken;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.CompileTimeConstantCheckVisitor;
import de.xima.fc.form.expression.visitor.JumpCheckVisitor;
import de.xima.fc.form.expression.visitor.ScopeCollectVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.VariableHoistVisitor;
import de.xima.fc.form.expression.visitor.VariableResolveVisitor;
import de.xima.fc.form.expression.visitor.VariableTypeCheckVisitor;
import de.xima.fc.form.expression.visitor.VariableTypeCollectVisitor;

@ParametersAreNonnullByDefault
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
	public static IFormExpressionFactory forTemplate() {
		return TemplateImpl.INSTANCE;
	}

	/**
	 * Methods for parsing code as one complete program.
	 *
	 * @see #forTemplate()
	 */
	public static IFormExpressionFactory forProgram() {
		return ProgramImpl.INSTANCE;
	}

	private static enum ProgramImpl implements IFormExpressionFactory {
		INSTANCE;

		@Override
		@Nonnull
		public <T> IFormExpression<T> compile(@Nonnull final String code,
				@Nonnull final IEvaluationContextContract<T> factory, final ISeverityConfig config)
						throws ParseException, TokenMgrError, SemanticsException {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(factory);
			Preconditions.checkNotNull(config);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				final Node node = parser.CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return postProcess(node, parser, factory, config);
			}
		}

		@Override
		@Nonnull
		public String format(@Nonnull final String code, @Nonnull final IUnparseConfig config) throws ParseException, TokenMgrError {
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

		// Called reflectively by Demo.asNode(String code), only for testing.
		@SuppressWarnings("unused")
		private Node asNode(final String code) throws ParseException, TokenMgrError {
			try (final StringReader reader = new StringReader(code)) {
				final Node node = asParser(asTokenManager(reader)).CompleteProgram(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return node;
			}
		}

		private FormExpressionParserTokenManager asTokenManager(final Reader reader) {
			return tokenManagerForState(reader, FormExpressionParserTokenManager.CODE);
		}
		
		@Override
		public void highlight(final String code, final IFormExpressionHighlighter highlighter,
				final IFormExpressionHighlightTheme theme) throws IOException, ParseException {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(highlighter);
			Preconditions.checkNotNull(theme);
			try (StringReader reader = new StringReader(code)) {
				highlighter.process(new TokenIterator(asTokenManager(reader)), theme);
			}
		}

		@Override
		public Iterator<IToken> asTokenStream(final String code) throws TokenMgrError {
			Preconditions.checkNotNull(code);
			try (StringReader reader = new StringReader(code)) {
				return NullUtil.iterator(tokenManagerToList(asTokenManager(reader)));
			}
		}
	}

	private static enum TemplateImpl implements IFormExpressionFactory {
		INSTANCE;

		// Called reflectively by Demo.asNode(String code), only for testing.
		@SuppressWarnings("unused")
		private Node asNode(final String code) throws ParseException, TokenMgrError {
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
		public <T> IFormExpression<T> compile(final String code,
				final IEvaluationContextContract<T> factory, final ISeverityConfig config)
						throws ParseException, TokenMgrError {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(factory);
			Preconditions.checkNotNull(config);
			try (final StringReader reader = new StringReader(code)) {
				final FormExpressionParser parser = asParser(asTokenManager(reader));
				parser.setLosAllowed(true);
				final Node node = parser.Template(null);
				if (node == null)
					throw new ParseException(CmnCnst.Error.PARSER_RETURNED_NULL_NODE);
				return postProcess(node, parser, factory, config);
			}
		}

		@Override
		public String format(final String code, final IUnparseConfig config)
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
		public void highlight(final String code, final IFormExpressionHighlighter highlighter,
				final IFormExpressionHighlightTheme theme) throws IOException, ParseException {
			Preconditions.checkNotNull(code);
			Preconditions.checkNotNull(highlighter);
			Preconditions.checkNotNull(theme);
			try (StringReader reader = new StringReader(code)) {
				highlighter.process(new TokenIterator(asTokenManager(reader)), theme);
			}
		}

		@Override
		public Iterator<IToken> asTokenStream(final String code) throws TokenMgrError {
			Preconditions.checkNotNull(code);
			try (StringReader reader = new StringReader(code)) {
				return NullUtil.iterator(tokenManagerToList(asTokenManager(reader)));
			}
		}
		
	}

	private static FormExpressionParserTokenManager asTokenManager(final Reader reader) {
		return tokenManagerForState(reader, FormExpressionParserTokenManager.LOS);
	}
	
	private final static class TokenIterator implements Iterator<Token> {
		private boolean hasNext = true;
		private final FormExpressionParserTokenManager tm;

		public TokenIterator(final FormExpressionParserTokenManager tm) {
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

	private static FormExpressionParserTokenManager tokenManagerForState(final Reader reader, final int state)
			throws TokenMgrError {
		final SimpleCharStream stream = new SimpleCharStream(reader);
		return new FormExpressionParserTokenManager(null, stream, state);
	}

	private static FormExpressionParser asParser(final FormExpressionParserTokenManager tm) {
		final FormExpressionParser parser = new FormExpressionParser(tm);
		tm.parser = parser;
		return parser;
	}

	private static List<IToken> tokenManagerToList(final FormExpressionParserTokenManager tm)
			throws TokenMgrError {
		final List<IToken> list = new ArrayList<>();
		for (Token token = tm.getNextToken(); token.kind != FormExpressionParserConstants.EOF; token = tm
				.getNextToken())
			list.add(token);
		return list;
	}

	/**
	 * Performs several static semantic code checks.
	 * <ul>
	 *   <li>Whether all <code>break</code>, <code>continue</code>, and <code>return</code> statements are legal.</li>
	 *   <li>Whether the initial value of all variables declared within a scope block is compile-time constant.</li>
	 *   <li>Whether all variables used were declared.</li>
	 *   <li>Whether all declared variable types match.</li>
	 * </ul>
	 * @param node
	 * @param parser
	 * @param factory
	 * @param strictMode
	 * @return
	 * @throws ParseException
	 */
	private static <T> IFormExpression<T> postProcess(final Node node,
			final FormExpressionParser parser,
			final IEvaluationContextContract<T> factory,
			final ISeverityConfig severityConfig) throws ParseException {
		JumpCheckVisitor.check(node);
		final IScopeDefinitionsBuilder scopeDefBuilder = ScopeCollectVisitor.collect(node, factory, severityConfig);
		VariableHoistVisitor.hoist(node, scopeDefBuilder, factory, severityConfig);
		final int symbolTableSize = VariableResolveVisitor.resolve(node, scopeDefBuilder, factory,
				severityConfig);
		final IScopeDefinitions scopeDefs = scopeDefBuilder.build();
		checkScopeDefsConstancy(scopeDefs);
		if (severityConfig.hasOption(ESeverityOption.TREAT_UNMATCHING_VARIABLE_TYPES_AS_ERROR)) {
			final IVariableType[] symbolTypeTable = VariableTypeCollectVisitor.collect(node, symbolTableSize, scopeDefs);
			VariableTypeCheckVisitor.check(node, symbolTypeTable, factory, severityConfig, scopeDefs);
		}
		return new FormExpressionImpl<>(node, parser.buildComments(), scopeDefs, factory, symbolTableSize);
	}

	private static void checkScopeDefsConstancy(final IScopeDefinitions scopeDef)
			throws HeaderAssignmentNotCompileTimeConstantException {
		final CompileTimeConstantCheckVisitor visitor = new CompileTimeConstantCheckVisitor();
		checkScopeDefsConstancy(scopeDef.getGlobal(), visitor);
		for (final Collection<IHeaderNode> coll : scopeDef.getManual().values())
			checkScopeDefsConstancy(coll, visitor);
	}

	private static void checkScopeDefsConstancy(@Nullable final Collection<IHeaderNode> collection,
			final CompileTimeConstantCheckVisitor visitor)
					throws HeaderAssignmentNotCompileTimeConstantException {
		if (collection == null)
			return;
		for (final IHeaderNode hn : collection)
			if (!hn.getNode().jjtAccept(visitor))
				throw new HeaderAssignmentNotCompileTimeConstantException(null, hn.getVariableName(), hn.getNode());
	}
}