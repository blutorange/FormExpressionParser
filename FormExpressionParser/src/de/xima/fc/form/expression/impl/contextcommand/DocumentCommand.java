package de.xima.fc.form.expression.impl.contextcommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public final class DocumentCommand extends AExternalContextCommand {
	private final EDocumentCommandType type;
	private final String[] data;

	/**
	 * @param type
	 *            Command type.
	 * @param data
	 *            Data for the command.
	 * @throws FormExpressionException
	 *             When type is <code>null</code>, or the number of String
	 *             arguments does not match {@link EDocumentCommandType#argc}.
	 */
	protected DocumentCommand(final EDocumentCommandType type, @Nullable final String... data)
			throws FormExpressionException {
		final String[] dataArray = data != null ? data : CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
		assertArguments(type, dataArray);
		this.type = type;
		this.data = dataArray;
	}

	private final static void assertArguments(@Nullable final EDocumentCommandType type, final String[] data) {
		if (type == null)
			throw new FormExpressionException(CmnCnst.Error.NULL_TYPE);
		if (data.length != type.argc)
			throw new FormExpressionException(NullUtil.messageFormat(CmnCnst.Error.ILLEGAL_ARGUMENTS_FOR_DOCUMENT_COMMAND, type,
					Integer.valueOf(type.argc), Integer.valueOf(data.length), StringUtils.join(data, ',')));
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

	public static DocumentCommand newInsertLink(final String href, @Nullable final String text, @Nullable final String target) {
		return new DocumentCommand(EDocumentCommandType.INSERT_LINK, href, text, target);
	}

	public static DocumentCommand newRemoveEnclosingTag(@Nullable final String tagName) {
		return tagName == null ? getNoOpInstance()
				: new DocumentCommand(EDocumentCommandType.REMOVE_ENCLOSING_TAG, tagName);
	}

	public static DocumentCommand newRemovePreviousTag(@Nullable final String tagName) {
		return tagName == null ? getNoOpInstance()
				: new DocumentCommand(EDocumentCommandType.REMOVE_PREVIOUS_TAG, tagName);
	}

	public static DocumentCommand newRemoveNextTag(@Nullable final String tagName) {
		return tagName == null ? getNoOpInstance() : new DocumentCommand(EDocumentCommandType.REMOVE_NEXT_TAG, tagName);
	}

	@Override
	public String toString() {
		return NullUtil.stringFormat(CmnCnst.ToString.DOCUMENT_COMMAND, type, StringUtils.join(data, ','));
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
		public final static DocumentCommand REMOVE_ENCLOSING_PARAGRAPH = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "p"); //$NON-NLS-1$
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "table"); //$NON-NLS-1$
		public final static DocumentCommand REMOVE_ENCLOSING_TABLE_ROW = new DocumentCommand(
				EDocumentCommandType.REMOVE_ENCLOSING_TAG, "tr"); //$NON-NLS-1$
		public final static DocumentCommand NO_OP = new DocumentCommand(EDocumentCommandType.NO_OP);
	}
}
