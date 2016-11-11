package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTIdentifierNameNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	@Nonnull private String name = CmnCnst.EMPTY_STRING;

	public ASTIdentifierNameNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(@Nullable final EMethod method, @Nonnull final String name) throws ParseException {
		super.init(method);
		this.name = Preconditions.checkNotNull(name);
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(name).append(',');
	}
}
