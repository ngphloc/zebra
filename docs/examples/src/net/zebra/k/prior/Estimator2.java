/**
 * 
 */
package net.zebra.k.prior;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Loc Nguyen
 *
 */
public class Estimator2 {

	
	/**
	 * 
	 */
	protected final static double EPSILON = 0.1;//0.000001;
	
	
	/**
	 * 
	 */
	protected List<Double> sample;
	
	
	/**
	 * 
	 */
	protected PrintWriter printer = null;

	
	/**
	 * 
	 * @param printer
	 */
	public Estimator2(PrintWriter printer) {
		this.sample = new ArrayList<Double>();
		this.printer = printer;
	}

	
	/**
	 * 
	 */
	public Estimator2() {
		this(new PrintWriter(System.out, true));
	}
	
	
	/**
	 * 
	 * @param point sample point
	 */
	public void addSample(double point) {
		sample.add(point);
	}
	
	
	/**
	 * 
	 * @param points
	 */
	public void addSample(double[] points) {
		for (double point : points)
			sample.add(point);
	}

	
	/**
	 * 
	 * @param points
	 */
	public void addSample(Collection<Double> points) {
		for (double point : points)
			sample.add(point);
	}

	
	/**
	 * 
	 */
	public void emptySample() {
		sample.clear();
	}
	
	
	/**
	 * 
	 * @param aRange
	 * @param bRange
	 * @return estimates of parameters a and b
	 */
	public double[] estimate(int aRange, int bRange) {
		if (sample.size() == 0)
			return null;
		
		double dmin = Double.MAX_VALUE;
		double aEstimate = 1, bEstimate = 1;
		for (int a = 1; a <= aRange; a++) {
			for (int b = 1; b <= bRange; b++) {
				double L1= 1, L2=1;
				for (double point : sample) {
					point = point == 0? point + EPSILON : point;
					point = point == 1? point - EPSILON : point;
					L1 = L1*point;
					L2 = L2*(1-point);
				}
				
				double G1=0, G2=0;
				double sum1=0, sum2=0;
				for (int k = a; k<= a+b-1; k++) {
					sum1 += 1.0/(double)k;
				}
				G1 = Math.exp(-sample.size()*sum1);
				for (int k = b; k<= a+b-1; k++) {
					sum2 += 1.0/(double)k;
				}
				G2 = Math.exp(-sample.size()*sum2);
				
				double d1=0, d2=0, d=0;
				d1 = G1 - L1;
				d2 = G2 - L2;
				d = Math.sqrt(d1*d1 + d2*d2);
				
				if (d < dmin) {
					dmin = d;
					aEstimate = a;
					bEstimate = b;
				}
				
				out("a=" + a + ", b=" + b + 
						", L1=" + L1 + ", L2=" + L2 + 
						", G1=" + G1 + ", G2=" + G2 + 
						", d1=" + d1 + ", d2=" + d2 + ", d=" + d);
			}
		}
		
		out("a^=" + aEstimate + ", b^=" + bEstimate);
		return new double[] {aEstimate, bEstimate};
		
	}
	
	
	/**
	 * 
	 * @param o
	 */
	private void out(Object o) {
		printer.println(o);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PrintWriter printer = new PrintWriter(System.out, true);
		Estimator2 estimator = new Estimator2(printer);
		
		estimator.addSample(new double[] {1, 1, 1, 1, 0});
		printer.println("D=" + estimator.sample);
		estimator.estimate(4, 4);
		printer.println("****************");

		estimator.emptySample();
		estimator.addSample(new double[] {1, 1, 1, 0});
		printer.println("D=" + estimator.sample);
		estimator.estimate(4, 4);
		printer.println("****************");

		estimator.emptySample();
		estimator.addSample(new double[] {0});
		printer.println("D=" + estimator.sample);
		estimator.estimate(4, 4);
	}

}
