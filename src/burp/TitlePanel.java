package burp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import target.TargetEntry;
import target.TargetMapTree;
import target.TargetMapTreeModel;

import javax.swing.JTree;

public class TitlePanel extends JPanel {

	private JPanel buttonPanel;
	private static LineTable titleTable;
	private JLabel lblSummaryOfTitle;
	private static JTextField textFieldCookie;
	public  JRadioButton rdbtnHideCheckedItems;
	//add table and tablemodel to GUI
	private static LineTableModel titleTableModel = new LineTableModel();
	PrintWriter stdout;
	PrintWriter stderr;
	private ThreadGetTitle threadGetTitle;
	private List<LineEntry> BackupLineEntries;
	private History searchHistory = new History(10);
	private TargetMapTree sitemapTree;

	public static LineTable getTitleTable() {
		return titleTable;
	}

	public static LineTableModel getTitleTableModel() {
		return titleTableModel;
	}

	public ThreadGetTitle getThreadGetTitle() {
		return threadGetTitle;
	}

	public List<LineEntry> getBackupLineEntries() {
		return BackupLineEntries;
	}

	public TargetMapTree getSitemapTree() {
		return sitemapTree;
	}


	public TitlePanel() {//构造函数

		try{
			stdout = new PrintWriter(BurpExtender.getCallbacks().getStdout(), true);
			stderr = new PrintWriter(BurpExtender.getCallbacks().getStderr(), true);
		}catch (Exception e){
			stdout = new PrintWriter(System.out, true);
			stderr = new PrintWriter(System.out, true);
		}

		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout(0, 0));
		this.add(createButtonPanel(), BorderLayout.NORTH);

		/////////////////////////////////////////
		JSplitPane TargetAndTitlePanel = new JSplitPane();//存放目标域名
		TargetAndTitlePanel.setResizeWeight(0.2);
		this.add(TargetAndTitlePanel,BorderLayout.CENTER);

		JScrollPane TargetMapPane = new JScrollPane();
		TargetMapPane.setPreferredSize(new Dimension(200, 200));
		TargetAndTitlePanel.setLeftComponent(TargetMapPane);


		titleTable = new LineTable(titleTableModel);
		TargetAndTitlePanel.setRightComponent(titleTable.getTableAndDetailSplitPane());

		TargetMapTreeModel treeModel=new TargetMapTreeModel();
		sitemapTree = new TargetMapTree(treeModel);
		TargetMapPane.setViewportView(sitemapTree);


		//sitemapTree.setModel(new TargetMapTreeModel(null));
	}

	public JPanel createButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JButton btnSyncTarget = new JButton("Sync Target");
		btnSyncTarget.setToolTipText("sync domain to Target");
		btnSyncTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnSyncTarget.setEnabled(false);
						syncTarget();
						btnSyncTarget.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						try {
							btnSyncTarget.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace(stderr);
						}
					}
				};
				worker.execute();
			}
		});
		buttonPanel.add(btnSyncTarget);

		JLabel cookieLabel = new JLabel("Cookie:");
		buttonPanel.add(cookieLabel);
		textFieldCookie = new JTextField("");
		textFieldCookie.setColumns(30);
		buttonPanel.add(textFieldCookie);


		JButton btnGettitle1 = new JButton("dnsquery");
		btnGettitle1.setToolTipText("A fresh start");
		btnGettitle1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//https://stackabuse.com/how-to-use-threads-in-java-swing/

				//method one: // don't need to wait threads in getAllTitle to exits
				//but hard to know the finish time of task
				//// Runs inside of the Swing UI thread
				/*			    SwingUtilities.invokeLater(new Runnable() {
			        public void run() {// don't need to wait threads in getAllTitle to exits
			        	btnGettitle.setEnabled(false);
			        	getAllTitle();
			        	btnGettitle.setEnabled(true);
			        	//domainResult.setLineEntries(TitletableModel.getLineEntries());
			        }
			    });*/

				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnGettitle1.setEnabled(false);
						getAllTitle();
						btnGettitle1.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						try {
							btnGettitle1.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace(stderr);
						}
					}
				};
				worker.execute();
			}
		});
		buttonPanel.add(btnGettitle1);

		JButton btnGettitle = new JButton("Get Title");
		btnGettitle.setToolTipText("A fresh start");
		btnGettitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//https://stackabuse.com/how-to-use-threads-in-java-swing/

				//method one: // don't need to wait threads in getAllTitle to exits
				//but hard to know the finish time of task
				//// Runs inside of the Swing UI thread
				/*			    SwingUtilities.invokeLater(new Runnable() {
			        public void run() {// don't need to wait threads in getAllTitle to exits
			        	btnGettitle.setEnabled(false);
			        	getAllTitle();
			        	btnGettitle.setEnabled(true);
			        	//domainResult.setLineEntries(TitletableModel.getLineEntries());
			        }
			    });*/

				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnGettitle.setEnabled(false);
						getAllTitle();
						btnGettitle.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						try {
							btnGettitle.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace(stderr);
						}
					}
				};
				worker.execute();
			}
		});
		buttonPanel.add(btnGettitle);

		JButton btnGetExtendtitle = new JButton("Get Extend Title");
		btnGetExtendtitle.setToolTipText("Get title of the host that in same subnet,you should do this after get domain title done!");
		btnGetExtendtitle.setEnabled(true);//default is false,only true after "get title" is done.
		btnGetExtendtitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnGetExtendtitle.setEnabled(false);
						getExtendTitle();
						btnGetExtendtitle.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						try {
							btnGetExtendtitle.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace(stderr);
						}
					}
				};
				worker.execute();
			}
		});
		buttonPanel.add(btnGetExtendtitle);

		JButton btnGetSubnet = new JButton("Get Subnet");
		btnGetSubnet.setEnabled(true);
		btnGetSubnet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {

						btnGetSubnet.setEnabled(false);
						int result = JOptionPane.showConfirmDialog(null,"Just get IP Subnets of [Current] lines ?");
						String subnetsString;
						if (result == JOptionPane.YES_OPTION) {
							subnetsString = getSubnet(true);
						}else {
							subnetsString = getSubnet(false);
						}
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						StringSelection selection = new StringSelection(subnetsString);
						clipboard.setContents(selection, null);
						stdout.println(subnetsString);
						btnGetSubnet.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						try {
							btnGetSubnet.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace(stderr);
						}
					}
				};
				worker.execute();
			}
		});
		buttonPanel.add(btnGetSubnet);

		//通过tableModelListener实现自动保存后，无需这个模块了
		JButton btnSaveState = new JButton("Save");
		btnSaveState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnSaveState.setEnabled(false);
						btnSaveState.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						btnSaveState.setEnabled(true);
					}
				};
				worker.execute();
			}
		});
		btnSaveState.setToolTipText("Save Data To DataBase");
		//buttonPanel.add(btnSaveState);


		InputMap inputMap1 = btnSaveState.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke Save = KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK); //Ctrl+S
		inputMap1.put(Save, "Save");

		btnSaveState.getActionMap().put("Save", new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Map, Map> worker = new SwingWorker<Map, Map>() {
					@Override
					protected Map doInBackground() throws Exception {
						btnSaveState.setEnabled(false);
						//saveDBfileToExtension();
						btnSaveState.setEnabled(true);
						return new HashMap<String, String>();
						//no use ,the return.
					}
					@Override
					protected void done() {
						btnSaveState.setEnabled(true);
					}
				};
				worker.execute();
			}
		});

		JTextField textFieldSearch = new JTextField("");
		textFieldSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textFieldSearch.getText().equals("Input text to search")) {
					textFieldSearch.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				/*
				 * if (textFieldSearch.getText().equals("")) {
				 * textFieldSearch.setText("Input text to search"); }
				 */

			}
		});

		textFieldSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = textFieldSearch.getText().trim();
				titleTable.search(keyword);
				searchHistory.addRecord(keyword);//记录搜索历史
			}
		});

		textFieldSearch.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)    
			{    
				if (e.getKeyCode()==KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_UP)//上键
				{
					try {
						String record = searchHistory.moveUP();
						if (record != null) {
							textFieldSearch.setText(record);
						}
					} catch (Exception ex) {
						ex.printStackTrace(stderr);
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_KP_DOWN || e.getKeyCode() == KeyEvent.VK_DOWN){
					try {
						String record = searchHistory.moveDown();
						if (record != null) {
							textFieldSearch.setText(record);
						}
					} catch (Exception ex) {	
						ex.printStackTrace(stderr);
					}
				}

			}
		});
		textFieldSearch.setColumns(30);
		buttonPanel.add(textFieldSearch);


		JButton buttonSearch = new JButton("Search");
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = textFieldSearch.getText().trim();
				titleTable.search(keyword);
				searchHistory.addRecord(keyword);
			}
		});
		buttonPanel.add(buttonSearch);

		rdbtnHideCheckedItems = new JRadioButton("Hide Checked");
		rdbtnHideCheckedItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = textFieldSearch.getText().trim();
				titleTable.search(keyword);
				//lineTable.getModel().unHideLines();
			}
		});
		buttonPanel.add(rdbtnHideCheckedItems);

		JButton btnRefresh = new JButton("Refresh");//主要目的是隐藏新标注的条目，代替自动隐藏
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = textFieldSearch.getText().trim();
				titleTable.search(keyword);
			}
		});
		buttonPanel.add(btnRefresh);

		JButton btnStatus = new JButton("status");
		btnStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				digStatus();
			}
		});
		btnStatus.setToolTipText("Show Status Of Digging.");
		buttonPanel.add(btnStatus);

		lblSummaryOfTitle = new JLabel("      ^_^");
		buttonPanel.add(lblSummaryOfTitle);

		return buttonPanel;
	}

	public void syncTarget() {
		Set<String> domains = DomainPanel.getDomainResult().getSubDomainSet();
		TargetMapTreeModel treeModel = (TargetMapTreeModel)sitemapTree.getModel();
		treeModel.addTargetsFromDomains(domains);
	}


	public void getAllTitle(){
		DomainPanel.backupDB();
		Set<String> domains = new HashSet<>();//新建一个对象，直接赋值后的删除操作，实质是对domainResult的操作。
		domains.addAll(DomainPanel.getDomainResult().getSubDomainSet());
		//remove domains in black list
		domains.removeAll(DomainPanel.getDomainResult().getBlackDomainSet());

		//backup to history
		BackupLineEntries = titleTableModel.getLineEntries();
		//clear tableModel

		titleTableModel.clear(true);//clear

		threadGetTitle = new ThreadGetTitle(domains);
		threadGetTitle.Do();
	}


	public void getExtendTitle(){
		Set<String> extendIPSet = titleTableModel.GetExtendIPSet();
		stdout.println(extendIPSet.size()+" extend IP Address founded"+extendIPSet);
		threadGetTitle = new ThreadGetTitle(extendIPSet);
		threadGetTitle.Do();
	}


	public String getSubnet(boolean isCurrent){
		Set<String> subnets;
		if (isCurrent) {//获取的是现有可成功连接的IP集合
			subnets = titleTableModel.GetSubnets();
		}else {//重新解析所有域名的IP
			Set<String> IPsOfDomain = new ThreadGetSubnet(BurpExtender.getGui().getDomainPanel().getDomainResult().getSubDomainSet()).Do();
			//Set<String> CSubNetIPs = Commons.subNetsToIPSet(Commons.toSubNets(IPsOfDomain));
			subnets = Commons.toSmallerSubNets(IPsOfDomain);
		}
		return String.join(System.lineSeparator(), subnets);
	}

	public void showToTitleUI(List<LineEntry> lineEntries) {
		//titleTableModel.setLineEntries(new ArrayList<LineEntry>());//clear
		//这里没有fire delete事件，会导致排序号加载文件出错，但是如果fire了又会触发tableModel的删除事件，导致数据库删除。改用clear()
		titleTableModel.clear(false);//clear
		titleTableModel.setListenerIsOn(false);
		for (LineEntry line:lineEntries) {
			titleTableModel.addNewLineEntry(line);
		}
		digStatus();
		stdout.println("Load Title Panel Data Done");
		titleTableModel.setListenerIsOn(true);
	}


	public void showToTargetUI(TargetMapTreeModel treeModel){
		if (treeModel ==null){
			return;
		}
		sitemapTree.setModel(treeModel);
		treeModel.fireTreeStructureChanged();
	}

	public void showToTargetUI2(TargetEntry rootNode){
		if (rootNode ==null){
			return;
		}
		((TargetMapTreeModel)sitemapTree.getModel()).setRootNode(rootNode);
		((TargetMapTreeModel)sitemapTree.getModel()).fireTreeStructureChanged();
	}




	public void digStatus() {
		String status = titleTableModel.getStatusSummary();
		lblSummaryOfTitle.setText(status);
	}

	public static JTextField getTextFieldCookie() {
		return textFieldCookie;
	}
}
