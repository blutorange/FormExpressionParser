package de.xima.fc.form.expression;

// TODO List
// - unparse, variableScopeChecker
// - EmbeddedBlocks
// - Regex literal

/**
 * Das ist ein Test [%tfVorname + §_ + tfNachname%]
 *
 * Man könnte auch mehrfache Wiederholung ermöglichen:
 */
// <ul>
// [%
//   for(val in dyn('tfVorname')
//    doc.out('<ul>', val, '</ul>')
// %]
// </ul>

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IBinding;
import de.xima.fc.form.expression.context.ICustomScope;
import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IScope;
import de.xima.fc.form.expression.context.ITracer;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericScope;
import de.xima.fc.form.expression.impl.binding.OnDemandLookUpBinding;
import de.xima.fc.form.expression.impl.scope.FormFieldScope;
import de.xima.fc.form.expression.impl.scope.FormFieldScope.FormVersion;
import de.xima.fc.form.expression.impl.scope.ReadScopedEvaluationContext.Builder;
import de.xima.fc.form.expression.impl.tracer.GenericTracer;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitor;

public class FormExpressionEvaluator {

	public static void main(final String args[]) {

		if (args.length != 1) {
			System.err.println("Specify input file");
			return;
		}
		final String string;
		try (InputStream is = new FileInputStream(args[0])) {
			string = IOUtils.toString(is);
		}
		catch (final IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Input string\n" + string);

		final ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
		final FormExpressionParser parser = new FormExpressionParser(bais, "UTF-8");
		final Node rootNode;
		try {
			final long t1 = System.nanoTime();
			rootNode = parser.Program();
			final long t2 = System.nanoTime();
			System.out.println("Parsing took " + (t2-t1)/1000000 + "ms\n");
		} catch (final ParseException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("\nParse tree");
		try {
			rootNode.jjtAccept(DumpVisitor.getSystemOutDumper(), StringUtils.EMPTY);
		} catch (final EvaluationException e) {
			// System.out probably will not throw an IOException,
			// System.out.println just catches it anyways.
			e.printStackTrace();
		}
		System.out.println("");

		final UnparseVisitor unparse = new UnparseVisitor();
		System.out.println("Unparse:");
		System.out.println(rootNode.jjtAccept(unparse, 0).toString());
		System.out.println();

		final IEvaluationContext ec = getEc();
		final EvaluateVisitor visitor = new EvaluateVisitor();

		try {
			final long t1 = System.nanoTime();
			final ALangObject result = rootNode.jjtAccept(visitor, ec);
			final long t2 = System.nanoTime();
			System.out.println("Evaluation took " + (t2-t1)/1000000 + "ms\n");

			System.out.println("Evaluated result:");
			System.out.println("toString: " + result.toString());
			System.out.println("internal: " + result.inspect());
		}
		catch (final EvaluationException e) {
			System.err.println("Failed to evaluate expression.");
			e.printStackTrace(System.err);
		} catch (final Exception e) {
			System.err.println("Unhandled exception!!!");
			e.printStackTrace(System.err);
		}
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