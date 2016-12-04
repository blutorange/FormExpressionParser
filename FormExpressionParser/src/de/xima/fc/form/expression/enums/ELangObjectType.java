package de.xima.fc.form.expression.enums;

import javax.annotation.Nonnull;

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

public enum ELangObjectType {
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return true;
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
		public boolean isCompound() {
			return false;
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
		public boolean isCompound() {
			return true;
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
		public boolean isCompound() {
			return true;
		}
	},
	;
	public final int order;
	private ELangObjectType(final int order) {
		this.order = order;
	}		
	@Nonnull
	public abstract Class<? extends ALangObject> getLangObjectClass();
	/**
	 * @return The name of this type used for variable declarations, eg. <code>error myErrorVar;</code>.
	 */
	@Nonnull
	public abstract String getSyntacticalTypeName();
	/**
	 * A compound (generic) type requires additional type parameters, eg. <code>array&lt;string&gt;</code>.
	 * @return Whether this type is compound.
	 */
	public abstract boolean isCompound();
}