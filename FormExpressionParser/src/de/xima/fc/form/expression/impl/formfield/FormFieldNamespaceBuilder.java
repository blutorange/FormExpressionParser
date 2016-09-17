package de.xima.fc.form.expression.impl.formfield;

import java.util.Map;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.impl.GenericNamespace;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;


public class FormFieldNamespaceBuilder extends GenericNamespace.Builder {

	private final Object myForm;

	// Illustrates how to access external variables from within functions via closures.
	// IEvaluationContext ec = new FormFieldEvaluationContextBuilder(myForm)...build()
	public FormFieldNamespaceBuilder(final Object myForm) {
		this.myForm = myForm;
	}

	@Override
	protected void customGlobalMethod(final Map<String, INamedFunction<NullLangObject>> globalMethod) {
		globalMethod.put("alias", new INamedFunction<NullLangObject>() {
			@Override
			public ALangObject evaluate(final IEvaluationContext ec, final NullLangObject thisContext, final ALangObject... args)
					throws EvaluationException {
				return NumberLangObject.create(myForm.hashCode());
			}
			@Override
			public String getName() {
				return "alias";
			}
		});
	}

}
