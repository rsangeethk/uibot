package com.view;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;

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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConditionalGalenValidation extends JFrame {

	private JPanel contentPane;
	private JTable addgalenTable;
	private DefaultTableModel addgalenTableModel = new DefaultTableModel(new String[] {"Name", "galen Spec File Path"}, 0);
	private DefaultTableModel addTextTableModel = new DefaultTableModel(new String[] {"Texts"}, 0);
	private DefaultComboBoxModel<String> condTypeModel = new DefaultComboBoxModel<String>(new String[] {"url", "title", "text", "element"});
	private DefaultComboBoxModel<String> compModel1 = new DefaultComboBoxModel<String>(new String[] {"is", "contains"});
	private DefaultComboBoxModel<String> compModel2 = new DefaultComboBoxModel<String>(new String[] {"exist in page"});
	private DefaultComboBoxModel<String> selModel = new DefaultComboBoxModel<String>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	private JTextField condvalueTextField;
	JComboBox<String> compCombo = new JComboBox<String>();
	JComboBox<String> selectorCondCombo = new JComboBox<String>(selModel);
	JPanel condValuePanel = new JPanel();
	private JComboBox<String> condTypeCombo;
	Settings crawlSettings;
	JSONObject condObj;
	private boolean newItem;

	/**
	 * Create the frame.
	 */
	public ConditionalGalenValidation(Settings crawlSettings, JSONObject condObj) {
		this.crawlSettings = crawlSettings;
		this.condObj = condObj;
		initComp();
		if(!condObj.containsKey("type")) {
			newItem = true;
		}
	}
	public void initComp() {
		setTitle("Conditional Galen validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
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
				String condtype = condTypeCombo.getSelectedItem().toString();
				condObj.put("type", condtype);
				condObj.put("compare", compCombo.getSelectedItem().toString());
				condObj.put("value", condvalueTextField.getText());
				JSONArray galenArr = new JSONArray();
				for(int eachRow = 0; eachRow<addgalenTableModel.getRowCount(); eachRow++) {
					if(!addgalenTableModel.getValueAt(eachRow, 0).toString().trim().equals("")) {
						JSONObject eachCapabs = new JSONObject();
						eachCapabs.put("name",  addgalenTableModel.getValueAt(eachRow, 0));
						eachCapabs.put("file", addgalenTableModel.getValueAt(eachRow, 1));
						galenArr.add(eachCapabs);
					}
				}
				condObj.put("galen-specs", galenArr);
				if(newItem) {
					JSONArray condVal = (JSONArray)crawlSettings.getProperty("validation-settings>conditional");
					condVal.add(condObj);
				}
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addgalenPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addgalenPanel.add(cancelBtn);
		
		JPanel conditionPanel = new JPanel();
		contentPane.add(conditionPanel, BorderLayout.NORTH);
		conditionPanel.setLayout(new BorderLayout(10, 0));
		
		/*JLabel lblAddTextsTo = new JLabel("Add texts to validate in every page");
		conditionPanel.add(lblAddTextsTo, BorderLayout.NORTH);
		lblAddTextsTo.setFont(new Font("Tahoma", Font.PLAIN, 12));*/
		
		JPanel condPanel = new JPanel();
		conditionPanel.add(condPanel, BorderLayout.CENTER);
		condPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel condLblPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) condLblPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		condLblPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		condPanel.add(condLblPanel, BorderLayout.NORTH);
		
		JLabel lblCondition = new JLabel("Condition");
		lblCondition.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		condLblPanel.add(lblCondition);
		lblCondition.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JPanel condItemsPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) condItemsPanel.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		condPanel.add(condItemsPanel);
		
		condTypeCombo = new JComboBox<String>(condTypeModel);
		condItemsPanel.add(condTypeCombo);
		condTypeCombo.setMaximumSize(new Dimension(250, 20));
		condTypeCombo.setPreferredSize(new Dimension(150, 20));
		condTypeCombo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//JComboBox<String> item = (JComboBox<String>)e.getItem();
					String changedValue = e.getItem().toString();
					switch(changedValue) {
						case "url":
							compCombo.setModel(compModel1);
							condValuePanel.remove(selectorCondCombo);
							break;
						case "title":
							compCombo.setModel(compModel1);
							condValuePanel.remove(selectorCondCombo);
							break;
						case "text":
							compCombo.setModel(compModel2);
							condValuePanel.remove(selectorCondCombo);
							break;
						case "element":
							compCombo.setModel(compModel2);
							condValuePanel.add(selectorCondCombo, 0);
							
							break;
						default:
							break;
					}
					condValuePanel.repaint();
			        RepaintManager.currentManager(condValuePanel).markCompletelyClean(condValuePanel);
			     }
			}
		});
		
		
		compCombo.setPreferredSize(new Dimension(150, 20));
		condItemsPanel.add(compCombo);
		compCombo.setMaximumSize(new Dimension(250, 20));
		compCombo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		compCombo.setMinimumSize(new Dimension(250, 20));		
		
		
		conditionPanel.add(condValuePanel, BorderLayout.SOUTH);
		condValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		
		selectorCondCombo.setPreferredSize(new Dimension(150, 20));
		selectorCondCombo.setMinimumSize(new Dimension(150, 20));
		//
		
		condvalueTextField = new JTextField();
		condvalueTextField.setMinimumSize(new Dimension(250, 20));
		condvalueTextField.setHorizontalAlignment(SwingConstants.LEFT);
		condValuePanel.add(condvalueTextField);
		condvalueTextField.setColumns(31);
		updateGalenValidations(crawlSettings);
	}
	public void updateGalenValidations(Settings crawlSettings) {
		if(condObj.get("type")!=null) {
			condTypeCombo.setSelectedItem(condObj.get("type").toString());
			compCombo.setSelectedItem(condObj.get("compare").toString());
			condvalueTextField.setText(condObj.get("value").toString());
			JSONArray galenArr = (JSONArray) condObj.get("galen-specs");
	 	   	for(Object eachText : galenArr) {
	 	   		String eachGalenVal = (String)((JSONObject)eachText).get("name");
	 	   		String eachGalenFile = (String)((JSONObject)eachText).get("file");
	 	   		if(!eachGalenVal.trim().equals("")) {
	 	   			addgalenTableModel.addRow(new String[] {eachGalenVal, eachGalenFile});
	 	   		}
	 	   	}
		}
 	   
	}
}
