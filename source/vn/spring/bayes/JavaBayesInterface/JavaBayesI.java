package vn.spring.bayes.JavaBayesInterface;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface JavaBayesI {
	  /**
	   * Open a file and read the network in it. 
	   */
	  public boolean open(String filename);

	  /**
	   * Open a URL and read the network in it.
	   */
	  public boolean open_url(String filename);

	  /**
	   * Clear the network.
	   */
	  public void clear();

	  /**
	   * Save the network.
	   */
	  public boolean save();

	  /**
	   * Save the network.
	   */
	  public boolean save(String filename);

	  /**
	   * Interact with menu options: whether to show BucketTree.
	   */
	  public void what_to_show_bucket_tree_action(boolean what_to_show_bucket_tree);

	  /**
	   * Interact with menu options: whether to show bayesian networks.                                                *
	   */
	  public void what_to_show_bayesian_network_action(boolean what_to_show_bayesian_network);

	 /**
	   * Inferences produce expectations.   
	   */
	  public void posterior_expectation_action();

	  /**
	   * Inferences produce posterior marginals.
	   */
	  public void posterior_marginal_action();

	  /**
	   * Estimate explanation variables.
	   */
	  public void estimate_explanation_variables_action();

	  /**
	   * Produce the estimates for the best configuration.     
	   */
	  public void estimate_best_configuration_action();

	  /**
	   * Produce sensitivity analysis.
	   */
	  public void sensitivity_analysis_action();
	  
	  /**
	   * Use bif format for saving.  
	   */
	  public void bif_format_action();

	  /**
	   * Use xml format for saving. 
	   */
	  public void xml_format_action();

	  /**
	   * Use bugs format for saving.
	   */
	  public void bugs_format_action();

	  /**
	   * Quit gracefully.
	   */
	  public void quit();

	  /**
	   * Put text in the consoleFrame.
	   */
	  public void appendText(String s);
	  
	  /**
	   * Get the current filename for saving.
	   */
	  public String get_current_save_filename();
	  
	  /**
	   * Set the current filename for saving.
	   */
	  public void set_current_save_filename(String filename);
	  
	  /**
	   * Set the inference algorithm as variable elimination.
	   */
	  public void set_algorithm_variable_elimination();

	  
	  /**
	   * Set the inference algorithm as bucket tree.
	   */
	  public void set_algorithm_bucket_tree();
	  
	  public boolean isApplet();
}
