/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorToWow.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.text.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.JOptionPane;
import vn.spring.WOW.graphauthor.GraphAuthor.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.PrintWriter;
import java.io.IOException;


/**
 * AuthorToWOW converts the relations to WOW! code. Modified by Loc Nguyen 2008
 *
 */
public class AuthorToWow {
    public CRTConceptRelationType creltype;

    //Natasha
    private	URL home;
    private	Hashtable checkAttribute;
    private	Hashtable objectWrapper;
    private	Hashtable selectHashtable;
    private	Hashtable sortHashtable;
    private	String dirname = "";
    private	int counter;
    private	Vector appliedStrategiesVector;
    //end Natasha

    /**
     * Default constructor.
     */
    public AuthorToWow(URL base) {
    	
    	//Natasha
    	this.home = base;
    	this.checkAttribute = new Hashtable();
    	this.objectWrapper = new Hashtable();
    	this.selectHashtable = new Hashtable();
    	this.sortHashtable = new Hashtable();
    	this.counter = 1;
		//check for applied strategies
		try {
			//read from strategy list AuthorSTATIC
			AuthorSTATIC.strategyInfoList = new LinkedList();
			for (ListIterator i = AuthorSTATIC.strategyList.listIterator();i.hasNext();) {
				new ReadStrategyXML(i.next().toString(), home);
			}
			for (Iterator i =  AuthorSTATIC.strategyInfoList.iterator(); i.hasNext();) {
				Strategy strategy = (Strategy) i.next();
				for (Iterator j = strategy.ifStatements.iterator(); j.hasNext();) {
					StrategyIfStatement strategyIfStatement = (StrategyIfStatement) j.next();
					StrategyThenStatement strategyThenStatement = strategyIfStatement.thenStatement;
					appliedStrategiesVector = new Vector();
                    String selectAttribute = strategyThenStatement.selectAttribute;;
					String sortAttribute = strategyThenStatement.sortAttribute;;
					String setDefaultAttribute = strategyThenStatement.setDefaultAttribute;
					String actionAttribute = strategyThenStatement.actionAttribute;
					//change
					Vector attrType = new Vector();
					if (!selectAttribute.equals("")) {
					//if (selectAttribute != null) {
						attrType = new Vector();
						attrType.add(selectAttribute);
						attrType.add("select");
						if (checkAttribute.get(attrType) != null) appliedStrategiesVector = (Vector)checkAttribute.get(attrType);
					}
					//if (sortAttribute != null) {
					if (!sortAttribute.equals("")) {
						attrType = new Vector();
						attrType.add(sortAttribute);
						attrType.add("sort");
						if (checkAttribute.get(attrType) != null) appliedStrategiesVector = (Vector)checkAttribute.get(attrType);
					}
					//if (setDefaultAttribute != null) {
					if (!setDefaultAttribute.equals("")) {
						attrType = new Vector();
						attrType.add(setDefaultAttribute);
						attrType.add("default");
						if (checkAttribute.get(attrType) != null) appliedStrategiesVector = (Vector)checkAttribute.get(attrType);
					}
					//if (actionAttribute != null) {
					if (!actionAttribute.equals("")) {
						attrType = new Vector();
						attrType.add(actionAttribute);
						attrType.add("action");
						if (checkAttribute.get(attrType) != null) appliedStrategiesVector = (Vector)checkAttribute.get(attrType);
					}
					boolean conflict = CheckConflictingStatements(appliedStrategiesVector, strategyIfStatement);
					if (!conflict) {
						appliedStrategiesVector.add(strategyIfStatement);
						checkAttribute.put(attrType, appliedStrategiesVector);
					}
					//here check if this attribute already exists in the hashtable and check for conflicting strategies
				}
			}
			String path = home.getPath();
			String pathttemp = path.substring(1, path.length());
			int index = pathttemp.indexOf("/");
			index++;
			dirname = path.substring(0, index);
			if (dirname.equals("/graphAuthor")) dirname = "";
		}
		catch (Exception e) {
        	JOptionPane.showMessageDialog(null,
                    "@Natasha: AuthorToWow(URL base): An error occured while read strategies",
                    "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		//end Natasha

        AuthorSTATIC.wowOut = new WOWOutConceptList();
        this.AddConceptsToWOWOut();

        AuthorSTATIC.relwow.AddDivideInformation();

        // group all relation
        LinkedList groupedRelations = AuthorSTATIC.relwow.SplitConceptRelations();

        //check if empty
        if (groupedRelations.isEmpty()) {
            String error = "Relation list is empty, there is nothing to save!";
            javax.swing.JOptionPane.showMessageDialog(null, error, "Error",
                                                      javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
        }

        for (Iterator i = groupedRelations.iterator(); i.hasNext();) {
            this.ProcessConceptRel((LinkedList) i.next());
        }
    }

    void listConceptInfoAndAttributes() {
      for (Iterator i =  AuthorSTATIC.conceptInfoList.iterator(); i.hasNext();) {
        WOWOutConcept tempConcept = (WOWOutConcept) i.next();
        for (Iterator j =  tempConcept.attributeList.iterator(); j.hasNext();) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
          //Loc Nguyen add
          if(tempAttr == null) {}
        }
      }
    }


    void listConceptAndAttributes() {
      for (Iterator i =  AuthorSTATIC.conceptList.iterator(); i.hasNext();) {
        WOWOutConcept tempConcept = (WOWOutConcept) i.next();
        for (Iterator j =  tempConcept.attributeList.iterator(); j.hasNext();) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
          //Loc Nguyen add
          if(tempAttr == null) {}
        }
      }
    }

    void listWOWOut() {
      for (Iterator i =  AuthorSTATIC.wowOut.conceptList.iterator(); i.hasNext();) {
        WOWOutConcept tempConcept = (WOWOutConcept) i.next();
        for (Iterator j =  tempConcept.attributeList.iterator(); j.hasNext();) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
          //Loc Nguyen add
          if(tempAttr == null) {}
        }
      }
    }


    /**
     * debug function
     */
    public void printWOWOut() {
        for (Iterator j = AuthorSTATIC.wowOut.conceptList.iterator();
             j.hasNext();) {
            WOWOutConcept outtest = (WOWOutConcept) j.next();
            for (Iterator k = outtest.attributeList.iterator(); k.hasNext();) {
                k.next();
            }
        }
    }

    //added by @David @18-05-2008
    private DefaultMutableTreeNode findRecNode(DefaultMutableTreeNode node, String name) {
        if ((AuthorSTATIC.projectName + "." + node.toString()).trim().equals(name)) return node; else {
            for (Enumeration i = node.children(); i.hasMoreElements();) {
                DefaultMutableTreeNode next = (DefaultMutableTreeNode)i.nextElement();
                DefaultMutableTreeNode result = findRecNode(next, name);
                if (result != null) return result;
            }
        }
        return null;
    }

    // returns the node of the concept with specified name.
    private DefaultMutableTreeNode findNode(String name) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)GraphAuthor.sharedConceptTree.getModel().getRoot();
        return findRecNode(root, name);
    }

    /**
     * Adds the hierarchy information to the WOWOutConcept.
     */
    private void addTreeInformation(WOWOutConcept outc) {
        DefaultMutableTreeNode node = findNode(outc.name.trim());
        if (node != null) {
            outc.parent = ""; if (node.getParent()!=null) outc.parent = AuthorSTATIC.projectName + "." + node.getParent().toString().trim();
            outc.firstchild = ""; if (node.getChildCount()!=0) outc.firstchild = AuthorSTATIC.projectName + "." + node.getFirstChild().toString().trim();
            outc.nextsib = ""; if (node.getNextSibling()!=null) outc.nextsib = AuthorSTATIC.projectName + "." + node.getNextSibling().toString().trim();
        }
    }
    //end added by @David @18-05-2008

    /**
     * AddConceptsToWOWOut adds all the concepts with attributes to the wowout concept list.
     */
	public void AddConceptsToWOWOut() {
		// all concepts
		for (Iterator i = AuthorSTATIC.conceptInfoList.iterator(); i.hasNext(); ) {
			WOWOutConcept inC = (WOWOutConcept) i.next();
	
			String aConceptName = (AuthorSTATIC.projectName + "." + inC.name).trim();
			WOWOutConcept outc = new WOWOutConcept();
			outc.name = aConceptName;
			outc = inC;
	
			//added by @David @18-05-2008
			addTreeInformation(outc);
	        //end added by @David @18-05-2008
	
			AuthorSTATIC.wowOut.conceptList.add(outc);

			//Natasha
			//check for children having sort or select attribute
			for (Enumeration keys = checkAttribute.keys();keys.hasMoreElements();) {//start Enumeration
				Vector attrType = (Vector)keys.nextElement();
				String key = (String)attrType.elementAt(0);
				for (Iterator j =  outc.attributeList.iterator(); j.hasNext();) {//start attribute list
					WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
	    			if (tempAttr.name.equals(key)) {//start: attribute from "checkAttribute" found
	    				if (((String)attrType.elementAt(1)).equals("select")) selectHashtable = fillInHashtable(selectHashtable, outc.parent, tempAttr, key);
	    				if (((String)attrType.elementAt(1)).equals("sort")) sortHashtable = fillInHashtable(sortHashtable, outc.parent, tempAttr, key);
						if (((String)attrType.elementAt(1)).equals("action") || ((String)attrType.elementAt(1)).equals("default")) {//start: add actions or set default
							Vector vec = (Vector)checkAttribute.get(attrType);
							for (int k=0; k<vec.size(); k++) {
								StrategyIfStatement strategyIfStatement = (StrategyIfStatement)vec.get(k);
								StrategyThenStatement strategyThenStatement = strategyIfStatement.thenStatement;
								if (strategyThenStatement.actionList.size() > 0 && !outc.name.startsWith("generated")) {//add actions
									StrategyAction strategyAction = (StrategyAction)strategyThenStatement.actionList.elementAt(0);
									CRTGenerateListItem gloutAccess = new CRTGenerateListItem();
									CRTAction outActionAccess = new CRTAction();
									String requirement = strategyIfStatement.condition;
									gloutAccess.requirement = this.ReplaceParent(this.ReplaceKeyword(this.ReplaceConcepttype(requirement, outc.concepttype), outc.name), outc.parent);
									if (strategyAction.UMvariable.indexOf("{") > 0) {
										String tmp = this.ReplaceBrackets(strategyAction.UMvariable, treeNodeLevel(outc.name));
										outActionAccess.conceptName = tmp.substring(0, tmp.lastIndexOf("."));
										outActionAccess.attributeName = tmp.substring(tmp.lastIndexOf(".")+1);
									}
									else {
										outActionAccess.conceptName = strategyAction.UMvariable.substring(0, strategyAction.UMvariable.lastIndexOf("."));
										outActionAccess.attributeName = strategyAction.UMvariable.substring(strategyAction.UMvariable.lastIndexOf(".")+1);
									}
									if (strategyAction.UMvariable.indexOf("{") > 0) {
										outActionAccess.expression = this.ReplaceKeyword(this.ReplaceVar(this.ReplaceFirstChild(this.ReplaceBrackets(strategyAction.expression, treeNodeLevel(outc.name)), outc.parent)), outc.name);
									}
									else outActionAccess.expression = this.ReplaceKeyword(this.ReplaceVar(strategyAction.expression), outc.name);
									gloutAccess.trueActions.actionList.add(outActionAccess);
									WOWOutAttribute accessAtt = null;
									try {
										accessAtt = outc.getAttribute("access");
									} catch (Exception exc) {}
									if (accessAtt != null) {
										boolean result = false;
										for (Iterator l = accessAtt.generateListItemList.iterator(); l.hasNext();) {
											CRTGenerateListItem glItem = (CRTGenerateListItem) l.next();
											if (glItem.requirement.equals(gloutAccess.requirement)) {
												result = true;
												glItem.trueActions.actionList.add(outActionAccess);
											}
										}
										if (!result) accessAtt.generateListItemList.add(gloutAccess);
									}
								}//end: add actions
								if (!strategyThenStatement.setDefaultAttribute.equals("") && !outc.parent.equals("") && !outc.template.equals("fragment concept")) {//setdefault
									//getting an attribute of a concept for which a default should be set
									WOWOutAttribute defaultAtt = null;
									try {
										defaultAtt = outc.getAttribute(strategyThenStatement.setDefaultAttribute.trim());
									} catch (Exception exc) {}
									if (defaultAtt != null) {//8
										String setDefaultCondition = this.ReplaceFirstChild(this.ReplaceName(strategyIfStatement.condition, outc.name), outc.parent);
										boolean b = false;
										try {
											b = EvaluateExpression(setDefaultCondition);
										} catch (Exception err) {System.out.println("Can't evaluate expression");}
										if (b) {
											if (defaultAtt.setDefaultList.setdefault.trim().equals("true")) {
												String tmp = this.ReplaceKeyword(this.ReplaceBrackets(strategyThenStatement.setDefaultExpression.trim(), treeNodeLevel(outc.name)), outc.name);
												defaultAtt.setDefaultList.setdefault = this.ReplacePreviousSibling(this.ReplaceParent(tmp, outc.parent), outc.name);
											}
											//combination
											else {
												String tmp = this.ReplaceKeyword(this.ReplaceBrackets(strategyThenStatement.setDefaultExpression.trim(), treeNodeLevel(outc.name)), outc.name);
												tmp = this.ReplacePreviousSibling(this.ReplaceParent(tmp, outc.parent), outc.name);
												defaultAtt.setDefaultList.setdefault = defaultAtt.setDefaultList.setdefault + "&amp;&amp;" + tmp;
											}
										}
									}
								}//end: set default
							}//end: for
						}//end: add actions or set default
					}//end: attribute from "checkAttribute" found
	    		}//end attribute list
			}//end Enumeration
		} //start "for" conceptInfoList
		
		if (selectHashtable.size() > 0) { //selectHashtable
			DefaultMutableTreeNode genNode = null;
			if (!CheckConceptNameGAExist("generatedConcepts")) { //check for "generatedConcepts"
				genNode = new DefaultMutableTreeNode("generatedConcepts");
				WOWOutConcept genConcept = new WOWOutConcept();
				genConcept.name = "generatedConcepts";
				genConcept.description = "Automatically generated concept";
				//replace with the string returned by the save method
				//?directory and course name
				genConcept.template ="abstract concept";
				genConcept.concepttype ="abstract";
				genConcept.stable ="";
				genConcept.attributeList = new LinkedList();
				genConcept.title = "generated concept";
				genConcept.nocommit = false;
				for (Iterator m = AuthorSTATIC.templateList.iterator(); m.hasNext();) {
					ConceptTemplate cTemp = (ConceptTemplate) m.next();
					if (genConcept.template.equals(cTemp.name)) {
						// process all attributes form the template
						for (Iterator l = cTemp.attributes.iterator(); l.hasNext();) {
							WOWOutAttribute tempAtt = (WOWOutAttribute) l.next();
							// new is needed to reserve new memory
							WOWOutAttribute cloneAtt = new WOWOutAttribute();
							cloneAtt.name = tempAtt.name;
							cloneAtt.type = tempAtt.type;
							cloneAtt.description = tempAtt.description;
							cloneAtt.setDefaultList = new CRTSetDefault();
							if (tempAtt.name.equals("level")) cloneAtt.setDefaultList.setdefault = "1";
							else cloneAtt.setDefaultList.setdefault = tempAtt.setDefaultList.setdefault;
							cloneAtt.isChangeable = tempAtt.isChangeable;
							cloneAtt.isPersistent = tempAtt.isPersistent;
							cloneAtt.isSystem = tempAtt.isSystem;
							genConcept.attributeList.add(cloneAtt);
						}
					}
				}
				GraphAuthor.conceptList.add(genConcept);
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) GraphAuthor.conceptTree.getModel().getRoot();
				n.add(genNode);
				GraphAuthor.sharedConceptTree.setModel(GraphAuthor.conceptTree.getModel());
				((DefaultTreeModel) GraphAuthor.conceptTree.getModel()).reload();
			}//end check for "generatedConcepts"
			else
				genNode = findNode(AuthorSTATIC.projectName+".generatedConcepts");
			
			Hashtable parentResources = new Hashtable();
			for (Enumeration selectKeys = selectHashtable.keys();selectKeys.hasMoreElements();) { //start Enumeration selectKeys
				String parentKey = (String)selectKeys.nextElement();
				Vector selectVector = (Vector)selectHashtable.get(parentKey);
				WOWOutConcept outp = getWOWOutConcept(parentKey);
				DefaultMutableTreeNode node = findNode(parentKey);
				for (Enumeration children = node.children(); children.hasMoreElements();) {
					//here add actions to the children attributes!!!!!!!
					String childName = children.nextElement().toString();
					WOWOutAttribute checkAttr = null;
					WOWOutConcept outconc = null;
					for (int i = 0; i < selectVector.size(); i++) {
						Vector attrVector = (Vector)selectVector.elementAt(i);
						String attrName = (String)attrVector.elementAt(0);
						String attrValue = (String)attrVector.elementAt(1);
						int childrenNum = ((Integer)attrVector.elementAt(2)).intValue();
						try {
							outconc = getWOWOutConcept(AuthorSTATIC.projectName+"."+childName);
							checkAttr = outconc.getAttribute(attrName);
						} catch (Exception exc) {}
						if (outconc != null && checkAttr != null) {
							if (checkAttr.setDefaultList.setdefault.equals(attrValue)) addActionsToChildren(outconc, checkAttr, childrenNum, parentKey, childName);
						}
					}
				}
				for (Enumeration keys = checkAttribute.keys();keys.hasMoreElements();) {//start Enumeration keys
					Vector attrType = (Vector)keys.nextElement();
					String key = (String)attrType.elementAt(0);
					Vector vec = (Vector)checkAttribute.get(attrType);
					if (((String)attrType.elementAt(1)).equals("select")) {
						for (int k=0; k<vec.size(); k++) {
							StrategyIfStatement strategyIfStatement = (StrategyIfStatement)vec.get(k);
							StrategyThenStatement strategyThenStatement = strategyIfStatement.thenStatement;
							if (strategyThenStatement.selectList.size() > 0) {
								boolean showDefault = true;
								for (Enumeration children = node.children(); children.hasMoreElements();) {
									String childName = children.nextElement().toString();
									for (int c=0; c<strategyThenStatement.selectList.size(); c++) {
										Vector v = (Vector)strategyThenStatement.selectList.elementAt(c);
										String hashtableKey = (String)v.elementAt(0);
										String hashtableKeyValue = (String)v.elementAt(1);
										if (hashtableKeyValue.equals("showContent")) {
											WOWOutAttribute checkAttribute = null;
											try {
												WOWOutConcept outconc = getWOWOutConcept(AuthorSTATIC.projectName+"."+childName);
												checkAttribute = outconc.getAttribute(key);
											} catch (Exception exc) {}
											if (checkAttribute != null && checkAttribute.setDefaultList.setdefault.equals(hashtableKey)) {
												showDefault = false;
											}
										}
									}
								}
								StringBuffer parentResource = new StringBuffer("");
								parentResource.append("<span>\n");
								for (int c=0; c<strategyThenStatement.selectList.size(); c++) {
									Vector v = (Vector)strategyThenStatement.selectList.elementAt(c);
									String hashtableKey = (String)v.elementAt(0);
									String hashtableKeyValue = (String)v.elementAt(1);
									for (Enumeration children = node.children(); children.hasMoreElements();) {
										String childName = children.nextElement().toString();
										WOWOutConcept outch = getWOWOutConcept(AuthorSTATIC.projectName+"."+childName);
										for (Iterator j =  outch.attributeList.iterator(); j.hasNext();) {
											boolean stop = false;
											WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
											if (tempAttr.name.equals(key) && tempAttr.setDefaultList.setdefault.trim().equals(hashtableKey)) {
												if (hashtableKeyValue.equals("showLink")) {
													if (objectWrapper.get(outch.name) == null) {
														objectWrapper.put(outch.name, "generated"+counter);
														StringBuffer XMLstring = new StringBuffer("");
														XMLstring.append("<!DOCTYPE html SYSTEM \""+dirname+"/WOWstandard/xhtml1-strict.dtd\">\n");
														XMLstring.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<body>\n");
														XMLstring.append("<object data=\"../header.xml\" type=\"text/xml\"> </object>\n");
														XMLstring.append("<object name=\""+outch.name+"\" type=\"wow/text\" />");
														XMLstring.append("\n</body>\n</html>");
														save("generated"+counter, XMLstring);
														//save method should return a resource
														WOWOutConcept outconcept = new WOWOutConcept();
														outconcept.name = "generated"+counter;
														outconcept.description = "Automatically generated concept";
														//replace with the string returned by the save method
														//?directory and course name
														outconcept.resource = "file:/"+AuthorSTATIC.projectName+"/xhtml/generated"+counter+".xhtml";
														outconcept.template ="generated page concept";
														outconcept.concepttype ="generated page";
														outconcept.stable ="";
														outconcept.attributeList = new LinkedList();
														outconcept.title = "generated"+counter;
														outconcept.nocommit = false;
														// find the template for a concept
														for (Iterator m = AuthorSTATIC.templateList.iterator(); m.hasNext();) {
															ConceptTemplate cTemp = (ConceptTemplate) m.next();
															if (outconcept.template.equals(cTemp.name)) {
																// process all attributes form the template
																for (Iterator l = cTemp.attributes.iterator(); l.hasNext();) {
																	WOWOutAttribute tempAtt = (WOWOutAttribute) l.next();
																	// new is needed to reserve new memory
																	WOWOutAttribute cloneAtt = new WOWOutAttribute();
																	cloneAtt.name = tempAtt.name;
																	cloneAtt.type = tempAtt.type;
																	cloneAtt.description = tempAtt.description;
																	cloneAtt.setDefaultList = new CRTSetDefault();
																	if (tempAtt.name.equals("suitability")) cloneAtt.setDefaultList.setdefault = "false";
																	else if (tempAtt.name.equals("level")) cloneAtt.setDefaultList.setdefault = "2";
																	else cloneAtt.setDefaultList.setdefault = tempAtt.setDefaultList.setdefault;
																	cloneAtt.isChangeable = tempAtt.isChangeable;
																	cloneAtt.isPersistent = tempAtt.isPersistent;
																	cloneAtt.isSystem = tempAtt.isSystem;
																	outconcept.attributeList.add(cloneAtt);
																}
															}
														}
														//the hierarchy attribute - is it necessary?
														WOWOutAttribute hierarchyAttr = new WOWOutAttribute();
														hierarchyAttr.name = "hierarchy";
														//change
														hierarchyAttr.description = "the visibility of this concept in the hierarchy";
														hierarchyAttr.setDefaultList = new CRTSetDefault();
														hierarchyAttr.setDefaultList.setdefault = "false";
														//?? check true
														hierarchyAttr.isChangeable = Boolean.FALSE;
														hierarchyAttr.isPersistent = Boolean.FALSE;
														hierarchyAttr.isSystem = Boolean.TRUE;
														hierarchyAttr.type = "bool";
														outconcept.attributeList.add(hierarchyAttr);
														if (!CheckConceptNameGAExist(outconcept.name)) {
															GraphAuthor.conceptList.add(outconcept);
															genNode.add(new DefaultMutableTreeNode(outconcept.name));
														}
														counter++;
													}
													String comment = null;
													try {
														comment = (String)v.elementAt(2);
													} catch (Exception e) {}
													parentResource.append("<br />\n<a href=\""+objectWrapper.get(outch.name)+".xhtml"+"\" class=\"Neutral\">"+comment+"</a>");
												}
												//or showContentDefault
												if (hashtableKeyValue.equals("showContent") || (hashtableKeyValue.equals("showContentDefault") && showDefault)) {
													parentResource.append("<br />\n<object name=\""+outch.name+"\" type=\"wow/text\" />");
												}
												stop = true;
												break;
											}
											if (stop) break;
										}
									}
								}
								parentResource.append("\n</span>");
								save("generated"+counter, parentResource);
								parentResources.put(strategyIfStatement.condition, "file:/"+AuthorSTATIC.projectName+"/xhtml/generated"+counter+".xhtml");
								//add the resource to a fragment concept corresponding to a parent
								counter++;
							}
						}
					} //end Enumeration keys
				} //end Enumeration selectKeys
				//add resources to a parent
				WOWOutAttribute showabilityAtt = null;
				try {
					showabilityAtt = outp.getAttribute("showability");
				} catch (Exception exc) {}
			
				if (showabilityAtt != null) {
					showabilityAtt.casegroup = new CaseGroup();
					showabilityAtt.casegroup.setDefaultFragment("file:/"+AuthorSTATIC.projectName+"/xhtml/empty.xhtml");
					Vector tempCV = new Vector();
					for (Enumeration keys = parentResources.keys();keys.hasMoreElements();) {
						String key = (String)keys.nextElement();
						String keyValue = (String)parentResources.get(key);
						Case caseValue = new Case();
						caseValue.setValue(key);
						caseValue.setReturnfragment(keyValue);
						tempCV.add(caseValue);
					}
					showabilityAtt.casegroup.setCaseValues(tempCV);
					WOWOutConcept outGraphAuthor = null;
					int index = -1;
					for (Iterator ii = GraphAuthor.conceptList.iterator(); ii.hasNext();) {
						outGraphAuthor = (WOWOutConcept) ii.next();
						if (outp.name.replaceAll(AuthorSTATIC.projectName+".", "").equals(outGraphAuthor.name)) {
							index = GraphAuthor.conceptList.indexOf(outGraphAuthor);
							break;
						}
					}
					if (outGraphAuthor != null) {
						WOWOutAttribute showabilityAttGA = null;
						showabilityAttGA = outGraphAuthor.getAttribute("showability");
						if (showabilityAttGA != null) outGraphAuthor.attributeList.remove(showabilityAttGA);
						outGraphAuthor.attributeList.add(showabilityAtt);
						GraphAuthor.conceptList.set(index, outGraphAuthor);
					}
				}
			}
		}//end selectHashtable
		
		if (sortHashtable.size() > 0) {//sortHashtable
			for (Enumeration sortKeys = sortHashtable.keys();sortKeys.hasMoreElements();) {
				String parentKey = (String)sortKeys.nextElement();
				Vector sortVector = (Vector)sortHashtable.get(parentKey);
				getWOWOutConcept(parentKey);
				DefaultMutableTreeNode node = findNode(parentKey);
				for (Enumeration children=node.children();children.hasMoreElements();) {
					String childName = children.nextElement().toString();
					WOWOutConcept outconc = getWOWOutConcept(AuthorSTATIC.projectName+"."+childName);
		            String addrequirement = "";
					for (int i = 0; i < sortVector.size(); i++) {
						Vector attrVector = (Vector)sortVector.elementAt(i);
						String attrName = (String)attrVector.elementAt(0);
						String attrValue = (String)attrVector.elementAt(1);
						int childrenNum = ((Integer)attrVector.elementAt(2)).intValue();
		                WOWOutAttribute checkAttr = null;
						try {
							checkAttr = outconc.getAttribute(attrName);
						} catch (Exception exc) {}
		                if (outconc != null && checkAttr != null) {
							if (checkAttr.setDefaultList.setdefault.equals(attrValue)) {
								addActionsToChildren(outconc, checkAttr, childrenNum, parentKey, childName);
								Vector attrType = new Vector();
								attrType.add(attrName);
								attrType.add("sort");
								Vector vec = (Vector)checkAttribute.get(attrType);
								if (vec != null) {
									for (int k=0; k<vec.size(); k++) {//6
										StrategyIfStatement strategyIfStatement = (StrategyIfStatement)vec.get(k);
										StrategyThenStatement strategyThenStatement = strategyIfStatement.thenStatement;
											if (strategyThenStatement.sortList.size() > 0) {
												if (strategyThenStatement.sortList.contains(attrValue)) {
													int index = strategyThenStatement.sortList.indexOf(attrValue);
													if (index > 0) {
													//check for existance of a previous child
														if (!addrequirement.equals("")) addrequirement = addrequirement + " || ";
														//replace the threshold
														addrequirement = addrequirement + "("+outconc.parent+"."+ strategyThenStatement.sortList.elementAt(index-1).toString().replaceAll("\"", "") +" > 75 && "+strategyIfStatement.condition+")";
													}
													else {
														if (!addrequirement.equals("")) addrequirement = addrequirement + " || ";
														addrequirement = addrequirement + strategyIfStatement.condition;
													}
												}
											}
									}
								}
							}
		                }
					}
					WOWOutAttribute suitabilityAtt = null;
					try {
						suitabilityAtt = outconc.getAttribute("suitability");
					} catch (Exception exc) {}
					if (suitabilityAtt != null && !addrequirement.equals("")) {
						if (suitabilityAtt.setDefaultList.setdefault.trim().equals("true")) suitabilityAtt.setDefaultList.setdefault = addrequirement;
						else suitabilityAtt.setDefaultList.setdefault = addrequirement + suitabilityAtt.setDefaultList.setdefault.replaceAll("\"","");
						addrequirement = "";
					}
				}
			}
		} //end sortHashtable
		((DefaultTreeModel) GraphAuthor.conceptTree.getModel()).reload();
		addGeneratedTreeInformation();
    }


    /**
     * AddAttributesToWOWOut adds the attributes defined in attlist to the output concepts.
     */
    public void AddAttributesToWOWOut() {

// all concepts
      for (Iterator i = AuthorSTATIC.conceptList.iterator(); i.hasNext();) {
         WOWOutConcept inC = (WOWOutConcept) i.next();

         String aConceptName = (AuthorSTATIC.projectName + "." + inC.name).trim();
         WOWOutConcept outc = new WOWOutConcept();
         outc.name = aConceptName;
 // find the template for a concept
         for (Iterator k = AuthorSTATIC.templateList.iterator(); k.hasNext();) {
           ConceptTemplate cTemp = (ConceptTemplate) k.next();
           if (inC.template.equals(cTemp.name)) {

           // process all attributes form the template
             for (Iterator l = cTemp.attributes.iterator(); l.hasNext();) {
               WOWOutAttribute tempAtt = (WOWOutAttribute) l.next();

               // new is needed to reserve new memory
               WOWOutAttribute cloneAtt = new WOWOutAttribute();
               cloneAtt.description = tempAtt.description;
           cloneAtt.setDefaultList = new CRTSetDefault();
           cloneAtt.setDefaultList.setdefault = tempAtt.setDefaultList.setdefault;
               cloneAtt.isChangeable = tempAtt.isChangeable;
               cloneAtt.isPersistent = tempAtt.isPersistent;
               cloneAtt.isSystem = tempAtt.isSystem;
               cloneAtt.name = tempAtt.name;
               cloneAtt.type = tempAtt.type;

              outc.attributeList.add(cloneAtt);
             }
           }
         }

         for (Iterator iIterator = AuthorSTATIC.conceptInfoList.iterator();iIterator.hasNext();) {
           WOWOutConcept info = (WOWOutConcept) iIterator.next();

           if (info.name.equals(outc.name.trim())) {
             outc.description = info.description;
             outc.resource = info.resource;
           }
         }
       AuthorSTATIC.wowOut.conceptList.add(outc);
      }
    }

    /**
     * CheckAttributeName returns true if attributename exists in conceptname.
     */
    public boolean CheckAttributeName(String conceptname, String attributename) {
        boolean result;
        result = false;

        for (Iterator i =  AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptname)) {
                for (Iterator j = acon.attributeList.iterator(); j.hasNext();) {
                    WOWOutAttribute attr = (WOWOutAttribute) j.next();

                    if (attr.name.equals(attributename)) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    /**
     *  AddAttributeToConceptAttribute, add to attribute expression to the output concept
     */
    public void AddAttributeToConceptAttribute(String conceptName,
                                               String attributeName,
                                               String sValue) {
        String sNewValue = new String(sValue);
        sValue = " && " + sNewValue;

        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptName)) {
                // found conceptName
                for (Iterator j = acon.attributeList.iterator(); j.hasNext();) {
                    WOWOutAttribute outat = (WOWOutAttribute) j.next();

                    if (outat.name.equals(attributeName)) {
                        //found attributename
                        if (outat.setDefaultList.setdefault.trim().equals("")) {
                            outat.setDefaultList.setdefault = sNewValue;
                        } else {
                            outat.setDefaultList.setdefault = outat.setDefaultList.setdefault +
                                                              sValue;
                        }
                    }
                }
            }
        }
    }

    /**
     * AddAttibuteToConcept adds a new attribute to a concept.
     *
     * This is function isn`t used anymore, all attributes are set through attlist.xml
     */
    public void AddAttributeToConcept(String conceptname, WOWOutAttribute ain) {
        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptname)) {
                acon.attributeList.add(ain);
            }
        }
    }

    /**
     * CheckConceptNameExist returns true if conceptname is already in the output conceptlist.
     */
    public boolean CheckConceptNameExist(String conceptname) {
        boolean result;
        result = false;

        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptname)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * ReplaceVar replaces the keyword "var:" with the label.
     */
    public String ReplaceVar(String sin, RELConceptRelation rin) {
        String sout;
        sout = sin;

        if (rin.label.equals("")) {
            sout = sout.replaceAll("var:", "");

            return sout;
        }

        int n = sout.indexOf("var:");
        int last = n;
        int first = n;
        char ch;
        ch = '1';

        while (n != -1) {
            last = n;
            first = last;
            ch = '1';

            while ((last < sout.length()) && (ch != ' ')) {
                ch = sout.charAt(last);
                last = last + 1;
            }

            if (last < sout.length()) {
                last = last - 1;
            }

            String subs = sout.substring(first, last);
            int m = rin.label.indexOf("%");
            String sProcent;

            if (m != -1) {
                sProcent = rin.label.replaceFirst("%", "");

                int intProcent = Integer.parseInt(sProcent);
                double doubleProcent = ((double) intProcent / 100);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);

                String test = df.format(doubleProcent);
                sout = sout.replaceFirst(subs, test);
                n = sout.indexOf("var:");
            } else {
                sout = sout.replaceFirst(subs, rin.label);
                n = sout.indexOf("var:");
            }
        }

        return sout;
    }

    /**
     * ReplaceKeyword change source,destination,parent, child with the correct concept.
     */
    public String ReplaceKeyword(String sin, RELConceptRelation rin) {
        String sout;
        sout = sin;
        sout = sout.replaceAll("___destination", rin.destinationConceptName);
        sout = sout.replaceAll("___source", rin.sourceConceptName);
        sout = sout.replaceAll("___parent", rin.destinationConceptName);
        sout = sout.replaceAll("___child", rin.sourceConceptName);

        return sout;
    }

    /**
     * checkgeneratelistitem rerturn true if the requirements allready exsists.
     */
    public boolean CheckGenerateListItem(String conceptName,
                                         String attributeName,
                                         String requirement) {
        boolean result = false;

        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptName)) {
                for (Iterator j = acon.attributeList.iterator(); j.hasNext();) {
                    WOWOutAttribute attr = (WOWOutAttribute) j.next();

                    if (attr.name.equals(attributeName)) {
                        for (Iterator k = attr.generateListItemList.iterator();
                             k.hasNext();) {
                            CRTGenerateListItem glItem =
                                    (CRTGenerateListItem) k.next();

                            if (glItem.requirement.equals(requirement)) {
                                result = true;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public void AddGenereateListItemToAttributeConcept(String conceptName,
                                                       String attributeName,
                                                       CRTGenerateListItem glout) {
        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptName)) {
                // found conceptName
                for (Iterator j = acon.attributeList.iterator(); j.hasNext();) {
                    WOWOutAttribute outat = (WOWOutAttribute) j.next();

                    if (outat.name.equals(attributeName)) {
                        outat.generateListItemList.add(glout);
                    }
                }
            }
        }
    }

    /**
     * AddActionToAttributeConcept adds an action to the output concept.
     */
    public void AddActionToAttributeConcept(String conceptName,
                                            String attributeName,
                                            String actionPlace,
                                            CRTAction outAction) {
        for (Iterator i = AuthorSTATIC.wowOut.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptName)) {
                // found conceptName
                for (Iterator j = acon.attributeList.iterator(); j.hasNext();) {
                    WOWOutAttribute outat = (WOWOutAttribute) j.next();

                    if (outat.name.equals(attributeName)) {
                        for (Iterator k = outat.generateListItemList.iterator();
                             k.hasNext();) {
                            CRTGenerateListItem gList =
                                    (CRTGenerateListItem) k.next();

                            if (actionPlace.equals("trueActions")) {
                                gList.trueActions.actionList.add(outAction);
                            } else {
                                gList.falseActions.actionList.add(outAction);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *  ProcessHashtableGL reads the concepts out of the hashtable.
     */
    public void ProcessHashtableGL(Hashtable hashIn,
                                   CRTGenerateListItem genlistIn,
                                   String attributeName, String actionPlace,
                                   CRTAction origAction) {
        for (Iterator i = hashIn.entrySet().iterator(); i.hasNext();) {
            Map.Entry m = (Map.Entry) i.next();
            String sValue = (String) m.getValue();
            String sKey = "";
            CRTAction outAction = new CRTAction();
            CRTGenerateListItem glout = new CRTGenerateListItem();

            if (sValue.equals("") == false) {
                sValue = sValue + ")";
                sKey = (String) m.getKey();

                if (this.CheckConceptNameExist(sKey) == false) {
                    // concept does not exists
                    WOWOutAttribute atout = new WOWOutAttribute();
                    atout.name = attributeName; //oppassen met suitability
                    glout.location = genlistIn.location;
                    glout.propagating = genlistIn.propagating;
                    glout.requirement = genlistIn.requirement;
                    outAction.attributeName = origAction.attributeName;
                    outAction.conceptName = origAction.conceptName;
                    outAction.expression = sValue;

                    if (actionPlace.equals("trueActions")) {
                        glout.trueActions.actionList.add(outAction);
                    } else {
                        glout.falseActions.actionList.add(outAction);
                    }

                    atout.generateListItemList.add(glout);

                    WOWOutConcept outc = new WOWOutConcept();
                    outc.name = sKey;
                    outc.attributeList.add(atout);
                    AuthorSTATIC.wowOut.conceptList.add(outc);
                } else {
                    // concepts does exists
                    if (this.CheckAttributeName(sKey, attributeName) == false) {
                        // new attribute
                        WOWOutAttribute atout = new WOWOutAttribute();
                        atout.name = attributeName;
                        glout.location = genlistIn.location;
                        glout.propagating = genlistIn.propagating;
                        glout.requirement = genlistIn.requirement;
                        outAction.attributeName = origAction.attributeName;
                        outAction.conceptName = origAction.conceptName;
                        outAction.expression = sValue;

                        if (actionPlace.equals("trueActions")) {
                            glout.trueActions.actionList.add(outAction);
                        } else {
                            glout.falseActions.actionList.add(outAction);
                        }

                        atout.generateListItemList.add(glout);
                        this.AddAttributeToConcept(sKey, atout);
                    } else {
                        // attribute exists
                        if (this.CheckGenerateListItem(sKey, attributeName,
                                                       genlistIn.requirement) == false) {
                            glout.location = genlistIn.location;
                            glout.propagating = genlistIn.propagating;
                            glout.requirement = genlistIn.requirement;
                            outAction.attributeName = origAction.attributeName;
                            outAction.conceptName = origAction.conceptName;
                            outAction.expression = sValue;

                            if (actionPlace.equals("trueActions")) {
                                glout.trueActions.actionList.add(outAction);
                            } else {
                                glout.falseActions.actionList.add(outAction);
                            }

                            this.AddGenereateListItemToAttributeConcept(sKey,
                                                                        attributeName,
                                                                        glout);
                        } else {
                            outAction.attributeName = origAction.attributeName;
                            outAction.conceptName = origAction.conceptName;
                            outAction.expression = sValue;
                            this.AddActionToAttributeConcept(sKey,
                                                             attributeName,
                                                             actionPlace,
                                                             outAction);
                        }
                    }
                }
            }
        }
    }

    /**
     *  ProceessActioniNone reads the action, change all keywords and give them to the output functions.
     */
    public void ProcessActionNone(LinkedList crelGroupIn, CRTAction actionIn,
                                  String location, CRTGenerateListItem genIn,
                                  String actionPlace) {
        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();

            CRTGenerateListItem glout = new CRTGenerateListItem();
            WOWOutAttribute atout = new WOWOutAttribute();
            CRTAction outAction = new CRTAction();
            String sKey = this.ReplaceKeyword(location, crel);

            int n = sKey.lastIndexOf(".");
            String glConceptName = sKey.substring(0, n);
            String glAttributeName = sKey.substring(n + 1);

            if (this.CheckConceptNameExist(glConceptName) == false) {
                // concept does not exists
                atout.name = glAttributeName;
                glout.location = genIn.location;
                glout.propagating = genIn.propagating;

                glout.requirement = this.ReplaceKeyword(genIn.requirement, crel);
                glout.requirement = this.ReplaceVar(glout.requirement, crel);
                outAction.attributeName = this.ReplaceKeyword(
                                                  actionIn.attributeName, crel);
                outAction.conceptName = this.ReplaceKeyword(
                                                actionIn.conceptName, crel);

                outAction.expression = this.ReplaceVar(this.ReplaceKeyword(
                                                               actionIn.expression,
                                                               crel), crel);

                if (actionPlace.equals("trueActions")) {
                    glout.trueActions.actionList.add(outAction);
                } else {
                    glout.falseActions.actionList.add(outAction);
                }

                atout.generateListItemList.add(glout);

                WOWOutConcept outc = new WOWOutConcept();
                outc.name = glConceptName;
                outc.attributeList.add(atout);
                AuthorSTATIC.wowOut.conceptList.add(outc);
            } else {
                if (this.CheckAttributeName(glConceptName, glAttributeName) == false) {
                    // new attribute
                    atout = new WOWOutAttribute();
                    atout.name = glAttributeName;
                    glout.location = genIn.location;
                    glout.propagating = genIn.propagating;

                    glout.requirement = this.ReplaceKeyword(genIn.requirement,
                                                            crel);
                    glout.requirement = this.ReplaceVar(glout.requirement, crel);
                    outAction.attributeName = this.ReplaceKeyword(
                                                      actionIn.attributeName,
                                                      crel);
                    outAction.conceptName = this.ReplaceKeyword(
                                                    actionIn.conceptName, crel);
                    outAction.expression = this.ReplaceVar(this.ReplaceKeyword(
                                                                   actionIn.expression,
                                                                   crel), crel);

                    if (actionPlace.equals("trueActions")) {
                        glout.trueActions.actionList.add(outAction);
                    } else {
                        glout.falseActions.actionList.add(outAction);
                    }

                    atout.generateListItemList.add(glout);
                    this.AddAttributeToConcept(glConceptName, atout);
                } else {
                    // attribute exists
                    if (this.CheckGenerateListItem(glConceptName,
                                                   glAttributeName,
                                                   genIn.requirement) == false) {
                        glout.location = genIn.location;
                        glout.propagating = genIn.propagating;


                        //       glout.requirement = genIn.requirement;
                        glout.requirement = this.ReplaceKeyword(
                                                    genIn.requirement, crel);
                        glout.requirement = this.ReplaceVar(glout.requirement,
                                                            crel);
                        outAction.attributeName = this.ReplaceKeyword(
                                                          actionIn.attributeName,
                                                          crel);
                        outAction.conceptName = this.ReplaceKeyword(
                                                        actionIn.conceptName,
                                                        crel);
                        outAction.expression = this.ReplaceVar(
                                                       this.ReplaceKeyword(
                                                               actionIn.expression,
                                                               crel), crel);

                        if (actionPlace.equals("trueActions")) {
                            glout.trueActions.actionList.add(outAction);
                        } else {
                            glout.falseActions.actionList.add(outAction);
                        }

                        this.AddGenereateListItemToAttributeConcept(
                                glConceptName, glAttributeName, glout);
                    } else {
                        outAction.attributeName = this.ReplaceKeyword(
                                                          actionIn.attributeName,
                                                          crel);
                        outAction.conceptName = this.ReplaceKeyword(
                                                        actionIn.conceptName,
                                                        crel);
                        outAction.expression = this.ReplaceVar(
                                                       this.ReplaceKeyword(
                                                               actionIn.expression,
                                                               crel), crel);
                        this.AddActionToAttributeConcept(glConceptName,
                                                         glAttributeName,
                                                         actionPlace, outAction);
                    }
                }
            }
        }
    }

    /**
     * ProcessAction replaces the keywords.
     */
    public void ProcessAction(LinkedList crelGroupIn, CRTAction actionIn,
                              String location, CRTGenerateListItem genIn,
                              String actionPlace) {
        Hashtable hashCT = new Hashtable();
        FillHashtable(hashCT, crelGroupIn);

        RELConceptRelation drel;
        String actionExp = "";

        int n = location.lastIndexOf(".");
        String glConceptName = location.substring(0, n);
        String glAttributeName = location.substring(n + 1);
        String combination = actionIn.combination;

        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            drel = (RELConceptRelation) i.next();

            String hashValue = "";
            actionExp = this.ReplaceVar(ReplaceKeyword(actionIn.expression,
                                                       drel), drel);

            if (glConceptName.equals("___destination")) {
                hashValue = (String) hashCT.get(drel.destinationConceptName);
            } else {
                hashValue = (String) hashCT.get(drel.sourceConceptName);
            }

            if (hashValue.equals("")) {
                //first value
                hashValue = "((" + actionExp + ")";
            } else {
                // combination
                if (combination.equals("AND")) {
                    hashValue = hashValue + "&& (" + actionExp + ")";
                }

                if (combination.equals("OR")) {
                    hashValue = hashValue + "|| (" + actionExp + ")";
                }
            }

            if (glConceptName.equals("___destination")) {
                hashCT.remove(drel.destinationConceptName);
                hashCT.put(drel.destinationConceptName, hashValue);
            }

            if (glConceptName.equals("___source")) {
                hashCT.remove(drel.sourceConceptName);
                hashCT.put(drel.sourceConceptName, hashValue);
            }
        }

        this.ProcessHashtableGL(hashCT, genIn, glAttributeName, actionPlace,
                                actionIn);
    }

    /**
     * FindDivideS retrieves the divide information.
     */
    public int FindDivideS(LinkedList crelgroup, String crt, String conceptName) {
        for (Iterator k = crelgroup.iterator(); k.hasNext();) {
            RELConceptRelation relConcept = (RELConceptRelation) k.next();

            if (relConcept.sourceConceptName.equals(conceptName)) {
                LinkedList llInfo = (LinkedList) AuthorSTATIC.relwow.divideInformation.get(
                                            relConcept.destinationConceptName);

                for (Iterator i = llInfo.iterator(); i.hasNext();) {
                    ConceptDivideInfo dInfo = (ConceptDivideInfo) i.next();

                    if (crt.equals(dInfo.crtName)) {
                        return dInfo.destination;
                    } else if (crt.equals("tree") &&
                                   dInfo.crtName.equals("tree")) {
                        return dInfo.destination;
                    }
                }
            }
        }

        return 0;
    }

    /**
     * ReplaceDiv_S replaces the keyword into the correct number.
     */
    public void ReplaceDIV_S(LinkedList crelGroupIn, String location,
                             String actionPlace) {
        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();
            double value = 0;
            int number = -1;

            for (Iterator j = AuthorSTATIC.wowOut.conceptList.iterator();
                 j.hasNext();) {
                WOWOutConcept outConcept = (WOWOutConcept) j.next();

                for (Iterator k = outConcept.attributeList.iterator();
                     k.hasNext();) {
                    number = this.FindDivideS(crelGroupIn, crel.relationType,
                                              outConcept.name);

                    WOWOutAttribute outAtt = (WOWOutAttribute) k.next();

                    for (Iterator l = outAtt.generateListItemList.iterator();
                         l.hasNext();) {
                        CRTGenerateListItem glOut = (CRTGenerateListItem) l.next();

                        if (number != 0) {
                            value = 1 / (double) number;
                        }

                        for (Iterator m = glOut.trueActions.actionList.iterator();
                             m.hasNext();) {
                            CRTAction aOut = (CRTAction) m.next();

                            aOut.expression = aOut.expression.replaceAll(
                                                      "DIVIDE",
                                                      Double.toString(value));
                        } // close for m
                    } // close for l

                    for (Iterator l = outAtt.generateListItemList.iterator();
                         l.hasNext();) {
                        CRTGenerateListItem glOut = (CRTGenerateListItem) l.next();

                        if (number != 0) {
                            value = 1 / number;
                        }

                        for (Iterator m = glOut.trueActions.actionList.iterator();
                             m.hasNext();) {
                            CRTAction aOut = (CRTAction) m.next();
                            aOut.expression = aOut.expression.replaceAll(
                                                      "DIVIDE",
                                                      Double.toString(value));
                        } //close for m
                    } // close for l
                }
            } // close for k
        }
    }

    /**
     * ReplaceDiv_D not used anymore.
     */
    public void ReplaceDIV_D(LinkedList crelGroupIn, String location,
                             String actionPlace) {


        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();
            String sKey = this.ReplaceKeyword(location, crel);
            int n = sKey.lastIndexOf(".");
            String glConceptName = sKey.substring(0, n);
            String glAttributeName = sKey.substring(n + 1);
            double value = 0;

            for (Iterator j = AuthorSTATIC.wowOut.conceptList.iterator();
                 j.hasNext();) {
                WOWOutConcept outConcept = (WOWOutConcept) j.next();

                if (outConcept.name.equals(glConceptName)) {
                    for (Iterator k = outConcept.attributeList.iterator();
                         k.hasNext();) {
                        WOWOutAttribute outAtt = (WOWOutAttribute) k.next();

                        if (outAtt.name.equals(glAttributeName)) {
                            for (Iterator l = outAtt.generateListItemList.iterator();
                                 l.hasNext();) {
                                CRTGenerateListItem glOut =
                                        (CRTGenerateListItem) l.next();
                                int number = glOut.trueActions.actionList.size();

                                if (number != 0) {
                                    value = 1 / (double) number;
                                }

                                for (Iterator m = glOut.trueActions.actionList.iterator();
                                     m.hasNext();) {
                                    CRTAction aOut = (CRTAction) m.next();
                                    aOut.expression = aOut.expression.replaceAll(
                                                              "DIVIDE",
                                                              Double.toString(
                                                                      value));
                                } // close for m
                            } // close for l

                            for (Iterator l = outAtt.generateListItemList.iterator();
                                 l.hasNext();) {
                                CRTGenerateListItem glOut =
                                        (CRTGenerateListItem) l.next();
                                int number = glOut.falseActions.actionList.size();

                                if (number != 0) {
                                    value = 1 / number;
                                }

                                for (Iterator m = glOut.trueActions.actionList.iterator();
                                     m.hasNext();) {
                                    CRTAction aOut = (CRTAction) m.next();
                                    aOut.expression = aOut.expression.replaceAll(
                                                              "DIVIDE",
                                                              Double.toString(
                                                                      value));
                                } //close for m
                            } // close for l
                        } // close if
                    } // close for k
                }
            }
        }
    }

    /**
     * ProcessGenerateListItem addes the generatelist item from the crt to the output concepts.
     */
    public void ProcessGenerateListItem(LinkedList crelGroupIn,
                                        CRTGenerateListItem glistIn) {
        crelGroupIn.getFirst();

        String location = glistIn.location;
        CRTGenerateListItem outGenList = new CRTGenerateListItem();

        for (Iterator i = glistIn.trueActions.actionList.iterator();
             i.hasNext();) {
            CRTAction actionIn = (CRTAction) i.next();
            String actionPlace = "trueActions";

            if (actionIn.combination.equals("NONE")) {
                this.ProcessActionNone(crelGroupIn, actionIn, location, glistIn,
                                       actionPlace);
            } else if (actionIn.combination.equals("DIV_D")) {
                this.ProcessActionNone(crelGroupIn, actionIn, location, glistIn,
                                       actionPlace);
                this.ReplaceDIV_D(crelGroupIn, location, actionPlace);
            } else if (actionIn.combination.equals("DIV_S")) {
                this.ProcessActionNone(crelGroupIn, actionIn, location, glistIn,
                                       actionPlace);

                this.ReplaceDIV_S(crelGroupIn, location, actionPlace);
            } else {
                this.ProcessAction(crelGroupIn, actionIn, location, glistIn,
                                   actionPlace);
            }
        }

        for (Iterator i = outGenList.falseActions.actionList.iterator();
             i.hasNext();) {
            CRTAction actionIn = (CRTAction) i.next();
            String actionPlace = "falseActions";

            if (actionIn.combination.equals("NONE")) {
                this.ProcessActionNone(crelGroupIn, actionIn, location, glistIn,
                                       actionPlace);
            } else {
                this.ProcessAction(crelGroupIn, actionIn, location, glistIn,
                                   actionPlace);
            }
        }
    }

    /**
     * FillHashtable: Fills the hashtable with all the concepts used in crelGroupIn.
     */
    public void FillHashtable(Hashtable hashIn, LinkedList crelGroupIn) {
        // fills the hashIn table with all the concepts used in crelGroupIn
        RELConceptRelation drel;
        String hashKey;

        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            drel = (RELConceptRelation) i.next();
            hashKey = drel.destinationConceptName;

            if (hashIn.containsKey(hashKey) == false) {
                hashIn.put(hashKey, "");
            }

            hashKey = drel.sourceConceptName;

            if (hashIn.containsKey(hashKey) == false) {
                hashIn.put(hashKey, "");
            }
        }
    }

    /**
     * ProcessHashtable all concepts in hashtable
     */
    public void ProcessHashtable(Hashtable hashIn, String attributeName) {
        for (Iterator i = hashIn.entrySet().iterator(); i.hasNext();) {
            Map.Entry m = (Map.Entry) i.next();
            String sValue = (String) m.getValue();
            String sKey = "";
            CRTSetDefault outDefault = new CRTSetDefault();

            if (sValue.equals("") == false) {
                sValue = sValue + ")";
                sKey = (String) m.getKey();

                if (this.CheckConceptNameExist(sKey) == false) {
                    // concept does not exists
                    WOWOutAttribute atout = new WOWOutAttribute();
                    atout.name = attributeName; //oppassen met suitability
                    outDefault.setdefault = sValue;
                    atout.setDefaultList = outDefault;

                    WOWOutConcept outc = new WOWOutConcept();
                    outc.name = sKey;
                    outc.attributeList.add(atout);
                    AuthorSTATIC.wowOut.conceptList.add(outc);
                } else {
                    // concepts does exists
                    if (this.CheckAttributeName(sKey, attributeName) == false) {
                        // new attribute
                        WOWOutAttribute atout = new WOWOutAttribute();
                        atout.name = attributeName;
                        outDefault.setdefault = sValue;
                        atout.setDefaultList = outDefault;
                        this.AddAttributeToConcept(sKey, atout);
                    } else {
                        // attribute exists
                        this.AddAttributeToConceptAttribute(sKey, attributeName,sValue);
                    }
                }
            }
        }
    }

    /**
     *  ProcessSetDefault start function to set the default values.
     */
    public void ProcessSetDefault(LinkedList crelGroupIn,
                                  CRTSetDefault setDefault) {
        Hashtable hashCT = new Hashtable();
        FillHashtable(hashCT, crelGroupIn);

        RELConceptRelation drel = (RELConceptRelation) crelGroupIn.getFirst();

        // set attributeName and conceptName
        String location = setDefault.location;
        int n = location.lastIndexOf(".");
        String defConceptName = location.substring(0, n);

        String defAttributeName = location.substring(n + 1);

        String combination = setDefault.combination;
        new CRTSetDefault();
        String tdefault;

        for (Iterator i = crelGroupIn.iterator(); i.hasNext();) {
            drel = (RELConceptRelation) i.next();

            String hashValue = "";
            tdefault = this.ReplaceVar(ReplaceKeyword(setDefault.setdefault,
                                                      drel), drel);

            if (defConceptName.equals("___destination")) {
                hashValue = ((String) hashCT.get(drel.destinationConceptName)).trim();
            }

            if (defConceptName.equals("___parent")) {
                hashValue = ((String) hashCT.get(drel.destinationConceptName)).trim();
            }

            if (defConceptName.equals("___source")) {
                hashValue = ((String) hashCT.get(drel.sourceConceptName)).trim();
            }

            if (defConceptName.equals("___child")) {
                hashValue = ((String) hashCT.get(drel.sourceConceptName)).trim();
            }

            // combination check
            if (hashValue.equals("")) {
                //first value
                hashValue = "((" + tdefault + ")";
            } else {
                // combination
                if (combination.equals("AND")) {
                    hashValue = hashValue + "&& (" + tdefault + ")";
                }

                if (combination.equals("OR")) {
                    hashValue = hashValue + "|| (" + tdefault + ")";
                }
            }

            if (defConceptName.equals("___destination")) {
                hashCT.remove(drel.destinationConceptName);
                hashCT.put(drel.destinationConceptName, hashValue);
            }

            if (defConceptName.equals("___parent")) {
                hashCT.remove(drel.destinationConceptName);
                hashCT.put(drel.destinationConceptName, hashValue);
            }

            if (defConceptName.equals("___source")) {
                hashCT.remove(drel.sourceConceptName);
                hashCT.put(drel.sourceConceptName, hashValue);
            }

            if (defConceptName.equals("___child")) {
                hashCT.remove(drel.sourceConceptName);
                hashCT.put(drel.sourceConceptName, hashValue);
            }
        }

        ProcessHashtable(hashCT, defAttributeName);
    }

    /**
     *  processConceptRel main function to process conceptrels of a certain type.
     */
    public void ProcessConceptRel(LinkedList crelGroupIn) {
        creltype = null;

        CRTConceptRelationType dummie = null;
        RELConceptRelation cdummie = null;

        //creltype to the correct CRT
        for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
            dummie = (CRTConceptRelationType) i.next();
            cdummie = (RELConceptRelation) crelGroupIn.getFirst();

            if (dummie.name.equals(cdummie.relationType)) {
                creltype = dummie;
            }
        }

        if (cdummie.relationType.trim().equals("unary")) {
            creltype = dummie;
        }

        dummie = AuthorSTATIC.trel;
        cdummie = (RELConceptRelation) crelGroupIn.getFirst();

        if (cdummie.relationType.trim().equals("tree")) {
            creltype = dummie;
        }

        // get generatelistitem from crt
        if (creltype == null) {
            String error = "Error: The concept relation " +
                           cdummie.relationType + " does not exist!";
            JOptionPane.showConfirmDialog(null, error, "error",
                                          JOptionPane.ERROR_MESSAGE);
        }

        // process the setDefaults
        for (Iterator i = creltype.listItem.setDefaultList.iterator();
             i.hasNext();) {
            this.ProcessSetDefault(crelGroupIn, (CRTSetDefault) i.next());
        }

        for (Iterator i = creltype.listItem.generateListItemList.iterator();
             i.hasNext();) {
            // processGenerateListItem
            this.ProcessGenerateListItem(crelGroupIn,
                                         (CRTGenerateListItem) i.next());
        }
    }

    public void AddDivideInfo() {
    }

//Natasha saving generated files
    public void save(String filename, StringBuffer sb) {
        URL url = null;
        try {
            url = new URL("http://" + home.getHost() + ":" + home.getPort() + dirname +
                    "/servlet/authorservlets.SaveCourseFile?courseName=" + AuthorSTATIC.projectName);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);

            PrintWriter outb = new PrintWriter(uc.getOutputStream());
            outb.println("xmlFile=[" + filename + ".xhtml" + "]");
            outb.println(sb.toString());
            outb.flush();
            outb.close();
            uc.getResponseCode();
            if (uc.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(null, "There was an error while saving the concept structure.");
            }
        } catch (IOException except) {
            System.out.println("error!: " + except.toString());
        }
    }

    private WOWOutConcept getWOWOutConcept(String name) {
		WOWOutConcept tempConcept = null;
		for (Iterator i = AuthorSTATIC.conceptInfoList.iterator(); i.hasNext();) {
      	//for (Iterator i =  AuthorSTATIC.wowOut.conceptList.iterator(); i.hasNext();) {
        //for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
        tempConcept = (WOWOutConcept) i.next();
        if (tempConcept.name.equals(name)) break;
      }
      return tempConcept;
    }

    /**
     * ReplaceVar replaces the keyword "var:" with the label.
     */
    public String ReplaceVar(String sin) {
        String sout;
        //sout = sin.substring(4);
		sout = sin.replaceAll("var:", "");
        return sout;
    }

    /**
     * ReplaceKeyword change source,destination,parent, child with the correct concept.
     */
    public String ReplaceKeyword(String sin, String keyword) {
        String sout;
        sout = sin;
        sout = sout.replaceAll("concept", keyword);
        return sout;
    }

    public String ReplaceParent(String sin, String keyword) {
		//??????
        String sout;
        sout = sin;
        sout = sout.replaceAll("parent", keyword);
        return sout;
    }

    public String ReplaceBrackets(String sin, int keyword) {
        String sout;
        sout = sin;
        int begin = sout.indexOf("{");
        while (begin > -1) {
			int temp = keyword;
			int end = sout.indexOf("}");
			String regex = sout.substring(begin+1, end);
			if (regex.indexOf("concept.level-1") > -1) {
				temp--;
			}
			if (regex.indexOf("concept.level+1") > -1) {
				temp++;
			}
			//if (regex.indexOf("concept.level") > -1) sout = sout.replaceAll(regex, String.valueOf(keyword));
			sout = sout.substring(0, begin) + String.valueOf(temp) + sout.substring(end+1);
			begin = sout.indexOf("{");
		}
        return sout;
    }

    public String ReplaceConcepttype(String sin, String keyword) {
        String sout;
        sout = sin;
        sout = sout.replaceAll("concept.concepttype", "\""+keyword+"\"");
        return sout;
    }

    public String ReplacePreviousSibling(String sin, String keyword) {
        String sout;
        sout = sin;
        if (sout.indexOf("previousSibling") > 0) {
			DefaultMutableTreeNode node = findNode(keyword);
			DefaultMutableTreeNode previousSibling = node.getPreviousSibling();
			if (previousSibling == null) {
				sout = sout.replaceAll("previousSibling.knowledge", "0");
				sout = sout.replaceAll("previousSibling.visited", "0");
				sout = sout.replaceAll("previousSibling", "\"\"");
			}
			else {
				//???sout = sout.replaceAll("previousSibling.", AuthorSTATIC.projectName + "." + previousSibling.toString());
				sout = sout.replaceAll("previousSibling.knowledge", AuthorSTATIC.projectName + "." + previousSibling.toString() + ".knowledge");
				sout = sout.replaceAll("previousSibling.visited", AuthorSTATIC.projectName + "." + previousSibling.toString() + ".visited");
				sout = sout.replaceAll("previousSibling", "\"" + AuthorSTATIC.projectName + "." + previousSibling.toString() + "\"");
			}
		}
        return sout;
    }

    public String ReplaceFirstChild(String sin, String keyword) {
        String sout;
        sout = sin;
        if (sout.indexOf("firstChild") > 0) {
			if (keyword.equals("")) sout = sout.replaceAll("parent.firstChild", keyword);
			else {
				DefaultMutableTreeNode node = findNode(keyword);
				TreeNode firstChild = node.getFirstChild();
				sout = sout.replaceAll("parent.firstChild", "\"" + AuthorSTATIC.projectName + "." + firstChild.toString() + "\"");
			}
		}
        return sout;
    }

    public String ReplaceName(String sin, String keyword) {
        String sout;
        sout = sin;
        sout = sout.replaceAll("concept.name", "\"" + keyword + "\"");
        return sout;
    }

    /**
     * CheckConceptNameExist returns true if conceptname is already in the output conceptlist.
     */
    public boolean CheckConceptNameGAExist(String conceptname) {
        boolean result;
        result = false;

        for (Iterator i = GraphAuthor.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();

            if (acon.name.equals(conceptname)) {
                result = true;
            }
        }

        return result;
    }

    // level of the concept in the hierarchy
    private int treeNodeLevel(String name) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)GraphAuthor.sharedConceptTree.getModel().getRoot();
        return findRecNode(root, name).getLevel();
    }

	public boolean EvaluateExpression(String expression) {
		try {
			vn.spring.WOW.parser.UMVariableLocator umvl = new vn.spring.WOW.parser.UMVariableLocator();
        	vn.spring.WOW.parser.Parser parser = new vn.spring.WOW.parser.Parser(umvl);
        	Object result = parser.parse(expression);
        	return (result instanceof Boolean?((Boolean)result).booleanValue():((Float)result).floatValue() > 0);
        } catch (Exception e) {
            System.out.println("EvaluateExpression: Exception by evaluating expression: " +expression
                               +" Exception: " + e.toString() +" message: " +e.getMessage() );
            e.printStackTrace();
            return false;
        }
	}

    private void addGeneratedTreeInformation() {

        for (Iterator i = GraphAuthor.conceptList.iterator();
             i.hasNext();) {
            WOWOutConcept acon = (WOWOutConcept) i.next();
            if (acon.name.startsWith("generated")){
				DefaultMutableTreeNode node = findNode(AuthorSTATIC.projectName+"."+acon.name.trim());
        		if (node != null) {
            		acon.parent = ""; if (node.getParent()!=null) acon.parent = AuthorSTATIC.projectName + "." + node.getParent().toString().trim();
            		acon.firstchild = ""; if (node.getChildCount()!=0) acon.firstchild = AuthorSTATIC.projectName + "." + node.getFirstChild().toString().trim();
            		acon.nextsib = ""; if (node.getNextSibling()!=null) acon.nextsib = AuthorSTATIC.projectName + "." + node.getNextSibling().toString().trim();

				}
        		AuthorSTATIC.wowOut.conceptList.add(acon);
        	}
    	}
	}
//Natasha saving generated files

//Natasha:added methods
    /**
     * CheckConflictingStatements returns a boolean value indication if there conflicting statements in the strategies the author tries to apply
     * returns true in case there are and false in case there are no conflicting statements
     */

    public boolean CheckConflictingStatements(Vector v, StrategyIfStatement strategyIfStatement) {
        boolean conflict;
        conflict = false;
        //check for condition & for the attributeName
        for (int i=0; i<v.size(); i++) {
			StrategyIfStatement ifStatement = (StrategyIfStatement)v.elementAt(i);
			if (ifStatement.condition.equals(strategyIfStatement.condition) && ifStatement.thenStatement.selectAttribute.equals(strategyIfStatement.thenStatement.selectAttribute)) {
				conflict = true;
				break;
			}
		}
		return conflict;
    }

    /**
     * fillInHashtable returns selectHashtable or sortHashtable
     */

    public Hashtable fillInHashtable(Hashtable hashtableName, String parent, WOWOutAttribute tempAttr, String key) {
		if (hashtableName.containsKey(parent)) {
			Vector selectVector = (Vector)hashtableName.get(parent);
			int index = -1;
			int childrenNum = 0;
			Vector tmpVector = new Vector();
			for (int ii = 0; ii < selectVector.size(); ii++) {
				Vector attrVector = (Vector)selectVector.elementAt(ii);
				if (((String)attrVector.elementAt(1)).equals(tempAttr.setDefaultList.setdefault.trim())) {
					childrenNum = ((Integer)attrVector.elementAt(2)).intValue();
					childrenNum++;
					attrVector.set(2, childrenNum);
					index = ii;
					tmpVector = attrVector;
					break;
				}
			}
			if (index > -1) selectVector.setElementAt(tmpVector, index);
			else {
				Vector attrVector = new Vector();
				attrVector.add(key);
				attrVector.add(tempAttr.setDefaultList.setdefault.trim());
				attrVector.add(new Integer(1));
				selectVector.add(attrVector);
				hashtableName.put(parent, selectVector);
				addAttributesToParent(parent, tempAttr);
			}
		} else {
        	Vector attrVector = new Vector();
			attrVector.add(key);
			attrVector.add(tempAttr.setDefaultList.setdefault.trim());
			attrVector.add(new Integer(1));
			Vector selectVector = new Vector();
			selectVector.add(attrVector);
			hashtableName.put(parent, selectVector);
			addAttributesToParent(parent, tempAttr);
		}
		return hashtableName;
	}

    /**
     * addAttributesToParent adds attributes to a parent concept to indicate the "read" status of its children of a certain type
     */

	public void addAttributesToParent(String parent, WOWOutAttribute tempAttr) {
		WOWOutConcept parentConcept = null;
		parentConcept = getWOWOutConcept(parent);
		WOWOutAttribute parentAtt = null;
		try {
			parentAtt = parentConcept.getAttribute(tempAttr.setDefaultList.setdefault.trim().replaceAll("\"",""));
		} catch (Exception exc) {}
		if (parentAtt == null) {
			parentAtt = new WOWOutAttribute();
			parentAtt.name = tempAttr.setDefaultList.setdefault.trim().replaceAll("\"","");
			parentAtt.description = "indicates if children of a certain type are read";
			parentAtt.setDefaultList = new CRTSetDefault();
			parentAtt.setDefaultList.setdefault = "0";
			parentAtt.isChangeable = Boolean.FALSE;
			parentAtt.isPersistent = Boolean.TRUE;
			parentAtt.isSystem = Boolean.FALSE;
			parentAtt.type = "int";
			parentConcept.attributeList.add(parentAtt);
			WOWOutConcept outGraphAuthor = null;
			int index = -1;
			for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
				outGraphAuthor = (WOWOutConcept) i.next();
				if (parentConcept.name.replaceAll(AuthorSTATIC.projectName+".", "").equals(outGraphAuthor.name)) {
					index = GraphAuthor.conceptList.indexOf(outGraphAuthor);
					outGraphAuthor.attributeList.add(parentAtt);
					break;
				}
			}
			if (outGraphAuthor != null) GraphAuthor.conceptList.set(index, outGraphAuthor);
		}
	}

    /**
     * addActionsToChildren adds actions updating the attributes of a parent concept indicating the "read" status of its children of a certain type
     */

	public void addActionsToChildren(WOWOutConcept outconc, WOWOutAttribute checkAttr, int childrenNum, String parentKey, String childName) {
		CRTGenerateListItem gloutAccess = new CRTGenerateListItem();
		CRTAction outActionAccess = new CRTAction();
		gloutAccess.requirement = "true";
		outActionAccess.attributeName = checkAttr.setDefaultList.setdefault.replaceAll("\"","");
		outActionAccess.conceptName = outconc.parent;
		double value = 0;
		value = 1/(double)childrenNum;
		outActionAccess.expression = parentKey+"."+outActionAccess.attributeName+" + ("+Double.toString(value)+" * _"+AuthorSTATIC.projectName+"."+childName+".knowledge)";
		gloutAccess.trueActions.actionList.add(outActionAccess);
		WOWOutAttribute accessAtt = null;
		try {
			accessAtt = outconc.getAttribute("knowledge");
		} catch (Exception exc) {}
		if (accessAtt != null) {
			boolean result = false;
			for (Iterator l = accessAtt.generateListItemList.iterator(); l.hasNext();) {
				CRTGenerateListItem glItem = (CRTGenerateListItem) l.next();
				if (glItem.requirement.equals(gloutAccess.requirement)) {
					result = true;
					glItem.trueActions.actionList.addLast(outActionAccess);
				}
			}
			if (!result) accessAtt.generateListItemList.addLast(gloutAccess);
		}
	}
//end Natasha: added methods
}