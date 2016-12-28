package de.xima.fc.form.expression.impl.variable.provider;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableProvider;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.BooleanLangObject;

@NonNullByDefault
public abstract class BooleanVariableProvider extends AVariableProvider<BooleanLangObject> {
	private static final long serialVersionUID = 1L;
	protected BooleanVariableProvider() {
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