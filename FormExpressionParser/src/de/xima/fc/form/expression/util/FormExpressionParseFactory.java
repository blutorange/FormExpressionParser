package de.xima.fc.form.expression.util;

import java.io.StringReader;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;

public final class FormExpressionParseFactory {
	private FormExpressionParseFactory(){}

	/**
	 * Methods for parsing code as one complete program.
	 * @author mad_gaksha
	 */
	public final static class Program {
		private Program(){}
		/**
		 * Parses the given string and returns the top level node of the parse tree.
		 * @return Top level node of the parse tree.
		 * @throws ParseException When the code is not a valid program.
		 */
		public static Node parseString(String code) throws ParseException {
			try (final StringReader reader = new StringReader(code)){
				return new FormExpressionParser(reader).Program();
			}
		}
	}
}
