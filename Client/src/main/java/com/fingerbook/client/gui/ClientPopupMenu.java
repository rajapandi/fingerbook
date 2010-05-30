package com.fingerbook.client.gui;

import java.awt.MenuItem;
import java.awt.PopupMenu;

public class ClientPopupMenu  extends PopupMenu {

	public ClientPopupMenu() {
			MenuItem aboutItem = new MenuItem("About");
		    aboutItem.addActionListener(new MAbout());
		    this.add(aboutItem);
		    
		    MenuItem queryItem = new MenuItem("Query");
		    queryItem.addActionListener(new MQuery());
		    this.add(queryItem);
		    
		    
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(new MExit());
		    this.add(defaultItem);
	}
}
