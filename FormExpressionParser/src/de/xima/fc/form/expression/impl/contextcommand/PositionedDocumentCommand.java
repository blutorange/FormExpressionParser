package de.xima.fc.form.expression.impl.contextcommand;

import de.xima.fc.form.expression.grammar.html.Token;

public class PositionedDocumentCommand implements Comparable<PositionedDocumentCommand> {
	public final int position;
	public final DocumentCommand command;
	/**
	 * The order in which commands were issued. The lower, the earlier.
	 * Commands issued earlier will be processed earlier.
	 */
	public final int priority;
	public Token token;
	public PositionedDocumentCommand(final DocumentCommand command, final int position, final int priority) {
		this.command = command;
		this.position = position;
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		return 31 * priority + position;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof PositionedDocumentCommand))
			return false;
		final PositionedDocumentCommand pdc = (PositionedDocumentCommand)o;
		return position == pdc.position && priority == pdc.priority;
	}

	@Override
	public int compareTo(final PositionedDocumentCommand pdc) {
		return position == pdc.position ? priority - pdc.priority : position - pdc.position;
	}

	@Override
	public String toString() {
		return String.format("%s@%s(%s)", command, new Integer(position), token);
	}
}