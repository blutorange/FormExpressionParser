package de.xima.fc.form.expression.iface.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ICallResolvable {
	/** @return The resolved call ID: Valid IDs must be >=0.*/
	public Integer getFunctionId();
	/** @param callId Call ID for resolution. Must be >=0. */
	public void resolveFunctionId(Integer functionId);
	/** @return Whether the call ID was already resolved. */
	public boolean isFunctionIdResolved();
	public void resolveClosureTableSize(int closureTableSize);
	public int getClosureTableSize();
	public boolean isClosureTableSizeResolved();
}