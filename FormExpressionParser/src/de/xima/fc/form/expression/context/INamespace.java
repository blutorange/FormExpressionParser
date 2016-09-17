package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public interface INamespace {
	public INamedFunction<NullLangObject> globalMethod(String name) throws EvaluationException;

	public INamedFunction<StringLangObject> instanceMethodString(String name) throws EvaluationException;
	public INamedFunction<NumberLangObject> instanceMethodNumber(String name) throws EvaluationException;
	public INamedFunction<ArrayLangObject> instanceMethodArray(String name) throws EvaluationException;
	public INamedFunction<HashLangObject> instanceMethodHash(String name) throws EvaluationException;
	public INamedFunction<BooleanLangObject> instanceMethodBoolean(String name) throws EvaluationException;

	public INamedFunction<StringLangObject> attrAccessorString(String name) throws EvaluationException;
	public INamedFunction<NumberLangObject> attrAccessorNumber(String name) throws EvaluationException;
	public INamedFunction<ArrayLangObject> attrAccessorArray(String name) throws EvaluationException;
	public INamedFunction<HashLangObject> attrAccessorHash(String name) throws EvaluationException;
	public INamedFunction<BooleanLangObject> attrAccessorBoolean(String name) throws EvaluationException;
}
