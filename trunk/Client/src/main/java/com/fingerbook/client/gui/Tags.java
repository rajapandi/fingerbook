package com.fingerbook.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fingerbook.client.Client;
import com.fingerbook.client.gui.helpers.GBHelper;

public class Tags extends JDialog{
	private static final long serialVersionUID = 997L;

	/* GUI DEFINES */
	private static final int BORDER = 12; // Window border in pixels.
	private static final int GAP = 5; // Default gap btwn components

	/* Swing Components */
	private JPanel jContentPane = null;

	private JTextField tTags = null;
	private JButton bOk = null;
	private JButton bCancel = null;

	public Tags() {
		/* Set initial stuff */
		initialize();
		populate();
		this.setVisible(true);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setTitle("Tags"); //$NON-NLS-1$

		this.setContentPane(getJContentPane());

		/* Default Close Operation = do nothing */
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.pack();

		// Set Icon
		setIconImage(getToolkit().getImage(
		"src/main/resources/images/thumb.png")); //$NON-NLS-1$

		this.setModal(true);
		
		// Center window
		setLocationRelativeTo(null);
	}
	
	private void populate() {
		if(Front.getConfiguration().get("tags") != null)
			getTTags().setText(Front.getConfiguration().get("tags"));		
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.Container
	 */
	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER));

			// Create GridBag helper object.
			GBHelper pos = new GBHelper();
			
			// ... Next Row
			jContentPane.add(getTTags(), pos.width(2));

			// ... Next Row
			jContentPane.add(getBOk(), pos.nextRow().expandW());
			jContentPane.add(getBCancel(), pos.nextCol().expandW());

		}
		return jContentPane;
	}

	/**
	 * This method initializes tTags JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTTags() {
		if (tTags == null) {
			tTags = new JTextField(20);
		}
		return tTags;
	}
	
	/**
	 * This method initializes bOk JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBOk() {
		if (bOk == null) {
			bOk = new JButton("OK"); //$NON-NLS-1$
			bOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Set<String> tags = new HashSet<String>(getTags());
					Front.getConfiguration().put("tags", tags.toString().substring(1, tags.toString().length()-1));
					dispose();
				}

				private List<String> getTags() {
					String tags = tTags.getText().trim();

					/* Build List from String */
					List<String> tagList = new ArrayList<String>();
					java.util.Scanner scan = new java.util.Scanner(tags);
					scan.useDelimiter(",");
					while (scan.hasNext())
						tagList.add(scan.next().trim());
					return tagList;
				}
			});
		}
		return bOk;
	}

	/**
	 * This method initializes bCancel JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBCancel() {
		if (bCancel == null) {
			bCancel = new JButton("Cancel"); //$NON-NLS-1$
			bCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return bCancel;
	}
}