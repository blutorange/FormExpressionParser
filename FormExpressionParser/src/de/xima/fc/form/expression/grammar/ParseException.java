package de.xima.fc.form.expression.grammar;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.IPositionedError;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

/**
 * This exception is thrown when parse errors are encountered. You can
 * explicitly create objects of this exception type by calling the method
 * generateParseException in the generated parser.
 *
 * You can modify this class to customize your error reporting mechanisms so
 * long as you retain the public fields.
 */
@ParametersAreNonnullByDefault
public class ParseException extends Exception implements IPositionedError {

	/**
	 * The version identifier for this Serializable class. Increment only if the
	 * <i>serialized</i> form of the class changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This constructor is used by the method "generateParseException" in the
	 * generated parser. Calling this constructor generates a new object of this
	 * type with the fields "currentToken", "expectedTokenSequences", and
	 * "tokenImage" set.
	 */
	public ParseException(final Token currentTokenVal, final int[][] expectedTokenSequencesVal,
			final String[] tokenImageVal) {
		super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
		currentToken = currentTokenVal;
		expectedTokenSequences = expectedTokenSequencesVal;
		tokenImage = tokenImageVal;
	}

	public ParseException(final String msg, final Token currentTokenVal, @Nullable final int[][] expectedTokenSequencesVal,
			final String[] tokenImageVal) {
		super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal) + ": " + msg); //$NON-NLS-1$
		currentToken = currentTokenVal;
		expectedTokenSequences = expectedTokenSequencesVal;
		tokenImage = tokenImageVal;
	}

	/**
	 * The following constructors are for use by you for whatever purpose you
	 * can think of. Constructing the exception in this manner makes the
	 * exception behave in the normal way - i.e., as documented in the class
	 * "Throwable". The fields "errorToken", "expectedTokenSequences", and
	 * "tokenImage" do not contain relevant information. The JavaCC generated
	 * code does not use these constructors.
	 */

	public ParseException() {
		super();
		this.currentToken = null;
		this.expectedTokenSequences = null;
		this.tokenImage = null;
	}

	/** Constructor with message. */
	public ParseException(final String message) {
		this(message, null);
	}

	/** Constructor with message and token. */
	public ParseException(final String message, @Nullable final Token currentToken) {
		super(NullUtil.messageFormat(CmnCnst.Error.SEMANTIC_PARSE_EXCEPTION,
				Integer.valueOf(currentToken != null ? currentToken.beginLine : 0),
				Integer.valueOf(currentToken != null ? currentToken.beginColumn : 0),				message));
		this.currentToken = currentToken;
		this.expectedTokenSequences = null;
		this.tokenImage = null;
	}

	public ParseException(final String message, final int beginLine, final int beginColumn, final int endLine, final int endColumn) {
		super(NullUtil.messageFormat(CmnCnst.Error.SEMANTIC_PARSE_EXCEPTION, Integer.valueOf(beginLine),
				Integer.valueOf(beginColumn), message));
		this.currentToken = Token.newToken(FormExpressionParserConstants.EOF, "", beginLine, beginColumn, endLine, //$NON-NLS-1$
				endColumn);
		this.expectedTokenSequences = null;
		this.tokenImage = null;
	}

	/**
	 * This is the last token that has been consumed successfully. If this
	 * object has been created due to a parse error, the token followng this
	 * token will (therefore) be the first error token.
	 */
	@Nullable
	public final Token currentToken;

	/**
	 * Each entry in this array is an array of integers. Each array of integers
	 * represents a sequence of tokens (by their ordinal values) that is
	 * expected at this point of the parse.
	 */
	@Nullable
	public final int[][] expectedTokenSequences;

	/**
	 * This is a reference to the "tokenImage" array of the generated parser
	 * within which the parse error occurred. This array is defined in the
	 * generated ...Constants interface.
	 */
	@Nullable
	public final String[] tokenImage;

	/**
	 * It uses "currentToken" and "expectedTokenSequences" to generate a parse
	 * error message and returns it. If this object has been created due to a
	 * parse error, and you do not catch it (it gets thrown from the parser) the
	 * correct error message gets displayed.
	 */
	private static String initialise(final Token currentToken, @Nullable final int[][] expectedTokenSequences,
			final String[] tokenImage) {
		final String eol = System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		final StringBuffer expected = new StringBuffer();
		int maxSize = 0;

		if (expectedTokenSequences != null) {
			final Set<String> expectedSet = new TreeSet<>();
			for (int i = 0; i < expectedTokenSequences.length; i++) {
				if (maxSize < expectedTokenSequences[i].length) {
					maxSize = expectedTokenSequences[i].length;
				}
				for (int j = 0; j < expectedTokenSequences[i].length; j++) {
					expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
				}
				if (expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0) {
					expected.append("..."); //$NON-NLS-1$
				}
				expectedSet.add(expected.toString());
				expected.setLength(0);
			}
			for (final String s : expectedSet)
				expected.append(s).append(eol).append("    "); //$NON-NLS-1$
		}

		String retval = "Encountered "; //$NON-NLS-1$
		Token tok = currentToken.next;
		maxSize = maxSize == 0 && tok != null ? 1 : maxSize;
		for (int i = 0; i < maxSize && tok != null; i++) {
			if (i != 0)
				retval += ", "; //$NON-NLS-1$
			if (tok.kind == 0) {
				retval += tokenImage[0];
				break;
			}
			retval += tokenImage[tok.kind];
			final String img = "\"" + add_escapes(tok.image) + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			if (tokenImage[tok.kind] == null || !tokenImage[tok.kind].equals(img)) {
				retval += " "; //$NON-NLS-1$
				retval += img;
			}
			tok = tok.next;
		}
		final Token next = currentToken.next;
		if (next != null)
			retval += " at line " + next.beginLine + ", column " + next.beginColumn; //$NON-NLS-1$ //$NON-NLS-2$
		retval += "." + eol; //$NON-NLS-1$
		if (expectedTokenSequences != null) {
			if (expectedTokenSequences.length == 1) {
				retval += "Was expecting:" + eol + "    "; //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				retval += "Was expecting one of:" + eol + "    "; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		retval += expected.toString();
		return retval;
	}

	/**
	 * The end of line string for this machine.
	 */
	protected final String eol = NullUtil.or(System.getProperty("line.separator", "\n"), "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/**
	 * Used to convert raw characters to their escaped version when these raw
	 * version cannot be used as part of an ASCII string literal.
	 */
	static String add_escapes(@Nullable String str) {
		str = NullUtil.or(str, ""); //$NON-NLS-1$
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
		return NullUtil.checkNotNull(retval.toString());
	}

	@Override
	public boolean isPositionInformationAvailable() {
		return currentToken != null;
	}

	@Override
	public int getBeginLine() {
		final Token currentToken = this.currentToken;
		if (currentToken == null)
			return 0;
		final Token nextToken = currentToken.next;
		return nextToken != null ? nextToken.beginLine : currentToken.beginLine;
	}

	@Override
	public int getEndLine() {
		final Token currentToken = this.currentToken;
		if (currentToken == null)
			return 0;
		final Token nextToken = currentToken.next;
		return nextToken != null ? nextToken.endLine : currentToken.endLine;
	}

	@Override
	public int getBeginColumn() {
		final Token currentToken = this.currentToken;
		if (currentToken == null)
			return 0;
		final Token nextToken = currentToken.next;
		return nextToken != null ? nextToken.beginColumn : currentToken.beginColumn;
	}

	@Override
	public int getEndColumn() {
		final Token currentToken = this.currentToken;
		if (currentToken == null)
			return 0;
		final Token nextToken = currentToken.next;
		return nextToken != null ? nextToken.endColumn : currentToken.endColumn;
	}
}