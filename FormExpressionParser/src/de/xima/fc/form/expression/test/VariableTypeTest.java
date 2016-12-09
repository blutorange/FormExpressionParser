package de.xima.fc.form.expression.test;

import static de.xima.fc.form.expression.enums.ELangObjectType.ARRAY;
import static de.xima.fc.form.expression.enums.ELangObjectType.FUNCTION;
import static de.xima.fc.form.expression.enums.ELangObjectType.HASH;
import static de.xima.fc.form.expression.enums.ELangObjectType.NULL;
import static de.xima.fc.form.expression.enums.ELangObjectType.STRING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import de.xima.fc.form.expression.enums.ELangObjectType;
import de.xima.fc.form.expression.exception.IllegalVariableTypeException;
import de.xima.fc.form.expression.iface.parse.IVariableType;
import de.xima.fc.form.expression.impl.variable.SimpleVariableType;
import de.xima.fc.form.expression.impl.variable.VariableTypeBuilder;

public class VariableTypeTest {
	private static class Tuple {
		@Nonnull
		public final IVariableType var;
		@Nonnull
		public final ELangObjectType obj;

		public Tuple(@Nonnull final ELangObjectType obj) {
			this.obj = obj;
			this.var = new VariableTypeBuilder().setBasicType(obj).build();
		}
		public Tuple(@Nonnull final IVariableType var, @Nonnull final ELangObjectType obj) {
			this.var = var;
			this.obj = obj;
		}
	}

	
	@Test
	public void testFuncType() {
		final IVariableType arrayType = new VariableTypeBuilder().setBasicType(ARRAY).append(STRING).build();
		final IVariableType hashType = new VariableTypeBuilder().setBasicType(HASH).append(STRING).append(STRING).build();
		final IVariableType funcSingle = new VariableTypeBuilder().setBasicType(FUNCTION).append(NULL).build();
		final IVariableType funcNull = new VariableTypeBuilder().setBasicType(FUNCTION).append(NULL).append(NULL).build();
		try {
			new VariableTypeBuilder().setBasicType(FUNCTION).build();
			fail("Function type needs exactly 1-... generics.");
		} catch (final IllegalVariableTypeException e) {}
		new VariableTypeBuilder().setBasicType(FUNCTION).append(STRING).build();
		for (final Tuple t : getSimpleTypes()) {
			final IVariableType funcType = new VariableTypeBuilder().setBasicType(FUNCTION).append(t.var).append(t.var).build();
			basicTests(new Tuple(funcType, FUNCTION));
			if (t.obj != NULL) {
				assertFalse(funcType.equalsType(funcNull));
				assertFalse(funcNull.equalsType(funcType));
			}
			assertFalse(funcType.equalsType(funcSingle));
			assertFalse(funcSingle.equalsType(funcType));
			assertFalse(funcType.equalsType(arrayType));
			assertFalse(funcType.equalsType(hashType));
			assertTrue(funcType.union(funcNull).equalsType(funcType));
			assertTrue(funcType.union(funcSingle).equalsType(SimpleVariableType.OBJECT));
			assertTrue(funcType.union(arrayType).equalsType(SimpleVariableType.OBJECT));
			assertTrue(funcType.union(hashType).equalsType(SimpleVariableType.OBJECT));
		}
	}
	
	@Test
	public void testHashType() {
		final IVariableType arrayType = new VariableTypeBuilder().setBasicType(ARRAY).append(STRING).build();
		final IVariableType funcType = new VariableTypeBuilder().setBasicType(FUNCTION).append(STRING).build();
		try {
			new VariableTypeBuilder().setBasicType(HASH).build();
			fail("Hash type needs exactly 2 generics.");
		} catch (final IllegalVariableTypeException e) {}
		try {
			new VariableTypeBuilder().setBasicType(HASH).append(STRING).build();
			fail("Hash type needs exactly 2 generics.");
		} catch (final IllegalVariableTypeException e) {}		
		try {
			new VariableTypeBuilder().setBasicType(HASH).append(STRING).append(STRING).append(STRING).build();
			fail("Hash type needs exactly 2 generics.");
		} catch (final IllegalVariableTypeException e) {}		
		for (final Tuple t : getSimpleTypes()) {
			final IVariableType hashType = new VariableTypeBuilder().setBasicType(HASH).append(t.var).append(t.var).build();
			final IVariableType hashNull = new VariableTypeBuilder().setBasicType(HASH).append(NULL).append(NULL).build();
			basicTests(new Tuple(hashType, HASH));
			if (t.obj != NULL) {
				assertFalse(funcType.equalsType(hashNull));
				assertFalse(hashNull.equalsType(funcType));
			}
			assertFalse(hashType.equalsType(arrayType));
			assertFalse(hashType.equalsType(funcType));
			assertTrue(hashType.union(hashNull).equalsType(hashType));
			assertTrue(hashType.union(arrayType).equalsType(SimpleVariableType.OBJECT));
			assertTrue(hashType.union(funcType).equalsType(SimpleVariableType.OBJECT));
		}
	}

	@Test
	public void testArrayType() {
		final IVariableType hashType = new VariableTypeBuilder().setBasicType(HASH).append(STRING).append(STRING)
				.build();
		final IVariableType funcType = new VariableTypeBuilder().setBasicType(FUNCTION).append(STRING).build();
		try {
			new VariableTypeBuilder().setBasicType(ARRAY).build();
			fail("Array type needs exactly 1 generics.");
		} catch (final IllegalVariableTypeException e) {}
		try {
			new VariableTypeBuilder().setBasicType(ARRAY).append(STRING).append(STRING).build();
			fail("Array type needs exactly 1 generics.");
		} catch (final IllegalVariableTypeException e) {}		
		for (final Tuple t : getSimpleTypes()) {
			final IVariableType arrayType = new VariableTypeBuilder().setBasicType(ARRAY).append(t.var).build();
			final IVariableType arrayNull = new VariableTypeBuilder().setBasicType(ARRAY).append(NULL).build();
			basicTests(new Tuple(arrayType, ARRAY));
			if (t.obj != NULL) {
				assertFalse(funcType.equalsType(arrayNull));
				assertFalse(arrayNull.equalsType(funcType));
			}
			assertFalse(arrayType.equalsType(hashType));
			assertFalse(arrayType.equalsType(funcType));
			assertTrue(arrayType.union(arrayNull).equalsType(arrayType));
			assertTrue(arrayType.union(hashType).equalsType(SimpleVariableType.OBJECT));
			assertTrue(arrayType.union(funcType).equalsType(SimpleVariableType.OBJECT));
		}
	}

	@Test
	public void testSimpleTypes() {
		final IVariableType arrayType = new VariableTypeBuilder().setBasicType(ARRAY).append(STRING).build();
		final IVariableType hashType = new VariableTypeBuilder().setBasicType(HASH).append(STRING).append(STRING)
				.build();
		final IVariableType funcType = new VariableTypeBuilder().setBasicType(FUNCTION).append(STRING).build();
		for (final Tuple t : getSimpleTypes()) {
			basicTests(t);
			assertFalse(t.var.equalsType(arrayType));
			assertFalse(t.var.equalsType(hashType));
			assertFalse(t.var.equalsType(funcType));
			assertTrue(t.var.getGenericCount() == 0);
			assertTrue(t.var.union(t.var).equalsType(t.var));
			assertTrue(t.var.getBasicLangType() == t.obj);
			if (t.obj != NULL) {
				assertTrue(t.var.union(arrayType).equalsType(SimpleVariableType.OBJECT));
				assertTrue(t.var.union(hashType).equalsType(SimpleVariableType.OBJECT));
				assertTrue(t.var.union(funcType).equalsType(SimpleVariableType.OBJECT));
			}
		}
	}

	private void basicTests(final Tuple t) {
		assertTrue(t.var.equalsType(t.var));
		assertTrue(t.var.union(t.var).equalsType(t.var));
		assertTrue(t.var.union(SimpleVariableType.NULL).equalsType(t.var));
		assertTrue(SimpleVariableType.NULL.union(t.var).equalsType(t.var));
	}

	@Nonnull
	private List<Tuple> getSimpleTypes() {
		final List<Tuple> list = new ArrayList<>();
		for (final ELangObjectType obj : ELangObjectType.values())
			if (obj.allowsGenericsCount(0))
				list.add(new Tuple(obj));
		return list;
	}
}