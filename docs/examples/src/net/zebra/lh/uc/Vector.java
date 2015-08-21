/**
 * 
 */
package net.zebra.lh.uc;



/**
 * @author Loc Nguyen
 *
 */
public class Vector {

	
	/**
	 * 
	 */
	protected double[] data;
	
	
	/**
	 * 
	 * @param size
	 */
	public Vector(int size) {
		this(size, 0);
	}
	
	
	/**
	 * 
	 * @param array
	 */
	public Vector(double...array) {
		init(array);
	}
	
	
	/**
	 * 
	 * @param vector
	 */
	public Vector(Vector vector) {
		init(vector);
	}

	
	/**
	 * 
	 * @param size
	 * @param initValue
	 */
	public void init(int size, double initValue) {
		data = new double[size];
		for (int i = 0; i < size; i++)
			data[i] = initValue;
	}
	
	
	/**
	 * 
	 * @param array
	 */
	public void init(double...array) {
		data = new double[array.length];
		for (int i = 0; i < array.length; i++)
			data[i] = array[i];
	}
	
	
	/**
	 * 
	 * @param vector
	 */
	public void init(Vector vector) {
		init(vector.data);
	}
	
	
	/**
	 * 
	 * @return size of vector
	 */
	public int size() {
		return data.length;
	}
	
	
	/**
	 * 
	 * @param index
	 * @return value at specified index
	 */
	public double get(int index) {
		return data[index];
	}
	
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void set(int index, double value) {
		data[index] = value;
	}
	
	
	/**
	 * 
	 * @return module or length of vector
	 */
	public double module() {
		double sum = 0;
		for (double value : data) {
			sum += value*value;
		}
		return Math.sqrt(sum);
	}
	
	
	/**
	 * 
	 * @return mean value of vector
	 */
	public double mean() {
		double sum = 0;
		for (double value : data) {
			sum += value;
		}
		return sum / (double)data.length;
	}
	
	
	/**
	 * 
	 * @param vector
	 * @return dot product of given vector and specified vector
	 */
	public double product(Vector vector) {
		double sum = 0;
		for (int i = 0; i < data.length; i++) {
			sum += data[i]*vector.data[i];
		}
		return sum;
	}
	
	
	/**
	 * 
	 * @param vector
	 * @return cosine of this vector and specified vector
	 */
	protected double cosine(Vector vector) {
		return product(vector) / (module()*vector.module());
	}
	
	
	/**
	 * 
	 * @param vector
	 * @return correlation coefficient of this vector and specified vector
	 */
	protected double corr(Vector vector) {
		double mean1 = mean();
		double mean2 = vector.mean();
		double num = 0, den1 = 0, den2 = 0;
		for (int i = 0; i < data.length; i++) {
			double v1 = data[i];
			double v2 = vector.data[i];
			num += (v1 - mean1)*(v2 - mean2);
			den1 += (v1 - mean1)*(v1 - mean1);
			den2 += (v2 - mean2)*(v2 - mean2);
		}
		
		return num / Math.sqrt(den1*den2);
	}
	
	
	/**
	 * 
	 * @param vector
	 * @return similarity of given vector and specified vector
	 */
	public double sim(Vector vector) {
		return cosine(vector);
	}

	
	/**
	 * 
	 * @param vector
	 * @return whether or not two vectors are equal
	 */
	public boolean isEqual(Vector vector) {
		if (data.length != vector.data.length)
			return false;
		
		for (int i = 0; i < data.length; i++) {
			if (data[i] != vector.data[i])
				return false;
		}
		
		return true;
	}


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		for (int i = 0; i < data.length; i++) {
			if (i > 0)
				buffer.append(", " + data[i]);
			else
				buffer.append(data[i]);
		}
		buffer.append(")");
			
		return buffer.toString();
	}
	
	
}
