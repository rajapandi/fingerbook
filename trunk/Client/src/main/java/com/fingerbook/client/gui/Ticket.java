package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fingerbook.client.gui.helpers.GBHelper;
import com.fingerbook.client.gui.helpers.Gap;
import com.l2fprod.common.swing.JDirectoryChooser;

public class Ticket extends JDialog {
	private static final long serialVersionUID = 7852126552694250618L;

	private static final int BORDER = 12; // Window border in pixels.
	private static final int GAP = 5; // Default gap btwn components

	private String ticket = ""; //$NON-NLS-1$

	private JPanel jContentPane = null;

	private JLabel lMessage = null;
	private JLabel lTicket = null;
	private JTextField tTicket = null;
	private JButton bSave = null;
	private JButton bOK = null;

	public Ticket(String ticket) {
		this.ticket = ticket;

		this.setTitle("fbClient"); //$NON-NLS-1$ //$NON-NLS-2$

		// Center window
		this.setLocationRelativeTo(null);

		this.setContentPane(getJContentPane());

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.pack();
		this.setVisible(true);
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
			jContentPane.add(getLMessage(), pos.width(2));

			// ... Next Row
			jContentPane.add(getLTicket(), pos.nextRow().width(2));

			// ... Next Row
			jContentPane.add(getTTicket(), pos.nextRow().expandW());
			jContentPane.add(getBSave(), pos.nextCol());

			// ... Next Row
			jContentPane.add(new Gap(GAP * 2), pos.nextRow().width().expandH());

			// ... Next Row
			jContentPane.add(getBOK(), pos.nextRow().width(2).expandW());

			// ... Next Row
			jContentPane.add(new Gap(), pos.nextRow().width().expandH());
		}
		return jContentPane;
	}

	/**
	 * This method initializes lMessage JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLMessage() {
		if (lMessage == null) {
			lMessage = new JLabel(Messages.getString("Ticket.0")); //$NON-NLS-1$
		}
		return lMessage;
	}

	/**
	 * This method initializes lTicket JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private Component getLTicket() {
		if (lTicket == null) {
			if (!ticket.equals("")) //$NON-NLS-1$
				lTicket = new JLabel(Messages.getString("Ticket.3")); //$NON-NLS-1$
		}
		return lTicket;
	}

	/**
	 * This method initializes lMessage JTextField
	 * 
	 * @return javax.swing.JtextField
	 */
	private JTextField getTTicket() {
		if (tTicket == null) {
			tTicket = new JTextField(ticket);
			tTicket.setEditable(false);
		}
		return tTicket;
	}

	/**
	 * This method initializes bOK JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBSave() {
		if (bSave == null) {
			bSave = new JButton(Messages.getString("Ticket.1")); //$NON-NLS-1$
			bSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDirectoryChooser chooser = new JDirectoryChooser("."); //$NON-NLS-1$
					chooser.setShowingCreateDirectory(false);
					int choice = chooser.showOpenDialog((Component) e
							.getSource());
					if (choice == JDirectoryChooser.APPROVE_OPTION) {
						try {
							Long currentTimeInSeconds = System
									.currentTimeMillis() / 1000;

							// Create file if it does not exist
							BufferedWriter file = new BufferedWriter(
									new FileWriter((chooser.getSelectedFile()
											.getAbsolutePath()
											+ "/" + currentTimeInSeconds.toString()))); //$NON-NLS-1$
							file.write(getTTicket().getText());
							file.close();
							JOptionPane.showMessageDialog(
									(Component) e.getSource(), Messages.getString("Ticket.6") + currentTimeInSeconds.toString()); //$NON-NLS-1$
						} catch (IOException ex) {
						}

					}
				}
			});
		}
		return bSave;
	}

	/**
	 * This method initializes bOK JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBOK() {
		if (bOK == null) {
			bOK = new JButton(Messages.getString("Ticket.2")); //$NON-NLS-1$
			bOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return bOK;
	}
}