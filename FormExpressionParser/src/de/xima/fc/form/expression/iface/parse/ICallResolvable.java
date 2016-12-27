package de.xima.fc.form.expression.iface.parse;

public interface ICallResolvable {
	/** @return The resolved call ID: Valid IDs must be >=0.*/
	public int getFunctionId();
	/** @param callId Call ID for resolution. Must be >=0. */
	public void resolveFunctionId(int functionId);
	/** @return Whether the call ID was already resolved. */
	public boolean isFunctionIdResolved();
}