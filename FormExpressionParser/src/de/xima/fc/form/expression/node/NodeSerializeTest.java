package de.xima.fc.form.expression.node;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.SerializationUtils;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.DumpVisitor;

public final class NodeSerializeTest {

	public static void main(final String[] args) {
		final String string = "if('') 8 ; else 42;";
		System.out.println("Input string\n" + string);
		final ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
		final FormExpressionParser parser = new FormExpressionParser(bais, "UTF-8");
		final Node rootNode;
		try {
			final long t1 = System.nanoTime();
			rootNode = parser.Program();
			final long t2 = System.nanoTime();
			System.out.println("Parsing took " + (t2-t1)/1000000 + "ms");
		} catch (final ParseException e) {
			e.printStackTrace();
			return;
		}

		final byte[] bytes = SerializationUtils.serialize(rootNode);
		final long t1 = System.nanoTime();
		final Node n = SerializationUtils.<Node>deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms");
		try {
			n.jjtAccept(DumpVisitor.getSystemOutDumper(), "");
		} catch (final EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
