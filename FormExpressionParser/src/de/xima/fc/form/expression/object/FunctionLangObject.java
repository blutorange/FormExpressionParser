package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IUnparsableFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectType;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class FunctionLangObject extends ALangObject {
	private final IFunction<ALangObject> value;

	private static class InstanceHolder {
		public final static FunctionLangObject NO_OP = new FunctionLangObject(new IFunction<ALangObject>() {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext,
					final ALangObject... args) throws EvaluationException {
				return NullLangObject.getInstance();
			}

			@Override
			public String getDeclaredArgument(final int i) {
				throw new ArrayIndexOutOfBoundsException();
			}

			@Override
			public int getDeclaredArgumentCount() {
				return 0;
			}

			@Override
			public String getDeclaredName() {
				return CmnCnst.NonnullConstant.STRING_EMPTY;
			}

			@Override
			public ILangObjectClass getThisContextType() {
				return ELangObjectType.NULL;
			}

			@Override
			public boolean hasVarArgs() {
				return false;
			}
		});
	}

	private FunctionLangObject(final IFunction<ALangObject> value) {
		super();
		this.value = value;
	}

	@Override
	public ILangObjectClass getType() {
		return ELangObjectType.FUNCTION;
	}

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
		for (int i = 0; i < value.getDeclaredArgumentCount(); ++i)
			builder.append(value.getDeclaredArgument(i)).append(Syntax.COMMA);
		// Remove final comma
		if (builder.length() > 3)
			builder.setLength(builder.length() - 1);
		builder.append(Syntax.PAREN_CLOSE).append(Syntax.BRACE_OPEN);
		// Convert body.
		if (value instanceof IUnparsableFunction)
			((IUnparsableFunction<?>)value).unparseBody(builder);
		else
			builder.append(Syntax.NATIVE_CODE);
		builder.append(Syntax.BRACE_CLOSE).append(Syntax.SEMI_COLON);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_FUNCTION_LANG_OBJECT).append('(')
				.append(value.getDeclaredName()).append(')'));
	}

	@Nullable
	@Override
	public IFunction<FunctionLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().expressionMethod(method, this);
	}

	@Nullable
	@Override
	public IFunction<FunctionLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessor(object, accessedViaDot, this);
	}

	@Nullable
	@Override
	public IFunction<FunctionLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssigner(name, accessedViaDot, this);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec,
			final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethod(method, this), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot,
			final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessor(object, accessedViaDot, this), object,
				accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value,
			final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssigner(object, accessedViaDot, this), object,
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
	public boolean equals(@Nullable final Object o) {
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

	public static FunctionLangObject getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before calling the function.
	public static FunctionLangObject create(final IFunction<? extends ALangObject> value) {
		return new FunctionLangObject((IFunction<ALangObject>) value);
	}
}
