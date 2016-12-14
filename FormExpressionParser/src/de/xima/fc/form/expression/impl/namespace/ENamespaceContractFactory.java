package de.xima.fc.form.expression.impl.namespace;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.function.EAttrAccessorArray;
import de.xima.fc.form.expression.impl.function.EAttrAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAccessorException;
import de.xima.fc.form.expression.impl.function.EAttrAccessorFunction;
import de.xima.fc.form.expression.impl.function.EAttrAccessorHash;
import de.xima.fc.form.expression.impl.function.EAttrAccessorNumber;
import de.xima.fc.form.expression.impl.function.EAttrAccessorObject;
import de.xima.fc.form.expression.impl.function.EAttrAccessorRegex;
import de.xima.fc.form.expression.impl.function.EAttrAccessorString;
import de.xima.fc.form.expression.impl.function.EAttrAssignerArray;
import de.xima.fc.form.expression.impl.function.EAttrAssignerBoolean;
import de.xima.fc.form.expression.impl.function.EAttrAssignerException;
import de.xima.fc.form.expression.impl.function.EAttrAssignerFunction;
import de.xima.fc.form.expression.impl.function.EAttrAssignerHash;
import de.xima.fc.form.expression.impl.function.EAttrAssignerNumber;
import de.xima.fc.form.expression.impl.function.EAttrAssignerRegex;
import de.xima.fc.form.expression.impl.function.EAttrAssignerString;
import de.xima.fc.form.expression.impl.function.EExpressionMethodArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodBoolean;
import de.xima.fc.form.expression.impl.function.EExpressionMethodException;
import de.xima.fc.form.expression.impl.function.EExpressionMethodFunction;
import de.xima.fc.form.expression.impl.function.EExpressionMethodHash;
import de.xima.fc.form.expression.impl.function.EExpressionMethodNumber;
import de.xima.fc.form.expression.impl.function.EExpressionMethodRegex;
import de.xima.fc.form.expression.impl.function.EExpressionMethodString;
import de.xima.fc.form.expression.impl.function.GenericAttrAccessor;
import de.xima.fc.form.expression.impl.function.GenericAttrAssigner;

@ParametersAreNonnullByDefault
public enum ENamespaceContractFactory implements INamespaceContractFactory {
	EMPTY(new GenericNamespaceContractFactory.Builder().build()),
	GENERIC(
			new GenericNamespaceContractFactory.Builder()
			.addExpressionMethod(EExpressionMethodBoolean.values())
			.addExpressionMethod(EExpressionMethodNumber.values())
			.addExpressionMethod(EExpressionMethodString.values())
			.addExpressionMethod(EExpressionMethodArray.values())
			.addExpressionMethod(EExpressionMethodHash.values())
			.addExpressionMethod(EExpressionMethodException.values())
			.addExpressionMethod(EExpressionMethodFunction.values())
			.addExpressionMethod(EExpressionMethodRegex.values())

			.addAttrAccessor(EAttrAccessorObject.values())

			.addAttrAccessor(EAttrAccessorBoolean.values())
			.addAttrAccessor(EAttrAccessorNumber.values())
			.addAttrAccessor(EAttrAccessorString.values())
			.addAttrAccessor(EAttrAccessorArray.values())
			.addAttrAccessor(EAttrAccessorHash.values())
			.addAttrAccessor(EAttrAccessorException.values())
			.addAttrAccessor(EAttrAccessorFunction.values())
			.addAttrAccessor(EAttrAccessorRegex.values())

			.addAttrAssigner(EAttrAssignerBoolean.values())
			.addAttrAssigner(EAttrAssignerNumber.values())
			.addAttrAssigner(EAttrAssignerString.values())
			.addAttrAssigner(EAttrAssignerArray.values())
			.addAttrAssigner(EAttrAssignerHash.values())
			.addAttrAssigner(EAttrAssignerException.values())
			.addAttrAssigner(EAttrAssignerFunction.values())
			.addAttrAssigner(EAttrAssignerRegex.values())

			.addGenericAttrAccessor(GenericAttrAccessor.ARRAY)
			.addGenericAttrAccessor(GenericAttrAccessor.BOOLEAN)
			.addGenericAttrAccessor(GenericAttrAccessor.EXCEPTION)
			.addGenericAttrAccessor(GenericAttrAccessor.FUNCTION)
			.addGenericAttrAccessor(GenericAttrAccessor.HASH)
			.addGenericAttrAccessor(GenericAttrAccessor.NUMBER)
			.addGenericAttrAccessor(GenericAttrAccessor.STRING)
			.addGenericAttrAccessor(GenericAttrAccessor.REGEX)

			.addGenericAttrAssigner(GenericAttrAssigner.ARRAY)
			.addGenericAttrAssigner(GenericAttrAssigner.BOOLEAN)
			.addGenericAttrAssigner(GenericAttrAssigner.EXCEPTION)
			.addGenericAttrAssigner(GenericAttrAssigner.FUNCTION)
			.addGenericAttrAssigner(GenericAttrAssigner.HASH)
			.addGenericAttrAssigner(GenericAttrAssigner.NUMBER)
			.addGenericAttrAssigner(GenericAttrAssigner.STRING)
			.addGenericAttrAssigner(GenericAttrAssigner.REGEX)

			.build()),
	;
	private final INamespaceContractFactory impl;
	private ENamespaceContractFactory(final INamespaceContractFactory impl) {
		this.impl = impl;
	}
	@Override
	public INamespace make() {
		return impl.make();
	}
	@Nullable
	@Override
	public IVariableType getReturnOfExpressionMethod(final IVariableType lhs, final EMethod method, final IVariableType rhs) {
		return impl.getReturnOfExpressionMethod(lhs, method, rhs);
	}
	@Override
	public boolean isBracketAttributeAssignerDefined(final IVariableType lhs, final IVariableType property, final IVariableType rhs) {
		return impl.isBracketAttributeAssignerDefined(lhs, property, rhs);
	}
	@Override
	public boolean isDotAttributeAssignerDefined(final IVariableType lhs, final IVariableType property, final IVariableType rhs) {
		return impl.isDotAttributeAssignerDefined(lhs, property, rhs);
	}

}