/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAttributeAddNewDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	protected UserAttributeTableModel model = null;
	protected JFrame parent = null;
	
	JTextField attrName = new JTextField(8);
	JTextField attrValue = new JTextField(8);
	JRadioButton stringChoose = new JRadioButton("String");
	JRadioButton booleanChoose = new JRadioButton("Boolean");
	JRadioButton floatChoose = new JRadioButton("Float");
	
	public UserAttributeAddNewDlg(JFrame parent, UserAttributeTableModel model) {
		super(parent, "Enter Attribute", true);
		this.model = model;
		this.parent = parent;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JPanel main = new JPanel(); main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		add(main);
		
		JPanel tempPanel = null;
		
		tempPanel = new JPanel(); tempPanel.setAlignmentX(LEFT_ALIGNMENT);
		tempPanel.add(new JLabel("Attribute Name:"));
		tempPanel.add(attrName);
		main.add(tempPanel);
		
		tempPanel = new JPanel(); tempPanel.setAlignmentX(LEFT_ALIGNMENT);
		tempPanel.add(new JLabel("Attribute Value:"));
		tempPanel.add(attrValue);
		main.add(tempPanel);
		
		tempPanel = new JPanel(); tempPanel.setAlignmentX(LEFT_ALIGNMENT);
		ButtonGroup group = new ButtonGroup();
		group.add(stringChoose);
		group.add(booleanChoose);
		group.add(floatChoose);
		//
		JPanel choose = new JPanel();
		choose.add(stringChoose);
		choose.add(booleanChoose);
		choose.add(floatChoose);
		tempPanel.add(new JLabel("Type:"));
		tempPanel.add(choose);
		main.add(tempPanel);
		
		tempPanel = new JPanel(); tempPanel.setAlignmentX(CENTER_ALIGNMENT);
		JButton ok = new JButton(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { ok();}
		});
		
		JButton cancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { dispose();}
		});
		tempPanel.add(ok);		
		tempPanel.add(cancel);		
		main.add(tempPanel);
		
		pack();
	}

	public void ok() {
		String name = attrName.getText().trim();
		String value = attrValue.getText().trim();
		String type = null;
		if(stringChoose.isSelected())      type = "string";
		else if(booleanChoose.isSelected()) type = "boolean";
		else if(floatChoose.isSelected())  type = "float";
		
		if(name.length() == 0 || value.length() == 0 || type == null) {
			JOptionPane.showMessageDialog(this, "Some field(s) are empty", "Error", 
					JOptionPane.ERROR_MESSAGE);
			doAgain(); return;
		}
		
		if(model.checkExist(name) || model.disableKeys.contains(name) || model.hideKeys.contains(name)) {
			JOptionPane.showMessageDialog(this, "Attribute name exists", "Error", 
					JOptionPane.ERROR_MESSAGE);
			doAgain(); return;
		}
		
		try {
			Object trustValue = null;
			if(type.equals("string")) {
				trustValue = value;
			}
			else if(type.equals("boolean")) {
				trustValue = Boolean.parseBoolean(value);
			}
			else if(type.equals("float")) {
				trustValue = Float.parseFloat(value);
			}
			else {
				JOptionPane.showMessageDialog(this, "Not support this type", "Error", 
						JOptionPane.ERROR_MESSAGE);
				doAgain(); return;
			}
			
			model.addRow(name, trustValue, true);
			dispose(); return;
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, "Attribute value is invalid format", "Error", 
					JOptionPane.ERROR_MESSAGE);
			doAgain(); return;
		}
	}
	private void doAgain() {
		dispose();
		UserAttributeAddNewDlg addNewDlg = new UserAttributeAddNewDlg(parent, model);
		addNewDlg.attrName.setText(attrName.getText());
		addNewDlg.attrValue.setText(attrValue.getText());

		if(stringChoose.isSelected())       addNewDlg.stringChoose.setSelected(true);
		else if(booleanChoose.isSelected()) addNewDlg.booleanChoose.setSelected(true);
		else if(floatChoose.isSelected())   addNewDlg.floatChoose.setSelected(true);
		addNewDlg.setVisible(true);
	}
}
