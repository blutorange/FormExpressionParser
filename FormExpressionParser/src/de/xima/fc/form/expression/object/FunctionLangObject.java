package de.xima.fc.form.expression.object;

import org.apache.commons.lang3.ArrayUtils;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;

public class FunctionLangObject extends ALangObject {

	private final IFunction value;
	
	private static class InstanceHolder {
		public final static FunctionLangObject NO_OP = FunctionLangObject.create(new IFunction(){
			@Override
			public ALangObject evaluate(IEvaluationContext ec, ALangObject... args) throws EvaluationException {
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
		}); 
	}
	
	private FunctionLangObject(IFunction value) {
		super(Type.FUNCTION);
		this.value = value;
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
	public void toExpression(StringBuilder builder) {
		builder.append("->(");
		for (String arg : value.getDeclaredArgumentList()) builder.append(arg).append(", ");
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
	public INamedFunction<? extends ALangObject> instanceMethod(String name, IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().instanceMethodFunction(name);
	}

	@Override
	public INamedFunction<? extends ALangObject> attrAccessor(String name, IEvaluationContext ec)
			throws EvaluationException {
		return ec.getNamespace().attrAccessorFunction(name);
	}

	@Override
	public ALangObject evaluateInstanceMethod(String name, IEvaluationContext ec, ALangObject... args)
			throws EvaluationException {
		return evaluateMethod(this, ec.getNamespace().instanceMethodFunction(name), name, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(String name, IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorFunction(name), name, ec);
	}
	
	public static FunctionLangObject getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}
	
	public static FunctionLangObject create(IFunction value) {
		if (value == null) return getNoOpInstance();
		return new FunctionLangObject(value);
	}
	
	
	
}
