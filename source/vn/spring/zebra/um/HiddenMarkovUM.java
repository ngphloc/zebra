package vn.spring.zebra.um;

import java.util.ArrayList;
import java.util.List;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Attribute;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.bn.HMM;
import vn.spring.zebra.log.UserAccessRecord;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class HiddenMarkovUM {
	public static int      NUMBER_OF_RESOURCE0 = 4;
	public static String   RESOURCE0_NAME = "media";//them thuoc tinh nay vao concept
	public static String[] RESOURCE0 = {"text", "picture", "audio", "video"};

	public static int      NUMBER_OF_RESOURCE1 = 4;
	public static String   RESOURCE1_NAME = "rtype";//them thuoc tinh nay vao concept
	public static String[] RESOURCE1 = {"theory", "example", "exercise", "puzzle"};

	public static int      NUMBER_OF_RESOURCE2 = 3;
	public static String   RESOURCE2_NAME = "rinteract";//them thuoc tinh nay vao concept
	public static String[] RESOURCE2 = {"low", "medium", "high"};

	public static int      NUMBER_OF_PERSONAL_STATE = 3;
	public static int      PERSONAL_STATE_DIMENSION = 2;
	public static String[] PERSONAL_STATE0 = {"verbal", "visual"};
	public static String[] PERSONAL_STATE1 = {"active", "reflective"};
	public static String[] PERSONAL_STATE2 = {"theorist", "pragmatist"};
	
	protected HMM verbal_visual = null;
	protected HMM active_reflective = null;
	protected HMM theorist_pragmatist = null;
	
	public HiddenMarkovUM() {
		verbal_visual = new HMM(PERSONAL_STATE_DIMENSION, NUMBER_OF_RESOURCE0);
		
		verbal_visual.pi[0] = 0.5;
		verbal_visual.pi[1] = 0.5;
		
		verbal_visual.a[0][0] = 0.6;
		verbal_visual.a[0][1] = 0.4;
		verbal_visual.a[1][0] = 0.4;
		verbal_visual.a[1][1] = 0.6;
		
		verbal_visual.b[0][0] = 0.8;
		verbal_visual.b[0][1] = 0.10;
		verbal_visual.b[0][2] = 0.05;
		verbal_visual.b[0][3] = 0.05;
		
		verbal_visual.b[1][0] = 0.1;
		verbal_visual.b[1][1] = 0.4;
		verbal_visual.b[1][2] = 0.2;
		verbal_visual.b[1][3] = 0.3;

		//////////////////////////////////////
		
		active_reflective = new HMM(PERSONAL_STATE_DIMENSION, NUMBER_OF_RESOURCE1 + NUMBER_OF_RESOURCE2);
		
		active_reflective.pi[0] = 0.5;
		active_reflective.pi[1] = 0.5;
		
		active_reflective.a[0][0] = 0.6;
		active_reflective.a[0][1] = 0.4;
		active_reflective.a[1][0] = 0.4;
		active_reflective.a[1][1] = 0.6;

		active_reflective.b[0][0] = 0.2*4.0/7.0;
		active_reflective.b[0][1] = 0.3*4.0/7.0;
		active_reflective.b[0][2] = 0.1*4.0/7.0;
		active_reflective.b[0][3] = 0.4*4.0/7.0;
		
		active_reflective.b[0][4] = 0.0*3.0/7.0;
		active_reflective.b[0][5] = 0.0*3.0/7.0;
		active_reflective.b[0][6] = 1.0*3.0/7.0;

		active_reflective.b[1][0] = 0.3*4.0/7.0;
		active_reflective.b[1][1] = 0.4*4.0/7.0;
		active_reflective.b[1][2] = 0.2*4.0/7.0;
		active_reflective.b[1][3] = 0.1*4.0/7.0;

		active_reflective.b[1][4] = 1.0*3.0/7.0;
		active_reflective.b[1][5] = 0.0*3.0/7.0;
		active_reflective.b[1][6] = 0.0*3.0/7.0;

		//////////////////////////////////////
		
		theorist_pragmatist = new HMM(PERSONAL_STATE_DIMENSION, NUMBER_OF_RESOURCE1 + NUMBER_OF_RESOURCE2);
		
		theorist_pragmatist.pi[0] = 0.5;
		theorist_pragmatist.pi[1] = 0.5;
		
		theorist_pragmatist.a[0][0] = 0.6;
		theorist_pragmatist.a[0][1] = 0.4;
		theorist_pragmatist.a[1][0] = 0.4;
		theorist_pragmatist.a[1][1] = 0.6;

		theorist_pragmatist.b[0][0] = 0.4/4.0;
		theorist_pragmatist.b[0][1] = 0.3/4.0;
		theorist_pragmatist.b[0][2] = 0.2/4.0;
		theorist_pragmatist.b[0][3] = 0.1/4.0;
		
		theorist_pragmatist.b[0][4] = 0.1*3.0/7.0;
		theorist_pragmatist.b[0][5] = 0.2*3.0/7.0;
		theorist_pragmatist.b[0][6] = 0.7*3.0/7.0;

		theorist_pragmatist.b[1][0] = 0.2/4.0;
		theorist_pragmatist.b[1][1] = 0.3/4.0;
		theorist_pragmatist.b[1][2] = 0.4/4.0;
		theorist_pragmatist.b[1][3] = 0.1/4.0;

		theorist_pragmatist.b[1][4] = 0.7*3.0/7.0;
		theorist_pragmatist.b[1][5] = 0.2*3.0/7.0;
		theorist_pragmatist.b[1][6] = 0.1*3.0/7.0;
	}
	

	private OptimalStateChain viterbiState0(ArrayList<String> obs0) {
		int[] o = new int[obs0.size()];
		for(int i = 0; i < o.length; i++) {
			o[i] = rcIndexOf0(obs0.get(i));
		}
		double[][] answer = verbal_visual.viterbi(o);
		String[] states = new String[answer[1].length];
		for(int i = 0; i < states.length; i++) {
			states[i] = stateOf0( (int)(answer[1][i]) );
		}
		return new OptimalStateChain( states, Math.exp(-1*answer[0][0]) );
	}
	
	private OptimalStateChain viterbiState1(ArrayList<String> obs12) {
		int[] o = new int[obs12.size()];
		for(int i = 0; i < o.length; i++) {
			o[i] = rcIndexOf12(obs12.get(i));
		}
		double[][] answer = active_reflective.viterbi(o);
		String[] states = new String[answer[1].length];
		for(int i = 0; i < states.length; i++) {
			states[i] = stateOf1( (int)(answer[1][i]) );
		}
		return new OptimalStateChain( states, Math.exp(-1*answer[0][0]) );
	}
	private OptimalStateChain viterbiState2(ArrayList<String> obs12) {
		int[] o = new int[obs12.size()];
		for(int i = 0; i < o.length; i++) {
			o[i] = rcIndexOf12(obs12.get(i));
		}
		double[][] answer = theorist_pragmatist.viterbi(o);
		String[] states = new String[answer[1].length];
		for(int i = 0; i < states.length; i++) {
			states[i] = stateOf2( (int)(answer[1][i]) );
		}
		return new OptimalStateChain( states, Math.exp(-1*answer[0][0]) );
	}
	
	public UserStateChains inferUserStateChains(String personalid, String course) throws ZebraException, Exception {
		List<UserAccessRecord> userLog = ZebraStatic.getLogDB().getUserAccessLog(personalid, course, ZebraStatic.NETWORKER_LOG_BEGIN_DATE);
		ConceptDB cdb = ZebraStatic.getConceptDB();
		
		ArrayList<String> obs0 = new ArrayList<String>();//observations for chain 0 (verbal_visual)
		ArrayList<String> obs12 = new ArrayList<String>();//observations for chain 1 and 2 (active_reflective, theorist_pragmatist)
    	for(int i = 0; i < userLog.size(); i++) {
    		UserAccessRecord record = (UserAccessRecord) (userLog.get(i));
    		Concept concept = cdb.getConcept(cdb.findConcept(record.getConceptName()));
    		
    		String media = null;
    		try {
    			Attribute attr = concept.getAttribute(RESOURCE0_NAME);
    			media = attr.getDefault();
    			if(media == null && ZebraStatic.NETWORKER_LSHMM_RANDOM_IF_OBS_MISS)
    				media = rcOf0( (int)(Math.random()*NUMBER_OF_RESOURCE0) );
    		}
    		catch(Exception e) {
    			if(ZebraStatic.NETWORKER_LSHMM_RANDOM_IF_OBS_MISS)
    				media = rcOf0( (int)(Math.random()*NUMBER_OF_RESOURCE0) );
    		}
    		if(media != null) obs0.add(media);

    		String rtype = null;
    		try {
    			Attribute attr = concept.getAttribute(RESOURCE1_NAME);
    			rtype = attr.getDefault();
    		}
    		catch(Exception e) {
    			rtype = null;
    		}
    		
    		String rinteract = null;
    		try {
    			Attribute attr = concept.getAttribute(RESOURCE2_NAME);
    			rinteract = attr.getDefault();
    		}
    		catch(Exception e) {
    			rinteract = null;
    		}
    		
    		if(rtype != null) {
    			if(rinteract != null) {
    				obs12.add(rtype);
    				obs12.add(rinteract);
    			}
    			else
    				obs12.add(rtype);
    		}
    		else {
    			if(rinteract != null) {
    				obs12.add(rinteract);
    			}
    			else {
    				if(ZebraStatic.NETWORKER_LSHMM_RANDOM_IF_OBS_MISS) {
	            		String ob = rcOf12( (int)(Math.random()*(NUMBER_OF_RESOURCE1 + NUMBER_OF_RESOURCE2)) );
	            		obs12.add(ob);
    				}
    			}
    		}
    	}
    	
    	OptimalStateChain chain0 = ( obs0.size() == 0? null : viterbiState0(obs0) );
    	OptimalStateChain chain1 = ( obs12.size() == 0? null : viterbiState1(obs12) );
    	OptimalStateChain chain2 = ( obs12.size() == 0? null : viterbiState2(obs12) );
    	
    	UserStateChains uChains = new UserStateChains(this);
    	if(chain0 != null) uChains.addChain(chain0);
    	if(chain1 != null) uChains.addChain(chain1);
    	if(chain2 != null) uChains.addChain(chain2);
    	return uChains;
	}
	public class OptimalStateChain {
		public String[] states = null;// so chuyen trang thai
		public double probability = 0.0;
		public OptimalStateChain(String[] states, double probability) {
			this.states = states;
			this.probability = probability;
		}
		public String toString() {
		  	String output = "Optimal state chain is (";
		  	if(states == null) output += "null";
		  	else {
		  		for(int i = 0; i < states.length; i++) {
		  			output += states[i];
		  			if(i < states.length - 1) output += "->";
		  		}
		  		output += ") with optimized probability: " + (100.0*probability) + "%";
		  	}
			return output;
		}
	}
	public class UserStateChains {
		private  HiddenMarkovUM um = null;
		
		private  ArrayList<OptimalStateChain> chains = new ArrayList<OptimalStateChain>();// 3 dimensions
		public UserStateChains(HiddenMarkovUM um) {
			this.um = um;
		}
		public int getChainSize() {
			return chains.size();
		}
		public OptimalStateChain getChain(int idx) {
			return chains.get(idx);
		}
		public void addChain(OptimalStateChain chain) {
			chains.add(chain);
		}
		public void removeChain(int idx) {
			chains.remove(idx);
		}
		public void removeAllChains() {
			chains.clear();
		}
		public String toString() {
			String output = "";
			for(int i = 0; i < chains.size(); i++) {
				OptimalStateChain chain = chains.get(i);
				output += "[" + (i+1) + "]. ";
				if(chain == null) {
					output += "No state transition.";
				}
				else {
					output += chain.toString();
					String predictedState = null;
					try {
						predictedState = getPredictedState(i);
					}
					catch(Exception e) {predictedState = null;}
					if(predictedState != null)
						output += ". Next predicted state: " + predictedState;
				}
				if(i < chains.size() - 1) output += "\n";
			}
			return output;
		}
		
		public String getPredictedState(int chainOrder) throws ZebraException {
			// chainOrder is dimension: verbal-visual=0, active-reflective=1, theorist-pragmatist=2
			if(chainOrder < 0 || chainOrder >= NUMBER_OF_PERSONAL_STATE)
				throw new ZebraException("Not exist this chain");
			
			//Lay dimension
			OptimalStateChain chain = getChain(chainOrder);
			if(chain == null) throw new ZebraException("Not exist this chain (chain = null)");
			//dem so state (eg: verbal, visual) 
			int[] count = new int[PERSONAL_STATE_DIMENSION];
			for(int i = 0; i < count.length; i++) count[i] = 0;
			for(int i = 0; i < chain.states.length; i++) {
					count[stateIndexOf(chain.states[i], chainOrder)]++;
			}
			//chon index cua state co so dem lon nhat, Eg: verbal
			int maxCountIdx = 0;
			int maxCount = -1;
			for(int i = 0; i < PERSONAL_STATE_DIMENSION; i++) {
				if(count[i] > maxCount) {maxCount = count[i]; maxCountIdx = i;}
			}
			HMM hmm = null;
			if(chainOrder == 0)      hmm = um.verbal_visual;
			else if(chainOrder == 1) hmm = um.active_reflective;
			else if(chainOrder == 2) hmm = um.theorist_pragmatist;
			
			double[] a = hmm.a[maxCountIdx];
			
			//tim trang thai tiep theo lon nhat
			int maxIdx = 0;
			double maxProb = -1;
			for(int i = 0; i < a.length; i++) {
				if(a[i] > maxProb) {
					maxProb = a[i];
					maxIdx = i;
				}
			}
			return HiddenMarkovUM.stateOf(maxIdx, chainOrder);
		}
		
 	}
	private static String rcOf0(int idx) {
		return RESOURCE0[idx];
	}
	protected static String rcOf12(int idx) {
		if(idx == 0) return RESOURCE1[0];
		else if(idx == 1) return RESOURCE1[1];
		else if(idx == 2) return RESOURCE1[2];
		else if(idx == 3) return RESOURCE1[3];
		else if(idx == 4) return RESOURCE2[0];
		else if(idx == 5) return RESOURCE2[1];
		else if(idx == 6) return RESOURCE2[2];
		else return RESOURCE1[0];
	}
	private static int rcIndexOf0(String rc) {
		rc = rc.toLowerCase();
		if(rc.equals(RESOURCE0[0])) return 0;
		else if(rc.equals(RESOURCE0[1])) return 1;
		else if(rc.equals(RESOURCE0[2])) return 2;
		else if(rc.equals(RESOURCE0[3])) return 3;
		else return 0;
	}
	private static int rcIndexOf12(String rc) {
		rc = rc.toLowerCase();
		if(rc.equals(RESOURCE1[0])) return 0;
		else if(rc.equals(RESOURCE1[1])) return 1;
		else if(rc.equals(RESOURCE1[2])) return 2;
		else if(rc.equals(RESOURCE1[3])) return 3;
		else if(rc.equals(RESOURCE2[0])) return 4;
		else if(rc.equals(RESOURCE2[1])) return 5;
		else if(rc.equals(RESOURCE2[2])) return 6;
		else return 0;
	}
	private static String stateOf0(int idx) {
		return PERSONAL_STATE0[idx];
	}
	private static String stateOf1(int idx) {
		return PERSONAL_STATE1[idx];
	}
	private static String stateOf2(int idx) {
		return PERSONAL_STATE2[idx];
	}
	private static String stateOf(int idx, int order) throws ZebraException {
		if(order == 0) {
			return stateOf0(idx);
		}
		else if(order == 1) {
			return stateOf1(idx);
		}
		else if(order == 2) {
			return stateOf2(idx);
		}
		throw new ZebraException("Out of bound");
	}
	private static int stateIndexOf0(String state) {
		state = state.toLowerCase();
		if(state.equals(PERSONAL_STATE0[0])) return 0;
		else if(state.equals(PERSONAL_STATE0[1])) return 1;
		else return 0;
	}
	private static int stateIndexOf1(String state) {
		state = state.toLowerCase();
		if(state.equals(PERSONAL_STATE1[0])) return 0;
		else if(state.equals(PERSONAL_STATE1[1])) return 1;
		else return 0;
	}
	private static int stateIndexOf2(String state) {
		state = state.toLowerCase();
		if(state.equals(PERSONAL_STATE2[0])) return 0;
		else if(state.equals(PERSONAL_STATE2[1])) return 1;
		else return 0;
	}
	private static int stateIndexOf(String state, int order) throws ZebraException {
		if(order == 0) {
			return stateIndexOf0(state);
		}
		else if(order == 1) {
			return stateIndexOf1(state);
		}
		else if(order == 2) {
			return stateIndexOf2(state);
		}
		throw new ZebraException("Out of bound");
	}
	
	public static void main(String[] args) {
	}

}
