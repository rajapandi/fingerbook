package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotePad extends JFrame {

	private static final long serialVersionUID = 2440070097533761705L;
	
	public NotePad(String text) {

		Container contentPane = this.getContentPane();

		this.setTitle("fbClient - " + Messages.getString("NotePad.1")); //$NON-NLS-1$ //$NON-NLS-2$
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		
		contentPane.add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JTextArea notepad = new JTextArea(20,80);
		notepad.setText(text);
		JScrollPane scrollingArea = new JScrollPane(notepad);

		panel.add(scrollingArea, BorderLayout.CENTER);


		contentPane.add(panel);
	
		
		this.pack();
		
		// Center window
		setLocationRelativeTo(null);
		
		this.setVisible(true);
	}
}