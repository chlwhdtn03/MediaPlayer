package com.msgbox;

import javax.swing.JOptionPane;

import com.GUI.MainDisplay;

public class MessageBox {
	
	public static void InfoMessage(String title, String subtitle) {
		JOptionPane.showConfirmDialog(MainDisplay.frmMediaPlayer, subtitle, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void ErrorMessage(String title, String subtitle) {
		JOptionPane.showConfirmDialog(MainDisplay.frmMediaPlayer, subtitle, title, JOptionPane.ERROR_MESSAGE);
	}

}
