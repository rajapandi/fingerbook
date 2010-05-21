package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;

public class Front extends JFrame {

	private static final long serialVersionUID = 2440070097533761701L;
	private Toolkit toolkit;

	private JTextField tDir = new JTextField();

	public Front() {

		Container contentPane = this.getContentPane();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			LookAndFeelAddons.setAddon(LookAndFeelAddons
					.getBestMatchAddonClassName());
		} catch (Exception e) {
		}

		this.setTitle("fbClient");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		// MenuBar
		JMenuBar menuBar = new MainMenu();
		this.setJMenuBar(menuBar);

		tDir.setColumns(50);
		tDir.setEditable(false);

		JButton bBrowse = new JButton("Browse");
		bBrowse.addActionListener(new JDirPopUp());

		JButton bIni = new JButton("Scan & Send");
		bIni.addActionListener(new InitScan());

		panel.add(tDir);
		panel.add(bBrowse, BorderLayout.SOUTH);

		contentPane.add(panel);
		contentPane.add(bIni, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.center();
		this.setVisible(true);

		this.setDir(new File(".").getAbsolutePath().toString());
	}

	private void center() {
		toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);
	}

	public void setDir(String defaultDir) {
		if (defaultDir.endsWith("\\."))
			this.tDir.setText(defaultDir.substring(0, defaultDir.length() - 2));
		else
			this.tDir.setText(defaultDir);
	}

	public String getDir() {
		return this.tDir.getText();
	}
}