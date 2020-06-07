package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.src.Starter;

@SuppressWarnings("serial")
public class HoldFrame extends JDialog {

	private JPanel contentPane;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Create the frame.
	 */
	public HoldFrame(Starter start) throws Exception{
		super();
		setTitle("UI Bot - Initialization/Log in");
		setModal(true);
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		HoldFrame curUI = this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 209);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblTheExecutionIs = new JLabel("<html> <font color=\"red\"><center><b>UI Bot On Hold!!!</b></center><br/></font><center>The execution is paused to do Initialization/Login of the web page.<br/><br/>\r\nPlease navigate to the browser and do the Initialization steps. Once done click \"Continue\" to let the bot proceed.</center></html>");
		lblTheExecutionIs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTheExecutionIs.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTheExecutionIs, BorderLayout.CENTER);
		
		JButton btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					curUI.dispose();
				} catch (Exception e) {
				}
			}
		});
		btnContinue.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(btnContinue, BorderLayout.SOUTH);
		setVisible(true);
	}

}
