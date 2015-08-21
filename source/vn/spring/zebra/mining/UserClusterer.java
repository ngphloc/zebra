package vn.spring.zebra.mining;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.WOW.parser.UMVariableLocator;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;
import vn.spring.zebra.util.UserUtil;
import weka.clusterers.XMeans;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserClusterer extends XMeans {
	private static final long    serialVersionUID = 1L;
	
	private String   course = null;
	//nhung thuoc tinh (concept name) dung de cluster
	//dang personal.learningstyle, course.concept
	private String[] vars = null;//
	private int      maxCluster = 0;
	
	private String      arff = null;
	private ArrayList<Profile>  minedProfiles = new ArrayList<Profile>();
	private ArrayList<ProfileCluster> clusters = new ArrayList<ProfileCluster>();
	
	protected HashSet<UserClustererChangeListener> listeners = new HashSet<UserClustererChangeListener>();

	public UserClusterer() {super();}
	
	public String getArff() {return arff;}
	public String getCourse() {return course;}
	public synchronized ArrayList<ProfileCluster> getClusters() {return clusters;}
	public synchronized ProfileCluster getClusterOf(String userid) {
		for(ProfileCluster cluster : clusters) {
			if(cluster.profiles.get(userid) != null) return cluster;
		}
		return null;
	}
	public synchronized ArrayList<DistanceProfile> getNeighborsOf(String userid) {
		ArrayList<DistanceProfile> disProfiles = new ArrayList<DistanceProfile>();
		ProfileCluster cluster = getClusterOf(userid);
		if(cluster == null) return disProfiles;
		
		Set<Entry<String, DistanceProfile>> set = cluster.profiles.entrySet();
		for(Entry<String, DistanceProfile> entry : set) {
			if(entry.getKey().equals(userid)) continue;
			disProfiles.add(entry.getValue());
		}
		Collections.sort(disProfiles);
		return disProfiles;
	}
	public synchronized void setParameters(String course, String[] vars, int maxCluster) {
		this.course = (course == null ? "" : course);
		this.vars = vars;
		this.maxCluster = maxCluster;
	}
	
	public synchronized void perform() throws Exception {
		minedProfiles.clear();
		clusters.clear();
		
		ArrayList<Profile> tempProfiles = UserUtil.getProfileList(ZebraStatic.IGNORE_ANONYMOUS);
		if(tempProfiles.size() == 0) return;
		
		StringWriter swLog = new StringWriter();
		PrintWriter pwLog = new PrintWriter(swLog);
		pwLog.println("@relation " + course + "\n");
		
		for(String var : vars) {
			pwLog.println("@attribute " + var + " real");
		}
		
		pwLog.println();
		pwLog.println("@data");
		for(int i = 0; i < tempProfiles.size(); i++) {
			Profile profile = tempProfiles.get(i);
			double[] row = getValues(profile);
			if(row == null || row.length == 0) continue;
			minedProfiles.add(profile);
			pwLog.println(CommonUtil.concatNames(row, ","));
		}
		pwLog.flush();
		String pwData = swLog.getBuffer().toString();
		swLog.close();
		pwLog.close();

		//ZebraConfig.traceService.trace("--- Course User data which will be mined ---\n" + pwData);
		
		if(minedProfiles.size() > 0) {
			StringReader arff = new StringReader(pwData);
			Instances instances = new Instances(arff);
			arff.close();
			
			setMaxNumClusters(maxCluster < m_MaxNumClusters ? m_MaxNumClusters : maxCluster);
			//if(!clusterer.getCapabilities().handles(Capability.MISSING_VALUES)) clusterer.getCapabilities().enableDependency(Capability.MISSING_VALUES);
			//if(!clusterer.getCapabilities().handles(Capability.MISSING_CLASS_VALUES))clusterer.getCapabilities().enableDependency(Capability.MISSING_CLASS_VALUES);
			buildClusterer(instances);
			
			//ZebraConfig.traceService.trace("--- User cluster results ---\n" + clusterer.toString());
			updateClusters();
		}
		
		this.arff = pwData;
		UserClustererChangeEvent evt = new UserClustererChangeEvent(this, this.clusters, this.arff);
		FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	
	//dinh thanh cac nhom
	private void updateClusters() throws ZebraException {
		clusters.clear();
		
		if(m_NumClusters == 0) return;
		ArrayList<ProfileCluster> temp_clusters = new ArrayList<ProfileCluster>();
		for(int i = 0; i < m_NumClusters; i++) temp_clusters.add(null);
		
    	for(Profile profile : minedProfiles) {
    		double[] values = getValues(profile);
    		if(values == null || values.length == 0) continue;
    		
    	    //Tim instance
    		Instance found = null;
    		for (int i = 0; i < m_Instances.numInstances(); i++) {
    	    	Instance instance = m_Instances.instance(i);
	    		if(instance.numValues() != values.length) continue;
	    		
	    		int j = 0;
	    		for(;j < instance.numValues(); j++) {
	    			if(instance.value(j) != values[j]) break;
	    		}
	    		if(j == instance.numValues()) {//Neu tim thay instance
	    			found = instance;
	    			break;
	    		}
    		}
    		if(found == null) continue;
    		
    		//Tim cluster gan nhat
	    	Instance nearestCluster = m_ClusterCenters.instance(0);
	    	int nearestIdx = 0;
    		double minDistance = m_DistanceF.distance(found, m_ClusterCenters.instance(0));
	    	for(int i = 1; i < m_NumClusters; i++) {
	    		double distance = m_DistanceF.distance(found, m_ClusterCenters.instance(i));
	    		if(distance < minDistance) {
	    			minDistance = distance;
	    			nearestCluster = m_ClusterCenters.instance(i);
	    			nearestIdx = i;
	    		}
	    	}
	    	
	    	//To Profile Cluster
			ProfileCluster cluster = temp_clusters.get(nearestIdx);
			if(cluster == null) {
				cluster = new ProfileCluster();
				cluster.center = nearestCluster;
				temp_clusters.set(nearestIdx, cluster);
			}
			try {
				String userid = profile.getAttributeValue("personal", "id");
				DistanceProfile disProfile = new DistanceProfile(userid, profile, m_DistanceF.distance(found, nearestCluster));
				cluster.profiles.put(userid, disProfile);
			}
			catch(InvalidAttributeException e) {
				ZebraStatic.traceService.trace("UserClustere.updateClusters causes error: " + e.getMessage());
			}
    	}
    	
    	for(ProfileCluster cluster : temp_clusters) {
    		if(cluster != null && cluster.profiles.size() > 0 && cluster.center != null)
    			clusters.add(cluster);
    	}
	}
	
	private double[] getValues(Profile profile) throws ZebraException {
		String            userid = null;
		try {
			userid = profile.getAttributeValue("personal", "id");
		}
		catch(Exception e) {return null;}
		
		QUERY_TYPE        evaltype = ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE;
		OverlayBayesUM    knowledge = null;
		UMVariableLocator locator = null;
		try {
			if(evaltype == QUERY_TYPE.OVERLAY)
				locator = new UMVariableLocator(profile, ZebraStatic.getConceptDB());
			if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN)
				knowledge = TriUM.getBayesUM(userid, course);
			else if(evaltype == QUERY_TYPE.DYN_OVERLAY_BAYESIAN)
				knowledge = DynOverlayBayesUM.getDynBayesUM(userid, course);
		}
		catch(Exception e) {return null;}
		
		double[] values = new double[vars.length];
		for(int i = 0; i < vars.length; i++) {
			DotString  ds = new DotString(vars[i]);
			boolean    isUseLocator = false; //use locator
			String     locatorVar = null;
			
			if(ds.size() == 1) {
				if(ds.get(0).equals("personal")) {
					values[i] = 0; continue; //no exist
				}
				else {// knowledge
					if(knowledge.getItem(ds.get(0)) == null) {
						locatorVar = vars[i] + ".knowledge";
						isUseLocator = true;
					}
					else
						values[i] = knowledge.
							inferMariginalPosterior(ds.get(0), OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE).
							getResultDouble()[0];
				}
			}
			else {
				if(ds.get(0).equals("personal")) {
					locatorVar = vars[i];
					isUseLocator = true;
				}
				else if(ds.get(0).equals(course)) {//knowledge
					if(knowledge.getItem(ds.get(1)) == null) {
						locatorVar = vars[i] + ".knowledge";
						isUseLocator = true;
					}
					else
						values[i] = knowledge.
							inferMariginalPosterior(ds.get(1), OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE).
							getResultDouble()[0];
				}
				else
					return null;
			}
			
			try {
				if(isUseLocator) { //should resolve this action
					Object value = locator.getVariableValue(locatorVar);
					if(value == null)                  values[i] = 0;
					else if(value instanceof Boolean)  values[i] = ((Boolean)value).booleanValue() ? 1 : 0;
					else if(value instanceof Float)    values[i] = ((Float)value).doubleValue();
					else if(value instanceof Number)   values[i] = ((Number)value).doubleValue();
					else                               values[i] = 0;
				}
			}
			catch(Exception e) {values[i] = 0;}
		}//end for
		
		return values;
	}
	public synchronized void addChangeListener(UserClustererChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(UserClustererChangeListener listener) {
		listeners.remove(listener);
	}

	private class FireChangeEventTask implements FireEventTask {
		private UserClustererChangeEvent evt = null;
		public FireChangeEventTask(UserClustererChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(UserClustererChangeListener listener : listeners) {
				try {
					listener.clustererChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("UserClusterer.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}

	
}
