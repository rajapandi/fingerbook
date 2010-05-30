package com.fingerbook.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import com.fingerbook.client.Client;
import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FingerbookClient;
import com.fingerbook.models.Fingerbook;


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
