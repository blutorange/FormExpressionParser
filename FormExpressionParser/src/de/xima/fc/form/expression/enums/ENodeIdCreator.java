//package de.xima.fc.form.expression.enums;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.reflect.FieldUtils;
//
//import de.xima.fc.form.expression.grammar.FormExpressionParserTreeConstants;
//
//public class ENodeIdCreator {
//	private ENodeIdCreator() {
//	}
//
//	/**
//	 * Utility, gets all node types and returns the source code for
//	 * {@link ENodeId}. When something changes, run this and copy the output to
//	 * {@link ENodeId}.
//	 *
//	 * @param args
//	 *            Nothing required.
//	 */
//	public static void main(final String[] args) {
//		System.out.println(String.format("package %s;", ENodeId.class.getPackage().getName()));
//		System.out.println(String.format("import %s;", FormExpressionParserTreeConstants.class.getCanonicalName()));
//		System.out.println("public enum ENodeId {");
//		System.out.println("\tJJTVOID(FormExpressionParserTreeConstants.JJTVOID),");
//
//		final List<String> list = new ArrayList<>();
//		for (final Field field : FieldUtils.getAllFields(FormExpressionParserTreeConstants.class)) {
//			final String name = field.getName();
//			if (name.matches("^JJT[A-Z]+NODE$")) {
//				try {
//					final int id = ((Integer) FieldUtils.readStaticField(field)).intValue();
//					System.out.println(String.format("\t%s(FormExpressionParserTreeConstants.%s),", name, name));
//					if (id >= list.size())
//						for (int i = list.size(); i <= id; ++i)
//							list.add("JJTVOID");
//					list.set(id, name);
//				}
//				catch (final ClassCastException e) {
//					e.printStackTrace();
//				}
//				catch (final IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		System.out.println("\t;");
//		System.out.println("\tpublic final static ENodeId[] map = new ENodeId[] {");
//		for (final String s : list) {
//			System.out.println(String.format("\t\t\t%s.%s,", ENodeId.class.getCanonicalName(), s));
//		}
//		System.out.println("\t};");
//		System.out.println("\tstatic {");
//		System.out.println("\t\tfor (int i = map.length; i --> 0;) {");
//		System.out.println(String.format("\t\t\tif (map[i].id != i) throw new ExceptionInInitializerError(\"File %s seems to be old. Run %s and copy&paste the output to this file. You may need to remvoe this static block first or ENodeIdCreator might not run.\");",ENodeId.class.getCanonicalName(), ENodeIdCreator.class.getCanonicalName()));
//		System.out.println("\t\t}");
//		System.out.println(String.format("\t\tif (map.length != ENodeId.values().length) throw new ExceptionInInitializerError(\"File %s seems to be old. Run %s and copy&paste the output to this file. You may need to remvoe this static block first or ENodeIdCreator might not run.\");",ENodeId.class.getCanonicalName(), ENodeIdCreator.class.getCanonicalName()));
//
//		System.out.println("\t}");
//
//		System.out.println("\tpublic final int id;");
//		System.out.println("\tprivate ENodeId(final int id) {");
//		System.out.println("\t\tthis.id = id;");
//		System.out.println("\t}");
//		System.out.println("}");
//	}
//}
