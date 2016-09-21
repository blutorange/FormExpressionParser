package de.xima.fc.form.expression;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.GraphvizVisitor;

public class VisualizeTreeGraphviz {
	public static void main(final String args[]) {
		if (args.length == 0 || args.length > 3) {
			help();
			return;
		}

		// Parse the expression.
		final String expression = args[0];
		final ByteArrayInputStream bais = new ByteArrayInputStream(expression.getBytes(Charset.forName("UTF-8")));
		final FormExpressionParser parser = new FormExpressionParser(bais, "UTF-8");
		final Node rootNode;
		try {
			rootNode = parser.Program();
		} catch (final ParseException e) {
			System.out.println("Failed to parse expression.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// Visualize the parse tree.
		String graphviz;
		try {
			graphviz = GraphvizVisitor.withHeaderAndFooter(rootNode, expression, System.lineSeparator());
		} catch (final EvaluationException e) {
			e.printStackTrace();
			graphviz = StringUtils.EMPTY;
		}
		if (args.length > 1)
			try (FileOutputStream fos = new FileOutputStream(args[1])) {
				byte[] bytes;
				final String charset = args.length > 2 ? args[2] : "UTF-8";
				try {
					bytes = graphviz.getBytes(charset);
				} catch (final UnsupportedEncodingException e) {
					System.out.println("No such charset " + charset + "; using system default");
					bytes = graphviz.getBytes();
				}
				fos.write(bytes);
			} catch (final FileNotFoundException e) {
				System.out.println("File not found: " + args[1]);
				e.printStackTrace();
			} catch (final IOException e) {
				System.out.println("Error writing to file: " + args[1]);
				e.printStackTrace();
			}
		else
			System.out.println(graphviz);
	}

	private static void help() {
		System.out.println("Usage: visualizeTree expression [outputFile] [encoding=UTF-8]");
		System.out.println("For example, to convert the graph to a png:");
		System.out.println("./visualizeTree.jar \"(ab+cd)*ef\" out.dot && dot -T png out.dot > out.png");
	}
}