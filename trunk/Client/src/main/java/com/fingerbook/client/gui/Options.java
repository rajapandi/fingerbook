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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.fingerbook.client.gui.helpers.GBHelper;
import com.fingerbook.client.gui.helpers.Gap;

public class Options extends JDialog{
	private static final long serialVersionUID = 997L;

	/* GUI DEFINES */
	private static final int BORDER = 12; // Window border in pixels.
	private static final int GAP = 5; // Default gap btwn components

	/* Swing Components */
	private JPanel jContentPane = null;
	
	private JLabel lTags = null;
	private JLabel lComments = null;

	private JScrollPane sComments = null;
	private JTextField tTags = null;
	private JTextArea tComments = null;
	private JCheckBox cTray = null;
	private JButton bOk = null;
	private JButton bCancel = null;

	public Options() {
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
		//this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.pack();

		// Set Icon
		setIconImage(getToolkit().getImage(
		"src/main/resources/images/thumb.png")); //$NON-NLS-1$

		this.setModal(true);
		
		// Center window
		setLocationRelativeTo(null);
	}
	
	private void populate() {
		if(Front.getConfiguration().get("tags") != null) //$NON-NLS-1$
			getTTags().setText(Front.getConfiguration().get("tags"));		 //$NON-NLS-1$
		
		if(Front.getConfiguration().get("comment") != null) //$NON-NLS-1$
			getTComments().setText(Front.getConfiguration().get("comment"));		 //$NON-NLS-1$
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
			jContentPane.add(getLTags(), pos.width(2));
			// ... Next Row
			jContentPane.add(getTTags(), pos.nextRow().width(2));
			
			// ... Next Row
			/* GAP */
			jContentPane.add(new Gap(GAP * 2), pos.nextRow().width(2));
			
			// ... Next Row
			jContentPane.add(getLComments(), pos.nextRow().width(2));
			// ... Next Row
			jContentPane.add(getSComments(), pos.nextRow().width(2));
			
			// ... Next Row
			/* SendToTray CheckBox */
			//getCTray().setHorizontalAlignment(JCheckBox.RIGHT);
			jContentPane.add(getCTray(), pos.nextRow().width(2));

			// ... Next Row
			/* GAP */
			jContentPane.add(new Gap(GAP * 2), pos.nextRow().width(2));
			
			// ... Next Row
			jContentPane.add(getBOk(), pos.nextRow().expandW());
			jContentPane.add(getBCancel(), pos.nextCol().expandW());

		}
		return jContentPane;
	}

	/**
	 * This method initializes lTags JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLTags() {
		if (lTags == null)
			lTags = new JLabel("Tags"); //$NON-NLS-1$
			lTags.setToolTipText(Messages.getString("Options.4")); //$NON-NLS-1$

		return lTags;
	}
	
	/**
	 * This method initializes lComments JLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLComments() {
		if (lComments == null)
			lComments = new JLabel("Comments"); //$NON-NLS-1$

		return lComments;
	}
	
	/**
	 * This method initializes sComments JScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getSComments() {
		if (sComments == null)
			sComments = new JScrollPane(getTComments());

		return sComments;
	}
	
	/**
	 * This method initializes tComments JTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getTComments() {
		if (tComments == null) {
			tComments = new JTextArea(5, 20);
			tComments.setLineWrap(true);
		}

		return tComments;
	}
	
	/**
	 * This method initializes tTags JTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTTags() {
		if (tTags == null) {
			tTags = new JTextField(20);
			tTags.setToolTipText(Messages.getString("Options.4")); //$NON-NLS-1$

		}
		return tTags;
	}
	
	/**
	 * This method initializes cTray JCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCTray() {
		if (cTray == null) {
			cTray = new JCheckBox(Messages.getString("Front.23")); //$NON-NLS-1$
			/* Initially unselected */
			cTray.setSelected(false);
		}
		return cTray;
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
					Front.getConfiguration().put("tags", tags.toString().substring(1, tags.toString().length()-1)); //$NON-NLS-1$
					Front.getConfiguration().put("comment", tComments.getText()); //$NON-NLS-1$
					if (getCTray().isSelected())
						Front.getConfiguration().put("tray","true"); //$NON-NLS-1$ //$NON-NLS-2$
					else
						Front.getConfiguration().put("tray","false"); //$NON-NLS-1$ //$NON-NLS-2$
					dispose();
				}

				private List<String> getTags() {
					String tags = tTags.getText().trim();

					/* Build List from String */
					List<String> tagList = new ArrayList<String>();
					java.util.Scanner scan = new java.util.Scanner(tags);
					scan.useDelimiter(","); //$NON-NLS-1$
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