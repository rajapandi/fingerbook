package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.fingerbook.client.Client;
import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FingerbookClient;
import com.fingerbook.models.Fingerbook;

public class Query extends JDialog {
	private static final long serialVersionUID = 2440070097533761705L;
	private Toolkit toolkit;

	private JTextField tFile = new JTextField();
	private JButton bIni;
	
	public Query() {

		Container contentPane = this.getContentPane();

		this.setTitle("fbClient - Query");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		
		contentPane.add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		tFile.setColumns(50);
		tFile.setEditable(false);

		JButton bBrowse = new JButton("Browse");
		bBrowse.addActionListener(new JFilePopUp());

		bIni = new JButton("OK");
		bIni.setEnabled(false);
		bIni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File f = new File(((Query)(SwingUtilities.getRoot((JButton)e.getSource()))).getFDir());
//					FingerbookClient fiClient = Client.applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
					FingerbookClient fiClient = Client.applicationContext.getBean("fingerbookClient", FingerbookClient.class);
					FileHashCalculator fhc = Client.applicationContext.getBean("fileHashCalculator", FileHashCalculator.class);
					List<Fingerbook> list = fiClient.getGroups(fhc.getFileHash(f));
					
					
					new NotePad(list.toString());
				} catch (Exception ex) {ex.printStackTrace();}
				dispose();
			}});

		panel.add(tFile);
		panel.add(bBrowse, BorderLayout.SOUTH);

		contentPane.add(panel);
		contentPane.add(bIni, BorderLayout.SOUTH);
		
		this.setModal(true);
		this.pack();
		this.center();
		this.setVisible(true);


	}

	private void center() {
		toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);
	}

	public void setOKEnabled() {
		this.bIni.setEnabled(true);		
	}

	public void setFDir(String path) {
		this.tFile.setText(path);
	}
	
	public String getFDir() {
		return this.tFile.getText();
	}
}