package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.parse.IFormExpression;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;

final class NodeSerializeTest {
	@SuppressWarnings({ "nls", "null" })
	public static void main(final String[] args) {
		//final String string = "[%" + StringUtils.repeat("if (true) 42;else 0;", 500000) + "%]";
		final String string = StringUtils.repeat("abcdefghijklmnopqrst", 500000);
		final IFormExpression ex;
		try {
			FormExpressionFactory.Template.parse(string);
			final long t1 = System.nanoTime();
			ex = FormExpressionFactory.Template.parse(string);
			final long t2 = System.nanoTime();
			System.out.println("Parsing took " + (t2-t1)/1000000 + "ms");
		} catch (final ParseException e) {
			e.printStackTrace();
			return;
		}

		final byte[] bytes = SerializationUtils.serialize(ex);
		SerializationUtils.<IFormExpression>deserialize(bytes);
		final long t1 = System.nanoTime();
		final IFormExpression deser = SerializationUtils.<IFormExpression>deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println(deser.getClass());
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms");
	}
}
