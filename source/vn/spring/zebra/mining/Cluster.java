package vn.spring.zebra.mining;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.ParserException;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.WOW.parser.UMVariableLocator;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class Cluster {
	private double[] values = null;
	
	public String toString() {
		String str = "";
		for(int i =0; (values != null && i < values.length); i++) {
			str += String.valueOf(values[i]);
			if(i < values.length - 1) str += ",";
		}
		return str;
	}
	public Cluster(double[] values) {
		this.values = values;
	}
	public double get(int index) {return values[index];}
	
	public double distance(double[] values) throws ZebraException {
		if(this.values.length != values.length) 
			throw new ZebraException("Can't compute distance between two clusters having different dimension");
		
		double dis = 0;
		for(int i = 0; i < this.values.length; i++) {
			dis += Math.abs(this.values[i] - values[i]);
		}
		return dis;
	}
	public double distance(Cluster cluster) throws ZebraException {
		return distance(cluster.values);
	}
	public static Cluster create(Profile profile, String[] vars) {
		if(profile == null || vars.length == 0) return null;
		
		UMVariableLocator locator = new UMVariableLocator(profile, ZebraStatic.getConceptDB());
		double[] values = new double[vars.length];
		for(int i = 0; i < vars.length; i++) {
			try {
				String value = locator.getVariableValue(vars[i]).toString();
				double v = Double.parseDouble(value);
				values[i] = v;
			}
			catch(ParserException e) {
				//ZebraConfig.traceService.trace("Can not determine attribute (Cluster.create): " + e.getMessage());
				return null;
			}
			catch(NumberFormatException e) {
				//ZebraConfig.traceService.trace("Can not determine attribute (Cluster.create): " + e.getMessage());
				return null;
				
			}
		}
		return new Cluster(values);
	}
}
