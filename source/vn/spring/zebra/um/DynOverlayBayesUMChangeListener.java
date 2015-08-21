package vn.spring.zebra.um;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface DynOverlayBayesUMChangeListener {
	void stateAdded(DynOverlayBayesUMChangeEvent evt);
	void stateCleared(DynOverlayBayesUMChangeEvent evt);
	void stateReloaded(DynOverlayBayesUMChangeEvent evt);
}
