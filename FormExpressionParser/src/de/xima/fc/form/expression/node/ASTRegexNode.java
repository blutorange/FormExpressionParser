package de.xima.fc.form.expression.node;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class ASTRegexNode extends ANode {
	private static final long serialVersionUID = 1L;

	private Pattern pattern = CmnCnst.NonnullConstant.PATTERN_UNMATCHABLE;

	public ASTRegexNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASTRegexNode(final Node prototype) {
		super(prototype, FormExpressionParserTreeConstants.JJTREGEXNODE);
	}

	public void init(@Nullable final EMethod method, @Nullable final String regex) throws ParseException {
		assertChildrenExactly(0);
		Preconditions.checkNotNull(regex, new ParseException(CmnCnst.Error.NODE_NULL_REGEX));
		if (regex.length() < 2)
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_IMPROPER_REGEX_TERMINATION, regex));
		super.init(method);
		final int lastHash = regex.lastIndexOf('#');
		try {
			pattern = NullUtil.checkNotNull(Pattern.compile(regex.substring(1, lastHash), flags(regex, lastHash+1)));
		}
		catch (final PatternSyntaxException e) {
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INVALID_REGEX, e.getMessage()),
					getBeginLine(), getBeginColumn(), getEndLine(), getEndColumn());
		}
	}

	@Nullable
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(pattern.pattern()).append(',').append(pattern.flags()).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Nonnull
	public Pattern getPattern() {
		return pattern;
	}

	private static int flags(@Nonnull final String flagString, final int beginIndex) {
		final int len = flagString.length();
		int flags = 0;
		for (int i = beginIndex; i < len; ++i) {
			switch (flagString.charAt(i)) {
			case 's':
				flags |= Pattern.DOTALL;
				break;
			case 'm':
				flags |= Pattern.MULTILINE;
				break;
			case 'i':
				flags |= Pattern.CASE_INSENSITIVE;
				break;
			}
		}
		return flags;
	}
}
