package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IFunction;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.VoidType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public abstract class SimpleFunctionVariableProvider<R extends ALangObject>
		extends AVariableProvider<FunctionLangObject> implements IFunction<NullLangObject> {
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String[] args;

	public SimpleFunctionVariableProvider(final Class<R> clazz, final String name, final IVariableType returnType,
			final Arg... args) {
		super(GenericVariableType.forSimpleFunction(returnType, unwrap(args)));
		if (returnType.getBasicLangClass().getLangObjectClass() != clazz)
			throw new FormExpressionException(NullUtil.messageFormat(
					"Basic variable type {0} does not match return type {1}.", returnType.getBasicLangClass(), clazz));
		this.name = name;
		this.args = new String[args.length];
		for (int i = args.length; i-- > 0;)
			this.args[i] = args[i].name;
	}

	protected abstract R evaluate(IEvaluationContext ec, ALangObject[] args) throws EvaluationException;

	@Override
	public final FunctionLangObject make() {
		return FunctionLangObject.createForNullThisContextWithoutClosure(this);
	}

	@Override
	public final ALangObject evaluate(final IEvaluationContext ec, final NullLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		return evaluate(ec, args);
	}

	@Override
	public final String getDeclaredName() {
		return name;
	}

	// Nullness already checked during construction, Arg helper does not allow
	// null.
	@SuppressWarnings("null")
	@Override
	public final String getDeclaredArgument(final int i) throws ArrayIndexOutOfBoundsException {
		return args[i];
	}

	@Override
	public final int getDeclaredArgumentCount() {
		return args.length;
	}

	@Override
	public final ILangObjectClass getThisContextType() {
		return ELangObjectClass.NULL;
	}

	@Override
	public final boolean hasVarArgs() {
		return false;
	}

	private static IVariableType[] unwrap(final Arg[] args) {
		final IVariableType[] t = new IVariableType[args.length];
		for (int i = args.length; i-- > 0;)
			t[i] = args[i].type;
		return t;
	}

	/**
	 * Helper class for passing parameters to the constructor.
	 *
	 * @author madgaksha
	 */
	public final static class Arg {
		public final String name;
		public final IVariableType type;

		private Arg(final String name, final IVariableType type) {
			this.name = name;
			this.type = type;
		}

		public static Arg i(final String name, final IVariableType type) {
			return new Arg(name, type);
		}
	}

	public abstract static class SingleArgFunctionVariableProvider<T extends ALangObject>
			extends SimpleFunctionVariableProvider<NullLangObject> {
		private static final long serialVersionUID = 1L;
		private final ILangObjectClass argClass;

		public SingleArgFunctionVariableProvider(final String name, final String argName, final IVariableType argType) {
			super(NullLangObject.class, name, VoidType.INSTANCE, Arg.i(argName, argType));
			this.argClass = argType.getBasicLangClass();
		}

		@Override
		protected final NullLangObject evaluate(final IEvaluationContext ec, final ALangObject[] args)
				throws EvaluationException {
			if (args.length > 0)
				compute(args[0].<T> coerce(argClass, ec), ec);
			return NullLangObject.getInstance();
		}

		protected abstract void compute(T arg, IEvaluationContext ec) throws EvaluationException;
	}
}