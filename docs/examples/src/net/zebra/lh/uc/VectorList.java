/**
 * 
 */
package net.zebra.lh.uc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Loc Nguyen
 *
 */
public class VectorList {

	
	/**
	 * 
	 */
	protected List<Vector> vectors;
	
	
	/**
	 * 
	 */
	public VectorList() {
		vectors = new ArrayList<Vector>();
	}
	
	
	/**
	 * 
	 * @param index
	 * @return vector at specified index
	 */
	public Vector get(int index) {
		return vectors.get(index);
	}
	
	
	/**
	 * 
	 * @param index
	 * @param vector
	 */
	public Vector set(int index, Vector vector) {
		return vectors.set(index, vector);
	}
	
	
	/**
	 * 
	 * @param vector
	 */
	public void add(Vector vector) {
		vectors.add(vector);
	}
	
	
	/**
	 * 
	 * @param array
	 */
	public void add(Vector...array) {
		for (int i = 0; i < array.length; i++)
			vectors.add(array[i]);
	}

	
	/**
	 * 
	 * @param index
	 * @return removed vector
	 */
	public Vector remove(int index) {
		return vectors.remove(index);
	}
	
	
	/**
	 * 
	 * @param vector
	 * @return whether or not vector is removed
	 */
	public boolean remove(Vector vector) {
		return vectors.remove(vector);
	}
	
	
	/**
	 * 
	 * @param vList
	 */
	public void add(VectorList vList) {
		for (int i = 0; i < vList.size(); i++)
			vectors.add(vList.get(i));
	}

	
	/**
	 * 
	 */
	public void clear() {
		vectors.clear();
	}
	
	
	/**
	 * 
	 * @return size of vector list
	 */
	public int size() {
		return vectors.size();
	}
	

	/**
	 * 
	 * @param vector
	 * @return average similarity
	 */
	public double averageSim(Vector vector) {
		double sum = 0;
		for (Vector v : vectors) {
			sum += v.sim(vector);
		}
		
		return sum / (double)vectors.size();
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < vectors.size(); i++) {
			if (i > 0)
				buffer.append(", " + vectors.get(i).toString());
			else
				buffer.append(vectors.get(i).toString());
		}
			
		return buffer.toString();
	}
	
}
