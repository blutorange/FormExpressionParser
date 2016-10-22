package de.xima.fc.form.expression.impl.contextcommand;

import de.xima.fc.form.expression.context.IExternalContextCommand;
import de.xima.fc.form.expression.grammar.html.Token;

public enum EDocumentCommandPack implements IExternalContextCommand {
	REMOVE_ENCLOSING_PARAGRAPH,
	REMOVE_ENCLOSING_TABLE,
	;

	@Override
	public <T extends IExternalContextCommand> T castOrNull(Class<T> clazz) {
		return this.getClass().isAssignableFrom(clazz) ? clazz.cast(this) : null;
	}
	
	@Override
	public String getData() {
		return null;
	}
	
	public static class PositionedDocumentCommand implements Comparable<PositionedDocumentCommand> {
		public final int position;
		public final EDocumentCommandPack command;
		public Token token;
		public PositionedDocumentCommand(EDocumentCommandPack command, int position) {
			this.command = command;
			this.position = position;
		}

		@Override
		public int hashCode() {
			return 31 * command.hashCode() + position;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof PositionedDocumentCommand))
				return false;
			final PositionedDocumentCommand pdc = (PositionedDocumentCommand)o;
			return position == pdc.position && command == pdc.command;
		}
		
		@Override
		public int compareTo(PositionedDocumentCommand pdc) {
			return position == pdc.position ? command.compareTo(pdc.command) : position - pdc.position;
		}
		
		@Override
		public String toString() {
			return String.format("%s@%s(%s)", command, new Integer(position), token);
		}
	}
}
