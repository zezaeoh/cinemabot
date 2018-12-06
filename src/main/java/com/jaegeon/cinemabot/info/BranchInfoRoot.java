package com.jaegeon.cinemabot.info;

import java.util.LinkedList;

public class BranchInfoRoot {
	private final String thName;
	private LinkedList<BranchInfo> list;
	
	public BranchInfoRoot(String thName) {
		this.thName = thName;
		list = new LinkedList<>();
	}
	
	public void addInfo(String brName, int brId, int thId) {
		list.add(new BranchInfo(brName, brId, thId));
	}

	public void addInfo(BranchInfo tb){
		list.add(tb);
	}

	public String getThName() {
		return thName;
	}

	public LinkedList<BranchInfo> getList() {
		return list;
	}
	
}
