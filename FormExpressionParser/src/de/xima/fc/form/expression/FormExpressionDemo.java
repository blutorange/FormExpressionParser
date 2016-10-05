package de.xima.fc.form.expression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.FormExpressionEvaluationUtil;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;

/**
 * todo
 * - check scope for embedded blocks when used with with-clauses, are they ended properly, correct order???
 * - unparse
 * - resetting iEmbedment implementations
 */
public class FormExpressionDemo {

	private static Writer writer;
	
	public static void main(final String args[]) {
		final String code = readArgs(args);

		showInputCode(code);

		showTokenStream(code);

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

	private static void showTokenStream(final String code) {
		final Token[] tokenList;
		try {
			final long t1 = System.nanoTime();
			tokenList = FormExpressionParseFactory.Template.asTokenStream(code);
			final long t2 = System.nanoTime();
			System.out.println("\nTokenizing took " + (t2-t1)/1000000 + "ms\n");
		} catch (final TokenMgrError e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		System.out.println("===Token stream===");
		int charsWithoutLf = 0;
		for (final Token token : tokenList) {
			final String s = token.image.replaceAll("[ \n\r\t]", "") + " ";
			System.out.print(s);
			charsWithoutLf += s.length();
			if (charsWithoutLf > 40) {
				System.out.println();
				charsWithoutLf = 0;
			}
		}
		if (charsWithoutLf > 0) System.out.println();
	}

	private static Node showParseTree(final String code) {
		final Node rootNode;
		try {
			final long t1 = System.nanoTime();
			rootNode = FormExpressionParseFactory.Template.parse(code);
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
		System.out.println(rootNode.jjtAccept(unparse, 0).toString());
		System.out.println();
	}


	private static void showEvaluatedResult(final Node rootNode) {
		final ALangObject result;
		try {
			// Do it once so we don't measure setup times.
			FormExpressionEvaluationUtil.Formcycle.eval(rootNode, getWriter());

			// Measure how long it takes in practice.
			final long t1 = System.nanoTime();
			result = FormExpressionEvaluationUtil.Formcycle.eval(rootNode, getWriter());
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

	private static Writer getWriter() {
		if (writer != null) return writer;
		try {
			writer = new FileWriter(new File("/tmp/fep_embed_out.html"));
		} catch (IOException e) {
			e.printStackTrace();
			writer = new OutputStreamWriter(System.out);
		}
		return writer;
	}

}