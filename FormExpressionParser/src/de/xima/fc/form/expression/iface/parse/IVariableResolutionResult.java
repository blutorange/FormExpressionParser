package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IVariableResolutionResult {
	/**
	 * Maps between the old variable ID and a consecutive new ID.
	 * @param oldVariableId The old varibale ID.
	 * @return The new ID, or <code>null</code> when ther is no mapping.
	 */
	@Nullable
	public Integer getMappedEnvironmental(Integer oldVariableId);
	public int getEnvironmentalSize();
	@Nullable
	public Integer getMappedClosure(Integer functionId, Integer source);
	public int getClosureSize(Integer functionId);
	public int getInternalVariableCount();
}
