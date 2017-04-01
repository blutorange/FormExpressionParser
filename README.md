# FormExpressionParser

- Entry class is `de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory`
- This provides access to the two static method `forTemplate()` and `forProgram()` that return a IFormExpressionFactory.
- IFormExpressionFactory#compile(String code, IEvaluationContextContract factory, ISeverityConfig config)
- Code is the code you want to parse.
- For the config, use `SeverityConfig#getStrictConfig()` and `SeverityConfig#LooseConfig()`
- For the factory, use for example `EEvaluationContextContractWriter.INSTANCE` or `EEvaluationContextContractPrintStream.INSTANCE`.
- This gives you a `IFormExpression` for executing the code.
- Its main method is `ALangObject evaluate(@Nonnull final T object)` throws EvaluationException
- This returns the result of running the code.

Here is a simple code sample to get you started:

``` java
try {
	// Code we want to evaluate.
	final String code = "If I grow a banana and buy two more, I've got [%%=1+2%] bananas.";
	FormExpressionFactory.forTemplate() 							 // A factory for parsing code.
			.compile(code, 											 // The actual code.
					EEvaluationContextContractPrintStream.INSTANCE,  // Does not define additional variables
																	 // or functions and dumps the output to
																	 // a print stream.
					SeverityConfig.getLooseConfig())                 // Do not do any type checking.
		.evaluate(System.out);										 // Evaluate the result, dumping output to
																	 // stdout.
}
// A ParseException is thrown when the code is invalid syntactically.
catch (final ParseException parseException) {
	System.out.println(String.format("Failed to parse the code: %s (%s", parseException.getMessage(),
			parseException.getBeginLine()));
}
// A ParseException is thrown when the code throws an error during runtime.
catch (final EvaluationException evalException) {
	System.out.println(String.format("Failed to evaluate the code: %s (%s", evalException.getMessage(),
			evalException.getBeginLine()));
}
```

# What is it?

```html
<html>
  <head>
    [%% colors = ["red","green","blue","orange"]%]
  </head>
  <h1>Form Expression Language Test</h1>
  <p>I hope you recognize these...</p>
  <body>
    <ul>
      [%% a=-(b=1); %]
      [%% for (i in 25) { %]
        <li style="color: [%%= colors[i%4] %];">[%%= b=a+(a=b) %]</li>
      [%% } %]
    </ul>
  </body>
</html>
```

FormExpressionParser is a simple templating language interpreted by Java. It is
meant for simple or more complex data manipulation and display by calling one of
the (not yet) many available inbuilt functions. But it can also be run
stand-alone without embedding it.

It is also supposed to be used as part of a larger project that utilizes Java
and JavaScript. One design goal was to keep the syntax similar to JavaScript to
ease the learning curve, as well as to provide backwards compatibility with
existing templating systems.

When one combines many components, each of them represents a source of error and
the system may become error-prone. Another design goal was to minimize the set
of errors this components might introduce. Variable scoping is also more local
compared to JavaScript to allow checking for used/unused variables.

Some features that are supported:

  - Anonymous functions like ES6: `(x) => {x+x;}`
  - Closures
  - String interpolation like ES6: ``1 and 2 makes ${1+2}``
  - Namespaces
  - Extending it via Java, adding new functions.
  - Common imperative constructs like for, foreach, do, while, if-else.

## Plain mode

Thus there is a `plain` JavaScript like mode without much before-runtime
static code checking, but what can be checked is: 
  - Whether variables are defined before they are used
  - Whether variables are used at all
  - Whether there are labelled `break`s or `continue`s without a matching target.

Plain mode can be used for simple inline expression dumping the content of a
variable.

## Strict

    scope closure {
      method<string, string> mapper1;
      method<string, string> mapper2;
      function method<string, string> getDupMapper(number times) {
        return (string s) => {
          string result = s;
          for (i in times - 1)
            result += s;
          return result;
        };
      }
    }
    
    closure::mapper1 = closure::getDupMapper(3);
    closure::mapper2 = closure::getDupMapper(5);
    
    {
      run1: ["foo", "bar"].map(closure::mapper1),
      run2: ["foo", "bar"].map(closure::mapper2)
    };

And then there is also a strict mode available that does a few more checks. To
do so, it adds a simple type system: `string`, `array`, `hash`, `bool`,
`number`, `method`, `regex`, `exception`. To support arrays and hashes
(and methods) properly, there is some basic support for parmetrizing these
types:

  - array<number>, array<string> etc are arrays of numbers and strings,respectively.
  - hash<string, string> is a hash mapping string to strings.
  - hash<string, array<strign>> is a hash mapping strings to array of string (eg. form post data).
  - method<number, bool> is a function that takes a boolean value as its arguments and returns a number.

There is also the "type" `var`, which simply means any object. When no types are
given, the parser assumes `var` by default.

Now we are able to do some more static code checking:

  - Whether all methods and operators are defined.
  - Whether functions return the correct type.
  - Whether functions always return a value on any possible code path. This is done quite similarly to how Java does it. The [JavaSpecs](https://docs.oracle.com/javase/specs/jls/se6/html/defAssign.html) were quite a good read.

I still need to think a bit more about `null`, however.

Currently, FormExpressionParser assumes it is embedded within plain text. When
you use with HTML, you need to take care of properly escaping the output. I
might add some feature for HTML, perhaps by making HTML-escaping the default and
requiring the use of a special function when no escaping should take place.

# Performance

I haven't done much testing yet, but you should definitely not use it for tight
loops or any computational-expensive work.

There is also a serialized form so that code does not need to be parsed again
and again. Some preliminary testing I did indicated that parsing may actually
be faster under certain circumstances. More testing is needed of course, but
parsing uses [JavaCC](https://javacc.org/) and should should not be the
bottleneck.

There are also some unit tests that did help me greatly in finding bugs whenever
I modified something or added a feature. I still need to write more tests for
all language features and circumstances, though.

# Demo

A simple code editor using CodeMirror is available as a JSF project in
`FormExpressionWebDemo`. The editor displays warnings and errors by calling
Java code via servlets. It also includes some code samples.

![A screenshot of the sample code editor JSF app](https://raw.githubusercontent.com/blutorange/FormExpressionParser/master/FormExpressionWebDemo/WebDemo.png)

Run it via Apache Tomcat or some other server.
