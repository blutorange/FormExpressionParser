package de.xima.fc.form.expression;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;
import de.xima.fc.form.expression.visitor.GraphvizVisitor;

public class VisualizeTreeGraphviz {
	public static void main(final String args[]) {
		if (args.length == 0 || args.length > 3) {
			help();
			return;
		}

		// Parse the expression.
		final String expression = args[0];
		if (expression == null)
			throw new FormExpressionException("First argument (code) must not be null."); //$NON-NLS-1$
		final Node rootNode;
		try {
			rootNode = Demo.asNode(FormExpressionFactory.forProgram(), expression);
		}
		catch (final TokenMgrError e) {
			System.out.println("Failed to parse expression."); //$NON-NLS-1$
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		catch (final ParseException e) {
			System.out.println("Failed to parse expression."); //$NON-NLS-1$
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// Visualize the parse tree.
		String graphviz;
		try {
			graphviz = GraphvizVisitor.withHeaderAndFooter(rootNode, expression, System.lineSeparator());
		} catch (final IOException e) {
			e.printStackTrace();
			graphviz = StringUtils.EMPTY;
		}
		if (args.length > 1)
			try (FileOutputStream fos = new FileOutputStream(args[1])) {
				byte[] bytes;
				final String charset = args.length > 2 ? args[2] : "UTF-8"; //$NON-NLS-1$
				try {
					bytes = graphviz.getBytes(charset);
				} catch (final UnsupportedEncodingException e) {
					System.out.println("No such charset " + charset + "; using system default."); //$NON-NLS-1$ //$NON-NLS-2$
					e.printStackTrace(System.err);
					bytes = graphviz.getBytes();
				}
				fos.write(bytes);
			} catch (final FileNotFoundException e) {
				System.out.println("File not found: " + args[1]); //$NON-NLS-1$
				e.printStackTrace();
			} catch (final IOException e) {
				System.out.println("Error writing to file: " + args[1]); //$NON-NLS-1$
				e.printStackTrace();
			}
		else
			System.out.println(graphviz);
	}

	private static void help() {
		System.out.println("Usage: visualizeTree expression [outputFile] [encoding=UTF-8]"); //$NON-NLS-1$
		System.out.println("For example, to convert the graph to a png:"); //$NON-NLS-1$
		System.out.println("./visualizeTree.jar \"(ab+cd)*ef\" out.dot && dot -T png out.dot > out.png"); //$NON-NLS-1$
	}
}