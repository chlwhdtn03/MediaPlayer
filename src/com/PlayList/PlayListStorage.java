package com.PlayList;

public abstract class PlayListStorage {
	public abstract void addPlayListInfo(String name, String path);
	public abstract String searchPath(String name);
	public abstract String getPlayListInfo(int i);
}
