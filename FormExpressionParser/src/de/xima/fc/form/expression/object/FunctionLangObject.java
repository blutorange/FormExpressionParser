package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IFunction;
import de.xima.fc.form.expression.impl.variable.DummyScopeDefinitions;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public class FunctionLangObject extends ALangObject {
	@Nonnull
	private final IFunction<ALangObject> value;

	private static class InstanceHolder {
		@Nonnull
		public final static FunctionLangObject NO_OP = new FunctionLangObject(new IFunction<ALangObject>() {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NullLangObject.getInstance();
			}

			@Override
			public String[] getDeclaredArgumentList() {
				return CmnCnst.NonnullConstant.EMPTY_STRING_ARRAY;
			}

			@Override
			public Node getNode() {
				return null;
			}

			@Override
			public String getDeclaredName() {
				return CmnCnst.NonnullConstant.STRING_EMPTY;
			}

			@Override
			public Type getThisContextType() {
				return ALangObject.Type.NULL;
			}

			@Override
			public String getVarArgsName() {
				return null;
			}
		});
	}

	private FunctionLangObject(@Nonnull final IFunction<ALangObject> value) {
		super(Type.FUNCTION);
		this.value = value;
	}

	@Nonnull
	public IFunction<ALangObject> functionValue() {
		return value;
	}

	@Override
	public ALangObject shallowClone() {
		return FunctionLangObject.create(value);
	}

	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		builder.append(Syntax.LAMBDA_ARROW).append(Syntax.PAREN_OPEN);
		// Add arguments.
		for (final String arg : value.getDeclaredArgumentList())
			builder.append(arg).append(Syntax.COMMA);
		// Remove final comma
		if (builder.length() > 3)
			builder.setLength(builder.length() - 1);
		builder.append(Syntax.PAREN_CLOSE).append(Syntax.BRACE_OPEN);
		// Convert body.
		final Node n = value.getNode();
		if (n == null)
			builder.append(Syntax.NATIVE_CODE);
		else {
			final String unparse = UnparseVisitor.unparse(n, DummyScopeDefinitions.INSTANCE,
					UnparseVisitorConfig.getUnstyledWithoutCommentsConfig());
			builder.append(unparse);
		}
		builder.append(Syntax.BRACE_CLOSE).append(Syntax.SEMI_COLON);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_FUNCTION_LANG_OBJECT).append('(')
				.append(value.getDeclaredName()).append(')'));
	}

	@Override
	public IFunction<FunctionLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().expressionMethodFunction(method);
	}

	@Override
	public IFunction<FunctionLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorFunction(object, accessedViaDot);
	}

	@Override
	public IFunction<FunctionLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerFunction(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodFunction(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorFunction(object, accessedViaDot), object,
				accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value,
			final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerFunction(object, accessedViaDot), object,
				accessedViaDot, value, ec);
	}

	@Override
	protected boolean isSingletonLike() {
		return false;
	}

	@Override
	public int hashCode() {
		// getDeclaredName can be empty for anonymous functions
		return 31 * super.hashCode() + value.getDeclaredName().hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof FunctionLangObject))
			return false;
		return super.equalsSameObject((FunctionLangObject) o);
	}

	@Override
	public int compareToSameType(final ALangObject o) {
		return super.compareById(o);
	}

	// Coercion
	@Nonnull
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return StringLangObject.create(value.getDeclaredName());
	}

	@Nonnull
	@Override
	public FunctionLangObject coerceFunction(final IEvaluationContext ec) {
		return this;
	}

	@Nonnull
	public static FunctionLangObject getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before
									// calling IFunction#evaluate
	@Nonnull
	public static FunctionLangObject create(final IFunction<? extends ALangObject> value) {
		if (value == null)
			return getNoOpInstance();
		return new FunctionLangObject((IFunction<ALangObject>) value);
	}
}
