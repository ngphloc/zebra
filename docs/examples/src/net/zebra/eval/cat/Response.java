/**
 * 
 */
package net.zebra.eval.cat;

/**
 * @author Loc Nguyen
 *
 */
public class Response {

	
	/**
	 * Reference to item
	 */
	protected Item item = null;
	
	
	/**
	 * Number of answers
	 */
	protected int answers = 0;
	
	
	/**
	 * 
	 * @param item
	 * @param answers
	 */
	public Response(Item item, int answers) {
		this.item = item;
		this.answers = answers;
	}
	
	
	/**
	 * 
	 * @return reference to item
	 */
	public Item getItem() {
		return item;
	}
	
	
	/**
	 * 
	 * @return the number of answers
	 */
	public int getAnswers() {
		return answers;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Response of (" + item.toString() + "): answers=" + answers;
	}
	
	
}
