/**
 * 
 */
package net.zebra.lh.uc;

import java.io.PrintWriter;

/**
 * @author Loc Nguyen
 *
 */
public class KMedoidClustering implements Clustering {

	
	/**
	 * 
	 */
	public final static double TERMINATED_ERROR = 0.000001;
	
	
	/**
	 * 
	 */
	public final static int MAX_CLUSTERS = 2;

	
	/**
	 * 
	 */
	protected double terminatedError = TERMINATED_ERROR;
	
	
	/**
	 * 
	 */
	protected int maxClusters = MAX_CLUSTERS;

	
	/**
	 * 
	 */
	protected PrintWriter printer = null;

	
	/**
	 * 
	 */
	public KMedoidClustering() {
		printer = new PrintWriter(System.out, true);
	}
	
	
	/**
	 * 
	 * @param terminatedCriterion
	 */
	public void setParameters(double terminatedCriterion) {
		this.terminatedError = terminatedCriterion;
	}
	
	
	@Override
	public void setParameters(Object... parameters) {
		// TODO Auto-generated method stub
		if (parameters.length < 2)
			return;
		
		try {
			terminatedError = Double.parseDouble(parameters[0].toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			maxClusters = Integer.parseInt(parameters[1].toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public ClusterList clustering(VectorList sample) {
		// TODO Auto-generated method stub
		ClusterList inputClusters = new ClusterList();
		for (int i = 0; i < maxClusters; i++) {
			KMedoidCluster inputCluster = new KMedoidCluster();
			inputClusters.add(inputCluster);
		}
		
		VectorList vectors = new VectorList();
		vectors.add(sample);
		//Random rnd = new Random();
		for (int i = 0; i < maxClusters; i++) {
//			int index = rnd.nextInt(vectors.size());
			int index = 0;
			Vector vector = vectors.get(index);
			vectors.remove(index);
			
			Medoid medoid = new Medoid(vector);
			KMedoidCluster inputCluster = (KMedoidCluster) inputClusters.get(i);
			inputCluster.setMedoid(medoid);
		}
		for (int i = 0; i < vectors.size(); i++) {
			Vector vector = vectors.get(i);
			inputClusters.get(0).add(vector);
		}
		
		int t = 1;
		while (true) {
			out("**********Time " + t + "**********");
			ClusterList outputClusters = clustering(inputClusters);
			boolean equal = true;
			for (int i = 0; i < outputClusters.size(); i++) {
				Medoid outputMedoid = ((KMedoidCluster)outputClusters.get(i)).getMedoid();
				Medoid inputMedoid = ((KMedoidCluster)inputClusters.get(i)).getMedoid();
				if (!outputMedoid.isEqual(inputMedoid)) {
					equal = false;
					break;
				}
			}
			if (equal)
				break;
			
			double error =  1 - outputClusters.averageSim();
			if (error < terminatedError)
				break;
			
			inputClusters.clear();
			inputClusters = outputClusters;
			t++;
		}
		return clustering(inputClusters);
	}


	/**
	 * 
	 * @param inputClusters
	 * @return list of clusters
	 */
	protected ClusterList clustering(ClusterList inputClusters) {
		out("-----Input clusters-----\n" + inputClusters);

		ClusterList outputClusters = new ClusterList();
		for (int i = 0; i < inputClusters.size(); i++) {
			KMedoidCluster outputCluster = new KMedoidCluster();
			KMedoidCluster inputCluster = (KMedoidCluster)inputClusters.get(i);
			outputCluster.setMedoid(inputCluster.getMedoid() );
			outputClusters.add(outputCluster);
		}
		
		out("-----Step 2: Assign user models into clusters-----");
		VectorList vectors = inputClusters.getVectorList();
		while (vectors.size() > 0) {
			Vector vector = vectors.get(0);
			int maxClusterIndex = -1;
			double maxSim = -1;
			for (int j = 0; j < inputClusters.size(); j++) {
				KMedoidCluster inputCluster = (KMedoidCluster)inputClusters.get(j);
				double sim = inputCluster.getMedoid().sim(vector);
				out("Similarity of vector " + vector + " and medoid " + inputCluster.getMedoid() + ": " + sim);
				if (maxClusterIndex == -1) {
					maxSim = sim;
					maxClusterIndex = j;
				}
				else if (sim > maxSim) {
					maxSim = sim;
					maxClusterIndex = j;
				}
			}
			
			KMedoidCluster outputCluster = (KMedoidCluster)outputClusters.get(maxClusterIndex);
			outputCluster.add(vector);
			vectors.remove(vector);
			out("Vector " + vector + " : max similarity=" + maxSim + " : nearest cluster=" + maxClusterIndex);
		}
		
		out("-----Step 3: Re-calculating medoids-----");
		for (int i = 0; i < outputClusters.size(); i++) {
			KMedoidCluster outputCluster = (KMedoidCluster)outputClusters.get(i);
			if (outputCluster.size() < 1)
				continue;
			
			Vector vector = new Vector(outputCluster.getMedoid());
			outputCluster.add(vector);
			
			VectorList outputVectors = outputCluster.getVectorList();
			Vector maxVector = null;
			double maxAverageSim = -1;
			for (int j = 0; j < outputVectors.size(); j++) {
				Vector v = outputVectors.get(j);
				VectorList outputVectors2 = new VectorList();
				outputVectors2.add(outputVectors);
				outputVectors2.remove(j);
				double averageSim = outputVectors2.averageSim(v);
				out("Cluster " + i + ", vector " + v + ", average similarity=" + averageSim);
				if (maxVector == null) {
					maxAverageSim = averageSim;
					maxVector = v;
				}
				else if (averageSim > maxAverageSim) {
					maxAverageSim = averageSim;
					maxVector = v;
				}
			}
			
			VectorList outputVectors2 = new VectorList();
			outputVectors2.add(outputVectors);
			outputVectors2.remove(vector);
			double sim = outputVectors2.averageSim(vector);
			if (maxAverageSim > sim) {
				Medoid medoid = new Medoid(maxVector);
				outputCluster.remove(maxVector);
				outputCluster.setMedoid(medoid);
				out("Cluster " + i + " : medoid=" + medoid + ", max average similarity=" + maxAverageSim);
			}
			else {
				Medoid medoid = new Medoid(vector);
				outputCluster.remove(vector);
				outputCluster.setMedoid(medoid);
				out("Cluster " + i + " : medoid=" + medoid);
			}
		}
		out("-----Output clusters-----\n" + outputClusters);
		
		return outputClusters;
	}
	
	
	/**
	 * 
	 * @param o
	 */
	protected void out(Object o) {
		printer.println(o);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VectorList sample = new VectorList();
		sample.add(
		new Vector(1, 5), new Vector(1, 4), new Vector(2, 4),
		new Vector(1, 1), new Vector(2, 1), new Vector(3, 2));
		
		KMedoidClustering clustering = new KMedoidClustering();
		clustering.clustering(sample);
	}

}
