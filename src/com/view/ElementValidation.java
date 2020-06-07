package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.model.Settings;

@SuppressWarnings("serial")
public class ElementValidation extends JFrame {

	private JPanel contentPane;
	private JTable addelementTable;
	DefaultComboBoxModel<String> byComboModel = new DefaultComboBoxModel<>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	JComboBox<String> ByCombo = new JComboBox<String>(byComboModel);
	private DefaultTableModel addelementTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"By","By Value"});

	/**
	 * Create the frame.
	 */
	public ElementValidation(Settings crawlSettings) {
		setTitle("Add Element validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		/*JLabel lblAddelementsTo = new JLabel("Add elements to validate in every page");
		lblAddelementsTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblAddelementsTo, BorderLayout.NORTH);*/
		
		JPanel elementPanel = new JPanel();
		contentPane.add(elementPanel, BorderLayout.CENTER);
		elementPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel addPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) addPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		elementPanel.add(addPanel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addelementTableModel.addRow(new Object[] {null,""});
				addelementTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(ByCombo));
			}
		});
		addPanel.add(btnNewButton);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		
		addelementTable = new JTable(addelementTableModel);
		JScrollPane tableScroll = new JScrollPane(addelementTable);
		elementPanel.add(tableScroll, BorderLayout.CENTER);
		
		JPanel addelementPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) addelementPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		contentPane.add(addelementPanel, BorderLayout.SOUTH);
		
		JButton Add = new JButton("Add Validation");
		Add.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				JSONArray iecapabilitiesArray = new JSONArray();
				for(int eachRow = 0; eachRow<addelementTableModel.getRowCount(); eachRow++) {
					if(!addelementTableModel.getValueAt(eachRow, 1).toString().trim().equals("")) {
						JSONObject eachCapabs = new JSONObject();
						eachCapabs.put("by", addelementTableModel.getValueAt(eachRow, 0));
						eachCapabs.put("selector", addelementTableModel.getValueAt(eachRow, 1).toString());
						iecapabilitiesArray.add(eachCapabs);
					}
				}
				((JSONObject)crawlSettings.getProperty("validation-settings")).put("element",iecapabilitiesArray);
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addelementPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addelementPanel.add(cancelBtn);
		updateEleValidations(crawlSettings);
	}
	
	public void updateEleValidations(Settings crawlSettings) {
		JSONArray eleArr = ((JSONArray)crawlSettings.getProperty("validation-settings>element"));
 	   for(Object eachText : eleArr) {
 		   String eachBytVal = (String)((JSONObject)eachText).get("by");
 		   String eachByValueVal = (String)((JSONObject)eachText).get("selector");
 		   if(!eachBytVal.trim().equals("")) {
 			  addelementTableModel.addRow(new String[] {eachBytVal, eachByValueVal});
 		   }
 	   }
	}

}
