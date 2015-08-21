/**
 * 
 */
package net.zebra.eval.cat;


/**
 * @author Loc Nguyen
 *
 */
public class ExamineeList extends ObjectList<Examinee> {

	
	/**
	 * Constructor
	 */
	public ExamineeList() {

	}


	@Override
	public int getId(Examinee object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	
	/**
	 * 
	 * @return ability mean
	 */
	public double abilityMean() {
		double mean = 0;
		for (Examinee examinee : objects) {
			mean += examinee.getAbility();
		}
	
		return mean / objects.size();
		
	}
	
	
	/**
	 * 
	 * @return variance of sample of examinees
	 * @throws Exception
	 */
	public double abilityVariance() throws Exception {
		if (objects.size() == 1)
			return 0;
		
		double mean = abilityMean();
		double var = 0;
		for (Examinee examinee : objects) {
			double square = examinee.getAbility() - mean; 
			var += square*square;
		}
		return var / (objects.size() - 1);
	}
	
	
	/**
	 * 
	 * @return discriminatory estimate from sample of examinees
	 * @throws Exception
	 */
	public double discriminatoryEstimate() throws Exception {
		if (objects.size() == 1)
			return Double.MAX_VALUE;
		
		return Math.PI / Math.sqrt(3*abilityVariance());
	}


}
