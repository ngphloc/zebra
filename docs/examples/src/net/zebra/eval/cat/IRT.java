/**
 * 
 */
package net.zebra.eval.cat;

/**
 * @author Loc Nguyen
 *
 */
public class IRT implements Function {

	
	/**
	 * Discriminatory parameter
	 */
	protected double a = 0;
	
	
	/**
	 * Difficult parameter
	 */
	protected double b = 0;
	
	
	/**
	 * Guessing parameter
	 */
	protected double  c = 0;
	
	
	/**
	 * 
	 * @param a discriminatory parameter
	 * @param b difficult parameter
	 * @param c guessing parameter
	 */
	public IRT(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	
	@Override
	public void setParameters(Object... params) {
		// TODO Auto-generated method stub
		
		double[] numbers = new double[] {0, 0, 0};
		for (int i = 0; i < params.length; i++) {
			try {
				numbers[i] = Double.parseDouble(params[i].toString());
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		this.a = numbers[0];
		this.b = numbers[1];
		this.c = numbers[2];
	}


	@Override
	public double evaluate(double x) {
		// TODO Auto-generated method stub
		return c + (1 - c) / (1 + Math.exp(a*b - a*x));
	}


	@Override
	public double derivative(double x) {
		double exp = Math.exp(a*b - a*x);
		return ((1 - c)*a*exp) / ((1 + exp)*(1 + exp));
	}
	
}
