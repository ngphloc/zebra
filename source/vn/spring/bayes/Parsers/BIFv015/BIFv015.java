/* Generated By:JavaCC: Do not edit this line. BIFv015.java */
/* This parser uses the data structures in the JavaBayes core *
 * engine (package BayesianNetworks); other implementations   *
 * may use different data structures                          */
package vn.spring.bayes.Parsers.BIFv015;

import vn.spring.bayes.InterchangeFormat.*;

import java.util.Vector;
import java.util.Enumeration;

/* Definition of the Interchange Format class and its         *
 * variables. The IFBayesNet ifbn contains the *
 * parsed bayesian network.                                   */
public class BIFv015 extends InterchangeFormat implements BIFv015Constants {
  IFBayesNet ifbn;

  public IFBayesNet get_ifbn() { return(ifbn); }

  /* Method responsible for globbing undefined text in an input file */
  void glob_undefined_text() throws ParseException {
        Token t;
        while (true) {
                t = getToken(1);
                if ((t.kind == 0) ||
                    (t.kind == NETWORK) ||
                    (t.kind == VARIABLE) ||
                    (t.kind == PROBABILITY))
                        break;
                else
                        getNextToken();
          }
  }

/* ========================================================== */
/* THE INTERCHANGE FORMAT GRAMMAR STARTS HERE                 */
/* ========================================================== */

/* Basic parsing function. First looks for a Network Declaration, *
 * then looks for an arbitrary number of VariableDeclaration or   *
 * ProbabilityDeclaration non-terminals. The objects are          *
 * in the vectors ifbn.pvs and ifbn.upfs.                         */
  final public void CompilationUnit() throws ParseException {
IFProbabilityVariable pv;
IFProbabilityFunction upf;
          glob_undefined_text();
    NetworkDeclaration();
          glob_undefined_text();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
      case PROBABILITY:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
        pv = VariableDeclaration();
          ifbn.add(pv); glob_undefined_text();
        break;
      case PROBABILITY:
        upf = ProbabilityDeclaration();
          ifbn.add(upf); glob_undefined_text();
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(0);
  }

/* ========================================================== */
/* Detect and initialize the network                          */
/* ========================================================== */
  final public void NetworkDeclaration() throws ParseException {
String s;
Vector properties;
    jj_consume_token(NETWORK);
    s = getString();
    properties = NetworkContent();
          ifbn = new IFBayesNet(s, properties);
  }

/* Fill the network list of properties */
  final public Vector NetworkContent() throws ParseException {
Vector properties = new Vector();
String s;
    jj_consume_token(21);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      s = Property();
          properties.addElement(s);
    }
    jj_consume_token(22);
          {if (true) return(properties);}
    throw new Error("Missing return statement in function");
  }

/* ========================================================== */
/* Detect a variable declaration                              */
/* ========================================================== */
  final public IFProbabilityVariable VariableDeclaration() throws ParseException {
String s;
IFProbabilityVariable pv;
    jj_consume_token(VARIABLE);
    s = ProbabilityVariableName();
    pv = VariableContent(s);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

/* Fill a variable list of properties */
  final public IFProbabilityVariable VariableContent(String name) throws ParseException {
String s;
String values[] = null;
Vector properties = new Vector();
IFProbabilityVariable pv = new IFProbabilityVariable();
    jj_consume_token(21);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
      case VARIABLETYPE:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        s = Property();
          properties.addElement(s);
        break;
      case VARIABLETYPE:
        values = VariableDiscrete();
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(22);
          pv.set_name(name);
          pv.set_properties(properties);
          pv.set_values(values);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

/* Fill a variable type discrete */
  final public String[] VariableDiscrete() throws ParseException {
String values[] = null;
    jj_consume_token(VARIABLETYPE);
    jj_consume_token(DISCRETE);
    jj_consume_token(23);
    jj_consume_token(NON_NEGATIVE_NUMBER);
    jj_consume_token(24);
    jj_consume_token(21);
    values = VariableValuesList();
    jj_consume_token(22);
    jj_consume_token(25);
          {if (true) return(values);}
    throw new Error("Missing return statement in function");
  }

/* Get the values of a discrete variable */
  final public String[] VariableValuesList() throws ParseException {
int i;
String value;
String values[] = null;
Vector v = new Vector();
Enumeration e;
    value = ProbabilityVariableValue();
          v.addElement(value);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_4;
      }
      value = ProbabilityVariableValue();
          v.addElement(value);
    }
          values = new String[v.size()];
          for (e=v.elements(), i=0; e.hasMoreElements(); i++)
                values[i] = (String)(e.nextElement());
          {if (true) return(values);}
    throw new Error("Missing return statement in function");
  }

/* Pick a single word as a probability variable value */
  final public String ProbabilityVariableValue() throws ParseException {
String s;
    s = getString();
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* ========================================================== */
/* Detect a probability declaration                           */
/* ========================================================== */
  final public IFProbabilityFunction ProbabilityDeclaration() throws ParseException {
String vs[];
IFProbabilityFunction upf = new IFProbabilityFunction();
    jj_consume_token(PROBABILITY);
    ProbabilityVariablesList(upf);
    ProbabilityContent(upf);
          {if (true) return(upf);}
    throw new Error("Missing return statement in function");
  }

/* Parse the list of Probability variables */
  final public void ProbabilityVariablesList(IFProbabilityFunction upf) throws ParseException {
int i;
Enumeration e;
String variable_name;
int cond = -1;
String vs[];
Vector v_list = new Vector();
    jj_consume_token(26);
    variable_name = ProbabilityVariableName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 28:
      cond = ConditionalMark(v_list);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
          v_list.addElement(variable_name);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_5;
      }
      variable_name = ProbabilityVariableName();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
        cond = ConditionalMark(v_list);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
          v_list.addElement(variable_name);
    }
    jj_consume_token(27);
          vs = new String[v_list.size()];
          for (e=v_list.elements(), i=0; e.hasMoreElements(); i++)
                vs[i] = (String)(e.nextElement());
          upf.set_variables(vs);
          if (cond == -1)
            cond = 1;
          upf.set_conditional_index(cond);
  }

/* Find the conditional mark */
  final public int ConditionalMark(Vector v) throws ParseException {
    jj_consume_token(28);
      {if (true) return(v.size());}
    throw new Error("Missing return statement in function");
  }

/* Pick a single word as a probability variable name */
  final public String ProbabilityVariableName() throws ParseException {
String s;
    s = getString();
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* Fill a Probability list of properties */
  final public void ProbabilityContent(IFProbabilityFunction upf) throws ParseException {
String s = null;
Vector properties = new Vector();
IFProbabilityEntry e = null;
Vector entries = new Vector();
Vector defs = new Vector();
Vector tabs = new Vector();
double def[] = null;
double tab[] = null;
    jj_consume_token(21);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
      case DEFAULTVALUE:
      case TABLEVALUES:
      case 26:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        s = Property();
          properties.addElement(s);
        break;
      case DEFAULTVALUE:
        def = ProbabilityDefaultEntry();
      defs.addElement(def);
        break;
      case 26:
        e = ProbabilityEntry();
          entries.addElement(e);
        break;
      case TABLEVALUES:
        tab = ProbabilityTable();
      tabs.addElement(tab);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(22);
          upf.set_properties(properties);
          upf.set_defaults(defs);
          upf.set_entries(entries);
          upf.set_tables(tabs);
  }

  final public IFProbabilityEntry ProbabilityEntry() throws ParseException {
String s[];
double d[];
    s = ProbabilityValuesList();
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return( new IFProbabilityEntry(s, d) );}
    throw new Error("Missing return statement in function");
  }

/* Parse the list of Probability values in an entry */
  final public String[] ProbabilityValuesList() throws ParseException {
int i;
Enumeration e;
String variable_name;
String vs[];
Vector v_list = new Vector();
    jj_consume_token(26);
    variable_name = ProbabilityVariableValue();
          v_list.addElement(variable_name);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_7;
      }
      variable_name = ProbabilityVariableValue();
          v_list.addElement(variable_name);
    }
    jj_consume_token(27);
          vs = new String[v_list.size()];
          for (e=v_list.elements(), i=0; e.hasMoreElements(); i++)
                vs[i] = (String)(e.nextElement());
          {if (true) return(vs);}
    throw new Error("Missing return statement in function");
  }

  final public double[] ProbabilityDefaultEntry() throws ParseException {
double d[];
    jj_consume_token(DEFAULTVALUE);
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return(d);}
    throw new Error("Missing return statement in function");
  }

  final public double[] ProbabilityTable() throws ParseException {
double d[];
    jj_consume_token(TABLEVALUES);
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return(d);}
    throw new Error("Missing return statement in function");
  }

/* ====================================================== */
/*          Some general purpose non-terminals            */
/* ====================================================== */

/* Pick a list of non-negative floating numbers */
  final public double[] FloatingPointList() throws ParseException {
int i;
Double d;
double ds[];
Vector d_list = new Vector();
Enumeration e;
    d = FloatingPointNumber();
          d_list.addElement(d);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NON_NEGATIVE_NUMBER:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_8;
      }
      d = FloatingPointNumber();
          d_list.addElement(d);
    }
          ds = new double[d_list.size()];
          for (e=d_list.elements(), i=0; e.hasMoreElements(); i++) {
                d = (Double)(e.nextElement());
                ds[i] = d.doubleValue();
          }
          {if (true) return(ds);}
    throw new Error("Missing return statement in function");
  }

/* Pick a non-negative floating number; necessary to allow *
 * ignored characters and comments to exist in the middle  *
 * of a FloatingPointList()                                */
  final public Double FloatingPointNumber() throws ParseException {
Token t;
    t = jj_consume_token(NON_NEGATIVE_NUMBER);
          {if (true) return( Double.valueOf(t.image) );}
    throw new Error("Missing return statement in function");
  }

/* Property definition */
  final public String Property() throws ParseException {
String s;
    jj_consume_token(PROPERTY);
    s = getString();
    jj_consume_token(25);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* String */
  final public String getString() throws ParseException {
Token t;
    t = jj_consume_token(STRING);
          {if (true) return( (t.image).substring(1,t.image.length()-1) );}
    throw new Error("Missing return statement in function");
  }

  public BIFv015TokenManager token_source;
  ASCII_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  final private int[] jj_la1_0 = {0x1800,0x1800,0x2000,0x6000,0x6000,0x100000,0x10000000,0x100000,0x10000000,0x4032000,0x4032000,0x100000,0x40000,};

  public BIFv015(java.io.InputStream stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new BIFv015TokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public BIFv015(BIFv015TokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(BIFv015TokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[29];
    for (int i = 0; i < 29; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 29; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
