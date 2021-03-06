package de.xima.fc.form.expression.grammar.comment;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.ECommentType;
import de.xima.fc.form.expression.iface.parse.IComment;

class CommentImpl implements IComment {
	private static final long serialVersionUID = 1L;
	@Nonnull private final String text;
	@Nonnull private final ECommentType type;
	private final int column;
	private final int line;

	CommentImpl(@Nonnull final ECommentType type, @Nonnull final String text, final int line, final int column) {
		this.text = text;
		this.type = type;
		this.line = line;
		this.column = column;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public ECommentType getCommentType() {
		return type;
	}
}
