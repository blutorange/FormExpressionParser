package de.xima.fc.form.expression;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.grammar.Token;
import de.xima.fc.form.expression.grammar.TokenMgrError;
import de.xima.fc.form.expression.impl.GenericScope;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;
import de.xima.fc.form.expression.impl.scope.FormFieldScope;
import de.xima.fc.form.expression.impl.scope.FormFieldScope.FormVersion;
import de.xima.fc.form.expression.impl.scope.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.FormExpressionParseFactory;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;

public class FormExpressionDemo {

	public static void main(final String args[]) {
		final String code = readArgs(args);

		showInputCode(code);

		showTokenStream(code);

		final Node rootNode = showParseTree(code);

		showEvaluatedResult(rootNode);
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
		} catch (final EvaluationException e) {
			// System.out probably will not throw an IOException,
			// System.out.println just catches it anyways.
			e.printStackTrace();
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
			//final EvaluateProcessor processor = new EvaluateProcessor(getEc());
			final long t1 = System.nanoTime();
			result = EvaluateVisitor.evaluateProgram(rootNode, getEc());
			//final ALangObject result = processor.process(rootNode);
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

	private static IScope getScope() {
		final ICustomScope formFieldScope = new FormFieldScope(new FormVersion());
		final GenericScope.Builder builder = new GenericScope.Builder();
		builder.addCustomScope(formFieldScope);
		return builder.build();
	}

	private static IEvaluationContext getEc() {
		final IBinding binding = new OnDemandLookUpBinding();
		final ITracer<Node> tracer = new GenericTracer();
		final Builder builder = new Builder();
		final IScope scope = getScope();
		builder.setBinding(binding);
		builder.setScope(scope);
		builder.setTracer(tracer);
		return builder.build();
	}
}