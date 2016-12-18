package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.CustomRuntimeException;
import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.exception.evaluation.IllegalNumberOfFunctionParametersException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.impl.config.SeverityConfig;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;

@SuppressWarnings("nls")
enum SemanticsFailure implements ITestCase {
	TEST001("throw exception('HelloWorld');", CustomRuntimeException.class,"Custom Exception: HelloWorld"),
	TEST004("function foo(arg1,arg2){}foo(1);", IllegalNumberOfFunctionParametersException.class, "Function requires 2 parameter(s), but 1 were given."),
	TEST005("function foo(arg1,arg2,args...){args;}foo(1);", IllegalNumberOfFunctionParametersException.class, "Function requires 2 parameter(s), but 1 were given."),
	TEST006("function foo(){42;}foo(1,2,3);", IllegalNumberOfFunctionParametersException.class, "Function requires 0 parameter(s), but 3 were given."),
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
	@Override
	public ISeverityConfig getSeverityConfig() {
		return SeverityConfig.getLooseConfig();
	}
}