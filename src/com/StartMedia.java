package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.GUI.MainDisplay;
import com.GUI.PlayList;
import com.StopWatch.StopWatch;
import com.msgbox.MessageBox;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class StartMedia {
	
	public final static int NOTSTARTED = 0;
	public final static int PLAYING = 1;
	public final static int PAUSED = 2;
	public final static int FINISHED = 3;
	private static int min = 0;
	private static int sec = 0;
	public static Player player;
	
	private final static Object playerLock = new Object();
	public static int playerStatus = NOTSTARTED;
	public StartMedia(final File file) {
		FileInputStream input;
		try {
			input = new FileInputStream(file);
			player = new Player(input);
		} catch (NullPointerException err) {
			return;
		} catch (FileNotFoundException | JavaLayerException e) {
			MessageBox.ErrorMessage("파일 존재하지 않음", "해당 파일이 존재하지 않습니다.");
			return;
		}
	}
	
	public StartMedia(final File file, final AudioDevice audioDevice) {
		FileInputStream input;
		try {
			input = new FileInputStream(file);
			player = new Player(input, audioDevice);
		} catch (FileNotFoundException | JavaLayerException e) {
			MessageBox.ErrorMessage("파일 존재하지 않음", "해당 파일이 존재하지 않습니다.");
			return;
		}
	}
	
	public static void play() {
		synchronized (playerLock) {
			switch (playerStatus) {
			case FINISHED:
			case NOTSTARTED:
				final Runnable r =  new Runnable() {
					@Override
					public void run() {
						playInternal();	
					}
				};
				
				final Thread t = new Thread(r);
				
				t.setDaemon(true);
				t.setPriority(Thread.MAX_PRIORITY);
				t.start();
				playerStatus = PLAYING;
				break;
				
			case PAUSED:
				resume();
				break;
				
			default:
				break;
			}
		}
	}
	
	public static boolean pause() {
		synchronized (playerLock) {
			if(playerStatus == PLAYING) {
				playerStatus = PAUSED;	
			}
			return playerStatus == PAUSED;
		}
	}
	
	public static boolean resume() {
		synchronized (playerLock) {
			if(playerStatus == PAUSED) {
				playerStatus = PLAYING;	
				playerLock.notifyAll();
			}
			return playerStatus == PLAYING;
		}
	}
	
	public static void stop() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
			playerLock.notifyAll();
		}
	}
	
	private static void playInternal() {
		System.out.println(player.getPosition());
		while(playerStatus != FINISHED) {
			try {
				if(!player.play(1)) {
					nextTrack();
				}
			} catch (NullPointerException err) {
				return;
			} catch (JavaLayerException e1) {
				break;
			}
			synchronized (playerLock) {
				while(playerStatus == PAUSED) {
					try {
						playerLock.wait();
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		}
	}
	
	public static void close() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
		}
		player.close();
	}
		
	public static void nextTrack() {
		if(MainDisplay.반복재생여부 == false) {
			restartTrack();
			return;
		}
		if(MainDisplay.medialist.isEmpty()) {
			return;
		}
		
		for(int i = 0; i < MainDisplay.MaxMediaStack; i++) {
				if(MainDisplay.medialist.get(i).toString().contains(MediaFile.getFile().getName())) {
					PlayList.jplaylist.setSelectedIndex(i + 1);
					break;
				}
		}
		
		if(MainDisplay.medialist.lastElement().contains(MediaFile.getFile().getName())) {
			PlayList.jplaylist.setSelectedIndex(0);
		}
		
		String str = PlayList.jplaylist.getSelectedValue().toString();
		String url = MainDisplay.playlist.searchPath(str);
		File file = new File(url);
		MediaFile.setFile(file);
		MainDisplay.playMedia();
	}
	
	public static void restartTrack() {
		MainDisplay.playMedia();
	}
	
	public static void restoreTrack() {
		if(MainDisplay.반복재생여부 == false) {
			restartTrack();
			return;
		}
		
		if(MainDisplay.medialist.isEmpty()) {
			return;
		}
		
		for(int i = 0; i < MainDisplay.MaxMediaStack; i++) {
			if(MainDisplay.medialist.get(i).toString().contains(MediaFile.getFile().getName())) {
				PlayList.jplaylist.setSelectedIndex(i - 1);
				break;
			}
		}
		
		if(PlayList.jplaylist.getSelectedIndex() == PlayList.jplaylist.getFirstVisibleIndex()) {
			restartTrack();
			return;
		}
		
		String str = PlayList.jplaylist.getSelectedValue().toString();
		String url = MainDisplay.playlist.searchPath(str);
		File file = new File(url);
		MediaFile.setFile(file);
		MainDisplay.playMedia();	
	}
}
