/**
 * 
 */
package net.zebra.eval.cat;


/**
 * @author Loc Nguyen
 *
 */
public class ItemList extends ObjectList<Item> {

	
	/**
	 * Constructor
	 */
	public ItemList() {
		
	}

	
	@Override
	public int getId(Item object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	
	/**
	 * 
	 * @return mean of discriminatory parameters
	 */
	public double discriminatoryMean() {
		double mean = 0;
		for (Item item : objects) {
			mean += item.getDiscriminatory();
		}
		
		return mean / objects.size();
	}

	
	/**
	 * 
	 * @return mean of difficult parameters
	 */
	public double difficultMean() {
		double mean = 0;
		for (Item item : objects) {
			mean += item.getDifficult();
		}
		
		return mean / objects.size();
	}
	
	
}
