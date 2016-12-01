package de.xima.fc.form.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.style.HighlightThemeEclipse;
import de.xima.fc.form.expression.iface.context.IEvaluationWarning;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContractFactory;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.iface.parse.IFormExpressionFactory;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.impl.factory.FormcycleEcContractFactory;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.FormExpressionHighlightingUtil;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

/**
 * TODO - unparse: los nicer - optional variable types - update formatting js -
 * update highlighter with new token types (global, scope, require etc) - check
 * for used/unused variables (especially this and arguments variable in function
 * body) - make it possible to check variables or external scopes provided by
 * the external context for a particular external context without evaluating it
 * => for example to check whether the current form version defines all
 * field::.. variables
 */
public class FormExpressionDemo {
	private static final boolean STRICT_MODE = true;
	@Nonnull
	private static final IEvaluationContextContractFactory<FormcycleExternalContext> CONTRACT_FACTORY = FormcycleEcContractFactory.INSTANCE;
	@Nonnull
	private static final IFormExpressionFactory EXPRESSION_FACTORY = FormExpressionFactory.forProgram();
	@Nonnull
	private static final UnparseVisitorConfig UNPARSE_CONFIG = UnparseVisitorConfig.getUnstyledWithoutCommentsConfig();

	public static void main(final String args[]) {
		final String code = readArgs(args);
		if (code == null)
			throw new RuntimeException("Code must not be null."); //$NON-NLS-1$

		showInputCode(code);

		final Token[] tokenArray = showTokenStream(code);

		showHighlighting(tokenArray);

		IFormExpression<FormcycleExternalContext> expression = parseCode(code);

		if (expression == null)
			throw new RuntimeException("Parsed expression must not be null."); //$NON-NLS-1$

		expression = showSerialization(expression);

		final Node node = showParseTree(code);

		if (node == null)
			throw new RuntimeException("Node must not be null."); //$NON-NLS-1$

		showFormatted(code);

		showWarnings(expression);

		showEvaluatedResult(expression);
	}

	@Nonnull
	private static IFormExpression<FormcycleExternalContext> showSerialization(@Nonnull final IFormExpression<FormcycleExternalContext> ex) {
		System.out.println("===Serialization===");
		final byte[] bytes = SerializationUtils.serialize(ex);
		SerializationUtils.deserialize(bytes);
		final long t1 = System.nanoTime();
		final IFormExpression<FormcycleExternalContext> dex = SerializationUtils.deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms");
		if (dex == null)
			throw new RuntimeException("Deseialized expression is null.");
		return dex;
	}

	private static void showWarnings(final IFormExpression<FormcycleExternalContext> expression) {
		System.out.println("===Warnings==="); //$NON-NLS-1$
		try {
			for (final IEvaluationWarning warning : expression.analyze(new FormcycleExternalContext())) {
				System.out.println(String.format("Warning from line %d, column %d: %s", warning.getLine(),
						warning.getColumn(), warning.getMessage()));
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

	@Nonnull
	private static Token[] showTokenStream(@Nonnull final String code) {
		final Token[] tokenArray;
		try {
			final long t1 = System.nanoTime();
			tokenArray = EXPRESSION_FACTORY.asTokenArray(code);
			final long t2 = System.nanoTime();
			System.out.println("\nTokenizing took " + (t2 - t1) / 1000000 + "ms\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (final TokenMgrError e) {
			e.printStackTrace();
			System.exit(-1);
			return new Token[0];
		}

		System.out.println("===Token stream==="); //$NON-NLS-1$
		int charsWithoutLf = 0;
		for (final Token token : tokenArray) {
			final String s = token.image.replaceAll("[ \n\r\t]", "") + " "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		return tokenArray;
	}

	private static void showHighlighting(@Nonnull final Token[] tokenArray) {
		System.out.println("===Syntax highlighted HTML==="); //$NON-NLS-1$
		try {
			System.out.println(FormExpressionHighlightingUtil.highlightHtml(tokenArray,
					HighlightThemeEclipse.getInstance(), null, true));
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	private static IFormExpression<FormcycleExternalContext> parseCode(@Nonnull final String code) {
		final IFormExpression<FormcycleExternalContext> ex;
		try {
			final long t1 = System.nanoTime();
			ex = EXPRESSION_FACTORY.parse(code, CONTRACT_FACTORY, STRICT_MODE);
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

	private static Node showParseTree(@Nonnull final String code) {
		final Node node;
		try {
			node = EXPRESSION_FACTORY.asNode(code);
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

	private static void showEvaluatedResult(@Nonnull final IFormExpression<FormcycleExternalContext> ex) {
		final ALangObject result;
		try {
			// Do it once so we don't measure setup times.
			ex.evaluate(new FormcycleExternalContext());

			// Measure how long it takes in practice.
			final long t1 = System.nanoTime();
			result = ex.evaluate(new FormcycleExternalContext());
			final long t2 = System.nanoTime();

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
		System.out.println("toString: " + result.toString()); //$NON-NLS-1$
		System.out.println("inspect: " + result.inspect()); //$NON-NLS-1$
	}
}