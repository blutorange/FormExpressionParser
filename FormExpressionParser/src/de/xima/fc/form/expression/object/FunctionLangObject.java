package de.xima.fc.form.expression.object;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.visitor.UnparseVisitor;
import de.xima.fc.form.expression.visitor.UnparseVisitorConfig;

public class FunctionLangObject extends ALangObject {

	private final static Logger LOG = LoggerFactory.getLogger(FunctionLangObject.class);
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
			@Override
			public String getVarArgsName() {
				return null;
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
		// Add arguments.
		for (final String arg : value.getDeclaredArgumentList()) builder.append(arg).append(",");
		// Remove final comma
		if (builder.length()>3) builder.setLength(builder.length()-1);
		builder.append("){");
		// Convert body.
		final Node n = value.getNode();
		if (n == null)
			builder.append("'[native code]'");
		else {
			String unparse;
			try {
				unparse = UnparseVisitor.unparse(n, UnparseVisitorConfig.getUnstyledWithCommentsConfig());
			}
			catch (final IOException e) {
				LOG.error("Failed to unparse lambda", e);
				unparse = "'[error during unparsing]'";
			}
			builder.append(unparse);
		}
		builder.append("};");
	}

	@Override
	public String inspect() {
		return "FunctionLangObject(" + value.getDeclaredName() + ")";
	}

	@Override
	public IFunction<FunctionLangObject> expressionMethod(final EMethod method, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().expressionMethodFunction(method);
	}

	@Override
	public IFunction<FunctionLangObject> attrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAccessorFunction(object, accessedViaDot);
	}

	@Override
	public IFunction<FunctionLangObject> attrAssigner(final ALangObject name, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return ec.getNamespace().attrAssignerFunction(name, accessedViaDot);
	}

	@Override
	public ALangObject evaluateExpressionMethod(final EMethod method, final IEvaluationContext ec, final ALangObject... args) throws EvaluationException {
		return evaluateExpressionMethod(this, ec.getNamespace().expressionMethodFunction(method), method, ec, args);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final ALangObject object, final boolean accessedViaDot, final IEvaluationContext ec) throws EvaluationException {
		return evaluateAttrAccessor(this, ec.getNamespace().attrAccessorFunction(object, accessedViaDot), object, accessedViaDot, ec);
	}

	@Override
	public void executeAttrAssigner(final ALangObject object, final boolean accessedViaDot, final ALangObject value, final IEvaluationContext ec) throws EvaluationException {
		executeAttrAssigner(this, ec.getNamespace().attrAssignerFunction(object, accessedViaDot), object, accessedViaDot, value, ec);
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
		if (o == this) return true;
		if (!(o instanceof FunctionLangObject)) return false;
		return super.equalsSameObject((FunctionLangObject)o);
	}

	@Override
	public int compareToSameType(final ALangObject o) {
		return super.compareById(o);
	}

	// Coercion
	@Override
	public StringLangObject coerceString(final IEvaluationContext ec) {
		return StringLangObject.create(value.getDeclaredName());
	}

	@Override
	public FunctionLangObject coerceFunction(final IEvaluationContext ec) {
		return this;
	}

	public static FunctionLangObject getNoOpInstance() {
		return InstanceHolder.NO_OP;
	}

	public static FunctionLangObject create(final IFunction<? extends ALangObject> value) {
		if (value == null) return getNoOpInstance();
		return new FunctionLangObject((IFunction<ALangObject>)value);
	}
}
