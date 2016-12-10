package de.xima.fc.form.expression.enums;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.HashLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@ParametersAreNonnullByDefault
public enum ELangObjectType {
	OBJECT(-1) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.OBJECT;
		}
		@Override
		public Class<ALangObject> getLangObjectClass() {
			return ALangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
	},
	NULL(0) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.VAR;
		}
		@Override
		public Class<NullLangObject> getLangObjectClass() {
			return NullLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new UnsupportedOperationException();
		}
	},
	BOOLEAN(1) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.BOOLEAN;
		}
		@Override
		public Class<BooleanLangObject> getLangObjectClass() {
			return BooleanLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new UnsupportedOperationException();
		}
	},
	NUMBER(2) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.NUMBER;
		}
		@Override
		public Class<NumberLangObject> getLangObjectClass() {
			return NumberLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return true;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates from 0 to n-1.
			return SimpleVariableType.NUMBER;
		}
	},
	STRING(3) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.STRING;
		}
		@Override
		public Class<StringLangObject> getLangObjectClass() {
			return StringLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return true;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the string's characters.
			return SimpleVariableType.STRING;
		}
	},
	REGEX(4) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.REGEX;
		}
		@Override
		public Class<RegexLangObject> getLangObjectClass() {
			return RegexLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new UnsupportedOperationException();
		}
	},
	FUNCTION(5) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.METHOD;
		}
		@Override
		public Class<FunctionLangObject> getLangObjectClass() {
			return FunctionLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i > 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new UnsupportedOperationException();
		}
	},
	EXCEPTION(6) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.ERROR;
		}
		@Override
		public Class<ExceptionLangObject> getLangObjectClass() {
			return ExceptionLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 0;
		}
		@Override
		public boolean isIterable() {
			return false;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			throw new UnsupportedOperationException();
		}
	},
	ARRAY(7) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.ARRAY;
		}
		@Override
		public Class<ArrayLangObject> getLangObjectClass() {
			return ArrayLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 1;
		}
		@Override
		public boolean isIterable() {
			return true;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the array items.
			return NullUtil.checkNotNull(generics[0]);
		}
	},
	HASH(8) {
		@Override
		public String getSyntacticalTypeName() {
			return CmnCnst.Syntax.HASH;
		}
		@Override
		public Class<HashLangObject> getLangObjectClass() {
			return HashLangObject.class;
		}
		@Override
		public boolean allowsGenericsCount(final int i) {
			return i == 2;
		}
		@Override
		public boolean isIterable() {
			return true;
		}
		@Override
		public IVariableType getIterableItemType(final IVariableType[] generics) {
			// Iterates over the keys.
			return NullUtil.checkNotNull(generics[0]);
		}
	},
	;
	public final int order;
	private ELangObjectType(final int order) {
		this.order = order;
	}
	public abstract Class<? extends ALangObject> getLangObjectClass();
	/**
	 * @return The name of this type used for variable declarations, eg. <code>error myErrorVar;</code>.
	 */
	public abstract String getSyntacticalTypeName();
	/**
	 * A compound (generic) type requires additional type parameters, eg. <code>array&lt;string&gt;</code>.
	 * @return Whether this type is compound.
	 */
	public abstract boolean allowsGenericsCount(int i);

	public abstract boolean isIterable();

	/**
	 * @param generics Types for the generics.
	 * @return The type of the iteration item for the given generics.
	 * @throws UnsupportedOperationException Iff {@link #isIterable()} returns <code>false</code>.
	 * @throws ArrayIndexOutOfBoundsException Iff <code>allowsGenericsCount(generics.length)</code> return <code>false</code>.
	 */
	public abstract IVariableType getIterableItemType(IVariableType[] generics)
			throws UnsupportedOperationException, ArrayIndexOutOfBoundsException;
}