/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * BallTreeConstructor.java
 * Copyright (C) 2007 University of Waikato, Hamilton, New Zealand
 */

package weka.core.neighboursearch.balltrees;

import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Abstract class for constructing a BallTree .
 * 
 * @author Ashraf M. Kibriya (amk14[at-the-rate]cs[dot]waikato[dot]ac[dot]nz)
 * @version $Revision: 1.1 $
 */
public abstract class BallTreeConstructor 
  implements OptionHandler, Serializable {
  
  /** The maximum number of instances allowed in a leaf. */
  protected int m_MaxInstancesInLeaf=40;
  
  /** Should a parent ball completely enclose the balls
   * of its two children, or only the points inside
   * its children. */
  protected boolean m_FullyContainChildBalls = false;
  
  /** The instances on which to build the tree. */
  protected Instances m_Instances;
  
  /** The distance function to use to build the tree. */
  protected DistanceFunction m_DistanceFunction;
  
  /** The number of internal and leaf nodes in the 
   * built tree. */
  protected int m_NumNodes;
  
  /** The number of leaf nodes in the built tree. */
  protected int m_NumLeaves;
  
  /** The depth of the built tree. */
  protected int m_MaxDepth;
  
  /** The master index array. */
  protected int[] m_InstList;
  
  /**
   * Creates a new instance of BallTreeConstructor.
   */
  public BallTreeConstructor() {
  }
  
  /**
   *  Builds the ball tree. 
   * @return The root node of the tree. 
   * @throws Exception If there is problem building
   * the tree.
   */
  public abstract BallNode buildTree() throws Exception;
  
  /**
   * Adds an instance to the ball tree. 
   * @param node The root node of the tree.
   * @param inst The instance to add to the tree.
   * @return The new master index array after 
   * adding the instance. 
   * @throws Exception If there is some problem adding
   * the given instance to the tree. 
   */
  public abstract int[] addInstance(BallNode node, Instance inst) 
      throws Exception;
  
  /**
   * Returns the tip text for this property.
   * 
   * @return tip text for this property suitable for
   * displaying in the explorer/experimenter gui.
   */
  public String maxInstancesInLeafTipText() {
    return "The maximum number of instances allowed in a leaf.";
  }
  
  /**
   * Returns the maximum number of instances allowed in a leaf.
   * @return The maximum number of instances allowed in a leaf.
   */
  public int getMaxInstancesInLeaf() {
    return m_MaxInstancesInLeaf;
  }
  
  /**
   * Sets the maximum number of instances allowed in a leaf.
   * @param num The maximum number of instances allowed in 
   * a leaf.
   * @throws Exception If the num is < 1. 
   */ 
  public void setMaxInstancesInLeaf(int num) throws Exception {
    if(num<1)
      throw new Exception("The maximum number of instances in a leaf must " +
                          "be >=1.");
    m_MaxInstancesInLeaf = num;
  }
  
  /**
   * Returns the tip text for this property.
   * 
   * @return tip text for this property suitable for
   * displaying in the explorer/experimenter gui.
   */
  public String containChildBallsTipText() {
    return "Whether to contain fully the child balls.";
  }
  
  /**
   * Gets whether if a parent ball should completely enclose
   * its two child balls.
   * @return true if parent ball is to enclose its child 
   * balls.
   */
  public boolean getContainChildBalls() {
    return m_FullyContainChildBalls;
  }
  
  /**
   * Sets whether if a parent ball should completely enclose
   * its two child balls.
   * @param containChildBalls Should be tree if parent ball
   * is to enclose its child balls.
   */
  public void setContainChildBalls(boolean containChildBalls) {
    m_FullyContainChildBalls = containChildBalls;
  }
  
  /**
   * Sets the instances on which the tree is to be built.
   * @param inst The instances on which to build the 
   * ball tree.
   */
  public void setInstances(Instances inst) {
    m_Instances = inst;
  }
  
  /**
   * Sets the master index array that points to 
   * instances in m_Instances, so that only this array
   * is manipulated, and m_Instances is left 
   * untouched.
   * @param instList The master index array.
   */
  public void setInstanceList(int[] instList) {
    m_InstList = instList;
  }
  
  /**
   * Sets the distance function to use to build the 
   * tree.
   * @param func The distance function.
   */
  public void setEuclideanDistanceFunction(EuclideanDistance func) {
    m_DistanceFunction = func;
  }
  
  /**
   * Returns the number of nodes (internal + leaf) 
   * in the built tree. 
   * @return The number of nodes in the tree. 
   */ 
  public int getNumNodes() {
    return m_NumNodes;
  }
  
  /**
   * Returns the number of leaves in the built tree.
   * @return The number of leaves in the tree.
   */
  public int getNumLeaves() {
    return m_NumLeaves;
  }
  
  /**
   * Returns the depth of the built tree. 
   * @return The depth of the tree. 
   */
  public int getMaxDepth() {
    return m_MaxDepth;
  }
  
  /**
   * Returns an enumeration describing the available options.
   * 
   * @return 		an enumeration of all the available options.
   */
  public Enumeration listOptions() {
    Vector newVector = new Vector();
    
    newVector.addElement(new Option(
	"\tSet maximum number of instances in a leaf node\n"
	+ "\t(default: 40)", 
	"N", 0, "-N <value>")); 
    
    newVector.addElement(new Option(
	"\tSet internal nodes' radius to the sum \n"
	+ "\tof the child balls radii. So that it \n" 
	+ "contains the child balls.", 
	"R", 0, "-R")); 

    return newVector.elements();
  }

  /**
   * Parses a given list of options.
   * 
   * @param options 	the list of options as an array of strings
   * @throws Exception 	if an option is not supported
   */
  public void setOptions(String[] options)
    throws Exception {
    
    String optionString = Utils.getOption('N', options);
    if(optionString.length() !=  0) {
      setMaxInstancesInLeaf(Integer.parseInt(optionString));
    }
    else {
      setMaxInstancesInLeaf(40);
    }
    
    setContainChildBalls(Utils.getFlag('R', options));
  }

  /**
   * Gets the current settings.
   * 
   * @return 		an array of strings suitable for passing to setOptions
   */
  public String[] getOptions() {
    Vector<String>	result;
    
    result = new Vector<String>();
    
    result.add("-N");
    result.add("" + getMaxInstancesInLeaf());
    
    if (getContainChildBalls())
      result.add("-R");
    
    return result.toArray(new String[result.size()]);
  }
}
