/**
 * 
 */
package net.zebra.lh.uc;

/**
 * @author Loc Nguyen
 *
 */
public interface Clustering {

	
	/**
	 * 
	 * @param parameters
	 */
	void setParameters(Object...parameters);
	
	
	/**
	 * 
	 * @param sample
	 * @return list of clusters
	 */
	ClusterList clustering(VectorList sample);
}
