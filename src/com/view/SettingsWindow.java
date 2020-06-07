package com.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import java.awt.ComponentOrientation;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.model.Settings;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.TableView.TableRow;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.util.prefs.Preferences;
import java.awt.event.ActionEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

@SuppressWarnings({ "unused", "serial" })
public class SettingsWindow extends JFrame {

	private JPanel contentPane;
	private JTextField urltextField;
	private JTextField maxPagesField;
	private JTextField headerByValueField;
	private JTextField footerByValueField;
	private JTextField contentByValueField;
	private JTextField ffbinaryField;
	private JTable ffdesiredCapabsTable;
	DefaultTableModel ffCapsModel = new DefaultTableModel(new String[] {"Key","Value"}, 0);
	private JTextField chromebinaryField;
	private JTable chromedesiredCapabsTable;
	private DefaultTableModel chromeCapsModel= new DefaultTableModel(new String[] {"Key","Value"}, 0);
	
	private JTextField iebinaryField;
	private JTable iedesiredCapabsTable;
	private DefaultTableModel ieCapsModel= new DefaultTableModel(new String[] {"Key","Value"}, 0);
	private JTextField testnameField;
	private JRadioButton crawlRadioBtn;
	private JRadioButton selectiveRadioBtn;
	private JTextArea selectivetxtArea;
	private JComboBox<String> headerByCombo;
	private JComboBox<String> footerByCombo;
	private JComboBox<String> contentByCombo;
	Settings crawlSettings;
	MainUI mainWIndow;
	Preferences prefs = Preferences.userRoot().node(MainUI.class.getName());
	
	DefaultComboBoxModel<String> helementComboModel = new DefaultComboBoxModel<>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	DefaultComboBoxModel<String> felementComboModel = new DefaultComboBoxModel<>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	DefaultComboBoxModel<String> celementComboModel = new DefaultComboBoxModel<>(new String[] {"xpath", "cssSelector", "id", "linkText", "partialLinkText", "name", "className", "tagName"});
	DefaultComboBoxModel<String> browserModel = new DefaultComboBoxModel<>(new String[] {"firefox", "chrome", "ie", "remote-driver"});
	private JComboBox<String> comboBox;
	private JCheckBox jQueryChck;
	private JCheckBox chckbxWaitForAngular;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private JCheckBox chckbxInitializationloginRequired;
	private JCheckBox chckbxSaveHTML;
	private JTextField rwbinaryField;
	private JTable rwdesiredCapabsTable;
	private DefaultTableModel rwCapsModel = new DefaultTableModel(new String[] {"Key","Value"}, 0);;
	private DefaultTableModel linkNameModel = new DefaultTableModel(new String[] {"Link Names"}, 0);
	private DefaultTableModel linkURLModel = new DefaultTableModel(new String[] {"Link URLs"}, 0);
	boolean reOpen = false;
	private JTextField ffDrivertextField;
	private JTextField chromeDrivertextField;
	private JTextField ieDrivertextField;
	private JTable linkNameTable;
	private JTable linkURLTable;
	private JCheckBox chckbxHidden;

	/**
	 * Create the frame.
	 */
	public SettingsWindow(Settings crawlSettings) {
		this.crawlSettings = crawlSettings;
		this.mainWIndow = crawlSettings.getMainWindow();
		initUI();
		if(!crawlSettings.getTestpath().equals("")) {
			updateSettingsUI();
			reOpen = true;
			testnameField.setEditable(false);
			testnameField.setEnabled(false);
		}
		else {
			updateRecommendedSettings();
		}
		setVisible(true);
	}	
	
	public void initUI() {
		//setSize(new Dimension(500, 650));
		setTitle("UI Bot Test Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 700);
		setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(450, 430));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel tabPanel = new JPanel();
		tabPanel.setPreferredSize(new Dimension(450, 420));
		contentPane.add(tabPanel, BorderLayout.CENTER);
		tabPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainTabbedPane.setPreferredSize(new Dimension(450, 420));
		tabPanel.add(mainTabbedPane, BorderLayout.CENTER);
		mainTabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		JPanel webSitePanel = new JPanel();
		webSitePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mainTabbedPane.addTab("Website details", null, webSitePanel, null);
		webSitePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel websiteDetailsPanel = new JPanel();
		webSitePanel.add(websiteDetailsPanel, BorderLayout.NORTH);
		websiteDetailsPanel.setLayout(new MigLayout("", "[140px,grow,left][300px:n,grow,fill]", "[20px][20px][20px:n][20]"));
		
		JLabel urlLabel = new JLabel("URL");
		urlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		websiteDetailsPanel.add(urlLabel, "cell 0 0,alignx left,growy");
		
		urltextField = new JTextField();
		websiteDetailsPanel.add(urltextField, "cell 1 0,alignx left,aligny top");
		urltextField.setColumns(10);
		
		JLabel lblType = new JLabel("Type");
		websiteDetailsPanel.add(lblType, "flowx,cell 0 1");
		
		JPanel typesRadioPanel = new JPanel();
		websiteDetailsPanel.add(typesRadioPanel, "cell 1 1");
		
		crawlRadioBtn = new JRadioButton("Crawl pages");
		crawlRadioBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					maxPagesField.setEditable(true);
					maxPagesField.setEnabled(true);
					selectivetxtArea.setEditable(false);
					selectivetxtArea.setEnabled(false);
				}
			}
		});
		typesRadioPanel.add(crawlRadioBtn);
		
		selectiveRadioBtn = new JRadioButton("Selective pages");
		selectiveRadioBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
				maxPagesField.setEditable(false);
				maxPagesField.setEnabled(false);
				selectivetxtArea.setEditable(true);
				selectivetxtArea.setEnabled(true);
				}
			}
		});
		typesRadioPanel.add(selectiveRadioBtn);
		
		ButtonGroup typeBtnGroup = new ButtonGroup();
		typeBtnGroup.add(selectiveRadioBtn);
		typeBtnGroup.add(crawlRadioBtn);
		
		JLabel lblMaximumPages = new JLabel("Maximum Pages");
		websiteDetailsPanel.add(lblMaximumPages, "cell 0 2,alignx left");
		
		maxPagesField = new JTextField();
		websiteDetailsPanel.add(maxPagesField, "cell 1 2,growx");
		maxPagesField.setColumns(10);
		
		JLabel lblSelectivePages = new JLabel("Selective Pages");
		websiteDetailsPanel.add(lblSelectivePages, "cell 0 3");
		
		JPanel selectivePagesPanel = new JPanel();
		webSitePanel.add(selectivePagesPanel, BorderLayout.CENTER);
		selectivePagesPanel.setLayout(new BorderLayout(0, 0));
		
		selectivetxtArea = new JTextArea();
		
		
		JScrollPane selectiveScrollPane = new JScrollPane(selectivetxtArea);
		selectivePagesPanel.add(selectiveScrollPane, BorderLayout.CENTER);
		selectivePagesPanel.add(selectiveScrollPane);
		
		JPanel webPartitionPanel = new JPanel();
		mainTabbedPane.addTab("Website settings", null, webPartitionPanel, null);
		webPartitionPanel.setLayout(new MigLayout("", "[419px,grow]", "[39px][39px][100px,grow]"));
		
		JPanel partitionDetailsPanel = new JPanel();
		//webPartitionPanel.add(partitionDetailsPanel, "cell 0 0,grow");
		partitionDetailsPanel.setLayout(new MigLayout("", "[80px:n:100px,grow,left][200px:n,grow,fill]", "[20px][20px][20px:n][20][20][20][20][20][20]"));
		
		JLabel lblHeaderElement = new JLabel("Header Element");
		partitionDetailsPanel.add(lblHeaderElement, "cell 0 0");
		
		JLabel lblHeaderBy = new JLabel("By");
		partitionDetailsPanel.add(lblHeaderBy, "cell 0 1");
		
		headerByCombo = new JComboBox<String>();
		headerByCombo.setModel(helementComboModel);
		headerByCombo.setSelectedIndex(1);
		partitionDetailsPanel.add(headerByCombo, "cell 1 1,growx");
		
		JLabel lblHeaderByValue = new JLabel("By Value");
		partitionDetailsPanel.add(lblHeaderByValue, "cell 0 2");
		
		headerByValueField = new JTextField();
		partitionDetailsPanel.add(headerByValueField, "cell 1 2,growx");
		headerByValueField.setColumns(10);
		
		JLabel lblFooterElement = new JLabel("Footer Element");
		partitionDetailsPanel.add(lblFooterElement, "cell 0 3");
		
		JLabel lblFooterBy = new JLabel("By");
		partitionDetailsPanel.add(lblFooterBy, "cell 0 4");
		
		footerByCombo = new JComboBox<String>();
		footerByCombo.setModel(felementComboModel);
		footerByCombo.setSelectedIndex(1);
		partitionDetailsPanel.add(footerByCombo, "cell 1 4,growx");
		
		JLabel lblFooterByValue = new JLabel("By Value");
		partitionDetailsPanel.add(lblFooterByValue, "cell 0 5");
		
		footerByValueField = new JTextField();
		partitionDetailsPanel.add(footerByValueField, "cell 1 5,growx");
		footerByValueField.setColumns(10);
		
		JLabel lblContentElement = new JLabel("Content Element");
		partitionDetailsPanel.add(lblContentElement, "cell 0 6");
		
		JLabel lblContentBy = new JLabel("By");
		partitionDetailsPanel.add(lblContentBy, "cell 0 7");
		
		contentByCombo = new JComboBox<String>();
		contentByCombo.setModel(celementComboModel);
		contentByCombo.setSelectedIndex(1);
		partitionDetailsPanel.add(contentByCombo, "cell 1 7,growx");
		
		JLabel lblContentByValue = new JLabel("By Value");
		partitionDetailsPanel.add(lblContentByValue, "cell 0 8");
		
		contentByValueField = new JTextField();
		partitionDetailsPanel.add(contentByValueField, "cell 1 8,growx");
		contentByValueField.setColumns(10);
		
		JPanel waitPanel = new JPanel();
		webPartitionPanel.add(waitPanel, "cell 0 1,growx,aligny top");
		waitPanel.setLayout(new MigLayout("", "[grow]", "[10,grow][10,grow][10,grow]"));
		
		JPanel waitTitlePanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) waitTitlePanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		waitPanel.add(waitTitlePanel, "cell 0 0,grow");
		
		JPanel savePanel = new JPanel();
		webPartitionPanel.add(savePanel, "cell 0 0");
		savePanel.setLayout(new MigLayout("", "[100px:n,grow]", "[30px][30px]"));
		
		JLabel lblSavehtml = new JLabel("Save HTML Files");
		savePanel.add(lblSavehtml, "cell 0 0");
		
		chckbxSaveHTML = new JCheckBox("Save HTML Files Locally in report folder");
		savePanel.add(chckbxSaveHTML, "cell 0 1");
		
		JLabel pageLoadTitleLbl = new JLabel("Page load handling");
		waitTitlePanel.add(pageLoadTitleLbl);
		
		JPanel jqueryWaitPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) jqueryWaitPanel.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		waitPanel.add(jqueryWaitPanel, "cell 0 1,grow");
		
		jQueryChck = new JCheckBox("Wait for jQuery Load");
		jqueryWaitPanel.add(jQueryChck);
		
		JPanel angularPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) angularPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		waitPanel.add(angularPanel, "cell 0 2,grow");
		
		chckbxWaitForAngular = new JCheckBox("Wait for angular Load");
		angularPanel.add(chckbxWaitForAngular);
		
		JPanel initPanel = new JPanel();
		webPartitionPanel.add(initPanel, "cell 0 2,grow");
		initPanel.setLayout(new MigLayout("", "[100px:n,grow]", "[30px][30px]"));
		
		JLabel lblInitializationlogin = new JLabel("Initialization/Login");
		initPanel.add(lblInitializationlogin, "cell 0 0");
		
		chckbxInitializationloginRequired = new JCheckBox("Initialization/Login required");
		initPanel.add(chckbxInitializationloginRequired, "cell 0 1");
		
		JPanel seleniumPanel = new JPanel();
		mainTabbedPane.addTab("Browser settings", null, seleniumPanel, null);
		seleniumPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane broswerPanel = new JTabbedPane(JTabbedPane.TOP);
		seleniumPanel.add(broswerPanel, BorderLayout.CENTER);
		
		JPanel ffPanel = new JPanel();
		broswerPanel.addTab("Firefox", null, ffPanel, null);
		ffPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel ffDetailsPanel = new JPanel();
		ffPanel.add(ffDetailsPanel, BorderLayout.NORTH);
		ffDetailsPanel.setLayout(new MigLayout("", "[100px:n:100px,grow,left][230px:n,grow,fill][60px:60px]", "[20px][20px][20px][grow]"));
		
		JLabel lblFirefoxExe = new JLabel("Firefox exe");
		ffDetailsPanel.add(lblFirefoxExe, "cell 0 0,alignx left,aligny center");
		
		ffbinaryField = new JTextField();
		ffDetailsPanel.add(ffbinaryField, "cell 1 0,alignx right,aligny top");
		ffbinaryField.setColumns(10);
		
		JButton button = new JButton("Browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = null;
			    try {
			        fileChooser = new JFileChooser();
			    } catch (Exception ex) {}
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					ffbinaryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		ffDetailsPanel.add(button, "cell 2 0");
		button.setFont(new Font("Tahoma", Font.PLAIN, 8));
		
		JLabel lblffDriver = new JLabel("Driver");
		ffDetailsPanel.add(lblffDriver, "cell 0 1");
		
		ffDrivertextField = new JTextField();
		ffDetailsPanel.add(ffDrivertextField, "cell 1 1,growx");
		ffDrivertextField.setColumns(10);
		
		JButton ffDriverBrowseBtn = new JButton("Browse");
		ffDriverBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = null;
			    try {
			        fileChooser = new JFileChooser();
			    } catch (Exception ex) {}
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					ffDrivertextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		ffDriverBrowseBtn.setFont(new Font("Tahoma", Font.PLAIN, 8));
		ffDetailsPanel.add(ffDriverBrowseBtn, "cell 2 1");
		
		JLabel lblDesiredCapabilities = new JLabel("Desired Capabilities: ");
		ffDetailsPanel.add(lblDesiredCapabilities, "cell 0 2,alignx left,aligny top");
		
		JPanel ffcapabilitiesPanel = new JPanel();
		ffPanel.add(ffcapabilitiesPanel, BorderLayout.CENTER);
		ffcapabilitiesPanel.setLayout(new BorderLayout(0, 0));
		
		
		ffdesiredCapabsTable = new JTable();
		ffdesiredCapabsTable.setModel(ffCapsModel);
		JScrollPane ffcapsScrollPane = new JScrollPane(ffdesiredCapabsTable);
		ffcapabilitiesPanel.add(ffcapsScrollPane);
		
		JPanel capabsControlPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) capabsControlPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		ffcapabilitiesPanel.add(capabsControlPanel, BorderLayout.NORTH);
		
		JButton ffbtnAdd = new JButton("Add");
		ffbtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ffCapsModel.addRow(new String[] {"",""});
			}
		});
		capabsControlPanel.add(ffbtnAdd);
		ffbtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JButton encryptorBtn = new JButton("Encryptor");
		encryptorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EncryptorWindow().setVisible(true);
			}
		});
		capabsControlPanel.add(encryptorBtn);
		
		JPanel chromePanel = new JPanel();
		broswerPanel.addTab("Chrome", null, chromePanel, null);
		chromePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel chromeDetailsPanel = new JPanel();
		chromePanel.add(chromeDetailsPanel, BorderLayout.NORTH);
		chromeDetailsPanel.setLayout(new MigLayout("", "[100px:n:100px,grow,left][230px:n,grow,fill][60px:60px]", "[20px][20px][20px][20px][grow]"));
		
		JLabel lblchromeExe = new JLabel("Chrome exe");
		chromeDetailsPanel.add(lblchromeExe, "cell 0 0,alignx left,aligny center");
		
		chromebinaryField = new JTextField();
		chromeDetailsPanel.add(chromebinaryField, "cell 1 0,alignx right,aligny top");
		chromebinaryField.setColumns(10);
		
		JButton chromeBrowsebutton = new JButton("Browse");
		chromeBrowsebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = null;
			    try {
			        fileChooser = new JFileChooser();
			    } catch (Exception ex) {}
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					chromebinaryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		chromeDetailsPanel.add(chromeBrowsebutton, "cell 2 0");
		chromeBrowsebutton.setFont(new Font("Tahoma", Font.PLAIN, 8));
		
		JLabel lblchromeDriver = new JLabel("Driver");
		chromeDetailsPanel.add(lblchromeDriver, "cell 0 1");
		
		chromeDrivertextField = new JTextField();
		chromeDetailsPanel.add(chromeDrivertextField, "cell 1 1,growx");
		chromeDrivertextField.setColumns(10);
		
		JButton chromeDriverBrowseBtn = new JButton("Browse");
		chromeDriverBrowseBtn.setFont(new Font("Tahoma", Font.PLAIN, 8));
		chromeDetailsPanel.add(chromeDriverBrowseBtn, "cell 2 1");
		chromeDriverBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = null;
			    try {
			        fileChooser = new JFileChooser();
			    } catch (Exception ex) {}
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					chromeDrivertextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JLabel lblchromeDesiredCapabilities = new JLabel("Desired Capabilities: ");
		chromeDetailsPanel.add(lblchromeDesiredCapabilities, "cell 0 3,alignx left,aligny top");
		
		JLabel lblHiddenMode = new JLabel("Mode");
		chromeDetailsPanel.add(lblHiddenMode, "cell 0 2");
		
		chckbxHidden = new JCheckBox("Hidden");
		chromeDetailsPanel.add(chckbxHidden, "cell 1 2");
		
		JPanel chromecapabilitiesPanel = new JPanel();
		chromePanel.add(chromecapabilitiesPanel, BorderLayout.CENTER);
		chromecapabilitiesPanel.setLayout(new BorderLayout(0, 0));
		
		
		chromedesiredCapabsTable = new JTable();
		chromedesiredCapabsTable.setModel(chromeCapsModel);
		JScrollPane chromecapsScrollPane = new JScrollPane(chromedesiredCapabsTable);
		chromecapabilitiesPanel.add(chromecapsScrollPane);
		
		JPanel chromecapabsControlPanel = new JPanel();
		FlowLayout chromeflowLayout = (FlowLayout) chromecapabsControlPanel.getLayout();
		chromeflowLayout.setAlignment(FlowLayout.LEFT);
		chromecapabilitiesPanel.add(chromecapabsControlPanel, BorderLayout.NORTH);
		
		JButton chromebtnAdd = new JButton("Add");
		chromebtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chromeCapsModel.addRow(new String[] {"",""});
			}
		});
		chromecapabsControlPanel.add(chromebtnAdd);
		chromebtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JButton chromeencryptorBtn = new JButton("Encryptor");
		chromeencryptorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EncryptorWindow().setVisible(true);
			}
		});
		chromecapabsControlPanel.add(chromeencryptorBtn);
		
		JPanel iePanel = new JPanel();
		broswerPanel.addTab("Internet Explorer", null, iePanel, null);
		iePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel ieDetailsPanel = new JPanel();
		iePanel.add(ieDetailsPanel, BorderLayout.NORTH);
		ieDetailsPanel.setLayout(new MigLayout("", "[100px:n:100px,grow,left][230px:n,grow,fill][60px:60px]", "[20px][20px][grow]"));
		
		JLabel lblieDriver = new JLabel("Driver");
		ieDetailsPanel.add(lblieDriver, "cell 0 0");
		
		ieDrivertextField = new JTextField();
		ieDetailsPanel.add(ieDrivertextField, "cell 1 0,growx");
		ieDrivertextField.setColumns(10);
		
		JButton ieDriverBrowseBtn = new JButton("Browse");
		ieDriverBrowseBtn.setFont(new Font("Tahoma", Font.PLAIN, 8));
		ieDetailsPanel.add(ieDriverBrowseBtn, "cell 2 0");
		chromeDriverBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = null;
			    try {
			        fileChooser = new JFileChooser();
			    } catch (Exception ex) {}
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					ieDrivertextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JLabel lblieDesiredCapabilities = new JLabel("Desired Capabilities: ");
		ieDetailsPanel.add(lblieDesiredCapabilities, "cell 0 1,alignx left,aligny top");
		
		JPanel iecapabilitiesPanel = new JPanel();
		iePanel.add(iecapabilitiesPanel, BorderLayout.CENTER);
		iecapabilitiesPanel.setLayout(new BorderLayout(0, 0));
		
		
		iedesiredCapabsTable = new JTable();
		iedesiredCapabsTable.setModel(ieCapsModel);
		JScrollPane iecapsScrollPane = new JScrollPane(iedesiredCapabsTable);
		iecapabilitiesPanel.add(iecapsScrollPane);
		
		JPanel iecapabsControlPanel = new JPanel();
		FlowLayout ieflowLayout = (FlowLayout) iecapabsControlPanel.getLayout();
		ieflowLayout.setAlignment(FlowLayout.LEFT);
		iecapabilitiesPanel.add(iecapabsControlPanel, BorderLayout.NORTH);
		
		JButton iebtnAdd = new JButton("Add");
		iebtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ieCapsModel.addRow(new String[] {"",""});
			}
		});
		iecapabsControlPanel.add(iebtnAdd);
		iebtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JButton ieencryptorBtn = new JButton("Encryptor");
		ieencryptorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EncryptorWindow().setVisible(true);
			}
		});
		iecapabsControlPanel.add(ieencryptorBtn);
		
		JPanel rwPanel = new JPanel();
		broswerPanel.addTab("Remote WebDriver", null, rwPanel, null);
		rwPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel rwDetailsPanel = new JPanel();
		rwPanel.add(rwDetailsPanel, BorderLayout.NORTH);
		rwDetailsPanel.setLayout(new MigLayout("", "[100px:n:100px,grow,left][230px:n,grow,fill]", "[20px][20px][grow]"));
		
		JLabel lblrwURL = new JLabel("URL");
		rwDetailsPanel.add(lblrwURL, "cell 0 0,alignx left,aligny center");
		
		rwbinaryField = new JTextField();
		rwDetailsPanel.add(rwbinaryField, "cell 1 0,alignx right,aligny top");
		rwbinaryField.setColumns(10);
		
		JLabel lblrwDesiredCapabilities = new JLabel("Desired Capabilities: ");
		rwDetailsPanel.add(lblrwDesiredCapabilities, "cell 0 1,alignx left,aligny top");
		
		JPanel rwcapabilitiesPanel = new JPanel();
		rwPanel.add(rwcapabilitiesPanel, BorderLayout.CENTER);
		rwcapabilitiesPanel.setLayout(new BorderLayout(0, 0));
		
		
		rwdesiredCapabsTable = new JTable();
		rwdesiredCapabsTable.setModel(rwCapsModel);
		JScrollPane rwcapsScrollPane = new JScrollPane(rwdesiredCapabsTable);
		rwcapabilitiesPanel.add(rwcapsScrollPane);
		
		JPanel capabsrwControlPanel = new JPanel();
		FlowLayout rwflowLayout = (FlowLayout) capabsrwControlPanel.getLayout();
		rwflowLayout.setAlignment(FlowLayout.LEFT);
		rwcapabilitiesPanel.add(capabsrwControlPanel, BorderLayout.NORTH);
		
		JButton rwbtnAdd = new JButton("Add");
		rwbtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rwCapsModel.addRow(new String[] {"",""});
			}
		});
		capabsrwControlPanel.add(rwbtnAdd);
		rwbtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JButton rwencryptorBtn = new JButton("Encryptor");
		rwencryptorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EncryptorWindow().setVisible(true);
			}
		});
		capabsrwControlPanel.add(rwencryptorBtn);
		
		JPanel browserPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) browserPanel.getLayout();
		flowLayout_1.setHgap(20);
		seleniumPanel.add(browserPanel, BorderLayout.NORTH);
		
		JLabel lblBrowser = new JLabel("Browser");
		browserPanel.add(lblBrowser);
		
		comboBox = new JComboBox<String>(browserModel);
		comboBox.setPreferredSize(new Dimension(150, 20));
		comboBox.setMinimumSize(new Dimension(150, 20));
		comboBox.setSelectedIndex(2);
		browserPanel.add(comboBox);
		
		JPanel exceptionPanel = new JPanel();
		mainTabbedPane.addTab("Exception", null, exceptionPanel, null);
		exceptionPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane exceptionTabPanel = new JTabbedPane(JTabbedPane.TOP);
		exceptionPanel.add(exceptionTabPanel);
		
		JPanel exceptionLinkNameTab = new JPanel();
		exceptionTabPanel.addTab("Link Text", null, exceptionLinkNameTab, null);
		exceptionLinkNameTab.setLayout(new BorderLayout(0, 0));
		
		JPanel linkNamesPanel = new JPanel();
		exceptionLinkNameTab.add(linkNamesPanel, BorderLayout.CENTER);
		linkNamesPanel.setLayout(new BorderLayout(0, 0));
		
		
		linkNameTable = new JTable();
		linkNameTable.setModel(linkNameModel);
		JScrollPane linkNamecapsScrollPane = new JScrollPane(linkNameTable);
		linkNamesPanel.add(linkNamecapsScrollPane);
		
		JPanel linkNameAddPanel = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) linkNameAddPanel.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		FlowLayout linkNameflowLayout = (FlowLayout) capabsrwControlPanel.getLayout();
		linkNameflowLayout.setAlignment(FlowLayout.LEFT);
		linkNamesPanel.add(linkNameAddPanel, BorderLayout.NORTH);
		
		JButton linkNamebtnAdd = new JButton("Add");
		linkNamebtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				linkNameModel.addRow(new String[] {""});
			}
		});
		linkNameAddPanel.add(linkNamebtnAdd);
		linkNamebtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JPanel exceptionLinkURLTab = new JPanel();
		exceptionTabPanel.addTab("Link URL", null, exceptionLinkURLTab, null);
		exceptionLinkURLTab.setLayout(new BorderLayout(0, 0));
		
		JPanel linkURLsPanel = new JPanel();
		exceptionLinkURLTab.add(linkURLsPanel);
		linkURLsPanel.setLayout(new BorderLayout(0, 0));
		
		
		linkURLTable = new JTable();
		linkURLTable.setModel(linkURLModel);
		JScrollPane linkURLcapsScrollPane = new JScrollPane(linkURLTable);
		linkURLsPanel.add(linkURLcapsScrollPane);
		
		JPanel linkURLAddPanel = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) linkURLAddPanel.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		FlowLayout linkURLflowLayout = (FlowLayout) capabsrwControlPanel.getLayout();
		linkURLflowLayout.setAlignment(FlowLayout.LEFT);
		linkURLsPanel.add(linkURLAddPanel, BorderLayout.NORTH);
		
		JButton linkURLbtnAdd = new JButton("Add");
		linkURLbtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				linkURLModel.addRow(new String[] {""});
			}
		});
		linkURLAddPanel.add(linkURLbtnAdd);
		linkURLbtnAdd.setMaximumSize(new Dimension(60, 25));
		
		JPanel footerBtns = new JPanel();
		tabPanel.add(footerBtns, BorderLayout.SOUTH);
		footerBtns.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton addSaveBtn = new JButton("Add");
		addSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateJSONValue();
				dispose();
				mainWIndow.updateTree();
				mainWIndow.updateSettings(crawlSettings);
				mainWIndow.updateGrid();
			}
		});
		addSaveBtn.setHorizontalAlignment(SwingConstants.RIGHT);
		footerBtns.add(addSaveBtn);
		
		JButton calcelBtn = new JButton("Cancel");
		footerBtns.add(calcelBtn);
		
		JPanel headerPanel = new JPanel();
		tabPanel.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new MigLayout("", "[100px:n:100px][300,grow,fill]", "[30px]"));
		
		JLabel lblName = new JLabel("Name");
		headerPanel.add(lblName, "cell 0 0,alignx leading");
		
		testnameField = new JTextField();
		headerPanel.add(testnameField, "cell 1 0,growx");
		testnameField.setColumns(10);
	}

	public void generateJSONValue() {
		String givenName = testnameField.getText().trim();
		if(!new File(crawlSettings.getProjectpath() +"\\"+ givenName+".json").exists()) {
			setWebSiteDetails();
			crawlSettings.writeTestFile(givenName);
		}
		else if(reOpen) {
			setWebSiteDetails();
			crawlSettings.writeTestFile();
		}
		else {
			JOptionPane.showMessageDialog(null, "Name already taken. Please try some other name!");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void setWebSiteDetails() {
		((JSONObject)crawlSettings.getProperty("crawl-test")).put("name", testnameField.getText().trim());
		((JSONObject)crawlSettings.getProperty("website-details")).put("URL", urltextField.getText().trim());
		((JSONObject)crawlSettings.getProperty("website-details>header-element")).put("by", headerByCombo.getSelectedItem().toString());
		((JSONObject)crawlSettings.getProperty("website-details>header-element")).put("selector", headerByValueField.getText().trim());
		((JSONObject)crawlSettings.getProperty("website-details>footer-element")).put("by", footerByCombo.getSelectedItem().toString());
		((JSONObject)crawlSettings.getProperty("website-details>footer-element")).put("selector", footerByValueField.getText().trim());
		((JSONObject)crawlSettings.getProperty("website-details>content-element")).put("by", contentByCombo.getSelectedItem().toString());
		((JSONObject)crawlSettings.getProperty("website-details>content-element")).put("selector", contentByValueField.getText().trim());
		
		((JSONObject)crawlSettings.getProperty("website-details")).put("jquery-wait", jQueryChck.isSelected());
		((JSONObject)crawlSettings.getProperty("website-details")).put("angular-wait", chckbxWaitForAngular.isSelected());
		((JSONObject)crawlSettings.getProperty("website-details")).put("init-login", chckbxInitializationloginRequired.isSelected());
		((JSONObject)crawlSettings.getProperty("website-details")).put("source", chckbxSaveHTML.isSelected());
		if(crawlRadioBtn.isSelected()) {
			((JSONObject)crawlSettings.getProperty("website-details")).put("type", "crawl");
			((JSONObject)crawlSettings.getProperty("website-details")).put("maximum-pages", maxPagesField.getText());
		}else {
			((JSONObject)crawlSettings.getProperty("website-details")).put("type", "selective-pages");
			JSONArray selectivePages = new JSONArray();
			String selectivePagesCnt = selectivetxtArea.getText();
			if(!selectivePagesCnt.trim().equals("")) {
				for(String eachURL : selectivePagesCnt.split("\n")) {
					selectivePages.add(eachURL);
				}
				((JSONObject)crawlSettings.getProperty("website-details")).put("selective-pages", selectivePages);
			}
		}
		
		((JSONObject)crawlSettings.getProperty("run-settings")).put("browser", comboBox.getSelectedItem().toString());
		
		((JSONObject)crawlSettings.getProperty("selenium-settings>firefox")).put("binary", ffbinaryField.getText());
		((JSONObject)crawlSettings.getProperty("selenium-settings>firefox")).put("driver", ffDrivertextField.getText());
		JSONArray ffcapabilitiesArray = new JSONArray();
		for(int eachffRow = 0; eachffRow<ffCapsModel.getRowCount(); eachffRow++) {
			if(!ffCapsModel.getValueAt(eachffRow, 0).toString().trim().equals("") && !ffCapsModel.getValueAt(eachffRow, 1).toString().trim().equals("")) {
				JSONObject eachCapabs = new JSONObject();
				eachCapabs.put("Key", ffCapsModel.getValueAt(eachffRow, 0));
				eachCapabs.put("Value", ffCapsModel.getValueAt(eachffRow, 1));
				ffcapabilitiesArray.add(eachCapabs);
			}
			
		}
		((JSONObject)crawlSettings.getProperty("selenium-settings>firefox")).put("capabilities", ffcapabilitiesArray);
		
		((JSONObject)crawlSettings.getProperty("selenium-settings>chrome")).put("binary", chromebinaryField.getText());
		((JSONObject)crawlSettings.getProperty("selenium-settings>chrome")).put("driver", chromeDrivertextField.getText());
		((JSONObject)crawlSettings.getProperty("selenium-settings>chrome")).put("headless", chckbxHidden.isSelected());
		JSONArray chromecapabilitiesArray = new JSONArray();
		for(int eachchromeRow = 0; eachchromeRow<chromeCapsModel.getRowCount(); eachchromeRow++) {
			if(!chromeCapsModel.getValueAt(eachchromeRow, 0).toString().trim().equals("") && !chromeCapsModel.getValueAt(eachchromeRow, 1).toString().trim().equals("")) {
				JSONObject eachCapabs = new JSONObject();
				eachCapabs.put("Key", chromeCapsModel.getValueAt(eachchromeRow, 0));
				eachCapabs.put("Value", chromeCapsModel.getValueAt(eachchromeRow, 1));
				chromecapabilitiesArray.add(eachCapabs);
			}
		}
		((JSONObject)crawlSettings.getProperty("selenium-settings>chrome")).put("capabilities", chromecapabilitiesArray);
		
		((JSONObject)crawlSettings.getProperty("selenium-settings>ie")).put("driver", ieDrivertextField.getText());
		JSONArray iecapabilitiesArray = new JSONArray();
		for(int eachieRow = 0; eachieRow<ieCapsModel.getRowCount(); eachieRow++) {
			if(!ieCapsModel.getValueAt(eachieRow, 0).toString().trim().equals("") && !ieCapsModel.getValueAt(eachieRow, 1).toString().trim().equals("")) {
				JSONObject eachCapabs = new JSONObject();
				eachCapabs.put("Key", ieCapsModel.getValueAt(eachieRow, 0));
				eachCapabs.put("Value", ieCapsModel.getValueAt(eachieRow, 1));
				iecapabilitiesArray.add(eachCapabs);
			}
		}
		((JSONObject)crawlSettings.getProperty("selenium-settings>ie")).put("capabilities", iecapabilitiesArray);
		
		((JSONObject)crawlSettings.getProperty("selenium-settings>remote-driver")).put("url", rwbinaryField.getText());
		
		JSONArray rwcapabilitiesArray = new JSONArray();
		for(int eachrwRow = 0; eachrwRow<rwCapsModel.getRowCount(); eachrwRow++) {
			if(!rwCapsModel.getValueAt(eachrwRow, 0).toString().trim().equals("") && !rwCapsModel.getValueAt(eachrwRow, 1).toString().trim().equals("")) {
				JSONObject eachCapabs = new JSONObject();
				eachCapabs.put("Key", rwCapsModel.getValueAt(eachrwRow, 0));
				eachCapabs.put("Value", rwCapsModel.getValueAt(eachrwRow, 1));
				rwcapabilitiesArray.add(eachCapabs);
			}
		}
		((JSONObject)crawlSettings.getProperty("selenium-settings>remote-driver")).put("capabilities", rwcapabilitiesArray);
		
		JSONArray linkNamesArray = new JSONArray();
		for(int eachrwRow = 0; eachrwRow<linkNameModel.getRowCount(); eachrwRow++) {
			if(!linkNameModel.getValueAt(eachrwRow, 0).toString().trim().equals("")) {
				linkNamesArray.add(linkNameModel.getValueAt(eachrwRow, 0));
			}
		}
		((JSONObject)crawlSettings.getProperty("exceptions")).put("link-names", linkNamesArray);
		
		JSONArray linkURLsArray = new JSONArray();
		for(int eachrwRow = 0; eachrwRow<linkURLModel.getRowCount(); eachrwRow++) {
			if(!linkURLModel.getValueAt(eachrwRow, 0).toString().trim().equals("")) {
				linkURLsArray.add(linkURLModel.getValueAt(eachrwRow, 0));
			}
		}
		((JSONObject)crawlSettings.getProperty("exceptions")).put("link-urls", linkURLsArray);
	}
	
	public void updateSettingsUI() {
		testnameField.setText((String)crawlSettings.getProperty("crawl-test>name"));
		urltextField.setText((String)crawlSettings.getProperty("website-details>URL"));
		headerByCombo.setSelectedItem((String)crawlSettings.getProperty("website-details>header-element>by"));
		headerByValueField.setText((String)crawlSettings.getProperty("website-details>header-element>selector"));
		footerByCombo.setSelectedItem((String)crawlSettings.getProperty("website-details>footer-element>by"));
		footerByValueField.setText((String)crawlSettings.getProperty("website-details>footer-element>selector"));
		contentByCombo.setSelectedItem((String)crawlSettings.getProperty("website-details>content-element>by"));
		contentByValueField.setText((String)crawlSettings.getProperty("website-details>content-element>selector"));
		
		jQueryChck.setSelected(((boolean)crawlSettings.getProperty("website-details>jquery-wait")));
		chckbxWaitForAngular.setSelected(((boolean)crawlSettings.getProperty("website-details>angular-wait")));
		chckbxInitializationloginRequired.setSelected(((boolean)crawlSettings.getProperty("website-details>init-login")));
		chckbxSaveHTML.setSelected(((boolean)crawlSettings.getProperty("website-details>source")));
		
		if(((String)crawlSettings.getProperty("website-details>type")).equals("crawl")) {
			crawlRadioBtn.setSelected(true);
			maxPagesField.setText((String)crawlSettings.getProperty("website-details>maximum-pages"));
		}else {
			selectiveRadioBtn.setSelected(true);
			JSONArray selectivePages = ((JSONArray)crawlSettings.getProperty("website-details>selective-pages"));
			String selectivePagesCnt = "";
			for(Object eachURL : selectivePages) {
				String eachCapabs = (String)eachURL;
				selectivePagesCnt = selectivePagesCnt + "\n" + eachCapabs;
				//ffCapsModel.addRow(new String[] {eachCapabs.get("Key").toString(), eachCapabs.get("Value").toString()});
			}
			selectivetxtArea.setText(selectivePagesCnt);
		}
		
		comboBox.setSelectedItem((String)crawlSettings.getProperty("run-settings>browser"));
		
		ffbinaryField.setText(((String)crawlSettings.getProperty("selenium-settings>firefox>binary")));
		ffDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>firefox>driver")));
		JSONArray ffcapabilitiesArray = ((JSONArray)crawlSettings.getProperty("selenium-settings>firefox>capabilities"));
		for(Object eachCapab : ffcapabilitiesArray) {
			JSONObject eachCapabObj = (JSONObject)eachCapab;
			ffCapsModel.addRow(new String[] {eachCapabObj.get("Key").toString(), eachCapabObj.get("Value").toString()});
			
		}
		
		chromebinaryField.setText(((String)crawlSettings.getProperty("selenium-settings>chrome>binary")));
		chromeDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>chrome>driver")));
		chckbxHidden.setSelected(((boolean)crawlSettings.getProperty("selenium-settings>chrome>headless")));
		JSONArray chromecapabilitiesArray = ((JSONArray)crawlSettings.getProperty("selenium-settings>chrome>capabilities"));
		for(Object eachCapab : chromecapabilitiesArray) {
			JSONObject eachCapabObj = (JSONObject)eachCapab;
			chromeCapsModel.addRow(new String[] {eachCapabObj.get("Key").toString(), eachCapabObj.get("Value").toString()});
		}
		
		ieDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>ie>driver")));
		JSONArray iecapabilitiesArray = ((JSONArray)crawlSettings.getProperty("selenium-settings>ie>capabilities"));
		for(Object eachCapab : iecapabilitiesArray) {
			JSONObject eachCapabObj = (JSONObject)eachCapab;
			ieCapsModel.addRow(new String[] {eachCapabObj.get("Key").toString(), eachCapabObj.get("Value").toString()});
			
		}
		rwbinaryField.setText(((String)crawlSettings.getProperty("selenium-settings>remote-driver>url")));
		JSONArray rwcapabilitiesArray = ((JSONArray)crawlSettings.getProperty("selenium-settings>remote-driver>capabilities"));
		for(Object eachCapab : rwcapabilitiesArray) {
			JSONObject eachCapabObj = (JSONObject)eachCapab;
			rwCapsModel.addRow(new String[] {eachCapabObj.get("Key").toString(), eachCapabObj.get("Value").toString()});
		}
		
		JSONArray linkNamesArray = ((JSONArray)crawlSettings.getProperty("exceptions>link-names"));
		for(Object eachCapab : linkNamesArray) {
			String eachCapabObj = (String)eachCapab;
			linkNameModel.addRow(new String[] {eachCapabObj});
		}
		
		JSONArray linkURLsArray = ((JSONArray)crawlSettings.getProperty("exceptions>link-urls"));
		for(Object eachCapab : linkURLsArray) {
			String eachCapabObj = (String)eachCapab;
			linkURLModel.addRow(new String[] {eachCapabObj});
		}
	}
	
	public void updateRecommendedSettings() {
		ieDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>ie>driver")));
		chromeDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>chrome>driver")));
		ffDrivertextField.setText(((String)crawlSettings.getProperty("selenium-settings>firefox>driver")));
	}
	
}
