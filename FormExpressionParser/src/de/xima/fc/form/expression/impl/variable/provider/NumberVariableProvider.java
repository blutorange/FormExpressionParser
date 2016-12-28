package de.xima.fc.form.expression.impl.variable.provider;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.NumberLangObject;

@NonNullByDefault
public abstract class NumberVariableProvider extends AVariableProvider<NumberLangObject> {
	private static final long serialVersionUID = 1L;
	protected NumberVariableProvider() {
		super(SimpleVariableType.NUMBER);
	}
	@Override
	public abstract NumberLangObject make();
	public static IVariableProvider<NumberLangObject> forNumber(final int number) {
		return new NumberVariableProvider() {
			private static final long serialVersionUID = 1L;
			@Override
			public NumberLangObject make() {
				return NumberLangObject.create(number);
			}
		};
	}
	public final static class StaticNumberVariableProvider extends NumberVariableProvider {
		private static final long serialVersionUID = 1L;
		double value;
		public StaticNumberVariableProvider(final Number value) {
			this.value = value.doubleValue();
		}
		public StaticNumberVariableProvider(final double value) {
			this.value = value;
		}
		@Override
		public final NumberLangObject make() {
			return NumberLangObject.create(value);
		}
	}
}