package database;
import chatting.protocol;
import com.mysql.cj.protocol.Resultset;
import encryption.*;

import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class database {
    String url = "jdbc:mysql://swiftsjh.tplinkdns.com:3306/insta?autoReconnect=true";
    String userName = "dmz";
    String password = "1234";
    Connection con = null;
    Statement statement = null;
    PreparedStatement preparedstatement = null;
    ResultSet result=null;


    public database() {
        try {
            con = DriverManager.getConnection(url, userName, password);
            //System.out.println("get con complete");
            //System.out.println(con);
            statement = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean reset_db(){
        try{
            String sql = "delete from chat_manager;";
            String sql2= "delete from chat_table";
            String sql3="delete from online_user";
            String sql4="delete from User";
            statement.execute(sql);
            statement.execute(sql2);
            statement.execute(sql3);
            statement.execute(sql4);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int get_user_id(String id){
        int user_id = -1;
        try{
            String sql = "select user_id from User where email = ?";
            preparedstatement = con.prepareStatement(sql);
            preparedstatement.setString(1, id);
            result = preparedstatement.executeQuery();
            if(result.next()){
                user_id = result.getInt("user_id");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return user_id;
    }

    public boolean follow_request(String sender,String follow){
        try{
            String sql1="INSERT INTO follow (follower_id,following_id) VALUES (?,?)";
            int a = get_user_id(sender);
            int b = get_user_id(follow);
            preparedstatement = con.prepareStatement(sql1);
            preparedstatement.setInt(1, a);
            preparedstatement.setInt(2, b);
            preparedstatement.executeUpdate();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean follow_cancel(String sender,String follow){
        try{
            String sql="delete from follow where follower_id=? and following_id=?;";
            int a = get_user_id(sender);
            int b = get_user_id(follow);
            preparedstatement = con.prepareStatement(sql);
            preparedstatement.setInt(1, a);
            preparedstatement.setInt(2, b);
            preparedstatement.executeUpdate();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> get_all_user_id(){
        ArrayList<String> user_id = new ArrayList<>();
        try{
            String sql = "select email from User;";
            preparedstatement = con.prepareStatement(sql);
            result = preparedstatement.executeQuery();

            while(result.next()){
                user_id.add(result.getString("email"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return user_id;
    }

    public String get_user_id_as_String(int id){
        String user_id = null;
        try{
            String sql = "select email from User where user_id = ?;";
            preparedstatement = con.prepareStatement(sql);
            preparedstatement.setInt(1, id);
            result = preparedstatement.executeQuery();

            if(result.next()){
                user_id = result.getString("email");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return user_id;
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
            System.out.println(preparedstatement);
            preparedstatement.setString(1,idclient);
            System.out.println(preparedstatement);
            preparedstatement.setString(2,pwdclient);
            System.out.println(preparedstatement);
            int count = preparedstatement.executeUpdate();
            System.out.println(preparedstatement);
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

    public boolean logout(int user_id){
        String sq ="update online_user set session_id=-1 where user_id=?;";
        try {
            preparedstatement = con.prepareStatement(sq);
            preparedstatement.setInt(1,user_id);
            preparedstatement.executeUpdate();
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }


    }

    public ArrayList<String> get_users_room(int user_id){
        String sq ="select chat_id from chat_manager where member=?;";
        ArrayList<String> room_id_list=new ArrayList<>();
        try {
            preparedstatement = con.prepareStatement(sq);
            preparedstatement.setInt(1,user_id);
            result=preparedstatement.executeQuery();
            while (result.next()){
                room_id_list.add(result.getString(1));
            }
            return room_id_list;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public String getroom_id(ArrayList<Integer> user_list){
        int size=user_list.size();
        String room_id=Integer.toString(size);
        for(int i=0; i<size; i++){
            room_id=room_id+Integer.toString(user_list.get(i));
        }
        room_id=room_id+getServerDateTime();
        String room_id_md5=md5.encMD5(room_id);
        return room_id_md5;
    }

    public String getServerDateTime(){
        String DateTime=null;
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm_ss");
        DateTime = now.format(formatter);
        return DateTime;

    }

    public String newroom(protocol tmp){
        String sql ="insert into chat_manager (chat_id,member) values (?,?);";
        String sql2="insert into chat_table (chat_room_id,chat_file) values (?,?);";
        ArrayList<Integer> user_list= new ArrayList<>();
        for(int i=0; i<tmp.getList().size(); i++){
            user_list.add(get_user_id(tmp.getList().get(i)));
            System.out.println("초대 할 유저 아이디 : "+user_list.get(i));
        }
        if(user_list.contains(-1)){
            Integer t = Integer.getInteger("-1");
            user_list.remove(t);
        }
        String room_id=getroom_id(user_list);
        System.out.println("new room_id : "+room_id);
        try{
            preparedstatement =con.prepareStatement(sql);
            for(int i=0; i<user_list.size();i++){
                preparedstatement.setString(1,room_id);
                preparedstatement.setInt(2,user_list.get(i));
                preparedstatement.executeUpdate();
            }

            String folder_path=makedir(room_id);
            preparedstatement = con.prepareStatement(sql2);
            preparedstatement.setString(1,room_id);
            preparedstatement.setString(2,folder_path);
            preparedstatement.executeUpdate();

            return room_id;

        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    public boolean exitroom(String room_id,int user_id){
        String sql="delete from chat_manager where member=? and chat_id=?;";
        try {
            preparedstatement = con.prepareStatement(sql);
            preparedstatement.setInt(1,user_id);
            preparedstatement.setString(2,room_id);
            preparedstatement.executeUpdate();
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean invite_user_to_room(int user_id,String room_id,ArrayList<String> list){

        if(list.size()==0){
            return false;
        }


        try {
            String sq ="insert into chat_manager (chat_id,member) values (?,?);";
            String check_sql="select chat_id from chat_manager where chat_id=? and member=?;";
            PreparedStatement tmp = con.prepareStatement(check_sql);
            PreparedStatement tmp2=con.prepareStatement(sq);
            for(int i=0; i<list.size(); i++){
                tmp.setString(1,room_id);
                tmp.setInt(2,get_user_id(list.get(i)));
                result=tmp.executeQuery();
                int count=0;
                while (result.next()){
                    count=result.getRow();
                }
                if(count==0){
                    tmp2 = con.prepareStatement(sq);
                    tmp2.setString(1,room_id);
                    tmp2.setInt(2,get_user_id(list.get(i)));
                    tmp2.executeUpdate();
                    System.out.println(user_id + "(고유번호)에 의해 " + list.get(i) + "가 방" + room_id + "에 초대되었습니다.");
                }else {
                    System.out.println(list.get(i)+"는 이미 "+room_id+"방에 있습니다.");
                }

            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }


    public String makedir(String room_id){

        String path = "chatting_data/"+room_id;
        File Folder = new File(path);

        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
                return Folder.getAbsolutePath();
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
            return null;
        }
        return null;
    }

    public ArrayList<String> get_user_list_in_room(String room_id){
        String check_sql="select member from chat_manager where chat_id=?;";
        ArrayList<String> tmp = new ArrayList<String>();
        try {
            ResultSet result_tmp;
            PreparedStatement A = con.prepareStatement((check_sql));
            A.setString(1,room_id);
            result_tmp=A.executeQuery();
            while (result_tmp.next()){
                tmp.add(get_user_id_as_String(result_tmp.getInt(1)));
            }

            return tmp;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }


    }




}