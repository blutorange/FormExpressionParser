package de.xima.fc.form.expression.object;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.error.NullObjectAccessException;

public class NullLangObject extends ALangObject {
	private NullLangObject() {
		super(Type.NULL);
	}

	private static class InstanceHolder {
		private static NullLangObject INSTANCE = new NullLangObject();
	}

	public Object nullValue() {
		return null;
	}

	public static ALangObject create() {
		return getInstance();
	}

	/**
	 * @return <code>this</code>, as only one object may ever exist.
	 */
	@Override
	public ALangObject shallowClone() {
		return this;
	}

	/**
	 * @return <code>this</code>, as only one object may ever exist.
	 */
	@Override
	public ALangObject deepClone() {
		return shallowClone();
	}

	public static NullLangObject getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public ALangObject evaluateInstanceMethod(final String name, final IEvaluationContext ec, final ALangObject... args)
			throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}

	@Override
	public ALangObject evaluateAttrAccessor(final String name, final IEvaluationContext ec) throws EvaluationException {
		throw new NullObjectAccessException(this, ec);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof NullLangObject)) return false;
		return true;
	}

	@Override
	public String inspect() {
		return "NullLangObject";
	}

	@Override
	public String toString() {
		return "§null";
	}


}
