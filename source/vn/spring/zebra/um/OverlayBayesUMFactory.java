package vn.spring.zebra.um;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.awt.*;

import javax.swing.JOptionPane;

import vn.spring.bayes.BayesianInferences.BucketTree;
import vn.spring.bayes.BayesianInferences.Explanation;
import vn.spring.bayes.BayesianNetworks.*;
import vn.spring.bayes.QuasiBayesianInferences.QBExpectation;
import vn.spring.bayes.QuasiBayesianInferences.QBInference;
import vn.spring.bayes.QuasiBayesianNetworks.QuasiBayesNet;
import vn.spring.bayes.InferenceGraphs.*;
import vn.spring.bayes.InterchangeFormat.IFException;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_BAYESNET_TYPE;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_FILE_FORMAT_TYPE;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.helperservice.AuthorService;
import vn.spring.WOW.graphauthor.GraphAuthor.*;

import vn.spring.zebra.math.BetaDensity;
import vn.spring.zebra.math.BinaryArrangementWithRepetitionGenerator;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class OverlayBayesUMFactory {
	public static void throwError(String err) throws ZebraException {
		JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
		throw new ZebraException(err);
	}
	
	public static int getAndCheckNumberOfDefaultVariableValues(ConceptNodeTypeWrapper cNodeType) throws ZebraException {
		int base = 0;
    	if(!cNodeType.isEvidence()) {
    		base = OverlayBayesUM.OBUM_DEFAULT_NONEVIDENCE_VARIABLE_VALUES.length;
        	if(base != 2) throwError("Non-evidence variable must be binary");
    	}
    	else if(cNodeType.isTestEvidence()) {
        	base = OverlayBayesUM.OBUM_DEFAULT_TESTEVIDENCE_VARIABLE_VALUES.length;
    	}
    	else if(cNodeType.isTempEvidence()) {
    		base = OverlayBayesUM.OBUM_DEFAULT_NONEVIDENCE_VARIABLE_VALUES.length;
        	if(base != 2) throwError("Temp-evidence variable must be binary");
    	}
    	else
    		throwError("That evidence type is not implemented yet");
    	return base;
	}
	
	public static OverlayBayesUM create(OBUM_BAYESNET_TYPE bayesType) throws ZebraException {
		if(bayesType == OBUM_BAYESNET_TYPE.JAVABAYES) {
			OverlayBayesUM um = new OverlayBayesUM_JavaBayes();
			return um;
		}
		throwError("Invalid Inner Bayesian network class"); return null;
	}
	public static OverlayBayesUM createFromAuthor(URL base, String author, String course, OBUM_BAYESNET_TYPE bayesType) throws ZebraException, MalformedURLException, IOException, IFException, Exception {
		//E.g: base = http://localhost:8080/wow
		OverlayBayesUM um = create(bayesType);
		um.createFromAuthor(base, author, course);
		return um;
	}
	public static OverlayBayesUM loadFromAuthor(URL base, String author, String course, OBUM_BAYESNET_TYPE bayesType) throws ZebraException, MalformedURLException, IOException, IFException {
		//E.g: base = http://localhost:8080/base
		OverlayBayesUM um = create(bayesType);
		um.loadFromAuthor(base, author, course);
		return um;
	}
	public static OverlayBayesUM loadFromDatabase(String userid, String course, OBUM_BAYESNET_TYPE bayesType, boolean isDynamic) throws ZebraException, MalformedURLException, IOException, IFException {
		OverlayBayesUM um = create(bayesType);
		um.loadFromDatabase(userid, course, isDynamic);
		return um;
	}
	public static OBUM_FILE_FORMAT_TYPE getFormatType(String ext) throws ZebraException {
		if(ext.toLowerCase().equals(".bif"))
			return OBUM_FILE_FORMAT_TYPE.BIF;
		else if(ext.toLowerCase().equals(".xml"))
			return OBUM_FILE_FORMAT_TYPE.XML;
		throwError("Not support this file type yet"); return null;
	}
	public static String getFormatExt(OBUM_FILE_FORMAT_TYPE format) throws ZebraException {
		if(format == OBUM_FILE_FORMAT_TYPE.BIF)
			return ".bif";
		else if(format == OBUM_FILE_FORMAT_TYPE.XML)
			return ".xml";
		throwError("Not support this file type yet"); return null;
	}
	
	public static String[] createValuesForVar(ConceptNodeTypeWrapper cNodeType) throws ZebraException {
		String[] values = new String[0];
    	if(!cNodeType.isEvidence()) {
        	values = new String[OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(cNodeType)];
        	for(int i = 0; i < values.length; i++) 
        		values[i] = OverlayBayesUM.OBUM_DEFAULT_NONEVIDENCE_VARIABLE_VALUES[i].toString();
    	}
    	else if(cNodeType.isTestEvidence()) {
        	values = new String[OverlayBayesUM.OBUM_DEFAULT_TESTEVIDENCE_VARIABLE_VALUES.length];
        	for(int i = 0; i < values.length; i++) 
        		values[i] = OverlayBayesUM.OBUM_DEFAULT_TESTEVIDENCE_VARIABLE_VALUES[i].toString();
    	}
    	else if(cNodeType.isTempEvidence()) {
        	values = new String[getAndCheckNumberOfDefaultVariableValues(cNodeType)];
        	for(int i = 0; i < values.length; i++) 
        		values[i] = OverlayBayesUM.OBUM_DEFAULT_NONEVIDENCE_VARIABLE_VALUES[i].toString();
    	}
    	else
    		throwError("That evidence type is not implemented yet");
		return values;
	}

	public static double[] getNoParentProbs(ConceptNodeTypeWrapper cNodeType) throws ZebraException {
		if(cNodeType.isEvidence()) throwError("Evidence mus have parent");
    	getAndCheckNumberOfDefaultVariableValues(cNodeType);
    	return new double[] {0.5, 0.5};
    }
	
    //Tu trong so chuyen thanh bang xac suat dieu kien, cach 1: tat ca bang 1 roi den tat ca bang 0
    public static double[] getProbsOfWeights(double[] weights, ConceptNodeTypeWrapper cNodeType) throws ZebraException {
    	int number = getAndCheckNumberOfDefaultVariableValues(cNodeType);
    	if(number == 2) return binaryProbsOfWeights(weights, 1);
    	
    	int      n = (int) (Math.pow(2, weights.length));
    	double[] probs = new double[number * n];
    	double sum = 0; for(int i = 0; i < number; i++) sum += i;
    	
    	int pos = 0;
    	for(int i = 0; i < number; i++) {
    		double mark = (double)(number - 1 - i) / (double)(number - 1);
    		double[] segment_probs = binaryProbsOfWeights(weights, mark);
    		double[] segment_probs2 = binaryProbsOfWeights(weights, 1 - mark);
    		
    		for(int j = 0;   j < n/2; j++) probs[pos + j] = segment_probs[j] / sum;
    		for(int j = n/2; j < n;   j++) probs[pos + j] = segment_probs2[j-n/2] / sum;
    		
    		pos += n;
    	}
    	return probs;
    }
    //Tu bang xac suat dieu kien chuyen thanh trong so, n la so nut cha cua nut hien tai, probs.length = pow(2, n) or pow(2, n+1)
	public static double[] getWeightsOfProbs(int nParent, double[] probs, ConceptNodeTypeWrapper cNodeType) throws ZebraException {
    	int number = getAndCheckNumberOfDefaultVariableValues(cNodeType);
    	int n = (int) (Math.pow(2, nParent + 1));
    	double sum = 0; for(int i = 0; i < number; i++) sum += i;
    	return binaryWeightsOfProbs(nParent, Arrays.copyOf(probs, n), 1, sum);
	}
	
    //Tu trong so chuyen thanh bang xac suat dieu kien, cach 1: tat ca bang 1 roi den tat ca bang 0
    private static double[] binaryProbsOfWeights(double[] weights, double mark) throws ZebraException {
    	double[] values = new double[] {1.0, 0.0};
    	
    	int      r = weights.length; if(r == 0) return new double[] {0.5, 0.5};
    	int      stats = (int) (Math.pow(2, r + 1));
    	double[] temp_probs = new double [stats / 2];
    	BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(2, r);
    	if( (arr.getNumArrangement()) != (stats / 2) )
    		OverlayBayesUMFactory.throwError("Invalid BinaryArrangementWithRepetitionGenerator");
    	for(int i = 0; i < arr.getNumArrangement(); i++) {
    		int[] a = arr.getArrangement(i);
    		temp_probs[i] = 0.0;
    		for(int j = 0; j < r; j++) {
    			temp_probs[i] += ( values[a[j]] ) * ( weights[j] );
    		}
    	}
    	
    	double[] probs = new double [stats];
    	for(int i = 0; i < stats / 2; i++) {
    		probs[i] = (float) (temp_probs[i]*mark);
    		if(probs[i] > 1) throwError("Probability can't be greater than 1!");
    	}
    	for(int j = stats / 2; j < stats; j++)  probs[j] = 1 - probs[j - stats / 2];
    	return probs;
    }
//    //Tu trong so chuyen thanh bang xac suat dieu kien, cach 2: bang 1 va bang 0 xen nhau
//    private static double[] binarProbsOfWeights2(double[] weights, double mark) throws ZebraException {
//    	double[] values = new double[] {1.0, 0.0};
//    	
//    	int      r = weights.length; if(r == 0) return new double[] {0.5, 0.5};
//    	int      stats = (int) (Math.pow(2, r + 1));
//    	double[] temp_probs = new double [stats / 2];
//    	BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(2, r);
//    	if( (arr.getNumArrangement()) != (stats / 2) )
//    		OverlayBayesUMFactory.throwError("Invalid BinaryArrangementWithRepetitionGenerator");
//    	for(int i = 0; i < arr.getNumArrangement(); i++) {
//    		int[] a = arr.getArrangement(i);
//    		temp_probs[i] = 0.0;
//    		for(int j = 0; j < r; j++)
//    			temp_probs[i] += ( values[a[j]] ) * ( weights[j] );
//    	}
//    	
//    	double[] probs = new double [stats];
//    	for(int i = 0; i < stats - 1; i += 2) {
//    		probs[i] = (float) (temp_probs[i / 2]*mark);
//    		if(probs[i] > 1) throwError("Probability can't be greater than 1!");
//    		probs[i + 1] = 1 - probs[i]; 
//    	}
//    	return probs;
//    }
    
    //Tu bang xac suat dieu kien chuyen thanh trong so, n la so nut cha cua nut hien tai, probs.length = pow(2, n) or pow(2, n+1)
	private static double[] binaryWeightsOfProbs(int nParent, double[] probs, double mark, double sum) throws ZebraException {
		double[] weights = new double[nParent];
		if(nParent == 0) return weights;
		
    	if(probs.length != Math.pow(2, nParent) && probs.length != Math.pow(2, nParent + 1)) 
    		throwError("The length of probs is invalid");
		
    	BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(2, nParent);
    	for(int i = 0; i < arr.getNumArrangement(); i++) {
    		int[] a = arr.getArrangement(i);
    		
    		int j = 0;
    		for(j = 0; j < weights.length; j++) {
    			if(checkAllZeroExcept(a, j)) break;
    		}
    		if(j < weights.length) weights[j] = (probs[i] * sum / mark);
    	}
		return weights;
    }
//    //Tu bang xac suat dieu kien chuyen thanh trong so, n la so nut cha cua nut hien tai, probs.length = pow(2, n+1), cach 2
//	private static double[] binaryWeightsOfProbs2(int nParent, double[] probs) throws ZebraException {
//		double[] weights = new double[nParent];
//		if(n == 0) return weights;
//		
//		if(probs.length != Math.pow(2, nParent) && probs.length != Math.pow(2, nParent + 1)) 
//			throwError("The length of probs is invalid");
//		
//    	BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(2, nParent);
//    	for(int i = 0; i < arr.getNumArrangement(); i++) {
//    		int[] a = arr.getArrangement(i);
//    		
//    		int j = 0;
//    		for(j = 0; j < weights.length; j++) {
//    			if(checkAllZeroExcept(a, j)) break;
//    		}
//    		if(j < weights.length) weights[j] = probs[2*i];
//    	}
//		return weights;
//    }
	
	//arr[idx]=1, tat ca cac vi tri con lai la 0
	private static boolean checkAllZeroExcept(int[] arr, int idx) {
		if(idx < 0 || idx >= arr.length) return false;
		
    	double[] values = new double[] {1.0, 0.0};
		for(int i = 0; i < arr.length; i++) {
			if(i != idx) {
				if(values[ arr[i] ] != 0) return false;
			}
			else {
				if(values[ arr[i] ] != 1) return false;
			}
		}
		return true;
	}
}

@SuppressWarnings("unchecked")
class OverlayBayesUM_JavaBayes extends InferenceGraph implements OverlayBayesUM {
	private ConceptGraph cGraph = null;
	private boolean      isChanged = false;
	
	public OverlayBayesUM_JavaBayes() {
		super();
	}
	
	public ConceptGraph getConceptGraph() {return cGraph;}
	public String       getName() { return get_name(); }
	public void         setName(String name) { set_name(name); setChanged(true);}
	
	public OverlayBayesItem[] getItems() {
    	Vector<?> nodes = get_nodes();
    	if(nodes == null) return new OverlayBayesItem[0];
    	int    n = nodes.size();
    	
    	OverlayBayesItem[] items = new OverlayBayesItem[n]; 
    	for(int i=0; i<n; i++) {
    		items[i] = (OverlayBayesItem) (nodes.get(i));
    	}
    	return items;
	}
	
	public OverlayBayesItem getItem(String itemName) {
		OverlayBayesItem[] items = getItems();
		if(items == null || items.length == 0) return null;
    	for(int i = 0; i < items.length; i++) {
    		if(items[i].getName().equals(itemName)) return items[i];
    	}
		return null;
	}
	public void setItemPos(OverlayBayesItem item, Point pos) {
		set_pos((InferenceGraphNode)item, pos); setChanged(true);
	}
	public Object getInnerBayesNet() {
		return get_bayes_net();
	}
	public Vector<?> getNetworkProperties() {return get_network_properties();}
	public int    getGlobalNeighborhoodType() {return get_global_neighborhood_type();}
	public double getGlobalNeighborhoodParameter() {return get_global_neighborhood_parameter();}
	
	public void   setNetworkProperties(Vector<?> prop) {set_network_properties(prop); setChanged(true);}
	public void   setGlobalNeighborhoodType(int type) {set_global_neighborhood(type); setChanged(true);}
	public void   setGlobalNeighborhoodParameter(double parameter) {set_global_neighborhood_parameter(parameter); setChanged(true);}
	
	public String checkName(String n) {return check_name(n);}
	
	public void changeItemValues(OverlayBayesItem item, String values[]) throws ZebraException {
		if(item instanceof InferenceGraphNode) {
			change_values((InferenceGraphNode)item, values); setChanged(true);
		}
		else
			OverlayBayesUMFactory.throwError("Invalid parameter OverlayBayesItem");
	}
	public void resetMarginal() {reset_marginal();}
	public void resetExpectation() {reset_expectation();}
	
	public OverlayBayesItem createItem(String name, OBUM_CONCEPT_NODE_TYPE type) throws ZebraException {
		String[] values = OverlayBayesUMFactory.createValuesForVar(new ConceptNodeTypeWrapper(type));
		
		OverlayBayesItem item = (OverlayBayesItem)create_node(0, 0, values);
		item.setName(name);
		setChanged(true);
		return item;
	}
	public void removeItem(OverlayBayesItem item) {
		delete_node((InferenceGraphNode)item); setChanged(true);
	}
	public boolean createArc(OverlayBayesItem parent, OverlayBayesItem child) {
		setChanged(true);
		return create_arc((InferenceGraphNode)parent, (InferenceGraphNode)child);
	}
	public boolean createArc(OverlayBayesItem parent, OverlayBayesItem child, double weight) throws ZebraException {
		// (1 - quen)/(1 + doan)
		double[] old_weights = child.getParentWeights();
		int n = old_weights.length;
		double[] new_weights = new double[n + 1];
		for(int i = 0; i < n; i++) {
			new_weights[i] = old_weights[i] * ( 1 - weight );
		}
		new_weights[n] =  weight;
		
		if(! createArc(parent, child)) return false;
		child.setParentWeights(new_weights);
		return true;
	}

	public InferenceResult inferMariginalPosterior(String itemName, OBUM_ALGORITHM_TYPE algorithmType) {
		boolean do_compute_clusters = (algorithmType == OBUM_ALGORITHM_TYPE.VARIABLE_ELIMINATION?false:true);
		if( (do_compute_clusters == false) || (qbi == null) || (qbi.areClustersProduced() == false) ) 
	           qbi = new QBInference( get_bayes_net(), do_compute_clusters );
		qbi.inference(itemName);
		MarginalPosteriorResult_JavaBayes result = new MarginalPosteriorResult_JavaBayes(getItem(itemName));
		result.result = qbi.get_result();
		return result;
	}
	
	public InferenceResult inferExpectation(String itemName, OBUM_ALGORITHM_TYPE algorithmType) {
		boolean do_compute_clusters = (algorithmType == OBUM_ALGORITHM_TYPE.VARIABLE_ELIMINATION?false:true);
		if( (do_compute_clusters == false) || (qbe == null) || (qbi.areClustersProduced() == false) )
			qbe = new QBExpectation( get_bayes_net(), do_compute_clusters );
		qbe.expectation(itemName);
		ExpectationResult_JavaBayes result = new ExpectationResult_JavaBayes();
		result.result = qbe.get_results();
		result.current_function = qbe.getCurrentFunction();
		return result;
	}
	public InferenceResult inferExplanation() {
	    Explanation ex = new Explanation( get_bayes_net() );
	    ex.explanation();
	    ExplanationResult_JavaBayes result = new ExplanationResult_JavaBayes();
	    result.result = ex.get_results();
	    result.bayes_net = ex.getBayesNet();
	    result.bucket_tree = ex.getBucketTree();
	    return result;
	}

	public InferenceResult inferFullExplanation() {
	    Explanation ex = new Explanation( get_bayes_net() );
	    ex.full_explanation();
	    ExplanationResult_JavaBayes result = new ExplanationResult_JavaBayes();
	    result.result = ex.get_results();
	    result.bayes_net = ex.getBayesNet();
	    result.bucket_tree = ex.getBucketTree();
	    return result;
	}

	public InferenceResult inferSensitivityAnalysis() {
	    //SensitivityAnalysis sa = new SensitivityAnalysis( get_bayes_net() );
	    //sa.compute(queried_variable);
		return null;//return sa.get_result();
	}

	public void load(InputStream input) throws IOException, IFException {
		clear();
		qbn = new QuasiBayesNet(new
				java.io.DataInputStream(input));
		convert_bayes_net();
		setChanged(false);
	}
	public void load(URL url) throws IOException, IFException {
		clear();
		qbn = new QuasiBayesNet(url);
		convert_bayes_net();
		setChanged(false);
	}
	public void load(String filePath) throws IOException, IFException {
		FileInputStream input = new FileInputStream(filePath);
		load(input);
		input.close();
		setChanged(false);
	}
	public void loadFromDatabase(String userid, String course, boolean isDynamic) throws IOException, IFException {
		String bayesDir = ZebraStatic.getWowXmlRoot() + "/" +  OverlayBayesUM.OBUM_DIR_NAME;
        String filePath = bayesDir + "/" + 
        	(userid == null? "" : userid + "$") + course + (isDynamic?"$dyn":"") + OverlayBayesUM.OBUM_EXT;
		load(filePath);
		setChanged(false);
	}
	public void loadFromAuthor(URL base, String author, String course) throws ZebraException, MalformedURLException, IOException, IFException {
		AuthorService service = new AuthorService(base, author);
		URL url = service.getFileURL(course + OverlayBayesUM.OBUM_EXT);

		load(url);
		setChanged(false);
	}
	public void createFromAuthor(URL base, String author, String course) throws ZebraException, MalformedURLException, Exception {
		clear();
		ArrayList<String> filteredRelationTypes = new ArrayList<String>();
		for(String rType : OverlayBayesUM.OBUM_FILTER_RELATION_TYPES) filteredRelationTypes.add(rType);
		
		ConceptGraph cGraph = ConceptGraph.createFromAuthorFile(base, author, course, filteredRelationTypes);
		if(cGraph.getNumConceptNode() == 0) {
			JOptionPane.showMessageDialog(null, "ConceptGraph is empty");
			this.qbn.set_name(cGraph.getName());
			this.cGraph = cGraph;
			setChanged(false);
			return;
		}
		QuasiBayesNet bayesnet = new QuasiBayesNet();
		
		int n = cGraph.getNumConceptNode();
		ArrayList<ProbabilityVariable> vars = new ArrayList<ProbabilityVariable>();
		ArrayList<ProbabilityFunction> funcs = new ArrayList<ProbabilityFunction>();
		ArrayList<ProbabilityVariable> hasfunc = new ArrayList<ProbabilityVariable>();
		//browse all ConceptNode
		for(int i=0; i<n; i++) {
			ConceptNode node = cGraph.getConceptNode(i);
        	
        	//Var da co chua?
        	ProbabilityVariable var = null;
			for(int k=0; k<vars.size(); k++) {
				if(vars.get(k).get_name().equals(node.getName())) {
					var = vars.get(k);
					break;
				}
			}
			
			//Chua co, them var vao
			if(var == null) {
	        	Vector<String>       properties = null;
	        	ConceptCoord coord = (ConceptCoord)(node.attr.get("coord")); 
	        	Rectangle    rect = (coord == null ? null : coord.rect);
	        	properties = new Vector<String>();
	        	if(rect != null) {
		        	properties.add("position = (" + rect.x + ", " + rect.y + ")");
		        	properties.add("bound = (" + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height + ")");
	        	}
	        	properties.add("type = " + node.getConceptType());
	        	
	        	String[] values = OverlayBayesUMFactory.createValuesForVar(new ConceptNodeTypeWrapper(node));
	        	var = new ProbabilityVariable(bayesnet, node.getName(), values.length, values, properties);
	        	if(node.isEvidence()) {
	        		String[] v = var.get_values();
	        		var.set_observed_value(v[v.length - 1]);
	        	}
				vars.add(var);
			}
			else if(hasfunc.contains(var)) continue; //neu var da co func thi bo qua
			
			
			//tao pvars va pweight cua var hien hanh
        	int r = node.parents.size();
        	ProbabilityVariable[] pvars = new ProbabilityVariable[r+1];
        	double[]              pweights = new double[r];
        	pvars[0] = var;

        	for(int j = 1; j < r+1; j++) {
    			ConceptNode pnode = node.parents.get(j-1).node;
    			if(OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(new ConceptNodeTypeWrapper(pnode)) != 2) {
    				OverlayBayesUMFactory.throwError("Not implementing for situation that the non-evidence variable isn't binary");
    			}
    			
            	ProbabilityVariable pvar = null;
    			for(int k=0; k<vars.size(); k++) {
    				if(vars.get(k).get_name().equals(pnode.getName())) {
    					pvar = vars.get(k);
    					break;
    				}
    			}
    			
    			if(pvar == null) {
    	        	Vector<String>       properties = null;
    	        	ConceptCoord coord = (ConceptCoord)(pnode.attr.get("coord")); 
    	        	Rectangle    rect = coord==null?null:coord.rect;
    	        	if(rect != null) {
    		        	int         x=rect.x + rect.width/2, y=rect.y + rect.height/2;
    		        	properties = new Vector<String>();
    		        	properties.add("position = (" + x + ", " + y + ")");
    	        	}
    	        	
    	        	if(pnode.isEvidence())
    	        		OverlayBayesUMFactory.throwError("The situation that evidences are parents isn't implemented yet");
    	        	
    	        	String[] values = OverlayBayesUMFactory.createValuesForVar(new ConceptNodeTypeWrapper(pnode));
    	        	pvar = new ProbabilityVariable(bayesnet, pnode.getName(), values.length, values, properties);
    				vars.add(pvar);
    			}
    			
            	pvars[j] = pvar;
            	pweights[j-1] = node.parents.get(j-1).weight;
        	}
        	
        	ProbabilityFunction f = null;
        	if(r == 0) {
        		f = new ProbabilityFunction(bayesnet, pvars, 
        				OverlayBayesUMFactory.getNoParentProbs(new ConceptNodeTypeWrapper(node)), 
        				null);
        	}
        	else {
	        	f = new ProbabilityFunction(bayesnet, pvars, 
	        			OverlayBayesUMFactory.getProbsOfWeights(pweights, new ConceptNodeTypeWrapper(node)), 
	        			null);
        	}
        	funcs.add(f); hasfunc.add(var);
			
		}
		ProbabilityVariable[] a_var = new ProbabilityVariable[vars.size()];   a_var = vars.toArray(a_var);
		ProbabilityFunction[] a_func = new ProbabilityFunction[funcs.size()]; a_func = funcs.toArray(a_func);
		
		if(a_var.length != a_func.length)
			OverlayBayesUMFactory.throwError("Invalid Parameters in OverlayBayesUM_JavaBayes.createFromAuthor");
		bayesnet.set_name(cGraph.getName());
		bayesnet.set_probability_variables(a_var);
		bayesnet.set_probability_functions(a_func);
		
		this.qbn = bayesnet; 
	    convert_bayes_net();
		this.cGraph = cGraph;
		setChanged(false);
	}

	public void save(PrintStream out, OBUM_FILE_FORMAT_TYPE format) throws ZebraException {
		if(format == OBUM_FILE_FORMAT_TYPE.BIF) {
			save_bif(out); setChanged(false);
		}
		else if(format == OBUM_FILE_FORMAT_TYPE.XML) {
			save_xml(out); setChanged(false);
		}
		else OverlayBayesUMFactory.throwError("Not yet support this format type");
	}
	
	public void saveToDatabase(String userid, String course, OBUM_FILE_FORMAT_TYPE format, boolean isDynamic)  throws FileNotFoundException, ZebraException{
        File root = new File(ZebraStatic.getWowXmlRoot()  + "/" + OverlayBayesUM.OBUM_DIR_NAME);
        if (!root.exists()) root.mkdir();
        
        String ext = OverlayBayesUMFactory.getFormatExt(format);
		
        PrintStream out = new PrintStream(new File(root, 
        		(userid == null? "" : userid + "$") + course + (isDynamic?"$dyn":"") + ext));
		if(format == OBUM_FILE_FORMAT_TYPE.BIF)
        	save(out, OBUM_FILE_FORMAT_TYPE.BIF);
		else if(format == OBUM_FILE_FORMAT_TYPE.XML)
        	save(out, OBUM_FILE_FORMAT_TYPE.XML);
        out.flush(); out.close();
	}
	public void saveToAuthor(URL base, String author, String course, OBUM_FILE_FORMAT_TYPE format, boolean isSaveToDatabase)  throws FileNotFoundException, ZebraException {
        String ext = OverlayBayesUMFactory.getFormatExt(format);
        
  		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  		PrintStream out = new PrintStream(bytes);
		if(format == OBUM_FILE_FORMAT_TYPE.BIF)
        	save(out, OBUM_FILE_FORMAT_TYPE.BIF);
		else if(format == OBUM_FILE_FORMAT_TYPE.XML)
        	save(out, OBUM_FILE_FORMAT_TYPE.XML);
  		out.flush(); out.close();
  		
  		byte[] data = bytes.toByteArray();
  		
  		String fileName = course + ext;
  		AuthorService service = new AuthorService(base, author);
  		service.saveFile(data, fileName, false);
  		if(isSaveToDatabase) service.saveFile(data, fileName, true);
	}
	public void clear() {
		qbn = new QuasiBayesNet();
		qbi = null;
		qbe = null;
		nodes = new Vector<Object>();
		setChanged(true);
	}
	public Object clone() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytes);
		try {
			save(out, OBUM_FILE_FORMAT_TYPE.BIF);
			out.flush(); out.close();

	  		byte[] data = bytes.toByteArray();
	  		ByteArrayInputStream input = new ByteArrayInputStream(data);
	  		OverlayBayesUM_JavaBayes um = new OverlayBayesUM_JavaBayes();
	  		um.load(input);
	  		return um;
		}
		catch(Exception e) {ZebraStatic.traceService.trace("Error caused by: " + e.getMessage()); e.printStackTrace();}
		return null;
	}
	
    public void betaLearningByEM(ArrayList<String> attrs, ArrayList<double[]> dataset, double maxProbability) throws ZebraException {
    	if(dataset.size() == 0) {ZebraStatic.traceService.trace("Training data in EM algorithm is empty"); return;}
    	if(attrs.size() == 0) {ZebraStatic.traceService.trace("Attribute EM algorithm is empty"); return;}
    	
    	OverlayBayesItem[] items = getItems();
    	if(items.length == 0) {ZebraStatic.traceService.trace("The number of nodes is zero"); return;}
    	
    	ArrayList<OverlayBayesItemBetaDensity> itembetas = new ArrayList<OverlayBayesItemBetaDensity>();
    	for(OverlayBayesItem item : items) {
    		if(!item.isObserved()) {
    			itembetas.add(new OverlayBayesItemBetaDensity(item));
    		}
    	}
    	for(double[] data : dataset) {
    		//E.g: data = 111010
    		//data[j] = -1: i attribute missing
    		for(OverlayBayesItemBetaDensity itembeta : itembetas) {
    			int j = attrs.indexOf(itembeta.getItem().getName());
    			if(j == -1)       continue;
    			if(data[j] == -1) continue;
    			
    			BetaDensity beta = (itembeta.hasParent() ? itembeta.getBeta(attrs, data) : itembeta.getOneBeta());
    			if(beta == null) continue;
    			if(beta.expectation() > maxProbability) continue; //Expectation of beta distribution is the probability
    			if(data[j] == 0) beta.incB();
    			else             beta.incA();
    			
    		}
    	}
		for(OverlayBayesItemBetaDensity itembeta : itembetas) {
			itembeta.updateBackItem();
		}
		setChanged(true);
    }
    
    public void betaLearningByMLE(ArrayList<String> attrs, ArrayList<double[]> dataset, double maxProbability) throws ZebraException {
		if(dataset.size() == 0) {ZebraStatic.traceService.trace("Training data in EM algorithm is empty"); return;}
    	if(attrs.size() == 0) {ZebraStatic.traceService.trace("Attribute EM algorithm is empty"); return;}
    	
    	OverlayBayesItem[] items = getItems();
    	if(items.length == 0) {ZebraStatic.traceService.trace("The number of nodes is zero"); return;}
    	
    	ArrayList<OverlayBayesItemBetaDensity> itembetas = new ArrayList<OverlayBayesItemBetaDensity>();
    	for(OverlayBayesItem item : items) {
    		if(!item.isObserved()) {
    			itembetas.add(new OverlayBayesItemBetaDensity(item));
    		}
    	}
    	for(double[] data : dataset) {
    		//data[j] = -1: i attribute missing
    		for(OverlayBayesItemBetaDensity itembeta : itembetas) {
    			int j = attrs.indexOf(itembeta.getItem().getName());
    			if(j == -1) continue;
    			if(data[j] == -1) continue;
    			
    			BetaDensity beta = (itembeta.hasParent() ? itembeta.getBeta(attrs, data) : itembeta.getOneBeta());
    			if(beta == null) continue;
    			if(beta.expectation() > maxProbability) continue; //Expectation of beta distribution is the probability
    			beta.setSum1( beta.getSum1() + ( (data[j] == 0)? BetaDensity.NEGATIVE_INFINITY : BetaDensity.POSSITIVE_INFINITY) );
    			beta.setSum2( beta.getSum2() + ( (1 - data[j] == 0)? BetaDensity.NEGATIVE_INFINITY : BetaDensity.POSSITIVE_INFINITY) );
    		}
    	}
		for(OverlayBayesItemBetaDensity itembeta : itembetas) {
			for(BetaDensity beta : itembeta.getBetas()) {
				beta.updateParamsFromSum(dataset.size());
			}
		}
    	
		for(OverlayBayesItemBetaDensity itembeta : itembetas) {
			itembeta.updateBackItem();
		}
		setChanged(true);
    }
    public boolean isChanged() {
    	if(isChanged) return true;
    	OverlayBayesItem[] items = getItems();
    	for(OverlayBayesItem item : items) {
    		if(item.isChanged()) return true;
    	}
    	return false;
    }
    public void setChanged(boolean isChanged) {
    	this.isChanged = isChanged;
    	if(!isChanged) {
        	OverlayBayesItem[] items = getItems();
        	for(OverlayBayesItem item : items) {
        		item.setChanged(isChanged);
        	}
    	}
    }
	
	//rat can thiet cho JavaBayes
	public InferenceGraphNode newInferenceGraphNode(InferenceGraph i_g, ProbabilityVariable p_v, ProbabilityFunction p_f) {
		InferenceGraphNode ign = super.newInferenceGraphNode(i_g, p_v, p_f);
		return (InferenceGraphNode) (copyFrom(ign));
	}
	public InferenceGraphNode newInferenceGraphNode(InferenceGraph i_g, String name, Point position) {
		InferenceGraphNode ign = super.newInferenceGraphNode(i_g, name, position);
		return (InferenceGraphNode) (copyFrom(ign));
	}
	public InferenceGraphNode newInferenceGraphNode(InferenceGraph i_g, String name, String[] values, Point position) {
		InferenceGraphNode ign = super.newInferenceGraphNode(i_g, name, values, position);
		return (InferenceGraphNode) (copyFrom(ign));
	}
	public InferenceGraphNode newInferenceGraphNode() {
		InferenceGraphNode ign = super.newInferenceGraphNode();
		return (InferenceGraphNode) (copyFrom(ign));
	}

	public static OverlayBayesItem copyFrom(InferenceGraphNode ign) {
		OverlayBayesItem_JavaBayes newItem = new OverlayBayesItem_JavaBayes();

		newItem.ig = ign.ig;
		newItem.pv = ign.pv;
		newItem.pf = ign.pf;
		newItem.parents = ign.parents;
		newItem.children = ign.children;
		newItem.pos = ign.pos;
		return newItem;
    }
}

@SuppressWarnings("unchecked")
class OverlayBayesItem_JavaBayes extends InferenceGraphNode implements OverlayBayesItem {
	private boolean isChanged = false;
	
	public OverlayBayesItem_JavaBayes() {
		super();
	}
	
	public OverlayBayesItem query(Object item) {
		return OverlayBayesUM_JavaBayes.copyFrom((InferenceGraphNode)item);
	}
	
    public String getName() { return get_name(); }
    public int    getNumberValues() {return get_number_values();}
    
    public String[] getValues() {  //such as: "true", "false"
    	return get_values();
    }
    public int getObservedValue() {return get_observed_value();}
    
    public Point getPos() {
    	Point pos = new Point();
    	pos.x = get_pos_x();
    	pos.y = get_pos_y();
    	return pos;
    }
	public void setPos(Point pos) {
    	if(pos.x < 0 || pos.y < 0) return;
    	Vector<Object> properties = get_variable_properties();
    	if(properties == null) {
    		properties = new Vector<Object>();
    		set_variable_properties(properties);
    	}
    	String property = "position = (" + pos.x + ", " + pos.y + ")";
		
    	boolean shouldAdd = true;
    	for(int i = 0; i < properties.size(); i++) {
			String temp_property = ((String)(properties.get(i))).trim();
			if (! temp_property.startsWith("position")) continue;
			properties.set(i, property);
			shouldAdd = false;
			break;
		}
    	if(shouldAdd) properties.add(property);
    	this.pos = pos;
    	update_position();
    	setChanged(true);
    }
    public void setBound(Rectangle bound) {
    	if(bound.x < 0 || bound.y < 0 || bound.width <= 0 || bound.height <= 0) return;
    	Vector<Object> properties = get_variable_properties();
    	if(properties == null) {
    		properties = new Vector<Object>();
    		set_variable_properties(properties);
    	}
    	String property = "bound = (" + bound.x + ", " + bound.y + ", " 
    		+ bound.width + ", " + bound.height + ")";
		
    	boolean shouldAdd = true;
    	for(int i = 0; i < properties.size(); i++) {
			String temp_property = ((String)(properties.get(i))).trim();
			if (! temp_property.startsWith("bound")) continue;
			properties.set(i, property);
			shouldAdd = false;
			break;
		}
    	if(shouldAdd) properties.add(property);
    	setChanged(true);
    }
    public Rectangle getBound() {
    	Rectangle bound = new Rectangle();
    	bound.x = bound.y = bound.width = bound.height = -1;
    	
    	Vector<?> properties = get_variable_properties();

    	// get position values from the list of properties
    	if (properties == null || properties.size() == 0)
	        return null;

		try {
			for (Enumeration<?> e = properties.elements(); e.hasMoreElements(); ) {
				String s = ((String) e.nextElement()).trim();

				// If property is not position, skip it
				if (! s.startsWith("bound")) continue;

				// Parse the position property
				StreamTokenizer st = new StreamTokenizer(new StringReader(s));
				st.parseNumbers();
				int tok = 0;
				while ((tok = st.nextToken()) != StreamTokenizer.TT_EOF) {
					if (tok != StreamTokenizer.TT_NUMBER)
						continue;
					if(bound.x == -1)
						bound.x = (int) st.nval;
					else if(bound.y == -1) {
						bound.y = (int) st.nval;
					}
					else if(bound.width == -1) {
						bound.width = (int) st.nval;
					}
					else {
						bound.height = (int) st.nval;
					}
				}
				if(bound.x==-1 || bound.y==-1 || bound.width==-1 || bound.height==-1) return null;
				return bound;
			}
		}
		catch (IOException e) {
			return null;
		}

        return null;
    }
   
    public boolean hasObserved() {
    	OverlayBayesItem[] children = getChildren();
    	if(children == null || children.length == 0) return false;
    	for(OverlayBayesItem child : children) {
    		if(child.isObserved()) return true;
    	}
		return false;
	}

	public OverlayBayesItem[] getParents() {
    	Vector<?> temp_parents = get_parents();
    	int    n = temp_parents.size();
    	
    	OverlayBayesItem[] parents = new OverlayBayesItem[n]; 
    	for(int i=0; i<n; i++) {
    		parents[i] = (OverlayBayesItem) (temp_parents.get(i));
    	}
    	return parents;
    }
	public double[] getParentWeights() throws ZebraException {
		return OverlayBayesUMFactory.getWeightsOfProbs(
				getParents().length, 
				getFunctionValues(),
				new ConceptNodeTypeWrapper(this));
	}
	public void setParentWeights(double[] weights) throws ZebraException {
		if(!hasParent()) return;
		double[] pweights2 = getParentWeights();
		if(pweights2.length != weights.length)
			OverlayBayesUMFactory.throwError("Invalid weights parameter");
		double[] probs = OverlayBayesUMFactory.getProbsOfWeights(weights, new ConceptNodeTypeWrapper(this));
		setFunctionValues(probs);
	}
    public OverlayBayesItem getParent(String itemName) {
    	OverlayBayesItem[] parents = getParents(); 
    	int                n = parents.length;
    	
    	for(int i=0; i<n; i++) {
    		if(parents[i].getName().equals(itemName)) return parents[i];
    	}
    	return null;
    }
    
    public OverlayBayesItem[] getChildren() {
    	Vector<?> temp_children = get_children();
    	int    n = temp_children.size();
    	
    	OverlayBayesItem[] children = new OverlayBayesItem[n]; 
    	for(int i=0; i<n; i++) {
    		children[i] = (OverlayBayesItem) (temp_children.get(i));
    	}
    	return children;
    }
    public OverlayBayesItem getChild(String itemName) {
    	OverlayBayesItem[] children = getChildren(); 
    	int                n = children.length;
    	
    	for(int i=0; i<n; i++) {
    		if(children[i].getName().equals(itemName)) return children[i];
    	}
    	return null;
    }

    public double[] getFunctionValues() { return get_function_values();}
    public double[] getFunctionValues(int index) {return get_function_values(index);}

    public String[]   getAllNames() {return get_all_names();}
    public String[][] getAllValues() {return get_all_values();};
    
    public Vector<?> getVariableProperties() {return get_variable_properties();}
    public Vector<?> getFunctionProperties() {return get_function_properties();}

    public boolean isObserved() {return is_observed();}
    public boolean isExplanation() {return is_explanation();}
    public boolean isCredalSet() {return is_credal_set();}

    public int numberExtremeDistributions() {return number_extreme_distributions();}
    
    public void setName(String n) {set_name(n); setChanged(true);}
    public void setExplanation(boolean flag) {set_explanation(flag); setChanged(true);}
    public void setLocalCredalSet() {set_local_credal_set(); setChanged(true);}
    public void setLocalCredalSet(int number_extreme_points) {set_local_credal_set(number_extreme_points); setChanged(true);}    
    public void setNoLocalCredalSet() {set_no_local_credal_set(); setChanged(true);}
    public void setObservedValue(String value) {
		int idx = getObservedValue();
		if(idx != -1 && idx < getValues().length) {
			if(!getValues()[idx].equals(value)) setChanged(true);
		}
    	set_observation_value(value);
    }
    public void clearObserved() {clear_observation(); setChanged(true);}

    public void setVariableProperties(Vector<?> prop) {set_variable_properties(prop); setChanged(true);}
    public void setFunctionProperties(Vector<?> prop) {set_function_properties(prop); setChanged(true);}
    public void setFunctionValues(double[] fv) {set_function_values(fv); setChanged(true);}
    public void setFunctionValues(int iep, double[] fv) {set_function_values(iep, fv); setChanged(true);}

    
    public void updatePositionFromProperty(String s) {update_position_from_property(s); setChanged(true);}

    public boolean isChanged() {return isChanged;}
    public void    setChanged(boolean isChanged) {this.isChanged = isChanged;}

    public String toString() {return getName();}
    
}
class MarginalPosteriorResult_JavaBayes implements InferenceResult {
	ProbabilityFunction result = null;
	OverlayBayesItem item = null;
	public MarginalPosteriorResult_JavaBayes(OverlayBayesItem item) {
		this.item = item;
	}
	public Object getResult() {return result;}
	public String toString() {
		if(result == null) return "";
		String  sResult = "Posterior distribution:";
		sResult += result.toString();
		return sResult;
	}
	public double[] getResultDouble() throws ZebraException {
		if(item == null || !item.isObserved()) return result.get_values();
		double[] values = result.get_values();
		int maxIdx = -1;
		double maxValue = -1;
		for(int i = 0; i < values.length; i++) {
			if(maxValue < values[i]) {
				maxValue = values[i];
				maxIdx = i;
			}
		}
		if(maxIdx == -1) return result.get_values();
		
		ConceptNodeTypeWrapper cNodeType = new ConceptNodeTypeWrapper(item);
		int n = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(cNodeType) - 1;
		double value = ((double)(n - maxIdx)) / ((double)n);
		return new double[] {value};
	}
}
class ExpectationResult_JavaBayes implements InferenceResult {
	double[]         result = null;
	DiscreteFunction current_function = null;
	public Object getResult() {return result;}
	public String toString() {
		if(result == null) return "";

		String sResult = "Posterior expectation: [";
		for(int i=0; i<result.length; i++)
			sResult += result[i] + " ";
		sResult += "], for function:\n";
		sResult += current_function.toString();
		return sResult;
	}
	public double[] getResultDouble() throws ZebraException {
		return result;
	}
}
class ExplanationResult_JavaBayes implements InferenceResult {
	ProbabilityFunction[] result = null;
	BucketTree bucket_tree = null;
	BayesNet bayes_net = null;
	public Object getResult() {return result;}
	public String toString() {
		if(result == null) return "";
		String sResult = "Explanation:\n";

		if (bucket_tree.getBackwardPointers() == null) {
			sResult += "No explanatory variable; posterior distribution:\n";
			for(int i=0; i<result.length; i++) {
				sResult += result[i].toString(); 
			}
		}
		else {
			int[] bp = bucket_tree.getBackwardPointers();
			for(int i=0; i < bp.length; i++) {
				if (bp[i] != BayesNet.INVALID_INDEX) {
					ProbabilityVariable pv = bayes_net.get_probability_variable(i);
					sResult += "Variable " + pv.get_name() + ": " +
							pv.get_value(bp[i]);
					if(i < bp.length - 1) sResult += "\n";
				}
			}
		}
		return sResult;
	}
	public double[] getResultDouble() throws ZebraException {
		OverlayBayesUMFactory.throwError("Not implement yet");
		return null;
	}
}
