/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCLAG.java 1.0, December 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.conversion.mot2wow;

import vn.spring.WOW.genparser.*;

import java.util.*;

/**
 * This parse component contains the main LAG language aspects.
 */
public class PCLAG implements ParseComponent {
    /**
     * A list of recursive decent parsing methods contained in this
     * parse component.
     */
    public String[] getParseMethods() {return new String[]
        {"STAT", "STATS", "PROGRAM", "enough"};}

    public PCLAG() {}

    /**
     * Uses the specified method to parse the next tokens in the
     * list.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException {
        if ("STAT".equals(method)) return STAT(pl, info);
        if ("STATS".equals(method)) return STATS(pl, info);
        if ("PROGRAM".equals(method)) return PROGRAM(pl, info);
        if ("enough".equals(method)) return ENOUGH(pl, info);
        throw new ParserException("method '"+method+"' is not handled by PCLAG");
    }

    public static class IfNode extends ParseNode implements Cloneable {
        public ParseNode expr = null;
        public ParseNode truestats = null;
        public ParseNode falsestats = null;

        public IfNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(expr); result.add(truestats);
            if (falsestats != null) result.add(falsestats);
            return result;
        }

        public Object get(String key) {
            if (key.equals("expr")) return expr;
            if (key.equals("truestats")) return truestats;
            if (key.equals("falsestats")) return falsestats;
            return null;
        }

        public String toString() {
            if (falsestats == null)
                return "if "+expr+" then "+truestats; else
                return "if "+expr+" then "+truestats+" else "+falsestats;
        }

        public String getType() {return "if";}

        public Object clone() {
            IfNode result = new IfNode(parent);
            if (expr != null) result.expr = (ParseNode)expr.clone();
            if (truestats != null) result.truestats = (ParseNode)truestats.clone();
            if (falsestats != null) result.falsestats = (ParseNode)falsestats.clone();
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (expr != null) result += expr.hashCode();
            if (truestats != null) result += truestats.hashCode();
            if (falsestats != null) result += falsestats.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof IfNode)) return false;
            IfNode node = (IfNode)object;
            boolean result = true;
            result = result && (expr==null?node.expr==null:expr.equals(node.expr));
            result = result && (truestats==null?node.truestats==null:truestats.equals(node.truestats));
            result = result && (falsestats==null?node.falsestats==null:falsestats.equals(node.falsestats));
            return result;
        }
    }

    public static class StatsNode extends ParseNode implements Cloneable {
        public LinkedList stats = new LinkedList();

        public StatsNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {return stats;}

        public Object get(String key) {
            if (key.equals("stats")) return stats;
            return null;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("(\n");
            for (int i=0;i<stats.size();i++) {
                ParseNode node = (ParseNode)stats.get(i);
                result.append(node.toString()+"\n");
            }
            result.append(")\n");
            return result.toString();
        }

        public String getType() {return "stats";}

        public Object clone() {
            StatsNode result = new StatsNode(parent);
            for (int i=0;i<stats.size();i++) {
                result.stats.add(((ParseNode)stats.get(i)).clone());
            }
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (stats != null) result += stats.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof StatsNode)) return false;
            StatsNode node = (StatsNode)object;
            boolean result = true;
            result = result && (stats==null?node.stats==null:stats.equals(node.stats));
            return result;
        }
    }

    public static class WhileNode extends ParseNode implements Cloneable {
        public ParseNode expr = null;
        public ParseNode stats = null;

        public WhileNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(expr); result.add(stats);
            return result;
        }

        public Object get(String key) {
            if (key.equals("expr")) return expr;
            if (key.equals("stats")) return stats;
            return null;
        }

        public String toString() {return "while "+expr+" "+stats;}

        public String getType() {return "while";}

        public Object clone() {
            WhileNode result = new WhileNode(parent);
            if (expr != null) result.expr = (ParseNode)expr.clone();
            if (stats != null) result.stats = (ParseNode)stats.clone();
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (expr != null) result += expr.hashCode();
            if (stats != null) result += stats.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof WhileNode)) return false;
            WhileNode node = (WhileNode)object;
            boolean result = true;
            result = result && (expr==null?node.expr==null:expr.equals(node.expr));
            result = result && (stats==null?node.stats==null:stats.equals(node.stats));
            return result;
        }
    }

    public static class ProgramNode extends ParseNode implements Cloneable {
        public ParseNode initialization = null;
        public ParseNode implementation = null;
        public int priority = 10;

        public ProgramNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(initialization); result.add(implementation);
            return result;
        }

        public Object get(String key) {
            if (key.equals("initialization")) return initialization;
            if (key.equals("implementation")) return implementation;
            if (key.equals("priority")) return new Integer(priority);
            return null;
        }

        public String toString() {return "priority "+priority+" initialization "+initialization+"implemenation "+implementation;}

        public String getType() {return "program";}

        public Object clone() {
            ProgramNode result = new ProgramNode(parent);
            if (initialization != null) result.initialization = (ParseNode)initialization.clone();
            if (implementation != null) result.implementation = (ParseNode)implementation.clone();
            result.priority = priority;
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (initialization != null) result += initialization.hashCode();
            if (implementation != null) result += implementation.hashCode();
            result += priority;
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof ProgramNode)) return false;
            ProgramNode node = (ProgramNode)object;
            boolean result = true;
            result = result && (initialization==null?node.initialization==null:initialization.equals(node.initialization));
            result = result && (implementation==null?node.implementation==null:implementation.equals(node.implementation));
            result = result && (node.priority == priority);
            return result;
        }
    }

    public static class AssignNode extends ParseNode implements Cloneable {
        public ParseNode variable = null;
        public String operator = null;
        public ParseNode expr = null;

        public AssignNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(variable);
            result.add(expr);
            return result;
        }

        public Object get(String key) {
            if (key.equals("variable")) return variable;
            if (key.equals("operator")) return operator;
            if (key.equals("expr")) return expr;
            return null;
        }

        public String toString() {return variable+" "+operator+" "+expr;}

        public String getType() {return "assign";}

        public Object clone() {
            AssignNode result = new AssignNode(parent);
            if (variable != null) result.variable = (ParseNode)variable.clone();
            result.operator = operator;
            if (expr != null) result.expr = (ParseNode)expr.clone();
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (variable != null) result += variable.hashCode();
            if (operator != null) result += operator.hashCode();
            if (expr != null) result += expr.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof AssignNode)) return false;
            AssignNode node = (AssignNode)object;
            boolean result = true;
            result = result && (variable==null?node.variable==null:variable.equals(node.variable));
            result = result && (operator==null?node.operator==null:operator.equals(node.operator));
            result = result && (expr==null?node.expr==null:expr.equals(node.expr));
            return result;
        }
    }

    public static class EnoughNode extends ParseNode implements Cloneable {
        public LinkedList exprs = new LinkedList();
        public int num = 0;

        public EnoughNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            return exprs;
        }

        public Object get(String key) {
            if (key.equals("exprs")) return exprs;
            if (key.equals("num")) return new Integer(num);
            return null;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("enough(");
            if (exprs.size()>0) result.append(exprs.get(0));
            for (int i=1;i<exprs.size();i++) {
                result.append(" "+exprs.get(i));
            }
            result.append(", "+num+")");
            return result.toString();
        }

        public String getType() {return "enough";}

        public Object clone() {
            EnoughNode result = new EnoughNode(parent);
            for (int i=0;i<exprs.size();i++) {
                result.exprs.add(((ParseNode)exprs.get(i)).clone());
            }
            result.num = num;
            return result;
        }

        public int hashCode() {
            int result = num;
            if (exprs != null) result += exprs.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof EnoughNode)) return false;
            EnoughNode node = (EnoughNode)object;
            boolean result = true;
            result = result && (exprs==null?node.exprs==null:exprs.equals(node.exprs));
            result = result && (num==node.num);
            return result;
        }
    }

    public static class BreakNode extends ParseNode implements Cloneable {
        public ParseNode expr = null;

        public BreakNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(expr);
            return result;
        }

        public Object get(String key) {
            if (key.equals("expr")) return expr;
            return null;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("break(");
            if (expr != null) result.append(expr);
            result.append(")");
            return result.toString();
        }

        public String getType() {return "break";}

        public Object clone() {
            BreakNode result = new BreakNode(parent);
            result.expr = (ParseNode)expr.clone();
            return result;
        }

        public int hashCode() {
            if (expr != null) return (expr.hashCode()); else return 1;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof BreakNode)) return false;
            BreakNode node = (BreakNode)object;
            if (expr == null) return (node.expr == null);
            return (expr.equals(node.expr));
        }
    }

    public static class CommandNode extends ParseNode implements Cloneable {
        public ParseNode expr = null;
        public String command = null;

        public CommandNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(expr);
            return result;
        }

        public Object get(String key) {
            if (key.equals("expr")) return expr;
            if (key.equals("command")) return command;
            return null;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append(command+"(");
            if (expr != null) result.append(expr);
            result.append(")");
            return result.toString();
        }

        public String getType() {return "command";}

        public Object clone() {
            CommandNode result = new CommandNode(parent);
            result.expr = (ParseNode)expr.clone();
            result.command = command;
            return result;
        }

        public int hashCode() {
            if (expr != null) return (expr.hashCode()); else return 1;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof CommandNode)) return false;
            CommandNode node = (CommandNode)object;
            boolean result = true;
            if (expr == null) result = result && node.expr == null; else
                              result = result && expr.equals(node.expr);
            if (command == null) result = result && node.command == null; else
                                 result = result && command.equals(node.command);
            return result;
        }
    }

    private ParseNode ENOUGH(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        if (!token.getType().equals("id")) throw new ParserException("'enough' expected", token);
        if (!((String)token.get("name")).equals("enough")) throw new ParserException("'enough' expected", token);
        pl.moveNext(); token = pl.current();
        if (!token.getType().equals("lbrack")) throw new ParserException("'(' expected", token);
        pl.moveNext(); token = pl.current();
        ParseNode result = new EnoughNode(null);
        while (!token.getType().equals("comma")) {
            ParseNode expr = info.parse("EXPR", pl); expr.setParent(result);
            ((EnoughNode)result).exprs.add(expr);
            token = pl.current();
        }
        pl.moveNext();
        ParseNode num = info.parse("DENOT", pl);
        if (!(num.get("value") instanceof Float)) throw new ParserException("numerical value expected", pl.current());
        ((EnoughNode)result).num = ((Float)num.get("value")).intValue();
        token = pl.current();
        if (!token.getType().equals("rbrack")) throw new ParserException("')' expected", token);
        pl.moveNext();
        return result;
    }

    private ParseNode STAT(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        ParseNode result = null;
        if (token.getType().equals("if")) {
            result = new IfNode(null);
            pl.moveNext(); token = pl.current();
            ParseNode expr = info.parse("EXPR", pl);
            token = pl.current();
            if (!token.getType().equals("then")) throw new ParserException("'then' expected", token);
            pl.moveNext();
            ParseNode stats = info.parse("STATS", pl);
            ((IfNode)result).expr = expr; expr.setParent(result);
            ((IfNode)result).truestats = stats; stats.setParent(result);
            token = pl.current();
            if (token.getType().equals("else")) {
                pl.moveNext();
                ParseNode falsestats = info.parse("STATS", pl);
                ((IfNode)result).falsestats = falsestats; falsestats.setParent(result);
            }
            return result;
        } else if (token.getType().equals("while")) {
            result = new WhileNode(null);
            pl.moveNext();
            ParseNode expr = info.parse("EXPR", pl);
            ParseNode stats = info.parse("STATS", pl);
            ((WhileNode)result).expr = expr; expr.setParent(result);
            ((WhileNode)result).stats = stats; stats.setParent(result);
            return result;
        } else if (token.getType().equals("id")) {
            result = new AssignNode(null);
            ((AssignNode)result).variable = info.parse("VARP", pl);
            token = pl.current();
            if (token.getType().equals("assign")) ((AssignNode)result).operator = "="; else
            if (token.getType().equals("plusassign")) ((AssignNode)result).operator = "+="; else
            if (token.getType().equals("minusassign")) ((AssignNode)result).operator = "-="; else
                throw new ParserException("assignment expected", token);
            pl.moveNext();
            ParseNode expr = info.parse("EXPR", pl);
            ((AssignNode)result).expr = expr; expr.setParent(result);
            return result;
        } else if (token.getType().equals("break")) {
            result = new BreakNode(null);
            pl.moveNext(); token = pl.current();
            ((BreakNode)result).expr = info.parse("EXPR", pl);
            return result;
        } else if ((token.getType().equals("specialize")) || (token.getType().equals("generalize"))) {
            result = new CommandNode(null);
            ((CommandNode)result).command = token.getType();
            pl.moveNext(); token = pl.current();
            ((CommandNode)result).expr = info.parse("EXPR", pl);
            return result;
        }
        throw new ParserException("statement expected", pl.current());
    }

    private ParseNode STATS(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        ParseNode result = null;
        if (token.getType().equals("lbrack")) {
            result = new StatsNode(null);
            pl.moveNext(); token = pl.current();
            while (!token.getType().equals("rbrack")) {
                ParseNode stat = STAT(pl, info);
                ((StatsNode)result).stats.add(stat); stat.setParent(result);
                token = pl.current();
            }
            pl.moveNext();
            return result;
        } else {
            return STAT(pl, info);
        }
    }

    private ParseNode PROGRAM(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        ParseNode result = new ProgramNode(null);
        if (token.getType().equals("priority")) {
            pl.moveNext();
            ParseNode priority = info.parse("DENOT", pl);
            Object value = priority.get("value");
            if (!(value instanceof Float)) throw new ParserException("priority should be an integer", token);
            ((ProgramNode)result).priority = ((Float)value).intValue();
            token = pl.current();
        }
        if (!token.getType().equals("initialization")) throw new ParserException("'initialization' expected", token);
        pl.moveNext();
        ParseNode initialization = info.parse("STATS", pl);
        token = pl.current();
        if (!token.getType().equals("implementation")) throw new ParserException("'implementation' expected", token);
        pl.moveNext();
        ParseNode implementation = info.parse("STATS", pl);
        ((ProgramNode)result).initialization = initialization; initialization.setParent(result);
        ((ProgramNode)result).implementation = implementation; implementation.setParent(result);
        return result;
    }

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        GenToken result = token;
        if (token == null) return null;
        if (token.getType().equals("id")) {
            String name = (String)token.get("name");
            if (name.equals("if")) result = new GenToken("if");
            if (name.equals("else")) result = new GenToken("else");
            if (name.equals("then")) result = new GenToken("then");
            if (name.equals("while")) result = new GenToken("while");
            if (name.equals("initialization")) result = new GenToken("initialization");
            if (name.equals("implementation")) result = new GenToken("implementation");
            if (name.equals("break")) result = new GenToken("break");
            if (name.equals("specialize")) result = new GenToken("specialize");
            if (name.equals("generalize")) result = new GenToken("generalize");
            if (name.equals("priority")) result = new GenToken("priority");
        }
        return result;
    }
}
