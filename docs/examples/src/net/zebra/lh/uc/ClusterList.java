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
public class ClusterList {

	
	/**
	 * 
	 */
	protected List<Cluster> clusters;
	
	
	/**
	 * 
	 */
	public ClusterList() {
		clusters = new ArrayList<Cluster>();
	}
	
	
	/**
	 * 
	 * @param index
	 * @return cluster at specified index
	 */
	public Cluster get(int index) {
		return clusters.get(index);
	}
	
	
	/**
	 * 
	 * @param index
	 * @param cluster
	 */
	public Cluster set(int index, Cluster cluster) {
		return clusters.set(index, cluster);
	}
	
	
	/**
	 * 
	 * @param cluster
	 */
	public void add(Cluster cluster) {
		clusters.add(cluster);
	}
	
	
	/**
	 * 
	 */
	public void clear() {
		clusters.clear();
	}
	
	
	/**
	 * 
	 * @return size of cluster list
	 */
	public int size() {
		return clusters.size();
	}
	
	
	/**
	 * 
	 * @return list of all vectors
	 */
	public VectorList getVectorList() {
		VectorList vectors = new VectorList();
		for (Cluster cluster : clusters) {
			vectors.add(cluster.getVectorList());
		}
		
		return vectors;
	}


	/**
	 * 
	 * @return average similarity of cluster list
	 */
	public double averageSim() {
		double sum = 0;
		for (Cluster cluster : clusters) {
			sum += cluster.averageSim();
		}
		
		return sum / (double)clusters.size();
	}

	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Clusters\n");
		for (int i = 0; i < clusters.size(); i++) {
			buffer.append("    Cluster " + i + ": ");
			if (i > 0)
				buffer.append(clusters.get(i).toString() + "");
			else
				buffer.append(clusters.get(i).toString() + "\n");
		}
			
		return buffer.toString();
	}
	
	
}
