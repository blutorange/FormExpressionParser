package de.xima.fc.form.expression;

// TODO List
// - +=, -=, = etc. ( a += b ==> a = a+b)
// - Functions as LangObjects, print('me') is evaluated as print -> function(){} -> functionCall
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

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamespace;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.GenericEvaluationContext;
import de.xima.fc.form.expression.impl.GenericNamespace;
import de.xima.fc.form.expression.impl.formfield.FormFieldEvaluationBinding;
import de.xima.fc.form.expression.impl.function.EAttrAccessorArray;
import de.xima.fc.form.expression.impl.function.EAttrAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAccessorException;
import de.xima.fc.form.expression.impl.function.EAttrAccessorFunction;
import de.xima.fc.form.expression.impl.function.EAttrAccessorHash;
import de.xima.fc.form.expression.impl.function.EAttrAccessorNumber;
import de.xima.fc.form.expression.impl.function.EAttrAccessorString;
import de.xima.fc.form.expression.impl.function.EExpressionMethodArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodBoolean;
import de.xima.fc.form.expression.impl.function.EExpressionMethodException;
import de.xima.fc.form.expression.impl.function.EExpressionMethodFunction;
import de.xima.fc.form.expression.impl.function.EExpressionMethodHash;
import de.xima.fc.form.expression.impl.function.EExpressionMethodNumber;
import de.xima.fc.form.expression.impl.function.EExpressionMethodString;
import de.xima.fc.form.expression.impl.function.GenericAttrAccessor;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.visitor.DumpVisitor;
import de.xima.fc.form.expression.visitor.EvaluateVisitor;

public class FormExpressionEvaluator {

	private final static INamespace NAMESPACE;
	static {
		final GenericNamespace.Builder builder = new GenericNamespace.Builder();

		builder.addExpressionMethodBoolean(EExpressionMethodBoolean.values());
		builder.addExpressionMethodNumber(EExpressionMethodNumber.values());
		builder.addExpressionMethodString(EExpressionMethodString.values());
		builder.addExpressionMethodArray(EExpressionMethodArray.values());
		builder.addExpressionMethodHash(EExpressionMethodHash.values());
		builder.addExpressionMethodException(EExpressionMethodException.values());
		builder.addExpressionMethodFunction(EExpressionMethodFunction.values());

		builder.addAttrAccessorBoolean(EAttrAccessorBoolean.values());
		builder.addAttrAccessorNumber(EAttrAccessorNumber.values());
		builder.addAttrAccessorString(EAttrAccessorString.values());
		builder.addAttrAccessorArray(EAttrAccessorArray.values());
		builder.addAttrAccessorHash(EAttrAccessorHash.values());
		builder.addAttrAccessorException(EAttrAccessorException.values());
		builder.addAttrAccessorFunction(EAttrAccessorFunction.values());

		builder.setGenericAttrAccessorArray(GenericAttrAccessor.ARRAY);
		builder.setGenericAttrAccessorBoolean(GenericAttrAccessor.BOOLEAN);
		builder.setGenericAttrAccessorException(GenericAttrAccessor.EXCEPTION);
		builder.setGenericAttrAccessorFunction(GenericAttrAccessor.FUNCTION);
		builder.setGenericAttrAccessorHash(GenericAttrAccessor.HASH);
		builder.setGenericAttrAccessorNumber(GenericAttrAccessor.NUMBER);
		builder.setGenericAttrAccessorString(GenericAttrAccessor.STRING);

		NAMESPACE = builder.build();
	}

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

		//		System.exit(0);

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
		return new GenericEvaluationContext(new FormFieldEvaluationBinding(), NAMESPACE);
	}
}
