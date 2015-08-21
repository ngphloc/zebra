/**
 * 
 */
package vn.spring.zebra.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class MailUtil {
	public final static String MAIL_LIST_SEP = ";";

	public static Address newMailAddress(String addr) throws AddressException {
		return new InternetAddress(addr);
	}
	public static Address newMailAddress(String addr, String personal) throws AddressException, UnsupportedEncodingException {
		return new InternetAddress(addr, personal);
	}
	public static boolean contains(ArrayList<Address> addrs, Address addr) {
		for(Address t_addr : addrs) {
			if(t_addr.equals(addr)) return true;
		}
		return false;
	}
	public static ArrayList<Address> getNetAddrList(ArrayList<String> userids) {
		ArrayList<Address> addrs = new ArrayList<Address>();
		if(userids == null) return addrs;
		for(String userid : userids) {
			Address addr = getNetAddr(userid);
			if(addr != null) addrs.add(addr);
		}
		return addrs;
	}
	public static Address getNetAddr(String userid) {
		try {
			UserMailInfo info = new UserMailInfo(userid);
			if(info == null || info.userid == null || info.mail == null) return null;
			if(info.username == null || info.username.equals(info.userid))
				return newMailAddress(info.mail);
			return newMailAddress(info.mail, info.username);
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}
	public static String netAddrToStr(Address addr) {
		return addr.toString();
	}
	public static Address strToNetAddr(String netAddrStr) {
		if(netAddrStr == null || netAddrStr.length() == 0) return null;
		Matcher matcher = Pattern.compile("<\\S*>").matcher(netAddrStr.trim());
		String netAddr = null;
		while(matcher.find()) {
			netAddr = matcher.group().trim();
			break;
		}
		if(netAddr == null) {
			try {
				return newMailAddress(netAddrStr.replaceAll("<", "").replaceAll(">", "").trim());
			}
			catch(Exception e) {e.printStackTrace();}
			return null;
		}
		netAddr = netAddr.replaceAll("<", "").replaceAll(">", "").trim();
		if(netAddr.length() == 0) return null;
		
		String personal = matcher.replaceAll("").trim();
		if(personal.length() == 0) personal = null;
			
		try {
			if(personal != null) return newMailAddress(netAddr, personal);
			else                 return newMailAddress(netAddr);
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static ArrayList<Address> strToNetAddrList(String str) {
		ArrayList<Address> addrList = new ArrayList<Address>();
		if(str == null) return addrList;
		String[] netAddrStrList = str.split(MAIL_LIST_SEP);
		if(netAddrStrList.length == 0) return addrList;
		
		for(String netAddrStr : netAddrStrList) {
			netAddrStr = netAddrStr.trim();
			if(netAddrStr.length() == 0) continue;
			Address addr = strToNetAddr(netAddrStr);
			if(addr != null) addrList.add(addr);
		}
		
		return addrList;
	}
	public static String netAddrListToStr(ArrayList<Address> addrList) {
		StringBuffer netAddrListStr = new StringBuffer();
		for(int i = 0; i < addrList.size(); i++) {
			Address addr = addrList.get(i);
			netAddrListStr.append(netAddrToStr(addr));
			if(i < addrList.size() - 1) netAddrListStr.append(MAIL_LIST_SEP + " ");
		}
		return netAddrListStr.toString();
	}
	
	public static void main(String[] args) {
		ArrayList<String> userids = new ArrayList<String>();
		userids.add("guest");
		userids.add("lhxung");
		System.out.println(
				netAddrListToStr(strToNetAddrList(netAddrListToStr(strToNetAddrList("Nguyen Phuoc Loc <nploc@yahoo.com> ; Ca Le <abc@gmail.com>; laga@uhd.com ")))));
	}
}
