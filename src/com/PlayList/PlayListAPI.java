package com.PlayList;

public class PlayListAPI extends PlayListStorage {
	
	public static PlayListInfo[] listarr;
	public static int listinfo;
	
	public PlayListAPI(int size) {
		listarr = new PlayListInfo[size];
		listinfo = 0;
	}

	public void addPlayListInfo(String name, String path) {
		listarr[listinfo] = new PlayListInfo(name, path);
		listinfo++;
	}

	public String searchPath(String name) {
		for(int i = 0; i < listinfo; i++) {
			if(name.contains(listarr[i].getName()))
			return listarr[i].getPath();
		}
		return null;
	}

	public String getPlayListInfo(int i) {		
		return listarr[i].getName();		
	}

}
