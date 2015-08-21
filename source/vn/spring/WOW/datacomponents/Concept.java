/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Concept.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.parser.VariableLocator;

import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * This class defines a single concept, which is composed of
 * attributes and a suitability expression.
 */
public class Concept {

    /** The database identifier of this concept */
    public long id = -1;
    //the name of the concept
    private String name = null;
    //the description of this concept
    private String description = null;
    //the list of attributes
    private Vector attributes = null;
    //the linked resource (possibly null);
    private URL resource = null;
    // the nocommit value, if this variable is true then there will be nocommit on the updates
    private boolean nocommit = false;
    //the stable string tells the engine if this concept must be kept stable
    // and how. Possible values: always,session,freeze ONLY FOR PAGE STABILITY
    private String stable = null;
    //the stable expression is only valid if stable is set to freeze
    //it defines the expression which determines if the attribute must be stabe or not
    //ONLY FOR PAGE STABILITY
    private String stable_expression = null;
    //WOW-Pitt integration
    //the type of the concept
    private String ctype=null;
    //the hierarchy struct for this concept
    private ConceptHierStruct hierStruct=null;
    //concept title
    private String title=null;
    //End WOW-Pitt

    /**
     * The constant of an string type.
     */
    public final static String STABLE_SESSION = "session";

    /**
     * The constant of a string type.
     */
    public final static String STABLE_ALWAYS = "always";

    /**
     * The constant of a string type.
     */
    public final static String STABLE_FREEZE = "freeze";


    /**
     * Creates a new concept with the specified name.
     */
    public Concept(String name) {

        this.name = name;
        this.nocommit = false;
        attributes = new Vector();
    }

    /**
     * Returns a list (possibly empty) with attributes that are
     * defined for this concept.
     */
    public Vector getAttributes() {return attributes;}

    /**
     * Returns whether the specified attribute exists.
     */
    public boolean hasAttribute(String name) {
        boolean result = false;
        for (int i=0;i<attributes.size();i++)
            result = result || ((Attribute)attributes.get(i)).getName().equals(name);
        return result;
    }

    /**
     * Returns the name of this concept.
     */
    public String getName() {return name;}

    /**
     * Sets the name of this concept.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this concept.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this concept.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the linked resource.
     */
    public void setResourceURL(URL resource) {this.resource = resource;}

    /**
     * Returns the linked resource.
     */
    public URL getResourceURL() {return resource;}

    /**
     * Sets the nocommit value.
     */
    public void setNoCommit(boolean nocommitValue) {
      this.nocommit = nocommitValue;
    }

    /**
     * Returns the nocommit value.
     */
    public boolean getNoCommit() {
      return this.nocommit ;
    }

    /*
    * Added by @Bart 13-05-2008
    */

    /**
    * Sets what the stable property of this attribute is. Is optional so it can be null
    */
    public void setStable(String stable) {
      this.stable = stable;
    }

    /**
    * Returns what the stable property of this attribute is. Is optional so it can be null
    */
    public String getStable() {
      return stable;
    }

    /**
    * Sets what the stable expression property of this attribute is. Is optional so it can be null
    */
    public void setStableExpression(String stable_expression) {
      this.stable_expression = stable_expression;
    }

    /**
    * Returns what the stable expression property of this attribute is. Is optional so it can be null
    */
    public String getStableExpression() {
      return this.stable_expression;
    }

    /**
    * Returns whether the stable property is set
    */
   public boolean hasStableProperty() {
     if ( (this.stable == null) || (this.stable.equals(""))) {
       return false;
     }
     else {
       // check if the stable string is valid
       if (this.stable.equals(STABLE_SESSION) ||
           this.stable.equals(STABLE_FREEZE) || this.stable.equals(STABLE_ALWAYS)) {
         return true;
       }
       else {
         System.out.println("Concept: Error, The stable property has an invalid value so it is ignored. The value was: '" +stable + "'");
         return false;
       }
     }
   }

    /**
    * Returns whether the stable_expression property is set .
    */
        public boolean hasStableExpressionProperty() {
          if ( (this.stable_expression == null) || (this.stable_expression.equals(""))) {
            return false;
          } else {
            return true;
          }
        }

        /**
        * Return whether the stable property is equal to "session" .
        * If the stable property is "session" then this attribute is session stable .
        * @return boolean if it is session stable
        */
        public boolean isSessionStable() {
          // check if it has the stable property
          if (this.hasStableProperty()) {
            // check if it is equal with STABLE_SESSION
            if (this.stable.equals(STABLE_SESSION)) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        }

        /**
        * Return whether the stable property is equal to "freeze" .
        * If the stable property is "freeze" then this attribute is freeze stable .
        * @return boolean if it is freeze stable
        */
        public boolean isFreezeStable() {
          // check if it has the stable property
          if (this.hasStableProperty()) {
            // check if it is equal with STABLE_FREEZE
            if (this.stable.equals(STABLE_FREEZE)) {
              // check if the stable_expr is there
              String expr = this.getStableExpression();
              // check if the expr is null or empty, if so then report this
              // and ignore this stability because it is not valid then
              if ( (expr == null) || (expr.equals(""))) {
                System.out.println("Concept: isFreezeStable: The attribute: " +this.name
                                   +"' is INVALID because the stable_expr field is empty or null."
                                   + " This attribute is now always kept stable");
                return false;
              } else
                return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        }

    /*
    * end Added by @Bart 13-05-2008
    */
    /**
     * Returns the name of this concept.
     * @return the name of this concept
     */
    public String toString() {return name;}

    /**
     * Returns the type of this concept.
     */
    public String getType() {return ctype;}

    /**
     * Sets the type of for this concept.
     */
    public String setType(String type) {return this.ctype=type;}

    /**
     * Returns the ConceptHierStruct of this concept.
     */
    public ConceptHierStruct getHierStruct() {
        return this.hierStruct;
    }

    /**
     * Sets the hierarchy struct for this concept.
     */
    public void setHierStruct(ConceptHierStruct h) {
        this.hierStruct=h;
    }

    /**
     * Returns the title of this concept.
     */
    public String getTitle() {return title;}

    /**
     * Sets the title for this concept.
     */
    public void setTitle(String title) {this.title=title;}

    //added by @David @08-06-2008
    /**
     * Returns the specified attribute.
     */
    public Attribute getAttribute(String name) throws InvalidAttributeException {
        for (int i=0;i<attributes.size();i++) {
            Attribute attr = (Attribute)attributes.get(i);
            if (attr.getName().equals(name)) return attr;
        }
        throw new InvalidAttributeException("'"+name+"' attribute not found in concept '"+name+"'");
    }
    //end added by @David @08-06-2008

    /**
     * Returns a copy of this Concept.
     */
    public Concept copy(String source, String dest) {
        try {
            Concept result = new Concept((name==null?null:name.replaceAll(source, dest)));
            result.setDescription((description==null?null:description.replaceAll(source, dest)));
            result.setResourceURL(resource==null?null:new URL(resource.toString().replaceAll(source, dest)));
            result.setNoCommit(nocommit);
            result.setStable(stable);
            result.setStableExpression((stable_expression==null?null:stable_expression.replaceAll(source, dest)));
            result.setType(ctype);
            result.setTitle((title==null?null:title.replaceAll(source, dest)));
            if (hierStruct!=null) result.setHierStruct(hierStruct.copy(source, dest));
            Attribute attr;
            for (int i=0;i<attributes.size();i++) {
                attr = (Attribute)attributes.get(i);
                result.getAttributes().add(attr.copy(source, dest));
            }
            return result;
        } catch (MalformedURLException e) {e.printStackTrace();return null;}
    }

    /**
     * Returns the apropriate resource as defined by the casegroup of the showability attribute.
     **/
    public URL getActiveURL(VariableLocator vl) throws WOWException, MalformedURLException {
        int showability = -1;
        boolean showattr = true;
        try {
            showability = ((Float)vl.getVariableValue(name+".showability")).intValue();
        } catch (ParserException e) {
            showattr = false;
        } catch (ClassCastException cce) {
            throw new WOWException(name+".showability is not of type 'int'");
        }
        if (!showattr) return resource;

        //find the showability attribute
        Attribute attr = null;
        for (int i=0;i<attributes.size();i++) {
            attr = (Attribute)attributes.get(i);
            if (attr.getName().trim().toLowerCase().equals("showability")) break;
        }

        CaseGroup cg = attr.getCasegroup();
        if (cg == null) return resource;

        Case c = null; Case d;
        for (int i=0;i<cg.getCaseValues().size();i++) {
            d = (Case)cg.getCaseValues().get(i);
            if ( (new Integer(showability)).toString().equals(d.getValue().trim())) {c=d;break;}
        }
        if (c == null) return new URL(cg.getDefaultFragment());

        return new URL(c.getReturnfragment());
    }
}