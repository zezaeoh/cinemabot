package com.jaegeon.cinemabot.service;

import com.jaegeon.cinemabot.db.CachedData;
import com.jaegeon.cinemabot.db.DBManager;
import com.jaegeon.cinemabot.info.BranchInfo;
import com.jaegeon.cinemabot.info.QueryInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MessageProcessingService {
    private static DBManager dm = new DBManager();
    private static CachedData cd = new CachedData(dm);

    public static String processMssage(String msg) {
        printLog("processMgs()");
        String thName = null;
        String time = null;
        BranchInfo bi = null;
        List<BranchInfo> biList = new LinkedList<>();
        HashMap<String, LinkedList<String>> commandSet = cd.getCm();
        LinkedList<String> splitedMsg = new LinkedList<>();
        QueryInfo qi = new QueryInfo(); // 밑으로 내려가면서 조건을 체크하면서 점차 이 인스턴스를 채울 것

        if(msg == null || msg.isEmpty()) {
            printLog("null message inserted");
            return null;
        }

        // command split
        Collections.addAll(splitedMsg, msg.split("\\s+"));
        printLog(splitedMsg.toString());
        // command check
        outerloop:
        for(String command: commandSet.keySet())
            for (String sMsg : splitedMsg)
                for (String ssleaf : commandSet.get(command))
                    if (sMsg.equals(ssleaf)) {
                        printLog("Command is matched: " + sMsg + "=" + ssleaf);
                        splitedMsg.remove(sMsg);
                        qi.setCommand(command); // 명령어가 매치되었으므로 qi에 입력
                        break outerloop;
                    }
        if (!qi.haveCommand()) // 위 loop를 지나서도 명령어를 매치하지 못했으면 false를 리턴
            return null;
        printLog("Command is checked.." + splitedMsg.toString());
        // theater check
        outerloop:
        for (String sMsg : splitedMsg)
            for (String[] id : cd.getThid())
                for (String thIDString : cd.getTm().get(Integer.parseInt(id[0])))
                    if (sMsg.equals(thIDString)) {
                        printLog("theater is matched: " + sMsg + "=" + thIDString);
                        thName = id[1];
                        splitedMsg.remove(sMsg);
                        qi.setThId(Integer.parseInt(id[0]));
                        break outerloop;
                    }
        printLog("theater is checked.." + splitedMsg.toString());

        outerloop:
        for (String sMsg : splitedMsg)
            if (qi.haveThId()) {// branch check
                if (qi.getThId() == 100)
                    sMsg = "CGV" + sMsg;
                printLog(sMsg);
                bi = cd.getBranchName(thName, sMsg);
                if (bi != null) {
                    qi.addThBrId(bi);
                    break outerloop;
                }
            } else {// time check
                printLog("!haveThId() " + sMsg);
                biList.addAll(cd.getBranchNames(sMsg));
                if (Pattern.matches("^[0-9 시]*$", sMsg)) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                    Date date = new Date();
                    time = sMsg.replaceAll("[^0-9]", "");
                    printLog(time);
                    if (time.length() == 1)
                        time = "0" + time;
                    time = dateFormat.format(date) + time + ":%";
                    printLog("Time.." + time);
                    qi.setMvTime(time);
                }
            }
        qi.addAllThBrIds(biList);

        printLog(biList);
        printLog("QueryInfo: " + qi.getCommand() + " " + Integer.toString(qi.getThId()) + " " + qi.getThBrIds() + " "
                + qi.getMvTime() + "\n");

        /*
         * 조건 매칭을 계속 해나가면서 위의 과정을 반복 조건들: (영화관 이름 & 지점 이름) 또는 (지역 이름 & 영화 시간대)
         *
         * 조건 매칭에 사용할 수 있는 명령어들: 1. db.getBranchName(thName, msg) --> 영화관 이름이 찾아진 경우 해당
         * 명령어를 이용하여 영화관 이름과 잘라진 msg들을 넘기면 해당되는 정확한 이름의 지점이 존재하는 경우 BranchInfo를 리턴한다.
         * 못찾는 경우에는 null을 리턴한다. 2. db.getBranchNames(msg) --> 영화관 이름을 발견하지 못한 경우 지역정보를
         * 확인하기 위해 모든 잘라진 msg들을 넘긴다. 지역정보가 포함된 지점의 정보들을 모두 확인하여 BranchInfo의 리스트를 리턴한다.
         * 지점이 존해하지 않는 경우 empty한 리스트를 리턴한다.
         */

        /*
         * Test command 수원 롯시 시간표 알려줘 13시 수원 시간표 알려줘 수원 13시 시간표 알려줘
         */

        List<String> qr = dm.getQueryResult(qi); // 모든 조건이 체크된 qi를 이용하여 쿼리를 날림
        if (qr == null || qr.isEmpty())
            return null;

        // 결과로 받은 String들을 client에게 전송
        StringBuffer sb = new StringBuffer();
        for (String ss : qr)
            sb.append(ss).append("<br>");
        return sb.toString();
    }

    private static void printLog(String msg) {
        System.out.println("\tLOG: " + msg);
    }

    private static void printLog(List<BranchInfo> biList) {
        int i = 0;
        for (BranchInfo tmp : biList) {
            printLog("biList[" + i + "] = " + tmp.getBrName());
            i++;
        }
    }
}
