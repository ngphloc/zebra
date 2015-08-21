/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * IconAnnotation.java 1.0, March 18, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import java.util.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.parser.*;
import vn.spring.WOW.WOWStatic;

//Modified by Loc Nguyen 2008
public class IconAnnotation {
    LinkedList icons = new LinkedList();
    public IconAnnotation() {}

    public void addIconInfo(String icon, String expr, String place, String view) {
        IconInfo info = new IconInfo(icon, expr, place, view);
        icons.add(info);
    }

    public LinkedList getIconAnnotation(String conceptname, Profile profile, String cview) {
        Parser parser = WOWStatic.createUMParser(profile);
        LinkedList result = new LinkedList();
        for (int i=0;i<icons.size();i++) {
            IconInfo info = (IconInfo)icons.get(i);
            String appname = WOWStatic.appNameFromConcept(conceptname);
            String expr = WOWStatic.fixExpr(info.expr, appname, conceptname.substring(appname.length()+1));
            Object value = null;
            try {value = parser.parse(expr);} catch (ParserException e) {System.out.println(e);}
            if (value instanceof Boolean) {
                boolean b = (((Boolean)value).booleanValue()) && ((info.view.size() == 0) || (info.view.contains(cview)));
                if (b) result.add(("front".equals(info.place)?"<front>"+info.icon:info.icon));
            }
        }
        return result;
    }

    private class IconInfo {
        String icon;
        String expr;
        String place;
        LinkedList view;
        public IconInfo(String icon, String expr, String place, String sview) {
            this.icon = icon;
            this.expr = expr;
            this.place = place;
            String[] views = sview.split(",");
            view = new LinkedList();
            for (int i=0;i<views.length;i++) if (!views[i].trim().equals("")) view.add(views[i].trim().toUpperCase());
        }
        public IconInfo() {}
    }
}
