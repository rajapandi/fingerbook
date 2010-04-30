package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.fingerbook.client.Client;
import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FingerbookClient;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
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
		Response resp = null;
		try {
			resp = Client.getScanner().scanDirectory(((Front)(SwingUtilities.getRoot((JButton)e.getSource()))).getDir());
		} catch (Exception ex) { ex.printStackTrace(); }

		if(resp == null || resp.getErrorCode() != null) {
			JOptionPane.showMessageDialog((Component) e.getSource(), "Error!:\n" + resp !=  null ? resp.getDesc() : "");
		} else {
			JOptionPane.showMessageDialog((Component) e.getSource(), "Success!:\n" + resp.getDesc());
		}
		
	}
}

class InitQuery implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		try {
			File f = new File(((Query)(SwingUtilities.getRoot((JButton)e.getSource()))).getFDir());
			FingerbookClient fiClient = Client.applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
			FileHashCalculator fhc = Client.applicationContext.getBean("fileHashCalculator", FileHashCalculator.class);
			List<Fingerbook> list = fiClient.getGroups(fhc.getFileHash(f));
			
			
			new NotePad(list.toString());
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