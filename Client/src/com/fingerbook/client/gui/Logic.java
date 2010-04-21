package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.fingerbook.client.Client;
import com.l2fprod.common.swing.JDirectoryChooser;

class JDirPopUp implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JDirectoryChooser chooser = new JDirectoryChooser(".");
		chooser.setShowingCreateDirectory(false);
		int choice = chooser.showOpenDialog((Component) e.getSource());
		if (choice == JDirectoryChooser.APPROVE_OPTION) {
			Front.tDir.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}

class JFilePopUp implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(null);
	    System.out.println(chooser.getSelectedFile().getAbsolutePath().toString());
//		Front.tDir.setText(chooser.getSelectedFile().getAbsolutePath());
	}
}

class InitScan implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			Client.getScanner().scanDirectory(Front.tDir.getText());
		} catch (Exception ex) {ex.printStackTrace();}

		JOptionPane.showMessageDialog((Component) e.getSource(), "Success!");

	}
}

class InitQuery implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			Client.getScanner().scanDirectory(Front.tDir.getText());
		} catch (Exception ex) {ex.printStackTrace();}

		JOptionPane.showMessageDialog((Component) e.getSource(), "Success!");

	}
}

// Menu ActionListeners
class MAbout implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JOptionPane
				.showMessageDialog(
						(Component) e.getSource(),
						"Fingerbook\nITBA - Proyecto Final\n\n"
								+ "Autores:\nAberg Cobo, Simon\nOybin, Nahuel\nGross, German\nPampliega, Juan Martin",
						"About Us", JOptionPane.INFORMATION_MESSAGE);
	}
}

class MExit implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit the application?", "Exit",
				JOptionPane.OK_CANCEL_OPTION) == 0)
			System.exit(0);
	}
}

class MQuery implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		new Query();		
	}
}