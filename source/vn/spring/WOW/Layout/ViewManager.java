/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ViewManager.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import java.util.LinkedList;

/**
 * This class manages all registered views
 * Modified by Loc Nguyen 2009 December
 */

public class ViewManager{

  //List containing all registered views
    private LinkedList viewList=null;


  /**
   * Constructor
   */
  public ViewManager() {
    viewList = new LinkedList();
  }

  /**
   * Returns number of registered views
   */
  public int getViewCount(){
    return viewList.size();

  }


  /**
   * Adds new to the viewList
   */
  public synchronized void addView(View v){
    if (v == null) throw new NullPointerException("View object 'v' is null");
    //@Loc Nguyen modified 2010.01.28
    if (!viewList.contains(v) && getView(v.getViewName()) == null)  viewList.add(v);
    else System.out.println("ViewManager: addView Error: Attempt to add double view!");
  }

  /**
   * Removes the view from the viewList
   */
  public synchronized void removeView(View v){
    if (v == null) throw new NullPointerException("View object 'v' is null");
    viewList.remove(v);
  }

/**
 * returns view with name 'name'
 */
  public View getView(String name){

    View view=null;
    boolean found=false;

    synchronized(viewList) {
            int i=viewList.size()-1;
            while (i>=0 && found==false) {
                view=(View) viewList.get(i);
                if ( name.toUpperCase().equals(view.getViewName().toUpperCase()) ) found=true;
                i--;
                }
     }

     if(found==false) view=null;


    return view;
  }


  /**
   * returns the value of the view parameter(pName) from the paramsString
   * The params in paramsString are name=value pairs devided by & sign
   */
  public String getParam(String pName, String paramsString){

    int i,i2;
    String s="";

    try{

      if(paramsString.indexOf(pName)==-1) {
       //System.out.println("VM.getParam: param '"+pName+"' not found in paramsString '"+paramsString+"' !");
       return "";
      }

      i=paramsString.indexOf(pName)+pName.length()+1;
      //i2=paramsString.indexOf(";",i);
      i2=paramsString.indexOf("&",i);
      if (i2==-1) i2=paramsString.length();
      s=paramsString.substring(i,i2);

    }
    catch(Exception e) {System.out.println("VM.getParam: Exception! "+e.getMessage());}

    return s;

  }

  	//Added by Loc Nguyen 2009 December
  	public boolean isViewTypeExisting(String viewType) {
  		synchronized(viewList) {
  			int n = viewList.size();
  			for(int i = 0; i < n; i++) {
  				String type = ((View)viewList.get(i)).getViewType();
  				type = type.toUpperCase();
  				if(type.equals(viewType.toUpperCase())) return true;
  			}
  			return false;
  	     }
  	}

}