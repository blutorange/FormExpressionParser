package de.xima.fc.form.expression.impl.variable.provider;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.BooleanLangObject;

@ParametersAreNonnullByDefault
public abstract class BooleanVariableProvider extends AVariableProvider<BooleanLangObject> {
	private static final long serialVersionUID = 1L;
	private BooleanVariableProvider() {
		super(SimpleVariableType.BOOLEAN);
	}
	@Override
	public abstract BooleanLangObject make();
	public static IVariableProvider<BooleanLangObject> forTrue() {
		return InstanceHolder.TRUE;
	}
	public static IVariableProvider<BooleanLangObject> forFalse() {
		return InstanceHolder.FALSE;
	}
	private static class InstanceHolder {
		public final static BooleanVariableProvider TRUE = new BooleanVariableProvider(){
			private static final long serialVersionUID = 1L;
			@Override
			public BooleanLangObject make() {
				return BooleanLangObject.getTrueInstance();
			}
		};
		public final static BooleanVariableProvider FALSE = new BooleanVariableProvider(){
			private static final long serialVersionUID = 1L;
			@Override
			public BooleanLangObject make() {
				return BooleanLangObject.getFalseInstance();
			}
		};
	}
}