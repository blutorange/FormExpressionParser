# FormExpressionParser

- Entry class is de.xima.fc.form.expression.impl.formexpression.FormExpressionFactory
- This provides access to the two static method forTemplate() and forProgram() that return a IFormExpressionFactory.
- IFormExpressionFactory#parse(String code, IEvaluationContextContractFactory factory, ISeverityConfig config)
- Code is the code you want to parse.
- For the config, use SeverityConfig#getStrictConfig() and SeverityConfig#LooseConfig()
- For the factory, use for example EWriterContractFactory or EFormcycleContractFactory
- This gives you a IFormExpression for execution.
- Its main method is ALangObject evaluate(@Nonnull final T object) throws EvaluationException
- This returns the result of running the code.

