package com.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.model.Settings;

import mdlaf.utils.MaterialFonts;

import javax.swing.JList;

import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;

import org.json.simple.JSONObject;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

@SuppressWarnings({ "unused", "serial" })
public class ValidationType extends JFrame {

	private JPanel contentPane;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Create the frame.
	 */
	public ValidationType(Settings crawlSettings) {
		setTitle("Global Validation Types");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		
		DefaultListModel<String> typeListModel = new DefaultListModel<String>();
		
		typeListModel.addElement("UI Layout Validation - Galen");
		typeListModel.addElement("Text Availability");
		typeListModel.addElement("Element Availability");
		typeListModel.addElement("W3C HTML Validation");
		typeListModel.addElement("SSL Validation");
		JList<String> typeList = new JList<String>(typeListModel);
		typeList.setFont(MaterialFonts.REGULAR);
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(typeList, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton nextBtn = new JButton("Next>");
		nextBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				switch(typeList.getSelectedValue()) {
				case "UI Layout Validation - Galen":
					GalenValidation galenAvailWindow = new GalenValidation(crawlSettings);
					dispose();
					galenAvailWindow.setVisible(true);
					break;
				case "Element Availability":
					ElementValidation eleAvailWindow = new ElementValidation(crawlSettings);
					dispose();
					eleAvailWindow.setVisible(true);
					break;
				case "Text Availability":
					TextAvailabilityWindow textAvailWindow = new TextAvailabilityWindow(crawlSettings);
					dispose();
					textAvailWindow.setVisible(true);
					break;
				case "W3C HTML Validation":
					((JSONObject)crawlSettings.getProperty("validation-settings")).put("html", true);
					dispose();
					crawlSettings.getMainWindow().updateGrid();
					break;
				case "SSL Validation":
					((JSONObject)crawlSettings.getProperty("validation-settings")).put("ssl", true);
					dispose();
					crawlSettings.getMainWindow().updateGrid();
					break;
				default :
					break;
				}
			}
		});
		panel.add(nextBtn);
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel);
		
		/*JLabel validationTypeLbl = new JLabel("Select a validation type to add");
		validationTypeLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(validationTypeLbl, BorderLayout.NORTH);*/
	}

}
