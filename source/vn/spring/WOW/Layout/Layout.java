/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Layout.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Layout;

import java.util.Hashtable;
import java.util.Enumeration;
import vn.spring.WOW.WOWStatic;


public class Layout {

  private Hashtable wnds=null;
  private String name=null;
  private String trigger=null; //name of the window that updates/opens all other windows of the layout

  /**
   * Constructor
   */
  public Layout(String name,String trigger){

    this.name=name;
    this.trigger=trigger;
    this.wnds=new Hashtable();
  }

  /**
   * Returns name of this layout
   */
   public String getName(){
       return this.name;
   }

  /**
   * Returns trigger window of this layout
   */
  public Window getTriggerWnd(){

   return (Window) this.wnds.get(this.trigger);

  }

  /**
  * Returns window from this layout that has name 'wndName'
  */
  public Window getWnd(String wndName){

    Enumeration enumer=wnds.keys();
    while (enumer.hasMoreElements()) {
      String key = enumer.nextElement().toString();
      Window wnd = (Window) wnds.get(key);
      //
      if(wnd == null) {}
    }

    return (Window) this.wnds.get(wndName);
  }

  /**
  * Adds a window to this layout
  */
  public void addWnd(Window wnd){

    this.wnds.put(wnd.getName(),wnd);

  }

  /**
  * Generates Javascript update code for all windows of the layout(except trigger wnd)
  */
 public String genJSTriggerCode(){
   StringBuffer rtn=new StringBuffer("");
   getTriggerWnd().getName();

     rtn.append("\n<script language='JavaScript'>\n");
	 rtn.append("window.name='"+this.trigger+"';\n");

     Enumeration enumer=this.wnds.elements();
     while (enumer.hasMoreElements()) {
       Window w = (Window) enumer.nextElement();
       if(w!=getTriggerWnd()){
         if(!w.isSecondary()){
           rtn.append(w.getName()+"=window.open('"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?wndName="+w.getName()+"','"+ w.getName()+"', '"+w.getWndOpts()+"'); \n");
         }
         else{
           //rtn.append("if(!(typeof self."+w.getName()+" == 'undefined')){ \n");
           rtn.append("\n if(self."+w.getName()+"){ \n");
           rtn.append("if(!self."+w.getName()+".closed) {\n"+w.getName()+".location.replace('"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?wndName="+w.getName()+"');} \n");
           rtn.append("}");
         }
       }


     }
     rtn.append("</script>\n\n");

   return rtn.toString();

 }


}

