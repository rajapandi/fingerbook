package com.fingerbook.client.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fingerbook.client.Client;
import com.fingerbook.models.Response;

public class Task extends SwingWorker<Void, Void> {
	private Response resp = null;

	Logger logger = LoggerFactory.getLogger(Client.class);

	/*
	 * Main task. Executed in background thread.
	 */
	@Override
	public Void doInBackground() throws Exception {
		Front.inProgress = true;
		// Initialize progress property.
		setProgress(0);

		resp = Client.getScanner().scanDirectory(Front.getConfiguration());

		setProgress(1);
		Front.inProgress = false;
		
		if (resp == null || resp.getErrorCode() != null)
			JOptionPane.showMessageDialog(
					Client.front, Messages
							.getString("Front.21")); //$NON-NLS-1$
		else if (resp != null
				&& !resp.getTicket().equals(Front.getConfiguration().get("ticket")))
			new Ticket(resp.getTicket());
		else
			JOptionPane.showMessageDialog(
					Client.front, Messages
							.getString("Front.22") + ":\n" //$NON-NLS-1$ //$NON-NLS-2$
							+ resp.getDesc());
		return null;
	}

	public Response getResp() {
		return resp;
	}
}
