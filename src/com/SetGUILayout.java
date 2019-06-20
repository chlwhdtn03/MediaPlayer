package com;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SetGUILayout {
	
	public SetGUILayout() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
