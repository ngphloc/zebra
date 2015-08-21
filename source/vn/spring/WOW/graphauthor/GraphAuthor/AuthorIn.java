/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorIn.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import vn.spring.WOW.graphauthor.author.*;

import com.jgraph.graph.*;

import java.awt.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.*;

/**
 * This class to load the saved concept relations into memory.
 *
 */
public class AuthorIn {
    public URL home;
    public StringBuffer wowIn = new StringBuffer();
    public LinkedList conceptCoordList;
    public RELConceptRelations relwow;
    public String fileName;
    public DefaultMutableTreeNode treeIn = new DefaultMutableTreeNode("unnamed");
    public static final int TEXT_TYPE =      3;
    public String dirname;

    public AuthorIn(URL base, String fName) {
        home = base;
        conceptCoordList = new LinkedList();
        relwow = new RELConceptRelations();
        GraphAuthor.conceptList = new LinkedList();
        // fileName = fName - extention
        fileName = fName.replaceAll(".gaf","");
        fileName = fName;
        GraphAuthor.visListData.clear();
        GraphAuthor.filteredListData.clear();
        //Natasha
        AuthorSTATIC.strategyList = new Vector();
        //end Natasha
    }

    public void LoadFromServer() {
        DOMParser p = new DOMParser();

        try {
            String path = home.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            dirname = path.substring(0, index);

            if (dirname.equals("/GraphAuthor")) {
                dirname = "";
            }

            URL url = new URL("http://" + home.getHost() + ":" +
                              home.getPort() + dirname +
                              "/servlet/authorservlets.GetFile?fileName=" +
                              fileName +  "&userName=" + AuthorSTATIC.authorName );
            // added by Natalia Stash, 15-07-2008
            if (this.CheckCourseName(fileName)) JOptionPane.showMessageDialog((Component) null,
                                                  "You cannot open this file. The course name is already in use by another author.",
                                                  "error",
                                                  JOptionPane.OK_OPTION);
            p.parse(url.toString());
        } catch (Exception e) {
            System.out.println("AuthorIn: LoadFromServer: Parse error!  file: " + fileName);
        }

        Document doc = p.getDocument();
        Node n = doc.getDocumentElement();
        n = n.getFirstChild();
        n = n.getNextSibling();

        while (n != null) {
            if (n.getNodeName().compareTo("concept_relations") == 0) {
                 this.ReadConceptRelations(n);
            }

            if (n.getNodeName().compareTo("views") == 0) {
                this.ReadViews(n);
            }

            if (n.getNodeName().compareTo("concept_information") == 0) {
                this.ReadConceptInformation(n);
            }

			//Natasha
            if (n.getNodeName().compareTo("applied_strategies") == 0) {
                this.ReadStrategiesList(n);
            }
			//end Natasha

            n = n.getNextSibling();
        }

        listConceptAndAttributesofGraphAuthor();

        this.AddConcepts();
        this.AddRelations();
        this.AddVisListData();
    }



 public void AddVisListData() {
   for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
           CRTConceptRelationType crel = (CRTConceptRelationType) i.next();
           GraphAuthor.visListData.add(crel.name);
    }

 }

    public void AddConcepts() {
        for (Iterator i = this.conceptCoordList.iterator(); i.hasNext();) {
            ConceptCoord conceptc = (ConceptCoord) i.next();
            this.addConcept(conceptc.name, conceptc.rect);
        }
    }

    public DefaultGraphCell FindConcept(String concept) {
        concept = concept.trim();

        Object[] cells = GraphAuthor.graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultGraphCell")) {
                    if (views[i].getCell().toString().trim().equals(concept)) {
                        DefaultGraphCell df = (DefaultGraphCell) views[i].getCell();

                        return df;
                    }
                }
            }
        }

        return null;
    }

	public void AddRelations() {
        // alle relaties staan in authorstatic, de concepten zoeken en de de verbanden leggen
        listConceptRelations();
        for (Iterator i = relwow.conceptRelationList.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();

            if (crel.sourceConceptName.equals(crel.destinationConceptName)){

              DefaultGraphCell dcell = FindConcept(crel.sourceConceptName);
              Map prop = dcell.getAttributes();
              Hashtable uhash = new Hashtable();
              if (prop.containsKey("unaryRelations")) {
                uhash = (Hashtable) prop.get("unaryRelations");
              }
              uhash.put(crel.relationType, crel.label);
              prop.put("unaryRelations", uhash);
              Map propMap = new Hashtable();
              GraphConstants.setBackground(prop, Color.red);
              propMap.put(dcell, prop);
              GraphAuthor.graph.getModel().edit(null, propMap, null, null);
           } else {
              DefaultGraphCell sourceCell = FindConcept(crel.sourceConceptName);
              DefaultGraphCell destCell = FindConcept(crel.destinationConceptName);
              ConnectionSet cs = new ConnectionSet();
              DefaultEdge edge = new DefaultEdge();
              DefaultPort sp = new DefaultPort();
              sourceCell.add(sp);
              DefaultPort dp = new DefaultPort();
              destCell.add(dp);
              ArrowStyle aStyle;
              aStyle = this.getArrowStyle(crel.relationType);
              cs.connect(edge, sp, dp);

              Map map = GraphConstants.createMap();
              GraphConstants.setLineColor(map, aStyle.color);
              GraphConstants.setDashPattern(map, aStyle.lineStyle);
              map.put("crt", aStyle.crtname);
              // added by @Loc Nguyen @ 07-05-2008
              GraphConstants.setValue(map,crel.label);
              // end added by @Bart
              GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);


              Hashtable attributes = new Hashtable();
              attributes.put(edge, map);
              GraphAuthor.graph.getModel().insert(new Object[] {edge}, cs, null, attributes);
           }
       }
    }

    public ArrowStyle getArrowStyle(String relname) {
        relname = relname.trim();

        ArrowStyle aStyle = new ArrowStyle();
        aStyle.color = Color.black;
        aStyle.crtname = relname;

        for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
            CRTConceptRelationType crel = (CRTConceptRelationType) i.next();

            if (relname.equals(crel.name)) {
                if (crel.color.equals("black")) {
                    aStyle.color = Color.black;
                }

                if (crel.color.equals("blue")) {
                    aStyle.color = Color.blue;
                }

                if (crel.color.equals("cyan")) {
                    aStyle.color = Color.cyan;
                }

                if (crel.color.equals("gray")) {
                    aStyle.color = Color.gray;
                }

                if (crel.color.equals("green")) {
                    aStyle.color = Color.green;
                }

                if (crel.color.equals("magenta")) {
                    aStyle.color = Color.magenta;
                }

                if (crel.color.equals("red")) {
                    aStyle.color = Color.red;
                }

                if (crel.color.equals("yellow")) {
                    aStyle.color = Color.yellow;
                }

                if (crel.style.equals("full")) {
                    aStyle.lineStyle[0] = 1;
                    aStyle.lineStyle[1] = 0;
                }

                if (crel.style.equals("dashed")) {
                    aStyle.lineStyle[0] = 5;
                    aStyle.lineStyle[1] = 6;
                }

                if (crel.style.equals("dotted")) {
                    aStyle.lineStyle[0] = 1;
                    aStyle.lineStyle[1] = 1;
                }
            }
        }

        return aStyle;
    }

    /**
     * Reads the concept_info node from the gaf file and creates an concept
     * @param n ConceptInfo node
     * Changed by @Loc Nguyen @ 03-04-2008
     */
    public void ReadConceptInfo(Node n) {
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
                caseValue = ReadCaseInfo(childNode);
                cv.add(caseValue);
              }
            }
          }
          else if (n.getNodeName().equals("attribute_information")) {
            NodeList nodeList = n.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
              Node childNode = nodeList.item(i);
              if (childNode.getNodeName().equals("attribute_info")) {
                attr = ReadAttributeInfo(childNode);
                ain.attributeList.add(attr);
              }
            }
          }

          n = n.getNextSibling();
        }
      }
      catch (Exception e) {
        System.out.println("AuthorIn: ReadConceptInfo: error reading extra information");
        System.out.println("Exception is: " +e.getMessage() );
      }

      //added by @David @20-07-04
      if (conceptTitle.equals("")) conceptTitle = conceptName;
      //end added by @David @20-07-04

      // find showability attribute
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

      GraphAuthor.conceptList.add(ain);
    }

    /**
     * Reads the attribute information of a concept from the gaf file and returns an attribute object with the information.
     * @param n
     * @return attr
     * Added by @Loc Nguyen @ 03-04-2008
     */
    public WOWOutAttribute ReadAttributeInfo(Node n) {
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

    /**
     * Reads the case information of a return fragment of an attribute from the gaf file and returns an case object with the information.
     * @param n case_info Node
     * @return caseValue
     * Added by @Loc Nguyen @ 04-04-2008
     */
    public Case ReadCaseInfo(Node n) {
      Case caseValue = new Case();
      Node tempNode = null;
      NodeList childNodes = null;
      try {
        childNodes = n.getChildNodes();
      } catch (Exception e) {
        System.out.println("AuthorIn: ReadCaseInfo: exception: " +e.toString() );
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
        System.out.println("AuthorIn: ReadCaseInfo: error reading extra information");
      }

      // return the created attribute
      return caseValue;
    }


    public void ReadConceptInformation(Node n) {
        n = n.getFirstChild();

        while (n != null) {
            if (n.getNodeName().toString().compareTo("concept_info") == 0) {
                this.ReadConceptInfo(n);
            }

            n = n.getNextSibling();
        }
    }

    public void ReadViews(Node n) {
      String name = "";
      n = n.getFirstChild();
      try {
        while (n != null) {
//          printNode(n);
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
//              printNode(firstNode);
            } catch (Exception e) {
              name = "";
            }
            if (name.equals("all")) {
              this.ReadAllView(n);
            }

            if (name.equals("tree")) {
              this.ReadTreeView(n);
            }
          }

          n = n.getNextSibling();
        }
      } catch (Exception e) {
        System.out.println("AuthorIn: ReadViews: error exception:" +e.toString() );
        e.printStackTrace();
      }
    }

    public void ReadTreeView(Node n) {
        n = n.getFirstChild().getNextSibling();
        n = n.getNextSibling().getNextSibling();

        n = n.getFirstChild();
        n = n.getNextSibling();
        GraphAuthor.loadTree = this.ReadTreeNode(n);
    }

    public DefaultMutableTreeNode ReadTreeNode(Node n) {

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
                      tnode.add(ReadTreeNode(n));
                    }
                  }
                  catch (Exception e) {
                    System.out.println("exception while reading the tree");
                  }
                }
            }

            return tnode;
        }

    public void ReadAllView(Node n) {

        n = n.getFirstChild().getNextSibling();
        n = n.getNextSibling();
        n = n.getNextSibling().getFirstChild().getNextSibling();

        while (n != null) {
            this.ReadConceptCoordinate(n);
            n = n.getNextSibling();
            n = n.getNextSibling();
        }
    }

    public void ReadConceptCoordinate(Node n) {
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

    public void addConcept(String conceptName, Rectangle rt) {
        DefaultGraphCell vertex = new DefaultGraphCell(conceptName.trim());
        vertex.add(new DefaultPort());

        Map map = GraphConstants.createMap();
        GraphConstants.setBounds(map, rt);
        GraphConstants.setBorderColor(map, Color.magenta);
        GraphConstants.setBackground(map, Color.lightGray);
        GraphConstants.setOpaque(map, true);

        Hashtable attributes = new Hashtable();
        attributes.put(vertex, map);
        GraphAuthor.graph.getModel()
                 .insert(new Object[] { vertex }, null, null, attributes);
 }

    public void ReadConceptRelations(Node n) {
        n = n.getFirstChild();

        while (n != null) {
            if (n.getNodeName().toString().compareTo("concept_relation") == 0) {
                this.ReadConceptRelation(n);
            }

            n = n.getNextSibling();
        }
    }

    public void ReadConceptRelation(Node n) {

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
      relwow.conceptRelationList.add(crel);
    }

    private void listConceptAndAttributesofGraphAuthor() {
      for (Iterator i =  GraphAuthor.conceptList.iterator(); i.hasNext();) {
        WOWOutConcept tempConcept = (WOWOutConcept) i.next();
        for (Iterator j =  tempConcept.attributeList.iterator(); j.hasNext();) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) j.next();
          
          //Loc Nguyen add
          if(tempAttr == null) {}
        }
      }
    }

    private void listConceptRelations() {

      for (Iterator i =  this.relwow.conceptRelationList.iterator(); i.hasNext();) {
        RELConceptRelation tempRelation = (RELConceptRelation) i.next();
        //Loc Nguyen add
        if(tempRelation == null) {}
      }
    }
 public boolean CheckCourseName(String name){
   try {

       URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                         dirname +
                         "/servlet/authorservlets.CheckCourse?courseName=" +
                         name + "&userName=" + AuthorSTATIC.authorName);
       BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       String sFile = "";
       do {
        sFile = in.readLine();
        if (sFile.trim().equals("true")) {
          return true;
        }
         } while (sFile != null);
       in.close();

   } catch (Exception e) {
            }
   return false;
 }

 	//Natasha
 	public void ReadStrategiesList(Node n) {
 		n = n.getFirstChild();
 		
 		while (n != null) {
 			if (n.getNodeName().toString().compareTo("applied_strategy") == 0) {
 				AuthorSTATIC.strategyList.add(n.getFirstChild().getNodeValue().trim());
 			}
 			n = n.getNextSibling();
 		}
 	}
 	//end Natasha
}