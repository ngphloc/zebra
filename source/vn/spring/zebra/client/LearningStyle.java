package vn.spring.zebra.client;

import java.io.Serializable;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class LearningStyle implements Serializable {
	private static final long serialVersionUID = 1L;

	public boolean isVerbalizer = false; 
	public boolean isActivist = false; 
	public boolean isTheorist = false; 
	
	public LearningStyle() {}
	public LearningStyle(boolean isVerbalizer, boolean isActivist, boolean isTheorist) {
		this.isVerbalizer = isVerbalizer;
		this.isActivist = isActivist;
		this.isTheorist = isTheorist;
	}
	public String toString() {
		String style = "";
		
		if(isVerbalizer) style += "verbalizer,";
		else             style += "visualizer,";
		
		if(isActivist)   style += "activist,";
		else             style += "reflector,";

		if(isTheorist)   style += "theorist";
		else             style += "pragmatist";
		
		return style;
	}
}
