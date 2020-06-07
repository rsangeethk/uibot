package com.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.src.Trigger;

@SuppressWarnings("serial")
public class ExecutionQueueWindow extends JFrame {

	private JPanel contentPane;
	DefaultListModel<JPanel> listModel = new DefaultListModel<JPanel>();
	Hashtable<String, Trigger> execuList = new Hashtable<>();
	JPanel exeListpanel;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Create the frame.
	 */
	public ExecutionQueueWindow() {
		setTitle("Execution Queue");
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		exeListpanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(exeListpanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 440, 270);
		scrollPane.setPreferredSize(new Dimension(440, 250));
		exeListpanel.setLayout(new WrapFlowLayout());
		contentPane.add(scrollPane);
		
	}
	
	public void createNewExecution(String path) {
		if(!execuList.keySet().contains(new File(path).getName())) {
			JPanel newPanel = createExeJpanel(new File(path).getName());
			execuList.put(new File(path).getName(), new Trigger(path, newPanel));
		}
		else {
			JOptionPane.showMessageDialog(null, "The test you are trying to run is already In-Progress. \n Please wait until it completes or Stop and Re-run");
		}
	}
	
	public JPanel createExeJpanel(String name) {
		JPanel panel = new JPanel();
		//panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setPreferredSize(new Dimension(420, 30));
		exeListpanel.add(panel);
		
		JLabel fileLbl = new JLabel(name + " Running...");
		fileLbl.setPreferredSize(new Dimension(320, 14));
		fileLbl.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		fileLbl.setHorizontalTextPosition(SwingConstants.LEADING);
		fileLbl.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(fileLbl);
		
		JButton btnStop = new JButton("STOP");
		btnStop.setHorizontalTextPosition(SwingConstants.CENTER);
		btnStop.setToolTipText(name);
		panel.add(btnStop);
		btnStop.addActionListener(new ActionListener() {
          	@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
          		try {
          			Trigger curThread = execuList.get(btnStop.getToolTipText());
          			curThread.suspend();
          			curThread.doCleanUp();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error occured while preparing the report and closing the session : ");
				}
          		execuList.remove(btnStop.getToolTipText());
      			exeListpanel.remove(btnStop.getParent());
      			exeListpanel.repaint();
          		
          	}
          });
		return panel;
	}
}
