package de.xima.fc.form.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.style.HighlightThemeEclipse;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationResult;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationWarning;
import de.xima.fc.form.expression.iface.factory.IFormExpressionFactory;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextContract;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.impl.config.SeverityConfig;
import de.xima.fc.form.expression.impl.config.UnparseConfig;
import de.xima.fc.form.expression.impl.ec.EEvaluationContextContractFormcycle;
import de.xima.fc.form.expression.impl.externalcontext.Formcycle;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.FormExpressionHighlightingUtil;
import de.xima.fc.form.expression.visitor.DumpVisitor;

/**
 * TODO
 * - check all serializable / immutable classes
 * - replace logger with tracer ?
 * - unparse: los nicer
 * - support closures for lambda expressions (=> for each function call, get a unique callID, create a separate set of values for each closure variable)
 * - update formatting js
 * - update highlighter with new token types (global, scope, require etc)
 * - everything returns a value; in strict mode, check that functions return explicitly
 */
public class Demo {
	@Nonnull
	private static final ISeverityConfig SEVERITY_CONFIG = SeverityConfig.getStrictConfig();
	@Nonnull
	private static final IEvaluationContextContract<Formcycle> CONTRACT_FACTORY = EEvaluationContextContractFormcycle.INSTANCE;
	@Nonnull
	private static final IFormExpressionFactory EXPRESSION_FACTORY = FormExpressionFactory.forProgram();
	@Nonnull
	private static final UnparseConfig UNPARSE_CONFIG = UnparseConfig.getStyledWithCommentsConfig();

	public static void main(final String args[]) {
		final String code = readArgs(args);
		if (code == null)
			throw new FormExpressionException("Code must not be null."); //$NON-NLS-1$

		showInputCode(code);

		final Token[] tokenArray = showTokenStream(code);

		showHighlighting(tokenArray);

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
		System.out.println("===Serialization===");
		final byte[] bytes = SerializationUtils.serialize(ex);
		SerializationUtils.deserialize(bytes);
		final long t1 = System.nanoTime();
		final IFormExpression<Formcycle> dex = SerializationUtils.deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms");
		if (dex == null)
			throw new FormExpressionException("Deseialized expression is null.");
		return dex;
	}

	private static void showWarnings(final IFormExpression<Formcycle> expression) {
		System.out.println("===Warnings==="); //$NON-NLS-1$
		try {
			final List<IEvaluationWarning> warningList = new ArrayList<>(
					expression.analyze(new Formcycle()));
			Collections.sort(warningList, IEvaluationWarning.COMPARATOR);
			for (final IEvaluationWarning warning : warningList) {
				System.out.println(String.format("Warning from line %d, column %d: %s", warning.getStartLine(),
						warning.getStartColumn(), warning.getMessage()));
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

	private static IFormExpression<Formcycle> parseCode(@Nonnull final String code) {
		final IFormExpression<Formcycle> ex;
		try {
			final long t1 = System.nanoTime();
			ex = EXPRESSION_FACTORY.parse(code, CONTRACT_FACTORY, SEVERITY_CONFIG);
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

	private static void showEvaluatedResult(@Nonnull final IFormExpression<Formcycle> ex) {
		System.out.println("===Evaluation===");
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