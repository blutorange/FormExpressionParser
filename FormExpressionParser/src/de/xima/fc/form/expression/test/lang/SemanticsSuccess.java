package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;
import de.xima.fc.form.expression.util.NullUtil;

@SuppressWarnings("nls")
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
	LITERALS016("{foo:'bar'};", HashLangObject.create(
			StringLangObject.create("foo"),
			StringLangObject.create("bar")
			)),
	LITERALS017("#^\\#$#.matches('#');", Tests.TRUE),

	// Variables and scopes.
	SCOPE001("k=42;k;", Tests.N42), // local variables
	SCOPE002("k=0;if(true)k=42;k;", Tests.N42), // can access parent when nesting
	SCOPE003("k=42;->(){var k=0;}();k;", Tests.N42), // functions with separate scope
	SCOPE004("k=42;function foo(){var k=0;};foo();k;", Tests.N42), // functions with separate scope
	SCOPE005("k=42;->(){k;}();", Tests.N42), // accessing global variables
	SCOPE006("k=0;->(){k=42;k;}();", Tests.N42), // shadowing global variables
	SCOPE007("foo::k=0;bar::k=42;bar::k;",Tests.N42), // scopes
	SCOPE008("foo::k=0;bar::k=42;with(foo,bar)k;",Tests.N42), // default scopes reading vars
	SCOPE009("foo::k=0;with(foo){k=42;k;}", Tests.N42), // default scopes writing to vars
	SCOPE010("foo::k=42;with(foo){k=0;foo::k;}", Tests.N42), // can still access explicitly
	SCOPE011("k=42;foo::k=0;with(foo){k;}", Tests.N42), // first global, then default scope
	SCOPE012("function foo(){0;}function foo::bar(){42;};with(foo)bar();", Tests.N42), // scoped function
	SCOPE013("scope myscope{var a=42;}with(myscope){a;}", Tests.N42), // resolving scopes

	//Attribute accessors
	PROP001("'Ab3'.toUpperCase.toLowerCase;", StringLangObject.create("ab3")),
	PROP002("s='Ab3';f=s.toLocaleLowerCase;f.call(s,null);", StringLangObject.create("ab3")),
	PROP003("h={'f':->(){42;}};h.f();", Tests.N42), // can call methods from hashes

	// Attribute assigners
	ATTRASS001("a=[];a.length=8;a.length;", NumberLangObject.create(8)),
	ATTRASS002("a=[1,2,3,4];a.length=2;a.length;", NumberLangObject.create(2)),

	// Expression methods number.
	EMETHODUN000("-42;", NumberLangObject.create(-42)),
	EMETHODUN001("-(-42);", Tests.N42),
	EMETHODUN002("+42;", Tests.N42),
	EMETHODUN003("!42;", BooleanLangObject.getFalseInstance()),
	EMETHODUN004("!!42;", BooleanLangObject.getTrueInstance()),
	EMETHODUN005("!'asd';", BooleanLangObject.getFalseInstance()),
	EMETHODUN006("!#foo#;", BooleanLangObject.getFalseInstance()),
	EMETHODUN007("!->(){};", BooleanLangObject.getFalseInstance()),
	EMETHODUN008("!null;", BooleanLangObject.getTrueInstance()),
	EMETHODUN009("!true;", BooleanLangObject.getFalseInstance()),
	EMETHODUN010("!false;", BooleanLangObject.getTrueInstance()),
	EMETHODUN011("a=-43;-++a;", Tests.N42),
	EMETHODUN012("a=42;a++;", Tests.N42),
	EMETHODUN013("a=41;a++;a;", Tests.N42),
	EMETHODUN014("40+ +2;", Tests.N42),
	EMETHODBIN001("40+2;", Tests.N42),
	EMETHODBIN002("50-8;", Tests.N42),
	EMETHODBIN003("2*21;", Tests.N42),
	EMETHODBIN004("168/4;", Tests.N42),
	EMETHODBIN005("42==42.0;", Tests.TRUE),
	EMETHODBIN006("[1,2]==[1,2];", Tests.TRUE),
	EMETHODBIN007("[1,2]===[1,2];", Tests.FALSE),
	EMETHODBIN008("a=[1,2]; a===a;", Tests.TRUE),
	EMETHODBIN009("48%7;;", NumberLangObject.create(6)),
	EMETHODBIN010("-50%7;;", NumberLangObject.create(6)),
	EMETHODBIN011("1==1;", Tests.TRUE),
	EMETHODBIN012("1===1;", Tests.TRUE),
	EMETHODBIN013("1<2;", Tests.TRUE),
	EMETHODBIN014("2<1;", Tests.FALSE),
	EMETHODBIN015("1<=1;", Tests.TRUE),
	EMETHODBIN016("1>=1;", Tests.TRUE),
	EMETHODBIN017("1!=1;", Tests.FALSE),
	EMETHODBIN018("1!==1;", Tests.FALSE),
	EMETHODBIN019("'1'=='1';", Tests.TRUE),
	EMETHODBIN020("'1'==='1';", Tests.FALSE),
	EMETHODBIN021("'1'<'2';", Tests.TRUE),
	EMETHODBIN022("'2'<'1';", Tests.FALSE),
	EMETHODBIN023("'1'<='1';", Tests.TRUE),
	EMETHODBIN024("'1'>='1';", Tests.TRUE),
	EMETHODBIN025("'1'!='1';", Tests.FALSE),
	EMETHODBIN026("'1'!=='1';", Tests.TRUE),
	EMETHODBIN027("1<'1';", Tests.TRUE),


	// Variable resolution
	RESOLUTION001("i=0;if(true)i=42;i;", Tests.N42),
	RESOLUTION002("i=42;if(true)var i=0;i;", Tests.N42),
	RESOLUTION003("global scope{var baz='global';var t1='bug1';var t2='bug2';}scope foo{var baz='foo';}scope bar{var baz='bar';}if(true){var baz='bug';t1='';}function foobar(){var baz='bug3';t2='';};foobar();baz+foo::baz+bar::baz+t1+t2;", StringLangObject.create("globalfoobar")),

	// Assignment
	ASSIGNMENT001("a=0;b=23;c=19;a += b += c;",Tests.N42),
	ASSIGNMENT002("a=[2,3,37];a[0]+=a[1]+=a[2];a;", ArrayLangObject.create(
			NumberLangObject.create(42),
			NumberLangObject.create(40),
			NumberLangObject.create(37)
			)),
	ASSIGNMENT003("a={a:[2],b:[0,3],c:[0,0,37]};a.a[0]+=a.b[1]+=a.c[2];a;", HashLangObject.create(
			StringLangObject.create("a"), ArrayLangObject.create(NumberLangObject.create(42)),
			StringLangObject.create("b"), ArrayLangObject.create(Tests.N0,NumberLangObject.create(40)),
			StringLangObject.create("c"), ArrayLangObject.create(Tests.N0, Tests.N0,NumberLangObject.create(37))
			)),
	ASSIGNMENT004("a=41;++a;++a;--a;a;",Tests.N42),

	// Functions and lambda support
	FUNCTION001("function foo(a){a;}foo(3);", NumberLangObject.create(3)),
	FUNCTION002("function foo(){this;}foo();", NullLangObject.getInstance()),
	FUNCTION003("foo=''.toLocaleLowerCase;foo.call('BAR',null);", StringLangObject.create("bar")),
	FUNCTION004("foo=->(){42;};foo();", Tests.N42),
	FUNCTION005("function foo(arg1,arg2,args...){args;}foo(1,2);", ArrayLangObject.create()),
	FUNCTION006("function foo(arg1,arg2,args...){args;}foo(1,2,3);", ArrayLangObject.create(NumberLangObject.create(3))),
	FUNCTION007("function foo(arg1,arg2,args...){args;}foo(1,2,3,4);", ArrayLangObject.create(NumberLangObject.create(3),NumberLangObject.create(4))),
	FUNCTION008("function foo(args...){args;}foo();", ArrayLangObject.create()),
	FUNCTION009("function foo(args...){args;}foo(3);", ArrayLangObject.create(NumberLangObject.create(3))),
	FUNCTION010("function foo(args...){args;}foo(3,4);", ArrayLangObject.create(NumberLangObject.create(3),NumberLangObject.create(4))),
	FUNCTION011("function foo(arg1,arg2,args...){[arg1,arg2];}foo(1,2);", ArrayLangObject.create(NumberLangObject.create(1),NumberLangObject.create(2))),

	//General
	GENERAL001("a=-(b=1);for(i in 20)b=a+(a=b);", NumberLangObject.create(4181)), // Fibonacci
	GENERAL002("false ? 0 : 42;", Tests.N42), // Ternary
	GENERAL003("true ? 42 : 0;", Tests.N42), // Ternary
	GENERAL004("function foo(){return 42;};foo();", Tests.N42), // Function return
	GENERAL005("42;;;;;", Tests.N42), // Empty node is through-pass.
	GENERAL006(NullUtil.checkNotNull(StringUtils.repeat("if (true) 42;else 0;", 50000)), Tests.N42), // Should not take too long

	// Embedment
	EMBED01("<p>[%%=42%]</p>", ETestType.TEMPLATE, EContextType.FORMCYCLE, StringLangObject.create("<p>42</p>")),
	;

	@Nonnull private final String code;
	private final ALangObject expectedResult;
	@Nonnull private final ETestType type;
	@Nonnull private final EContextType context;
	private SemanticsSuccess(@Nonnull final String code, final ALangObject expectedResult) {
		this(code, EContextType.GENERIC, expectedResult);
	}
	private SemanticsSuccess(@Nonnull final String code, @Nonnull final EContextType context, final ALangObject expectedResult) {
		this(code, ETestType.PROGRAM, context, expectedResult);
	}
	private SemanticsSuccess(@Nonnull final String code, @Nonnull final ETestType type, @Nonnull final EContextType context, final ALangObject expectedResult) {
		this.code = code;
		this.expectedResult = expectedResult;
		this.context = context;
		this.type = type;
	}

	@Override
	public ETestType getTestType() {
		return type;
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

	@Override
	public boolean isUseStrictMode() {
		return false;
	}

}