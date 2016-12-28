package de.xima.fc.form.expression.iface.factory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.parse.IVariableType;

@ParametersAreNonnullByDefault
public interface INamespaceContractFactory extends IContractFactory<INamespace> {
	@Nullable
	public IValueReturn getExpressionMethodInfo(IVariableType thisContext, EMethod method);
	@Nullable
	public IPropertyValue getBracketAssignerInfo(IVariableType thisContext);
	@Nullable
	public IPropertyReturn getBracketAccessorInfo(IVariableType thisContext);
	@Nullable
	public IValue getDotAssignerInfo(IVariableType thisContext, String property);
	@Nullable
	public IReturn getDotAccessorInfo(IVariableType thisContext, String property);

	public static interface IValueReturn extends IValue, IReturn {}
	public static interface IPropertyValue extends IProperty, IValue {}
	public static interface IPropertyReturn extends IProperty, IReturn {}
	public static interface IProperty {
		public IVariableType getProperty();
	}
	public static interface IValue {
		public IVariableType getValue();
	}
	public static interface IReturn {
		public IVariableType getReturn();
	}
	public static class Value implements IValue {
		private final IVariableType type;
		public Value(final IVariableType type) { this.type = type; }
		@Override public final IVariableType getValue() { return type; }
	}
	public static class Return implements IReturn {
		private final IVariableType type;
		public Return(final IVariableType type) { this.type = type; }
		@Override public final IVariableType getReturn() { return type; }
	}
	public final static class Property implements IProperty {
		private final IVariableType type;
		public Property(final IVariableType type) { this.type = type; }
		@Override public final IVariableType getProperty() { return type; }
	}
	public final static class ValueReturn extends Return implements IValueReturn {
		private final IVariableType typeValue;
		public ValueReturn(final IVariableType typeValue, final IVariableType typeReturn) {
			super(typeReturn);
			this.typeValue = typeValue;
		}
		@Override public final IVariableType getValue() { return typeValue; }
	}
	public final static class PropertyReturn extends Return implements IPropertyReturn {
		private final IVariableType typeProperty;
		public PropertyReturn(final IVariableType typeProperty, final IVariableType typeReturn) {
			super(typeReturn);
			this.typeProperty = typeProperty;
		}
		@Override public final IVariableType getProperty() { return typeProperty; }
	}
	public final static class PropertyValue extends Value implements IPropertyValue {
		private final IVariableType typeProperty;
		public PropertyValue(final IVariableType typeProperty, final IVariableType typeValue) {
			super(typeValue);
			this.typeProperty = typeProperty;
		}
		@Override public final IVariableType getProperty() { return typeProperty; }
	}
}