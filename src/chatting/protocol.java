package chatting;

import java.io.Serializable;
import java.util.ArrayList;

public class protocol implements Serializable {
    private int typeofrequest;
    private ArrayList<String> list = new ArrayList<>();
    private String sender;
    private String roomnumber;
    private String messege;
    private String time;
    private boolean file_exist;
    private String file_name;
    private String error_type;


    public protocol(){}

    // 요청 프로토콜
    // 방 생성인 경우 (방 생성 요청) type = 1
    // 방 초대인 경우 (방 초대 요청) type = 2
    // 방에서 나가기인 경우 (방 나가기 요청) type = 3
    // 메시지 보내기인 경우 (메시지 보내기 요청) type = 4
    // 로그아웃인 경우 (로그아웃 요청) type = 5
    // 체팅방 유저 목록 불러오기 (유저 정보가 담긴 list 요청) = 6
    // 팔로워 정보 불러오기 (팔로워 정보가 담긴 list 요청) = 7
    // 팔로잉 정보 불러오기 (팔로잉 정보가 담긴 list 요청) = 8

    // 전송 프로토콜
    // 에러 메시지 전송 (에러 메시지 전송)

    // 방 생성인 경우 1
    public protocol(int typeofrequest, String sender, ArrayList<String> list){
        this.sender = sender;
        this.typeofrequest = typeofrequest;
        this.list = list;
    }

    // 방 초대 2
    public protocol(int typeofrequest, String sender, String roomnumber, ArrayList<String>list){
        this.typeofrequest = typeofrequest;
        this.sender = sender;
        this.roomnumber = roomnumber;
        this.list = list;
    }

    // 방에서 나가기 3
    public protocol(int typeofrequest, String sender, String roomnumber){
        this.typeofrequest = typeofrequest;
        this.sender = sender;
        this.roomnumber = roomnumber;
    }

    // 메시지 보내기 4
    public protocol(int typeofrequest, String roomnumber, String sender, String messege, String time, boolean file_exist, String file_name){
        this.typeofrequest = typeofrequest;
        this.roomnumber = roomnumber;
        this.sender = sender;
        this.messege = messege;
        this.time = time;
        this.file_exist = file_exist;
        this.file_name = file_name;
    }

    // 로그아웃 5, 팔로워, 팔로잉 목록 불러오기 요청 9, 10
    public protocol(int typeofrequest, String sender){
        this.typeofrequest = typeofrequest;
        this.sender = sender;
    }

//    // 체팅방 유저 목록 불러오기 6
//    public protocol(int typeofrequest, String roomnumber){
//        this.typeofrequest = typeofrequest;
//        this.roomnumber = roomnumber;
//    }

    // 파일을 보내는 경우 7, 파일을 요청하는 경우 8
//    public protocol(int typeofrequest, String roomnumber, String file_path){
//        this.typeofrequest = typeofrequest;
//        this.roomnumber = roomnumber;
//        this.file_name = file_path;
//    }

    public protocol(String error_type) {
        this.error_type = error_type;
    }

    //make getter function
    public int getTypeofrequest() {
        return typeofrequest;
    }
    public ArrayList<String> getList() {
        return list;
    }
    public String getSender() {
        return sender;
    }
    public String getRoomnumber() {
        return roomnumber;
    }
    public String getMessege() {
        return messege;
    }
    public String getTime() {
        return time;
    }
    public boolean isFile_exist() {
        return file_exist;
    }
    public String getFile_name() {
        return file_name;
    }

    //make setter function
    public void setTypeofrequest(int typeofrequest) {
        this.typeofrequest = typeofrequest;
    }
    public void setList(ArrayList<String> list) {
        this.list = list;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }
    public void setMessege(String messege) {
        this.messege = messege;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setFile_exist(boolean file_exist) {
        this.file_exist = file_exist;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }



}
