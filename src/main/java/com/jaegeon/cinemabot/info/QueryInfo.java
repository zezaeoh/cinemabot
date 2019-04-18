package com.jaegeon.cinemabot.info;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class QueryInfo {
	private String command; // 명령어 입력 ex) 상영시간표
	private int thId; // 영화관 ID 입력 (cgv: 100, lc: 200)
	private LinkedList<BranchInfo> thBrIds; // 지점 id들 입력 !!반드시 BranchInfo instance를 넣을것!!
	private String mvTime; // 영화 시간대 입력 !!반드시 다음 형식으로 넣을것 "2018-08-11 14:%"!!
	private String date; // 추후 업데이트 기능 현재는 무조건 오늘만 가능 "2018-08-11 %"
	
	public QueryInfo() {
		command = null; 
		thId = -1;
		thBrIds = new LinkedList<>();
		mvTime = null;
		date = new SimpleDateFormat("yyyy-MM-dd %").format(new Date());
	}
	
	public String getDate() {
		return date;
	}

	public boolean haveCommand() {
		return command != null && !command.isEmpty();
	}
	
	public boolean haveThId() {
		return thId != -1;
	}
	
	public boolean haveThBrId() {
		return thBrIds != null && !thBrIds.isEmpty();
	}
	
	public boolean haveMvTime() {
		if(mvTime == null || mvTime.isEmpty())
			return false;
		return true;
	}
	
	public String getCommand() {
		return command;
	}

	public int getThId() {
		return thId;
	}

	public LinkedList<BranchInfo> getThBrIds() {
		return thBrIds;
	}

	public String getMvTime() {
		return mvTime;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setThId(int thId) {
		this.thId = thId;
	}

	public void addThBrId(BranchInfo thBrId) {
		this.thBrIds.add(thBrId);
	}
	
	public void addAllThBrIds(List<BranchInfo> thBrIds) {
		this.thBrIds.addAll(thBrIds);
	}

	public void setMvTime(String mvTime) {
		this.mvTime = mvTime;
	}
}
