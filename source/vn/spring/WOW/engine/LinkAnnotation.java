/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LinkAnnotation.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.parser.*;

import java.util.*;

/**
 * This class can be used for adaptive annotation of links,icons..... Modified by Loc Nguyen 2008
 */

public class LinkAnnotation {
    LinkedList links = new LinkedList();
    public LinkAnnotation() {}

    public void addLinkInfo(String linkclass, String expr, String view) {
        LinkInfo info = new LinkInfo(linkclass, expr, view);
        links.add(info);
    }

    public String getLinkAnnotation(String conceptname, Profile profile, String cview) {
        Parser parser = WOWStatic.createUMParser(profile);
        for (int i=0;i<links.size();i++) {
            LinkInfo info = (LinkInfo)links.get(i);
            String appname = WOWStatic.appNameFromConcept(conceptname);
            String expr = WOWStatic.fixExpr(info.expr, appname, conceptname.substring(appname.length()+1));
            Object value = null;
            try {value = parser.parse(expr);} catch (ParserException e) {System.out.println(e);}
            if (value instanceof Boolean) {
                boolean b = (((Boolean)value).booleanValue()) && ((info.view.size() == 0) || (info.view.contains(cview)));
                if (b) return info.linkclass;
            }
        }
        return "unconditional";
    }

    private class LinkInfo {
        String linkclass;
        String expr;
        LinkedList view;
        public LinkInfo(String linkclass, String expr, String sview) {
            this.linkclass = linkclass;
            this.expr = expr;
            String[] views = sview.split(",");
            view = new LinkedList();
            for (int i=0;i<views.length;i++) if (!views[i].trim().equals("")) view.add(views[i].trim().toUpperCase());
        }
        public LinkInfo() {}
    }
}
