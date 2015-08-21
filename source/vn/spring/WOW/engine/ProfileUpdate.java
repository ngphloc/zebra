/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ProfileUpdate.java 3.1, June 3, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.parser.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;

import java.util.Vector;
import java.util.Hashtable;

//Reviewed carefully by Loc Nguyen 2008
public class ProfileUpdate {
    //the queue of internal actions
    ActionQueue queue = null;

    //the parser that is used for evaluating expressions
    Parser parser = null;

    //the internal user profile
    Hashtable internal = null;

    //the actual profile
    Profile profile = null;

    //a reference to the profile manager
    ProfileManager manager = null;

    //a reference to the concept database
    ConceptDB cdb = null;

    //the update table used by the UM Variable Locator
    Hashtable utable = new Hashtable();

    /**
     * Creates a new profile update for the specified profile. This
     * method makes an in-memory copy of the user profile to determine
     * relative updates.
     */
    public ProfileUpdate(Profile profile, ProfileManager manager)
                  throws ParserException {
        WOWStatic.checkNull(profile, "profile");
        WOWStatic.checkNull(manager, "manager");
        this.profile = profile;
        this.manager = manager;
        cdb = manager.getWOWDB().getConceptDB();
        queue = new ActionQueue();
        UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
        umvl.setUpdateTable(utable);
        parser = new Parser(umvl);
    }

    /**
     * Updates the specified attribute to the new value. This may
     * produce new actions in the queue if trigger is true.
     */
    public void updateAttribute(String concept, String attribute,
                                AttributeValue value)
                         throws InvalidAttributeException,
                                InvalidConceptException, DatabaseException,
                                ParserException {
        WOWStatic.checkNull(concept, "concept");
        WOWStatic.checkNull(attribute, "attribute");
        WOWStatic.checkNull(value, "value");

        Attribute attr = cdb.getAttribute(cdb.findConcept(concept), attribute);
        processAttributeUpdate(concept, attribute, value, true, attr);
        processQueue();
    }

    private void processQueue()
          throws InvalidAttributeException,
                 InvalidConceptException, DatabaseException,
                 ParserException {
        Action action;
        while (!queue.isEmpty()) {
            action = queue.next();
            String condition = action.getCondition();
            Vector stats;
            if ( ((Boolean)parser.parse(condition)).booleanValue() ) {
                stats = action.getTrueStatements();
            } else {
                stats = action.getFalseStatements();
            }

            for (int i=0;i<stats.size();i++) {
                Assignment assign = (Assignment)stats.get(i);
                DotString var = new DotString(assign.getVariable());
                if (var.size()<2) throw new InvalidAttributeException("invalid attribute in assignment '"+assign.toString()+"'");
                String attrname = var.get(var.size()-1);
                var.set(var.size()-1, null);
                String conceptname = var.toString();
                if (conceptname.equals("personal") || conceptname.endsWith(".personal")) {
                    //attribute belongs to the personal concept
                    AttributeValue nvalue = new AttributeValue(false);
                    Object result = parser.parse(assign.getExpression());
                    nvalue.setValue(result.toString());
                    if (result instanceof Float) nvalue.setValue( new Integer( ((Float)result).intValue() ).toString() );
                    processAttributeUpdate(conceptname, attrname, nvalue, false, null);
                } else {
                    //not an attribute of the personal concept
                    Attribute attr = cdb.getAttribute(cdb.findConcept(conceptname), attrname);

                    boolean isStable = false;
                    if (attr.isFreezeStable()) {
                        // The attribute is freeze stable so evaluate expression to determine if the
                        // attribute is at the moment stable or not
                        // get the freeze stable expression
                        String expr = attr.getStableExpression();
                        isStable = ((Boolean)parser.parse(expr)).booleanValue();
                    } else {
                        // the attribute is not freeze stable so maybe it is normal stable
                        // so check if it has set the stable property
                        if (attr.hasStableProperty()) isStable = true;
                    }
                    AttributeValue value = (AttributeValue)profile.getValues().get(conceptname+"."+attrname);
                    boolean isftu = (value==null?false:value.isFirstTimeUpdated());
                    AttributeValue nvalue = new AttributeValue(false);
                    nvalue.setFirstTimeUpdated((value==null)?false:value.isFirstTimeUpdated());
                    boolean update = ( (!isStable) || ((isStable) && (!isftu)) );
                    if ((isStable) && (!isftu)) nvalue.setFirstTimeUpdated(true);
                    if ((!isStable) && (attr.hasStableProperty())) nvalue.setFirstTimeUpdated(false);
                    if (value!=null) value.setFirstTimeUpdated(nvalue.isFirstTimeUpdated());
                    if (update) {
                        Object result = parser.parse(assign.getExpression());
                        nvalue.setValue(result.toString());
                        if (result instanceof Float) nvalue.setValue( new Integer( ((Float)result).intValue() ).toString() );
                        processAttributeUpdate(conceptname, attrname, nvalue, action.getTrigger(), attr);
                    }
                }
            }
        }
    }

    private void processAttributeUpdate(String concept, String attribute,
                                        AttributeValue value, boolean trigger, Attribute attr)
                                 throws InvalidAttributeException,
                                        InvalidConceptException, DatabaseException,
                                        ParserException {
        Object oldvalue = parser.parse(concept + "." + attribute);

        //update the update table
        Object dif = null;
        try {
            if (attr.getType() == AttributeType.ATTRINT) {
                dif = new Float( (new Float(value.getValue())).intValue()-((Float)oldvalue).intValue() );
            } else if (attr.getType() == AttributeType.ATTRBOOL) {
                dif = new Boolean( (new Boolean(value.getValue())).booleanValue()!=((Boolean)oldvalue).booleanValue() );
            }
        } catch (Exception e) {}
        if (dif != null) utable.put(concept + "." + attribute, dif);

        //update the profile
        AttributeValue nvalue;
        if (!profile.getValues().contains(concept + "." + attribute)) {
            nvalue = new AttributeValue(true);
            profile.getValues().put(concept + "." + attribute, nvalue);
        } else {
            nvalue = ((AttributeValue)profile.getValues().get(concept + "." + attribute));
        }
        nvalue.setValue(value.getValue());
        nvalue.setFirstTimeUpdated(value.isFirstTimeUpdated());

        //add eventual actions
        if (trigger) queue.addActions(attr.getActions());
    }
}