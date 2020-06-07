package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class RunSettings extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public RunSettings() {
		setTitle("Run Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel browsersPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) browsersPanel.getLayout();
		flowLayout.setVgap(40);
		flowLayout.setHgap(20);
		contentPane.add(browsersPanel, BorderLayout.NORTH);
		
		JLabel lblBrowser = new JLabel("Browser");
		browsersPanel.add(lblBrowser);
		
		JComboBox<String> browserCombo = new JComboBox<String>();
		browserCombo.setPreferredSize(new Dimension(150, 20));
		browsersPanel.add(browserCombo);
		
		JPanel runPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) runPanel.getLayout();
		flowLayout_1.setHgap(15);
		flowLayout_1.setVgap(15);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		contentPane.add(runPanel, BorderLayout.SOUTH);
		
		JButton btnRun = new JButton("Run");
		runPanel.add(btnRun);
		
		JButton btnCancel = new JButton("Cancel");
		runPanel.add(btnCancel);
	}

}
