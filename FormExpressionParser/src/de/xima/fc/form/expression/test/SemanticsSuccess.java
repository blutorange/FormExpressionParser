package de.xima.fc.form.expression.test;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.test.TestUtil.ETestType;
import de.xima.fc.form.expression.test.TestUtil.ITestCase;

enum SemanticsSuccess implements ITestCase {
	// Object literals.
	LITERALS001("null;", NullLangObject.getInstance()),
	LITERALS002("true;", BooleanLangObject.getTrueInstance()),
	LITERALS003("false;", BooleanLangObject.getFalseInstance()),
	LITERALS004("42;", NumberLangObject.create(42)),
	LITERALS005("42.35;", NumberLangObject.create(42.35d)),
	LITERALS006("'single';", StringLangObject.create("single")),
	LITERALS007("'double';", StringLangObject.create("double")),
	LITERALS008("'escape\\\\';", StringLangObject.create("escape\\")),
	LITERALS009("'吵';", StringLangObject.create("吵")),
	LITERALS010("'\u5435';", StringLangObject.create("吵")),
	LITERALS011("[];", ArrayLangObject.create()),
	LITERALS012("[null, true, 0, '', [], {}];", ArrayLangObject.create(
			NullLangObject.getInstance(),
			BooleanLangObject.getTrueInstance(),
			NumberLangObject.getZeroInstance(),
			StringLangObject.getEmptyInstance(),
			ArrayLangObject.create(),
			HashLangObject.create()
			)),
	LITERALS013("{};", HashLangObject.create()),
	LITERALS014("{null:true, 0:'',[]:{},false:1};", HashLangObject.create(
			NullLangObject.getInstance(),
			BooleanLangObject.getTrueInstance(),
			NumberLangObject.getZeroInstance(),
			StringLangObject.getEmptyInstance(),
			ArrayLangObject.create(),
			HashLangObject.create(),
			BooleanLangObject.getFalseInstance(),
			NumberLangObject.getOneInstance()
			)),
	LITERALS015("->(x){x;}(42);", Tests.N42),

	// Variables and scopes.
	SCOPE001("k=42;k;", Tests.N42), // local variables
	SCOPE002("k=0;if(true)k=42;k;", Tests.N42), // can access parent when nesting
	SCOPE003("k=42;->(){k=0;}();k;", Tests.N42), // functions with separate scope
	SCOPE004("k=42;function foo(){k=0;};foo();k;", Tests.N42), // functions with separate scope
	SCOPE005("k=42;->(){k;}();", Tests.N42), // accessing global variables
	SCOPE006("k=0;->(){k=42;k;}();", Tests.N42), // shadowing global variables
	SCOPE007("foo::k=0;bar::k=42;bar::k;",Tests.N42), // scopes
	SCOPE008("foo::k=0;bar::k=42;with(foo,bar)k;",Tests.N42), // default scopes reading vars
	SCOPE009("foo::k=0;with(foo){k=42;k;}", Tests.N42), // default scopes writing to vars
	SCOPE010("foo::k=42;with(foo){k=0;foo::k;}", Tests.N42), // can still access explicitly
	SCOPE011("k=42;foo::k=0;with(foo){k;}", Tests.N42), // first global, then default scope

	// Expression methods number.
	EMETHODUN001("-(-42);", Tests.N42),
	EMETHODUN002("+42;", Tests.N42),
	EMETHODBIN001("40+2;", Tests.N42),
	EMETHODBIN002("50-8;", Tests.N42),
	EMETHODBIN003("2*21;", Tests.N42),
	EMETHODBIN004("168/4;", Tests.N42),
	
	//General
	GENERAL001("a=-(b=1);for(i:20)b=a+(a=b);", NumberLangObject.create(4181)) // Fibonacci
	;

	private final String code;
	private final ALangObject expectedResult;
	private SemanticsSuccess(final String code, final ALangObject expectedResult) {
		this.code = code;
		this.expectedResult = expectedResult;
	}

	@Override
	public ETestType getTestType() {
		return ETestType.PROGRAM;
	}

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public ALangObject getExpectedResult() {
		return expectedResult;
	}
	@Override
	public Class<? extends EvaluationException> getExpectedException() {
		return null;
	}
	@Override
	public boolean isPerformEvaluation() {
		return true;
	}

	@Override
	public String getErrorBegin() {
		return null;
	}
}