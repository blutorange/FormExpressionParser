package de.xima.fc.form.expression.impl.warning;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class MissingFormFieldWarning extends GenericWarning {
	public MissingFormFieldWarning(@Nonnull final String name, @Nonnull final IEvaluationContext ec) {
		super(NullUtil.messageFormat(CmnCnst.Warning.MISSING_FORM_FIELD, name), ec);
	}
}