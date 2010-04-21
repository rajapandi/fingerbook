package com.fingerbook.client.gui;

import java.awt.Toolkit;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 6334807614215718519L;

	public MainMenu() {

		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		this.add(fileMenu);
		this.add(helpMenu);

		JMenuItem objJMenuItem;

		/* File Menu */
		// New
		objJMenuItem = new JMenuItem("Scan & Send");
		objJMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		objJMenuItem.addActionListener(new About());
		fileMenu.add(objJMenuItem);

		// add a horizontal separator line
		fileMenu.addSeparator();

		// Exit
		objJMenuItem = new JMenuItem("Exit");
		objJMenuItem.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		objJMenuItem.addActionListener(new About());
		fileMenu.add(objJMenuItem);
		
		/* Help Menu */
		objJMenuItem = new JMenuItem("About");
		objJMenuItem.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		objJMenuItem.addActionListener(new About());
		helpMenu.add(objJMenuItem);

	}
}
