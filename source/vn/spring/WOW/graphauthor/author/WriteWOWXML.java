/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WriteWOWXML.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


/**
 * WriteWOWXML; writes the output concepts to an xml file.
 *
 */
public class WriteWOWXML {
    public  static WOWOutConceptList WowOut;
    public  FileWriter fw;
    public  CRTConceptRelationType CRT;
    private StringBuffer aout;
    public  String projectN = "test1234";
    public  Vector exprVector = new Vector();
    public  Vector pageVector = new Vector();
    private WOWOutConceptList outConceptList = null;

    public WriteWOWXML(WOWOutConceptList WOWOut, URL home, String fileName,
                       String projectName, boolean noOutput) {
        this.projectN = projectName;
        outConceptList = WOWOut;

        aout = new StringBuffer();

        // process the assigned resources

        WriteXMLHeader();
        WriteXMLConceptList(WOWOut);

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

            PrintWriter out = new PrintWriter(uc.getOutputStream());
            StringBuffer sbenc = new StringBuffer(/*URLEncoder.encode(*/aout.toString()/*,"UTF-8")*/);
//            out.println("xmlFile=" + "[" + fileName + "]");
            out.println(sbenc.toString());

            out.flush();
            out.close();
            uc.getRequestMethod();

            if ((uc.getResponseCode() == 200) && (noOutput == false)) {
            	JOptionPane.showMessageDialog(null,
                                    "Graph has been saved successfully into the WOW format!");
            } else {
                if (noOutput == false) {
                	JOptionPane.showMessageDialog(null,
                                        "An error occured while saving the Graph",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException except) {
            System.out.println("error!: " + except.toString());
        }
    }

    private void WriteXMLHeader() {
        aout.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<!DOCTYPE conceptList SYSTEM \"../generatelist4.dtd\">\n\n");
    }

    private void WriteXMLConceptList(WOWOutConceptList outclist) {
        aout.append("<conceptList>\n");
        aout.append("<name>");

        aout.append(this.projectN.trim());
        aout.append("</name>\n");

        for (Iterator i = outclist.conceptList.iterator(); i.hasNext();) {
            WOWOutConcept outc = (WOWOutConcept) i.next();
            this.WriteConcept(outc);
        }

        aout.append("</conceptList>");
    }

    private void WriteAction(CRTAction outa) {
        aout.append("       <action>\n");
        aout.append("        <conceptName>" + encodeXML(outa.conceptName.trim()) +
                    "</conceptName>\n");
        aout.append("        <attributeName>" +
                    encodeXML(outa.attributeName.trim()) + "</attributeName>\n");
        aout.append("        <expression>" + encodeXML(outa.expression.trim()) +
                    "</expression>\n");
        aout.append("       </action>\n");
    }

    private void WriteGenerateListItem(CRTGenerateListItem outgl) {
        aout.append("    <generateListItem isPropagating=\"" +
                    outgl.propagating + "\" >\n");

        aout.append("      <requirement>" + encodeXML(outgl.requirement.trim()) +
                    "</requirement>\n");
        aout.append("      <trueActions>\n");

        for (Iterator i = outgl.trueActions.actionList.iterator(); i.hasNext();) {
            CRTAction outta = (CRTAction) i.next();
            this.WriteAction(outta);
        }

        aout.append("      </trueActions>\n");

        if (!outgl.falseActions.actionList.isEmpty()) {
            aout.append("      <falseActions>\n");

            for (Iterator i = outgl.falseActions.actionList.iterator();
                 i.hasNext();) {
                CRTAction outfa = (CRTAction) i.next();
                this.WriteAction(outfa);
            }

            aout.append("      </falseActions>\n");
        }

        aout.append("    </generateListItem>\n");
    }

    private void WriteSetDefault(CRTSetDefault insd) {
        aout.append("   <default>" + this.encodeXML(insd.setdefault.trim()) +
                    "</default>\n");
    }

    /**
     *
     * @param outa
     * Changed by @Loc Nguyen @  03-04-2008
     */
    private void WriteAttribute(WOWOutAttribute outa) {

      aout.append("  <attribute name=\"" + outa.name.trim() + "\" type=\"" +outa.type + "\" isPersistent=\"" + outa.isPersistent +
                  "\" isSystem=\"" + outa.isSystem + "\" isChangeable=\"" +outa.isChangeable + "\">\n");
      aout.append("   <description>" + encodeXML(outa.description.trim()) +"</description>\n");
      this.WriteSetDefault(outa.setDefaultList);

      // added output for stability
      if (!outa.stable.equals("")) {
        aout.append("   <stable>" + encodeXML(outa.stable.trim()) +"</stable>\n");
        if (outa.stable.equals("freeze")) {
          aout.append("   <stable_expr>" + encodeXML(processReturnFragmentExpression(outa.stable_expr.trim())) +"</stable_expr>\n");
        }
      }

      // added output for casegroup
      if (outa.casegroup != null) {
        // check if default fragment is null
        if (!outa.casegroup.getDefaultFragment().equals("")) {
          aout.append("   <casegroup>\n");
          aout.append("     <defaultfragment>" +encodeXML(outa.casegroup.getDefaultFragment().trim()) +"</defaultfragment>\n");

          outa.casegroup.getCaseValues();
          // loop alle the cases
//          for (Iterator i = cv.iterator(); i.hasNext(); ) {
          for (int i=0; i<pageVector.size(); i++) {
            Case caseValue = (Case) pageVector.get(i);
            aout.append("       <casevalue>\n");
            // get the case from the pageVector, this contains the automatically generated nr
//            Case tempCase = (Case) pageVector.get(i);
//            aout.append("         <value>" +tempCase.getValue() +"</value>\n");
            aout.append("         <value>" + encodeXML(caseValue.getValue().trim()) +
                             "</value>\n");
            aout.append("         <returnfragment>" +
                             encodeXML(caseValue.getReturnfragment().trim()) +
                             "</returnfragment>\n");
            aout.append("       </casevalue>\n");
          }
          aout.append("     </casegroup>\n");
        }
      }

      //   }

      for (Iterator i = outa.generateListItemList.iterator(); i.hasNext(); ) {
        CRTGenerateListItem outgl = (CRTGenerateListItem) i.next();
        this.WriteGenerateListItem(outgl);
      }

      aout.append("  </attribute>\n");
    }

    public String encodeXML(String tag) {
        if (tag == null) {
            return null;
        }

        int length = tag.length();
        StringBuffer encodedTag = new StringBuffer(2 * length);

        for (int i = 0; i < length; i++) {
            char c = tag.charAt(i);

            if (c == '<') {
                encodedTag.append("&lt;");
            } else if (c == '>') {
                encodedTag.append("&gt;");
            } else if (c == '&') {
                encodedTag.append("&amp;");
            } else if (c == '"') {
                encodedTag.append("&quot;");
            }
            // else if (c==' ')
            //   encodedTag.append("&nbsp;");
            // plus sign in utf-8
//            else if (c == '+') {
//                encodedTag.append("%2b");
//            }
            else {
                encodedTag.append(c);
            }
        }

        return encodedTag.toString();
    }

    private void WriteConcept(WOWOutConcept outc) {
        aout.append("\n<concept>\n");
        aout.append(" <name>" + outc.name.trim() + "</name>\n");
        aout.append(" <description>" + encodeXML(outc.description.trim())  +
                    "</description>\n");
        aout.append(" <resource>" + outc.resource.trim()  + "</resource> \n");
        // added by @Loc Nguyen @  29-04-2008
        if (outc.nocommit) {
          aout.append(" <nocommit>" + Boolean.toString(outc.nocommit)   +"</nocommit>\n");
        }
        // end added by Bart @ 29-04-2008
        // added by @Loc Nguyen @  10-06-2008
        //if (outc.stable != "") {
          if (outc.stable != null) {
          aout.append(" <stable>" + outc.stable.trim() +"</stable>\n");
          if (outc.stable.equals("freeze")) {
            aout.append(" <stable_expr>" + encodeXML(processReturnFragmentExpression(outc.stable_expr.trim())) +"</stable_expr>\n");
          }
        }
        // end added by Bart @ 10-06-2008
        //added by @David @18-05-2008
        aout.append(" <concepttype>" + encodeXML(outc.concepttype.trim()) +
                    "</concepttype>\n");
        aout.append(" <title>" + encodeXML(outc.title.trim())+
                    "</title>\n");
        if ((outc.firstchild!=null) && (outc.nextsib!=null) && (outc.parent!=null)) {
            aout.append(" <hierarchy>\n");
            aout.append("   <firstchild>" + encodeXML(outc.firstchild.trim()) + "</firstchild>\n");
            aout.append("   <nextsib>" + encodeXML(outc.nextsib.trim()) + "</nextsib>\n");
            aout.append("   <parent>" + encodeXML(outc.parent.trim()) + "</parent>\n");
            aout.append(" </hierarchy>\n");
        }
        //end added by @David @18-05-2008

        // fill Vectors with correct cases for returnfragment
        prepareReturnFragmentAddition(outc);
        for (Iterator i = outc.attributeList.iterator(); i.hasNext();) {
            WOWOutAttribute outa = (WOWOutAttribute) i.next();

            if (outa.name.trim().equals("access")) {
                // loop the exprVector
                Vector allexprs = new Vector();
                for (int j=0; j<exprVector.size(); j++) {
                  Case tempExprNr = (Case) exprVector.get(j);
                  CRTGenerateListItem generateListItem = new CRTGenerateListItem();
                  CRTAction action = new CRTAction();
                  action.conceptName = outc.name;
                  action.attributeName = "showability";
                  // set the nr
                  action.expression = tempExprNr.getReturnfragment();
                  // set the expression
                  generateListItem.requirement = processReturnFragmentExpression(tempExprNr.getValue());
                  allexprs.add(generateListItem.requirement);
                  // add the action
                  generateListItem.trueActions.actionList.add(action);
                  // add the generateListItem to the list of the attribute
                  outa.generateListItemList.add(generateListItem);
                }

                //added by @David @16-06-2005
                //added the code to reset to the default fragment
                if (allexprs.size()!=0) {
                    StringBuffer strallexprs = new StringBuffer("!(("+((String)allexprs.get(0)).trim());
                    for (int j=1;j<allexprs.size();j++) strallexprs.append(") || ("+allexprs.get(j));
                    strallexprs.append("))");
                    CRTGenerateListItem generateListItem = new CRTGenerateListItem();
                    CRTAction action = new CRTAction();
                    action.conceptName = outc.name;
                    action.attributeName = "showability";
                    action.expression = "0";
                    generateListItem.requirement = strallexprs.toString();
                    generateListItem.trueActions.actionList.add(action);
                    outa.generateListItemList.add(generateListItem);
                }
                //end added by @David @16-06-2005
            }

            this.WriteAttribute(outa);
        }
        aout.append(" </concept>\n");
    }

    public void testXML() {
    }

    public static void main(String[] args) {
    }

    /**
     * Fill the two vectors (exprVector and pageVector) with the return fragment information.
     * The concept has a casegroup which contains cases These cases are expression -> page combinations
     * These combinations have to be translated to expression -> nr and nr -> page combinations These
     * combinations are stored in the two vectors. Later the information out of these vectors is
     * used to generate the wow file.
     * @param outc the current concept that is processed
     * Added by @Loc Nguyen @  10-04-2008
     */
    private void prepareReturnFragmentAddition(WOWOutConcept concept) {
      Case tempCase = null;
      int autoNumber = 0;
      // contains the expr -> nr combination so value == expr and returnfragment == nr
      Case exprCase = null;
      // contains the nr -> page combination so value == nr and returnfragment == page
      Case pageCase = null;

      // clear the two vectors
      exprVector.clear();
      pageVector.clear();

      // find showability attribute of this concept
      for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
        WOWOutAttribute attr = (WOWOutAttribute) i.next();
        if (attr.name.equals("showability")) {
          // get the cases vector
          if (attr.casegroup != null) {
            Vector cv = attr.casegroup.getCaseValues();

            // loop the stored casevalues and add these to this vector
            for (int j=0; j<cv.size(); j++) {
              try {
                autoNumber = autoNumber + 1;
                tempCase = (Case) cv.get(j);
                exprCase = new Case();
                pageCase = new Case();
                exprCase.setValue(tempCase.getValue());
                exprCase.setReturnfragment(String.valueOf(autoNumber));
                pageCase.setValue(String.valueOf(autoNumber));
                pageCase.setReturnfragment(tempCase.getReturnfragment());
                exprVector.add(exprCase);
                pageVector.add(pageCase);
              } catch (Exception e) {
                // something went wrong
                System.out.println("WriteWOWXML: prepareReturnFragmentAddition: exception with adding the cases to the vectors");
              }
            }
          } // end if
        } // end if
      } // end for

    } // end prepareReturnFragmentAddition


    private String processReturnFragmentExpression(String expression) {
      // find the . in the string
      // check if the character before and after is an character, not a number
      // get the part before the . (concept name)
      // loop the concepts of the conceptlist
      // get from these concepts only the concept name, not the course name
      // compare this concept name with the earlier got concept name
      // if they are the same replace that first concept name with the coursename.conceptname part
      // do this for every .

      String result = "";
      String course = "";
      String concept = "";
      String attribute = "";
      String token = "";
      String conceptListName = "";
      boolean noNumber = false;

      StringTokenizer st = new StringTokenizer(expression);
      while (st.hasMoreTokens()) {
          token = st.nextToken();
          if (token.indexOf(".") != -1) {
            // token contains a .
            // check if it's a number
            try {
              Integer.valueOf(token);
            } catch (NumberFormatException nfe) {
              // is no number :)
              noNumber = true;
            }

            if (noNumber) {
              // split it at the .
              StringTokenizer dotTokens = new StringTokenizer(token,".");
              int length = dotTokens.countTokens();
              if (length == 2) {
                // concept.attribute
                concept = dotTokens.nextToken();
                attribute = dotTokens.nextToken();
              } else if (length == 3) {
                // course.concept.attribute
                course = dotTokens.nextToken();
                concept = dotTokens.nextToken();
                attribute = dotTokens.nextToken();
              } else {
                // error
                return expression;
              }

              boolean notFound = true;
              // loop the conceptlist
              for (Iterator i = outConceptList.conceptList.iterator(); i.hasNext();) {
                  WOWOutConcept outc = (WOWOutConcept) i.next();
                  String name = outc.name;
                  // check for .
                  int conceptDotIndex = name.indexOf(".");
                  if (conceptDotIndex != -1) {
                    // get concept
                    conceptListName = name.substring(conceptDotIndex+1);
                  } else {
                    conceptListName = name;
                  }
                  // compare the concept names
                  if (conceptListName.equals(concept)) {
                    // match found
                    if (course.equals("")) {
                      // replace
                      notFound = false;
                      result = result + " " + name +"." +attribute;
                    }
                  }
              }
              if (notFound) {
                result = result + " " + token;
              }
            } else {
              // it's a number
              result = result + " " + token;
            }
          } else {
            // contains no .
            result = result + " " +token;
          }
      }

      return result;
    }

}