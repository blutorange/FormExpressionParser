package de.xima.fc.form.expression.test;

import de.xima.fc.form.expression.exception.BreakClauseException;
import de.xima.fc.form.expression.exception.ContinueClauseException;
import de.xima.fc.form.expression.exception.CustomRuntimeException;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.exception.IllegalThisContextException;
import de.xima.fc.form.expression.exception.VariableNotDefinedException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.test.TestUtil.EContextType;
import de.xima.fc.form.expression.test.TestUtil.ETestType;
import de.xima.fc.form.expression.test.TestUtil.ITestCase;

enum SemanticsFailure implements ITestCase {
	TEST001("k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST002("break;", BreakClauseException.class,"Break used outside of loop or switch, or label does not match any loop or switch."),
	TEST003("continue;", ContinueClauseException.class,"Continue used outside of loop or switch, or label does not match any loop or switch."),
	TEST004("throw error('HelloWorld');", CustomRuntimeException.class,"Custom Exception: HelloWorld"),
	TEST005("if (true) k=0;k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST006("for(i:2) k=0;k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST007("try{k=0;}catch(e){k=0;};k;", VariableNotDefinedException.class,"Variable k not resolvable to a defined variable."),
	TEST008("h={'f':''.toLowerCase};h.f();", IllegalThisContextException.class),
	;

	private final String code;
	private final String errorBegin;
	private final EContextType context;
	private final Class<? extends EvaluationException> expectedException;
	private SemanticsFailure (final String code, final Class<? extends EvaluationException> exception) {
		this(code, exception, null);
	}
	private SemanticsFailure (final String code, final Class<? extends EvaluationException> exception, final String errorBegin) {
		this(code, exception, EContextType.GENERIC, errorBegin);
	}
	private SemanticsFailure (final String code, final Class<? extends EvaluationException> exception, final EContextType context, final String errorBegin) {
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