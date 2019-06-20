package com.GUI;

import javax.swing.JFrame;
import javax.swing.JList;

import com.MediaFile;
import com.SetGUILayout;
import com.StartMedia;
import net.iharder.dnd.FileDrop;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;

public class PlayList {

	public static JFrame frame = new JFrame("\uC7AC\uC0DD\uBAA9\uB85D");
    public static JList<String> jplaylist = new JList<String>();
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	public static void open() {		
		new SetGUILayout();
		frame = new JFrame("\uC7AC\uC0DD\uBAA9\uB85D");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PlayList.class.getResource("/MP3.png")));
		frame.setBounds(100, 100, 350, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		jplaylist = new JList<String>();
		jplaylist.setListData(MainDisplay.medialist);
		
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setViewportView(jplaylist);
		new FileDrop(jplaylist, new FileDrop.Listener() {
			public void filesDropped( java.io.File[] files ) {
				for(int i = 0; i < files.length; i++) {
					if(files[i].getName().toLowerCase().endsWith(".mp3")) {
						MainDisplay.playlist.addPlayListInfo(files[i].getName(), files[i].getPath());
						MainDisplay.medialist.addElement(files[i].getName());
					}
				}
				jplaylist.setListData(MainDisplay.medialist);
			}
	      });
		
		jplaylist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouse) {
				if(MainDisplay.medialist.size() == 0) 
					return;
				
				if(mouse.getClickCount() == 2) {
					String select = jplaylist.getSelectedValue().toString();
					String FileUrl = MainDisplay.playlist.searchPath(select);
					System.out.println(FileUrl);
					File file = new File(FileUrl);
					MediaFile.setFile(file);
					MainDisplay.playMedia();
				}
			}
		});
		jplaylist.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == arg0.VK_NUMPAD8) {
					try{
						int getSelected = jplaylist.getSelectedIndex();
						String getSelectedString = jplaylist.getSelectedValue();
						jplaylist.setSelectedIndex(getSelected - 1);
						int otherSelected = jplaylist.getSelectedIndex();
						String otherSelectedString = jplaylist.getSelectedValue();
						MainDisplay.medialist.set(getSelected - 1, getSelectedString);
						MainDisplay.medialist.set(otherSelected + 1, otherSelectedString);
						jplaylist.setListData(MainDisplay.medialist);
						jplaylist.setSelectedIndex(getSelected - 1);
					} catch(ArrayIndexOutOfBoundsException e) {
						System.out.println("첫 항목");
					}
				}
				if(arg0.getKeyCode() == arg0.VK_NUMPAD2) {
					try{
						int getSelected = jplaylist.getSelectedIndex();
						String getSelectedString = jplaylist.getSelectedValue();
						jplaylist.setSelectedIndex(getSelected + 1);
						int otherSelected = jplaylist.getSelectedIndex();
						String otherSelectedString = jplaylist.getSelectedValue();
						MainDisplay.medialist.set(getSelected + 1, getSelectedString);
						MainDisplay.medialist.set(otherSelected - 1, otherSelectedString);
						jplaylist.setListData(MainDisplay.medialist);
						jplaylist.setSelectedIndex(getSelected + 1);
					} catch(ArrayIndexOutOfBoundsException e) {
						System.out.println("마지막 항목");
					}
				}
				if(arg0.getKeyCode() == arg0.VK_DELETE) {
					try{
						int getSelected = jplaylist.getSelectedIndex();
						MainDisplay.medialist.remove(getSelected);
						jplaylist.setListData(MainDisplay.medialist);
						if(MainDisplay.medialist.isEmpty()) {
							StartMedia.close();
							MainDisplay.노래제목.setText("Media Player 1.0");
							MainDisplay.아티스트앨범.setText(null);
							return;
						}
						jplaylist.setSelectedIndex(getSelected);
						String str = jplaylist.getSelectedValue();
						String url = MainDisplay.playlist.searchPath(str);
						File file = new File(url);
						if(file.getName().contains(MediaFile.getFile().getName())) {
							return;
						}
						MediaFile.setFile(file);
						MainDisplay.playMedia();
					} catch(ArrayIndexOutOfBoundsException e) {
						System.out.println("항목이 선택되지 않음");
					}
				}
				if(arg0.getKeyCode() == arg0.VK_ENTER) {
					String select = jplaylist.getSelectedValue().toString();
					String FileUrl = MainDisplay.playlist.searchPath(select);
					System.out.println(FileUrl);
					File file = new File(FileUrl);
					MediaFile.setFile(file);
					MainDisplay.playMedia();
				}
			}
		});
		frame.getContentPane().add(scrollPane);
		frame.setVisible(true);
	}

}
