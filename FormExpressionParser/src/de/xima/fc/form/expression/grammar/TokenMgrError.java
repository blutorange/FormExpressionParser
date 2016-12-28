package de.xima.fc.form.expression.grammar;

import de.xima.fc.form.expression.iface.IPositionedError;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/** Token Manager Error. */
public class TokenMgrError extends Error implements IPositionedError {
	/**
	 * The version identifier for this Serializable class. Increment only if the
	 * <i>serialized</i> form of the class changes.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Ordinals for various reasons why an Error of this type can be thrown.
	 */

	/**
	 * Lexical error occurred.
	 */
	static final int LEXICAL_ERROR = 0;

	/**
	 * An attempt was made to create a second instance of a static token
	 * manager.
	 */
	static final int STATIC_LEXER_ERROR = 1;

	/**
	 * Tried to change to an invalid lexical state.
	 */
	static final int INVALID_LEXICAL_STATE = 2;

	/**
	 * Detected (and bailed out of) an infinite loop in the token manager.
	 */
	static final int LOOP_DETECTED = 3;

	/**
	 * Indicates the reason why the exception is thrown. It will have one of the
	 * above 4 values.
	 */
	int errorCode;

	private int beginLine, beginColumn, endLine, endColumn;
	private boolean hasPosition;

	/**
	 * Replaces unprintable characters by their escaped (or unicode escaped)
	 * equivalents in the given string
	 */
	protected static final String addEscapes(final String str) {
		final StringBuffer retval = new StringBuffer();
		char ch;
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {
			case 0:
				continue;
			case '\b':
				retval.append("\\b"); //$NON-NLS-1$
				continue;
			case '\t':
				retval.append("\\t"); //$NON-NLS-1$
				continue;
			case '\n':
				retval.append("\\n"); //$NON-NLS-1$
				continue;
			case '\f':
				retval.append("\\f"); //$NON-NLS-1$
				continue;
			case '\r':
				retval.append("\\r"); //$NON-NLS-1$
				continue;
			case '\"':
				retval.append("\\\""); //$NON-NLS-1$
				continue;
			case '\'':
				retval.append("\\\'"); //$NON-NLS-1$
				continue;
			case '\\':
				retval.append("\\\\"); //$NON-NLS-1$
				continue;
			default:
				if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
					final String s = "0000" + Integer.toString(ch, 16); //$NON-NLS-1$
					retval.append("\\u" + s.substring(s.length() - 4, s.length())); //$NON-NLS-1$
				}
				else {
					retval.append(ch);
				}
				continue;
			}
		}
		return retval.toString();
	}

	/**
	 * Returns a detailed message for the Error when it is thrown by the token
	 * manager to indicate a lexical error.
	 * You can customize the lexical error message by modifying
	 * this method.
	 * @param EOFSeen Indicates if EOF caused the lexical error curLexState
	 * @param lexState In which state this error occurred.
	 * @param errorLine The line number when the error occurred.
	 * @param errorColumn The column number when the error occurred.
	 * @param errorAfter Prefix that was seen before this error occurred.
	 * @param curchar The offending character.
	 * @return The customized error message.
	 */
	protected static String LexicalError(final boolean EOFSeen, final int lexState, final int errorLine,
			final int errorColumn, final String errorAfter, final char curChar) {
		final String encountered = EOFSeen ? "<EOF>" : ("\"" + String.valueOf(curChar) + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return NullUtil.messageFormat(CmnCnst.Error.TOKEN_MGR_ERROR, Integer.valueOf(errorLine),
				Integer.valueOf(errorColumn), encountered, errorAfter);
	}

	/**
	 * You can also modify the body of this method to customize your error
	 * messages. For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE
	 * are not of end-users concern, so you can return something like :
	 *
	 * "Internal Error : Please file a bug report .... "
	 *
	 * from this method for such cases in the release version of your parser.
	 */
	@Override
	public String getMessage() {
		return super.getMessage();
	}

	/*
	 * Constructors of various flavors follow.
	 */

	/** No arg constructor. */
	public TokenMgrError() {
	}

	/** Constructor with message and reason. */
	public TokenMgrError(final String message, final int reason) {
		super(message);
		errorCode = reason;
	}

	/** Full Constructor. */
	public TokenMgrError(final boolean EOFSeen, final int lexState, final int errorLine, final int errorColumn,
			final String errorAfter, final char curChar, final int reason) {
		this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
		this.beginLine = this.endLine = errorLine;
		this.beginColumn = this.endColumn = errorColumn;
		this.hasPosition = errorLine > 0 && errorColumn >= 0;
	}

	@Override
	public boolean isPositionInformationAvailable() {
		return hasPosition;
	}

	@Override
	public int getBeginLine() {
		return beginLine;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getBeginColumn() {
		return beginColumn;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}
}