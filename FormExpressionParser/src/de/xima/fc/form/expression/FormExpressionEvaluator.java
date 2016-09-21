package de.xima.fc.form.expression;

// TODO List
// - ASTNodes Serializable, so that a parse can be stored in the database.
// - EmbeddedBlocks

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
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.impl.formfield.FormFieldEvaluationBinding;
import de.xima.fc.form.expression.impl.formfield.FormFieldNamespaceBuilder;
import de.xima.fc.form.expression.impl.function.EAttrAccessorArray;
import de.xima.fc.form.expression.impl.function.EAttrAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAccessorHash;
import de.xima.fc.form.expression.impl.function.EAttrAccessorNumber;
import de.xima.fc.form.expression.impl.function.EAttrAccessorString;
import de.xima.fc.form.expression.impl.function.EGlobalFunction;
import de.xima.fc.form.expression.impl.function.EInstanceMethodArray;
import de.xima.fc.form.expression.impl.function.EInstanceMethodBoolean;
import de.xima.fc.form.expression.impl.function.EInstanceMethodHash;
import de.xima.fc.form.expression.impl.function.EInstanceMethodNumber;
import de.xima.fc.form.expression.impl.function.EInstanceMethodString;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public class FormExpressionEvaluator {
	public static void main(final String args[]) {

		final String string = "if(a) { if (b) { do(); } }";

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

		System.exit(0);

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

	private static IEvaluationContext getEc() {
		return new GenericEvaluationContext(new FormFieldEvaluationBinding(), getNamespace());
	}

	private static INamespace getNamespace() {
		final FormFieldNamespaceBuilder builder = new FormFieldNamespaceBuilder(null);

		builder.setGlobalMethod(EGlobalFunction.values());

		builder.setInstanceMethodBoolean(EInstanceMethodBoolean.values());
		builder.setInstanceMethodNumber(EInstanceMethodNumber.values());
		builder.setInstanceMethodString(EInstanceMethodString.values());
		builder.setInstanceMethodArray(EInstanceMethodArray.values());
		builder.setInstanceMethodHash(EInstanceMethodHash.values());

		builder.setAttrAccessorBoolean(EAttrAccessorBoolean.values());
		builder.setAttrAccessorNumber(EAttrAccessorNumber.values());
		builder.setAttrAccessorString(EAttrAccessorString.values());
		builder.setAttrAccessorArray(EAttrAccessorArray.values());
		builder.setAttrAccessorHash(EAttrAccessorHash.values());

		return builder.build();
	}
}
