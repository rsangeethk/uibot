package com.view;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.model.Settings;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ConditionalElementValidation extends JFrame {

	private JPanel contentPane;
	private JTable addelementTable;
	DefaultComboBoxModel<String> byComboModel = new DefaultComboBoxModel<>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	JComboBox<String> ByCombo = new JComboBox<String>(byComboModel);
	private DefaultTableModel addelementTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"By","By Value"});
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
	boolean newItem;

	/**
	 * Create the frame.
	 */
	public ConditionalElementValidation(Settings crawlSettings, JSONObject condObj) {
		
		this.crawlSettings = crawlSettings;
		this.condObj = condObj;
		initComp();
		if(!condObj.containsKey("type")) {
			newItem = true;
		}
	}
	
	public void initComp() {

		setTitle("Conditional Element validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		
		
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
		//elementPanel.add(addelementTable, BorderLayout.CENTER);
		
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
				String condtype = condTypeCombo.getSelectedItem().toString();
				condObj.put("type", condtype);
				condObj.put("compare", compCombo.getSelectedItem().toString());
				condObj.put("value", condvalueTextField.getText());
				JSONArray eleArr = new JSONArray();
				for(int eachRow = 0; eachRow<addelementTableModel.getRowCount(); eachRow++) {
					if(!addelementTableModel.getValueAt(eachRow, 1).toString().trim().equals("")) {
						JSONObject eachCapabs = new JSONObject();
						eachCapabs.put("by", addelementTableModel.getValueAt(eachRow, 0));
						eachCapabs.put("selector", addelementTableModel.getValueAt(eachRow, 1).toString());
						eleArr.add(eachCapabs);
					}
				}
				condObj.put("element", eleArr);
				if(newItem) {
					JSONArray condVal = (JSONArray)crawlSettings.getProperty("validation-settings>conditional");
					condVal.add(condObj);
				}
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addelementPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addelementPanel.add(cancelBtn);
		
		JPanel conditionPanel = new JPanel();
		contentPane.add(conditionPanel, BorderLayout.NORTH);
		conditionPanel.setLayout(new BorderLayout(10, 0));
		
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
		updateEleValidations(crawlSettings);
	}
	
	public void updateEleValidations(Settings crawlSettings) {
		if(condObj.get("type")!=null) {
			condTypeCombo.setSelectedItem(condObj.get("type").toString());
			compCombo.setSelectedItem(condObj.get("compare").toString());
			condvalueTextField.setText(condObj.get("value").toString());
			JSONArray eleArr = (JSONArray) condObj.get("element");
	 	   for(Object eachText : eleArr) {
	 		   String eachBytVal = (String)((JSONObject)eachText).get("by");
	 		   String eachByValueVal = (String)((JSONObject)eachText).get("selector");
	 		   if(!eachBytVal.trim().equals("")) {
	 			  addelementTableModel.addRow(new String[] {eachBytVal, eachByValueVal});
	 		   }
	 	   }
		}
	}

}
