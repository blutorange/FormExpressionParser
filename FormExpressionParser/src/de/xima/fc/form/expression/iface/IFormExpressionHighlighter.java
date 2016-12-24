package de.xima.fc.form.expression.iface;

import java.io.IOException;
import java.util.Iterator;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Token;

@ParametersAreNonnullByDefault
public interface IFormExpressionHighlighter {
	public void process(final Iterator<Token> tokenStream, IFormExpressionHighlightTheme theme) throws IOException;
}