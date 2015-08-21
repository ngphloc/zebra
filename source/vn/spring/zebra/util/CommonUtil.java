package vn.spring.zebra.util;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vn.spring.WOW.engine.SerializeHandler;
import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class CommonUtil {
	
	public static Document createDOMParser(InputStream input) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        try {
        	parser.setFeature("http://xml.org/sax/features/validation", false);
        	parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        	parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (Exception e) {e.printStackTrace();}
        
        parser.parse(new InputSource(input));
        return parser.getDocument();
	}
    public static String serializeXHTML(Element element) throws IOException {
        StringWriter writer = new StringWriter();
        OutputFormat of = new OutputFormat("html", "UTF-8", true);
        of.setOmitDocumentType(true);
        of.setLineWidth(100);
        of.setNonEscapingElements(new String[] {"style"});
        BaseMarkupSerializer ser = SerializeHandler.getSerializer(writer, of);
        ser.serialize(element);
        return writer.toString();
    }
    public static String serializeXML(Element element) throws IOException {
        StringWriter writer = new StringWriter();
        OutputFormat of = new OutputFormat("xml", "UTF-8", true);
        of.setOmitDocumentType(true);
        of.setLineWidth(100);
        of.setNonEscapingElements(new String[] {"style"});
        BaseMarkupSerializer ser = SerializeHandler.getSerializer(writer, of);
        ser.serialize(element);
        return writer.toString();
    }
    
    public static boolean contains(String str, String[] a_str) {
    	if(a_str.length == 0) return false;
    	for(int i = 0; i < a_str.length; i++) {
    		if(str.equals(a_str[i])) return true;
    	}
    	return false;
    }
    public static boolean containsNoCase(String str, String[] a_str) {
    	if(a_str.length == 0) return false;
    	for(int i = 0; i < a_str.length; i++) {
    		if(str.toLowerCase().equals(a_str[i].toLowerCase())) return true;
    	}
    	return false;
    }
    
	public static String concatNames(Collection<String> cNames, String split) {
		String cNames$ = ""; 
		for(String name : cNames) cNames$ += name + split;
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	public static String concatNames(String[] cNames, String split) {
		String cNames$ = ""; 
		for(String name : cNames) cNames$ += name + split;
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	public static String concatNames2(String[] cNames, String split) {
		String cNames$ = ""; 
		for(String name : cNames) cNames$ += name + split;
		cNames$ = cNames$.trim();
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	public static String concatNames(double[] cNames, String split) {
		String cNames$ = ""; 
		for(double name : cNames) cNames$ += "" + name + split;
		if(cNames$.length() > 0)
			cNames$ = cNames$.substring(0, cNames$.length() - 1);
		return cNames$;
	}
	

	public static String showInputCourseDlg(Component parentComponent, String title, String defaultCourse) {
    	String course = null;
    	ArrayList<String> courseList = ZebraStatic.getAuthorsConfig().getAllCourses();
    	if(courseList.size() == 0) return null;
    	Collections.sort(courseList);
    	if(defaultCourse == null || defaultCourse.length() == 0 || courseList.indexOf(defaultCourse) == -1)
    		defaultCourse = courseList.get(0);
    	String message = "Please enter the course so as to " + title;
    	int messageType = JOptionPane.OK_CANCEL_OPTION;
    	course = (String)JOptionPane.showInputDialog(parentComponent,
                message,
                title,
                messageType,
                null,
                courseList.toArray(),
                defaultCourse);
    	if(course == null || course.length() == 0) return null;
    	return course;
    }
	@SuppressWarnings("deprecation")
	public static Date StringToDate(String s_date) throws IllegalArgumentException {
		String d = "";
		if(s_date.indexOf("ICT") != -1) {
			String[] frags = s_date.split(" ");
			d += frags[1] + " " + frags[2] + ", " + frags[5] + " ";
			
			String[] time = frags[3].split(":");
			int t = Integer.parseInt(time[0]);
			if(t == 0) {
				d += "12:" + time[1] + ":" + time[2] + " " + "AM";
			}
			else if(t == 12) {
				d += "12:" + time[1] + ":" + time[2] + " " + "PM";
			}
			else if(t < 12)
				d += t + ":" + time[1] + ":" + time[2] + " " + "AM";
			else
				d += (t - 12) + ":" + time[1] + ":" + time[2] + " " + "PM";
		}
		else
			d = s_date;
		return new Date(d);
	}
	
	public static String DateToString(Date date) {
		return date.toGMTString();
	}
	
	public static String generateIncludeJavaScriptCode(String javascriptSourcePath) {
		String code = ""; 
		code = "<script language=\"JavaScript\">\n//<!--\n";
		BufferedReader reader = new BufferedReader( new InputStreamReader(
				CommonUtil.class.getResourceAsStream(javascriptSourcePath)));
		String line = null;
		try {
			while((line = reader.readLine()) != null) {
				code += line + "\n";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		code += "\n//-->\n";
		code += "</script>";
		return code;
	}
	public static JButton makeToolButton(URL iconURL, String cmd, String tooltip, String alt, 
			ActionListener listener) {
		JButton button = new JButton();
        button.setActionCommand(cmd);
        button.setToolTipText(tooltip);
        button.addActionListener(listener);

        if (iconURL != null) {
            button.setIcon(new ImageIcon(iconURL, alt));
        }
        else {                                     //no image found
            button.setText(alt);
        }

        return button;
	}
	public static JMenuItem makeMenuItem(URL iconURL, String text, ActionListener listener) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(listener);
        if (iconURL != null) {
            item.setIcon(new ImageIcon(iconURL, text));
        }

        return item;
	}
	
	public static void showAndSaveReportFile(JFrame frmParent, String curDir, File defaultReportFile, String data) {
		JFileChooser fc = createReportFileChooser(curDir, defaultReportFile);
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
	public static JFileChooser createReportFileChooser(String curDir, File defaultReportFile) {
		JFileChooser fc = new JFileChooser(curDir);
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
	public static void gc() {
		try {System.gc();} catch(Throwable e) {e.printStackTrace();}
	}
	public static void main(String[] args) {
		System.out.println(new Date().toString());
		System.out.println(new Date().toLocaleString());
		System.out.println(DateToString(StringToDate(DateToString(new Date()))));
		
	}
}
