package com.fingerbook.client.gui.helpers;

import java.awt.*;
import javax.swing.*;

public class Gap extends JComponent {
	private static final long serialVersionUID = 8848413412780889939L;

	public Gap() {
		Dimension min = new Dimension(0, 0);
		Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		setMinimumSize(min);
		setPreferredSize(min);
		setMaximumSize(max);
	}

	public Gap(int size) {
		Dimension dim = new Dimension(size, size);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
}