package vn.spring.zebra.um;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.evaluation.TestEngineEvent;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUMChangeEvent.CHANGE_TYPE;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DynOverlayBayesUM {
	protected String         userid = null;
	protected String         course = null;
	protected OverlayBayesUM originUM = null;
	protected double guess = ZebraStatic.DYN_DEFAULT_GUESS;
	protected double forget = ZebraStatic.DYN_DEFAULT_FORGET;
	protected ArrayList<TOverlayBayesUM> states = new ArrayList<TOverlayBayesUM>();
	
	private boolean isChanged = false;

	protected HashSet<DynOverlayBayesUMChangeListener> listeners = new HashSet<DynOverlayBayesUMChangeListener>();
	
	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	public boolean isChanged() {return isChanged;}
	
	public DynOverlayBayesUM(String userid, String course, double guess, double forget) throws ZebraException, FileNotFoundException {
		this.userid = userid;
		this.course = course;
		this.guess = guess;
		this.forget = forget;
		reload();
	}
	public synchronized void reload() throws ZebraException, FileNotFoundException {
		states.clear();
		try {
			this.originUM = OverlayBayesUMFactory.loadFromDatabase(null, course, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, false);
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
		
    	OverlayBayesUM dynum = null;
    	try {
    		dynum = OverlayBayesUMFactory.loadFromDatabase(userid, course, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, true);
    	}
    	catch(Exception e) {
    		dynum = null; ZebraStatic.traceService.trace(e.getMessage() + ".\n DynUM should be created");
    	}
    	
    	//Must create
    	if(dynum == null) {
    		dynum = (OverlayBayesUM)originUM.clone();
    		dynum.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
    	}

		TOverlayBayesUM state = new TOverlayBayesUM(dynum, new Date()); 
		this.states.add(state);
		isChanged = false;
		FireEventUtil.fireEvent(new FireChangeEventTask(new DynOverlayBayesUMChangeEvent(this, state, CHANGE_TYPE.RELOAD)), 
				ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized void save()  throws ZebraException, FileNotFoundException {
		OverlayBayesUM um = getCurState().um;
    	um.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
    	isChanged = false;
		ZebraStatic.traceService.trace("DynOverlayBayesUM.save");
	}
	public synchronized int size() {return states.size();}
	public synchronized TOverlayBayesUM getState(int idx) {return states.get(idx);}
	public synchronized TOverlayBayesUM getLastState() {
		int size = states.size();
		if(size == 0) return null;
		return states.get(size - 1);
	}
	
	//This function is very important
	public synchronized TOverlayBayesUM getCurState() {
		int size = states.size();
		if(size == 0)      return null;
		else if(size == 1) return states.get(0);
		else               return states.get(size - 2);
	}
	
	public synchronized void testEvaluated(TestEngineEvent evt) throws ZebraException {
		Evidence evidence = evt.getEvidence();
		if(!evidence.userid.equals(this.userid)) return;
		if(!evidence.course.equals(this.course)) return;
		ArrayList<Evidence> evidences = new ArrayList<Evidence>();
		evidences.add(evidence);
		learn(evidences);
		try {
			save();
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
	}
	
    @Deprecated
	public synchronized void _learn() throws ZebraException, Exception {
		if(true) throw new ZebraException("Now the function DynOverlayBayesUM._learn isn't still supported");
		
		if(userid == null) {ZebraStatic.traceService.trace("ZEBRA: User ID is null"); return;}
		ArrayList<Evidence> evidences = Evidence.getTempEvidences(userid, course, originUM, ZebraStatic.NETWORKER_LOG_BEGIN_DATE);
		
		learn(evidences);
	}
	
	private void learn(ArrayList<Evidence> evidences) throws ZebraException {
		if(evidences.size() == 0) return;
		
		//Lay overlay cuoi cung
		OverlayBayesUM cur_um = getLastState().getUserModel();
		OverlayBayesUM new_um = (OverlayBayesUM)originUM.clone();
		for(Evidence evidence : evidences) {
			OverlayBayesItem cur_item = cur_um.getItem(evidence.briefName);
			if(cur_item != null && cur_item.isObserved()) cur_item.setObservedValue(evidence.value);

			OverlayBayesItem new_item = new_um.getItem(evidence.briefName);
			if(new_item != null && new_item.isObserved()) new_item.setObservedValue(evidence.value);
		}
		
		//Lay danh sach cur item
		HashMap<String, OverlayBayesItem> cur_items = new HashMap<String, OverlayBayesItem>();
		OverlayBayesItem[] temp_items = cur_um.getItems();
		for(OverlayBayesItem item : temp_items) {
			String name = item.getName();
			cur_items.put(name, item);
		}
		
		//set up weight
		OverlayBayesItem[] items = new_um.getItems();
		ArrayList<OverlayBayesItem> aux_items = new ArrayList<OverlayBayesItem>();
		for(OverlayBayesItem item : items) {
			if(item.isObserved()) continue;
			String name = item.getName();
			if(name.endsWith(ZebraStatic.DYN_AUX_SIGN)) continue;
			if(cur_items.get(name) == null) continue;// neu UM khong co item nay do bi thay doi thi bo qua
			
			//Carefully with two following line
			if(item.hasParent()) continue; //if item has parent, ignore
			if(!item.hasObserved()) continue; //if item has no evidences, ignore
			
			InferenceResult result = cur_um.inferMariginalPosterior(name, OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE);
			double[] posterior = result.getResultDouble();
			if(posterior.length != 2) {
				throw new ZebraException("Just support binary variable");
			}
			OverlayBayesItem aux_item = new_um.createItem(name + ZebraStatic.DYN_AUX_SIGN, new ConceptNodeTypeWrapper(item).getType());
			double[] prior = new double[2];
			prior[0] = 0.5 + posterior[0] * 0.5;
			prior[1] = 1 - prior[0];
			aux_item.setFunctionValues(prior);
			
			double weight = (1 - forget) / (1 + guess);
			new_um.createArc(aux_item, item, weight);
			aux_items.add(aux_item);
			
			//Xac dinh position cua aux_item
			Point aux_pos = item.getPos();
			if(aux_pos == null) aux_pos = new Point(0, 0);
			new_um.setItemPos(aux_item, aux_pos);
		}
		//Xac dinh lai position cua items
		if(aux_items.size() > 0) {
			Point maxPos = getMaxPos(aux_items);
			for(OverlayBayesItem item : items) {
				Point pos = item.getPos(); if(pos == null) pos = new Point(0, 0);
				pos.translate(maxPos.x + 10, 0);
				new_um.setItemPos(item, pos);
			}
		}
		
		if(states.size() > ZebraStatic.DYN_MAX_STATES) {
			TOverlayBayesUM last = getLastState();
			states.clear();
			states.add(last);
			FireEventUtil.fireEvent(new FireChangeEventTask(new DynOverlayBayesUMChangeEvent(this, null, CHANGE_TYPE.CLEAR)), 
					ZebraStatic.FIRE_EVENT_WAIT_DONE);
		}
		
		//Add new state
		TOverlayBayesUM state = new TOverlayBayesUM(new_um, new Date()); 
		states.add(state);
		isChanged = true;
		FireEventUtil.fireEvent(new FireChangeEventTask(new DynOverlayBayesUMChangeEvent(this, state, CHANGE_TYPE.ADD)), 
				ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}

	public synchronized InferenceResult inferMariginalPosterior(String briefItemName) {
		OverlayBayesUM um = states.get(states.size() - 1).getUserModel();
		return um.inferMariginalPosterior(briefItemName, OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE);
	}
	
	public synchronized void addChangeListener(DynOverlayBayesUMChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(DynOverlayBayesUMChangeListener listener) {
		listeners.remove(listener);
	}
	private static Point getMaxPos(Collection<OverlayBayesItem> items) {
		Point maxPos = new Point(0, 0);
		if(items == null || items.size() == 0) return maxPos;
		
		for(OverlayBayesItem item : items) {
			Point pos = item.getPos();
			if(pos == null) continue;
			
			if(maxPos.x < pos.x) maxPos.x = pos.x;
			if(maxPos.y < pos.y) maxPos.y = pos.y;
		}
		return maxPos;
	}
	public class TOverlayBayesUM {
		private OverlayBayesUM um = null;
		private Date state = null;
		public TOverlayBayesUM(OverlayBayesUM um, Date state) {
			this.um = um;
			this.state = state;
		}
		public OverlayBayesUM getUserModel() {return um;}
		public Date getState() {return state;}
	}
	
	private class FireChangeEventTask implements FireEventTask {
		private DynOverlayBayesUMChangeEvent evt = null;
		public FireChangeEventTask(DynOverlayBayesUMChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(DynOverlayBayesUMChangeListener listener : listeners) {
				try {
					switch(evt.changeType) {
					case ADD:
						listener.stateAdded(evt);
						break;
					case CLEAR:
						listener.stateCleared(evt);
						break;
					case RELOAD:
						listener.stateReloaded(evt);
						break;
					}
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("DynOverlayBayesUM.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}
	
    public static OverlayBayesUM getDynBayesUM(String userid, String course) {
    	OverlayBayesUM um = null;
    	try {
    		um = OverlayBayesUMFactory.loadFromDatabase(userid, course, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, true);
    	}
    	catch(Exception e) {
    		um = null; ZebraStatic.traceService.trace(e.getMessage() + ".\n It should be created");
    	}
    	if(um != null) return um;
    	
    	um = TriUM.getBayesUM(userid, course);
    	try {
    		if(um != null) um.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
    	}
    	catch(Exception e) {
    		um = null; ZebraStatic.traceService.trace(e.getMessage() + ".\n It can not be created");
    	}
    	return null;
    }
	public static void main(String[] args) throws Exception {
	}
}
