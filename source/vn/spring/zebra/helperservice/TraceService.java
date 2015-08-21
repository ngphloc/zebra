package vn.spring.zebra.helperservice;

import java.io.*;
import java.util.Date;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class TraceService {
	private final static int MAX_FILE_LENGTH = 1; //1mb
	
	protected PrintWriter fileOut = null;
	protected String      filePath = null;
	
	public TraceService() {
		if(ZebraStatic.TRACE_SERVICE_USE_FILE) {
			try {
	            File dir = new File(ZebraStatic.getWowWebRoot() + ZebraStatic.TRACE_SERVICE_DIR);
	            if(!dir.exists()) dir.mkdir();
	            prepareFileOutput();
			}
			catch(Exception e) {
				System.out.println("TraceService constructor causes error: " + e.getMessage());
				fileOut = null;
			}
		}
		else {
			fileOut = null;
		}
	}
	public void close() {
		try {
			if(fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
		}
		catch(Exception e) {
			System.out.println("TraceService close causes error: " + e.getMessage());
			fileOut = null;
		}
	}
	public void trace(String msg) {
		try {
			msg = new Date() + ": " + msg;
			System.out.println(msg);
			if(fileOut != null) {
				prepareFileOutput();
				fileOut.println(msg);
			}
		}
		catch(Exception e) {
			System.out.println("TraceService trace causes error: " + e.getMessage());
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			// TODO Auto-generated method stub
			close();
			super.finalize();
		}
		catch(Exception e) {
			System.out.println("TraceService finalize causes error: " + e.getMessage());
		}
	}

	private void prepareFileOutput() {
		try {
			Date    date = new Date();
			String  suffix = (date.getYear()+1900) + "." + (date.getMonth()+1) + "." + date.getDate();
			String  filePath = ZebraStatic.getWowWebRoot() + ZebraStatic.TRACE_SERVICE_DIR + "zebra-" + suffix + ".trace";
			
			if(this.fileOut == null) {
				boolean append = false;
				File    file = new File(filePath);
				if(file.exists()) {
					if(file.length() > MAX_FILE_LENGTH*1024*1024)
						append = false;
					else
						append = true;
				}
				else
					append = false;
				
				this.filePath = filePath;
				this.fileOut = new PrintWriter(new FileWriter(this.filePath, append), true);
			}
			else {
				File file = new File(this.filePath);
				if(file.length() > MAX_FILE_LENGTH*1024*1024) {
					this.fileOut.flush();
					this.fileOut.close();
					
					this.filePath = filePath;
					this.fileOut = new PrintWriter(new FileWriter(this.filePath, false), true);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(); 
			fileOut = null;
		}
	}

	public static void main(String[] args) {
	}

}
