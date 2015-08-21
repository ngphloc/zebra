/**
 * 
 */
package vn.spring.zebra.math;

import flanagan.plot.PlotGraph;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class BetaPlotGraph extends PlotGraph {
    protected static final long serialVersionUID = 1L;  // serial version unique identifier
	
	public BetaPlotGraph(double[][] data){
		super(data);
	}

	public BetaPlotGraph(double[] xData, double[] yData){
    	super(xData, yData);
	}

}
