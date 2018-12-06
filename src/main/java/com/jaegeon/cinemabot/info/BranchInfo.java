package com.jaegeon.cinemabot.info;

public class BranchInfo {
	private String brName;
	private int brId;
	private int thId;

	public BranchInfo(String brName, int brId, int thId) {
		this.brName = brName;
		this.brId = brId;
		this.thId = thId;
	}

	public String getBrName() {
		return brName;
	}

	public int getBrId() {
		return brId;
	}

	public int getThId() {
		return thId;
	}
}
