package com.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EncryptorWindow extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField encryptedText;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Create the frame.
	 */
	public EncryptorWindow() {
		setTitle("Password Encryptor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel fieldsPanel = new JPanel();
		contentPane.add(fieldsPanel, BorderLayout.CENTER);
		fieldsPanel.setLayout(new MigLayout("", "[100][grow]", "[30][60]"));
		
		JLabel lblTextToEncrypt = new JLabel("Text to encrypt");
		fieldsPanel.add(lblTextToEncrypt, "cell 0 0,alignx trailing");
		
		passwordField = new JPasswordField();
		fieldsPanel.add(passwordField, "cell 1 0,growx");
		
		encryptedText = new JTextField();
		encryptedText.setPreferredSize(new Dimension(6, 50));
		fieldsPanel.add(encryptedText, "cell 1 1,growx");
		encryptedText.setColumns(10);
		
		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pass = String.valueOf(passwordField.getPassword());
				if(!pass.trim().equals("")) {
					encryptedText.setText("^" + encryptText(pass) + "$");
				}
				else {
					JOptionPane.showMessageDialog(null, "Enter password to encrypt");
				}
			}
		});
		contentPane.add(btnEncrypt, BorderLayout.SOUTH);
	}
	
	public static String encryptText(String toDecrypt) {
		String decryptedText = null;

		try {
			// Get the key to encrypt with
			String secretKey = "CFAutomation";

			// initialize for encrypting
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "Blowfish"));
			byte[] decrypted = cipher.doFinal(toDecrypt.getBytes());
			decryptedText = bytesToHex(decrypted);
			} catch (InvalidKeyException e) {
				System.out.println("Invalid key, Please pass a valid key to encrypt.");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return decryptedText;
	}

	//Method will decrypt the string which was encrypted early with same key 'CFAutomation'  
	public static String decryptText(String toDecrypt) {
		String decryptedText = null;

		try {
			// Get the key to encrypt with
			String secretKey = "CFAutomation";

			// initialize for encrypting
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "Blowfish"));
			byte[] decrypted = cipher.doFinal(hexToBytes(toDecrypt));
			decryptedText = new String(decrypted);
			} catch (InvalidKeyException e) {
				System.out.println("Invalid key, Please pass a valid key to encrypt.");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return decryptedText;
	}

	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}

	}

	public static String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		} else {
			int len = data.length;
			String str = "";
			for (int i = 0; i < len; i++) {
				if ((data[i] & 0xFF) < 16)
					str = str + "0"
							+ java.lang.Integer.toHexString(data[i] & 0xFF);
				else
					str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
			}
			return str.toUpperCase();
		}
}
}
