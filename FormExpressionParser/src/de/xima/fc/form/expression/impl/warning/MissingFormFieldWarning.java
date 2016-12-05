package de.xima.fc.form.expression.impl.warning;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.NullUtil;

public class MissingFormFieldWarning extends GenericWarning {
	public MissingFormFieldWarning(@Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		//TODO externalize string
		super(NullUtil.format("Form field %s does not exist in the current form version.", name), ec);
	}
}