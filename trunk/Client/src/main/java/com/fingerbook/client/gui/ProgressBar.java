package com.fingerbook.client.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.fingerbook.client.Client;
import com.fingerbook.client.gui.helpers.GBHelper;
import com.fingerbook.client.gui.helpers.Gap;
import com.fingerbook.models.Response;

public class ProgressBar extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = -5888862598021877320L;
	
	private Task task;

	private static final int BORDER = 12; // Window border in pixels.
	private static final int GAP = 5; // Default gap btwn components

	private JPanel jContentPane = null;

	private JProgressBar pBar = null;
	private JButton bCancel = null;

	public ProgressBar(boolean resume) {
		// Center window
		setLocationRelativeTo(null);
		
		this.setTitle("fbClient - Please Wait..."); //$NON-NLS-1$ //$NON-NLS-2$

		this.setModal(true);
		
		// Center window
		this.setLocationRelativeTo(null);

		this.setContentPane(getJContentPane());
		
        //Instances of javax.swing.SwingWorker are not reusable, so
        //we create new instances as needed.
        task = new Task(resume);
        task.addPropertyChangeListener(this);
        task.execute();

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				Client.front.setVisible(false);
				setVisible(false);
				if (!Front.minimizedNotice) {
					Front.trayIcon.displayMessage(Messages.getString("Front.10"), //$NON-NLS-1$
							Messages.getString("Front.11"), //$NON-NLS-1$
							TrayIcon.MessageType.INFO);
					Front.minimizedNotice = true;
				}
			}
		});

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
			jContentPane.add(getPBar(), pos.width(2));
			
			// ... Next Row
			jContentPane.add(new Gap(GAP), pos.nextRow().width());

			// ... Next Row
			jContentPane.add(getBCancel(), pos.nextRow().expandW());

			// ... Next Row
			jContentPane.add(new Gap(), pos.nextRow().width().expandH());
		}
		return jContentPane;
	}

	private Component getPBar() {
		if (pBar == null) {
			pBar = new JProgressBar();

			pBar = new JProgressBar();
			pBar.setStringPainted(true);
			pBar.setString(Messages.getString("ProgressBar.0")); //$NON-NLS-1$
			
	        pBar.setIndeterminate(true);
		}
		return pBar;
	}

	/**
	 * This method initializes bCancel JButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBCancel() {
		if (bCancel == null) {
			bCancel = new JButton(Messages.getString("ProgressBar.1")); //$NON-NLS-1$
			bCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// interrupt scanner threads
					Client.getScanner().stopScanning();
					Task.canceled = true;
					dispose();
				}
			});
		}
		return bCancel;
	}
	
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) { //$NON-NLS-1$
            dispose();
        }
    }
    
    public Response getResp() {
    	return task.getResp();
    }
}
