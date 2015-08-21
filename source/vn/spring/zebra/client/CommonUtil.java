package vn.spring.zebra.client;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class CommonUtil {
	public static String concat(Collection<String> cNames, String split) {
		String cNames$ = ""; 
		for(String name : cNames) cNames$ += name + split;
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	public static String concat(String[] cNames, String split) {
		String cNames$ = ""; 
		for(String name : cNames) cNames$ += name + split;
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	public static String concat(Map<String, String> attrs, String split) {
		String result = "";
		Set<Entry<String, String>> entries = attrs.entrySet();
		int i = 0;
		for(Entry<String, String> entry : entries) {
			result += entry.getKey() + "=" + entry.getValue();
			if(i < entries.size() - 1) result += ",";
			i++;
		}
		return result;
	}
	
	public static void showAndSaveReportFile(JFrame frmParent, File defaultReportFile, String data) {
		JFileChooser fc = createReportFileChooser(defaultReportFile);
        int returnVal = fc.showSaveDialog(frmParent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
            try {
            	FileWriter writer = new FileWriter(file);
            	writer.write(data); writer.flush(); writer.close();
            	JOptionPane.showMessageDialog(frmParent, "Save sucessfully!");
            }
            catch(Exception ex) {ex.printStackTrace();}
        }
	}
	public static JFileChooser createReportFileChooser(File defaultReportFile) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String filename = f.getName();
				if(filename.endsWith(".html") || filename.endsWith(".htm") || filename.endsWith(".xhtml"))
					return true;
				return false;
			}

			@Override
			public String getDescription() {
				return "HTML file";
			}
		});
		if(defaultReportFile != null) fc.setSelectedFile(defaultReportFile);
		return fc;
	}
	public static void main(String[] args) {
	}
}
