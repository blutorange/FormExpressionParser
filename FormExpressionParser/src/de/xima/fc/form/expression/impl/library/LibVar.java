package de.xima.fc.form.expression.impl.library;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.GenericVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;
import de.xima.fc.form.expression.object.RegexLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public final class LibVar {
	public final ALangObject value;
	public final IVariableType type;

	private LibVar(final ALangObject value, final IVariableType type) {
		this.value = value;
		this.type = type;
	}

	public LibVar(final StringLangObject value) {
		this(value, SimpleVariableType.STRING);
	}

	public LibVar(final NumberLangObject value) {
		this(value, SimpleVariableType.NUMBER);
	}

	public LibVar(final BooleanLangObject value) {
		this(value, SimpleVariableType.BOOLEAN);
	}

	public LibVar(final RegexLangObject value) {
		this(value, SimpleVariableType.REGEX);
	}

	public LibVar(final NullLangObject value) {
		this(value, SimpleVariableType.NULL);
	}

	public LibVar(final ALangObject value) {
		this(value, SimpleVariableType.OBJECT);
	}

	public LibVar(final ExceptionLangObject value) {
		this(value, SimpleVariableType.EXCEPTION);
	}

	private static LibVar asArray(final IVariableType type, final ALangObject... items) {
		return new LibVar(ArrayLangObject.create(items),
				new GenericVariableType(ELangObjectType.ARRAY, type));
	}

	public static LibVar asArray(final NumberLangObject... items) {
		return asArray(SimpleVariableType.NUMBER, items);
	}

	public static LibVar asArray(final StringLangObject... items) {
		return asArray(SimpleVariableType.STRING, items);
	}

	public static LibVar asArray(final RegexLangObject... items) {
		return asArray(SimpleVariableType.REGEX, items);
	}

	public static LibVar asArray(final BooleanLangObject... items) {
		return asArray(SimpleVariableType.BOOLEAN, items);
	}

	public static LibVar asArray(final NullLangObject... items) {
		return asArray(SimpleVariableType.NULL, items);
	}

	public static LibVar asArray(final ExceptionLangObject... items) {
		return asArray(SimpleVariableType.EXCEPTION, items);
	}

	public static LibVar asArray(final ALangObject... items) {
		return asArray(SimpleVariableType.OBJECT, items);
	}
}