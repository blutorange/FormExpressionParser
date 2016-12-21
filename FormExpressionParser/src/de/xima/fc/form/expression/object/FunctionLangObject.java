package de.xima.fc.form.expression.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalThisContextException;
import de.xima.fc.form.expression.exception.evaluation.UnboundFunctionCallException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IUnparsableFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public class FunctionLangObject extends ALangObject {
	private final IFunction<ALangObject> value;
	@Nullable
	private ALangObject thisContext;

	private FunctionLangObject(final IFunction<ALangObject> value) {
		super();
		this.value = value;
	}

	private FunctionLangObject(final IFunction<ALangObject> value, @Nullable final ALangObject thisContext) {
		this(value);
		this.thisContext = thisContext;
	}

	@Override
	public ILangObjectClass getObjectClass() {
		return ELangObjectClass.FUNCTION;
	}

	public ALangObject evaluate(final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		if (thisContext != null)
			return value.evaluate(ec, thisContext, args);
		throw new UnboundFunctionCallException(value, ec);
	}

	@Override
	public ALangObject shallowClone() {
		return new FunctionLangObject(value, thisContext);
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
		if (hasVarArgs())
			builder.append(CmnCnst.Syntax.TRIPLE_DOT);
		builder.append(Syntax.PAREN_CLOSE).append(Syntax.BRACE_OPEN);
		// Convert body.
		if (value instanceof IUnparsableFunction)
			((IUnparsableFunction<?>)value).unparseBody(builder);
		else
			builder.append(Syntax.NATIVE_CODE).append(CmnCnst.Syntax.SEMI_COLON);
		builder.append(Syntax.BRACE_CLOSE).append(Syntax.SEMI_COLON);
	}

	@Override
	public String inspect() {
		return NullUtil.toString(new StringBuilder().append(CmnCnst.ToString.INSPECT_FUNCTION_LANG_OBJECT).append('(')
				.append(value.getDeclaredName()).append(')'));
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

	public static FunctionLangObject getNoOpNull() {
		return new FunctionLangObject(new IFunction<ALangObject>() {
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
				return ELangObjectClass.NULL;
			}

			@Override
			public boolean hasVarArgs() {
				return false;
			}
		}, NullLangObject.getInstance());
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before calling the function.
	public static FunctionLangObject create(final IFunction<? extends ALangObject> value) {
		return new FunctionLangObject((IFunction<ALangObject>) value);
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before calling the function.
	public static FunctionLangObject createNull(final IFunction<NullLangObject> value) {
		final IFunction<?> f = value;
		return new FunctionLangObject((IFunction<ALangObject>) f, NullLangObject.getInstance());
	}

	public boolean hasVarArgs() {
		return value.hasVarArgs();
	}

	public int getDeclaredArgumentCount() {
		return value.getDeclaredArgumentCount();
	}

	public FunctionLangObject bind(final ALangObject thisContext, final IEvaluationContext ec) throws UncatchableEvaluationException {
		if (!value.getThisContextType().isSuperClassOf(thisContext.getObjectClass()))
			throw new IllegalThisContextException(thisContext, value.getThisContextType(), value, ec);
		this.thisContext = thisContext;
		return this;
	}

	public String getDeclaredName() {
		return value.getDeclaredName();
	}
}