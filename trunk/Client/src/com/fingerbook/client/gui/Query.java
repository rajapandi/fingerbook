package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;

public class Query extends JFrame {

	private static final long serialVersionUID = 2440070097533761705L;
	private Toolkit toolkit;

	public Query() {

		Container contentPane = this.getContentPane();

		this.setTitle("fbClient - Query");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		
		contentPane.add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JTextField tFile = new JTextField();
		tFile.setColumns(50);
		tFile.setEditable(false);

		JButton bBrowse = new JButton("Browse");
		bBrowse.addActionListener(new JFilePopUp());

		JButton bIni = new JButton("OK");
		bIni.addActionListener(new InitQuery());

		panel.add(tFile);
		panel.add(bBrowse, BorderLayout.SOUTH);

		contentPane.add(panel);
		contentPane.add(bIni, BorderLayout.SOUTH);
		
		
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