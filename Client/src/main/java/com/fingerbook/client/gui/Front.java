package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fingerbook.client.Client;
import com.fingerbook.models.Response;
import com.l2fprod.common.swing.JDirectoryChooser;

public class Front extends JFrame {
	private static final long serialVersionUID = 2440070097533761701L;

	private Map<String, String> configuration = new HashMap<String, String>();

	private JPanel jContentPane = null;

	private JTextField tDir = null;
	private JButton bBrowse = null;
	private JCheckBox cRecursive = null;
	private JButton bIni = null;

	public Front() {
		initialize();

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);

		setVisible(true);
	}

	/**
	 * This method sets some Frames parameters
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(590, 180);
		this.setResizable(false);
		this.setTitle("fbClient");

		// MenuBar
		JMenuBar menuBar = new MainMenu();
		this.setJMenuBar(menuBar);

		this.setContentPane(getJContentPane());

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure you want to quit the application?",
						"Exit", JOptionPane.OK_CANCEL_OPTION) == 0)
					System.exit(0);
			}
		});
	}

	/**
	 * This method removes '/.' from the end of the path string
	 */
	public void setDir(JTextField tDir, String defaultDir) {
		if (defaultDir.endsWith("\\.") || defaultDir.endsWith("/."))
			configuration.put("scanDir", defaultDir.substring(0, defaultDir
					.length() - 2));
		else
			configuration.put("scanDir", defaultDir);
		tDir.setText(configuration.get("scanDir"));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTDir(), null);
			jContentPane.add(getBBrowse(), null);
			jContentPane.add(getCRecursive(), null);
			jContentPane.add(getBIni(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes tDir JTextBox
	 * 
	 * @return javax.swing.JTextBox
	 */
	private JTextField getTDir() {
		if (tDir == null) {
			tDir = new JTextField();

			tDir.setColumns(50);
			tDir.setEditable(false);
			tDir.setBounds(new Rectangle(10, 10, 500, 30));

			setDir(tDir, new File(".").getAbsolutePath().toString());
		}
		return tDir;
	}

	/**
	 * This method initializes bBrowse JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBBrowse() {
		if (bBrowse == null) {
			bBrowse = new JButton("Browse");
			bBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDirectoryChooser chooser = new JDirectoryChooser(".");
					chooser.setShowingCreateDirectory(false);
					int choice = chooser.showOpenDialog((Component) e
							.getSource());
					if (choice == JDirectoryChooser.APPROVE_OPTION) {
						configuration.put("scanDir", chooser.getSelectedFile()
								.getAbsolutePath());
						tDir.setText(chooser.getSelectedFile()
								.getAbsolutePath());
					}
				}
			});
			bBrowse.setBounds(new Rectangle(520, 10, 60, 30));
		}
		return bBrowse;
	}

	/**
	 * This method initializes cRecursive JCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCRecursive() {
		if (cRecursive == null) {
			cRecursive = new JCheckBox("Recursive?");
			cRecursive.setSelected(false);
			configuration.put("recursive", "false");
			cRecursive.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cRecursive.isSelected())
						configuration.put("recursive", "true");
					else
						configuration.put("recursive", "false");
				}
			});
			cRecursive.setBounds(new Rectangle(10, 45, 100, 30));
		}
		return cRecursive;
	}

	/**
	 * This method initializes bIni JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBIni() {
		if (bIni == null) {
			bIni = new JButton("Scan & Send");
			bIni.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Response resp = null;
					try {
						resp = Client.getScanner().scanDirectory(
								configuration.get("scanDir"),
								(HashMap<String, String>) configuration);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					if (resp == null || resp.getErrorCode() != null)
						JOptionPane.showMessageDialog(
								(Component) e.getSource(), "Error!");
					else {
						JOptionPane.showMessageDialog(
								(Component) e.getSource(), "Success!:\n"
										+ resp.getDesc());
					}
				}
			});
			bIni.setBounds(new Rectangle(10, 85, 570, 30));
		}
		return bIni;
	}
}
