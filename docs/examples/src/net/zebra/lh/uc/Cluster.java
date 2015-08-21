/**
 * 
 */
package net.zebra.lh.uc;

/**
 * @author Loc Nguyen
 *
 */
public interface Cluster {

	
	/**
	 * 
	 * @param index
	 * @return vector at specified index
	 */
	Vector get(int index);
	
	
	/**
	 * 
	 * @param index
	 * @param vector
	 */
	Vector set(int index, Vector vector);
	
	
	/**
	 * 
	 * @param vector
	 */
	void add(Vector vector);
	
	
	/**
	 * 
	 * @param index
	 * @return removed vector
	 */
	Vector remove(int index);
	
	
	/**
	 * 
	 * @param vector
	 * @return whether or not vector is removed
	 */
	boolean remove(Vector vector);
	

	/**
	 * 
	 */
	void clear();
	
	
	/**
	 * 
	 * @return size of cluster
	 */
	int size();
	
	
	/**
	 * 
	 * @return list of vectors
	 */
	VectorList getVectorList();
	
	
	/**
	 * 
	 * @return average similarity of this cluster
	 */
	double averageSim();
}
