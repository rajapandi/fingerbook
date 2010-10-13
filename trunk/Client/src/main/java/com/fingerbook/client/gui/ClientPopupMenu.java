package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ClientPopupMenu extends PopupMenu {
	private static final long serialVersionUID = -4792167419434448410L;

	public ClientPopupMenu() {
		/* About */
		MenuItem aboutItem = new MenuItem(Messages.getString("MainMenu.6"));	//$NON-NLS-1$
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
				.showMessageDialog(
						(Component) e.getSource(),
						Messages.getString("MainMenu.7") 				//$NON-NLS-1$
						+ Messages.getString("MainMenu.8"), 	//$NON-NLS-1$
						Messages.getString("MainMenu.9"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			}
		});

		MenuItem queryItem = new MenuItem(Messages.getString("MainMenu.2"));	//$NON-NLS-1$
		queryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Query();
			}
		});
		this.add(queryItem);


		MenuItem defaultItem = new MenuItem(Messages.getString("MainMenu.3"));	//$NON-NLS-1$
		defaultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						Messages.getString("MainMenu.4"), 						//$NON-NLS-1$
						Messages.getString("MainMenu.5"), JOptionPane.OK_CANCEL_OPTION) == 0) //$NON-NLS-1$
					System.exit(0);
			}
		});
		this.add(defaultItem);
	}
}
