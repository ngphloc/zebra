package vn.spring.zebra.um;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.awt.Rectangle;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.tree.DefaultMutableTreeNode;

import vn.spring.WOW.graphauthor.author.*;
import vn.spring.WOW.graphauthor.GraphAuthor.*;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.helperservice.AuthorService;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AuthorCourseModel {
	private ArrayList<String> filteredRelationTypes;
	
	protected String                    name;
    protected LinkedList<WOWOutConcept> conceptList;
    protected RELConceptRelations       rels;
    protected LinkedList<ConceptCoord>  conceptCoordList;
    protected DefaultMutableTreeNode    conceptTree;
    
    public String getName() {return name;}
    
    public LinkedList<WOWOutConcept> getConceptList() {
    	return conceptList;
    }
    public WOWOutConcept getConcept(String conceptname) {
    	if(conceptList == null) return null;
    	for(int i = 0; i < conceptList.size(); i++) {
    		if(conceptList.get(i).name.equals(conceptname)) return conceptList.get(i); 
    	}
    	return null;
    }
    public RELConceptRelations getRelations() {
    	return rels;
    }
    public LinkedList<ConceptCoord> getConceptCoordList() {
    	return conceptCoordList;
    }
    
	public static AuthorCourseModel loadFromAuthorFile(URL base, String author, String course, ArrayList<String> filteredRelationTypes) throws ZebraException, MalformedURLException {
		AuthorService authorService = new AuthorService(base, author);
		//if(!authorService.checkCourse(course)) throw new ZebraException("Invalid Course");
		URL url = authorService.getFileURL(course + ".gaf");
		
		DOMParser p = new DOMParser();
		try {
			p.parse(url.toString());
		}
		catch (IOException e) {
			throw new ZebraException("Error by IOException");
		}
		catch (SAXException e) {
			throw new ZebraException("Error by SAXException");
		}
		
        Document doc = p.getDocument();
        Node n = doc.getDocumentElement();
        n = n.getFirstChild();
        n = n.getNextSibling();

        AuthorCourseModel courseModel = new AuthorCourseModel(); 
        courseModel.name = course;
        if(filteredRelationTypes != null) courseModel.filteredRelationTypes.addAll(filteredRelationTypes);
        
        while (n != null) {
            if (n.getNodeName().compareTo("concept_relations") == 0) {
            	courseModel.readConceptRelations(n);
            }

            if (n.getNodeName().compareTo("views") == 0) {
            	courseModel.readViews(n);
            }

            if (n.getNodeName().compareTo("concept_information") == 0) {
            	courseModel.readConceptInformation(n);
            }

			//Natasha
            if (n.getNodeName().compareTo("applied_strategies") == 0) {
            	courseModel.readStrategiesList(n);
            }
			//end Natasha

            n = n.getNextSibling();
        }

        return courseModel;
    }
	
    private AuthorCourseModel() {
    	conceptList = new LinkedList<WOWOutConcept>();
    	rels = new RELConceptRelations();
    	conceptCoordList = new LinkedList<ConceptCoord>();
    	conceptTree = null;
    	filteredRelationTypes = new ArrayList<String>();
    }

	private void readConceptRelations(Node n) {
		n = n.getFirstChild();
        while (n != null) {
            if (n.getNodeName().toString().compareTo("concept_relation") == 0) {
                readConceptRelation(n);
            }
            n = n.getNextSibling();
        }
    }

	private void readConceptRelation(Node n) {
		Node nod = n;
		RELConceptRelation crel = new RELConceptRelation();
		nod = nod.getFirstChild();

		while (nod != null) {
			if (nod.getNodeName().equals("source_concept_name")) {
				try {
					crel.sourceConceptName = nod.getFirstChild().getNodeValue().trim();
				} catch (Exception e) {
					crel.sourceConceptName = "";
				}
			}

			if (nod.getNodeName().equals("destination_concept_name")) {
				try {
					crel.destinationConceptName = nod.getFirstChild().getNodeValue().trim();
				} catch (Exception e) {
					crel.destinationConceptName = "";
				}
			}

			if (nod.getNodeName().equals("relation_type")) {
				try {
					crel.relationType = nod.getFirstChild().getNodeValue().trim();
				} catch (Exception e) {
					crel.relationType = "";
				}
			}

			if (nod.getNodeName().equals("relation_label")) {
				crel.label = "";
				try {
					crel.label = nod.getFirstChild().getNodeValue().trim();
				} catch (Exception e) {}
			}

			if (nod.getNodeName().equals("relation_weight")) {
				crel.weight = 0.0;
				try {
					crel.weight = Double.parseDouble(nod.getFirstChild().getNodeValue().trim());
				} catch (Exception e) {}
			}

			nod = nod.getNextSibling();
		}
		if(filteredRelationTypes.size() == 0)
			rels.conceptRelationList.add(crel);
		else if(filteredRelationTypes.contains(crel.relationType)) {
			rels.conceptRelationList.add(crel);
		}
			
    }
    
	private void readViews(Node n) {
		String name = "";
		n = n.getFirstChild();
		try {
			while (n != null) {
				// printNode(n);
				if (n.getNodeName().equals("view")) {
					try {
						NodeList tempList = null;
						tempList = n.getChildNodes();
						for (int i=0; i<tempList.getLength(); i++) {
							Node tempNode = tempList.item(i);
							if (tempNode.getNodeName().trim().equals("name")) {
								name = tempNode.getFirstChild().getNodeValue().trim();
							}
						}
						// printNode(firstNode);
					} catch (Exception e) {
						name = "";
					}
					if (name.equals("all")) {
						readAllView(n);
					}

					if (name.equals("tree")) {
						readTreeView(n);
					}
				}

				n = n.getNextSibling();
			}
		} catch (Exception e) {
			System.out.println("AuthorIn: readViews: error exception:" +e.toString() );
			e.printStackTrace();
		}
	}
    
	private void readAllView(Node n) {
		n = n.getFirstChild().getNextSibling();
		n = n.getNextSibling();
		n = n.getNextSibling().getFirstChild().getNextSibling();

		while (n != null) {
			readConceptCoordinate(n);
			n = n.getNextSibling();
			n = n.getNextSibling();
		}
	}

    private void readConceptCoordinate(Node n) {
        Rectangle rt = new Rectangle(20, 20, 10, 10);
        n = n.getFirstChild();
        n = n.getNextSibling();

        String conceptName = n.getFirstChild().getNodeValue();
        n = n.getNextSibling();
        n = n.getNextSibling();
        rt.x = Integer.parseInt(n.getFirstChild().getNodeValue().trim());
        n = n.getNextSibling();
        n = n.getNextSibling();
        rt.y = Integer.parseInt(n.getFirstChild().getNodeValue().trim());
        n = n.getNextSibling();
        n = n.getNextSibling();
        rt.width = Integer.parseInt(n.getFirstChild().getNodeValue().trim());
        n = n.getNextSibling();
        n = n.getNextSibling();
        rt.height = Integer.parseInt(n.getFirstChild().getNodeValue().trim());

        ConceptCoord cc = new ConceptCoord();
        cc.name = conceptName;
        cc.rect = rt;
        conceptCoordList.add(cc);
    }

    private void readTreeView(Node n) {
        n = n.getFirstChild().getNextSibling();
        n = n.getNextSibling().getNextSibling();

        n = n.getFirstChild();
        n = n.getNextSibling();
        conceptTree = readTreeNode(n);
    }
    
	private DefaultMutableTreeNode readTreeNode(Node n) {
		String nodeName = n.getFirstChild().getNodeValue();
		
		DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(nodeName);
		n = n.getNextSibling();
		n = n.getNextSibling();
		n = n.getFirstChild();
		
		while (n != null) {
			n = n.getNextSibling();
            if (n != null) {
            	try {
            		if (n.getNodeName().trim().equals("node_name")) {
            			tnode.add(readTreeNode(n));
            		}
            	}
            	catch (Exception e) {
            		System.out.println("exception while reading the tree");
            	}
            }
		}

		return tnode;
    }
	
    private void readConceptInformation(Node n) {
        n = n.getFirstChild();

        while (n != null) {
            if (n.getNodeName().toString().compareTo("concept_info") == 0) {
                readConceptInfo(n);
            }

            n = n.getNextSibling();
        }
    }

    public void readConceptInfo(Node n) {
		WOWOutAttribute attr = null;
		WOWOutConcept ain = new WOWOutConcept();
		Vector cv = new Vector();
		Case caseValue = null;
		n = n.getFirstChild();
		
		String conceptName = "";
		String conceptDescription = "";
		String conceptResource = "";
		String conceptTemplate = "";
		//added by @David @18-05-2008
		String conceptType = "page";
		String conceptTitle = "";
		//end added by @David @18-05-2008
		String defaultFragment = "";
		// added by @Loc Nguyen @ 29-04-2008
		boolean nocommit = false;
		// end added
		// added by @Loc Nguyen @ 10-06-2008
		String conceptStable = "";
		String conceptStable_expr = "";
		// end added by @Loc Nguyen @ 10-06-2008
		
		try {
			while (n != null) {
				if (n.getNodeName().equals("concept_name")) {
					conceptName = n.getFirstChild().getNodeValue().trim();
				}
				else if (n.getNodeName().equals("concept_description")) {
					try {
						conceptDescription = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptDescription = "";
					}
				}
				else if (n.getNodeName().equals("concept_resource")) {
					try {
						conceptResource = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptResource = "";
					}
				}
				else if (n.getNodeName().equals("concept_template")) {
					try {
						conceptTemplate = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptTemplate = "";
					}
				}
				//added by @David @18-05-2008
				else if (n.getNodeName().equals("concept_type")) {
					try {
						conceptType = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptType = "page";
					}
				}
				else if (n.getNodeName().equals("concept_title")) {
					try {
						conceptTitle = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptTitle = conceptName;
					}
				}
				//end added by @David @18-05-2008
				// added by @Loc Nguyen @ 29-04-2008
				else if (n.getNodeName().equals("concept_nocommit")) {
					try {
						String tempNoCommit = n.getFirstChild().getNodeValue().trim();
						nocommit = Boolean.getBoolean(tempNoCommit);
					} catch (Exception e) {
						nocommit = false;
					}
				}
				// end added by @Loc Nguyen @ 29-04-2008
				// added by @Loc Nguyen @ 10-06-2008
				else if (n.getNodeName().equals("concept_stable")) {
					try {
						conceptStable = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptStable = "";
					}
				}
				else if (n.getNodeName().equals("concept_stable_expr")) {
					try {
						conceptStable_expr = n.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						conceptStable_expr = "";
					}
				}
				// end added by @Loc Nguyen @ 10-06-2008
				else if (n.getNodeName().equals("returnfragment_information")) {
					NodeList nodeList = n.getChildNodes();

					for (int i = 0; i < nodeList.getLength(); i++) {
						Node childNode = nodeList.item(i);
						if (childNode.getNodeName().equals("defaultfragment")) {
							try {
								defaultFragment = childNode.getFirstChild().getNodeValue().trim();
							} catch (Exception e) {
								defaultFragment = "";
							}
						}
						if (childNode.getNodeName().equals("case_info")) {
							caseValue = readCaseInfo(childNode);
							cv.add(caseValue);
						}
					}
				}
				else if (n.getNodeName().equals("attribute_information")) {
					NodeList nodeList = n.getChildNodes();

					for (int i = 0; i < nodeList.getLength(); i++) {
						Node childNode = nodeList.item(i);
						if (childNode.getNodeName().equals("attribute_info")) {
							attr = readAttributeInfo(childNode);
							ain.attributeList.add(attr);
						}
					}
				}

				n = n.getNextSibling();
			}
		}
		catch (Exception e) {
			System.out.println("AuthorIn: readConceptInfo: error reading extra information");
			System.out.println("Exception is: " +e.getMessage() );
		}

		//added by @David @20-07-04
		if (conceptTitle.equals("")) conceptTitle = conceptName;
		//end added by @David @20-07-04

		//find showability attribute
		for (Iterator i = ain.attributeList.iterator(); i.hasNext();) {
			WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
			if (tempAttr.name.equals("showability")) {
				// add the casegroup
				Vector tempCV = null;
				Case tempCase = null;
				if (tempAttr.casegroup == null) {
					tempAttr.casegroup = new CaseGroup();
				}
				tempAttr.casegroup.setDefaultFragment(defaultFragment);
				tempCV = tempAttr.casegroup.getCaseValues();
				// loop the stored casevalues and add these to this vector
				for (int j=0; j<cv.size(); j++) {
					tempCase = (Case) cv.get(j);
					tempCV.add(tempCase);
				}
			}
		}

		ain.name = conceptName.trim();
		ain.description = conceptDescription.trim();
		ain.resource = conceptResource.trim();
		ain.template = conceptTemplate.trim();
		//added by @David @18-05-2008
		ain.concepttype = conceptType.trim();
		ain.title = conceptTitle.trim();
		//end added by @David @18-05-2008
		// added by @Loc Nguyen @ 29-04-2008
		ain.nocommit = nocommit;
		// end added by @Loc Nguyen @ 29-04-2008
		// added by @Loc Nguyen @ 10-06-2008
		ain.stable = conceptStable;
		ain.stable_expr = conceptStable_expr;
		// end added by @Loc Nguyen @ 10-06-2008

		conceptList.add(ain);
    }
    
    private Case readCaseInfo(Node n) {
		Case caseValue = new Case();
		Node tempNode = null;
		NodeList childNodes = null;
		try {
			childNodes = n.getChildNodes();
		} catch (Exception e) {
			System.out.println("AuthorIn: readCaseInfo: exception: " +e.toString() );
		}
		try {
			for (int i = 0; i < childNodes.getLength(); i++) {
				tempNode = childNodes.item(i);
				if (tempNode.getNodeName().equals("case_expression")) {
					caseValue.setValue(tempNode.getFirstChild().getNodeValue().trim());
				}
				else if (tempNode.getNodeName().equals("case_fragment")) {
					caseValue.setReturnfragment(tempNode.getFirstChild().getNodeValue().trim());
				}
			}
		}
		catch (Exception e) {
			System.out.println("AuthorIn: readCaseInfo: error reading extra information");
		}

		// return the created attribute
		return caseValue;
    }

    private WOWOutAttribute readAttributeInfo(Node n) {
		WOWOutAttribute attr = new WOWOutAttribute();
		Node tempNode = null;
		NodeList childNodes = null;
		childNodes = n.getChildNodes();
		try {
			for (int i = 0; i < childNodes.getLength(); i++) {
				tempNode = childNodes.item(i);

				if (tempNode.getNodeName().equals("attribute_name")) {
					attr.name = tempNode.getFirstChild().getNodeValue().trim();
				}
				else if (tempNode.getNodeName().equals("attribute_description")) {
					try {
						attr.description  = tempNode.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						attr.description = "";
					}
				}
				else if (tempNode.getNodeName().equals("attribute_type")) {
					try {
						attr.type  = tempNode.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						attr.type = "";
					}
				}
				else if (tempNode.getNodeName().equals("attribute_default")) {
					attr.setDefaultList = new CRTSetDefault();
					try {
						attr.setDefaultList.setdefault = tempNode.getFirstChild().getNodeValue();
					} catch (Exception e) {
						attr.setDefaultList.setdefault = "";
					}
				}
				else if (tempNode.getNodeName().equals("attribute_isSystem")) {
					try {
						attr.isSystem = Boolean.valueOf(tempNode.getFirstChild().getNodeValue());
					} catch (Exception e) {
						attr.isSystem = Boolean.FALSE;
					}
				}
				else if (tempNode.getNodeName().equals("attribute_isPersistent")) {
					try {
						attr.isPersistent = Boolean.valueOf(tempNode.getFirstChild().getNodeValue());
					} catch (Exception e) {
						attr.isPersistent = Boolean.FALSE;
					}
				}
				else if (tempNode.getNodeName().equals("attribute_isChangeable")) {
					try {
						attr.isChangeable = Boolean.valueOf(tempNode.getFirstChild().getNodeValue());
					} catch (Exception e) {
						attr.isChangeable = Boolean.FALSE;
					}
				}
				else if (tempNode.getNodeName().equals("attribute_Stable")) {
					try {
						attr.stable  = tempNode.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						attr.stable = "";
					}
				}
				else if (tempNode.getNodeName().equals("attribute_Stable_Expr")) {
					try {
						attr.stable_expr  = tempNode.getFirstChild().getNodeValue().trim();
					} catch (Exception e) {
						attr.stable_expr = "";
					}
				}
			}
		} catch (Exception e) {
			System.out.println("AuthorIn: ReadAttributeInfo: error reading extra information");
		}

		// return the created attribute
		return attr;
    }
    
	private void readStrategiesList(Node n) {
		n = n.getFirstChild();
 		
		while (n != null) {
			if (n.getNodeName().toString().compareTo("applied_strategy") == 0) {
				AuthorSTATIC.strategyList.add(n.getFirstChild().getNodeValue().trim());
			}
			n = n.getNextSibling();
		}
	}
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
