/**
 * 
 */
package net.zebra.eval.cat;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Loc Nguyen
 *
 */
public class AdaptiveTest {

	
	/**
	 * Maximum discriminatory parameter
	 */
	final static int MAX_DISCRIMINATORY = 5;
	
	
	/**
	 * Maximum difficult parameter
	 */
	final static int MAX_DIFFICULT = 5;

	
	/**
	 * Maximum number of items
	 */
	final static int MAX_ITEMS = 5;

	
	/**
	 * Maximum number of answers for each item
	 */
	final static int MAX_QUESTIONS = 10;

	
	/**
	 * Maximum number of answers for each item
	 */
	final static int MAX_EXAMINEES = 4;

	
	/**
	 * Item pool
	 */
	protected ItemList pool = null;
	
	
	/**
	 * Tested item list
	 */
	protected ItemList testedItems = null;

	
	/**
	 * List of examinees
	 */
	protected ExamineeList examinees = null;

	
	/**
	 * 
	 */
	protected PrintWriter printer = null;
	
	
	/**
	 * 
	 */
	public AdaptiveTest() {
		pool = new ItemList();
		testedItems = new ItemList();
		examinees = new ExamineeList();
	}
	
	
	/**
	 * 
	 * @param items
	 * @param examinees
	 */
	public void beginTest(Collection<Item> items, Collection<Examinee> examinees) {
		this.endTest();
		this.pool.addAll(items);
		this.examinees.addAll(examinees);
		
		double discriminatoryMean = this.pool.discriminatoryMean();
		for (Examinee examinee : examinees) {
			examinee.setDiscriminatory(discriminatoryMean);
		}
		
		this.printer = new PrintWriter(System.out, true);
	}
	
	
	/**
	 * 
	 */
	public void beginTest() {
		Random rnd = new Random();
		ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<Examinee> examinees = new ArrayList<Examinee>();
		
		for (int i = 0; i < MAX_ITEMS; i++) {
			int a = rnd.nextInt(MAX_DISCRIMINATORY) + 1;
			int b = rnd.nextInt(MAX_DIFFICULT) + 1;  
			Item item = new Item(a, b);
			item.setId(i+1);
			item.setQuestions(MAX_QUESTIONS);
			
			items.add(item);
		}
		
		for (int i = 0; i < MAX_EXAMINEES; i++) {
			Examinee examinee = new Examinee();
			examinee.setId(i+1);
			examinee.setAbility(0);
			examinees.add(examinee);
		}
		
		beginTest(items, examinees);
	}
	
	
	/**
	 * 
	 * @return recommended item
	 * @throws Exception
	 */
	protected Item recommendItem() throws Exception {
		if (pool.size() == 0)
			return null;
		
		final double discriminatoryEstimate = examinees.get(0).getDiscriminatory();
		final double difficultEstimate = examinees.abilityMean();
		out("Discriminatory estimate=" + discriminatoryEstimate);
		out("Ability mean=" + difficultEstimate);
		
		List<Item> items = new ArrayList<Item>(); 
		items.addAll(pool.getList());
		Collections.sort(items, new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				// TODO Auto-generated method stub
				double dev1 = Math.abs(o1.getDifficult() - difficultEstimate*discriminatoryEstimate/o1.getDiscriminatory());
				double dev2 = Math.abs(o2.getDifficult() - difficultEstimate*discriminatoryEstimate/o2.getDiscriminatory());
				
				if (dev1 < dev2)
					return -1;
				else if (dev1 == dev2)
					return 0;
				else
					return 1;
			}
			
		});
		List<Item> tempList = new ArrayList<Item>(); 
		tempList.addAll(items.subList(0, Math.min(MAX_EXAMINEES/2, items.size())));
		
		Collections.sort(tempList, new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				// TODO Auto-generated method stub
				double dev1 = Math.abs(o1.getDiscriminatory() - discriminatoryEstimate);
				double dev2 = Math.abs(o2.getDiscriminatory() - discriminatoryEstimate);
				
				if (dev1 < dev2)
					return -1;
				else if (dev1 == dev2)
					return 0;
				else
					return 1;
			}
			
		});
		

		return tempList.get(0);
	}
	
	
	/**
	 * 
	 * @param responses
	 * @throws Exception
	 */
	public void doTest(int itemId, int[]...responses) throws Exception {
		if (pool.size() == 0)
			throw new Exception("Pool empty");
		
		Item item = pool.getById(itemId);
		for (int i = 0; i < responses.length; i++) {
			int examineeId = responses[i][0];
			int answers = responses[i][1];
			Examinee examinee = examinees.getById(examineeId);
			examinee.respond(item, answers);
			
			examinee.setAbility(0);
			double ability = examinee.abilityEstimate();
			examinee.setAbility(ability);
			out("Examinee " + examinee.getId() + ": ability estimate=" + ability);
		}
		pool.removeById(itemId);
		testedItems.add(item);
		
		double oldDiscriminatory = examinees.get(0).getDiscriminatory();
		double abilityVariance = examinees.abilityVariance();
		if (abilityVariance > 0) {
			double discriminatoryEstimate = examinees.discriminatoryEstimate();
			for (int i = 0; i < examinees.size(); i++) {
				Examinee examinee = examinees.get(i);
				examinee.setDiscriminatory(discriminatoryEstimate);
			}
		}
		else {
			double discriminatoryMean = pool.discriminatoryMean();
			for (int i = 0; i < examinees.size(); i++) {
				Examinee examinee = examinees.get(i);
				examinee.setDiscriminatory(discriminatoryMean);
			}
		}
		
		double currentDiscriminatory = examinees.get(0).getDiscriminatory();
		for (int i = 0; i < examinees.size(); i++) {
			Examinee examinee = examinees.get(i);
			examinee.setAbility(oldDiscriminatory*examinee.getAbility()/currentDiscriminatory);
		}
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void doExampleTest() throws Exception {
		out("\n**********Test random performance**********");

		int t = 1;
		Random rnd = new Random();
		while (!doesMeetStopCriterion()) {
			out("-----Time " + t + "-----");
			
			Item item = recommendItem();
			out("Tested " + item.toString());
			
			int[][] responses = new int[examinees.size()][];
			for (int i = 0; i < examinees.size(); i++) {
				int answers = rnd.nextInt(MAX_QUESTIONS-1) + 1;
				responses[i] = new int[2];
				responses[i][0] = examinees.get(i).getId();
				responses[i][1] = answers;
			}
			doTest(item.getId(), responses);
			
			for (int i = 0; i < examinees.size(); i++) {
				Examinee examinee = examinees.get(i);
				out(examinee.toString());
			}
			
			t++;
		}
	}
	
	
	/**
	 * 
	 * @return whether or not CAT algorithm stops
	 */
	private boolean doesMeetStopCriterion() {
		return pool.size() == 0;
	}
	
	
	/**
	 * 
	 */
	public void endTest() {
		pool.clear();
		testedItems.clear();
		examinees.clear();
		
		if (printer != null)
			printer.close();
		printer = null;
	}
	
	
	/**
	 * 
	 * @param o
	 */
	private void out(Object o) {
		printer.println(o);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		

		AdaptiveTest test = new AdaptiveTest();
		test.beginTest();
		test.out("**********Item pool**********");
		for (int i = 0; i < test.pool.size(); i++) {
			Item item = test.pool.get(i);
			test.out(item.toString());
		}
		test.out("\n**********Examinees**********");
		for (int i = 0; i < test.examinees.size(); i++) {
			Examinee examinee = test.examinees.get(i);
			test.out(examinee.toString());
		}
		
		test.doExampleTest();
		test.out("\n**********Results**********");
		for (int i = 0; i < test.examinees.size(); i++) {
			Examinee examinee = test.examinees.get(i);
			test.out(examinee.toString());
		}
		
		test.endTest();
	}

}
