package com.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.EnumSet;
import java.util.prefs.Preferences;

import javax.servlet.DispatcherType;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.model.Settings;
import com.src.Starter;

import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFonts;
import nu.validator.servlet.InboundGzipFilter;
import nu.validator.servlet.InboundSizeLimitFilter;
import nu.validator.servlet.Main;
import nu.validator.servlet.MultipartFormDataFilter;
import nu.validator.servlet.VerifierServlet;


@SuppressWarnings("serial")
public class MainUI extends JFrame {

       private JPanel contentPane;
       public JSplitPane splitpane1, spiltpane2;
       JTextComponent textComponent ;
       public KeyAdapter shortcut;
       private static final long SIZE_LIMIT = Integer.parseInt(System.getProperty(
	            "nu.validator.servlet.max-file-size", "2097152"));

       private JTree Tree;
       final String[] columnNames = { "Validations" };
       Object[][] data = {};
       DefaultTableModel model = new DefaultTableModel(data, columnNames){
    	    @Override
    	    public boolean isCellEditable(int row, int column) {
    	       return false;
    	    }
    	};
       JTable table = new JTable(model);
       Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
       protected String projectPath;
       Settings curSettings;
       MouseListener ml;
       MainUI curClassObj = this;
   		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
   		ExecutionQueueWindow exeQueue;
   		//Server server;
       
       /**
       * Launch the application.
       */
       public static void main(String[] args) {
    	   MainUI ui = new MainUI();
    	   if(!ui.isServerReachable()) {
    			try {
    				Thread vnuServer = new Thread() {
    					public void run() {
    						ui.startServlet();
    					}
    				};
    				vnuServer.start();
    			}
    			catch(Exception ex) {
    				System.out.println(ex.getMessage());
    			}
    		}
              EventQueue.invokeLater(new Runnable() {
                     public void run() {
                           try {
                        	   if(args.length==1) {
                           			new Starter(args[0]);
	                           	}
	                           	else {
	                           		UIManager.setLookAndFeel (new MaterialLookAndFeel ());
	                           		UIManager.put("Button.background", MaterialColors.GRAY_200);
	                           		UIManager.put("Label.font", MaterialFonts.REGULAR);
	                           		UIManager.put("Menu.font", MaterialFonts.REGULAR);
	                           		UIManager.put("MenuItem.font", MaterialFonts.REGULAR);
	                           		UIManager.put("JTree.lineStyle", "Angled" );
	                           		MainUI ui = new MainUI();
	                           		ui.setExtendedState(ui.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	                           		
	                           		ui.setVisible(true);
	                           	}     

                           } catch (Exception e) {
                                  e.printStackTrace();
                           }
                     }
              });
       }

       /**
       * Create the frame.
       */
       public MainUI() {
              super("Java Frame");
              setTitle("UI Bot");
              setIconImage(Toolkit.getDefaultToolkit().getImage(MainUI.class.getResource("../../favicon-32x32.png")));
              setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
              setBounds(0, 0, dim.width/2, dim.height/2);
              //setLocation(dim.width/2- getSize().width/2, dim.height/2-getSize().height/2);
              contentPane = new JPanel();
              contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
              contentPane.setLayout(new BorderLayout(0, 0));
              setContentPane(contentPane);

              JPanel panel = new JPanel();
              panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
              panel.setMinimumSize(new Dimension(150, 10));
              //contentPane.add(panel, BorderLayout.WEST);
              panel.setLayout(new BorderLayout(0, 0));
              

             /* DefaultMutableTreeNode rootNode;
              rootNode = new DefaultMutableTreeNode("Browse");
              DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
              treeModel.addTreeModelListener(new MyTreeModelListener());*/
              

              Tree = new JTree();
              Tree.setMaximumSize(new Dimension(150, 64));
              Tree.setPreferredSize(new Dimension(100, 64));
              Tree.setEditable(true);
              Tree.setModel(null);
              Tree.setVisible(false);
              
              
              /*Tree.getSelectionModel().setSelectionMode(
                           TreeSelectionModel.SINGLE_TREE_SELECTION);
              Tree.setShowsRootHandles(true);
              String[] anArray = { "Click", "Enter", "Wait", "Navigate", "Refresh" };

              for (int i = 0; i < anArray.length; i++) {
                     treeModel.insertNodeInto(new DefaultMutableTreeNode(anArray[i]),
                                  rootNode, rootNode.getChildCount());
              }*/
              
              
              JScrollPane treeScrollPane = new JScrollPane(Tree);
              treeScrollPane.setPreferredSize(new Dimension(200, 322));
              panel.add(treeScrollPane);
              

              JPanel panel_1 = new JPanel();
              //contentPane.add(panel_1, BorderLayout.CENTER);
              panel_1.setLayout(new BorderLayout(0, 0));
              DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
              centerRenderer.setHorizontalAlignment( JLabel.LEFT );
              table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
              //table.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
              
              
                            
              JScrollPane tablepane=new JScrollPane(table);
              panel_1.add(tablepane);
              
              JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, panel_1);
              contentPane.add(splitPane, BorderLayout.CENTER);
              JMenuBar menuBar = new JMenuBar();
              setJMenuBar(menuBar);

              JMenu mnFile = new JMenu("File");
              menuBar.add(mnFile);
              
              JMenuItem mnOpen = new JMenuItem("Open Folder");
              mnOpen.addActionListener(new ActionListener() {
              	public void actionPerformed(ActionEvent arg0) {
              		JFileChooser fileChooser = null;
    			    try {
    			        fileChooser = new JFileChooser();
    			    } catch (Exception ex) {}
    			    String recentPath = prefs.get("CrawlTester_Recent_Path", "");
    				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    				if(!recentPath.equals("")) {
    					fileChooser.setCurrentDirectory(new File(recentPath));
    				}
    				int rVal = fileChooser.showOpenDialog(null);
    				if (rVal == JFileChooser.APPROVE_OPTION) {
    					prefs.put("CrawlTester_Recent_Path", fileChooser.getSelectedFile().getAbsolutePath());
    					Tree.setVisible(false);
    					Tree.setModel(null);
    					//TreeModel model = new FileTreeModel(new File(fileChooser.getSelectedFile().getAbsolutePath()));
    					Tree.setVisible(true);
    					Tree.setRootVisible(false);
    					Tree.setShowsRootHandles(true);
    					Tree.putClientProperty( "JTree.lineStyle", "Angled" );
    					Tree.setModel(createTreeModel(new File(fileChooser.getSelectedFile().getAbsolutePath())));
    					Tree.repaint();
    			        projectPath = fileChooser.getSelectedFile().getAbsolutePath();
    			        RepaintManager.currentManager(Tree).markCompletelyClean(Tree);
    				}
              	}
              });
              mnFile.add(mnOpen);

              JMenu mnNew = new JMenu("New");
              mnFile.add(mnNew);
              
              JMenuItem mnCrawlTest = new JMenuItem("UIBot Test");
              mnCrawlTest.addActionListener(new ActionListener() {
              	@SuppressWarnings("unused")
				public void actionPerformed(ActionEvent arg0) {
              		Settings newSettings;
					try {
						newSettings = new Settings(projectPath, curClassObj);
						SettingsWindow setWindows = new SettingsWindow(newSettings);
					} catch (Exception e) {
						e.printStackTrace();
					}
              		
              	}
              });
              mnNew.add(mnCrawlTest);
              JMenuItem mntmSettings = new JMenuItem("Settings");
              mntmSettings.addActionListener(new ActionListener() {
              	@SuppressWarnings("unused")
    			public void actionPerformed(ActionEvent arg0) {
              		SettingsWindow settWindow = new SettingsWindow(curSettings);
              	}
              });
              mnFile.add(mntmSettings);
              
              JMenuItem mntmExit = new JMenuItem("Exit");
              mntmExit.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent arg0) {
              		System.exit(0);
              	}
              });
              mnFile.add(mntmExit);
              
              JMenu editMenu = new JMenu("Edit");


              /*Action cutAction = new DefaultEditorKit.CutAction();
             cutAction.putValue(Action.NAME, "Cut");

             Action copyAction = new DefaultEditorKit.CopyAction();
             copyAction.putValue(Action.NAME, "Copy");

             Action pasteAction = new DefaultEditorKit.PasteAction();
             pasteAction.putValue(Action.NAME, "Paste");
             */
             JMenuItem selectall = new JMenuItem("Select all", KeyEvent.VK_A);
             selectall.setAccelerator(
                       KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
             
              editMenu.add(selectall);
              
              selectall.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent arg0) {
                     table.selectAll();
                  }
              });
            
             JMenuItem deletemenu = new JMenuItem("Delete");
             deletemenu.addActionListener(new ActionListener() {
             	@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent arg0) {
             		if (table.getSelectedRow() != -1) {
        	        	String value = table.getValueAt(table.getSelectedRow(), 0).toString().split(":")[0].trim();
        	        	switch(value) {
	        				case "UI Layout validation":
	        					((JSONObject)curSettings.getProperty("validation-settings")).put("galen-specs", new JSONArray());
	        					break;
	        				case "Element Availability validation":
	        					((JSONObject)curSettings.getProperty("validation-settings")).put("element", new JSONArray());
	        					break;
	        				case "Text Availability validation":
	        					((JSONObject)curSettings.getProperty("validation-settings")).put("text", new JSONArray());
	        					break;
	        				case "Conditional validation" :
	        					int firstCondRow = 0;
	        					for(int eachRow = 0; eachRow<=table.getSelectedRow(); eachRow++) {
	        						if(!table.getValueAt(eachRow, 0).toString().split(":")[0].trim().equals("Conditional validation")) {
	        							firstCondRow++;
	        						}
	        						else {
	        							break;
	        						}
	        					}
	        					int curRow = table.getSelectedRow() - firstCondRow;
	        					JSONArray condArr = (JSONArray)curSettings.getProperty("validation-settings>conditional");
	        					condArr.remove(curRow);
	        					break;
	        				case "W3C HTML validation":
	        					((JSONObject)curSettings.getProperty("validation-settings")).put("html", false);
	        					break;
	        				case "SSL validation":
	        					((JSONObject)curSettings.getProperty("validation-settings")).put("ssl", false);
	        					break;
	        				default :
	        					break;
        				}
        	        }
             		updateGrid();
             	}
             });
             deletemenu.setAccelerator(
                       KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK));
             editMenu.add(deletemenu);
             menuBar.add(editMenu);
             

          JMenu mnTest = new JMenu("Validations");
          menuBar.add(mnTest);
          
          JMenuItem mntmAddValidation = new JMenuItem("Add global validation");
          mntmAddValidation.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		ValidationType validationType = new ValidationType(curSettings);
          		validationType.setVisible(true);
          	}
          });
          mnTest.add(mntmAddValidation);
          
          JMenuItem mntmAddConditionalValidation = new JMenuItem("Add conditional validation");
          mntmAddConditionalValidation.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		ConditionalValidationType validationType = new ConditionalValidationType(curSettings);
            		validationType.setVisible(true);
            	}
            });
          mnTest.add(mntmAddConditionalValidation);
          
          
          JMenu mnRun = new JMenu("Run");
          mnRun.setMnemonic(KeyEvent.VK_F);
          menuBar.add(mnRun);
          JMenuItem newMenuItem = new JMenuItem("Run", KeyEvent.VK_N);
          newMenuItem.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent arg0) {
          		try {
					//Starter start = new Starter(curSettings.getTestpath());
					exeQueue.createNewExecution(curSettings.getTestpath());
				} catch (Exception e) {
					e.printStackTrace();
				}
          		
          	}
          });
          mnRun.add(newMenuItem);
          
          JMenuItem mntmExecutionQueue = new JMenuItem("Execution Queue");
          mntmExecutionQueue.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		exeQueue.setVisible(true);
          	}
          });
          mnRun.add(mntmExecutionQueue);

          /*JMenu mnWindow = new JMenu("Window");
          menuBar.add(mnWindow);*/

          JMenu mnHelp = new JMenu("Help");
          menuBar.add(mnHelp);
          
          /*deletemenu.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                 int[] selectedRows = table.getSelectedRows();
                  // check for selected row first
                  for (int i = selectedRows.length - 1; i >= 0; i--){ 
                      // remove selected row from the model
                      model.removeRow(selectedRows[i]);
                  }
              }    
          });*/
          
          ml = new MouseAdapter() {
  			public void mousePressed(MouseEvent e) {
  				try {
  					if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
  						/*TreePath selPath = tcTree.getPathForLocation(e.getX(), e.getY());
  						if(new File(selPath.getLastPathComponent() + "\\moduleDetails.ts").exists()) {
  							moduleMenu.show(e.getComponent(), e.getX(), e.getY());
  						}
  						else {
  							treeMenu.show(e.getComponent(), e.getX(), e.getY());
  						}*/
  				    }
  					else if (e.getClickCount() == 2) {
  						TreePath selPath = Tree.getPathForLocation(e.getX(), e.getY());
  						String filePath = projectPath + "\\" + selPath.getLastPathComponent().toString();
						if(new File(filePath).exists()) {
							Settings newSettings = new Settings(filePath, projectPath, curClassObj);
							curSettings = newSettings;
							updateGrid();
						}
  					}
  				}
  				catch(Exception exc) {
  					
  				}
  			}
          };
          
          table.addMouseListener(new MouseAdapter() {
        	    public void mousePressed(MouseEvent mouseEvent) {
        	        JTable table =(JTable) mouseEvent.getSource();
        	        Point point = mouseEvent.getPoint();
        	        int row = table.rowAtPoint(point);
        	        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row!=-1) {
        	        	String value = table.getValueAt(row, 0).toString().split(":")[0].trim();
        	        	switch(value) {
	        				case "UI Layout validation":
	        					GalenValidation galenAvailWindow = new GalenValidation(curSettings);
	        					galenAvailWindow.setVisible(true);
	        					break;
	        				case "Element Availability validation":
	        					ElementValidation eleAvailWindow = new ElementValidation(curSettings);
	        					eleAvailWindow.setVisible(true);
	        					break;
	        				case "Text Availability validation":
	        					TextAvailabilityWindow textAvailWindow = new TextAvailabilityWindow(curSettings);
	        					textAvailWindow.setVisible(true);
	        					break;
	        				case "Conditional validation" :
	        					int firstCondRow = 0;
	        					for(int eachRow = 0; eachRow<=row; eachRow++) {
	        						if(!table.getValueAt(eachRow, 0).toString().split(":")[0].trim().equals("Conditional validation")) {
	        							firstCondRow++;
	        						}
	        						else {
	        							break;
	        						}
	        					}
	        					int curRow = row - firstCondRow;
	        					JSONArray condArr = (JSONArray)curSettings.getProperty("validation-settings>conditional");
	        					JSONObject clickedObj = (JSONObject)condArr.get(curRow);
	        					if(clickedObj.containsKey("text")) {
	        		    			ConditionalTextAvailability textAva = new ConditionalTextAvailability(curSettings, clickedObj);
	        		    			textAva.setVisible(true);
	    		    		    }
	    		    		    else if(clickedObj.containsKey("element")) {
	    		    			    ConditionalElementValidation eleAva = new ConditionalElementValidation(curSettings, clickedObj);
	    		    			    eleAva.setVisible(true);
	    		    		    }
	    		    		    else if((clickedObj).containsKey("galen-specs")) {
	    		    			    ConditionalGalenValidation galenAva = new ConditionalGalenValidation(curSettings, clickedObj);
	    		    			    galenAva.setVisible(true);
	    		    		    }
	        				default :
	        					break;
        				}
        	        }
        	    }
        	});
          Tree.addMouseListener(ml);
          exeQueue = new ExecutionQueueWindow();
       }
       
       public void updateTree() {
    	   	Tree.setModel(null);
    	   	Tree.setVisible(true);
			Tree.setRootVisible(false);
			Tree.setShowsRootHandles(true);
			Tree.putClientProperty( "JTree.lineStyle", "Angled" );
			Tree.setModel(createTreeModel(new File(projectPath)));
			Tree.repaint();
	        RepaintManager.currentManager(Tree).markCompletelyClean(Tree);
       }
       
       public void updateGrid() {
    	   while (model.getRowCount() > 0) {
    		   model.removeRow(0);
			}
    	   JSONArray textArr = ((JSONArray)curSettings.getProperty("validation-settings>text"));
    	   if(textArr.size()>0) {
	    	   String textStep = "Text Availability validation : ";
	    	   for(Object eachText : textArr) {
	    		   String eachtextVal = (String)eachText;
	    		   if(!eachtextVal.trim().equals("")) {
	    			   textStep = textStep + eachtextVal + "; ";
	    		   }
	    	   }
	    	   model.addRow(new String[] {textStep});
    	   }
    	   
    	   JSONArray eleArr = ((JSONArray)curSettings.getProperty("validation-settings>element"));
    	   String eleStep = "Element Availability validation : ";
    	   if(eleArr.size()>0) {
	    	   for(Object eachText : eleArr) {
	    		   String eachBytVal = (String)((JSONObject)eachText).get("by");
	    		   String eachByValueVal = (String)((JSONObject)eachText).get("selector");
	    		   if(!eachBytVal.trim().equals("")) {
	    			   eleStep = eleStep + eachBytVal + ":" + eachByValueVal+ "; ";
	    		   }
	    	   }
	    	   model.addRow(new String[] {eleStep});
    	   }
    	   
    	   JSONArray galenArr = ((JSONArray)curSettings.getProperty("validation-settings>galen-specs"));
    	   String galenStep = "UI Layout validation : ";
    	   if(galenArr.size()>0) {
	    	   for(Object eachText : galenArr) {
	    		   String eachGalenVal = (String)((JSONObject)eachText).get("name");
	    		   if(!eachGalenVal.trim().equals("")) {
	    			   galenStep = galenStep + eachGalenVal + "; ";
	    		   }
	    	   }
	    	   model.addRow(new String[] {galenStep});
    	   }
    	   
    	   boolean htmlVal = ((boolean)curSettings.getProperty("validation-settings>html"));
    	   if(htmlVal) {
    		   model.addRow(new String[] {"W3C HTML validation"});
    	   }
    	   
    	   boolean sslVal = ((boolean)curSettings.getProperty("validation-settings>ssl"));
    	   if(sslVal) {
    		   model.addRow(new String[] {"SSL validation"});
    	   }
    	   
    	   JSONArray condArr = ((JSONArray)curSettings.getProperty("validation-settings>conditional"));
    	   
    	   if(condArr.size()>0) {
	    	   for(Object eachText : condArr) {
	    		   String condStep = "Conditional validation : ";
	    		   condStep = condStep + "Condition : " + (String)((JSONObject)eachText).get("type");
	    		   if(((JSONObject)eachText).containsKey("text")) {
	    			   condStep = condStep + "; Validation : " + "Text";
	    		   }
	    		   else if(((JSONObject)eachText).containsKey("element")) {
	    			   condStep = condStep + "; Validation : " + "Element";
	    		   }
	    		   else if(((JSONObject)eachText).containsKey("galen-specs")) {
	    			   condStep = condStep + "; Validation : " + "UI Layout";
	    		   }
	    		   model.addRow(new String[] {condStep});
	    	   }
    	   }
    	   
    	   curSettings.writeTestFile(); 
       }
       
       public void updateSettings(Settings crawSettings) {
    	   this.curSettings = crawSettings;
       }
       
       public DefaultTreeModel createTreeModel(File dirPath) {
    	   DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(dirPath.getName());
    	   for(File eachFile: dirPath.listFiles()) {
    		   if(eachFile.getName().endsWith(".uibot")) {
    			   rootNode.add(new DefaultMutableTreeNode(eachFile.getName()));
    		   }
    	   }
    	   DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    	   return treeModel;
       }
       
       
		public void startServlet() {
			
				try {
					if (!"1".equals(System.getProperty("nu.validator.servlet.read-local-log4j-properties"))) {
			            PropertyConfigurator.configure(Main.class.getClassLoader().getResource(
			                    "nu/validator/localentities/files/log4j.properties"));
			        } else {
			            PropertyConfigurator.configure(System.getProperty(
			                    "nu.validator.servlet.log4j-properties", "log4j.properties"));
			        }
			
			        ServletContextHandler contextHandler = new ServletContextHandler();
			        contextHandler.setContextPath("/");
			        contextHandler.addFilter(new FilterHolder(new GzipFilter()), "/*",
			                EnumSet.of(DispatcherType.REQUEST));
			        contextHandler.addFilter(new FilterHolder(new InboundSizeLimitFilter(
			                SIZE_LIMIT)), "/*", EnumSet.of(DispatcherType.REQUEST));
			        contextHandler.addFilter(new FilterHolder(new InboundGzipFilter()),
			                "/*", EnumSet.of(DispatcherType.REQUEST));
			        contextHandler.addFilter(
			                new FilterHolder(new MultipartFormDataFilter()), "/*",
			                EnumSet.of(DispatcherType.REQUEST));
			        contextHandler.addServlet(new ServletHolder(new VerifierServlet()),
			                "/*");
			
			        Server server = new Server(new QueuedThreadPool(100));
			        server.setHandler(contextHandler);
			
			        ServerConnector serverConnector = new ServerConnector(server,
			                new HttpConnectionFactory(new HttpConfiguration()));
			        int port = 8888;
			        serverConnector.setPort(port);
			        server.setConnectors(new Connector[] { serverConnector });
			
			        int stopPort = -1;
			        if (stopPort != -1) {
			            try (Socket clientSocket = new Socket(
			                    InetAddress.getByName("127.0.0.1"), stopPort);
			                    InputStream in = clientSocket.getInputStream()) {
			                in.read();
			            } catch (ConnectException e) {
			
			            }
			
			            server.start();
			
			            try (ServerSocket serverSocket = new ServerSocket(stopPort, 0,
			                    InetAddress.getByName("127.0.0.1"));
			                    Socket s = serverSocket.accept()) {
			                server.stop();
			
			                s.getOutputStream().close();
			            }
			        } else {
			            server.start();
			        }
					}
					catch(Exception e) {
						
					}
				
		}
		public boolean isServerReachable() {
			int timeout = 2000;
			String url = "http://localhost:8888/";
			url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
	
		    try {
		        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		        connection.setConnectTimeout(timeout);
		        connection.setReadTimeout(timeout);
		        connection.setRequestMethod("HEAD");
		        int responseCode = connection.getResponseCode();
		        return (200 <= responseCode && responseCode <= 399);
		    } catch (IOException exception) {
		        return false;
		    }
		}
}
