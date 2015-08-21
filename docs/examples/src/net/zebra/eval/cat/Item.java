/**
 * 
 */
package net.zebra.eval.cat;

/**
 * @author Loc Nguyen
 *
 */
public class Item {

	
	/**
	 * Item identity
	 */
	protected int id = -1;
	
	
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
	 * The number of questions
	 */
	protected int questions = 0;
	
	
	/**
	 * 
	 */
	protected Function func = null;
	
	
	/**
	 * 
	 * @param a discriminatory parameter
	 * @param b difficult parameter
	 * @param c guessing parameter
	 */
	public Item(double a, double b, double c) {
		this.id = -1;
		this.a = a;
		this.b = b;
		this.c = c;
		this.func = new IRT(a, b, c);
	}
	
	
	/**
	 * 
	 * @param a
	 * @param b
	 */
	public Item(double a, double b) {
		this(a, b, 0);
	}

	
	/**
	 * 
	 * @return item identity
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * 
	 * @return discriminatory parameter
	 */
	public double getDiscriminatory() {
		return a;
	}
	
	
	/**
	 * 
	 * @param a discriminatory parameter
	 */
	public void setDiscriminatory(double a) {
		this.a = a;
	}

	
	/**
	 * 
	 * @return difficult parameter
	 */
	public double getDifficult() {
		return b;
	}

	
	/**
	 * 
	 * @param b difficult parameter
	 */
	public void setDifficult(double b) {
		this.b = b;
	}

	
	/**
	 * 
	 * @return guessing parameter
	 */
	public double getGuessing() {
		return c;
	}

	
	/**
	 * 
	 * @param c guessing parameter
	 */
	public void setGuessing(double c) {
		this.c = c;
	}

	
	/**
	 * 
	 * @return number of questions
	 */
	public int getQuestions() {
		return questions;
	}
	
	
	/**
	 * 
	 * @param questions the number of questions
	 */
	public void setQuestions(int questions) {
		this.questions = questions;
	}
	
	
	/**
	 * 
	 * @param x
	 * @return evaluated value
	 */
	public double evaluate(double x) {
		// TODO Auto-generated method stub
		return c + (1 - c) / (1 + Math.exp(a*b - a*x));
	}

	
	/**
	 * 
	 * @param x
	 * @return information function
	 */
	public double informative(double x) {
		// TODO Auto-generated method stub
		double eval = evaluate(x);
		double derivative = func.derivative(x);
		return (derivative*derivative) / (eval*(1 - eval));
	}

	
	/**
	 * 
	 * @return ability variance
	 */
	public double abilityVariance() {
		return (Math.PI*Math.PI) / (3*a*a);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Item " + id + ": a=" + a + ", b=" + b + ", c=" + c + ", questions=" + questions;
	}


}
