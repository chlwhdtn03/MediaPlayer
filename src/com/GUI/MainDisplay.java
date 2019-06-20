package com.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import com.MediaFile;
import com.SetGUILayout;
import com.StartMedia;
import com.PlayList.PlayListAPI;
import com.PlayList.PlayListStorage;
import com.StopWatch.StopWatch;
import com.Volume.Audio;
import com.msgbox.MessageBox;

import net.iharder.dnd.FileDrop;
import javax.swing.JTree;

public class MainDisplay {

	public static JFrame frmMediaPlayer;
	public static boolean blyricstoggle = false;
	public static JButton 중지;
	public static JButton 이전트랙;
	public static JMenuBar menuBar;
	public static JMenu mnFile;
	public static JMenu menu;
	public static JButton 다음트랙;
	public static JButton 재생정지;
	public static JLabel 시간;
	public static JLabel 길이;
	public static JLabel 노래제목;
	public static JLabel 아티스트앨범;
	public static JTextArea 가사;
	public static JScrollPane scrollPane;
	public static JProgressBar 바;
	
	public static int MaxMediaStack;
	public static PlayListStorage playlist;
	
	public static Vector<String> medialist = new Vector<String>();
	
	public static boolean 반복재생여부 = true;
	private static JRadioButton 반복재생버튼;
	private static JSlider 볼륨;
	private static JTextField 볼륨라벨;
	private static JLabel 이미지;
	private static int fontsize = 13;
	private static JSlider 가사크기;
	private static JTextField 가사크기라벨;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {	
				try {
					MaxMediaStack = 300;
					playlist = new PlayListAPI(300);
					initialize();
					frmMediaPlayer.setVisible(true);
					frmMediaPlayer.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void initialize() {
		new SetGUILayout();
		frmMediaPlayer = new JFrame();
		frmMediaPlayer.setIconImage(Toolkit.getDefaultToolkit().getImage(MainDisplay.class.getResource("/MP3.png")));
		frmMediaPlayer.setResizable(false);
		frmMediaPlayer.setBackground(Color.WHITE);
		frmMediaPlayer.getContentPane().setFont(new Font("맑은 고딕", Font.BOLD, 12));
		frmMediaPlayer.setTitle("Media Player");
		frmMediaPlayer.setBounds(100, 100, 450, 200);
		frmMediaPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMediaPlayer.getContentPane().setLayout(null);
		

		scrollPane = new JScrollPane();
		scrollPane.setBorder(new TitledBorder(null, "\uAC00\uC0AC", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		scrollPane.setBounds(0, 172, 444, 300);
		frmMediaPlayer.getContentPane().add(scrollPane);
		
		가사 = new JTextArea();
		가사.setFont(new Font("맑은 고딕", Font.PLAIN, fontsize));
		가사.setEditable(false);
		가사.setBackground(new Color(240, 240, 240));
		가사.setLineWrap(true);
		scrollPane.setViewportView(가사);
		
		아티스트앨범 = new JLabel("");
		아티스트앨범.setFont(new Font("맑은 고딕", Font.PLAIN, fontsize));
		아티스트앨범.setBounds(118, 55, 314, 35);
		아티스트앨범.setHorizontalAlignment(SwingConstants.CENTER);
		frmMediaPlayer.getContentPane().add(아티스트앨범);
		
		노래제목 = new JLabel("Media Player 1.0");
		노래제목.setBounds(152, 31, 245, 23);
		노래제목.setHorizontalAlignment(SwingConstants.CENTER);
		노래제목.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		frmMediaPlayer.getContentPane().add(노래제목);
		
		길이 = new JLabel("0:00");
		길이.setBounds(387, 100, 40, 20);
		길이.setHorizontalAlignment(SwingConstants.CENTER);
		frmMediaPlayer.getContentPane().add(길이);
		
		시간 = new JLabel("0:00");
		시간.setBounds(16, 100, 40, 20);
		시간.setHorizontalAlignment(SwingConstants.CENTER);
		frmMediaPlayer.getContentPane().add(시간);

		
		재생정지 = new JButton("\u25B6");
		재생정지.setIcon(null);
		재생정지.setBounds(275, 130, 65, 23);
		재생정지.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MediaFile.isFileExist()) {		
					if(StartMedia.playerStatus == StartMedia.NOTSTARTED) {
						playMedia();
					}
					if(StartMedia.playerStatus == StartMedia.PLAYING) {
						StartMedia.pause();
						재생정지.setText("▶");
					} else {
						StartMedia.resume();
					}
				} else {
					if(MediaFile.OpenFileDialog() == true) {
						playMedia();
					}				
				}
			}
		});
		
		바 = new JProgressBar();
		바.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int mouseX = arg0.getX();
				int progressBarValue = (int) Math.round(((double)mouseX / (double)바.getWidth()) * 바.getMaximum());
				바.setValue(progressBarValue);
			}
		});
		바.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				int mouseX = paramMouseEvent.getX();
				int progressBarValue = (int) Math.round(((double)mouseX / (double)바.getWidth()) * 바.getMaximum());
				바.setValue(progressBarValue);
			}
		});
		바.setForeground(new Color(30, 144, 255));
		바.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		바.setBounds(68, 100, 307, 20);
		frmMediaPlayer.getContentPane().add(바);
		frmMediaPlayer.getContentPane().add(재생정지);
		
		다음트랙 = new JButton("\u25B6\u25B6");
		다음트랙.setBounds(358, 130, 65, 23);
		다음트랙.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartMedia.nextTrack();
			}
		});
		frmMediaPlayer.getContentPane().add(다음트랙);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 444, 21);
		frmMediaPlayer.getContentPane().add(menuBar);
		
		mnFile = new JMenu("\uD30C\uC77C");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("\uC5F4\uAE30");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MediaFile.OpenFileDialog() == true) {
					playMedia();
				}				
			}
		});
		mnFile.add(mntmOpen);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmOpenPlaylist = new JMenuItem("\uC7AC\uC0DD\uBAA9\uB85D");
		mntmOpenPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PlayList.frame.isShowing()) {		
					PlayList.frame.show();
				} else {
					PlayList.open();
				}
			}
		});
		mnFile.add(mntmOpenPlaylist);
		
		menu = new JMenu("\uC624\uB514\uC624");
		menu.setBorder(null);
		menuBar.add(menu);
		JMenuItem 가사보기 = new JMenuItem("\uAC00\uC0AC\uBCF4\uAE30");
		가사보기.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LYRICToggle();
			}
		});
		
		볼륨라벨 = new JTextField();
		볼륨라벨.setText("\uC18C\uB9AC : ");
		볼륨라벨.setEditable(false);
		볼륨라벨.setBorder(null);
		menu.add(볼륨라벨);
		
		볼륨 = new JSlider();
		볼륨.setValue(10);
		볼륨.setMajorTickSpacing(10);
		볼륨.setMinorTickSpacing(5);
		볼륨.setPaintLabels(true);
		볼륨.setPaintTicks(true);
		볼륨.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = 볼륨.getValue();
				float volume = (float) (value * 0.01);
				Audio.setVolume(volume);
				볼륨라벨.setText("\uC18C\uB9AC : " + 볼륨.getValue());
			}
		});
		볼륨.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int value = 볼륨.getValue();
				float volume = (float) (value * 0.01);
				Audio.setVolume(volume);
				볼륨라벨.setText("\uC18C\uB9AC : " + 볼륨.getValue());
			}
		});
		볼륨.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int value = 볼륨.getValue(); 
				float volume = (float) (value * 0.01);
				Audio.setVolume(volume);
				볼륨라벨.setText("\uC18C\uB9AC : " + 볼륨.getValue());
			}
		});
		
		볼륨라벨.setText("\uC18C\uB9AC : " + 볼륨.getValue());
		menu.add(볼륨);
		JSeparator separator_1 = new JSeparator();
		menu.add(separator_1);
		
		가사크기라벨 = new JTextField();
		가사크기라벨.setBorder(null);
		가사크기라벨.setEditable(false);
		가사크기라벨.setText("가사 크기 : " + fontsize);
		menu.add(가사크기라벨);
		가사크기라벨.setColumns(10);
		
		가사크기 = new JSlider();
		가사크기.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fontsize = 가사크기.getValue();
				가사.setFont(new Font("맑은 고딕", Font.PLAIN, fontsize));
				가사크기라벨.setText("가사 크기 : " + fontsize);
			}
		});
		가사크기.setMinorTickSpacing(1);
		가사크기.setPaintLabels(true);
		가사크기.setMajorTickSpacing(5);
		가사크기.setPaintTicks(true);
		가사크기.setValue(13);
		가사크기.setMaximum(30);
		menu.add(가사크기);
		menu.add(가사보기);
		
		JProgressBar 메모리사용량 = new JProgressBar();
		메모리사용량.setFont(new Font("굴림", Font.PLAIN, 13));
		메모리사용량.setStringPainted(true);
		메모리사용량.setString("메모리 사용량");
		menuBar.add(메모리사용량);
		
		이전트랙 = new JButton("\u25C0\u25C0");
		이전트랙.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartMedia.restoreTrack();
			}
		});
		이전트랙.setBounds(109, 130, 65, 23);
		frmMediaPlayer.getContentPane().add(이전트랙);
		
		중지 = new JButton("\u25A0");
		중지.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new StartMedia(MediaFile.getFile());
				StartMedia.pause();
				
			}
		});
		중지.setBounds(192, 130, 65, 23);
		frmMediaPlayer.getContentPane().add(중지);
		
		반복재생버튼 = new JRadioButton("\uBC18\uBCF5\uC7AC\uC0DD");
		반복재생버튼.setBounds(18, 130, 73, 23);
		frmMediaPlayer.getContentPane().add(반복재생버튼);
		
		이미지 = new JLabel("");
		이미지.setBounds(46, 30, 60, 60);
		frmMediaPlayer.getContentPane().add(이미지);
		반복재생버튼.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(반복재생버튼.isSelected()) {
					반복재생여부 = false;
				} else {
					반복재생여부 = true;
				}
			}
		});

		Runnable r = new Runnable() {
		
			@Override
			public void run() {
				long aftUseMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				int totalmemory = (int) ((Runtime.getRuntime().totalMemory() / (1024 * 1024)));
				int usememory = (int) ((aftUseMemory / (1024 * 1024)));
				메모리사용량.setMaximum(totalmemory);
				메모리사용량.setValue(usememory);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				run();
			}
		};
		Thread thread = new Thread(r);
		thread.start();
		
		new FileDrop(frmMediaPlayer, new FileDrop.Listener() {
			public void filesDropped( java.io.File[] files ) {
				for(int i = 0; i < files.length; i++) {
					if(files[i].getName().toLowerCase().endsWith(".mp3")) {
						if(i == 0) {
							MediaFile.setFile(files[i]);
						}
						playlist.addPlayListInfo(files[i].getName(), files[i].getPath());
						medialist.addElement(files[i].getName());
					}
				}
				PlayList.jplaylist.setListData(medialist);
				playMedia();
			}
	      });
	}
	
	
	public static void LYRICToggle() {
		if(blyricstoggle == false) {
			frmMediaPlayer.setSize(450, 500);
			blyricstoggle = true;
		} else {
			frmMediaPlayer.setSize(450, 200);
			blyricstoggle = false;
		}
	}
	
	
	public static void playMedia() {
		try {
			// MP3 정보 가져오기			
			MP3File mp3 = new MP3File(MediaFile.getFile());
			
			Tag tag = mp3.getTag();
			
//			String title = new String(tag.getFirst(FieldKey.TITLE).getBytes("ISO-8859-1"), "EUC-KR");
//			String artist = new String(tag.getFirst(FieldKey.ARTIST).getBytes("ISO-8859-1"), "EUC-KR");
//			String album = new String(tag.getFirst(FieldKey.ALBUM).getBytes("ISO-8859-1"), "EUC-KR");
//			String lyric = new String(tag.getFirst(FieldKey.LYRICS).getBytes("ISO-8859-1"), "EUC-KR");
			String title = tag.getFirst(FieldKey.TITLE);
			String artist = tag.getFirst(FieldKey.ARTIST);
			String album = tag.getFirst(FieldKey.ALBUM);
			String lyric = tag.getFirst(FieldKey.LYRICS);
			AudioHeader audioheader = mp3.getAudioHeader();
			
			int lenght = audioheader.getTrackLength();

			//시간바
						
			바.setValue(0);
			바.setMinimum(0);
			바.setMaximum(lenght);
			
			이미지.setIcon(new ImageIcon(resize(getImage(tag), 60, 60)));
			
			길이.setText(StopWatch.secToTime(lenght));
			
			if(title.isEmpty()) {
				노래제목.setText(MediaFile.getFile().getName());
			} else {
				노래제목.setText(title);
			}
			
			if(artist.isEmpty() && album.isEmpty()) { 
				아티스트앨범.setText("<html>정보 없음</html>");
			} else if(artist.isEmpty()) {
				아티스트앨범.setText("<html>알 수 없음 - " + album + "</html>");
			} else if(album.isEmpty()) {
				아티스트앨범.setText("<html>" + artist + " - 알 수 없음</html>");
			} else {
				아티스트앨범.setText("<html>" + artist + " - " + album + "</html>");
			}
			
			if(lyric.isEmpty()) {
				가사.setText("가사가 등록되지 않았습니다.");
			} else {
				가사.setText(lyric);
				가사.setCaretPosition(0);
			}
			
			
			new StartMedia(MediaFile.getFile());
			StartMedia.play();
			
		} catch (CannotReadException e) {
			MessageBox.ErrorMessage("읽을 수 없음", "읽을 수 없는 파일입니다.");
		} catch (IOException e) {
			MessageBox.ErrorMessage("파일 존재하지 않음", "해당 파일이 존재하지 않습니다.");
		} catch (TagException e) {
			MessageBox.ErrorMessage("태그에 문제 있음", "태그에 문제가 발생하였습니다.");
		} catch (ReadOnlyFileException e) {
			MessageBox.ErrorMessage("사용할 수 없음", "읽기전용 파일입니다..");
		} catch (InvalidAudioFrameException e) {
			MessageBox.ErrorMessage("InvalidAudioFrameException", "InvalidAudioFrameException");
		} catch (NullPointerException e) {				
			System.out.println("선택되지 않음");
		}
	}
	
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public static BufferedImage getImage(Tag tag) {
		Artwork artwork = tag.getFirstArtwork();
		byte[] imageRawData = artwork != null ? artwork.getBinaryData() : null;
		if(imageRawData != null) {
			BufferedImage bi;
			try {
				bi = ImageIO.read(new ByteArrayInputStream(imageRawData));
				return bi;
			} catch (IOException e) {
			    
			}
		}
		return null;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
}
