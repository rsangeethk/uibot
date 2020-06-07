package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
public class TextAvailabilityWindow extends JFrame {

	private JPanel contentPane;
	private JTable addTextTable;
	private DefaultTableModel addTextTableModel = new DefaultTableModel(new String[] {"Texts"}, 0);

	/**
	 * Create the frame.
	 */
	public TextAvailabilityWindow(Settings crawlSettings) {
		setTitle("Global Text validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		/*JLabel lblAddTextsTo = new JLabel("Add texts to validate in every page");
		lblAddTextsTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblAddTextsTo, BorderLayout.NORTH);*/
		
		JPanel textPanel = new JPanel();
		contentPane.add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel addPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) addPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		textPanel.add(addPanel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTextTableModel.addRow(new String[] {""});
			}
		});
		btnNewButton.setHorizontalTextPosition(SwingConstants.LEADING);
		addPanel.add(btnNewButton);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		
		addTextTable = new JTable(addTextTableModel);
		
		JScrollPane tableScroll = new JScrollPane(addTextTable);
		textPanel.add(tableScroll, BorderLayout.CENTER);
		
		JPanel addTextPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) addTextPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		contentPane.add(addTextPanel, BorderLayout.SOUTH);
		
		JButton Add = new JButton("Add Validation");
		Add.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				JSONArray iecapabilitiesArray = new JSONArray();
				for(int eachRow = 0; eachRow<addTextTableModel.getRowCount(); eachRow++) {
					if(!addTextTableModel.getValueAt(eachRow, 0).toString().trim().equals("")) {
						iecapabilitiesArray.add(addTextTableModel.getValueAt(eachRow, 0));
					}
				}
				((JSONObject)crawlSettings.getProperty("validation-settings")).put("text",iecapabilitiesArray);
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addTextPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addTextPanel.add(cancelBtn);
		updateTextValidations(crawlSettings);
	}

	public void updateTextValidations(Settings crawlSettings) {
		JSONArray textArr = ((JSONArray)crawlSettings.getProperty("validation-settings>text"));
 	   for(Object eachText : textArr) {
 		   String eachtextVal = (String)eachText;
 		   if(!eachtextVal.trim().equals("")) {
 			  addTextTableModel.addRow(new String[] {eachtextVal});
 		   }
 	   }
	}
}
