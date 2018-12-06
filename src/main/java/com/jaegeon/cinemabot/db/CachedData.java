package com.jaegeon.cinemabot.db;

import com.jaegeon.cinemabot.info.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CachedData {
    private final String[][] thid;
    private final HashMap<String, LinkedList<String>> cm;
    private final HashMap<Integer,LinkedList<String>> tm;

    private static HashMap<String, BranchInfoRoot> branchInfos = null;

    public CachedData(DBManager dm){
        thid = TheaterSet.getThIDList();
        cm = CommandSet.getCommandMap();
        tm = TheaterSet.getTheaterMap();

        if(branchInfos == null){
            branchInfos = new HashMap<>();
            for(String[] s : thid)
                branchInfos.put(s[1], dm.getBranchInfo(s));
        }
    }

    public BranchInfo getBranchName(String th_name, String msg) { // 영화관 이름이 존재하고 정확한 지점 이름이 들어올 때 매칭된 branchInfo를 리턴한다.
        BranchInfoRoot root = branchInfos.get(th_name);
        if (root == null)
            return null;
        for (BranchInfo bi : root.getList()) {
            if (bi.getBrName().equals(msg))
                return bi;
        }
        return null;
    }

    public List<BranchInfo> getBranchNames(String msg) { // 특정 지역에 대한 String이 들어왔을 때 해당 지역에 해당하는 모든 branchInfo를 List형태로
        // 리턴한다.
        LinkedList<BranchInfo> rl = new LinkedList<>();
        for (String s : branchInfos.keySet())
            for (BranchInfo bi : branchInfos.get(s).getList())
                if (bi.getBrName().contains(msg))
                    rl.add(bi);
        return rl;
    }

    public HashMap<String, LinkedList<String>> getCm() {
        return cm;
    }

    public HashMap<Integer, LinkedList<String>> getTm() {
        return tm;
    }

    public String[][] getThid() {
        return thid;
    }
}
