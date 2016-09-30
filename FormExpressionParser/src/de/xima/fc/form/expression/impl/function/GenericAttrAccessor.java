package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.MathException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

/**
 * A set of generic attribute accessors for elements. Each object has got a
 * pre-defined set of named attributes, eg. <code>length</code> for
 * {@link ArrayLangObject}s or <code>message</code> for
 * {@link ExceptionLangObject}s. When none of these match, these functions will
 * be called. For example, they can be used to access an entry of a
 * {@link HashLangObject}, or the character at a certain index of a
 * {@link StringLangObject}.
 *
 * <br><br>
 *
 * The argument array is guaranteed (by the grammar) to contain exactly one entry, ie. <code>args[0]</code> may be accessed safely.
 *
 * @author madgaksha
 *
 * @param <T>
 */
public abstract class GenericAttrAccessor<T extends ALangObject> implements IFunction<T> {
	/**
	 * @param index {@link NumberLangObject} The index of the character to get from the string. May be negative to address elements from the end of the array, eg. <code>-1</code> for the last element.
	 * @return {@link StringLangObject}. The character at the given position.
	 * @NullLangObject When there is no element at the given position, ie. either when <code>index&gt;=string.length</code> or <code>index &lt; -string.length</code>.
	 * @throws MathException When the index is too large or too small to be represented as an <code>int</code>.
	 */
	public final static IFunction<StringLangObject> STRING = new GenericAttrAccessor<StringLangObject>(Type.STRING,
			"genericAttrAccessorString", "index") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final StringLangObject thisContext, final ALangObject... args)
				throws EvaluationException, MathException {
			final int index = args[0].coerceNumber(ec).intValue();
			final int len = thisContext.length();
			if (index >= len || index < -len) return NullLangObject.getInstance();
			return StringLangObject.create(thisContext.stringValue().charAt(index < 0 ? index+len : index));
		}
	};

	/**
	 * @param index {@link NumberLangObject} The index of the character to get from the array. May be negative to address elements from the end of the array, eg. <code>-1</code> for the last element.
	 * @return {@link ALangObject}. The element at the given position.
	 * @NullLangObject When there is no element at the given position, ie. either when <code>index&gt;=string.length</code> or <code>index &lt; -string.length</code>.
	 * @throws MathException When the index is too large or too small to be represented as an <code>int</code>.
	 */
	public final static IFunction<ArrayLangObject> ARRAY = new GenericAttrAccessor<ArrayLangObject>(Type.ARRAY,
			"genericAttrAccessorArray", "index") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
				throws EvaluationException, MathException {
			final int index = args[0].coerceNumber(ec).intValue();
			final int len = thisContext.length();
			if (index >= len || index < -len) return NullLangObject.getInstance();
			return thisContext.get(index < 0 ? index+len : index);
		}
	};

	/**
	 * @return {@link NullLangObject}. No generic attribute accessors.
	 */
	public final static IFunction<BooleanLangObject> BOOLEAN = new GenericAttrAccessor<BooleanLangObject>(Type.BOOLEAN,
			"genericAttrAccessorBoolean", "null") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NullLangObject.getInstance();
		}
	};

	/**
	 * @return {@link NullLangObject}. No generic attribute accessors.
	 */
	public final static IFunction<ExceptionLangObject> EXCEPTION = new GenericAttrAccessor<ExceptionLangObject>(Type.EXCEPTION,
			"genericAttrAccessorException", "null") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NullLangObject.getInstance();
		}
	};

	/**
	 * @return {@link NullLangObject}. No generic attribute accessors.
	 */
	public final static IFunction<FunctionLangObject> FUNCTION = new GenericAttrAccessor<FunctionLangObject>(Type.FUNCTION,
			"genericAttrAccessorFunction", "null") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NullLangObject.getInstance();
		}
	};

	/**
	 * @return {@link NullLangObject}. No generic attribute accessors.
	 */
	public final static IFunction<NumberLangObject> NUMBER = new GenericAttrAccessor<NumberLangObject>(Type.NUMBER,
			"genericAttrAccessorFunction", "null") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NullLangObject.getInstance();
		}
	};

	/**
	 * @param key {@link ALangObject} The key. When not specified, {@link NullLangObject} is assumed.
	 * @return {@link ALangObject} The object that is mapped to the given key.
	 * @NullLangObject When the map does not contain any value for the key, or the key is mapped to {@link NullLangObject}. Use {@link EAttrAccessorHash#contains} to check.
	 */
	public final static IFunction<HashLangObject> HASH = new GenericAttrAccessor<HashLangObject>(Type.HASH,
			"genericAttrAccessorHash", "key") {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final HashLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return thisContext.get(args[0]);
		}
	};

	private final String name;
	private final String[] argList;
	private final Type type;

	private GenericAttrAccessor(final Type type, final String name, final String... argList) {
		this.type = type;
		this.name = name;
		this.argList = argList;
	}

	@Override
	public String getDeclaredName() {
		return name;
	}

	@Override
	public String[] getDeclaredArgumentList() {
		return argList;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public Type getThisContextType() {
		return type;
	}

	@Override
	public abstract ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args)
			throws EvaluationException;
}