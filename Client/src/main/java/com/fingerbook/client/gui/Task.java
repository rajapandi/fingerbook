package com.fingerbook.client.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fingerbook.client.Client;
import com.fingerbook.models.Response;

public class Task extends SwingWorker<Void, Void> {
	protected static boolean canceled = false;
	private Response resp = null;
	private boolean resume;

	Logger logger = LoggerFactory.getLogger(Client.class);

	public Task(boolean resume) {
		super();
		this.resume = resume;
	}

	/*
	 * Main task. Executed in background thread.
	 */
	@Override
	public Void doInBackground() throws Exception {
		Front.inProgress = true;
		// Initialize progress property.
		setProgress(0);

		try {
			resp = Client.getScanner().scanDirectory(Front.getConfiguration(), resume);			
		} catch (Exception e) {
			logger.error(e.getMessage());
			Client.getScanner().stopScanning();
			resp = null; //TODO: ver si esta bien setear resp en null o habria que hacer otra cosa
		}

		setProgress(1);
		Front.inProgress = false;

		if (!canceled && (resp == null || resp.getErrorCode() != null)) {
			JOptionPane.showMessageDialog(
					Client.front, Messages
					.getString("Front.21") + " " + resp.getDesc()); //$NON-NLS-1$
		}
		else if (resp != null && Front.getConfiguration().get("cLogin").equals(false)
				&& !Client.getScanner().getTicket().equals(Front.getConfiguration().get("ticket"))) {
			new Ticket(Client.getScanner().getTicket());
			//logger.error("Erroneous server implementation: Tickets should be sent at the beginning of the transaction\n");
		}			
		else {
			JOptionPane.showMessageDialog(
					Client.front, Messages
					.getString("Front.22") + ":\n" //$NON-NLS-1$ //$NON-NLS-2$
					+ resp.getDesc());			
		}
		
		canceled = false;
		return null;
	}

	public Response getResp() {
		return resp;
	}
}
