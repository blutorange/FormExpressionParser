package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public interface INamespace {
	public IFunction<StringLangObject> expressionMethodString(EMethod method) throws EvaluationException;
	public IFunction<NumberLangObject> expressionMethodNumber(EMethod method) throws EvaluationException;
	public IFunction<ArrayLangObject> expressionMethodArray(EMethod method) throws EvaluationException;
	public IFunction<HashLangObject> expressionMethodHash(EMethod method) throws EvaluationException;
	public IFunction<BooleanLangObject> expressionMethodBoolean(EMethod method) throws EvaluationException;
	public IFunction<ExceptionLangObject> expressionMethodException(EMethod method) throws EvaluationException;
	public IFunction<FunctionLangObject> expressionMethodFunction(EMethod method) throws EvaluationException;
	public IFunction<RegexLangObject> expressionMethodRegex(EMethod method) throws EvaluationException;

	public IFunction<StringLangObject> attrAccessorString(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<NumberLangObject> attrAccessorNumber(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<ArrayLangObject> attrAccessorArray(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<HashLangObject> attrAccessorHash(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<BooleanLangObject> attrAccessorBoolean(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<ExceptionLangObject> attrAccessorException(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<FunctionLangObject> attrAccessorFunction(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<RegexLangObject> attrAccessorRegex(ALangObject name, boolean accessedViaDot) throws EvaluationException;

	public IFunction<StringLangObject> attrAssignerString(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<NumberLangObject> attrAssignerNumber(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<ArrayLangObject> attrAssignerArray(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<HashLangObject> attrAssignerHash(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<BooleanLangObject> attrAssignerBoolean(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<ExceptionLangObject> attrAssignerException(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<FunctionLangObject> attrAssignerFunction(ALangObject name, boolean accessedViaDot) throws EvaluationException;
	public IFunction<RegexLangObject> attrAssignerRegex(ALangObject name, boolean accessedViaDot) throws EvaluationException;
}
