package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IllegalThisContextException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;

@SuppressWarnings("nls")
enum SemanticsFailure implements ITestCase {
	TEST001("k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST002("break;", BreakClauseException.class,"Break used outside of loop or switch, or label does not match any loop or switch."),
	TEST003("continue;", ContinueClauseException.class,"Continue used outside of loop or switch, or label does not match any loop or switch."),
	TEST004("throw error('HelloWorld');", CustomRuntimeException.class,"Custom Exception: HelloWorld"),
	TEST005("if (true) k=0;k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST006("for(i:2) k=0;k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST007("try{k=0;}catch(e){k=0;};k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST008("h={'f':''.toLowerCase};h.f();", IllegalThisContextException.class, "Provided this context <{\"f\":->(locale){'[native code]'};}> of type HASH does not match the expected type STRING for function <toLowerCase>."),
	TEST009("foo=''.toLowerCase;foo.call();", IllegalThisContextException.class, "Provided this context <null> of type NULL does not match the expected type STRING for function <toLowerCase>."),
	;

	@Nonnull private final String code;
	@Nonnull private final EContextType context;
	private final String errorBegin;
	private final Class<? extends EvaluationException> expectedException;
	private SemanticsFailure (@Nonnull final String code, final Class<? extends EvaluationException> exception) {
		this(code, exception, null);
	}
	private SemanticsFailure (@Nonnull final String code, final Class<? extends EvaluationException> exception, final String errorBegin) {
		this(code, exception, EContextType.GENERIC, errorBegin);
	}
	private SemanticsFailure (@Nonnull final String code, final Class<? extends EvaluationException> exception, @Nonnull final EContextType context, final String errorBegin) {
		this.code = code;
		this.expectedException = exception;
		this.errorBegin = errorBegin;
		this.context = context;
	}
	@Override
	public ETestType getTestType() {
		return ETestType.PROGRAM;
	}
	@Override
	public EContextType getContextType() {
		return context;
	}
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public ALangObject getExpectedResult() {
		return NullLangObject.getInstance();
	}
	@Override
	public Class<? extends EvaluationException> getExpectedException() {
		return expectedException;
	}
	@Override
	public boolean isPerformEvaluation() {
		return true;
	}
	@Override
	public String getErrorBegin() {
		return errorBegin;
	}
}