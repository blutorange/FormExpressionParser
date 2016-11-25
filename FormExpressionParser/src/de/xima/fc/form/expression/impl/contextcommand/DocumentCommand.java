package de.xima.fc.form.expression.impl.contextcommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.impl.AExternalContextCommand;
import de.xima.fc.form.expression.util.CmnCnst;

public class DocumentCommand extends AExternalContextCommand {
	@Nonnull
	private final EDocumentCommandType type;
	@Nonnull
	private final String[] data;

	/**
	 * @param type
	 *            Command type.
	 * @param data
	 *            Data for the command.
	 * @throws IllegalArgumentException
	 *             When type is <code>null</code>, or the number of String
	 *             arguments does not match {@link EDocumentCommandType#argc}.
	 */
	private DocumentCommand(@Nonnull final EDocumentCommandType type, @Nullable final String... data)
			throws IllegalArgumentException {
		final String[] dataArray = data != null ? data : CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		assertArguments(type, dataArray);
		this.type = type;
		this.data = dataArray;
	}

	private final static void assertArguments(@Nullable final EDocumentCommandType type, @Nonnull final String[] data) {
		if (type == null)
			throw new IllegalArgumentException(CmnCnst.Error.NULL_TYPE);
		if (data.length != type.argc)
			throw new IllegalArgumentException(String.format(CmnCnst.Error.ILLEGAL_ARGUMENTS_FOR_DOCUMENT_COMMAND, type,
					new Integer(type.argc), new Integer(data.length), StringUtils.join(data, ',')));
	}

	@Nonnull
	@Override
	public String[] getData() {
		return data;
	}

	@Nonnull
	@Override
	public EDocumentCommandType getType() {
		return type;
	}

	@Nonnull
	public static DocumentCommand getRemoveEnclosingParagraphInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_PARAGRAPH;
	}

	@Nonnull
	public static DocumentCommand getRemoveEnclosingTableInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_TABLE;
	}

	@Nonnull
	public static DocumentCommand getRemoveEnclosingTableRowInstance() {
		return InstanceHolder.REMOVE_ENCLOSING_TABLE_ROW;
	}

	@Nonnull
	public static DocumentCommand getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	@Nonnull
	public static DocumentCommand newInsertLink(final String href, final String text, final String target) {
		return new DocumentCommand(EDocumentCommandType.INSERT_LINK, href, text, target);
	}

	@Nonnull
	public static DocumentCommand newRemoveEnclosingTag(final String tagName) {
		return tagName == null ? getNoOpInstance()
				: new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, tagName);
	}

	@Nonnull
	public static DocumentCommand newRemovePreviousTag(final String tagName) {
		return tagName == null ? getNoOpInstance()
				: new DocumentCommand(EDocumentCommandType.REMOVE_PREVIOUS_TAG, tagName);
	}

	@Nonnull
	public static DocumentCommand newRemoveNextTag(final String tagName) {
		return tagName == null ? getNoOpInstance() : new DocumentCommand(EDocumentCommandType.REMOVE_NEXT_TAG, tagName);
	}

	@Override
	public String toString() {
		return String.format(CmnCnst.ToString.DOCUMENT_COMMAND, type, StringUtils.join(data, ','));
	}

	public static enum EDocumentCommandType {
		/**
		 * @param tagName
		 *            Name of the tag to remove. May be null.
		 */
		REMOVE_ENCLOSING_TAG(1),
		/**
		 * @param tagName
		 *            Name of the tag to remove. May be null.
		 */
		REMOVE_PREVIOUS_TAG(1),
		/**
		 * @param tagName
		 *            Name of the tag to remove. May be null.
		 */
		REMOVE_NEXT_TAG(1),
		/**
		 * @param href
		 *            Hyperlink. Can be null.
		 * @param text
		 *            Link text. Can be null.
		 * @param target
		 *            Link target. Can be null.
		 */
		INSERT_LINK(3),
		NO_OP(0);
		public final int argc;

		private EDocumentCommandType(final int argc) {
			this.argc = argc;
		}
	}

	private final static class InstanceHolder {
		@Nonnull
		public final static DocumentCommand REMOVE_ENCLOSING_PARAGRAPH = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "p"); //$NON-NLS-1$
		@Nonnull
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "table"); //$NON-NLS-1$
		@Nonnull
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE_ROW = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "tr"); //$NON-NLS-1$
		@Nonnull
		public final static DocumentCommand NO_OP = new DocumentCommand(EDocumentCommandType.NO_OP);
	}
}
