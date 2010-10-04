package com.fingerbook.client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fingerbook.client.Client;
import com.fingerbook.client.TicketFile;
import com.fingerbook.client.gui.helpers.GBHelper;
import com.fingerbook.client.gui.helpers.Gap;
import com.l2fprod.common.swing.JDirectoryChooser;

public class Front extends JFrame {
	private static final long serialVersionUID = 2440070097533761701L;

	Logger logger = LoggerFactory.getLogger(Client.class);

	private static final int BORDER = 12; // Window border in pixels.
	private static final int GAP = 5; // Default gap btwn components

	private static Map<String, String> configuration = new HashMap<String, String>();

	private boolean resume = false;
	
	private JPanel jContentPane = null;

	private JCheckBox cLogin = null;
	private JLabel lUser = null;
	private JTextField tUser = null;
	private JLabel lPass = null;
	private JTextField tPass = null;

	private JCheckBox cTicket = null;
	private JLabel lTicket = null;
	private JTextField tTicket = null;
	private JButton bTicket = null;

	private JLabel lDir = null;
	private JTextField tDir = null;
	private JButton bBrowse = null;
	private JCheckBox cRecursive = null;
	private JButton bIni = null;
	
	private ProgressBar pBar = null;
	public static boolean inProgress = false;

	public static TrayIcon trayIcon;
	public static Boolean minimizedNotice = false;

	public Front(boolean populate, boolean resume) {
		initialize();

		// Center window
		setLocationRelativeTo(null);

		// Set Icon
		setIconImage(getToolkit().getImage(
				"src/main/resources/images/thumb.png")); //$NON-NLS-1$
		
		if (populate)
			populate();

		setVisible(true);
		
		if (resume) {
			if (JOptionPane.showConfirmDialog(null,
					"fbClient has detected that last scanning was interrupted. Do you want to resume it?", "Resume" , JOptionPane.OK_CANCEL_OPTION) == 0) {
				this.resume = true;
				proceed();
			}
		}
	}

	private void populate() {
		configuration = Client.fMan.getLastParams();
		tDir.setText(configuration.get("scanDir")); //$NON-NLS-1$
		if (configuration.get("cTicket").equals("true")) {
			cTicket.setSelected(true);
			tTicket.setEnabled(true);
			tTicket.setText(configuration.get("ticket")); //$NON-NLS-1$
		}		
		if (configuration.get("recursive").equals("true"))
			cRecursive.setSelected(true);
	}

	/**
	 * This method sets some Frames parameters
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		// this.setSize(590, 180);
		// this.setResizable(false);
		this.setTitle("fbClient"); //$NON-NLS-1$

		// MenuBar
		JMenuBar menuBar = new MainMenu();
		this.setJMenuBar(menuBar);

		this.setContentPane(getJContentPane());

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (!minimizedNotice) {
					trayIcon.displayMessage(Messages.getString("Front.10"), //$NON-NLS-1$
							Messages.getString("Front.11"), //$NON-NLS-1$
							TrayIcon.MessageType.INFO);
					minimizedNotice = true;
				}
			}
		});
		addSysTrayIcon();

		this.pack();
	}

	/**
	 * This method removes '/.' from the end of the path string
	 */
	public void setDir(JTextField tDir, String defaultDir) {
		if (defaultDir.endsWith("\\.") || defaultDir.endsWith("/.")) //$NON-NLS-1$ //$NON-NLS-2$
			tDir.setText(defaultDir.substring(0, defaultDir.length() - 2));
		else
			tDir.setText(defaultDir); //$NON-NLS-1$
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.Container
	 */
	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER));

			// Create GridBag helper object.
			GBHelper pos = new GBHelper();

			// ... Next Row
			jContentPane.add(getCLogin(), pos.width(2));

			// ... Next Row
			jContentPane.add(getLUser(), pos.nextRow());
			jContentPane.add(getTUser(), pos.nextCol().expandW());

			// ... Next Row
			jContentPane.add(getLPass(), pos.nextRow());
			jContentPane.add(getTPass(), pos.nextCol().expandW());

			// ... Next Row
			jContentPane.add(new Gap(GAP * 4), pos.nextRow());

			// ... Next Row
			jContentPane.add(getCTicket(), pos.nextRow().width(2));

			// ... Next Row
			jContentPane.add(getLTicket(), pos.nextRow());
			jContentPane.add(getTTicket(), pos.nextCol().expandW());
			jContentPane.add(getBTicket(), pos.nextCol());

			// ... Next Row
			jContentPane.add(new Gap(GAP * 7), pos.nextRow());

			// ... Next Row
			jContentPane.add(getLDir(), pos.nextRow());
			jContentPane.add(getTDir(), pos.nextCol().expandW());
			jContentPane.add(getBBrowse(), pos.nextCol());

			// ... Next Row
			jContentPane.add(getCRecursive(), pos.nextRow());

			// ... Next Row
			jContentPane.add(new Gap(GAP), pos.nextRow());

			// ... Next Row
			jContentPane.add(getBIni(), pos.nextRow().width(3));

			// ... Next Row
			jContentPane.add(new Gap(), pos.nextRow().width().expandH());
		}
		return jContentPane;
	}

	/**
	 * This method initializes cLogin JCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCLogin() {
		if (cLogin == null) {
			cLogin = new JCheckBox(Messages.getString("Front.5")); //$NON-NLS-1$
			cLogin.setSelected(false);
			cLogin.setEnabled(false);

			cLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cLogin.isSelected()) {
						tUser.setEnabled(true);
						tPass.setEnabled(true);
					} else {
						tUser.setEnabled(false);
						tPass.setEnabled(false);
					}
				}
			});
		}
		return cLogin;
	}

	/**
	 * This method initializes lUser JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLUser() {
		if (lUser == null)
			lUser = new JLabel(Messages.getString("Front.7") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

		return lUser;
	}

	/**
	 * This method initializes tUser JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTUser() {
		if (tUser == null) {
			tUser = new JTextField(20);
			tUser.setEnabled(false);
		}
		return tUser;
	}

	/**
	 * This method initializes lPass JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLPass() {
		if (lPass == null)
			lPass = new JLabel(Messages.getString("Front.13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

		return lPass;
	}

	/**
	 * This method initializes tPass JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTPass() {
		if (tPass == null) {
			tPass = new JTextField(20);

			tPass.setEnabled(false);
		}
		return tPass;
	}

	/**
	 * This method initializes cLogin JCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCTicket() {
		if (cTicket == null) {
			cTicket = new JCheckBox(Messages.getString("Front.6")); //$NON-NLS-1$
			cTicket.setSelected(false);

			cTicket.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cTicket.isSelected()) {
						tTicket.setEnabled(true);
						bTicket.setEnabled(true);
					} else {
						tTicket.setEnabled(false);
						bTicket.setEnabled(false);
					}
				}
			});
		}
		return cTicket;
	}

	/**
	 * This method initializes lTicket JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLTicket() {
		if (lTicket == null)
			lTicket = new JLabel(Messages.getString("Front.15") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

		return lTicket;
	}

	/**
	 * This method initializes tTicket JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTTicket() {
		if (tTicket == null) {
			tTicket = new JTextField(30);

			/* Ticket Field is originally disabled */
			tTicket.setEnabled(false);
			/* Ticket can be entered manually? */
			tTicket.setEditable(false);
		}
		return tTicket;
	}

	/**
	 * This method initializes bTicket JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBTicket() {
		if (bTicket == null) {
			bTicket = new JButton(new ImageIcon(getToolkit().getImage(
					"src/main/resources/images/browse2.gif"))); //$NON-NLS-1$

			bTicket.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser("."); //$NON-NLS-1$
					
					int choice = chooser.showOpenDialog(null);
					if (choice == JFileChooser.APPROVE_OPTION) {
						try {
							TicketFile ticket = new TicketFile(chooser.getSelectedFile());
							getTTicket().setText(ticket.getTicket());
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(
											(Component) e.getSource(),
											Messages.getString("Front.16"), Messages.getString("Front.0"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			});
			bTicket.setEnabled(false);
		}
		return bTicket;
	}

	/**
	 * This method initializes lDir JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLDir() {
		if (lDir == null)
			lDir = new JLabel(Messages.getString("Front.17") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

		return lDir;
	}

	/**
	 * This method initializes tDir JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTDir() {
		if (tDir == null) {
			tDir = new JTextField(50);

			setDir(tDir, new File(".").getAbsolutePath().toString()); //$NON-NLS-1$

			/* Scan Directory can be enetered manually? */
			tDir.setEditable(true);
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
					chooser.setMultiSelectionEnabled(true);
					chooser.setShowingCreateDirectory(false);

					int choice = chooser.showOpenDialog((Component) e
							.getSource());
					if (choice == JDirectoryChooser.APPROVE_OPTION) {
						StringBuffer filesPaths = new StringBuffer("");
						for (File f:chooser.getSelectedFiles())
							filesPaths.append(f.getAbsolutePath() + ";");
						tDir.setText(filesPaths.toString());
					}
				}
			});
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
					//Response resp = null;

					if (!setConfig())
						return;					
					
					proceed();
				}
			});
		}
		return bIni;
	}
	
	private boolean setConfig() {
		// If no directories selected, show error and return
		String dirs = tDir.getText().trim();
		if (dirs.equals("")) {
			JOptionPane
			.showMessageDialog(null,
					"Please select at least one directory", Messages.getString("Front.0"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
			// Get Files
			configuration.put("scanDir", dirs); //$NON-NLS-1$
		
		// Get cTicket
		if (getCTicket().isSelected())
			configuration.put("cTicket", "true");
		else
			configuration.put("cTicket", "false");
		
		// Get Ticket
		if (getCTicket().isSelected())
			configuration.put("ticket", getTTicket().getText());
		else
			configuration.put("ticket", "");
		
		// Get if recursive is set
		if (cRecursive.isSelected())
			configuration.put("recursive", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		else
			configuration.put("recursive", "false"); //$NON-NLS-1$ //$NON-NLS-2$

		Client.fMan.saveActualParams(configuration);
		return true;
	}
	
	private void proceed() {
		// Abro el progress bar
		try {
			pBar = new ProgressBar(resume);
			//resp = pBar.getResp();
		} catch (Exception ex) {
			logger.error("An unexpected error happened: "
					+ ex.getMessage());
		}
	}

	private void addSysTrayIcon() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image image = getToolkit().getImage(
					"src/main/resources/images/thumbs_up.gif"); //$NON-NLS-1$

			MouseListener mouseListener = new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1
							&& e.getClickCount() == 2) {
						setVisible(true);
						if (inProgress)
							pBar.setVisible(true);
					}
				}

				public void mouseEntered(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseReleased(MouseEvent e) {
				}
			};

			PopupMenu popup = new ClientPopupMenu();

			trayIcon = new TrayIcon(image, "fbClient", popup); //$NON-NLS-1$
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(mouseListener);

			// ActionListener actionListener = new ActionListener() {
			// public void actionPerformed(ActionEvent e) {
			// trayIcon.displayMessage("Action Event",
			// "An Action Event Has Been Performed!",
			// TrayIcon.MessageType.INFO);
			// }
			// };
			// trayIcon.addActionListener(actionListener);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				logger.error("TrayIcon could not be added."); //$NON-NLS-1$
			}

		} else {
			// System Tray is not supported
			logger.error("Warning: System Tray Not Supported"); //$NON-NLS-1$
		}
	}
	
	public static Map<String, String> getConfiguration() {
		return configuration;
	}
}
