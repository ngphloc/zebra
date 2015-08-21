/**
 * 
 */
package net.zebra.lh.uc;


/**
 * @author thuy
 *
 */
public class KMedoidCluster implements Cluster {

	
	/**
	 * 
	 */
	protected VectorList vectors;
	
	
	/**
	 * 
	 */
	protected Medoid medoid;
	
	
	/**
	 * 
	 */
	public KMedoidCluster() {
		vectors = new VectorList();
		medoid = null;
	}
	
	
	/* (non-Javadoc)
	 * @see net.zebra.lh.uc.Cluster#get(int)
	 */
	@Override
	public Vector get(int index) {
		// TODO Auto-generated method stub
		return vectors.get(index);
	}

	
	/* (non-Javadoc)
	 * @see net.zebra.lh.uc.Cluster#set(int, net.zebra.lh.uc.Vector)
	 */
	@Override
	public Vector set(int index, Vector vector) {
		// TODO Auto-generated method stub
		return vectors.set(index, vector);
	}

	
	/* (non-Javadoc)
	 * @see net.zebra.lh.uc.Cluster#add(net.zebra.lh.uc.Vector)
	 */
	@Override
	public void add(Vector vector) {
		// TODO Auto-generated method stub
		vectors.add(vector);
	}


	@Override
	public Vector remove(int index) {
		return vectors.remove(index);
	}
	
	
	@Override
	public boolean remove(Vector vector) {
		return vectors.remove(vector);
	}

	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		vectors.clear();
		medoid = null;
	}


	/* (non-Javadoc)
	 * @see net.zebra.lh.uc.Cluster#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return vectors.size();
	}

	
	@Override
	public VectorList getVectorList() {
		// TODO Auto-generated method stub
		return vectors;
	}


	/**
	 * 
	 * @param medoid
	 */
	public void setMedoid(Medoid medoid) {
		this.medoid = medoid;
	}
	
	
	/**
	 * 
	 * @return inside {@link Medoid}
	 */
	public Medoid getMedoid() {
		return medoid;
	}

	
	@Override
	public double averageSim() {
		return vectors.averageSim(medoid);
	}
	
	
	@Override
	public String toString() {
		return "vectors=" + vectors.toString() + ", medoid=" + medoid.toString();
	}
	
}
