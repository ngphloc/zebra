/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LAGPreProcessor.java 1.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.conversion.mot2wow;

import vn.spring.WOW.genparser.*;
import vn.spring.WOW.datacomponents.DotString;
import java.util.*;

/**
 * This static class has one public method called <code>process
 * </code>. This method should be called with the root program node
 * as a parameter. It will then process all the nodes in the parse
 * tree and change some program constructs in others. It will remove
 * the '+=' and '-=' assignment operators. It will change a 'while'
 * loop into an if. And it will change the use of the enough function
 * to normal && and || operations.
 */
public class LAGPreProcessor {
    //The course name used by the ID changing process
    public static String course = "";
    //A table linking LAG concept names to WOW! concept names
    public static Hashtable conceptconversion = new Hashtable();

    private static LinkedList breakstats;
    /**
     * Transforms the specified program so that it is acceptable by
     * the actual interpreter.
     */
    public static ParseNode process(ParseNode node) throws ParserException {
        breakstats = new LinkedList();
        ParseNode result = process(node, null);
        processBreakStats();
        return result;
    }

    private static void processBreakStats() {
        for (int i=0;i<breakstats.size();i++) {
            PCLAG.BreakNode breaknode = (PCLAG.BreakNode)breakstats.get(i);
            PCLAG.IfNode ifnode = new PCLAG.IfNode(breaknode.getParent());
            PCLAG.StatsNode truestats = new PCLAG.StatsNode(ifnode);
            ifnode.truestats = truestats;
            truestats.stats = new LinkedList();
            PCCommon.ExprNode newexpr = new PCCommon.ExprNode(ifnode);
            newexpr.first = breaknode.expr;
            breaknode.expr.setParent(newexpr);
            ifnode.expr = newexpr;
            newexpr.operator = "!";
            PCLAG.StatsNode statsnode = (PCLAG.StatsNode)breaknode.getParent();
            for (int j=statsnode.stats.indexOf(breaknode)+1;j<statsnode.stats.size();j++) {
                truestats.stats.add(statsnode.stats.get(j));
                ((ParseNode)statsnode.stats.get(j)).setParent(truestats);
            }
            for (int j=0;j<truestats.stats.size();j++) statsnode.stats.remove(truestats.stats.get(j));
            statsnode.stats.remove(breaknode);
            statsnode.stats.add(ifnode);
        }
    }

    /**
     * Transforms the specified program so that it is acceptable by
     * the actual interpreter.
     */
    private static ParseNode process(ParseNode node, ParseNode parent) throws ParserException {
        if (node instanceof PCLAG.IfNode) return processIF((PCLAG.IfNode)node, parent);
        else if (node instanceof PCLAG.WhileNode) return processWHILE((PCLAG.WhileNode)node, parent);
        else if (node instanceof PCLAG.StatsNode) return processSTATS((PCLAG.StatsNode)node, parent);
        else if (node instanceof PCLAG.ProgramNode) return processPROGRAM((PCLAG.ProgramNode)node, parent);
        else if (node instanceof PCLAG.AssignNode) return processASSIGN((PCLAG.AssignNode)node, parent);
        else if (node instanceof PCLAG.EnoughNode) return processENOUGH((PCLAG.EnoughNode)node, parent);
        else if (node instanceof PCLAG.BreakNode) return processBREAK((PCLAG.BreakNode)node, parent);
        else if (node instanceof PCLAG.CommandNode) return processCOMMAND((PCLAG.CommandNode)node, parent);
        else if (node instanceof PCCommon.ExprNode) return processEXPR((PCCommon.ExprNode)node, parent);
        else if (node instanceof PCCommon.ConstNode) return processDENOT((PCCommon.ConstNode)node, parent);
        else if (node instanceof PCLAGIdentifier.IDNode) return processVARP((PCLAGIdentifier.IDNode)node, parent);
        return node;
    }

    /**
     * Runs the process method for all nodes in the list. Assumes
     * that the specified list contains ParseNode objects.
     */
    private static LinkedList process(LinkedList list, ParseNode parent) throws ParserException {
        if (list == null) return null;
        LinkedList result = new LinkedList();
        for (int i=0;i<list.size();i++) result.add(process( (ParseNode)list.get(i), parent ));
        return result;
    }

    private static ParseNode processDENOT(PCCommon.ConstNode node, ParseNode parent) throws ParserException {
        PCCommon.ConstNode result = null;
        if (node == null) return result;
        result = new PCCommon.ConstNode(parent);
        result.value = node.value;
        return result;
    }

    private static ParseNode processVARP(PCLAGIdentifier.IDNode node, ParseNode parent) throws ParserException {
        PCLAGIdentifier.IDNode result = null;
        if (node == null) return result;
        result = new PCLAGIdentifier.IDNode(parent);
        result.name = changeID(node.name);
        if (result.name.indexOf(".")==-1) {
            //interpret as a constant
            PCCommon.ConstNode r2 = new PCCommon.ConstNode(parent);
            r2.value = new String(result.name);
            return r2;
        }
        return result;
    }

    private static String changeID(String id) {
        DotString did = new DotString(id.trim());
        if (did.size() > 2) {
            if (( (did.get(1).equals("GM")) || (did.get(1).equals("DM")) ) && (did.get(2).equals("Concept"))) {
                did.set(0, null);
            }
        }
        if (did.size() > 1) {
            if (( (did.get(0).equals("GM")) || (did.get(0).equals("DM")) ) && (did.get(1).equals("Concept"))) {
                did.set(0, null);
                did.set(0, "specialLAGcurrentconcept");
            }
        }
        if (did.size() > 2) {
            if ((did.get(0).equals("UM")) && (did.get(1).equals("GM"))) {
                did = new DotString(course+".variables."+did.get(2));
            }
        }
        //check if it is a specific concept
        if (did.size() > 1) {
            String cname = did.getString(0, did.size()-2);
            if (conceptconversion.containsKey(cname)) {
                did = new DotString(conceptconversion.get(cname)+"."+did.get(did.size()-1));
            }
        }
        if (did.size() > 1) {
            String attrname = did.get(did.size()-1);
            if (attrname.equals("show")) did.set(did.size()-1, "suitability"); else
            if (!Arrays.asList(new String[] {"show", "access"}).contains(attrname))
                did.set(did.size()-1, "lag_"+attrname);
        }
        return did.toString();
    }

    private static ParseNode processEXPR(PCCommon.ExprNode node, ParseNode parent) throws ParserException {
        PCCommon.ExprNode result = null;
        if (node == null) return result;
        result = new PCCommon.ExprNode(parent);
        result.first = process(node.first, result);
        result.operator = node.operator;
        result.second = process(node.second, result);
        return result;
    }

    private static ParseNode processSTATS(PCLAG.StatsNode node, ParseNode parent) throws ParserException {
        PCLAG.StatsNode result = null;
        if (node == null) return result;
        result = new PCLAG.StatsNode(parent);
        result.stats = process(node.stats, result);
        return result;
    }

    private static ParseNode processPROGRAM(PCLAG.ProgramNode node, ParseNode parent) throws ParserException {
        PCLAG.ProgramNode result = null;
        if (node == null) return result;
        result = new PCLAG.ProgramNode(parent);
        result.initialization = process(node.initialization, result);
        result.implementation = process(node.implementation, result);
        result.priority = node.priority;
        return result;
    }

    private static ParseNode processASSIGN(PCLAG.AssignNode node, ParseNode parent) throws ParserException {
//System.out.println("assignnode: "+node);
        PCLAG.AssignNode result = null;
        if (node == null) return result;
        result = new PCLAG.AssignNode(parent);
        PCLAGIdentifier.IDNode variable = (PCLAGIdentifier.IDNode)process(node.variable, result);
        ParseNode expr = process(node.expr, result);
        if ((node.operator.equals("+=")) || (node.operator.equals("-="))) {
            String operator = node.operator.substring(0,1);
            PCCommon.ExprNode newexpr = new PCCommon.ExprNode(result);
            PCLAGIdentifier.IDNode idnode = new PCLAGIdentifier.IDNode(newexpr);
            idnode.name = variable.name;
            newexpr.first = idnode;
            newexpr.operator = operator;
            newexpr.second = expr;
            expr.setParent(newexpr);
            expr = newexpr;
        }
        result.variable = variable;
        result.operator = "=";
        result.expr = expr;
        return result;
    }

    private static ParseNode processENOUGH(PCLAG.EnoughNode node, ParseNode parent) throws ParserException {
        PCCommon.ExprNode nullresult = null;
        if (node == null) return nullresult;
        LinkedList exprs = process(node.exprs, null);

        PascalVector pv = new PascalVector(exprs.size());
        pv.first(node.num);
        LinkedList resultnodes = new LinkedList();
        do {
            LinkedList state = pv.state();
            LinkedList part = new LinkedList();
            for (int i=0;i<exprs.size();i++) {
                if (((Boolean)state.get(i)).booleanValue()) part.add(exprs.get(i));
            }
            resultnodes.add(nodeFromList(0, part, "&&"));
        } while (pv.next());
        ParseNode result = nodeFromList(0, resultnodes, "||");

        if (result == null) {
            //the expression should evaluate to false
            PCCommon.ConstNode c = new PCCommon.ConstNode(parent);
            c.value = new Boolean(false);
            return c;
        }

        fixParents(result);
        result.setParent(parent);
        return result;
    }

    /**
     * Recursively changes all parent fields of <code>node</code>'s
     * children to their actual parent.
     */
    private static void fixParents(ParseNode node) {
        LinkedList children = node.getChildList();
        for (int i=0;i<children.size();i++) {
            ParseNode child = (ParseNode)children.get(i);
            fixParents(child);
            child.setParent(node);
        }
    }

    /**
     * Changes a list of expressions to a recursive expression tree
     * using the specified operator.
     */
    private static ParseNode nodeFromList(int index, LinkedList list, String operator) {
        PCCommon.ExprNode result = null;
        if (index >= list.size()) return result;
        if (index < 0) return result;
        if (index == list.size()-1) return (ParseNode)list.get(index);
        result = new PCCommon.ExprNode(null);
        result.first = (ParseNode)list.get(index);
        result.operator = operator;
        result.second = nodeFromList(index+1, list, operator);
        return result;
    }

    /**
     * A class that is used to enumerate all possible ways to pick
     * X elements out of set of Y elements. The class is initialized
     * by specifying Y and then calling <code>first</code> with X as
     * a parameter.
     */
    private static class PascalVector {
        private LinkedList current = null;
        private int max = -1;
        public PascalVector(int max) {this.max = max;}
        public LinkedList state() {return current;}
        public boolean next() {
            int i=max-1;
            while ( (i>=0) && (((Boolean)current.get(i)).booleanValue()) ) {
                current.remove(i);
                i--;
            }
            if (i==-1) {
                for (i=0;i<max;i++) current.add(new Boolean(true));
                return false;
            }
            int j=max-i-1;
            while ( (i>=0) && (!((Boolean)current.get(i)).booleanValue()) ) {
                current.remove(i);
                i--;
            }
            if (i==-1) {
                for (i=0;i<max;i++) current.add(new Boolean( i>=(max-j) ));
                return false;
            }
            current.remove(i);
            current.add(new Boolean(false));
            current.add(new Boolean(true));
            for (i=0;i<j;i++) current.add(new Boolean(true));
            while (current.size()<max) current.add(new Boolean(false));
            return true;
        }
        public void first(int nr) {
            current = new LinkedList();
            for (int i=0;i<max;i++) current.add(new Boolean(i<nr));
        }
        public String toString() {
            StringBuffer r = new StringBuffer();
            for (int i=0;i<max;i++) {
                if ( ((Boolean)current.get(i)).booleanValue() ) r.append("1"); else r.append("0");
            }
            return r.toString();
        }
    }

    private static ParseNode processIF(PCLAG.IfNode node, ParseNode parent) throws ParserException {
        PCLAG.IfNode result = null;
        if (node == null) return result;
        result = new PCLAG.IfNode(parent);
        result.expr = process(node.expr, result);
        result.truestats = process(node.truestats, result);
        result.falsestats = process(node.falsestats, result);
        return result;
    }

    private static ParseNode processBREAK(PCLAG.BreakNode node, ParseNode parent) throws ParserException {
        PCLAG.BreakNode result = null;
        if (node == null) return result;
        result = new PCLAG.BreakNode(parent);
        result.expr = process(node.expr, result);
        breakstats.add(result);
        return result;
    }

    private static ParseNode processCOMMAND(PCLAG.CommandNode node, ParseNode parent) throws ParserException {
        PCLAG.IfNode result = null;
        if (node == null) return result;
        result = new PCLAG.IfNode(parent);
        result.expr = process(node.expr, result);
        PCLAG.AssignNode assign = new PCLAG.AssignNode(result);
        result.truestats = assign;
        assign.operator = "=";
        PCLAGIdentifier.IDNode var = new PCLAGIdentifier.IDNode(assign);
        if (node.command.equals("specialize")) {
            var.name = "specialLAGcurrentconcept.group.children.children.suitability";
        } else if (node.command.equals("generalize")) {
            var.name = "specialLAGcurrentconcept.parent.Attribute.suitability";
        }
        assign.variable = var;
        PCCommon.ConstNode ctrue = new PCCommon.ConstNode(assign);
        assign.expr = ctrue;
        ctrue.value = new Boolean(true);
        return result;
    }

    private static ParseNode processWHILE(PCLAG.WhileNode node, ParseNode parent) throws ParserException {
        PCLAG.IfNode result = null;
        if (node == null) return result;
        result = new PCLAG.IfNode(parent);
        result.expr = process(node.expr, result);
        result.truestats = process(node.stats, result);
        return result;
    }
}