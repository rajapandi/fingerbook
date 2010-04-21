package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotePad extends JFrame {

	private static final long serialVersionUID = 2440070097533761705L;
	private Toolkit toolkit;
	
	public NotePad(String text) {

		Container contentPane = this.getContentPane();

		this.setTitle("fbClient - Result");
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
		this.center();
		this.setVisible(true);


	}

	private void center() {
		toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);
	}
}