package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;

public class Front extends JFrame {

	private static final long serialVersionUID = 2440070097533761701L;
	private Toolkit toolkit;
	
	public static JTextField tDir;

	public Front() {
		
		Container contentPane = this.getContentPane();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			LookAndFeelAddons.setAddon(LookAndFeelAddons
					.getBestMatchAddonClassName());
		} catch (Exception e) {}
		
		this.setTitle("fbClient");
		this.setLayout(new BorderLayout());
	
		JPanel panel = new JPanel();
		
		
		JMenuBar menuBar = new JMenuBar();
	    this.setJMenuBar(menuBar);
	    JMenu menu = new JMenu("File");
	    menuBar.add(menu);
	    
	    JMenuItem objJMenuItem;



	    objJMenuItem = new JMenuItem("New");        //New
	    objJMenuItem.addActionListener( new Pepe() );
	    menu.add( objJMenuItem );
	  
	    objJMenuItem = new JMenuItem("Open");    //Open...
	    objJMenuItem.addActionListener( new Pepe() );
	    menu.add( objJMenuItem );
	  
	    objJMenuItem = new JMenuItem("Save");       //Save
	    objJMenuItem.addActionListener( new Pepe() );
	    menu.add( objJMenuItem );
	  
	    objJMenuItem = new JMenuItem("Save As"); //Save As...
	    objJMenuItem.addActionListener( new Pepe() );
	    menu.add( objJMenuItem );
	  
	    menu.addSeparator();           //add a horizontal separator line
	  
	    objJMenuItem = new JMenuItem("Quit");       //Quit
	    objJMenuItem.addActionListener( new Pepe() );
	    menu.add( objJMenuItem );



	    
	    
	    
		
		
		
		tDir = new JTextField();
		tDir.setColumns(50);
		tDir.setEditable(false);

		JButton bBrowse = new JButton("Browse");
		bBrowse.addActionListener(new JDirPopUp());
		
		JButton bIni = new JButton("Iniciar");
		bIni.addActionListener(new InitScan());
		
		panel.add(tDir);
		panel.add(bBrowse, BorderLayout.SOUTH);
		
		contentPane.add(panel);
		contentPane.add(bIni, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.center();
		this.setVisible(true);
		
		this.setDefaultDir(new File(".").getAbsolutePath().toString());
	}

	private void center() {
		toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);
	}
	
	private void setDefaultDir(String defaultDir) {
		if (defaultDir.endsWith("\\."))
			Front.tDir.setText(defaultDir.substring(0, defaultDir.length()-2));
	}
}