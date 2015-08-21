package vn.spring.zebra.um;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.*;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.bayes.InferenceGraphs.*;
import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface OverlayBayesUM {
	static enum  OBUM_BAYESNET_TYPE     {JAVABAYES, BNJ, ELVIRA}
	static enum  OBUM_INFERENCE_TYPE    {MARGINAL_POSTERIOR, EXPECTATION, EXPLANATION, FULL_EXPLANATION, SENSITIVITY_ANALYSIS}
	static enum  OBUM_ALGORITHM_TYPE    {VARIABLE_ELIMINATION, BUCKET_TREE}
	static enum  OBUM_FILE_FORMAT_TYPE  {BIF, XML}
	static enum  OBUM_CONCEPT_NODE_TYPE {NON_EVIDENCE, TEMP_EVIDENCE, TEST_EVIDENCE}
	
	final static OBUM_BAYESNET_TYPE    OBUM_DEFAULT_BAYESNET_TYPE = OBUM_BAYESNET_TYPE.JAVABAYES;
	final static OBUM_ALGORITHM_TYPE   OBUM_DEFAULT_ALGORITHM_TYPE = OBUM_ALGORITHM_TYPE.VARIABLE_ELIMINATION;
	
	final static String[]   OBUM_FILTER_RELATION_TYPES = {"knowledge_propagation", "test", "evidencetemp"};
	final static String[]   OBUM_DEFAULT_NONEVIDENCE_VARIABLE_VALUES = new String[] {"true", "false"};
	final static String[]   OBUM_DEFAULT_TESTEVIDENCE_VARIABLE_VALUES = new String[] 
                              {"10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
	final static int        OBUM_DEFAULT_TEST_MAX_KNOWLEDG = 100; //test score
	
	final static String     OBUM_ROBSERVED = "robserved";//this reversed observation attribute: 0=value[0]="true", 1=value[1]=false
	final static String     OBUM_EXT = ".bif";
	final static String     OBUM_DIR_NAME = "bayes";
	final static String     OBUM_ATTR_STATIC_BAYES_POSTERIOR_INFER_PREFIX = "do$bayes$infer";//do$bayes$infer, do$bayes$infer$0, do$bayes$infer$1, do$bayes$infer$valueidx
	final static String     OBUM_ATTR_DYN_BAYES_POSTERIOR_INFER_PREFIX = "do$dynbayes$infer";//do$dynbayes$infer, do$dynbayes$infer$0, do$dynbayes$infer$1, do$dynbayes$infer$valueidx

	ConceptGraph getConceptGraph();
	String       getName();
	void         setName(String name);
	String       checkName(String n);//check name
	
	OverlayBayesItem[] getItems();
	OverlayBayesItem   getItem(String itemName);
	void               setItemPos(OverlayBayesItem item, Point pos);
	
	Object getInnerBayesNet();
	Vector<?> getNetworkProperties();
	int    getGlobalNeighborhoodType();
	double getGlobalNeighborhoodParameter();
	
	void   setNetworkProperties(Vector<?> prop);
	void   setGlobalNeighborhoodType(int type);
	void   setGlobalNeighborhoodParameter(double parameter);
	
	void   changeItemValues(OverlayBayesItem item, String values[]) throws ZebraException;
	void   resetMarginal();
	void   resetExpectation();
	
	OverlayBayesItem createItem(String name, OBUM_CONCEPT_NODE_TYPE type) throws ZebraException;
	void   removeItem(OverlayBayesItem item);
	boolean createArc(OverlayBayesItem parent, OverlayBayesItem child);
	boolean createArc(OverlayBayesItem parent, OverlayBayesItem child, double weight) throws ZebraException;
	
	InferenceResult inferMariginalPosterior(String itemName, OBUM_ALGORITHM_TYPE algorithmType);
	InferenceResult inferExpectation(String itemName, OBUM_ALGORITHM_TYPE algorithmType);
	InferenceResult inferExplanation();
	InferenceResult inferFullExplanation();
	InferenceResult inferSensitivityAnalysis();
	
	void load(InputStream input) throws IOException, IFException;
	void load(String filePath) throws IOException, IFException;
	void load(URL url) throws IOException, IFException;
	//If isDynamic=true: userid$course$dyn.bif, isDynamic=false: userid$course.bif
	void loadFromDatabase(String userid, String course, boolean isDynamic) throws IOException, IFException;
	void loadFromAuthor(URL base, String author, String course) throws ZebraException, MalformedURLException, IOException, IFException;
	void createFromAuthor(URL base, String author, String course) throws ZebraException, MalformedURLException, Exception;
	void save(PrintStream out, OBUM_FILE_FORMAT_TYPE format) throws ZebraException;
	//If isDynamic=true: userid$course$dyn.bif, isDynamic=false: userid$course.bif
	void saveToDatabase(String userid, String course, OBUM_FILE_FORMAT_TYPE format, boolean isDynamic)  throws FileNotFoundException, ZebraException;
	//If isSaveToDatabase=true: save in directory bayes/course.bif
	void saveToAuthor(URL base, String author, String course, OBUM_FILE_FORMAT_TYPE format, boolean isSaveToDatabase)  throws FileNotFoundException, ZebraException;
	
	void clear();
	Object clone();
    void betaLearningByEM(ArrayList<String> attrs, ArrayList<double[]> dataset, double maxProbability) throws ZebraException;
    void betaLearningByMLE(ArrayList<String> attrs, ArrayList<double[]> dataset, double maxProbability) throws ZebraException;
    
    boolean isChanged();
    void    setChanged(boolean isChanged);
}
