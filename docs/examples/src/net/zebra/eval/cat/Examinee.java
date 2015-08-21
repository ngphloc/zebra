/**
 * 
 */
package net.zebra.eval.cat;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Loc Nguyen
 *
 */
public class Examinee extends Item {
	
	
	/**
	 * 
	 */
	protected double ability = 0;
	
	
	/**
	 * List of responses
	 */
	protected List<Response> responses = null;
	
	
	/**
	 * Default constructor
	 */
	public Examinee() {
		super(0, 0, 0);
		responses = new ArrayList<Response>();
	}
	
	
	/**
	 * 
	 * @return examinee's ability
	 */
	public double getAbility() {
		return ability;
	}
	
	
	/**
	 * 
	 * @param ability examinee's ability
	 */
	public void setAbility(double ability) {
		this.ability = ability;
	}
	
	
	/**
	 * 
	 * @param item
	 * @param answers
	 */
	public void respond(Item item, int answers) {
		Response response = null;
		if (item.getId() == -1)
			response = new Response(item, answers);
		else {
			for (Response r : responses) {
				if (r.getItem().getId() == item.getId()) {
					response = r;
					break;
				}
			}
			if (response == null)
				response = new Response(item, answers);
		}
		responses.add(response);
	}
	
	
	/**
	 * 
	 * @return responses size
	 */
	public int getResponseSize() {
		return responses.size();
	}
	
	
	/**
	 * 
	 * @param index
	 * @return {@link Response}
	 */
	public Response getResponseByIndex(int index) {
		return responses.get(index);
	}
	
	
	/**
	 * 
	 * @return examinee's ability estimate
	 * @throws Exception
	 */
	public double abilityEstimate() throws Exception {
		if (getDiscriminatory() == 0)
			throw new Exception("Discriminatory parameter a is zero");
		if (responses.size() == 0)
			return 0;
		
		double R=0, QR=0;
		for(Response response : responses) {
			R += response.getAnswers();
			QR += response.getItem().getQuestions() - response.getAnswers();
		}
		
		return (Math.log(R) - Math.log(QR))/getDiscriminatory() + ability;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("Examinee " + id + ": ability=" + ability + ", discriminatory=" + getDiscriminatory());
		for (Response response : responses) {
			buffer.append("\n    " + response.toString());
		}
		
		return buffer.toString();
	}
	
}
