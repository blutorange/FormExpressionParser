package de.xima.fc.form.expression.impl.namespace;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.iface.evaluate.INamespace;
import de.xima.fc.form.expression.iface.factory.INamespaceContractFactory;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.function.EDotAccessorArray;
import de.xima.fc.form.expression.impl.function.EDotAccessorBoolean;
import de.xima.fc.form.expression.impl.function.EDotAccessorException;
import de.xima.fc.form.expression.impl.function.EDotAccessorFunction;
import de.xima.fc.form.expression.impl.function.EDotAccessorHash;
import de.xima.fc.form.expression.impl.function.EDotAccessorNumber;
import de.xima.fc.form.expression.impl.function.EDotAccessorObject;
import de.xima.fc.form.expression.impl.function.EDotAccessorRegex;
import de.xima.fc.form.expression.impl.function.EDotAccessorString;
import de.xima.fc.form.expression.impl.function.EDotAssignerArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodArray;
import de.xima.fc.form.expression.impl.function.EExpressionMethodBoolean;
import de.xima.fc.form.expression.impl.function.EExpressionMethodHash;
import de.xima.fc.form.expression.impl.function.EExpressionMethodNumber;
import de.xima.fc.form.expression.impl.function.EExpressionMethodRegex;
import de.xima.fc.form.expression.impl.function.EExpressionMethodString;
import de.xima.fc.form.expression.impl.function.GenericBracketAccessor;
import de.xima.fc.form.expression.impl.function.GenericBracketAssigner;
import de.xima.fc.form.expression.impl.function.GenericDotAccessor;
import de.xima.fc.form.expression.impl.function.GenericDotAssigner;

@ParametersAreNonnullByDefault
public enum ENamespaceContractFactory implements INamespaceContractFactory {
	EMPTY(new GenericNamespaceContractFactory.Builder()
			.build()),
	GENERIC(
			new GenericNamespaceContractFactory.Builder()
			.addExpressionMethod(EExpressionMethodBoolean.values())
			.addExpressionMethod(EExpressionMethodNumber.values())
			.addExpressionMethod(EExpressionMethodString.values())
			.addExpressionMethod(EExpressionMethodArray.values())
			.addExpressionMethod(EExpressionMethodHash.values())
			.addExpressionMethod(EExpressionMethodRegex.values())

			.addDotAccessor(EDotAccessorObject.values())
			.addDotAccessor(EDotAccessorBoolean.values())
			.addDotAccessor(EDotAccessorNumber.values())
			.addDotAccessor(EDotAccessorString.values())
			.addDotAccessor(EDotAccessorArray.values())
			.addDotAccessor(EDotAccessorHash.values())
			.addDotAccessor(EDotAccessorException.values())
			.addDotAccessor(EDotAccessorFunction.values())
			.addDotAccessor(EDotAccessorRegex.values())

			.addDotAssigner(EDotAssignerArray.values())

			.addGenericBracketAccessor(GenericBracketAccessor.ARRAY)
			.addGenericBracketAccessor(GenericBracketAccessor.HASH)
			.addGenericBracketAccessor(GenericBracketAccessor.STRING)
			.addGenericDotAccessor(GenericDotAccessor.HASH)

			.addGenericBracketAssigner(GenericBracketAssigner.ARRAY)
			.addGenericBracketAssigner(GenericBracketAssigner.HASH)
			.addGenericDotAssigner(GenericDotAssigner.HASH)

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
	public IVariableType getExpressionMethodReturnType(final IVariableType lhs, final EMethod method, final IVariableType rhs) {
		return impl.getExpressionMethodReturnType(lhs, method, rhs);
	}
	@Override
	public boolean isBracketAssignerDefined(final IVariableType thisContext, final IVariableType property, final IVariableType value) {
		return impl.isBracketAssignerDefined(thisContext, property, value);
	}
	@Nullable
	@Override
	public IVariableType getBracketAccessorReturnType(final IVariableType thisContext, final IVariableType property) {
		return impl.getBracketAccessorReturnType(thisContext, property);
	}
	@Override
	public boolean isDotAssignerDefined(final IVariableType thisContext, final String property, final IVariableType value) {
		return impl.isDotAssignerDefined(thisContext, property, value);
	}
	@Nullable
	@Override
	public IVariableType getDotAccessorReturnType(final IVariableType thisContext, final String property) {
		return impl.getDotAccessorReturnType(thisContext, property);
	}
}