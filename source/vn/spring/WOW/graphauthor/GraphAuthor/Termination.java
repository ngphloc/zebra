/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Termination 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;
import vn.spring.WOW.graphauthor.author.*;

import vn.spring.WOW.datacomponents.DotString;

import com.jgraph.graph.*;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.Collection;

import javax.swing.tree.*;

/**
 * The main class used for termination detection. You can set the
 * debug variable to false if you want to disable receiving debug
 * information in the console.
 */
public class Termination {
    private TermModel curmodel = null;           //the current internal graph
    private Hashtable typeList = null;           //the list of all known TermType's
    private Hashtable typeMappings = null;       //maps crt's to internal TermType's
    private boolean checking = true;             //should the model be updated if changes are received?
    private boolean cycle = false;               //the current state of the algorithm
    private static final boolean debug = false;  //should debug information be displayed?
    private Vector cycleListeners = new Vector();//the collection of cycle listeners.

    public Termination() {
        new TypeListBuilder();
        curmodel = buildModel();
    }

    /**
     * Adds a TermCycleListener class that is notified when the state
     * of the algorithm changes.
     */
    public void addCycleListener(TermCycleListener listener) {
        cycleListeners.add(listener);
    }

    public void removeCycleListener(TermCycleListener listener) {
        cycleListeners.remove(listener);
    }

    private void fireStateChanged(boolean state) {
        for (int i=0;i<cycleListeners.size();i++) ((TermCycleListener)cycleListeners.get(i)).cycleStateChanged(state);
    }

    public void modelChanged() {
        if (!checking) return;

        TermModel model = buildModel();

        // stupid implementation
        curmodel = model;
        recalcAdvanced(curmodel);
    }

    /**
     * Returns the current state of the algorithm.
     */
    public boolean containsCycle() {return cycle;}

    /**
     * Turns updating the model when it changes on.
     */
    public void check() {checking = true;}

    /**
     * Turns updating the model when it changes off.
     */
    public void nocheck() {checking = false;}

    private void recalcAdvanced(TermModel model) {
        new AdvancedCalculator(model);
    }

    // This is the old simple algorithm. It is not used any more.
    void recalcModel(TermModel model) {
/**/if (debug) System.out.println("  *** recalculating model ***");
        Vector starters = new Vector();
        Vector left = new Vector();
        Vector current = new Vector();
        Vector total = new Vector();
        Vector newcurrent;
        TermEdge edge;
        TermEdge toEdge;
        int i; int j;
        Vector loop;
        boolean ncycle = false;

        for (i=0;i<model.edges.size();i++) {
            edge = (TermEdge)model.edges.get(i);
            edge.reachable = false;
            left.add(edge);
            if (edge.type.starter) starters.add(edge);
        }

        while (starters.size() > 0) {
/**/if (debug) System.out.println("starter: "+starters.get(0));
            current.clear();
            current.add(starters.get(0));
            ((TermEdge)current.get(0)).reachable = true;
            starters.remove(0);
            left.remove(current.get(0));
            total.clear();

            while (current.size() > 0) {
                total.addAll(current);
/**/if (debug) System.out.println("current set: "+current);
                newcurrent = new Vector();
                for (i=0;i<current.size();i++) {
                    edge = (TermEdge)current.get(i);
                    loop = edge.target.edgesOut;
                    for (j=0;j<loop.size();j++) {
                        toEdge = (TermEdge)loop.get(j);
                        // check if the 'edge' can trigger the 'toEdge'
                        if (edge.type.triggerlist.contains(toEdge.type.name)) {
                            if (total.contains(toEdge)) ncycle=true;
                            if (left.contains(toEdge)) {
                                starters.remove(toEdge);
                                left.remove(toEdge);
                                newcurrent.add(toEdge);
                                toEdge.reachable = true;
                            }
                        }
                    }
                }
                current = newcurrent;
            }
        }

        if (ncycle != cycle) {
            cycle = ncycle;
            fireStateChanged(cycle);
        } else cycle = ncycle;
/**/if (debug) System.out.println("recalculated: "+cycle);
    }

    /**
     * Rebuilds the current internal graph based on the graph based
     * on the current graph in the graphauthor.
     */
    private TermModel buildModel() {
/**/if (debug) System.out.println("");
/**/if (debug) System.out.println("  *** constructing model ***");
        TermModel result = new TermModel();
        addVertices(result);
        addGraphModel(result);
        addTreeModel(result);
        return result;
    }

    /**
     * Adds the vertices to the internal graph.
     */
    private void addVertices(TermModel model) {
        LinkedList list = GraphAuthor.conceptList;
        if (list == null) return;

        WOWOutConcept item;
        TermVertex vertex;
        for (Iterator iterator = list.iterator();iterator.hasNext();) {
            item = (WOWOutConcept)iterator.next();
            vertex = new TermVertex(item.name);
/**/if (debug) System.out.println("adding vertex: "+item.name);
            model.vertices.put(item.name, vertex);
            addTemplateCRTs(item, model);
        }
    }

    /**
     * Adds edges to the graph that are defined in page templates.
     */
    private void addTemplateCRTs(WOWOutConcept item, TermModel model) {
        ConceptTemplate ctemplate;
        String key;
        Enumeration e;
        for (Iterator iterator = AuthorSTATIC.templateList.iterator();iterator.hasNext();) {
            ctemplate = (ConceptTemplate)iterator.next();
            if (ctemplate.name.equals(item.template)) {
                for (e = ctemplate.conceptRelations.keys();e.hasMoreElements();) {
                    key = (String)e.nextElement();
                    addEdges(key, item.name, item.name, model);
                }
            }
        }
    }

    /**
     * Adds unary and binary edges found in the graph author to the
     * internal graph.
     */
    private void addGraphModel(TermModel model) {
        DefaultGraphModel gmodel = (DefaultGraphModel)GraphAuthor.graph.getModel();
        Object[] roots = GraphAuthor.graph.getRoots();
        DefaultEdge edge;
        DefaultGraphCell cell;
        String source = null; String target = null;
        String key;
        Hashtable unary;

        for (int i=0;i<roots.length;i++) {
            if (roots[i] instanceof DefaultEdge) {
                edge = (DefaultEdge)roots[i];
                boolean goodedge = true;
                try {
                    source = gmodel.getParent(gmodel.getSource(edge)).toString();
                    target = gmodel.getParent(gmodel.getTarget(edge)).toString();
                } catch (Exception e) {
                    goodedge = false;
                }
                if (goodedge) addEdges( (String)edge.getAttributes().get("crt") , source, target, model);
            }
            if (roots[i].getClass().getName().equals("com.jgraph.graph.DefaultGraphCell")) {
                cell = (DefaultGraphCell)roots[i];
/**/if (debug) System.out.println("found cell: "+cell);
                unary = (Hashtable)cell.getAttributes().get("unaryRelations");
                if (unary != null) {
                    for (Enumeration e = unary.keys();e.hasMoreElements();) {
                        key = (String)e.nextElement();
                        addEdges(key, cell.toString(), cell.toString(), model);
                    }
                }
            }
        }
    }

    /**
     * Adds edges implied by the tree in the graph author
     */
    private void addTreeModel(TermModel model) {
        TreeModel tmodel = GraphAuthor.sharedConceptTree.getModel();
        String treetype = AuthorSTATIC.trel.name;
        DefaultMutableTreeNode element = (DefaultMutableTreeNode)tmodel.getRoot();
        addTreeRelations(element, treetype, model);
    }

    private void addTreeRelations(DefaultMutableTreeNode parent, String type, TermModel model) {
        DefaultMutableTreeNode tnode;
        for (Enumeration e = parent.children();e.hasMoreElements();) {
            tnode = (DefaultMutableTreeNode)e.nextElement();
            addEdges(type, tnode.toString(), parent.toString(), model);
            addTreeRelations(tnode, type, model);
        }
    }

    /**
     * Uses the known mappings to convert a concept relation in the
     * graph author to possibly several edges in the internal graph.
     */
    private void addEdges(String type, String source, String target, TermModel model) {
/**/if (debug) System.out.println("adding edges: "+type+", "+source+", "+target);
        if (!typeMappings.containsKey(type)) return;
        TermMapping mapping;
        TermEdge edge;
        TermVertex sourcev;
        TermVertex targetv;
        Vector mappings = (Vector)typeMappings.get(type);
        for (int i=0;i<mappings.size();i++) {
            mapping = (TermMapping)mappings.get(i);
            sourcev = (TermVertex)(mapping.source==0 ? model.vertices.get(source) : model.vertices.get(target));
            targetv = (TermVertex)(mapping.target==0 ? model.vertices.get(source) : model.vertices.get(target));
            edge = new TermEdge(sourcev, targetv, (TermType)typeList.get(mapping.name));
            model.addEdge(edge);
        }
    }

    /**
     * This class is used to read the CRT's and build triggerlist's
     * and activatorlist's.
     */
    private class TypeListBuilder {
        private int mappingCount;
        private Hashtable attrTrigger = new Hashtable();
        private Hashtable attrAction = new Hashtable();
        private Hashtable attrActionToTypes = new Hashtable();

        public TypeListBuilder() {
            typeList = new Hashtable();
            typeMappings = new Hashtable();

            CRTConceptRelationType crt;
            for (Iterator iterator = AuthorSTATIC.CRTList.iterator();iterator.hasNext();) {
                crt = (CRTConceptRelationType)iterator.next();
                processCRT(crt);
            }

            processAttrMaps();
            processTypes();
        }

        private void processCRT(CRTConceptRelationType crt) {
/**/if (debug) System.out.println("processing crt: "+crt.name);
            mappingCount = 1;

            LinkedList list = crt.listItem.generateListItemList;
            if (list == null) return;

            CRTGenerateListItem item;
            for (Iterator iterator = list.iterator();iterator.hasNext();) {
                item = (CRTGenerateListItem)iterator.next();
                processGLItem(item, crt);
            }
        }

        private void processGLItem(CRTGenerateListItem item, CRTConceptRelationType crt) {
            CRTAction action;
            LinkedList list = item.trueActions.actionList;
            if (list != null) {
                for (Iterator iterator = list.iterator();iterator.hasNext();) {
                    action = (CRTAction)iterator.next();
                    processAction(action, item, crt);
                }
            }
            list = item.falseActions.actionList;
            if (list != null) {
                for (Iterator iterator = list.iterator();iterator.hasNext();) {
                    action = (CRTAction)iterator.next();
                    processAction(action, item, crt);
                }
            }
        }

        private void processAction(CRTAction action, CRTGenerateListItem item, CRTConceptRelationType crt) {
            // determine the location concept and attribute
            if ((item.location == null) || (item.location.equals(""))) return;
            DotString location = new DotString(item.location);
            String attr = location.get(location.size()-1);
            location.set(location.size()-1, null);

            // create the mapping
            TermMapping mapping = new TermMapping(crt.name+mappingCount);
            mapping.source =
                ( (location.toString().equals("___source")||location.toString().equals("___child")) ? 0 :
                ( (location.toString().equals("___destination")||location.toString().equals("___parent")) ? 1 : -1) );
            mapping.target =
                ( (action.conceptName.equals("___source")||action.conceptName.equals("___child")) ? 0 :
                ( (action.conceptName.equals("___destination")||action.conceptName.equals("___parent")) ? 1 : -1) );
            if ((mapping.source == -1) || (mapping.target == -1)) return;
            mapping.req = item.requirement.trim();
            mapping.actionattr = action.conceptName.trim()+"."+action.attributeName.trim();
            mapping.actionexpr = action.expression.trim();
            addOneEntry(typeMappings, crt.name, mapping);
/**/if (debug) System.out.println("adding mapping: "+crt.name+", "+mapping.name);

            // update the attribute tables
            addOneEntry(attrActionToTypes, action.attributeName.trim(), mapping.name);
            addOneEntry(attrTrigger, attr, mapping.name);
            attrAction.put(mapping.name, action.attributeName.trim());
            mappingCount++;
        }

        private void addOneEntry(Hashtable table, Object entry, Object o) {
            Vector vector;
            if (table.containsKey(entry)) {
                vector = (Vector)table.get(entry);
            } else {
                vector = new Vector();
                table.put(entry, vector);
            }
            vector.add(o);
        }

        private void processAttrMaps() {
            String key;
            Vector mappings;
            int i;
            TermMapping mapping;
            TermType type;
            Vector access = new Vector();

            if (attrTrigger.containsKey("access")) access = (Vector)attrTrigger.get("access");

            for (Enumeration e = typeMappings.keys();e.hasMoreElements();) {
                key = (String)e.nextElement();
                mappings = (Vector)typeMappings.get(key);
                for (i=0;i<mappings.size();i++) {
                    mapping = (TermMapping)mappings.get(i);
                    type = new TermType(mapping.name);
                    type.triggerlist = (Vector)attrTrigger.get(attrAction.get(mapping.name));
                    if (type.triggerlist == null) type.triggerlist = new Vector();
                    if (access.contains(mapping.name)) type.starter = true;
                    type.mapping = mapping;
                    typeList.put(mapping.name, type);
/**/if (debug) System.out.println("adding type: "+mapping.name+", "+type.starter+", "+type.triggerlist);
                }
            }
        }

        private void processTypes() {
            String key;
            TermType type;
            String s;

            for (Enumeration e = typeList.keys();e.hasMoreElements();) {
                key = (String)e.nextElement();
                type = (TermType)typeList.get(key);
                if (type.mapping.req.equals("true")) type.truereq = true;
                if (isSimpleReq(type.mapping.req)) {
                    s = type.mapping.req;
                    if ((s.startsWith("___source")) || (s.startsWith("___child")))
                        type.sourcehalt = true; else type.targethalt = true;
                    addActivators(type);
                }
            }
        }

        private boolean isSimpleReq(String req) {
            String s;
            StringTokenizer st = new StringTokenizer(req, " .\t\n\r\f");
            if (!st.hasMoreTokens()) return false; s = st.nextToken();
            if ((!s.equals("___child")) && (!s.equals("___source")) && (!s.equals("___destination")) && (!s.equals("___parent"))) return false;
            if (!st.hasMoreTokens()) return false; s = st.nextToken();
            if (!attrAction.contains(s)) return false;
            if (!st.hasMoreTokens()) return false; s = st.nextToken();
            if ((!s.equals("<")) && (!s.equals("==")) && (!s.equals(">"))) return false;
            if (!st.hasMoreTokens()) return false; s = st.nextToken();
            if (st.hasMoreTokens()) return false;
            boolean r = true;
            if ((! Character.isDigit(s.charAt(0)) ) && (! (s.charAt(0) == '-') )) r = false;
            for (int i=1;i<s.length();i++) {r = r || Character.isDigit(s.charAt(i));}
            return r;
        }

        private void addActivators(TermType type) {
/**/if (debug) System.out.println("adding activators to type: "+type.name+" ("+(type.targethalt ? "___target" : "___source")+")");
            StringBuffer sb;
            int haType;
            TermType activator;
            TermHaltActivator HA;
            StringTokenizer st = new StringTokenizer(type.mapping.req, " .\t\n\r\f");
            st.nextToken(); String attr = st.nextToken();
            Vector activators = (Vector)attrActionToTypes.get(attr);
            for (int i=0;i<activators.size();i++) {
                activator = (TermType)typeList.get((String)activators.get(i));
                sb = new StringBuffer(activator.mapping.actionexpr);
                if (isSimpleExpr(sb, activator.mapping.actionattr)) {
                    haType = getHAType(type.mapping.req, sb.toString());
                    if (haType != 0) {
                        HA = new TermHaltActivator(activator);
/**/if (debug) System.out.println("adding activator: "+activator.name+" HA: "+haType);
                        HA.haType = haType;
                        type.haltActivators.add(HA);
                    }
                } else {
                    HA = new TermHaltActivator(activator);
                    HA.haType = 3;
                    type.haltActivators.add(HA);
                }
            }
        }

        private boolean isSimpleExpr(StringBuffer sb, String attr) {
            try {
                new Integer(sb.toString());
                return true;
            } catch (NumberFormatException e) {
            }
            if (!sb.toString().startsWith(attr)) return false;
            StringTokenizer st = new StringTokenizer(sb.toString());
            st.nextToken();
            if (!st.hasMoreTokens()) return false; String op = st.nextToken();
            if ((!op.equals("-")) && (!op.equals("+"))) return false;
            if (!st.hasMoreTokens()) return false; String number = st.nextToken();
            try {
                int num = (new Integer(number)).intValue();
                if (num < 1) return false;
                if (op.equals("-")) sb.replace(0,sb.length(),"<");
                if (op.equals("+")) sb.replace(0,sb.length(),">");
                return true;
            } catch (NumberFormatException e) {
            }
            return false;
        }

        private int getHAType(String s, String expr) {
            StringTokenizer st = new StringTokenizer(s);
            st.nextToken();
            String op = st.nextToken();
            int reqnum = (new Integer(st.nextToken())).intValue();
            if (op.equals("==")) {
                if ((expr.equals("<")) || (expr.equals(">"))) return 1;
                int num = (new Integer(expr)).intValue();
                if (reqnum == num) return 3; else return 1;
            }
            if (op.equals(expr)) return 3;
            if (op.equals("<")) {
                if (expr.equals(">")) return 1;
                int num = (new Integer(expr)).intValue();
                if (num < reqnum) return 3; else return 1;
            }
            if (op.equals(">")) {
                if (expr.equals("<")) return 1;
                int num = (new Integer(expr)).intValue();
                if (num > reqnum) return 3; else return 1;
            }
            return 0; //als alles goed gaat wordt dit punt nooit bereikt
        }
    }

    /**
     * This class implements the new algorithm.
     */
    private class AdvancedCalculator {
        private TermModel model = null;        //a reference to the model we are analyzing.
        private Hashtable V = new Hashtable(); //contains all Va sets, where a is an edge used as index.
        private Hashtable W = new Hashtable(); //contains all Wa sets, where a is an edge used as index.

        private Vector currentcalc = new Vector();
        private Vector safeEdges = new Vector();

        boolean ncycle = false;

        public AdvancedCalculator(TermModel model) {
            this.model = model;
/**/if (debug) System.out.println("  *** recalculating model ***");
            findVW();

            checkCycles();

            if (ncycle != cycle) {
                cycle = ncycle;
                fireStateChanged(cycle);
            }
/**/if (debug) System.out.println("recalculated: "+cycle);
        }

        private void checkCycles() {
            Vector checkEdges = new Vector();
            int i;
            TermEdge edge;
            for (i=0;i<model.edges.size();i++) {
                edge = (TermEdge)model.edges.get(i);
                if (V.containsKey(edge)) if ( ((HashSet)V.get(edge)).contains(edge) ) checkEdges.add(edge);
            }
            for (i=0;((i<checkEdges.size()) && (!ncycle));i++) {
                edge = (TermEdge)checkEdges.get(i);
                checkCycle(edge);
            }
        }

        private void checkCycle(TermEdge edge) {
/**/if (debug) System.out.println("testing cycles for edge: "+edge);
            Vector donecycles = new Vector();
            Vector cycles = new Vector();
            Cycle cycle = new Cycle();
            Vector addedges = new Vector();
            Cycle newcycle;
            cycles.add(cycle);
            cycle.add(edge);

            TermEdge lEdge;
            TermEdge toEdge;
            int i;
            int j;

            // find all valid cycles where edge is a part of
            while (cycles.size()>0) {
                for (i=0;i<cycles.size();i++) {
                    addedges.clear();
                    cycle = (Cycle)cycles.get(i);
                    lEdge = (TermEdge)cycle.get(cycle.size()-1);
                    for (j=0;j<lEdge.target.edgesOut.size();j++) {
                        toEdge = (TermEdge)lEdge.target.edgesOut.get(j);
                        if ( (lEdge.type.triggerlist.contains(toEdge.type.name)) && ( ((HashSet)V.get(toEdge)).contains(edge) ) ) {
                            if (toEdge == edge) {
                                newcycle = new Cycle();
                                newcycle.addAll(cycle);
                                donecycles.add(newcycle);
                            } else {
                                if (!cycle.contains(toEdge)) addedges.add(toEdge);
                            }
                        }
                    }

                    for (j=1;j<addedges.size();j++) {
                        newcycle = new Cycle();
                        newcycle.addAll(cycle);
                        newcycle.add(addedges.get(j));
                    }
                    if (addedges.size()==0) {
                        cycles.remove(cycle);
                    } else {
                        cycle.add(addedges.get(0));
                    }
                }
            }

            // check the found cycles for 'halt' types
            for (i=0;((i<donecycles.size()) && (!ncycle));i++)
                ncycle = (ncycle || testCycle((Cycle)donecycles.get(i)));

            // if this edge is part of a cycle then that cycle is safe
            if (!ncycle) safeEdges.add(edge);
        }

        // return true if the cycle is a potential real cycle
        private boolean testCycle(Cycle cycle) {
/**/if (debug) System.out.println("testing cycle: "+cycle);
            int i;

            // test if it contains "safe" edges
            for (i=0;i<safeEdges.size();i++)
                if (cycle.contains(safeEdges.get(i))) return false;

            // test every edge for possible halt types
            for (i=0;i<cycle.size();i++)
                if (testCycleEdge(cycle, (TermEdge)cycle.get(i))) return false;

            return true;
        }

        // return true if the specified edge of the cycle is a true halt type
        private boolean testCycleEdge(Cycle cycle, TermEdge edge) {
            // test if this edge is a halt type
            if ((!edge.type.targethalt) && (!edge.type.sourcehalt)) return false;

            Vector activators = new Vector();
            Vector edges = new Vector();
            HashSet VW;
            TermEdge aEdge;
            int i;
            TermHaltActivator HA;
            TermVertex pVertex = (edge.type.targethalt ? edge.target : edge.source);

            // test if it has an anti activator
/**/if (debug) System.out.println("testing anti activators for "+edge);
            // find anti activator types for this edge
            activators.clear();
            for (i=0;i<edge.type.haltActivators.size();i++) {
                HA = (TermHaltActivator)edge.type.haltActivators.get(i);
                if (HA.haType==3) activators.add(HA.type.name);
            }
            // find edges to pVertex that are in Vcycle (=Vedge)
            edges.clear();
            VW = (HashSet)V.get(edge);
            for (i=0;i<pVertex.edgesIn.size();i++) {
                aEdge = (TermEdge)pVertex.edgesIn.get(i);
                if (VW.contains(aEdge)) edges.add(aEdge);
            }
            // is any of the found edges an anti activator edge
            for (i=0;i<edges.size();i++) if (activators.contains( ((TermEdge)edges.get(i)).type.name )) return false;

            // test if it has a cycle activator
/**/if (debug) System.out.println("testing cycle activators for "+edge);
            // find cycle activator types for this edge
            activators.clear();
            for (i=0;i<edge.type.haltActivators.size();i++) {
                HA = (TermHaltActivator)edge.type.haltActivators.get(i);
                if (HA.haType==2) activators.add(HA.type.name);
            }
            // find the previous edge in the cycle
            i = (cycle.indexOf(edge)-1+cycle.size()) % cycle.size();
            aEdge = (TermEdge)cycle.get(i);
            // is the previous edge a cycle activator edge
            if (activators.contains(aEdge.type.name)) return true;

            // test if it has an immediate activator
/**/if (debug) System.out.println("testing immediate activators for "+edge);
            // find immediate activator type for this edge
            activators.clear();
            for (i=0;i<edge.type.haltActivators.size();i++) {
                HA = (TermHaltActivator)edge.type.haltActivators.get(i);
                if (HA.haType==1) activators.add(HA.type.name);
            }
            // find edges to pVertex that are in Wcycle
            edges.clear();
            VW = cycle.cycleW;
            for (i=0;i<pVertex.edgesIn.size();i++) {
                aEdge = (TermEdge)pVertex.edgesIn.get(i);
                if (VW.contains(aEdge)) edges.add(aEdge);
            }
/**/if (debug) System.out.println("found immediate activators: "+activators);
/**/if (debug) System.out.println("edges to check: "+edges);
/**/if (debug) System.out.println("Wcycle: "+VW);
            // is any of the found edges an immediate activator edge
            for (i=0;i<edges.size();i++) if (activators.contains( ((TermEdge)edges.get(i)).type.name )) return true;
/**/if (debug) System.out.println("not a valid halt type");

            return false;
        }

        private void findVW() {
            TermEdge edge;
            for (int i=0;i<model.edges.size();i++) {
                edge = (TermEdge)model.edges.get(i);
                if (edge.type.starter) {
                    addVW(edge);
                    currentcalc.clear();
                }
            }
        }

        private void addVW(TermEdge edge) {
            // recursive function to determine V and W
            HashSet edgeV = new HashSet();
            HashSet edgeW = new HashSet();
            V.put(edge, edgeV);
            W.put(edge, edgeW);
            currentcalc.add(edge);

            Vector next = edge.target.edgesOut;
            TermEdge toEdge;
            for (int i=0;i<next.size();i++) {
                toEdge = (TermEdge)next.get(i);
                if (edge.type.triggerlist.contains(toEdge.type.name)) {
                    if (currentcalc.contains(toEdge)) {
                        // the current edge is part of a cycle so do real calc
                        realcalcVW(edge);
                    } else {
                        if (!V.containsKey(toEdge)) addVW(toEdge);
                        edgeV.addAll((HashSet)V.get(toEdge));
                        if (toEdge.type.truereq) edgeW.addAll((HashSet)W.get(toEdge));
                        edgeV.add(toEdge);
                        if (toEdge.type.truereq) edgeW.add(toEdge);
                    }
                }
            }
/**/if (debug) System.out.println("V added to "+edge+": "+edgeV);
        }

        private void realcalcVW(TermEdge edge) {
            HashSet edgeV = (HashSet)V.get(edge);
            HashSet edgeW = (HashSet)W.get(edge);
            edgeV.clear();
            edgeW.clear();
            Vector current = new Vector();
            Vector temp = new Vector();
            int i;
            int j;
            TermEdge toEdge;
            TermEdge cEdge;

            for (i=0;i<edge.target.edgesOut.size();i++) {
                toEdge = (TermEdge)edge.target.edgesOut.get(i);
                if (edge.type.triggerlist.contains(toEdge.type.name)) current.add(toEdge);
            }
            while (current.size()!=0) {
                temp.clear();
                edgeV.addAll(current);
                for (i=0;i<current.size();i++) {
                    cEdge = (TermEdge)current.get(i);
                    for (j=0;j<cEdge.target.edgesOut.size();j++) {
                        toEdge = (TermEdge)cEdge.target.edgesOut.get(j);
                        if ((cEdge.type.triggerlist.contains(toEdge.type.name)) && (!edgeV.contains(toEdge)))
                            temp.add(toEdge);
                    }
                }
                current.clear();
                current.addAll(temp);
            }

            current.clear();
            for (i=0;i<edge.target.edgesOut.size();i++) {
                toEdge = (TermEdge)edge.target.edgesOut.get(i);
                if ((toEdge.type.truereq) && (edge.type.triggerlist.contains(toEdge.type.name))) current.add(toEdge);
            }
            while (current.size()!=0) {
                temp.clear();
                edgeW.addAll(current);
                for (i=0;i<current.size();i++) {
                    cEdge = (TermEdge)current.get(i);
                    for (j=0;j<cEdge.target.edgesOut.size();j++) {
                        toEdge = (TermEdge)cEdge.target.edgesOut.get(j);
                        if ((toEdge.type.truereq) && (cEdge.type.triggerlist.contains(toEdge.type.name)) && (!edgeV.contains(toEdge)))
                            temp.add(toEdge);
                    }
                }
                current.clear();
                current.addAll(temp);
            }
        }

        private class Cycle extends Vector {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public HashSet cycleW = new HashSet();
            public Cycle() {super();}
            public boolean add(Object o) {
                cycleW.addAll((HashSet)W.get(o));
                cycleW.add(o);
                return super.add(o);
            }
            public boolean addAll2(Collection c) {
                Iterator iterator = c.iterator();
                while (iterator.hasNext()) this.add(iterator.next());
                return true;
            }
        }
    }
}
