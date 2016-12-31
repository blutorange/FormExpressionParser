package de.xima.fc.form.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParserConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.highlighter.HtmlHighlighter;
import de.xima.fc.form.expression.highlight.style.EHighlightThemePack;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationResult;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.factory.IFormExpressionFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IToken;
import de.xima.fc.form.expression.impl.config.SeverityConfig;
import de.xima.fc.form.expression.impl.config.UnparseConfig;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractFormcycle;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.DumpVisitor;

/**
 * TODO Master to-do list.
 *
 * - Enforce boolean for && and || in strict mode, perhaps also for "!" ?
 * - Replace compile-time constant expressions with their evaluated value
 *   => also check that it works with the definite assignment analysis, eg.
 *     function foo() {
 *       var v;
 *       if ((3+3==6))
 *          v = 0;
 *       v;
 *     }
 * - Safe navigation operator: a?.b?.c or a.?b.c: only accessed the property when the value is not null, returns null otherwise.
 *   => Did you keep backward compatibility? x?.1:y must continue to work.
 *   => Consider: a?.b[x=y];
 *   => short-circuiting may occur when a is null and assignment may never happen
 *   => definite assignment visitor needs to take this into account
 *
 * - Allow parametrized dot accessors:
 *    array<boolean> arr;
 *    array<hash<string,string> mapped;
 *    method<hash<string,string>,boolean> mapper;
 *    method<array<hash<string,string>>,method<hash<string,string>,boolean>> m = arr.<hash<string,string>>map;
 *    mapped = m(mapper);
 *  => Effectively, this is simply adding additional constraints.
 *
 * - Definitely assigned visitor. See https://docs.oracle.com/javase/specs/jls/se6/html/defAssign.html and http://www.jot.fm/issues/issue_2004_10/article2.pdf
 *     Must consider these cases:
 *      - var a;if(maybe()&&(a=0));else a=0;a; // now a is definitely assigned
 *      - var a;while(maybe()||(a=0)){a=0;};a  // now a is definitely assigned
 *
 * - Check for errors even in loose config mode: "var v; v.toStr" is always wrong as there is not class on the class path with such an attr accessor => check every possible sub-type, introduce a treat_unmatching_declared_types_as_error option.
 *
 * - Add some type inference
 *    a = 9;   // a must be a number
 *    a = 2+3; // a must be a number
 *    method<string,string> m;
 *    m(a);    // a must be a string
 *    3 + b;   // b must be a number
 *
 * - A range object like (0..9) [0..9] (0..9] [0..9). Syntax?
 *
 * - Special modes for outputting text html-escaped for HTML templates.
 *
 * - unparse: los nicer
 */
public class Demo {
	@Nonnull
	private static final ISeverityConfig SEVERITY_CONFIG = SeverityConfig.getLooseConfig();
	@Nonnull
	private static final IEvaluationContextContract<Formcycle> CONTRACT_FACTORY = EEvaluationContextContractFormcycle.INSTANCE;
	@Nonnull
	private static final IFormExpressionFactory EXPRESSION_FACTORY = FormExpressionFactory.forProgram();
	@Nonnull
	private static final UnparseConfig UNPARSE_CONFIG = UnparseConfig.getStyledWithCommentsConfig();

	public static void main(final String args[]) throws TokenMgrError {
		final String code = readArgs(args);
		if (code == null)
			throw new FormExpressionException("Code must not be null."); //$NON-NLS-1$

		showInputCode(code);

		showTokenStream(code);

		showHighlighting(code);

		IFormExpression<Formcycle> expression = parseCode(code);

		if (expression == null)
			throw new FormExpressionException("Parsed expression must not be null."); //$NON-NLS-1$

		expression = showSerialization(expression);

		final Node node = showParseTree(code);

		if (node == null)
			throw new FormExpressionException("Node must not be null."); //$NON-NLS-1$

		showFormatted(code);

		showWarnings(expression);

		showEvaluatedResult(expression);
	}

	@Nonnull
	private static IFormExpression<Formcycle> showSerialization(@Nonnull final IFormExpression<Formcycle> ex) {
		System.out.println("===Serialization==="); //$NON-NLS-1$
		final byte[] bytes = SerializationUtils.serialize(ex);
		SerializationUtils.deserialize(bytes);
		final long t1 = System.nanoTime();
		final IFormExpression<Formcycle> dex = SerializationUtils.deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		if (dex == null)
			throw new FormExpressionException("Deseialized expression is null."); //$NON-NLS-1$
		return dex;
	}

	private static void showWarnings(final IFormExpression<Formcycle> expression) {
		System.out.println("===Warnings==="); //$NON-NLS-1$
		try {
			final List<IEvaluationWarning> warningList = new ArrayList<>(
					expression.analyze(new Formcycle()));
			Collections.sort(warningList, IEvaluationWarning.COMPARATOR);
			for (final IEvaluationWarning warning : warningList) {
				System.out.println(
						String.format("Warning from line %d, column %d: %s", Integer.valueOf(warning.getBeginLine()), //$NON-NLS-1$
								Integer.valueOf(warning.getBeginColumn()), warning.getMessage()));
				System.out.println();
			}
		}
		catch (final EvaluationException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println();
	}

	private static String readArgs(final String[] args) {
		if (args.length != 1) {
			System.err.println("Specify input file"); //$NON-NLS-1$
			System.exit(-1);
			return null;
		}
		try (InputStream is = new FileInputStream(args[0])) {
			return IOUtils.toString(is);
		}
		catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	private static void showInputCode(final String code) {
		System.out.println("===Input code==="); //$NON-NLS-1$
		System.out.println(code);
	}

	private static void showTokenStream(@Nonnull final String code) {
		final Iterator<IToken> tokenStream;
		try {
			final long t1 = System.nanoTime();
			tokenStream = EXPRESSION_FACTORY.asTokenStream(code);
			final long t2 = System.nanoTime();
			System.out.println("\nTokenizing took " + (t2 - t1) / 1000000 + "ms\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (final TokenMgrError e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		System.out.println("===Token stream==="); //$NON-NLS-1$
		int charsWithoutLf = 0;
		for (final IToken token : IteratorUtils.asIterable(tokenStream)) {
			final String s;
			if (token.getKind() == FormExpressionParserConstants.TemplateLiteralChars)
				s = token.getImage() + " "; //$NON-NLS-1$
			else
				s = token.getImage().replaceAll("[ \n\r\t]", "") + " "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			System.out.print(s);
			charsWithoutLf += s.length();
			if (charsWithoutLf > 40) {
				System.out.println();
				charsWithoutLf = 0;
			}
		}
		if (charsWithoutLf > 0)
			System.out.println();
		System.out.println();
	}

	private static void showHighlighting(@Nonnull final String code) {
		System.out.println("===Syntax highlighted HTML==="); //$NON-NLS-1$
		try {
			final HtmlHighlighter highlighter = HtmlHighlighter.create();
			EXPRESSION_FACTORY.highlight(code, highlighter, EHighlightThemePack.ECLIPSE);
			System.out.println(highlighter.getHtmlWithInlinedCss());
		}
		catch (final ParseException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	private static IFormExpression<Formcycle> parseCode(@Nonnull final String code) {
		final IFormExpression<Formcycle> ex;
		try {
			final long t1 = System.nanoTime();
			ex = EXPRESSION_FACTORY.compile(code, CONTRACT_FACTORY, SEVERITY_CONFIG);
			final long t2 = System.nanoTime();
			System.out.println("\nParsing took " + (t2 - t1) / 1000000 + "ms\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (final ParseException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		return ex;
	}

	public static Node asNode(final IFormExpressionFactory factory, final String code)
			throws ParseException, TokenMgrError, IllegalArgumentException {
		final Method method;
		try {
			method = factory.getClass().getDeclaredMethod("asNode", String.class); //$NON-NLS-1$
			final Class<?> clazz = method.getReturnType();
			if (!Node.class.isAssignableFrom(clazz))
				throw new IllegalArgumentException("return type not a node, but " + clazz); //$NON-NLS-1$
			method.setAccessible(true);
			return (Node) method.invoke(factory, code);
		}
		catch (final NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
		catch (final SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		catch (final IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
		catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		catch (final InvocationTargetException e) {
			final Throwable t = e.getTargetException();
			if (t instanceof ParseException)
				throw (ParseException)t;
			if (t instanceof TokenMgrError)
				throw (TokenMgrError)t;
			if (t instanceof RuntimeException)
				throw (RuntimeException)t;
			if (t instanceof Error)
				throw (Error)t;
			throw new IllegalArgumentException(e);
		}
	}

	private static Node showParseTree(@Nonnull final String code) {
		final Node node;
		try {
			node = asNode(EXPRESSION_FACTORY, code);
		}
		catch (final TokenMgrError e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		catch (final ParseException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}

		System.out.println("\n===Parse tree==="); //$NON-NLS-1$
		try {
			node.jjtAccept(DumpVisitor.getSystemOutDumper(), CmnCnst.NonnullConstant.STRING_EMPTY);
		}
		catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		System.out.println();
		return node;
	}

	private static void showFormatted(@Nonnull final String code) {
		final String format;
		try {
			format = EXPRESSION_FACTORY.format(code, UNPARSE_CONFIG);
		}
		catch (final ParseException e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		System.out.println("===Unparse==="); //$NON-NLS-1$
		System.out.println(format);
		System.out.println();
	}

	private static void showEvaluatedResult(@Nonnull final IFormExpression<Formcycle> ex) {
		System.out.println("===Evaluation==="); //$NON-NLS-1$
		final IEvaluationResult result;
		try {
			// Do it once so we don't measure setup times.
			ex.evaluate(new Formcycle());
			System.out.println();

			// Measure how long it takes in practice.
			final long t1 = System.nanoTime();
			result = ex.evaluate(new Formcycle());
			final long t2 = System.nanoTime();

			System.out.println();
			System.out.println();
			System.out.println("Evaluation took " + (t2 - t1) / 1000000 + "ms\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (final EvaluationException e) {
			System.err.println("Failed to evaluate expression."); //$NON-NLS-1$
			e.printStackTrace(System.err);
			System.exit(-1);
			return;
		}
		catch (final Exception e) {
			System.err.println("Unhandled exception!!!"); //$NON-NLS-1$
			e.printStackTrace(System.err);
			System.exit(-1);
			return;
		}

		System.out.println("===Evaluated result==="); //$NON-NLS-1$
		System.out.println("toString: " + result.getObject().toString()); //$NON-NLS-1$
		System.out.println("inspect: " + result.getObject().inspect()); //$NON-NLS-1$

		System.out.println();
		System.out.println();

		System.out.println("===Evaluation warnings==="); //$NON-NLS-1$
		final List<IEvaluationWarning> warnings = result.getWarnings();
		Collections.sort(warnings, IEvaluationWarning.COMPARATOR);
		for (final IEvaluationWarning warning : result.getWarnings())
			System.out.println(warning);
	}
}
