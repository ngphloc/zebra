/**
 * 
 */
package vn.spring.zebra.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

import javax.swing.*;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryClient {
	public static String onQuery(HashMap<String, String> argMap) throws Exception {
		String protocol = argMap.get("protocol");
		protocol = protocol.toLowerCase();
		if(protocol == null || protocol.length() == 0) protocol = "rmi";
		
		String host = argMap.get("host");
		if(host == null || host.length() == 0) host = "localhost";
		
		String port = argMap.get("port");
		if(port == null) {
			if(protocol.equals("rmi"))
				port = String.valueOf(TriUMQuery.RMI_QUERY_PORT);
			else if(protocol.equals("http"))
				port = String.valueOf(TriUMQuery.HTTP_SERVICE_PORT);
			else if(protocol.equals("socket"))
				port = String.valueOf(TriUMQuery.COMMUNICATE_SOCKET_SERVICE_PORT);
		}

		String type = argMap.get("type");
		if(type == null || type.length() == 0) type = "overlay";
		
		argMap.remove("protocol");
		argMap.remove("host");
		argMap.remove("port");
		
		if(protocol.equals("rmi")) {
			TriUMQuery query = TriUMQueryClientUtil.getTriUMQuery(host, Integer.parseInt(port));
			return query.query(argMap).toString();
		}
		else if(protocol.equals("soap")) {
			String endpoint = "http://" + host + ":" + port + "/wow/services/TriUMQueryService?"; 
			Service service = new Service();
			Call call = (Call)service.createCall();
			
			call.setTargetEndpointAddress(new URL(endpoint));
			call.setOperationName("query");
			call.addParameter("type", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("userid", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("course", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("concept", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			String value = (String)call.invoke(new Object[] {
							type, 
							argMap.get("userid"), 
							argMap.get("password"), 
							argMap.get("course"), 
							argMap.get("concept")});
			return value;
		}
		else if(protocol.equals("http")) {
			String result = "";
			
			String urlText = "http://" + host + ":" + port + "/wow/TriUMQueryService?" +
				TriUMQueryArg.createHttpQueryString(argMap);
			URL url = new URL(urlText);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String data = null;
			while((data = in.readLine()) != null) {
				result += data;
			}
			in.close();
			return result;
		}
		else if(protocol.equals("socket")) {
			Socket socket = new Socket(host, TriUMQuery.COMMUNICATE_SOCKET_SERVICE_PORT);
			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
			String queryString = TriUMQueryArg.createHttpQueryString(argMap);
			dout.writeUTF(queryString);
			String result = din.readUTF();
			din.close(); dout.close();
			return result;
		}
		else
			throw new Exception("Not implement yet for other protocols");
	}
	public static void showConsoleResult(QUERY_TYPE type, String result) {
		if(result == null || result.length() == 0) {
			JOptionPane.showMessageDialog(null, "There is no information");
			return;
		}
		if(type == QUERY_TYPE.OVERLAY || type == QUERY_TYPE.OVERLAY_BAYESIAN ||
				type == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.LEARNING_STYLE) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.RECOMMENDED_POST_CONCEPT) {
			System.out.println(result.toString());
		}
		else if(type == QUERY_TYPE.LEARNING_PATH) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.COMMUNITY) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.PERSONAL_INFO) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.CONCEPT_INFO) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.STUDY_TIME) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.LIST_COURSES) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.LIST_CONCEPTS) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.DOCCLASS) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.USERREPORT) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.FEEDBACKREPORT) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.TOTALREPORT) {
			System.out.println(result);
		}
		else if(type == QUERY_TYPE.MAILINGLIST_REG || type == QUERY_TYPE.MAILINGLIST_UNREG) {
			if(result.toLowerCase().equals("true"))
				System.out.println("Success");
			else
				System.out.println("Fail");
		}
		else
			System.out.println(result);
			
	}
	
	public static void main(String[] args) {
		HashMap<String, String> argMap = TriUMQueryArg.parseQueryArg(args);
		if(argMap.size() == 0) {
			TriUMQueryFrame frame = new TriUMQueryFrame(null);
			frame.setVisible(true);
		}
		else {
			try {
				TriUMQueryArg arg = TriUMQueryArg.createQueryArg(argMap); 
				showConsoleResult(arg.type, onQuery(argMap));
			}
			catch(Exception e) {
				String err = "TriUMQueryClient.main (console) causes error: " + e.getMessage();
				System.out.println(err);
				e.printStackTrace();
			}
		}
			
	}
}

