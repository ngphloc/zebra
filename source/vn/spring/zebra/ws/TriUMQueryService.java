/**
 * 
 */
package vn.spring.zebra.ws;

import java.net.URL;
import java.util.HashMap;

import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQueryArg;
import vn.spring.zebra.server.TriUMServer;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryService {
	public String query(String type, String userid, String password, String course, String concept) {
		try {
	    	HashMap<String, String> argMap = new HashMap<String, String>();
	    	argMap.put("type", type);
	    	argMap.put("userid", userid);
	    	if(password != null && password.length() > 0)
	    		argMap.put("password", password);
	    	argMap.put("course", course);
	    	argMap.put("concept", concept);
	    	
	    	TriUMQueryArg arg = TriUMQueryArg.createQueryArg(argMap);
	    	TriUMQuery query = TriUMServer.getInstance().
	    		getCommunicateService().getQueryDelegator();
	    	return query.query(arg).toString();
		}
		catch(Exception e) {e.printStackTrace();}
		return "";
	}
	
	public static void main(String[] args) {
		try {
			String endpoint = "http://localhost:8080/wow/services/TriUMQueryService";
			Service service=new Service();
			Call call = (Call)service.createCall();
			
			call.setTargetEndpointAddress(new URL(endpoint));
			call.setOperationName("query");
			call.addParameter("type", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("userid", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("course", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("concept", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			String ret = (String)call.invoke(new Object[] {"bayes", "guest", null, "javatutorial", "GettingStarted"});
			
			System.out.println("Got result : " + ret);			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
