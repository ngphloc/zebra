/**
 * 
 */
package vn.spring.zebra.feedback;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class QA {
	private final static String ID_PREFIX = "qa_";
	private final static String DELIMETER = ";";
	
	protected int     ID = -1;
	protected String  title = null;
	protected boolean multichoice = false;
	protected String  options[] = null;
	protected double  optionsScore[] = null;
	protected int[]   answerIdxes = null;
	
	public QA(int ID, String title, boolean multichoice, String[] options) throws ZebraException {
		this(ID, title, multichoice, options, null);
	}
	public QA(int ID, String title, boolean multichoice, String[] options, double[] optionsScore) throws ZebraException {
		this.ID = ID;
		this.title = title;
		this.multichoice = multichoice;
		this.options = options;
		if(optionsScore == null || optionsScore.length == 0) {
			this.optionsScore = new double[this.options.length];
			for(int i = 0; i < this.optionsScore.length; i++) this.optionsScore[i] = i; 
		}
		this.answerIdxes = new int[options.length];
		resetAnswer();
		
		if(!isValid()) throw new ZebraException("Invalid parameters");
	}
	protected QA(Element qaElement) throws ZebraException {
		loadXML(qaElement);
	}
	protected QA(String cvsLine) throws ZebraException {
		loadCVS(cvsLine);
	}

	public boolean checkValidAnswerIdx(int answerIdx) {
		if(answerIdx < -1 || answerIdx >= options.length) return false;
		return true;
	}
	public void setAnswer(int[] answerIdxes) throws ZebraException {
		this.answerIdxes = Arrays.copyOf(answerIdxes, answerIdxes.length);
		if(!isValid()) throw new ZebraException("Invalid parameter(s)");
	}
	public void setAnswer(ArrayList<Integer> answerIdxes) throws ZebraException {
		this.answerIdxes = new int[answerIdxes.size()];
		for(int i = 0; i < answerIdxes.size(); i++) this.answerIdxes[i] = answerIdxes.get(i);
		if(!isValid()) throw new ZebraException("Invalid parameter(s)");
	}

	public void  resetAnswer()   {
		for(int i = 0; i < answerIdxes.length; i++) answerIdxes[i] = 0;
	}
	public boolean isAnswered()    {
		for(int i = 0; i < answerIdxes.length; i++) {
			if(answerIdxes[i] != 0) return true;
		}
		return false;
	}
	public boolean isOptionChosen(int optionIdx) {
		return (answerIdxes[optionIdx] != 0);
	}
	public boolean isOptionChosen(int[] answerIdxes) {
		for(int i = 0; i < answerIdxes.length; i++) {
			if(answerIdxes[i] != this.answerIdxes[i]) return false;
		}
		return true;
	}
	public int indexOfOption(String option) {
		for(int i = 0; i < options.length; i++) {
			if(options[i].equals(option)) return i;
		}
		return -1;
	}
	public String  getOption(int idx) {return options[idx];}
	public int     getOptionLength()  {return options.length;}
	public boolean isMultiChoice() {return multichoice;}
	
	@Override
	public boolean equals(Object obj) {
		return this.ID == ((QA)obj).ID;
	}
	
	@Override
	public String toString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<qa id=\"" + ID + "\" title=\"" + title + "\" multichoice=\"" + multichoice + "\" >" + "\n");
		xml.append("  <options>" + "\n");
		for(int i = 0; i < options.length; i++)
			xml.append("    <option name=\"" + options[i] + "\" score=\"" + optionsScore[i] + "\" />" + "\n");
		xml.append("  </options>" + "\n");

		xml.append("  <answers>" + "\n");
		for(int i = 0; i < answerIdxes.length; i++)
			xml.append("    <answer>" + answerIdxes[i] + "</answer>" + "\n");
		xml.append("  </answers>" + "\n");
		
		xml.append("</qa>");
		
		return xml.toString();
	}
	protected Element toElement(Document doc) {
		Element qaEl = doc.createElement("qa");
		qaEl.setAttribute("id", "" + ID);
		qaEl.setAttribute("title", title);
		qaEl.setAttribute("multichoice", String.valueOf(multichoice));
		
		Element optionsEl = doc.createElement("options");
		qaEl.appendChild(optionsEl);
		for(int i = 0; i < options.length; i++) {
			Element optionEl = doc.createElement("option");
			optionEl.setAttribute("name", options[i]);
			optionEl.setAttribute("score", String.valueOf(optionsScore[i]));
			optionsEl.appendChild(optionEl);
		}
		
		Element answersEl = doc.createElement("answers");
		qaEl.appendChild(answersEl);
		for(int i = 0; i < answerIdxes.length; i++) {
			Element answerEl = doc.createElement("answer");
			answerEl.appendChild(doc.createTextNode(String.valueOf(answerIdxes[i])));
			answersEl.appendChild(answerEl);
		}
		return qaEl;
	}
	protected String genHTML(boolean enable) {
		String disabled = (enable ? "" : "disabled=\"true\"");
		
		String html = "";
		html += "<tr>" + "\n";
		html += "  <td>" + ID + "</td>" + "\n";
		html += "  <td>" + title + "</td>" + "\n";
		html += "  <td>" + "\n";
		html += "    <table border=\"1\">" + "\n";
		
		html += "      <tr>" + "\n";
		for(int i = 0; i < options.length; i++) {
		html += "        <td>" + options[i] + "</td>" + "\n";
		}
		html += "      </tr>" + "\n";

		html += "      <tr>" + "\n";
		for(int i = 0; i < options.length; i++) {
			String value = "" + i;
			String checked = (isOptionChosen(i)?"checked=\"true\"":"");
			if(multichoice)
				html += "        <td>" + "<input type=\"checkbox\" name=\"" + makeElementName(i) + 
				"\" value=\"" + value + "\" " + checked + " " + disabled + " ></td>" + "\n";
			else
				html += "        <td>" + "<input type=\"radio\" name=\"" + makeElementName(i) + 
					"\" value=\"" + value + "\" " + checked + " " + disabled + " ></td>" + "\n";
		}
		html += "      </tr>" + "\n";

		html += "    </table>" + "\n";
		html += "  </td>" + "\n";
		html += "</tr>";
		return html;
	}
	protected String genReportHTML() {
		String html = "";
		String idhref = "#" + "qa_" + ID + "_top";
		html += "<tr><td>ID</td><td><a href=\"" + idhref + "\">" + ID + "</a></td></tr>" + "\n";
		html += "<tr><td>Title</td><td>" + title + "</td></tr>" + "\n";
		html += "<tr><td>Multichoice</td><td>" + (isMultiChoice()?"True":"False") + "</td></tr>" + "\n";
		html += "<tr><td>Options</td>" + "\n";
		html += "  <td>" + "\n";
		html += "    <table border=\"1\">" + "\n";
		html += "      <tr>" + "\n";
		for(int i = 0; i < options.length; i++) {
		String optionhref = "#" + "qa_" + ID + "_" + i;
		html += "        <td><a href=\"" + optionhref + "\">" + options[i] + "</a></td>" + "\n";
		}
		html += "      </tr>" + "\n";
		html += "    </table>" + "\n";
		html += "  </td>" + "\n";
		html += "</tr>";
		return html;
	}
	public JPanel genReportGUI() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		JPanel row = null;
		JTextField txtField = null;
		
		row = new JPanel(); row.setLayout(new BorderLayout());
		txtField = new JTextField("" + ID); txtField.setEditable(false);
		row.add(new JLabel("ID:"), BorderLayout.WEST); row.add(txtField, BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);
		
		row = new JPanel(); row.setLayout(new BorderLayout());
		txtField = new JTextField(title); txtField.setEditable(false);
		row.add(new JLabel("Question:"), BorderLayout.WEST); row.add(txtField, BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);

		row = new JPanel(); row.setLayout(new BorderLayout());
		txtField = new JTextField(multichoice?"Yes":"No"); txtField.setEditable(false);
		row.add(new JLabel("Multichoice:"), BorderLayout.WEST); row.add(txtField, BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);
		
		row = new JPanel(); row.setLayout(new BorderLayout());
		txtField = new JTextField(CommonUtil.concatNames2(options, ", ")); txtField.setEditable(false);
		row.add(new JLabel("Possible answers:"), BorderLayout.WEST); row.add(txtField, BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);
		
		return main;
	}

	protected class OptionPane extends JPanel {
		private static final long serialVersionUID = 1L;
		public QA qa = null;
		public OptionPane(QA qa) {
			this.qa = qa;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			if(multichoice) {
				for(int i = 0; i < options.length; i++) {
					JCheckBox op = new JCheckBox(options[i]);
					add(op);
				}
			}
			else {
				ButtonGroup group = new ButtonGroup();
				for(int i = 0; i < options.length; i++) {
					JRadioButton op = new JRadioButton(options[i]);
					group.add(op);
					add(op);
				}
			}
		}
		public int[] getChosenIdxes() {
			int[] chosen = new int[getComponentCount()];
			for(int i = 0; i < getComponentCount(); i++) {
				AbstractButton op = (AbstractButton)getComponent(i);
				chosen[i] = op.isSelected() ? 1 : 0; 
			}
			return chosen;
		}
		public boolean isSelected() {
			int[] idxes = getChosenIdxes();
			for(int i = 0; i < idxes.length; i++) {
				if(idxes[i] != 0) return true;
			}
			return false;
		}
	}
	public String makeElementName(int optionIdx) {
		if(multichoice)
			return "qa_" + ID + "_" + optionIdx;
		else
			return "qa_" + ID;
	}
	public static int getQAIDFromElementName(String name) {
		if(name.indexOf(ID_PREFIX) != 0) return -1;
		try {
			return Integer.parseInt(name.substring(ID_PREFIX.length()).split("_")[0]);
		}
		catch(Exception e) {return -1;}
	}
	public static int getOptionIdxFromElementName(String name) {
		if(name.indexOf(ID_PREFIX) != 0) return -1;
		try {
			return Integer.parseInt(name.substring(ID_PREFIX.length()).split("_")[1]);
		}
		catch(Exception e) {return -1;}
	}
	protected void copy(QA qa) {
		title = qa.title;
		multichoice = qa.multichoice;
		options = Arrays.copyOf(qa.options, qa.options.length);
		optionsScore = Arrays.copyOf(qa.optionsScore, qa.optionsScore.length);
		
		int[] newAnswerIdxes = Arrays.copyOf(qa.answerIdxes, qa.answerIdxes.length);
		for(int i = 0; i < Math.min(newAnswerIdxes.length, this.answerIdxes.length); i++) {
			newAnswerIdxes[i] = this.answerIdxes[i];
		}
		this.answerIdxes = newAnswerIdxes;
	}
	
	private void loadXML(Element qaElement) throws ZebraException {
		reset();
    	try {
    		ID = Integer.parseInt(qaElement.getAttribute("id"));
    		title = qaElement.getAttribute("title");
    		multichoice = Boolean.parseBoolean(qaElement.getAttribute("multichoice"));
    		
    		Node node = qaElement.getFirstChild();
    		while(node != null) {
    			String nodename = node.getNodeName();
    			if(nodename.equals("options")) {
    	    		NodeList optionList = ((Element)node).getChildNodes();
    	    		ArrayList<String> temp_options = new ArrayList<String>();
    	    		ArrayList<Double> temp_optionsScore = new ArrayList<Double>();
    	    		for(int i = 0; i < optionList.getLength(); i++) {
    	    			String nodename2 = optionList.item(i).getNodeName();
    	    			if(!nodename2.equals("option")) continue;
    	    			Element option = (Element)optionList.item(i);
    	    			temp_options.add(option.getAttribute("name"));
    	    			temp_optionsScore.add(new Double(option.getAttribute("score")));
    	    		}
    	    		if(temp_options.size() == 0) throw new ZebraException("Error xml");
    	    		options = new String[temp_options.size()];
    	    		optionsScore = new double[temp_options.size()];
    	    		for(int i = 0; i < options.length; i++) {
    	    			options[i] = temp_options.get(i);
    	    			optionsScore[i] = temp_optionsScore.get(i).doubleValue();
    	    		}
    			}
    			else if(nodename.equals("answers")) {
    	    		ArrayList<Integer> tempAnswerIdxes = new ArrayList<Integer>();
    	    		NodeList answerNodeList = ((Element)node).getChildNodes();
    	    		for(int i = 0; i < answerNodeList.getLength(); i++) {
    	    			String nodename2 = answerNodeList.item(i).getNodeName();
    	    			if(!nodename2.equals("answer")) continue;
    	    			Element answer = (Element)answerNodeList.item(i);
    	    			
        				int answerIdx = -1;
    					String value = (answer.getFirstChild() == null ? null : answer.getFirstChild().getNodeValue());
        				if(value == null || value.trim().length() == 0) answerIdx = -1;
        				else {
        					try {
        						answerIdx = Integer.parseInt(value.trim());
        					}
        					catch(Exception e) {e.printStackTrace(); answerIdx = -1;}
        				}
        				tempAnswerIdxes.add(answerIdx);
    	    		}
    	    		this.setAnswer(tempAnswerIdxes);
    			}
    			node = node.getNextSibling();
    		}
    	}
    	catch(Exception e) {e.printStackTrace(); throw new ZebraException(e.getMessage());}
    	if(!isValid()) throw new ZebraException("Error xml");
	}
	private void loadCVS(String cvsLine) throws ZebraException {
		String[] fields = cvsLine.split(DELIMETER);
		if(fields.length < 5) throw new ZebraException("Invalid parameter");
		reset();
		
		this.ID = Integer.parseInt(fields[0].trim());
		this.title = fields[1].trim();
		fields[2] = fields[2].trim().toLowerCase();
		if(fields[2].equals("multichoice"))
			this.multichoice = true;
		else if(fields[1].equals("singlechoice"))
			this.multichoice = false;
		else
			this.multichoice = Boolean.parseBoolean(fields[1]);
		
		this.options = new String[fields.length - 3];
		double[] optionsScore = new double[fields.length - 3];
		boolean isDefaultScore = false;
		for(int i = 3; i < fields.length; i++) {
			String[] ss = fields[i].split(":");
			if(ss.length != 1 && ss.length != 2) throw new ZebraException("Invalid parameter");
			if(ss.length == 1) {
				isDefaultScore = true;
				this.options[i-3] = ss[0].trim();
			}
			else {
				this.options[i-3] = ss[0].trim();
				optionsScore[i-3] = Double.parseDouble(ss[1].trim());
			}
		}
		if(isDefaultScore) {
			this.optionsScore = new double[this.options.length];
			for(int i = 0; i < this.optionsScore.length; i++) this.optionsScore[i] = i; 
		}
		else
			this.optionsScore = optionsScore;

		this.answerIdxes = new int[options.length];
		resetAnswer();
		
		if(!isValid()) throw new ZebraException("Error cvs");
	}

	private void reset() {
		ID = -1;
		title = null;
		options = null;
		optionsScore = null;
		answerIdxes = null;
	}
	private boolean isValid() {
		if(ID < 0 || 
		   title == null || title.length() == 0 || 
		   options == null || options.length == 0 ||
		   optionsScore == null || optionsScore.length == 0 || optionsScore.length != options.length ||
		   answerIdxes == null || answerIdxes.length == 0 || answerIdxes.length != options.length)
			return false;
		
		int count = 0;
		for(int i = 0; i < answerIdxes.length; i++) {
			if(answerIdxes[i] != 0) count++;
		}
		if(!multichoice && count > 1) return false;
		
		return true;
	}
}
