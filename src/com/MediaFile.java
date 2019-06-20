package com;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.GUI.MainDisplay;
import com.GUI.PlayList;
import com.msgbox.MessageBox;

public class MediaFile {
	
	public static File media = null;
	
	public static File getFile() {
		return media;
	}
	
	public static boolean isFile(File file) {
		if(file == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isFileExist() {
		if(media == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void setFile(File file) {
		if(isFile(file)) {
			media = file;
		} else {
			MessageBox.ErrorMessage("파일 존재하지 않음", "해당 파일이 존재하지 않습니다.");
			return;
		}
	}
	
	public static boolean OpenFileDialog() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("파일 열기");
		filechooser.setVisible(true);
		filechooser.setFileFilter(new FileNameExtensionFilter("가능한 파일", "mp3"));
		filechooser.setMultiSelectionEnabled(true);
				
		if(filechooser.showOpenDialog(MainDisplay.frmMediaPlayer) == JFileChooser.APPROVE_OPTION) {
			if(filechooser.getSelectedFiles().length == 0) {
				return false;
			}
			PlayList.jplaylist = new JList<String>(); 
			setFile(filechooser.getSelectedFile());
			for(File file : filechooser.getSelectedFiles()) {
				if(MainDisplay.medialist.contains(file.getName())) {
					continue;
				}
				MainDisplay.playlist.addPlayListInfo(file.getName(), file.getPath());
				MainDisplay.medialist.addElement(file.getName());
			}
			try {
				PlayList.jplaylist.setListData(MainDisplay.medialist);
				SwingUtilities.updateComponentTreeUI(PlayList.frame);
				return true;
			} catch(NullPointerException err) {
				return false;
			}
		}
		return false;
	}
		
}
