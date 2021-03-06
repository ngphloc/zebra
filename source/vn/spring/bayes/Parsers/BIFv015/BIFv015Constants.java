/* Generated By:JavaCC: Do not edit this line. BIFv015Constants.java */
/* This parser uses the data structures in the JavaBayes core *
 * engine (package BayesianNetworks); other implementations   *
 * may use different data structures                          */
package vn.spring.bayes.Parsers.BIFv015;

public interface BIFv015Constants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 7;
  int FORMAL_COMMENT = 8;
  int MULTI_LINE_COMMENT = 9;
  int NETWORK = 10;
  int VARIABLE = 11;
  int PROBABILITY = 12;
  int PROPERTY = 13;
  int VARIABLETYPE = 14;
  int DISCRETE = 15;
  int DEFAULTVALUE = 16;
  int TABLEVALUES = 17;
  int NON_NEGATIVE_NUMBER = 18;
  int EXPONENT = 19;
  int STRING = 20;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\",\"",
    "<SINGLE_LINE_COMMENT>",
    "<FORMAL_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "\"network\"",
    "\"variable\"",
    "\"probability\"",
    "\"property\"",
    "\"type\"",
    "\"discrete\"",
    "\"default\"",
    "\"table\"",
    "<NON_NEGATIVE_NUMBER>",
    "<EXPONENT>",
    "<STRING>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\"(\"",
    "\")\"",
    "\"|\"",
  };

}
