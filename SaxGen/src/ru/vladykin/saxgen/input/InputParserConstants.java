/* Generated By:JavaCC: Do not edit this line. InputParserConstants.java */
package ru.vladykin.saxgen.input;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface InputParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int COMMENT = 2;
  /** RegularExpression Id. */
  int LANGLE = 9;
  /** RegularExpression Id. */
  int RANGLE = 10;
  /** RegularExpression Id. */
  int SLASH = 11;
  /** RegularExpression Id. */
  int LCURLY = 12;
  /** RegularExpression Id. */
  int RCURLY = 13;
  /** RegularExpression Id. */
  int LPAREN = 14;
  /** RegularExpression Id. */
  int RPAREN = 15;
  /** RegularExpression Id. */
  int ALT = 16;
  /** RegularExpression Id. */
  int ASTERISK = 17;
  /** RegularExpression Id. */
  int QUESTION = 18;
  /** RegularExpression Id. */
  int EQUALS = 19;
  /** RegularExpression Id. */
  int NOT_EQUALS = 20;
  /** RegularExpression Id. */
  int SUBSTR_EQUALS = 21;
  /** RegularExpression Id. */
  int NOT_SUBSTR_EQUALS = 22;
  /** RegularExpression Id. */
  int OR = 23;
  /** RegularExpression Id. */
  int AND = 24;
  /** RegularExpression Id. */
  int STRING = 25;
  /** RegularExpression Id. */
  int ID = 26;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_COMMENT = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"/*\"",
    "\"*/\"",
    "<token of kind 3>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\r\\n\"",
    "\"<\"",
    "\">\"",
    "\"/\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "\")\"",
    "\"|\"",
    "\"*\"",
    "\"?\"",
    "\"==\"",
    "\"!=\"",
    "\"=~\"",
    "\"!~\"",
    "\"||\"",
    "\"&&\"",
    "<STRING>",
    "<ID>",
    "\"@\"",
    "\".\"",
    "\";\"",
    "\"=\"",
    "\",\"",
  };

}
