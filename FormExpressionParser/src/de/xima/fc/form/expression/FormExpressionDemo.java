package de.xima.fc.form.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.highlight.style.HighlightThemeEclipse;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.FormExpressionEvaluationUtil;
import de.xima.fc.form.expression.util.FormExpressionHighlightingUtil;
import de.xima.fc.form.expression.util.FormExpressionParsingUtil;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;

/**
 * todo
 * - unparse / format
 * - regex literal
 * - more operators, <<  >> etc.
 * - suffix ++ --
 */
public class FormExpressionDemo {

	private static Writer writer;
	
	public static void main(final String args[]) {
		final String code = readArgs(args);
		
		showInputCode(code);

		Token[] tokenArray = showTokenStream(code);

		showHighlighting(tokenArray);
		
		final Node rootNode = showParseTree(code);

		showEvaluatedResult(rootNode);
		
		if (writer != null)
			try {
				writer.write("\n");
				writer.flush();
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}

	private static String readArgs(final String[] args) {
		if (args.length != 1) {
			System.err.println("Specify input file");
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
		System.out.println("===Input code===");
		System.out.println(code);
	}

	private static Token[] showTokenStream(final String code) {
		final Token[] tokenArray;
		try {
			final long t1 = System.nanoTime();
			tokenArray = FormExpressionParsingUtil.Program.asTokenArray(code);
			final long t2 = System.nanoTime();
			System.out.println("\nTokenizing took " + (t2-t1)/1000000 + "ms\n");
		} catch (final TokenMgrError e) {
			e.printStackTrace();
			System.exit(-1);
			return new Token[0];
		}

		System.out.println("===Token stream===");
		int charsWithoutLf = 0;
		for (Token token : tokenArray) {
			final String s = token.image.replaceAll("[ \n\r\t]", "") + " ";
			System.out.print(s);
			charsWithoutLf += s.length();
			if (charsWithoutLf > 40) {
				System.out.println();
				charsWithoutLf = 0;
			}
		}
		if (charsWithoutLf > 0) System.out.println();
		System.out.println();
		return tokenArray;
	}

	private static void showHighlighting(final Token[] tokenArray) {
		System.out.println("===Syntax highlighted HTML===");
		try {
			System.out.println(FormExpressionHighlightingUtil.highlightHtml(tokenArray,
					HighlightThemeEclipse.getInstance(), null, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	private static Node showParseTree(final String code) {
		final Node rootNode;
		try {
			final long t1 = System.nanoTime();
			rootNode = FormExpressionParsingUtil.Program.parse(code);
			final long t2 = System.nanoTime();
			System.out.println("\nParsing took " + (t2-t1)/1000000 + "ms\n");
		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}

		System.out.println("\n===Parse tree===");
		try {
			rootNode.jjtAccept(DumpVisitor.getSystemOutDumper(), StringUtils.EMPTY);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		System.out.println();
		return rootNode;
	}

	private static void showUnparsed(final Node rootNode) {
		final UnparseVisitor unparse = new UnparseVisitor();
		System.out.println("Unparse:");
		System.out.println(rootNode.jjtAccept(unparse, new Integer(0)).toString());
		System.out.println();
	}

	private static void showEvaluatedResult(final Node rootNode) {
		final ALangObject result;
		try {
			// Do it once so we don't measure setup times.
			FormExpressionEvaluationUtil.Formcycle.eval(rootNode, getFec());

			// Measure how long it takes in practice.
			final long t1 = System.nanoTime();
			result = FormExpressionEvaluationUtil.Formcycle.eval(rootNode, getFec());
			final long t2 = System.nanoTime();
			
			System.out.println("Evaluation took " + (t2-t1)/1000000 + "ms\n");
		}
		catch (final EvaluationException e) {
			System.err.println("Failed to evaluate expression.");
			e.printStackTrace(System.err);
			System.exit(-1);
			return;
		} catch (final Exception e) {
			System.err.println("Unhandled exception!!!");
			e.printStackTrace(System.err);
			System.exit(-1);
			return;
		}

		System.out.println("===Evaluated result===");
		System.out.println("toString: " + result.toString());
		System.out.println("inspect: " + result.inspect());
	}

	private static FormcycleExternalContext getFec() {
		return new FormcycleExternalContext();
	}

}