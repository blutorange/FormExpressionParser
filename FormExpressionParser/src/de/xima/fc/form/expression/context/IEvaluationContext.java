package de.xima.fc.form.expression.context;

public interface IEvaluationContext {

	public IBinding getBinding();
	public INamespace getNamespace();
	public int getRecursionLimit();

	// Probably not needed anymore because of visitor pattern.
	//	public void nestBinding();
	//	public void unnestBinding();


	/**
	 * Must be an equivalence relation.
	 * @param name1
	 *            Name of one variable.
	 * @param name2
	 *            Name of another variable.
	 * @return Whether two variable names are equivalent and refer to the same
	 *         variable. Default could be {@link String#equals(Object)}
	 */
	public boolean variableNameEquals(String name1, String name2);
}
