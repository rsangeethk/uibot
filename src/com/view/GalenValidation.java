package com.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.model.Settings;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Dimension;

import javax.swing.JTable;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class GalenValidation extends JFrame {

	private JPanel contentPane;
	private JTable addgalenTable;
	private DefaultTableModel addgalenTableModel = new DefaultTableModel(new String[] {"Name", "Galen Spec File Path"}, 0);

	/**
	 * Create the frame.
	 */
	public GalenValidation(Settings crawlSettings) {
		setTitle("Add Galen validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		/*JLabel lblAddgalensTo = new JLabel("Add galens to validate in every page");
		lblAddgalensTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblAddgalensTo, BorderLayout.NORTH);*/
		
		JPanel galenPanel = new JPanel();
		contentPane.add(galenPanel, BorderLayout.CENTER);
		galenPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel addPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) addPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		galenPanel.add(addPanel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addgalenTableModel.addRow(new String[] {"",""});
			}
		});
		addPanel.add(btnNewButton);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		
		addgalenTable = new JTable(addgalenTableModel);
		JScrollPane tableScroll = new JScrollPane(addgalenTable);
		galenPanel.add(tableScroll, BorderLayout.CENTER);
		
		JPanel addgalenPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) addgalenPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		contentPane.add(addgalenPanel, BorderLayout.SOUTH);
		
		JButton Add = new JButton("Add Validation");
		Add.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				JSONArray iecapabilitiesArray = new JSONArray();
				for(int eachRow = 0; eachRow<addgalenTableModel.getRowCount(); eachRow++) {
					if(!addgalenTableModel.getValueAt(eachRow, 0).toString().trim().equals("")) {
						JSONObject eachCapabs = new JSONObject();
						eachCapabs.put("name",  addgalenTableModel.getValueAt(eachRow, 0));
						eachCapabs.put("file", addgalenTableModel.getValueAt(eachRow, 1));
						iecapabilitiesArray.add(eachCapabs);
					}
				}
				((JSONObject)crawlSettings.getProperty("validation-settings")).put("galen-specs",iecapabilitiesArray);
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addgalenPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addgalenPanel.add(cancelBtn);
		updateGalenValidations(crawlSettings);
	}
	public void updateGalenValidations(Settings crawlSettings) {
		JSONArray galenArr = ((JSONArray)crawlSettings.getProperty("validation-settings>galen-specs"));
 	   	for(Object eachText : galenArr) {
 	   		String eachGalenVal = (String)((JSONObject)eachText).get("name");
 	   		String eachGalenFile = (String)((JSONObject)eachText).get("file");
 	   		if(!eachGalenVal.trim().equals("")) {
 	   			addgalenTableModel.addRow(new String[] {eachGalenVal, eachGalenFile});
 	   		}
 	   	}
 	   
	}
}
