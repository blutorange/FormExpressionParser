package de.xima.fc.form.expression.impl.contextcommand;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.grammar.html.Token;
import de.xima.fc.form.expression.impl.AExternalContextCommand;

public class DocumentCommand extends AExternalContextCommand {
	@Nonnull
	private final EDocumentCommandType type;
	@Nonnull
	private final String data;
		
	public DocumentCommand(EDocumentCommandType type) {
		this(type, null);
	}
	
	public DocumentCommand(EDocumentCommandType type, String data) {
		if (type == null) throw new IllegalArgumentException("type must not be null");
		this.type = type;
		this.data = data != null ? data: StringUtils.EMPTY;
	}
		
	@Override
	public String getData() {
		return data;
	}
	
	@Override
	public EDocumentCommandType getType() {
		return type;
	}
	
	public static DocumentCommand getRemoveEnclosingParagraphInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_PARAGRAPH;
	}

	public static DocumentCommand getRemoveEnclosingTableInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_TABLE;
	}
	
	public static DocumentCommand getRemoveEnclosingTableRowInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_TABLE_ROW;
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", type, data);
	}
	
	public static enum EDocumentCommandType {
		REMOVE_ENCLOSING_TAG,
		REMOVE_PREVIOUS_TAG,
		REMOVE_NEXT_TAG,
		INSERT_LINK,
		;
	}
	
	private final static class InstanceHolder {
		public final static DocumentCommand REMOVE_ENCLOSING_PARAGRAPH = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "p");
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "table");
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE_ROW = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "tr");
	}
	
	public static class PositionedDocumentCommand implements Comparable<PositionedDocumentCommand> {
		public final int position;
		public final DocumentCommand command;
		/**
		 * The order in which commands were issued. The lower, the earlier.
		 * Commands issued earlier will be processed earlier. 
		 */
		public final int priority;
		public Token token;
		public PositionedDocumentCommand(DocumentCommand command, int position, int priority) {
			this.command = command;
			this.position = position;
			this.priority = priority;
		}

		@Override
		public int hashCode() {
			return 31 * priority + position;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof PositionedDocumentCommand))
				return false;
			final PositionedDocumentCommand pdc = (PositionedDocumentCommand)o;
			return position == pdc.position && priority == pdc.priority;
		}
		
		@Override
		public int compareTo(PositionedDocumentCommand pdc) {
			return position == pdc.position ? priority - pdc.priority : position - pdc.position;
		}
		
		@Override
		public String toString() {
			return String.format("%s@%s(%s)", command, new Integer(position), token);
		}
	}
}
