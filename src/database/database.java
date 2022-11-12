package database;

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
        String sq = "select * from user";
        try {
            result = statement.executeQuery(sq);
            while(result.next()){
                String pwdfromdb=result.getString(2);
                if(pwdfromdb.equals(pwdclient)){
                    System.out.println("Correct ID and Password");
                    return true;
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
        String sq = "insert into user values(?,?)";
        try {
            preparedstatement =con.prepareStatement(sq);
            preparedstatement.setString(1,idclient);
            preparedstatement.setString(2,pwdclient);
            int count = preparedstatement.executeUpdate();
            if (count == 0) {
                System.out.println("데이터 입력 실패");
                return false;
            } else {
                System.out.println("데이터 입력 성공");
                return true;
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
