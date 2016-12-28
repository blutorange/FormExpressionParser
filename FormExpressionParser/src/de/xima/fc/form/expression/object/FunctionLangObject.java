package de.xima.fc.form.expression.object;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.BreakClauseException;
import de.xima.fc.form.expression.exception.evaluation.ContinueClauseException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalNumberOfFunctionParametersException;
import de.xima.fc.form.expression.exception.evaluation.IllegalThisContextException;
import de.xima.fc.form.expression.exception.evaluation.UnboundFunctionCallException;
import de.xima.fc.form.expression.exception.evaluation.UncatchableEvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IClosure;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.evaluate.IUnparsableFunction;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.util.NullUtil;

@NonNullByDefault
public class FunctionLangObject extends ALangObject {
	private final IFunction<ALangObject> value;
	@Nullable private ALangObject thisContext;
	@Nullable private final IClosure parentClosure;
	private final int closureSymbolTableSize;

	private FunctionLangObject(final IFunction<ALangObject> value, @Nullable final IClosure parentClosure, final int closureSymbolTableSize) {
		super();
		this.value = value;
		this.parentClosure = parentClosure;
		this.closureSymbolTableSize = closureSymbolTableSize;
	}

	private FunctionLangObject(final IFunction<ALangObject> value, @Nullable final ALangObject thisContext,
			@Nullable final IClosure parentClosure, final int closureSymbolTableSize) {
		this(value, parentClosure, closureSymbolTableSize);
		this.thisContext = thisContext;
	}

	@Override
	public ILangObjectClass getObjectClass() {
		return ELangObjectClass.FUNCTION;
	}

	public final ALangObject evaluate(final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		// Check argument count.
		if (value.hasVarArgs() ? args.length < value.getDeclaredArgumentCount() - 1
				: args.length != value.getDeclaredArgumentCount())
			throw new IllegalNumberOfFunctionParametersException(this, args.length, ec);

		// Evaluate function
		final ALangObject thisContext = this.thisContext;
		final ALangObject result;
		if (thisContext == null)
			throw new UnboundFunctionCallException(value, ec);
		ec.closureStackPush(new ClosureImpl(closureSymbolTableSize, parentClosure));
		ec.getTracer().descend();
		try {
			result = value.evaluate(ec, thisContext, args);
		}
		finally {
			ec.getTracer().ascend();
			ec.closureStackPop();
		}

		// Check for disallowed break / continue clauses.
		switch (ec.getJumpType()) {
		case RETURN:
			ec.unsetJump();
			break;
		case NONE:
			break;
		case BREAK:
			throw new BreakClauseException(ec.getJumpLabel(), ec);
		case CONTINUE:
			throw new ContinueClauseException(ec.getJumpLabel(), ec);
		default:
			throw new UncatchableEvaluationException(ec,
				NullUtil.messageFormat(CmnCnst.Error.INVALID_JUMP_TYPE, ec.getJumpType()));
		}

		return result;
	}

	@Override
	public ALangObject shallowClone() {
		return new FunctionLangObject(value, thisContext, parentClosure, closureSymbolTableSize);
	}

	@Override
	public ALangObject deepClone() {
		return new FunctionLangObject(value, thisContext, parentClosure != null ? parentClosure.copy() : null, closureSymbolTableSize);
	}

	@Override
	public void toExpression(final StringBuilder builder) {
		// Add arguments.
		builder.append(Syntax.PAREN_OPEN);
		for (int i = 0; i < value.getDeclaredArgumentCount(); ++i)
			builder.append(value.getDeclaredArgument(i)).append(Syntax.COMMA);
		// Remove final comma
		if (value.getDeclaredArgumentCount() > 0)
			builder.setLength(builder.length() - 1);
		// Add triple dot for varargs.
		if (hasVarArgs())
			builder.append(CmnCnst.Syntax.TRIPLE_DOT);
		builder.append(Syntax.PAREN_CLOSE);
		builder.append(Syntax.LAMBDA_ARROW);
		// Convert body.
		builder.append(Syntax.BRACE_OPEN);
		if (value instanceof IUnparsableFunction)
			((IUnparsableFunction<?>)value).unparseBody(builder);
		else
			builder.append(Syntax.NATIVE_CODE).append(CmnCnst.Syntax.SEMI_COLON);
		builder.append(Syntax.BRACE_CLOSE);
	}

	@Override
	public String inspect() {
		return new StringBuilder().append(CmnCnst.ToString.INSPECT_FUNCTION_LANG_OBJECT).append('(')
				.append(value.getDeclaredName()).append(')').toString();
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
		final StringBuilder sb = new StringBuilder();
		toExpression(sb);
		return StringLangObject.create(sb);
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
		}, NullLangObject.getInstance(), null, 0);
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before
									// calling the function.
	public static FunctionLangObject create(final IFunction<? extends ALangObject> value,
			@Nullable final IClosure closure, final int closureSymbolTableSize) {
		return new FunctionLangObject((IFunction<ALangObject>) value, closure, closureSymbolTableSize);
	}

	public static FunctionLangObject createWithoutClosure(final IFunction<? extends ALangObject> value) {
		return create(value, null, 0);
	}

	@SuppressWarnings("unchecked") // Evaluate visitor checks the type before
									// calling the function.
	public static FunctionLangObject createForNullThisContext(final IFunction<NullLangObject> value,
			@Nullable final IClosure closure, final int closureSymbolTableSize) {
		final IFunction<?> f = value;
		return new FunctionLangObject((IFunction<ALangObject>) f, NullLangObject.getInstance(), closure,
				closureSymbolTableSize);
	}

	public static FunctionLangObject createForNullThisContextWithoutClosure(final IFunction<NullLangObject> value) {
		return createForNullThisContext(value, null, 0);
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

	private static class ClosureImpl implements IClosure {
		private final ALangObject[] symbolTable;
		@Nullable public final IClosure parent;
		public ClosureImpl(final int symbolTableSize, @Nullable final IClosure parent) {
			this.symbolTable = new ALangObject[symbolTableSize];
			this.parent = parent;
		}
		private ClosureImpl(final ALangObject[] symbolTable, @Nullable final IClosure parent, final boolean makeCopy) {
			this.symbolTable = makeCopy ? NullUtil.checkNotNull(Arrays.copyOf(symbolTable, symbolTable.length)) : symbolTable;
			this.parent = parent;
		}
		@Nullable
		@Override
		public IClosure getParent() {
			return parent;
		}
		@Override
		public IClosure copy() {
			return new ClosureImpl(symbolTable, parent, true);
		}
		// Cannot be null: setObject(...) does not accepts nulls, table is
		// filled with non-nulls during initialization.
		@SuppressWarnings("null")
		@Override
		public ALangObject getObject(final int source) {
			return source < symbolTable.length ? symbolTable[source] : NullLangObject.getInstance();
		}
		@Override
		public void setObject(final int source, final ALangObject object) {
			if (source < symbolTable.length)
				symbolTable[source] = object;
		}
	}
}