/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Window.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Layout;

import java.util.LinkedList;

public class Window {

  private String name=null;
  private LinkedList viewNames=null;
  private StringBuffer framesetStruct=null;
  private String wndOpts=null;
  private boolean secondary;

  /**
   * Constructor
   */
  public Window(String name,String sec,String wndOpts){

    this.name=name;
    this.secondary=Boolean.valueOf(sec).booleanValue();
    this.wndOpts=wndOpts;

    this.viewNames = new LinkedList();
    this.framesetStruct=new StringBuffer();

  }



  /**
   * Returns name of this window
   */
  public String getName(){

    return this.name;

  }

  /**
   * Returns true if this window is secondary window (is triggered by a secondary link)
   */
  public boolean isSecondary(){

    return this.secondary;

  }

  /**
   * Returns wndOpts of this window
   */
  public String getWndOpts(){

    return this.wndOpts;

  }


  /**
   * sets framesetStruct for this window
   */
  public void setBrsCode(StringBuffer framesetStruct){

    this.framesetStruct=framesetStruct;
  }

  /**
   * gets framesetStruct of this window
   */
  public StringBuffer getBrsCode(){

    return this.framesetStruct;
  }

  /**
   * Returns number of registered views
   */
  public int getViewCount(){
    return viewNames.size();

  }


  /**
   * Adds new name to the viewNames
   */
  public synchronized void addViewName(String v){
    if (v == null) throw new NullPointerException("View object 'v' is null");
    if (!viewNames.contains(v))  viewNames.add(v);
    else System.out.println("addView Error: Attempt to add double viewName to window!");
  }

}