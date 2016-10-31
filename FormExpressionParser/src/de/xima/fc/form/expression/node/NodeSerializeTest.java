package de.xima.fc.form.expression.node;

import org.apache.commons.lang3.SerializationUtils;

import de.xima.fc.form.expression.context.IFormExpression;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory;

public final class NodeSerializeTest {

	@SuppressWarnings("nls")
	public static void main(final String[] args) {
		final String string = "a1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtsera1342342342asdasdasdaewrwefreyaegtsergtser[%if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;if('') 8 ; else 42;%]";
		System.out.println("Input string\n" + string);
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
		final long t1 = System.nanoTime();
		SerializationUtils.<IFormExpression>deserialize(bytes);
		final IFormExpression deser = SerializationUtils.<IFormExpression>deserialize(bytes);
		final long t2 = System.nanoTime();
		System.out.println("Deserialization took " + (t2-t1)/1000000 + "ms");
		System.out.println(deser);
	}

}
