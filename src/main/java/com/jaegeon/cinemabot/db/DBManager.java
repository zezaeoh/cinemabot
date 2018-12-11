package com.jaegeon.cinemabot.db;

import com.jaegeon.cinemabot.info.BranchInfo;
import com.jaegeon.cinemabot.info.BranchInfoRoot;
import com.jaegeon.cinemabot.info.QueryInfo;
import com.jaegeon.cinemabot.info.SettingInfo;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DBManager {
    private final String JDBC_DRIVER;
    private final String DB_URL;
    private final String USER_NAME;
    private final String PASSWORD;

    private Connection conn = null;

    private Statement state = null;

    public DBManager() {
        JDBC_DRIVER = SettingInfo.getJdbcDriver();
        DB_URL = SettingInfo.getDbUrl();
        USER_NAME = SettingInfo.getUserName();
        PASSWORD = SettingInfo.getPassword();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            state = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectionValiedTest(){
        try {
            if(!conn.isValid(3600)) {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                state = conn.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BranchInfoRoot getBranchInfo(String[] s){
        String sql;
        BranchInfoRoot root;
        ResultSet rs;

        connectionValiedTest();

        sql = String.format("select br_name, br_id from th_branch where th_id = %s", s[0]);
        root = new BranchInfoRoot(s[1]);

        try {
            rs = state.executeQuery(sql);
            if(rs != null)
                while (rs.next())
                    root.addInfo(rs.getString(1), rs.getInt(2), Integer.parseInt(s[0]));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return root;
    }

    public List<String> getQueryResult(QueryInfo qi) { // 쿼리 info를 받아서 해당되는 쿼리를 수행 한 후 결과를 리턴한다. 해석할 수 없는경우 null을 리턴한다.
        ResultSet rs;
        LinkedList<String> reMsgs;
        StringBuffer sb;
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>> dict;
        String th_name, br_name, mv_title, mv_time;

        try {
            if (qi.getCommand().equals("상영시간표")) {
                rs = getTimeTable(qi);
                if(rs == null)
                    return null;

                dict = new LinkedHashMap<>();
                reMsgs = new LinkedList<>();
                sb = new StringBuffer();

                while(rs.next()) {
                    th_name = rs.getString(1);
                    br_name = rs.getString(2);
                    mv_title = rs.getString(3);
                    mv_time = rs.getString(4);

                    if(!dict.containsKey(th_name))
                        dict.put(th_name, new LinkedHashMap<>());
                    if(!dict.get(th_name).containsKey(br_name))
                        dict.get(th_name).put(br_name, new LinkedHashMap<>());
                    if(!dict.get(th_name).get(br_name).containsKey(mv_title))
                        dict.get(th_name).get(br_name).put(mv_title, new LinkedList<>());

                    dict.get(th_name).get(br_name).get(mv_title).add(mv_time.substring(11));
                }

                for(String tn : dict.keySet()) {
                    reMsgs.add(tn);
                    for(String bn: dict.get(tn).keySet()) {
                        reMsgs.add("  " + bn);
                        reMsgs.add(" ");
                        for(String mn: dict.get(tn).get(bn).keySet()) {
                            reMsgs.add("    " + mn);
                            for(String mt: dict.get(tn).get(bn).get(mn))
                                sb.append(mt + " ");
                            reMsgs.add(sb.toString());
                            reMsgs.add(" ");
                            sb.setLength(0);
                        }
                    }
                }

                if(reMsgs.isEmpty())
                    return null;
                return reMsgs;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private ResultSet getTimeTable(QueryInfo qi) {
        List<BranchInfo> li;
        ResultSet rs = null;
        String sql;
        StringBuffer sb;

        connectionValiedTest();

        try {
            if(qi.haveMvTime()) {
                if(!qi.haveThBrId())
                    return null;

                li = qi.getThBrIds();
                sb = new StringBuffer();

                sql = "select th_name, br_name, mv_title, mv_time " +
                        "from th_info t, th_branch b, (" +
                        "select th_id, br_id, mv_title, mv_time " +
                        "from th_mv_times " +
                        "where mv_time like '%s' && (%s)" +
                        ") m " +
                        "where m.th_id = t.th_id && m.br_id = b.br_id && t.th_id = b.th_id " +
                        "order by mv_time";

                for(int i=0; i<li.size(); i++) {
                    if(i == 0)
                        sb.append(String.format("(th_id = %d && br_id = %d)", li.get(i).getThId(), li.get(i).getBrId()));
                    else
                        sb.append(String.format(" || (th_id = %d && br_id = %d)", li.get(i).getThId(), li.get(i).getBrId()));
                }

                sql = String.format(sql, qi.getMvTime(), sb.toString());
                rs = state.executeQuery(sql);
            }else if(qi.haveThId()) {
                li = qi.getThBrIds();
                if(li.size() != 1)
                    return null;

                sql = "select th_name, br_name, mv_title, mv_time " +
                        "from th_mv_times m, th_branch b, th_info t " +
                        "where t.th_id = b.th_id && b.br_id = m.br_id &&" +
                        " t.th_id = m.th_id && m.th_id = %d && m.br_id = %d &&" +
                        " m.mv_time like '%s' " +
                        "order by mv_time";
                sql = String.format(sql, qi.getThId(), li.get(0).getBrId(), qi.getDate());
                rs = state.executeQuery(sql);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }

}
