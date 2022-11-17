package database;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class database {
    String url = "jdbc:mysql://swiftsjh.tplinkdns.com:3306/insta";
    String userName = "dmz";
    String password = "1234";
    Connection con = null;
    Statement statement = null;
    PreparedStatement preparedstatement = null;
    ResultSet result=null;


    public database() {
        try {
            con = DriverManager.getConnection(url, userName, password);
            statement = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Boolean logincheck(String idclient, String pwdclient) {
        String sq = "select user_id,email,password from User";
        String makeuseronline ="INSERT INTO online_user (session_id,user_id) VALUE(1,?);";
        String checkifuseronline="select session_id from online_user where user_id=?;";
        String makeuseronline2 ="update online_user set session_id=1 where user_id=?;";
        String user_id;
        try {
            result = statement.executeQuery(sq);


            while(result.next()){
                String pwdfromdb=result.getString(3);
                String idfromdb=result.getString(2);
                if(pwdfromdb.equals(pwdclient)){
                    if(idfromdb.equals(idclient)){ //아이디 비번이 일치하는게 존재하면
                        ResultSet rs2=null; //세션 정보를 가져올 변수
                        user_id=result.getString(1);
                        System.out.println("Correct ID and Password");
                        preparedstatement = con.prepareStatement(checkifuseronline);
                        preparedstatement.setString(1,user_id);
                        rs2 = preparedstatement.executeQuery(); //세션 정보 가져오는 쿼리 실행
                        while (rs2.next()){
                            String session_id=rs2.getString(1);
                            if(session_id.equals("-1")){// 오프라인 상태로 확인 된다면 온라인으로 전환
                                preparedstatement = con.prepareStatement(makeuseronline2);
                                preparedstatement.setString(1,user_id);
                                int count = preparedstatement.executeUpdate();
                                if (count == 0) {
                                    System.out.println("이미 세션에 존재하는 유저 온라인 상태 전환 실패");

                                } else {
                                    System.out.println("이미 세션에 존재하는 유저 온라인 상태 전환 성공");
                                    return true;
                                }
                            }
                            else{ //비정상적 로그아웃으로 인해 세션에 온라인으로 표시되는 유저라면
                                System.out.println("비정상 로그아웃으로 인해 세션에 온라인으로 표시되던 유저가 로그인 함");
                                return true;

                            }
                        }
                        // 세션에 한번도 로그인 된적 없는 경우
                        preparedstatement =con.prepareStatement(makeuseronline);
                        preparedstatement.setString(1,user_id);
                        int count = preparedstatement.executeUpdate();
                        if (count == 0) {
                            System.out.println("처음으로 로그인하는 유저 온라인 상태 전환 실패");

                        } else {
                            System.out.println("처음으로 로그인 하는 유저 온라인 상태 전환 성공");
                            return true;
                        }

                    }

                }
            }
            System.out.println("no correct password or id");
            return false;
        }catch (Exception e){
            System.out.println(e);
        }
        return false;


    }

    public Boolean register(String idclient, String pwdclient) {
        String sq ="INSERT INTO User (email, password) VALUE(?,?);";
        try {
                preparedstatement =con.prepareStatement(sq);
                preparedstatement.setString(1,idclient);
                preparedstatement.setString(2,pwdclient);
                int count = preparedstatement.executeUpdate();
                if (count == 0) {
                    System.out.println("데이터 입력 실패");
                    return false;
                } else {
                    System.out.println("회원가입성공");
                    return true;
                }

        }catch (Exception e){
            System.out.println(e);
        }
        return false;


    }

    public boolean duplicateemailcheck(String id){
        String sq ="select email from User where email=?;";
        try {
            preparedstatement =con.prepareStatement(sq);
            preparedstatement.setString(1,id);
            result=preparedstatement.executeQuery();
            int count=0;
            while (result.next()){
                count=result.getRow();
            }
            if(count==0){
                return true;
            }
            else{
                return false;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public int newroom(HashSet<String> member){

        int chat_id=0;
        String getlastchatnum="SELECT chat_id FROM chat_manager ORDER BY chat_id DESC LIMIT 1;";

        try {
            result = statement.executeQuery(getlastchatnum);
            while(result.next()) {
                chat_id = Integer.parseInt(result.getString(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        chat_id+=1;

        Iterator iter = member.iterator();	// Iterator 사용
        while(iter.hasNext()) {
            String sq = "insert into chat_manager values(?,?)";

            try {
                preparedstatement = con.prepareStatement(sq);
                preparedstatement.setString(1, Integer.toString(chat_id));
                preparedstatement.setString(2, iter.next().toString());
                int count = preparedstatement.executeUpdate();
                if (count == 0) {
                    System.out.println("데이터 입력 실패");

                } else {
                    System.out.println("데이터 입력 성공");

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }



        return chat_id;
    }



}
