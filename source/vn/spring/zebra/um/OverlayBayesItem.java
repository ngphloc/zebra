package vn.spring.zebra.um;

import java.util.*;
import java.awt.*;

import vn.spring.zebra.exceptions.ZebraException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface OverlayBayesItem {
    OverlayBayesItem 	query(Object item);
    
    String              getName();
    void                setName(String n);
    String[]            getAllNames();
    int                 getNumberValues();
    String[]            getValues();  //such as: "true", "false"
    int                 getObservedValue();//la chi muc thu may
    String[][]          getAllValues();
    Point               getPos();
    Rectangle           getBound();

    boolean             hasParent();
    boolean             hasObserved();
    OverlayBayesItem[] 	getParents();
    OverlayBayesItem    getParent(String itemName);
	double[]            getParentWeights() throws ZebraException;
	void                setParentWeights(double[] weights) throws ZebraException;
    OverlayBayesItem[] 	getChildren();
    OverlayBayesItem    getChild(String itemName);

    Vector<?>  getVariableProperties();
    void    setVariableProperties(Vector<?> prop);
    void    updatePositionFromProperty(String s);
    boolean isObserved();
    boolean isExplanation();
    boolean isCredalSet();
    void    setExplanation(boolean flag);
    void    setLocalCredalSet();
    void    setLocalCredalSet(int number_extreme_points);    
    void    setNoLocalCredalSet();
    void    setObservedValue(String value);
    void    clearObserved();


    double[] getFunctionValues();
    double[] getFunctionValues(int index);//credal
    Vector<?>   getFunctionProperties();
    int      numberExtremeDistributions();
    void     setFunctionProperties(Vector<?> prop);
    void     setFunctionValues(double[] fv);
    void     setFunctionValues(int iep, double[] fv);
    
    boolean isChanged();
    void    setChanged(boolean isChanged);
    String toString();
}
