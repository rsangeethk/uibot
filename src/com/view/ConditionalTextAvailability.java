package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import java.awt.ComponentOrientation;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;

import java.awt.Insets;
import java.awt.Point;

@SuppressWarnings("serial")
public class ConditionalTextAvailability extends JFrame {

	private JPanel contentPane;
	private JTable addTextTable;
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
	boolean newItem = false;

	/**
	 * Create the frame.
	 */
	
	public ConditionalTextAvailability(Settings crawlSettings, JSONObject condObj) {
		this.crawlSettings = crawlSettings;
		this.condObj = condObj;
		initComp();
		if(!condObj.containsKey("type")) {
			newItem = true;
		}
	}
	
	public void initComp() {
		setTitle("Conditional Text validation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
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
				String condtype = condTypeCombo.getSelectedItem().toString();
				condObj.put("type", condtype);
				condObj.put("compare", compCombo.getSelectedItem().toString());
				condObj.put("value", condvalueTextField.getText());
				JSONArray textArr = new JSONArray();
				for(int eachRow = 0; eachRow<addTextTableModel.getRowCount(); eachRow++) {
					if(!addTextTableModel.getValueAt(eachRow, 0).toString().trim().equals("")) {
						textArr.add(addTextTableModel.getValueAt(eachRow, 0));
					}
				}
				condObj.put("text", textArr);
				if(newItem) {
					JSONArray condVal = (JSONArray)crawlSettings.getProperty("validation-settings>conditional");
					condVal.add(condObj);
				}
				crawlSettings.writeTestFile();
				dispose();
				crawlSettings.getMainWindow().updateGrid();
			}
		});
		addTextPanel.add(Add);
		
		JButton cancelBtn = new JButton("Cancel");
		addTextPanel.add(cancelBtn);
		
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
		updateTextValidations(crawlSettings);
	}

	public void updateTextValidations(Settings crawlSettings) {
		if(condObj.get("type")!=null) {
		condTypeCombo.setSelectedItem(condObj.get("type").toString());
		compCombo.setSelectedItem(condObj.get("compare").toString());
		condvalueTextField.setText(condObj.get("value").toString());
		JSONArray textArr = (JSONArray) condObj.get("text");
 	   	for(Object eachText : textArr) {
 		   String eachtextVal = (String)eachText;
 		   if(!eachtextVal.trim().equals("")) {
 			  addTextTableModel.addRow(new String[] {eachtextVal});
 		   }
 	   }
		}
	}
}
