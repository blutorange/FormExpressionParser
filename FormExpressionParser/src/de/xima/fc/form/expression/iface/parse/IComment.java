package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import de.xima.fc.form.expression.enums.ECommentType;

public interface IComment extends Serializable {
	/** @return The text of this comment. */
	public String getText();

	/**
	 * The column where this comment starts, including any
	 * syntactical markup.
	 * <pre>//comment</pre>
	 * The above comment starts at column 1, not 3.
	 * The first column is column 1.
	 * @return Column number of this comment
	 */
	public int getColumn();

	/**
	 * The line where this comment starts, including any
	 * syntactical markup.
	 * <pre>/*
	 *comment&#x2a;/</pre>
	 * The above comment starts at line 1, not 2.
	 * The first line is line 1.
	 * @return Line number of this comment
	 */
	public int getLine();

	/** @return The type of comment, ie. single-line or multi-line. */
	public ECommentType getCommentType();
}
