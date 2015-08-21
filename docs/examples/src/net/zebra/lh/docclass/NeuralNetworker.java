package net.zebra.lh.docclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Loc Nguyen
 *
 */
public class NeuralNetworker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Map<String, Float>> corpus = new ArrayList<>();
		Map<String, Float> d1 = new HashMap<>();
		d1.put("C", 0.5f);
		d1.put("P", 0.3f);
		d1.put("A", 0.1f);
		d1.put("D", 0.1f);
		d1.put("L", 1f);
		corpus.add(d1);
		
		Map<String, Float> d2 = new HashMap<>();
		d2.put("C", 0.05f);
		d2.put("P", 0.05f);
		d2.put("A", 0.4f);
		d2.put("D", 0.5f);
		d2.put("L", 0f);
		corpus.add(d2);

		Map<String, Float> d3 = new HashMap<>();
		d3.put("C", 0.2f);
		d3.put("P", 0.05f);
		d3.put("A", 0.2f);
		d3.put("D", 0.55f);
		d3.put("L", 0f);
		corpus.add(d3);
		
		Map<String, Float> d4 = new HashMap<>();
		d4.put("C", 0.2f);
		d4.put("P", 0.55f);
		d4.put("A", 0.05f);
		d4.put("D", 0.2f);
		d4.put("L", 1f);
		corpus.add(d4);
		
		Map<String, Float> d5 = new HashMap<>();
		d5.put("C", 0.15f);
		d5.put("P", 0.15f);
		d5.put("A", 0.4f);
		d5.put("D", 0.3f);
		d5.put("L", 0f);
		corpus.add(d5);
		
		Map<String, Float> d6 = new HashMap<>();
		d6.put("C", 0.35f);
		d6.put("P", 0.1f);
		d6.put("A", 0.45f);
		d6.put("D", 0.1f);
		d6.put("L", 1f);
		corpus.add(d6);
		
		float WCS=0.7f, WCM=0.3f;
		float WPS=0.6f, WPM=0.4f;
		float WAS=0.4f, WAM=0.6f;
		float WDS=0.3f, WDM=0.7f;
		float WSL=0.8f, WML=0.2f;
		
		float OC=0, OP=0, OA=0, OD=0;
		float IS=0, OS=0;
		float IM=0, OM=0;
		float IL=0, OL=0;

		float VL=0;
		
		float ThetaS=0, ThetaM=0, ThetaL=0;
		float ErrS=0, ErrM=0, ErrL=0;
		float l=1f;
		
		for(Map<String, Float> d : corpus) {
			OC = d.get("C");
			OP = d.get("P");
			OA = d.get("A");
			OD = d.get("D");
			VL = d.get("L");
			System.out.println("OC=IC=" + OC + ", OP=IP=" + OP + ", OA=IA=" + OA + ", OD=ID=" + OD + ", VL=" + VL);
			
			IS = WCS*OC + WPS*OP + WAS*OA + WDS*OD + ThetaS;
			OS = (float) (1.0/(1.0 + Math.exp(-IS)));
			System.out.println("IS=" + IS + ", OS=" + OS);
			
			IM = WCM*OC + WPM*OP + WAM*OA + WDM*OD + ThetaM;
			OM = (float) (1.0/(1.0 + Math.exp(-IM)));
			System.out.println("IM=" + IM + ", OM=" + OM);

			IL = WSL*OS + WML*OM + ThetaL;
			OL = (float) (1.0/(1.0 + Math.exp(-IL)));
			System.out.println("IL=" + IL + ", OL=" + OL);
			
			ErrL = OL*(1-OL)*(VL-OL);
			ErrS = OS*(1-OS)*ErrL*WSL;
			ErrM = OM*(1-OM)*ErrL*WML;
			System.out.println("ErrL=" + ErrL + ", ErrS=" + ErrS + ", ErrM=" + ErrM);

			float ErrWCS = l*ErrS*OC; WCS = WCS + ErrWCS;
			float ErrWCM = l*ErrM*OC; WCM = WCM + ErrWCM;
			float ErrWPS = l*ErrS*OP; WPS = WPS + ErrWPS;
			float ErrWPM = l*ErrM*OP; WPM = WPM + ErrWPM;
			float ErrWAS = l*ErrS*OA; WAS = WAS + ErrWAS;
			float ErrWAM = l*ErrM*OA; WAM = WAM + ErrWAM;
			float ErrWDS = l*ErrS*OD; WDS = WDS + ErrWDS;
			float ErrWDM = l*ErrM*OD; WDM = WDM + ErrWDM;
			float ErrWSL = l*ErrL*OS; WSL = WSL + ErrWSL;
			float ErrWML = l*ErrL*OM; WML = WML + ErrWML;
			System.out.println(
					"WCS=" + WCS + 
					", WCM=" + WCM +
					", WPS=" + WPS +
					", WPM=" + WPM +
					", WAS=" + WAS +
					", WAM=" + WAM +
					", WDS=" + WDS +
					", WDM=" + WDM +
					", WSL=" + WSL +
					", WML=" + WML
					);
			
			WML=0.5f;

			float ErrThetaS = l*ErrS; ThetaS = ThetaS + ErrThetaS;
			float ErrThetaM = l*ErrM; ThetaM = ThetaM + ErrThetaM;
			float ErrThetaL = l*ErrL; ThetaL = ThetaL + ErrThetaL;
			System.out.println("ThetaS=" + ThetaS + ", ThetaM=" + ThetaM + ", ThetaL=" + ThetaL);
			
			System.out.println("----------------");
		}
		
		System.out.println("****************Classification****************");
		OC = 0.4f;
		OP = 0.3f;
		OA = 0.1f;
		OD = 0.2f;
		System.out.println("OC=IC=" + OC + ", OP=IP=" + OP + ", OA=IA=" + OA + ", OD=ID=" + OD);
		
		IS = WCS*OC + WPS*OP + WAS*OA + WDS*OD + ThetaS;
		OS = (float) (1.0/(1.0 + Math.exp(-IS)));
		System.out.println("IS=" + IS + ", OS=" + OS);
		
		IM = WCM*OC + WPM*OP + WAM*OA + WDM*OD + ThetaM;
		OM = (float) (1.0/(1.0 + Math.exp(-IM)));
		System.out.println("IM=" + IM + ", OM=" + OM);

		IL = WSL*OS + WML*OM + ThetaL;
		OL = (float) (1.0/(1.0 + Math.exp(-IL)));
		System.out.println("IL=" + IL + ", OL=" + OL);
		
	}

}
