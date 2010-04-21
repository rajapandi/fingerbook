package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.fingerbook.client.Client;
import com.l2fprod.common.swing.JDirectoryChooser;

class JDirPopUp implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JDirectoryChooser chooser = new JDirectoryChooser(".");
		chooser.setShowingCreateDirectory(false);
		int choice = chooser.showOpenDialog((Component) e.getSource());
		if (choice == JDirectoryChooser.APPROVE_OPTION) {
			((Front)(SwingUtilities.getRoot((JButton)e.getSource()))).setDir(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}

class JFilePopUp implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(null);
		if(chooser.getSelectedFile() != null){
			((Query)(SwingUtilities.getRoot((JButton)e.getSource()))).setFDir(chooser.getSelectedFile().getAbsolutePath());
			((Query)(SwingUtilities.getRoot((JButton)e.getSource()))).setOKEnabled();
		}
	}
}

class InitScan implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			Client.getScanner().scanDirectory(((Front)(SwingUtilities.getRoot((JButton)e.getSource()))).getDir());
		} catch (Exception ex) { ex.printStackTrace(); }

		JOptionPane.showMessageDialog((Component) e.getSource(), "Success!");
	}
}

class InitQuery implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			//ACA VA EL GET
			
			/*-------------------------*/
			
			//ACA VA EL toString DEL OBJETO QUE DEVUELVE EL GET
			new NotePad("hola");
		} catch (Exception ex) {ex.printStackTrace();}
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