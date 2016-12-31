package de.xima.fc.form.expression.test.lang;

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.Cfg;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;
import de.xima.fc.form.expression.util.NullUtil;

@SuppressWarnings("nls")
@NonNullByDefault
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
	LITERALS015("(x)=>{x;}(42);", Tests.N42),
	LITERALS016("{foo:'bar'};", HashLangObject.create(
			StringLangObject.create("foo"),
			StringLangObject.create("bar")
			)),
	LITERALS017("#^\\#$#.matches('#');", Tests.TRUE),

	// Variables and scopes.
	SCOPE001("k=42;k;", Tests.N42), // local variables
	SCOPE002("k=0;if(true)k=42;k;", Tests.N42), // can access parent when nesting
	SCOPE003("k=42;()=>{var k=0;}();k;", Tests.N42), // functions with separate scope
	SCOPE004("k=42;function foo(){var k=0;};foo();k;", Tests.N42), // functions with separate scope
	SCOPE005("k=42;()=>{k;}();", Tests.N42), // accessing global variables
	SCOPE006("k=0;()=>{k=42;k;}();", Tests.N42), // shadowing global variables
	SCOPE007("foo::k=0;bar::k=42;bar::k;",Tests.N42), // scopes
	SCOPE008("foo::k=0;bar::k=42;with(foo,bar)k;",Tests.N42), // default scopes reading vars
	SCOPE009("foo::k=0;with(foo){k=42;k;}", Tests.N42), // default scopes writing to vars
	SCOPE010("foo::k=42;with(foo){k=0;foo::k;}", Tests.N42), // can still access explicitly
	SCOPE011("k=42;foo::k=0;with(foo){k;}", Tests.N42), // first global, then default scope
	SCOPE012("function foo(){0;}function foo::bar(){42;};with(foo)bar();", Tests.N42), // scoped function
	SCOPE013("scope myscope{var a=42;}with(myscope){a;}", Tests.N42), // resolving scopes

	// Template literals
	TEMPLIT001("a=20;b=22;`0${{res:`1${`2${`3${`4--${a+b}--4`}3`}2`}1`}}0`;",StringLangObject.create("0{\"res\":\"1234--42--4321\"}0")),
	TEMPLIT002("`${20+22}`;", StringLangObject.create("42")),
	TEMPLIT003("`${}`;", StringLangObject.create("")),

	//Attribute accessors
	PROPSTRING001("'Ab3'.toUpperCase.toLowerCase;", StringLangObject.create("ab3")),
	PROPSTRING002("'123'.matches(#^\\d+$#);", Tests.TRUE),
	PROPSTRING003("'a123'.matches(#^\\d+$#);", Tests.FALSE),

	PROPOBJECT001("4.2.toString;", StringLangObject.create("4.2")),
	PROPOBJECT002("'42'.toString;", StringLangObject.create("42")),
	PROPOBJECT003("true.toString;", StringLangObject.create("true")),
	PROPOBJECT004("false.toString;", StringLangObject.create("false")),
	PROPOBJECT005("null.toString;", StringLangObject.create("")),
	PROPOBJECT006("[1,2,3].toString;", StringLangObject.create("[1,2,3]")),
	PROPOBJECT007("{0:1}.toString;", StringLangObject.create("{0:1}")),
	PROPOBJECT008("exception('foo').toString;", StringLangObject.create("foo")),
	PROPOBJECT009("( x ) => { x ; }.toString;", StringLangObject.create("(x)=>{x;}")),

	PROPHASH001("h={'f':()=>{42;}};h.f();", Tests.N42), // can call methods from hashes

	CLOSURE001("function f(x){(i)=>{var a=x+i;(j)=>{a*j+x;};};}m1=f(0);n1=m1(5);m2=f(11);n3=m2(15);n2=m1(10);n4=m2(20);[n1(2),n2(2),n3(2),n4(2),n1(4),n2(4),n3(4),n4(4)];", ArrayLangObject.create(
			NumberLangObject.create(10),
			NumberLangObject.create(20),
			NumberLangObject.create(63),
			NumberLangObject.create(73),
			NumberLangObject.create(20),
			NumberLangObject.create(40),
			NumberLangObject.create(115),
			NumberLangObject.create(135))),
	CLOSURE002("f=(x)=>{(i)=>{var a=x+i;(j)=>{a*j+x;};};};m1=f(0);n1=m1(5);m2=f(11);n3=m2(15);n2=m1(10);n4=m2(20);[n1(2),n2(2),n3(2),n4(2),n1(4),n2(4),n3(4),n4(4)];", ArrayLangObject.create(
			NumberLangObject.create(10),
			NumberLangObject.create(20),
			NumberLangObject.create(63),
			NumberLangObject.create(73),
			NumberLangObject.create(20),
			NumberLangObject.create(40),
			NumberLangObject.create(115),
			NumberLangObject.create(135))),
	CLOSURE003("f=(t)=>{(s)=>{for(r=s,i=1;i<t;++i)r+=s;};};m1=f(3);m2=f(5);[['foo','bar'].map(m2),['foo','bar'].map(m1)];",
			ArrayLangObject.create(
					ArrayLangObject.create(StringLangObject.create("foofoofoofoofoo"),StringLangObject.create("barbarbarbarbar")),
					ArrayLangObject.create(StringLangObject.create("foofoofoo"),StringLangObject.create("barbarbar")))),
	CLOSURE004("()=>{arr=[];for(i in 3){arr.push(()=>{return 21*i;});}arr;}().mapNumber((m)=>{m();});", ArrayLangObject.create(
			Tests.N42,Tests.N42,Tests.N42)),

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
	EMETHODUN007("!()=>{};", BooleanLangObject.getFalseInstance()),
	EMETHODUN008("!null;", BooleanLangObject.getTrueInstance()),
	EMETHODUN009("!true;", BooleanLangObject.getFalseInstance()),
	EMETHODUN010("!false;", BooleanLangObject.getTrueInstance()),
	EMETHODUN011("a=-43;-++a;", Tests.N42),
	EMETHODUN012("a=42;a++;", Tests.N42),
	EMETHODUN013("a=41;a++;a;", Tests.N42),
	EMETHODUN014("40+ +2;", Tests.N42),
	EMETHODNUMBER001("40+2;", Tests.N42),
	EMETHODNUMBER002("50-8;", Tests.N42),
	EMETHODNUMBER003("2*21;", Tests.N42),
	EMETHODNUMBER004("168/4;", Tests.N42),
	EMETHODNUMBER005("42==42.0;", Tests.TRUE),
	EMETHODNUMBER009("48%7;;", NumberLangObject.create(6)),
	EMETHODNUMBER010("-50%7;;", NumberLangObject.create(6)),
	EMETHODNUMBER011("1==1;", Tests.TRUE),
	EMETHODNUMBER012("1===1;", Tests.TRUE),
	EMETHODNUMBER013("1<2;", Tests.TRUE),
	EMETHODNUMBER014("2<1;", Tests.FALSE),
	EMETHODNUMBER015("1<=1;", Tests.TRUE),
	EMETHODNUMBER016("1>=1;", Tests.TRUE),
	EMETHODNUMBER017("1!=1;", Tests.FALSE),
	EMETHODNUMBER018("1!==1;", Tests.FALSE),
	EMETHODNUMBER027("1<'1';", Tests.TRUE),

	EMETHODBOOLEAN001("false&&false;", Tests.FALSE),
	EMETHODBOOLEAN002("true&&false;", Tests.FALSE),
	EMETHODBOOLEAN003("false&&true;", Tests.FALSE),
	EMETHODBOOLEAN004("true&&true;", Tests.TRUE),
	EMETHODBOOLEAN005("false||false;", Tests.FALSE),
	EMETHODBOOLEAN006("true||false;", Tests.TRUE),
	EMETHODBOOLEAN007("false||true;", Tests.TRUE),
	EMETHODBOOLEAN008("true||true;", Tests.TRUE),
	EMETHODBOOLEAN009("false^false;", Tests.FALSE),
	EMETHODBOOLEAN010("true^false;", Tests.TRUE),
	EMETHODBOOLEAN011("false^true;", Tests.TRUE),
	EMETHODBOOLEAN012("true^true;", Tests.FALSE),
	EMETHODBOOLEAN013("!true;", Tests.FALSE),
	EMETHODBOOLEAN014("!false;", Tests.TRUE),

	EMETHODARRAY001("[1,2]==[1,2];", Tests.TRUE),
	EMETHODARRAY002("[1,2]===[1,2];", Tests.FALSE),
	EMETHODARRAY003("a=[1,2]; a===a;", Tests.TRUE),
	EMETHODARRAY004("[1,3]+[2,4];", ArrayLangObject.create(
			NumberLangObject.create(1),
			NumberLangObject.create(3),
			NumberLangObject.create(2),
			NumberLangObject.create(4))),
	EMETHODARRAY005("[1,3]-[2,3];", ArrayLangObject.create(
			NumberLangObject.create(1))),
	EMETHODARRAY006("[1,3]-[];", ArrayLangObject.create(
			NumberLangObject.create(1),
			NumberLangObject.create(3))),
	EMETHODARRAY007("[]-[1,3];", ArrayLangObject.create()),

	EMETHODHASH001("{0:42}+{1:42};", HashLangObject.create(
			NumberLangObject.create(0), Tests.N42,
			NumberLangObject.create(1), Tests.N42)),
	EMETHODHASH002("{0:40}+{0:42};", HashLangObject.create(NumberLangObject.create(0), Tests.N42)),

	EMETHODREGEX001("#^\\d+$# =~ '123';", Tests.TRUE),
	EMETHODREGEX002("#^\\d+$# =~ 'a123';", Tests.FALSE),

	EMETHODOBJECT001("42.0.toString;", StringLangObject.create(42)),
	EMETHODOBJECT002("!!42.0.id;", Tests.TRUE),
	EMETHODOBJECT003("a=42;b=42;(a=0)||(b=0);b;", Tests.N42),
	EMETHODOBJECT004("a=0;false||(a=42);a;", Tests.N42),
	EMETHODOBJECT005("a=0;b=0;(a=42)&&(b=42);b;", Tests.N42),
	EMETHODOBJECT006("a=0;true&&(a=42);a;", Tests.N42),
	EMETHODOBJECT007(new Cfg("global scope{boolean b;}b=0&&1;").strict().res(Tests.TRUE)),

	EMETHODSTRING001("'1'=='1';", Tests.TRUE),
	EMETHODSTRING002("'1'==='1';", Tests.FALSE),
	EMETHODSTRING003("'1'<'2';", Tests.TRUE),
	EMETHODSTRING004("'2'<'1';", Tests.FALSE),
	EMETHODSTRING005("'1'<='1';", Tests.TRUE),
	EMETHODSTRING006("'1'>='1';", Tests.TRUE),
	EMETHODSTRING007("'1'!='1';", Tests.FALSE),
	EMETHODSTRING008("'1'!=='1';", Tests.TRUE),
	EMETHODSTRING009("'foo'+'bar';", StringLangObject.create("foobar")),
	EMETHODSTRING010("'123' =~ #^\\d+$#;", Tests.TRUE),
	EMETHODSTRING011("'a123' =~ #^\\d+$#;", Tests.FALSE),

	// Document commands
	DOC001(new Cfg("<p><p>[%%doc::unwrap('p')%]</p></p>").res(StringLangObject.create("<p></p>")).template().fc()),
	DOC002(new Cfg("Hello, [%%doc::write('foobar')%]!").res(StringLangObject.create("Hello, foobar!")).template()),
	DOC003(new Cfg("Hello, [%%doc::writeln('foobar')%]!").res(StringLangObject.create("Hello, foobar\n!")).template()),

	// Variable resolution
	RESOLUTION001("i=0;if(true)i=42;i;", Tests.N42),
	RESOLUTION002("i=42;if(true)var i=0;i;", Tests.N42),
	RESOLUTION003("global scope{var baz='global';var t1='bug1';var t2='bug2';}scope foo{var baz='foo';}scope bar{var baz='bar';}if(true){var baz='bug';t1='';}function foobar(){var baz='bug3';t2='';};foobar();baz+foo::baz+bar::baz+t1+t2;", StringLangObject.create("globalfoobar")),

	// Type checking
	TYPE001("global scope {string a='42';string b = null;}a+b;", StringLangObject.create("42")),

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
	FUNCTION003("foo='BAR'.toLocaleLowerCase;foo(null);", StringLangObject.create("bar")),
	FUNCTION004("foo=()=>{42;};foo();", Tests.N42),
	FUNCTION005("function foo(arg1,arg2,...args){args;}foo(1,2);", ArrayLangObject.create()),
	FUNCTION006("function foo(arg1,arg2,...args){args;}foo(1,2,3);", ArrayLangObject.create(NumberLangObject.create(3))),
	FUNCTION007("function foo(arg1,arg2,...args){args;}foo(1,2,3,4);", ArrayLangObject.create(NumberLangObject.create(3),NumberLangObject.create(4))),
	FUNCTION008("function foo(...args){args;}foo();", ArrayLangObject.create()),
	FUNCTION009("function foo(...args){args;}foo(3);", ArrayLangObject.create(NumberLangObject.create(3))),
	FUNCTION010("function foo(...args){args;}foo(3,4);", ArrayLangObject.create(NumberLangObject.create(3),NumberLangObject.create(4))),
	FUNCTION011("function foo(arg1,arg2,...args){[arg1,arg2];}foo(1,2);", ArrayLangObject.create(NumberLangObject.create(1),NumberLangObject.create(2))),
	FUNCTION012("a=[20,22];a.length;h={bar:a.get};h.bar(0)+h.bar(1);", Tests.N42),
	FUNCTION013("(((((m)=>{(m(42));}))))((x)=>{((x));});", Tests.N42),
	FUNCTION014("(string a,string b)=>string{return a+\" \"+b;}(\"foo\",\"bar\");", StringLangObject.create("foo bar")),

	CONTROL001("if(false)41;else 42;", Tests.N42),
	CONTROL002("i=0;while(++i<42);i;", Tests.N42),
	CONTROL003("i=0;do{}while(++i<42);i;", Tests.N42),
	CONTROL004("j=10;for(i in 32)++j;j;", Tests.N42),
	CONTROL005("j=0;for(i=0;i<42;++i)++j;j;", Tests.N42),
	CONTROL006("switch(20+22){case 40+2: 42;}", Tests.N42),
	CONTROL007("i=0;switch(2){case (++i): case(++i): ++i;break;};i;", NumberLangObject.create(3)),
	CONTROL008("i=0;switch(1){case (++i): case(++i): ++i;break;};i;", NumberLangObject.create(2)),
	CONTROL009("i=0;switch(1){case (i++): case(i++): ++i;break;};i;", NumberLangObject.create(3)),
	CONTROL010("i=0;switch<lbl>(1){case (++i): break lbl; case(++i): ++i;};i;", NumberLangObject.create(1)),
	CONTROL011("(true ? 20 : 0) + (false ? 0 : 22);", Tests.N42),
	CONTROL012("while<outer>(true){for<inner>(i in 10){if(i>5) break outer;continue inner;}}", NullLangObject.getInstance()),
	CONTROL013("i=0;do{if(i>41)break;continue;}while(++i);i;", Tests.N42),
	CONTROL014("v=0;for(;;){v=42;break;};v;", Tests.N42),
	CONTROL015("v=42;for(;false;){v=0;break;};v;", Tests.N42),

	//General
	GENERAL001("a=-(b=1);for(i in 20)b=a+(a=b);", NumberLangObject.create(4181)), // Fibonacci
	GENERAL002("false ? 0 : 42;", Tests.N42), // Ternary
	GENERAL003("true ? 42 : 0;", Tests.N42), // Ternary
	GENERAL004("function foo(){return 42;};foo();", Tests.N42), // Function return
	GENERAL005("42;;;;;", Tests.N42), // Empty node is through-pass.
	GENERAL006(NullUtil.checkNotNull(StringUtils.repeat("if (true) 42;else 0;", 50000)), Tests.N42), // Should not take too long
	GENERAL007("loginfo('42');", StringLangObject.create("42")), // Log node return
	GENERAL008("a=42;switch(0){default:a;}", Tests.N42), // Resolving variables in switch default case.
	GENERAL009("global scope {boolean a;number b;string c;regex d;error e;array<var> f;hash<var,var>g;method<void>h;var i;}[a,b,c,d,e.message,f,g,h,i];", ArrayLangObject.create(
			BooleanLangObject.getFalseInstance(),
			NumberLangObject.getZeroInstance(),
			StringLangObject.getEmptyInstance(),
			RegexLangObject.getUnmatchableInstance(),
			StringLangObject.getEmptyInstance(),
			ArrayLangObject.create(),
			HashLangObject.create(),
			NullLangObject.getInstance(),
			NullLangObject.getInstance()
			)),
	GENERAL010("global scope {boolean a=true;number b=1;string c=' ';regex d=#.#;error e=exception('foo');array<var> f=[0];hash<string,string>g={foo:'bar'};method<void>h=(x)=>void{};var i = 42;}[a,b,c,d,e.message,f,g,h.length,i];", ArrayLangObject.create(
			BooleanLangObject.getTrueInstance(),
			NumberLangObject.getOneInstance(),
			StringLangObject.getSpaceInstance(),
			RegexLangObject.create(Pattern.compile(".")),
			StringLangObject.create("foo"),
			ArrayLangObject.create(NumberLangObject.getZeroInstance()),
			HashLangObject.create(StringLangObject.create("foo"), StringLangObject.create("bar")),
			NumberLangObject.getOneInstance(),
			NumberLangObject.getFourtyTwoInstance()
			)),


	// Embedment
	EMBED01(new Cfg("<p>[%%=42%]</p>").template().fc().res(StringLangObject.create("<p>42</p>"))),
	;

	private final Cfg cfg;

	private SemanticsSuccess(final String code, final ALangObject expectedResult) {
		this(new Cfg(code).res(expectedResult));
	}

	private SemanticsSuccess(final Cfg cfg) {
		this.cfg = cfg;
	}

	@Override
	public ETestType getTestType() {
		return cfg.type;
	}

	@Override
	public EContextType getContextType() {
		return cfg.context;
	}

	@Override
	public String getCode() {
		return cfg.code;
	}
	@Nullable
	@Override
	public ALangObject getExpectedResult() {
		return NullUtil.or(cfg.res, NullLangObject.getInstance());
	}
	@Nullable
	@Override
	public Class<? extends EvaluationException> getExpectedException() {
		return null;
	}
	@Override
	public boolean isPerformEvaluation() {
		return true;
	}

	@Nullable
	@Override
	public String getErrorBegin() {
		return null;
	}

	@Override
	public ISeverityConfig getSeverityConfig() {
		return cfg.config;
	}
}