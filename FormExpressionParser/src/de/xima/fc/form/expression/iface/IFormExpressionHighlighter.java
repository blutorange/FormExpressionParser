package de.xima.fc.form.expression.iface;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Token;

@NonNullByDefault
public interface IFormExpressionHighlighter {
	public void process(final Iterator<Token> tokenStream, IFormExpressionHighlightTheme theme) throws IOException;
}