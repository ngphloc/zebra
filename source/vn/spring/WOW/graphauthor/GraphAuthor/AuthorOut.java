/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorOut.java 1.0, June 1, 2008
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

/**
 * This class saves the concept relations to an xml file.
 *
 */

public class AuthorOut {
    public StringBuffer authorOut = new StringBuffer();
    public URL home;
    public String fileName;

    public AuthorOut(URL base, String fName) {
        AddXMLHead();
        home = base;
        // adds the graph author extention to the filename
        // changed by @Loc Nguyen @  13-05-2008
        // only do this when there is no .gaf in the filename
        if (!fName.endsWith(".gaf")) {
          fileName = fName + ".gaf";
        }
        // end changed by @Loc Nguyen @  13-05-2008
    }

    public void AddXMLHead() {
        // no dtd
        authorOut.append("<?xml version=\"1.0\"?> \n");
        authorOut.append("<!DOCTYPE wow_authortool> \n");
        authorOut.append("<wow_authortool>\n");

        //Natasha
        authorOut.append("\t<applied_strategies>\n");
        for (int i = 0; i < AuthorSTATIC.strategyList.size(); i++) {
			authorOut.append("\t\t<applied_strategy>"+(String)AuthorSTATIC.strategyList.get(i)+"</applied_strategy>\n");
		}
        authorOut.append("\t</applied_strategies>\n");
        //end Natasha
        
        authorOut.append("\t<concept_relations>\n");
    
    }

public void AddUnRelation(Object cell){

  GraphAuthor.graph.getModel();

  DefaultGraphCell dcell = (DefaultGraphCell) cell;
  Map prop = dcell.getAttributes();

 Color backColor = GraphConstants.getBackground(prop);
if (backColor == Color.red) {
  Hashtable urel = (Hashtable) prop.get("unaryRelations");
  String source = cell.toString();
  String destination = cell.toString();

  for (Iterator j = urel.entrySet().iterator(); j.hasNext();) {
                 Map.Entry m = (Map.Entry) j.next();
                String label = (String) m.getValue();
                String relType = (String) m.getKey();
  authorOut.append("\t\t<concept_relation>\n");
       authorOut.append("\t\t\t<source_concept_name>" + source +
                        "</source_concept_name>\n");
       authorOut.append("\t\t\t<destination_concept_name>" + destination +
                        "</destination_concept_name>\n");
       authorOut.append("\t\t\t<relation_type>" + relType +
                        "</relation_type>\n");

       if (label == null) {
           authorOut.append("\t\t\t<relation_label>" + " " +
                            "</relation_label>\n");
       } else {
           authorOut.append("\t\t\t<relation_label>" + label +
                            "</relation_label>\n");
       }

        authorOut.append("\t\t</concept_relation>\n"); }
}
}

    public void AddRelation(Object oEdge) {
        GraphModel model = GraphAuthor.graph.getModel();
        DefaultEdge dedge = (DefaultEdge) oEdge;
        Map edgeAtt = dedge.getAttributes();
        String svalue = (String) edgeAtt.get("crt");
        String rLabel = (String) GraphConstants.getValue(edgeAtt);
//        GraphConstants.getValue(map)  string
        String source = "";
        String destination = "";

        if (model.getSource(oEdge) != null) {
            source = model.getParent(model.getSource(oEdge)).toString();
        }

        if (model.getTarget(oEdge) != null) {
            destination = model.getParent(model.getTarget(oEdge)).toString();
        }

        authorOut.append("\t\t<concept_relation>\n");
        authorOut.append("\t\t\t<source_concept_name>" + source +
                         "</source_concept_name>\n");
        authorOut.append("\t\t\t<destination_concept_name>" + destination +
                         "</destination_concept_name>\n");
        authorOut.append("\t\t\t<relation_type>" + svalue +
                         "</relation_type>\n");

        if (rLabel == null) {
            authorOut.append("\t\t\t<relation_label>" + " " +
                             "</relation_label>\n");
        } else {
            authorOut.append("\t\t\t<relation_label>" + rLabel +
                             "</relation_label>\n");
        }
        
        double weight = 0.0;
        if(rLabel!=null) {
        	rLabel = rLabel.trim();
	        if(rLabel.indexOf(".")!=-1 || rLabel.equals("0") || rLabel.equals("1")) {
	        	try {
	        		weight = Double.parseDouble(rLabel);
	        		if(weight < 0 || weight > 1) weight = 0.0;
	        	}
	        	catch (NumberFormatException e) {
	        		weight = 0.0;
	        	}
	        }
        }
        authorOut.append("\t\t\t<relation_weight>" + weight + "</relation_weight>\n");

        authorOut.append("\t\t</concept_relation>\n");
    }

    public void WriteConceptInfo() {
        authorOut.append("\t<concept_information>\n");

        for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
            WOWOutConcept aout = (WOWOutConcept) i.next();
            authorOut.append("\t\t<concept_info>\n");
            authorOut.append("\t\t\t<concept_name>" + aout.name +
                             "</concept_name>\n");
            authorOut.append("\t\t\t<concept_description>" +
                             encodeXML(aout.description) + "</concept_description>\n");
            authorOut.append("\t\t\t<concept_resource>" + aout.resource +
                             "</concept_resource>\n");
            authorOut.append("\t\t\t<concept_template>" + aout.template +
                             "</concept_template>\n");
            //added by @David @18-05-2008
            authorOut.append("\t\t\t<concept_type>" + aout.concepttype +
                             "</concept_type>\n");
            authorOut.append("\t\t\t<concept_title>" + encodeXML(aout.title) +
                             "</concept_title>\n");
            //end added by @David @18-05-2008

            // added by @Loc Nguyen @  29-04-2008
            authorOut.append("\t\t\t<concept_nocommit>" + Boolean.toString(aout.nocommit) +
                             "</concept_nocommit>\n");
            // end added by @Loc Nguyen @  29-04-2008
            // added by @Loc Nguyen @  10-06-2008
            authorOut.append("\t\t\t<concept_stable>" + aout.stable +"</concept_stable>\n");
            authorOut.append("\t\t\t<concept_stable_expr>" + encodeXML(aout.stable_expr) +"</concept_stable_expr>\n");
            // end added by @Loc Nguyen @  10-06-2008
            WriteReturnFragmentInfo(aout);
            WriteAttributeInfo(aout);
            authorOut.append("\t\t</concept_info>\n");
        }
        authorOut.append("\t</concept_information>\n");
    }

    /**
     * Writes the return fragment information of a concept to the gaf file.
     * @param concept
     * Added by @Loc Nguyen @  02-04-2008
     */
    private void WriteReturnFragmentInfo(WOWOutConcept concept) {
      boolean showability = false;
      WOWOutAttribute attribute = null;
      // check if there is a showability attribute, if not then no return fragments
      showability = false;
      for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
          WOWOutAttribute attr = (WOWOutAttribute) i.next();
          if (attr.name.equals("showability")) {
            showability = true;
            attribute = attr;
          }
      }

      try {
        if (showability) {
          if (attribute.casegroup != null) {
            Vector cv = null;
            if (!attribute.casegroup.getDefaultFragment().equals("")) {
              // there is a default fragment so cases exists
              authorOut.append("\t\t\t<returnfragment_information>\n");
              authorOut.append("\t\t\t\t<defaultfragment>" +
                               attribute.casegroup.getDefaultFragment().trim() +"</defaultfragment>\n");
              cv = attribute.casegroup.getCaseValues();
              // loop alle the cases of the casevector (cv)
              for (Iterator i = cv.iterator(); i.hasNext(); ) {
                try {
                  Case caseValue = (Case) i.next();
                  authorOut.append("\t\t\t\t<case_info>\n");
                  authorOut.append("\t\t\t\t\t<case_expression>" +
                                   encodeXML(caseValue.getValue()).trim() +"</case_expression>\n");
                  authorOut.append("\t\t\t\t\t<case_fragment>" +
                                   encodeXML(caseValue.getReturnfragment()).trim() +
                                   "</case_fragment>\n");
                  authorOut.append("\t\t\t\t</case_info>\n");
                }
                catch (Exception e) {
                  System.out.println(
                      "AuthorOut: WriteReturnFragmentInfo: some exception occured. " +e.toString());
                }
              }
              authorOut.append("\t\t\t</returnfragment_information>\n");
            }
          }
        }
      } catch (Exception e) {
        System.out.println("AuthorOut: WriteReturnFragmentInfo: some exception occured. " +
            e.toString());
      }
    }

    /**
     * Writes the attribute information of a concept to the gaf file.
     * @param concept
     * Added by @Loc Nguyen @  02-04-2008
     */
    private void WriteAttributeInfo(WOWOutConcept concept) {
      authorOut.append("\t\t\t<attribute_information>\n");

      for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
          WOWOutAttribute attr = (WOWOutAttribute) i.next();
          authorOut.append("\t\t\t\t<attribute_info>\n");
          authorOut.append("\t\t\t\t\t<attribute_name>" + attr.name.trim() +"</attribute_name>\n");
          authorOut.append("\t\t\t\t\t<attribute_description>" +attr.description + "</attribute_description>\n");
          authorOut.append("\t\t\t\t\t<attribute_type>" + attr.type.trim() +"</attribute_type>\n");
          authorOut.append("\t\t\t\t\t<attribute_default>" + encodeXML(attr.setDefaultList.setdefault).trim() +"</attribute_default>\n");
          authorOut.append("\t\t\t\t\t<attribute_isSystem>" + attr.isSystem.toString()  +"</attribute_isSystem>\n");
          authorOut.append("\t\t\t\t\t<attribute_isPersistent>" + attr.isPersistent.toString() +"</attribute_isPersistent>\n");
          authorOut.append("\t\t\t\t\t<attribute_isChangeable>" + attr.isChangeable.toString() +"</attribute_isChangeable>\n");
          authorOut.append("\t\t\t\t\t<attribute_Stable>" + attr.stable.trim() +"</attribute_Stable>\n");
          authorOut.append("\t\t\t\t\t<attribute_Stable_Expr>" + encodeXML(attr.stable_expr).trim() +"</attribute_Stable_Expr>\n");
          authorOut.append("\t\t\t\t</attribute_info>\n");
      }

      authorOut.append("\t\t\t</attribute_information>\n");
    }

    public void WriteRelations() {
        Object[] cells = GraphAuthor.graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName().equals("com.jgraph.graph.DefaultEdge")) {
                  this.AddRelation(views[i].getCell());
                }
                if (views[i].getCell().getClass().getName().equals("com.jgraph.graph.DefaultGraphCell")) {
                  this.AddUnRelation(views[i].getCell());
                }
            }
        }
        authorOut.append("\t</concept_relations>\n");
    }

	public void WriteConcepts() {
        Object[] cells = GraphAuthor.graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultGraphCell")) {
                    Map map = views[i].getAttributes();
                    Rectangle gB = GraphConstants.getBounds(map);
                    authorOut.append("\t\t\t\t<concept_coordinate>\n");
                    authorOut.append("\t\t\t\t\t<concept_name>" +
                                     views[i].getCell().toString() +
                                     "</concept_name>\n");
                    authorOut.append("\t\t\t\t\t<cx>" + gB.x + " </cx>\n");
                    authorOut.append("\t\t\t\t\t<cy>" + gB.y + " </cy>\n");
                    authorOut.append("\t\t\t\t\t<cw>" + gB.width + "</cw>\n");
                    authorOut.append("\t\t\t\t\t<ch>" + gB.height +
                                     "</ch>\n");
                    authorOut.append("\t\t\t\t</concept_coordinate>\n");
                }
            }
        }
    }

    public void WriteAllView() {
        authorOut.append("\t\t<view>\n");
        authorOut.append("\t\t\t<name>all</name>\n");
        authorOut.append("\t\t\t<concept_coordinates>\n");

        this.WriteConcepts();

        authorOut.append("\t\t\t</concept_coordinates>\n");
        authorOut.append("\t\t</view>\n");
    }

    public void outputChildren(DefaultMutableTreeNode element, int tabs) {
        String tabstring = "";
        int j = 0;

        while (j < tabs) {
            tabstring = tabstring + "\t";
            j++;
        }

        authorOut.append(tabstring + "<node_name>" + element.toString().trim() +"</node_name>\n");
        authorOut.append(tabstring + "<children>\n");

        for (Enumeration i = element.children(); i.hasMoreElements();) {
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) i.nextElement();

            this.outputChildren(tnode, (tabs + 1));
        }

        authorOut.append(tabstring + "</children>\n");
    }

    public void WriteTreeView() {
        authorOut.append("\t\t<view>\n");
        authorOut.append("\t\t\t<name>tree</name>\n");
        authorOut.append("\t\t\t<tree_structure>\n");

        TreeModel tmodel = GraphAuthor.sharedConceptTree.getModel();
        DefaultMutableTreeNode element = (DefaultMutableTreeNode) tmodel.getRoot();

        element.getChildCount();
        this.outputChildren(element, 4);

        authorOut.append("\t\t\t</tree_structure>\n");
        authorOut.append("\t\t</view>\n");
    }

    public void WriteViews() {
        // alleen standaard view
        authorOut.append("\t<views>\n");
        this.WriteAllView();
        this.WriteTreeView();
        authorOut.append("\t</views>\n");
    }

    public void WriteAuthorXML(boolean noOutput) {
        this.WriteRelations();
        this.WriteConceptInfo();
        this.WriteViews();
        authorOut.append("</wow_authortool>");
        this.SaveToServer(noOutput);

    }

    public void SaveToServer(boolean noOutput) {
        URL url = null;

        try {
            String path = home.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            String dirname = path.substring(0, index);

            if (dirname.equals("/graphAuthor")) {
                dirname = "";
            }

            url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                          dirname + "/SaveList");
/*            url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                          dirname + "/servlet/authorservlets.SaveFile?userName=" + AuthorSTATIC.authorName);
*/
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            uc.setRequestProperty("Filename", fileName);
            uc.setRequestProperty("Author", AuthorSTATIC.authorName);
            uc.setRequestProperty("Created", "");

            PrintWriter outb = new PrintWriter(uc.getOutputStream());
//            outb.println("xmlFile=[" + fileName + "]");
            outb.println(/*URLEncoder.encode(*/authorOut.toString()/*,"UTF-8")*/);
            outb.close();
            uc.getResponseCode();
            if ((uc.getResponseCode() == 200) && (noOutput == false)) {
            	JOptionPane.showMessageDialog(null, "Graph has been saved successfully into the author format!");
            } else {
              if (noOutput == false)
            	  JOptionPane.showMessageDialog(null,
                                    "An error occured while saving the Graph: "+uc.getResponseMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException except) {
        	String error = "AuthorOut: SaveToServer: error!: " + except.toString();
      	  JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(error);
        } catch (Exception except) {
        	String error = "AuthorOut: SaveToServer: error!: " + except.toString();
        	  JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(error);
        }

    }

    /**
     * Replaces some characters with the XML equivalents.
     * @param tag
     * @return the cleaned tag
     * written by BlackOak
     * Changed by @Bart
     */
    public String encodeXML(String tag) {
        if (tag == null || tag.equals("")) {
            return "";
        }
        int length = tag.length();
        StringBuffer encodedTag = new StringBuffer(2 * length);
        try {

          for (int i = 0; i < length; i++) {
            char c = tag.charAt(i);

            if (c == '<') {
              encodedTag.append("&lt;");
            }
            else if (c == '>') {
              encodedTag.append("&gt;");
            }
            else if (c == '&') {
              encodedTag.append("&amp;");
            }
            else if (c == '"') {
              encodedTag.append("&quot;");
            }
            // else if (c==' ')
            //   encodedTag.append("&nbsp;");
            // plus sign in utf-8
//            else if (c == '+') {
//              encodedTag.append("%2b");
//            }
            else {
              encodedTag.append(c);
            }
          }
        } catch (Exception e) {
          System.out.println("AuthorOut: encodeXML: exception: " +e.toString() );
        }

        return encodedTag.toString();
    }

}