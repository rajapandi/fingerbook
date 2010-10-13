package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 6334807614215718519L;

	private JMenu fileMenu = null;
	private JMenu helpMenu = null;

	private JMenuItem bFileQuery = null;
	private JMenuItem bFileExit = null;

	private JMenuItem bHelpAbout = null;

	public MainMenu() {

		this.add(getJFileMenu());
		this.add(getJHelpMenu());
	}

	/**
	 * This method initializes the File menu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu(Messages.getString("MainMenu.0")); //$NON-NLS-1$

			fileMenu.add(getBFileQuery());

			// add a horizontal separator line
			fileMenu.addSeparator();
			fileMenu.add(getBFileExit());
		}
		return fileMenu;
	}

	/**
	 * This method initializes the Help menu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu(Messages.getString("MainMenu.1")); //$NON-NLS-1$

			helpMenu.add(getBHelpAbout());
		}
		return helpMenu;
	}

	/**
	 * This method initializes bFileQuery JMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getBFileQuery() {
		if (bFileQuery == null) {
			bFileQuery = new JMenuItem(Messages.getString("MainMenu.2")); //$NON-NLS-1$

			bFileQuery.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit
					.getDefaultToolkit().getMenuShortcutKeyMask(), false));
			bFileQuery.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Query();
				}
			});
		}
		return bFileQuery;
	}

	/**
	 * This method initializes bFileExit JMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getBFileExit() {
		if (bFileExit == null) {
			bFileExit = new JMenuItem(Messages.getString("MainMenu.3")); //$NON-NLS-1$

			bFileExit.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit
					.getDefaultToolkit().getMenuShortcutKeyMask(), false));
			bFileExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null,
							Messages.getString("MainMenu.4"), //$NON-NLS-1$
							Messages.getString("MainMenu.5"), JOptionPane.OK_CANCEL_OPTION) == 0) //$NON-NLS-1$
						System.exit(0);
				}
			});
		}
		return bFileExit;
	}

	/**
	 * This method initializes bHelpAbout JMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getBHelpAbout() {
		if (bHelpAbout == null) {
			bHelpAbout = new JMenuItem(Messages.getString("MainMenu.6")); //$NON-NLS-1$

			bHelpAbout.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
					.getDefaultToolkit().getMenuShortcutKeyMask(), false));
			bHelpAbout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									(Component) e.getSource(),
									Messages.getString("MainMenu.7") //$NON-NLS-1$
											+ Messages.getString("MainMenu.8") + ":\nAberg Cobo, Simon\nOybin, Nahuel\nGross, German\nPampliega, Juan Martin", //$NON-NLS-1$
									Messages.getString("MainMenu.9"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				}
			});
		}
		return bHelpAbout;
	}
}
