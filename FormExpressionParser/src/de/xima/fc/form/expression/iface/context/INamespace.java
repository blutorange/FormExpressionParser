package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

/**
 * <p>
 * A namespace that contains all available functions and attribute accessors for each type of
 * language object. For example, an array has got functions such as <code>length</code> or
 * <code>sort</code>, and the attribute accessor that return the i-th element <code>myArray[i]</code>.
 * </p><p>
 * Note that attribute accessors may be written in two ways: for a language object of type hash
 * <code>myHash.myValue</code> and <code>myHash["myValue"]</code> are equivalent. Whether an attribute
 * is accessed via the dotNotation or not is indicated by the boolean argument <code>accessedViaDot</code>
 * all attribute accessors and assigners get passed.
 * </p><p>
 * An attribute assigner is the opposite of an attribute accessor assigns a value to some attribute
 * of an object, eg <code>myArray[0] = 1</code>.
 * </p>
 * @author madgaksha
 *
 */
public interface INamespace {
	@Nullable public IFunction<StringLangObject> expressionMethodString(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<NumberLangObject> expressionMethodNumber(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<ArrayLangObject> expressionMethodArray(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<HashLangObject> expressionMethodHash(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<BooleanLangObject> expressionMethodBoolean(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<ExceptionLangObject> expressionMethodException(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<FunctionLangObject> expressionMethodFunction(@Nonnull EMethod method) throws EvaluationException;
	@Nullable public IFunction<RegexLangObject> expressionMethodRegex(@Nonnull EMethod method) throws EvaluationException;

	@Nullable public IFunction<StringLangObject> attrAccessorString(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<NumberLangObject> attrAccessorNumber(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<ArrayLangObject> attrAccessorArray(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<HashLangObject> attrAccessorHash(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<BooleanLangObject> attrAccessorBoolean(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<ExceptionLangObject> attrAccessorException(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<FunctionLangObject> attrAccessorFunction(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<RegexLangObject> attrAccessorRegex(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;

	@Nullable public IFunction<StringLangObject> attrAssignerString(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<NumberLangObject> attrAssignerNumber(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<ArrayLangObject> attrAssignerArray(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<HashLangObject> attrAssignerHash(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<BooleanLangObject> attrAssignerBoolean(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<ExceptionLangObject> attrAssignerException(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<FunctionLangObject> attrAssignerFunction(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
	@Nullable public IFunction<RegexLangObject> attrAssignerRegex(@Nonnull ALangObject name, boolean accessedViaDot) throws EvaluationException;
}
