package de.xima.fc.form.expression.test.lang;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.exception.evaluation.EvaluationException;
import de.xima.fc.form.expression.iface.config.ISeverityConfig;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.test.lang.TestUtil.Cfg;
import de.xima.fc.form.expression.test.lang.TestUtil.EContextType;
import de.xima.fc.form.expression.test.lang.TestUtil.ETestType;
import de.xima.fc.form.expression.test.lang.TestUtil.ITestCase;

@SuppressWarnings("nls")
enum SyntaxSuccess implements ITestCase {
	TEST001(""),
	TEST002(";"),
	TEST003(";;;;"),
	TEST004("2+2;;;2;;"),
	TEST005("i=bug=0;if(i==0)bug(i);"),
	TEST006("i=j=bug=fail=0;if(i==0)bug(i);else if(j==0)fail();"),
	TEST007("i=j=bug=fail=success=0;if(i==0)bug(i);else if(j==0)fail();else success();"),
	TEST008("i=bug=0;if (i==0){bug(i);}"),
	TEST009("i=j=bug=fail=0;if(i==0){bug(i);}else if(j==0){fail();fail();}"),
	TEST010("i=j=bug=fail=success=0;if(i==0){bug(i);bug();}else if(j==0){fail();fail();}else{success(j);}"),
	TEST011("a=b=c=0;if(a)a;else if(c)c;else b;"),
	TEST012("nodeList=null;for(node in nodeList)loginfo(node);"),
	TEST013("for(i=0;i!=10;++i)loginfo(i);"),
	TEST014("for(i=0,j=0;i!=10,i>=0;++i,++j){loginfo(i);logwarn(j);}"),
	TEST015("spam=0;for(;;)spam();"),
	TEST016("me=0;try{1/0;}catch(e){loginfo(me);}"),
	TEST017("getEnum=0;switch(getEnum()){}"),
	TEST018("getEnum=multiple=0;switch(getEnum()){case 1:case 2:multiple.stuff();}"),
	TEST019("getEnum=a=0;switch(getEnum()){default:a.b;}"),
	TEST020("getEnum=shout=loud=cry=0;switch(getEnum()){case 1:shout();loud();case 2:cry();case 3:;}"),
	TEST021("getEnum=shout=cry=beHappy=0;switch(getEnum()){case 1:shout();case 2:cry();default:beHappy();}"),
	TEST022("a=b=0;if(a)if(b)do;while(false);"),
	TEST023("i=0;while(''.complicatedStuff())i.stuff();"),
	TEST024("i=a=0;a()(++i);"),
	TEST025("a=0;a.b().c();"),
	TEST026("3**4**5;"),
	TEST027("foo=bar=a=0;a.b().c.d(foo,bar).e;"),
	TEST028("getFunction=foo=0;getFunction()(foo);"),
	TEST029("a=b=foo=0;(a+b)(foo);"),
	TEST030("a=b=bar=0;a[b.foo(bar)]().baz;"),
	TEST031("for(i in 10)if(i>5) break;"),
	TEST032("'012345'[0];"),
	TEST033("bar::foobar=0;with(foo,bar)foobar;"),
	TEST034("a=foobar=0;a.b.c=foobar=4+2;"),
	TEST035("myscope::myvar=0;"),
	TEST036("scope scoped{var variable;}scoped::variable.works['great']();"),
	TEST037("baz=0;foo::bar = baz = 'hello';"),
	TEST038(new Cfg("<foo></foo>").template()),
	TEST039(new Cfg("<foo>[%a=b=0;a+b;%]</foo>").template().fc()),
	TEST040(new Cfg("<foo>[%a=b=0;a+b;%]").template().fc()),
	TEST041(new Cfg("[%a=b=0;a+b;%]").template().fc()),
	TEST042(new Cfg("<foo>[%k=0;for(i in 10){%]</foo>[%k+1;%]<bar/>[%}%]").template().fc()),
	TEST043(new Cfg("<foo>[%i=0;%][%j=0;%]</foo>").template().fc()),
	TEST044(new Cfg("<foo>[%42%]</foo>").template().fc()),
	TEST045(new Cfg("<foo>[%42;%]</foo>").template().fc()),
	TEST046(new Cfg("<foo>[%{4:5}.b.c[0].d()%]</foo>").template().fc()),
	TEST047("bar=0;foo=bar;/*+&*/bar=foo;"),
	TEST048("#regex#;"),
	TEST049("#regex#i;"),
	TEST050("#regex#msii;"),
	TEST051("foo=bar=baz=0;foo?bar:baz;"), // Ternary
	TEST052("a=0;++a.b['c'];"),
	TEST053("r=0;++r;"),
	TEST054("a=b=0;a+b;//comment"),
	TEST055("a=b=0;a + /* comment \n end */ b;"),
	TEST056("global scope{}"),
	TEST057("global scope{var i; var j = 5+3;}"),
	TEST058("scope myScope{var i; var j = 5+3;}"),
	TEST059("scope myScope{function foo(){}}"),
	TEST060("require scope math;"),
	TEST061("a = 8;require scope math;"),
	TEST062("scope foo{var ko;}foo::ko=0;"),
	TEST063("scope myscope{function test(){for(var n=2;n<=10;++n){for(var r=n;r<=10;r+=n){}}}}"),
	TEST064("global scope{number n;string s;boolean b;regex r;error e;array<boolean>a1;array<array/*comment*/<boolean>>a2;hash<string,string>h;method<number,number>m;}function array<hash<string,string>>foo(){number x;}()=>string{string z;};"),
	TEST065("global scope {method<void, string> bar;}function void foo(string x){return x;};bar=foo;"),
	TEST066("global scope {method<void, var...> bar;array<var> a;hash<var,string> h;}"),
	TEST067("for(var x in 8);"),
	TEST068("a=5;b=10;`result=${a+b}`;"),
	TEST069("`${}`;"),
	TEST070("'\"`$\\'\\\\';"),
	TEST071("\"'`$\\\"\\\\\";"),
	TEST072("`\"'\\$\\`\\\\`;"),
	TEST073("function foo(){var i = 10;()=>{i;};}foo();"),
	TEST074(new Cfg("function method<number,number>f(){method<number,number>h=(number x)=>{return 21*x;};return h;}f()(2);").strict()),
	TEST075(new Cfg("function void foo(string a,number b,boolean c){}foo('',2,false);").strict()),
	TEST076("global scope{var x=0;}function foo(){v=0;}"), // There was a bug where this threw a concurrent modification exception.
	TEST077(new Cfg("function hash<string,number> bar(){return true?{null:5}:{'':null};}").strict()),
	TEST078(new Cfg("function array<array<number>>foo(number n){array<number> arr=[];arr.fill(n,n);return arr.<array<number>>map((number x)=>{return[];});}").strict()),
	TEST079("someArray=[];someArray.<string>map(()=>{});"),
	;
	@Nonnull private final String code;
	@Nonnull private final ETestType type;
	@Nonnull private final EContextType context;
	@Nonnull private final ISeverityConfig config;

	private SyntaxSuccess(@Nonnull final String code) {
		this(new Cfg(code));
	}

	private SyntaxSuccess(@Nonnull final Cfg cfg) {
		this.code = cfg.code;
		this.type = cfg.type;
		this.context = cfg.context;
		this.config = cfg.config;
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
		return null;
	}

	@Override
	public boolean isPerformEvaluation() {
		return false;
	}

	@Override
	public Class<? extends EvaluationException> getExpectedException() {
		return null;
	}
	@Override
	public String getErrorBegin() {
		return null;
	}

	@Override
	public ISeverityConfig getSeverityConfig() {
		return config;
	}
}