package de.xima.fc.form.expression.impl.contextcommand;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.impl.AExternalContextCommand;

public class DocumentCommand extends AExternalContextCommand {
	@Nonnull
	private final EDocumentCommandType type;
	@Nonnull
	private final String[] data;

	/**
	 * @param type Command type.
	 * @param data Data for the command.
	 * @throws IllegalArgumentException When type is <code>null</code>, or the number of String arguments does not match
	 * {@link EDocumentCommandType#argc}.
	 */
	private DocumentCommand(final EDocumentCommandType type, String... data) throws IllegalArgumentException {
		data = data != null ? data: ArrayUtils.EMPTY_STRING_ARRAY;
		assertArguments(type, data);
		this.type = type;
		this.data = data;
	}

	private final void assertArguments(final EDocumentCommandType type, final String[] data) {
		if (type == null) throw new IllegalArgumentException("Type must not be null.");
		if (data.length != type.argc)
			throw new IllegalArgumentException(
					String.format("Document command of type %s requires %d arguments, but %d were given: %s.", type,
							new Integer(type.argc), new Integer(data.length), StringUtils.join(data, ',')));
	}

	@Override
	public String[] getData() {
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

	public static DocumentCommand getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	public static DocumentCommand newInsertLink(final String href, final String text, final String target) {
		return new DocumentCommand(EDocumentCommandType.INSERT_LINK, href, text, target);
	}

	public static DocumentCommand newRemoveEnclosingTag(final String tagName) {
		return tagName == null ? getNoOpInstance() : new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, tagName);
	}

	public static DocumentCommand newRemovePreviousTag(final String tagName) {
		return tagName == null ? getNoOpInstance() : new DocumentCommand(EDocumentCommandType.REMOVE_PREVIOUS_TAG, tagName);
	}

	public static DocumentCommand newRemoveNextTag(final String tagName) {
		return tagName == null ? getNoOpInstance() : new DocumentCommand(EDocumentCommandType.REMOVE_NEXT_TAG, tagName);
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", type, StringUtils.join(data, ','));
	}

	public static enum EDocumentCommandType {
		/**
		 * @param tagName Name of the tag to remove. May be null.
		 */
		REMOVE_ENCLOSING_TAG(1),
		/**
		 * @param tagName Name of the tag to remove. May be null.
		 */
		REMOVE_PREVIOUS_TAG(1),
		/**
		 * @param tagName Name of the tag to remove. May be null.
		 */
		REMOVE_NEXT_TAG(1),
		/**
		 * @param href Hyperlink. Can be null.
		 * @param text Link text. Can be null.
		 * @param target Link target. Can be null.
		 */
		INSERT_LINK(3),
		NO_OP(0)
		;
		public final int argc;
		private EDocumentCommandType(final int argc) {
			this.argc = argc;
		}
	}

	private final static class InstanceHolder {
		public final static DocumentCommand REMOVE_ENCLOSING_PARAGRAPH = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "p");
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "table");
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE_ROW = new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, "tr");
		public final static DocumentCommand NO_OP = new DocumentCommand(EDocumentCommandType.NO_OP);
	}
}
