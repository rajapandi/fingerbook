package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.fingerbook.client.Scanner;
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

class InitScan implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			new Scanner(Front.tDir.getText());
		} catch (Exception ex) {}

		JOptionPane.showMessageDialog((Component) e.getSource(), "Success!");

	}
}

// Menu ActionListeners
class Pepe implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.out.println("pepe");
	}
}
