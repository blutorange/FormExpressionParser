package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.enums.EVariableTypeFlag;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class ASTVariableTypeNode extends ANode {

	private static final long serialVersionUID = 1L;

	private ILangObjectClass variableType = ELangObjectClass.OBJECT;
	private EVariableTypeFlag[] flags = CmnCnst.NonnullConstant.EMPTY_VARIABLE_TYPE_FLAG_ARRAY;

	public ASTVariableTypeNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public ASTVariableTypeNode(final Node prototype, final ILangObjectClass variableType) {
		super(prototype, FormExpressionParserTreeConstants.JJTVARIABLETYPENODE);
		this.variableType = variableType;
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

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return new ASTVariableTypeNode(jjtGetChild(i), ELangObjectClass.OBJECT);
	}

	@Override
	public void additionalToStringFields(final StringBuilder sb) {
		sb.append(variableType).append(',');
		for (final EVariableTypeFlag flag : flags)
			sb.append(flag).append(',');
	}

	public void init(final EMethod method, final ILangObjectClass variableType, @Nullable final EVariableTypeFlag... flags) throws ParseException {
		assertNonNull(variableType, CmnCnst.Error.NULL_VARIABLE_TYPE);
		super.init(method);
		this.variableType  = variableType;
		if (flags != null) {
			int count = 0;
			for (final Object o : flags)
				if (o != null)
					++count;
			final EVariableTypeFlag[] f = new EVariableTypeFlag[count];
			int i = 0;
			for (final EVariableTypeFlag flag : flags)
				if (flag != null && i < f.length)
					f[i++] = flag;
			this.flags = f;
		}
	}

	public ILangObjectClass getLangObjectClass() {
		return variableType;
	}

	public boolean hasGenerics() {
		return jjtGetNumChildren()>0;
	}

	public int getGenericsCount() {
		return jjtGetNumChildren();
	}

	public Node getGenericsNode(final int i) {
		return jjtGetChild(i);
	}

	public ImmutableCollection<EVariableTypeFlag> getFlags() {
		if (flags.length == 0)
			return ImmutableSet.of();
		@SuppressWarnings("null") // We already check for nullness when adding flags.
		@Nonnull
		final EVariableTypeFlag first = flags[0];
		return Sets.immutableEnumSet(first, flags);
	}

	public boolean hasFlag(final EVariableTypeFlag flag) {
		for (int i = flags.length; i --> 0;)
			if (flag == flags[i])
				return true;
		return false;
	}
}