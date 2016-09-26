package de.xima.fc.form.expression.object;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;

public class FunctionLangObject extends ALangObject {

	private final IFunction<ALangObject> value;

	private static class InstanceHolder {
		public final static FunctionLangObject NO_OP = new FunctionLangObject(new IFunction<ALangObject>(){
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final ALangObject thisContext, final ALangObject... args) throws EvaluationException {
				return NullLangObject.getInstance();
			}
			@Override
			public String[] getDeclaredArgumentList() {
				return ArrayUtils.EMPTY_STRING_ARRAY;
			}
			@Override
			public Node getNode() {
				return null;
			}
			@Override
			public String getDeclaredName() {
				return StringUtils.EMPTY;
			}
			@Override
			public Type getThisContextType() {
				return ALangObject.Type.NULL;
			}
		});
	}

	private FunctionLangObject(final IFunction<ALangObject> value) {
		super(Type.FUNCTION);
		this.value = value;
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
		builder.append("->(");
		for (final String arg : value.getDeclaredArgumentList()) builder.append(arg).append(", ");
		// Remove final comma
		if (builder.length()>3) builder.setLength(builder.length()-1);
		builder.append(") {\n");
		final Node n = value.getNode();
		if (n == null)
			builder.append("[native code]");
		else
			builder.append("[to be implemented]");
		builder.append("\n};");
	}

	@Override
	public IFunction<FunctionLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodFunction(method);
	}

	@Override
	public IFunction<FunctionLangObject> attrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorFunction(name);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodFunction(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorFunction(name), name, ec);
	}

	public static FunctionLangObject getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	public static ALangObject create(final IFunction<? extends ALangObject> value) {
		if (value == null) return getNoOpInstance();
		return new FunctionLangObject((IFunction<ALangObject>)value);
	}
}
