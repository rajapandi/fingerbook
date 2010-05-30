package com.fingerbook.client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fingerbook.client.Client;
import com.fingerbook.models.Response;
import com.l2fprod.common.swing.JDirectoryChooser;

public class Front extends JFrame {
	private static final long serialVersionUID = 2440070097533761701L;

	private Map<String, String> configuration = new HashMap<String, String>();

	private JPanel jContentPane = null;

	private JTextField tDir = null;
	private JButton bBrowse = null;
	private JCheckBox cRecursive = null;
	private JButton bIni = null;
	
	private TrayIcon trayIcon;
	private Boolean minimizedNotice = false;

	public Front() {
		initialize();

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);

		// Set Icon
		Image icon = toolkit.getImage("src/main/resources/images/thumb.png");
	    setIconImage(icon);
		setVisible(true);
	}

	/**
	 * This method sets some Frames parameters
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(590, 180);
		this.setResizable(false);
		this.setTitle("fbClient"); //$NON-NLS-1$

		// MenuBar
		JMenuBar menuBar = new MainMenu();
		this.setJMenuBar(menuBar);

		this.setContentPane(getJContentPane());

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {

//				if (JOptionPane.showConfirmDialog(null,
//						Messages.getString("Front.1"), //$NON-NLS-1$
//						Messages.getString("Front.2"), JOptionPane.OK_CANCEL_OPTION) == 0) //$NON-NLS-1$
//					System.exit(0);

				Client.front.setVisible(false);
				if(!minimizedNotice) {
					trayIcon.displayMessage("Minimized to tray","Fingerbook Client has been minimized to tray",TrayIcon.MessageType.INFO);
					minimizedNotice = true;
				}
			}
		});
		addSysTrayIcon();

	}

	/**
	 * This method removes '/.' from the end of the path string
	 */
	public void setDir(JTextField tDir, String defaultDir) {
		if (defaultDir.endsWith("\\.") || defaultDir.endsWith("/.")) //$NON-NLS-1$ //$NON-NLS-2$
			configuration.put("scanDir", defaultDir.substring(0, defaultDir //$NON-NLS-1$
					.length() - 2));
		else
			configuration.put("scanDir", defaultDir); //$NON-NLS-1$
		tDir.setText(configuration.get("scanDir")); //$NON-NLS-1$
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTDir(), null);
			jContentPane.add(getBBrowse(), null);
			jContentPane.add(getCRecursive(), null);
			jContentPane.add(getBIni(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes tDir JTextBox
	 * 
	 * @return javax.swing.JTextBox
	 */
	private JTextField getTDir() {
		if (tDir == null) {
			tDir = new JTextField();

			tDir.setColumns(50);
			tDir.setEditable(false);
			tDir.setBounds(new Rectangle(10, 10, 500, 30));

			setDir(tDir, new File(".").getAbsolutePath().toString()); //$NON-NLS-1$
		}
		return tDir;
	}

	/**
	 * This method initializes bBrowse JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBBrowse() {
		if (bBrowse == null) {
			bBrowse = new JButton(Messages.getString("Front.9")); //$NON-NLS-1$
			bBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDirectoryChooser chooser = new JDirectoryChooser("."); //$NON-NLS-1$
					chooser.setShowingCreateDirectory(false);
					int choice = chooser.showOpenDialog((Component) e
							.getSource());
					if (choice == JDirectoryChooser.APPROVE_OPTION) {
						configuration.put("scanDir", chooser.getSelectedFile() //$NON-NLS-1$
								.getAbsolutePath());
						tDir.setText(chooser.getSelectedFile()
								.getAbsolutePath());
					}
				}
			});
			bBrowse.setBounds(new Rectangle(520, 10, 60, 30));
		}
		return bBrowse;
	}

	/**
	 * This method initializes cRecursive JCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCRecursive() {
		if (cRecursive == null) {
			cRecursive = new JCheckBox(Messages.getString("Front.12")); //$NON-NLS-1$
			cRecursive.setSelected(false);
			configuration.put("recursive", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			cRecursive.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cRecursive.isSelected())
						configuration.put("recursive", "true"); //$NON-NLS-1$ //$NON-NLS-2$
					else
						configuration.put("recursive", "false"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			});
			cRecursive.setBounds(new Rectangle(10, 45, 100, 30));
		}
		return cRecursive;
	}

	/**
	 * This method initializes bIni JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBIni() {
		if (bIni == null) {
			bIni = new JButton(Messages.getString("Front.19")); //$NON-NLS-1$
			bIni.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Response resp = null;
					try {
						resp = Client.getScanner().scanDirectory(
								configuration.get("scanDir"), //$NON-NLS-1$
								(HashMap<String, String>) configuration);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					if (resp == null || resp.getErrorCode() != null)
						JOptionPane.showMessageDialog(
								(Component) e.getSource(), Messages.getString("Front.21")); //$NON-NLS-1$
					else {
						JOptionPane.showMessageDialog(
								(Component) e.getSource(), Messages.getString("Front.22") + ":\n" //$NON-NLS-1$ //$NON-NLS-2$
										+ resp.getDesc());
					}
				}
			});
			bIni.setBounds(new Rectangle(10, 85, 570, 30));
		}
		return bIni;
	}
	
	private void addSysTrayIcon(){
		
		if (SystemTray.isSupported()) {
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icon.jpg");
		    
		    MouseListener mouseListener = new MouseListener() {
                
		        public void mouseClicked(MouseEvent e) {
		        	if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
		        		Client.front.setVisible(true);
		        	}                
		        }

		        public void mouseEntered(MouseEvent e){}
		        public void mouseExited(MouseEvent e){}
		        public void mousePressed(MouseEvent e){}
		        public void mouseReleased(MouseEvent e){}
		    };

		            
		    PopupMenu popup = new ClientPopupMenu();
		    
		    trayIcon = new TrayIcon(image, "Tray Demo", popup);
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addMouseListener(mouseListener);
 
//		    ActionListener actionListener = new ActionListener() {
//		        public void actionPerformed(ActionEvent e) {
//		            trayIcon.displayMessage("Action Event", 
//		                "An Action Event Has Been Performed!",
//		                TrayIcon.MessageType.INFO);
//		        }
//		    };
//		    trayIcon.addActionListener(actionListener);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        System.err.println("TrayIcon could not be added.");
		    }

		} else {

		    //  System Tray is not supported
			System.err.println("Warning: System Tray Not Supported");

		}

	}
}
