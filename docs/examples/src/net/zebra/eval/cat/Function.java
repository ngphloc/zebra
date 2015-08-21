package net.zebra.eval.cat;


/**
 * 
 * @author Loc Nguyen
 *
 */
public interface Function {

	
	/**
	 * 
	 * @param params
	 */
	void setParameters(Object...params);
	
	
	/**
	 * @param x
	 * @return evaluated value
	 */
	double evaluate(double x);
	
	
	/**
	 * 
	 * @param x
	 * @return derivative value given parameter x
	 */
	public double derivative(double x);
}
