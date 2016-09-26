package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public interface INamespace {
	public IFunction<StringLangObject> expressionMethodString(EMethod method) throws EvaluationException;
	public IFunction<NumberLangObject> expressionMethodNumber(EMethod method) throws EvaluationException;
	public IFunction<ArrayLangObject> expressionMethodArray(EMethod method) throws EvaluationException;
	public IFunction<HashLangObject> expressionMethodHash(EMethod method) throws EvaluationException;
	public IFunction<BooleanLangObject> expressionMethodBoolean(EMethod method) throws EvaluationException;
	public IFunction<ExceptionLangObject> expressionMethodException(EMethod method) throws EvaluationException;
	public IFunction<FunctionLangObject> expressionMethodFunction(EMethod method) throws EvaluationException;

	public IFunction<StringLangObject> attrAccessorString(String name) throws EvaluationException;
	public IFunction<NumberLangObject> attrAccessorNumber(String name) throws EvaluationException;
	public IFunction<ArrayLangObject> attrAccessorArray(String name) throws EvaluationException;
	public IFunction<HashLangObject> attrAccessorHash(String name) throws EvaluationException;
	public IFunction<BooleanLangObject> attrAccessorBoolean(String name) throws EvaluationException;
	public IFunction<ExceptionLangObject> attrAccessorException(String name) throws EvaluationException;
	public IFunction<FunctionLangObject> attrAccessorFunction(String name) throws EvaluationException;
}
