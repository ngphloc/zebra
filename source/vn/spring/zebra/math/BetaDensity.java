/**
 * 
 */
package vn.spring.zebra.math;

import flanagan.analysis.Stat;
import flanagan.plot.PlotGraph;
import vn.spring.zebra.util.MathUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class BetaDensity {
	public final static double SAMPLE_SIZE = 100;
	public final static int MAX_BETA_PARAMS = 10;
	public final static int GRAPH_MAX_POINTS = 100;
	public final static double POSSITIVE_INFINITY = 1000;
	public final static double NEGATIVE_INFINITY = -1000;
	
	private double sum1=0, sum2=0;//available for Maximum Likelihood Expectation (MLE)
	private double a = 0 , b = 0;
	
	public BetaDensity(double a, double b) {
		this.a = a;
		this.b = b;
		repair();
	}
	public double value(double x) {
		return Stat.betaPDF(a, b, x);
	}
	public double getA() {return a;}
	public void   setA(double a) {this.a = a;}
	public void   incA() {a++;}
	public void   decA() {a--;}
	public double getB() {return b;}
	public void   setB(double b) {this.b = b;}
	public void   incB() {b++;}
	public void   decB() {b--;}
	
	public double getSum1()            {return sum1;}
	public void   setSum1(double sum1) {this.sum1 = sum1;}
	public void   incSum1()            {sum1++;}
	public void   resetSum1()          {sum1=0;}
	public double getSum2()            {return sum2;}
	public void   setSum2(double sum2) {this.sum2 = sum2;}
	public void   incSum2()            {sum2++;}
	public void   resetSum2()          {sum2=0;}

	public void repair() { 
		if(a == 0) a = 1;
		if(b == 0) b = 1;
	}
	
	public double expectation() {return a/(a+b);}
	
	public void updateParamsFromSum(int n) {
		if(sum1 == 0 && sum2 == 0) return;
		//sum1 /=n; sum2 /=n; //khong can chia cho n
		
    	int a = 1, b = 1;
    	double d1 = betaDigamma(a, b) - sum1;
    	double d2 = betaDigamma(b, a) - sum2;
    	double d = Math.sqrt(d1*d1 + d2*d2);
	    	
    	n = Math.min(MAX_BETA_PARAMS, n);
    	for(int temp_a = 1; temp_a <= n; temp_a++) {
        	for(int temp_b = 1; temp_b <= n; temp_b++) {
        		double f1 = betaDigamma(temp_b, temp_a);
        		double f2 = betaDigamma(temp_a, temp_b);
            	double temp_d1 = f1 - sum1;
            	double temp_d2 = f2 - sum2;
            	double temp_d = Math.sqrt(temp_d1*temp_d1 + temp_d2*temp_d2);
            	System.out.println("(a=" + temp_a + ", b=" + temp_b + 
            			"): f1=" + MathUtil.round(f1,2) + ", f2=" + MathUtil.round(f2,2) + 
            			", delta1=" + MathUtil.round(temp_d1,2) + ", delta2=" + MathUtil.round(temp_d2,2) + 
            			", delta" + MathUtil.round(temp_d,2));
        		if(temp_d < d) {
        			a = temp_a;
        			b = temp_b;
        			d = temp_d;
        		}
        	}
    	}
		this.a = a;  this.b = b; repair();
		resetSum1(); resetSum2();
		System.out.println("(optimal a=" + a + ", optimal b=" + b + ")");
	}
	public void drawGraph(String title) {
    	PlotGraph pg = getGraph();

    	pg.setGraphTitle(title);
    	//pg.setXaxisLegend("X"); pg.setYaxisLegend("Y");
    	pg.plot();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "beta(" + MathUtil.round(a, 2) + "," + MathUtil.round(b, 2) + ")";
	}
	public PlotGraph getGraph() {
		double[] xdata = new double[GRAPH_MAX_POINTS + 1];
		double[] ydata = new double[GRAPH_MAX_POINTS + 1];
		for(int i = 0; i <= GRAPH_MAX_POINTS; i++) {
			xdata[i] = (double)i/(double)GRAPH_MAX_POINTS;
			ydata[i] = value(xdata[i]);
		}
    	return new PlotGraph(xdata, ydata);
	}
	public final static double betaDigamma(int a, int b) {
		double sigma = 0;
		for(int k = 1; k <= b; k++) {
			sigma += Math.pow(-1, k)/(double)k*MathUtil.numberOfCombination(b,k).doubleValue();
		}
		return Math.pow(Math.E, a)*sigma;
	}
	public static void main(String[] ars) {
		BetaDensity beta = new BetaDensity(1, 1);
		beta.drawGraph("Graph of Beta(" + beta.getA() + ", " + beta.getB() + ")");
	}
}
