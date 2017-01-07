/* Generated By:JavaCC: Do not edit this line. HtmlParserConstants.java */
package de.xima.fc.form.expression.grammar.html;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
@SuppressWarnings("all")
public interface HtmlParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int htmlCommentBegin = 1;
  /** RegularExpression Id. */
  int htmlConditionalCommentBegin = 2;
  /** RegularExpression Id. */
  int xmlDeclaration = 3;
  /** RegularExpression Id. */
  int cdataBegin = 4;
  /** RegularExpression Id. */
  int dtdBegin = 5;
  /** RegularExpression Id. */
  int scripletQBegin = 6;
  /** RegularExpression Id. */
  int scripletPBegin = 7;
  /** RegularExpression Id. */
  int seaWs = 8;
  /** RegularExpression Id. */
  int scriptBegin = 9;
  /** RegularExpression Id. */
  int styleBegin = 10;
  /** RegularExpression Id. */
  int tagBegin = 11;
  /** RegularExpression Id. */
  int htmlText = 12;
  /** RegularExpression Id. */
  int tagEnd = 13;
  /** RegularExpression Id. */
  int tagSlash = 14;
  /** RegularExpression Id. */
  int tagWs = 15;
  /** RegularExpression Id. */
  int tagName = 16;
  /** RegularExpression Id. */
  int tagEquals = 17;
  /** RegularExpression Id. */
  int tagNameCharStart = 18;
  /** RegularExpression Id. */
  int tagNameChar = 19;
  /** RegularExpression Id. */
  int tagDigit = 20;
  /** RegularExpression Id. */
  int attvWs = 21;
  /** RegularExpression Id. */
  int attvAttribute = 22;
  /** RegularExpression Id. */
  int attvChar = 23;
  /** RegularExpression Id. */
  int attvHexChars = 24;
  /** RegularExpression Id. */
  int attvDecChars = 25;
  /** RegularExpression Id. */
  int attvChars = 26;
  /** RegularExpression Id. */
  int attvDoubleString = 27;
  /** RegularExpression Id. */
  int attvSingleString = 28;
  /** RegularExpression Id. */
  int styleChar = 29;
  /** RegularExpression Id. */
  int styleEndShort = 30;
  /** RegularExpression Id. */
  int styleEnd = 31;
  /** RegularExpression Id. */
  int scriptChar = 32;
  /** RegularExpression Id. */
  int scriptEndShort = 33;
  /** RegularExpression Id. */
  int scriptEnd = 34;
  /** RegularExpression Id. */
  int scripletQChar = 35;
  /** RegularExpression Id. */
  int scripletQEnd = 36;
  /** RegularExpression Id. */
  int scripletPChar = 37;
  /** RegularExpression Id. */
  int scripletPEnd = 38;
  /** RegularExpression Id. */
  int dtdChar = 39;
  /** RegularExpression Id. */
  int dtdEnd = 40;
  /** RegularExpression Id. */
  int cdataChar = 41;
  /** RegularExpression Id. */
  int cdataEnd = 42;
  /** RegularExpression Id. */
  int xmlDeclarationChar = 43;
  /** RegularExpression Id. */
  int xmlDeclarationEnd = 44;
  /** RegularExpression Id. */
  int htmlCommentChar = 45;
  /** RegularExpression Id. */
  int htmlCommentEnd = 46;
  /** RegularExpression Id. */
  int htmlConditionalCommentChar = 47;
  /** RegularExpression Id. */
  int htmlConditionalCommentEnd = 48;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int TAG = 1;
  /** Lexical state. */
  int ATTVALUE = 2;
  /** Lexical state. */
  int STYLE = 3;
  /** Lexical state. */
  int SCRIPT = 4;
  /** Lexical state. */
  int SCRIPLETQ = 5;
  /** Lexical state. */
  int SCRIPLETP = 6;
  /** Lexical state. */
  int DTD = 7;
  /** Lexical state. */
  int CDATA = 8;
  /** Lexical state. */
  int XMLDECLARATION = 9;
  /** Lexical state. */
  int HTMLCOMMENT = 10;
  /** Lexical state. */
  int HTMLCONDITIONALCOMMENT = 11;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"<!--\"",
    "\"<![\"",
    "\"<?xml\"",
    "\"<![CDATA[\"",
    "\"<!\"",
    "\"<?\"",
    "\"<%\"",
    "<seaWs>",
    "<scriptBegin>",
    "<styleBegin>",
    "\"<\"",
    "<htmlText>",
    "\">\"",
    "\"/\"",
    "<tagWs>",
    "<tagName>",
    "\"=\"",
    "<tagNameCharStart>",
    "<tagNameChar>",
    "<tagDigit>",
    "<attvWs>",
    "<attvAttribute>",
    "<attvChar>",
    "<attvHexChars>",
    "<attvDecChars>",
    "<attvChars>",
    "<attvDoubleString>",
    "<attvSingleString>",
    "<styleChar>",
    "\"</>\"",
    "\"</style>\"",
    "<scriptChar>",
    "\"</>\"",
    "\"</script>\"",
    "<scripletQChar>",
    "\"?>\"",
    "<scripletPChar>",
    "\"%>\"",
    "<dtdChar>",
    "\">\"",
    "<cdataChar>",
    "\"]]>\"",
    "<xmlDeclarationChar>",
    "\">\"",
    "<htmlCommentChar>",
    "\"-->\"",
    "<htmlConditionalCommentChar>",
    "\"]>\"",
  };

}
