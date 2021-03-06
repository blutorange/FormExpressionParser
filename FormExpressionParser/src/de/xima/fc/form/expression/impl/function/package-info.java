/**
 * <p>
 *
 * </p>
 * <p>
 * This package contains all methods for different {@link de.xima.fc.form.expression.object.ALangObject}.
 * <ul>
 *   <li>ExpressionMethod: Inline-expressions like a+b, a*b, a<<3 etc. The <code>thisContext</code> is the left-hand-side of the expression, the first argument the right-hand side of the expression. For unary methods, eg. <code>++i</code>, there is only the <code>thisContext</code></li>
 *   <li>DotAsssigner: Methods for assigning values to properties of various language objects via dot notation, for example <code>array.length = 5</code> or <code>hash.key = value;</code>. The <code>thisContext</code> is the left-hand side. The first string argument is the name of the property assigned to, eg. <code>"key"</code> for <code>hash.key=9;</code> or <code>false</code> for <code>hash[false] = 2</code>. The second argument is the language object which is to be assigned.</li>
 *   <li>DotAccessor: Methods for retrieving properties of various language objects via dot notation, for example <code>hash.key</code>. Arguments are similar to <code>AttrAssigner</code>s. The <code>thisContext</code> is the object whose properties are accessed. The first string argument is the name of the property assigned to, eg. <code>key</code> for <code>hash.key;</code>.</li>
 *   <li>GenericBracketAsssigner: Methods for assigning values to properties of various language objects via bracket notation, for example <code>array[2] = 5</code> or <code>hash["key"] = value;</code>. The <code>thisContext</code> is the left-hand side. The first object argument is the name of the property assigned to, eg. <code>"key"</code> for <code>hash["key"] = 9</code> or <code>false</code> for <code>hash[false] = 2</code>. The third argument is the language object which is to be assigned.</li>
 *   <li>GenericBracketAccessor: Methods for retrieving properties of various language objects via bracket notation, for example <code>hash["key"]</code>. Arguments are similar to <code>BracketAttrAssigner</code>s. The <code>thisContext</code> is the object whose properties are accessed. The first object argument is the name of the property assigned to, eg. <code>key</code> for <code>hash["key"]</code>.</li>
 * </ul>
 * </p>
 * <p>
 * For each type of language object, there is an {@link java.lang.Enum} for its
 * <code>ExpressionMethod</code>s, <code>AttrAssigner</code>s and <code>AttrAccessor</code>s.
 * Some <code>AttrAccessor</code>s return a function object that can be called with
 * more parameters. For example, {@link de.xima.fc.form.expression.impl.function.EDotAccessorArray}
 * contains the property <code>push</code>, which returns a function that takes any number
 * of arguments. When it is called, eg. <code>array.push(1,2,3)</code>, these arguments
 * are added to the end of the array.
 * </p>
 */
package de.xima.fc.form.expression.impl.function;