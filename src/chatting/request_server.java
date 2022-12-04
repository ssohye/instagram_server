package chatting;

import database.database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class request_server implements Runnable {


    private static class ConnectThread extends Thread
    {
        ServerSocket serverSocket;
        int count = 1;

        ConnectThread (ServerSocket serverSocket) //생성자를 통해 서버소켓을 받음
        {
            System.out.println(" Server opened"); //서버가 열렸다는 메세지 출력
            this.serverSocket = serverSocket; //서버소켓을 저장
        }

        @Override
        public void run () {
            try {
                while (true){ //계속 새로운 클라이언트의 연결을 수락하고 새 소켓을 cLIENTtHREAD에 넘겨줌
                    Socket socket = serverSocket.accept();  //클라이언트의 연결을 수락
                    System.out.println("    Thread " + count + " is started.");
                    request serverThread = new request(socket, count);
                    serverThread.start(); //새로운 클라이언트의 연결을 수락하고 새 소켓을 cLIENTtHREAD에 넘겨줌
                    count++;
                }
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("    SERVER CLOSE    ");
            }
        }
    }
    @Override
    public void run(){
        ServerSocket serverSocket = null;
        try {   // 서버소켓을 생성, 8080 포트와 binding
            serverSocket = new ServerSocket(9998); // 생성자 내부에 bind()가 있고, bind() 내부에 listen() 있음
            ConnectThread connectThread = new ConnectThread(serverSocket); // 서버소켓을 connectThread에 넘겨줌
            connectThread.start(); // connectThread 시작
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class request extends Thread {
        Socket socket;
        int id;
        database db = new database();


        //생성자를 통해 입력받은 소켓과 클라이언트(쓰레드)의 id를 저장
        request(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                protocol content = null;
                content = (protocol) ois.readObject();
                if (content.getTypeofrequest() == 7) {
                    System.out.println(content.getSender() + "로 부터 팔로우 신청 요청이 들어옴");
                    String sender = content.getSender();
                    if (db.follow_request(content.getSender(), content.getFollow()) == true) {
                        System.out.println(sender + "가 " + content.getFollow() + "에게 팔로우 신청을 보냈습니다.");
                    }
                } else if (content.getTypeofrequest() == 8) {
                    System.out.println(content.getSender() + "로 부터 팔로우 취소 요청이 들어옴");
                    String sender = content.getSender();
                    if (db.follow_cancel(content.getSender(), content.getFollow()) == true) {
                        System.out.println(sender + "가 " + content.getFollow() + "에게 팔로우 취소를 보냈습니다.");
                    }
                } else if (content.getTypeofrequest() == 9) {
                    System.out.println(content.getSender() + "로부터 팔로우 확인 요청 들어옴");
                    String sender = content.getSender();
                    if (db.follow_check(content.getSender(), content.getFollow()) == true) {
                        System.out.println(sender + "가 " + content.getFollow() + "를 팔로우 하고 있습니다.");
                        protocol tmp_content = new protocol(9, "server", "true");
                        ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                        temp_oos.writeObject(tmp_content);
                        temp_oos.flush();
                    } else {
                        protocol tmp_content = new protocol(9, "server", "false");
                        ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                        temp_oos.writeObject(tmp_content);
                        temp_oos.flush();
                    }
                } else if (content.getTypeofrequest() == 10) {
                    System.out.println(content.getSender() + "로 부터 게시물 개수 확인 요청이 들어옴");
                    String sender = content.getSender();
                    int post_num = db.get_post_num(content.getSender());
                    protocol tmp_content = new protocol(10, post_num);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();

                } else if (content.getTypeofrequest() == 11) {
                    System.out.println(content.getSender() + "로 부터 방 목록 요청이 들어옴");
                    String sender = content.getSender();
                    ArrayList<String> response = db.get_users_room(db.get_user_id(sender));
                    for (int i = 0; i < response.size(); i++) {
                        System.out.println("방 목록 : " + response.get(i));
                    }

                    protocol tmp_content = new protocol(11, "server", response);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                    System.out.println(content.getSender() + "에게 방 목록 전송");

                } else if (content.getTypeofrequest() == 12) {
                    System.out.println(content.getSender() + "로 부터 " + content.getRoomnumber() + "방안의 유저 목록 요청이 들어옴");
                    String room_id_tmp = content.getRoomnumber();
                    ArrayList<String> response = db.get_user_list_in_room(room_id_tmp);
                    for (int i = 0; i < response.size(); i++) {
                        System.out.println("방 안 유저  : " + response.get(i));
                    }
                    protocol tmp_content = new protocol(12, "server", response);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                } else if (content.getTypeofrequest() == 15) {
                    System.out.println(content.getSender() + "로 부터 전체 유저 목록 요청이 들어옴");
                    ArrayList<String> response = db.get_all_user_id();
                    protocol tmp_content = new protocol(15, "server", response);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                } else if (content.getTypeofrequest() == 17) {
                    System.out.println(content.getSender() + "로 부터 게시물 업로드 요청 들어옴");
                    String sender=content.getSender();
                    String post_content=content.getMessage();
                    ArrayList<String> tag= content.getList();
                    String post_photo_name= content.getFile_name();
                    db.new_post(sender,post_content,post_photo_name,tag);
                }else if(content.getTypeofrequest()==16){
                    System.out.println(content.getSender()+"로 부터 전체 게시물 아이디 요청 받음");
                    ArrayList<String> response=db.get_all_post_id();
                    protocol tmp_content = new protocol(16, "server", response);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);

                }else if(content.getTypeofrequest()==18){
                    System.out.println(content.getSender()+"로 부터 게시물 요청 받음");
                    String post_id=content.getSender();
                    String post_content=db.get_post_content(post_id);
                    String post_photo_name=db.get_post_photo_name(post_id);
                    ArrayList<String> tag=new ArrayList<>();
                    ArrayList<Integer> hashtag_id=db.get_post_hashtag_id(post_id);
                    for(int i=0; i<hashtag_id.size(); i++){
                        tag.add(db.get_tag(hashtag_id.get(i)));
                    }

                    protocol tmp_content = new protocol(18, post_id, post_content,tag,post_photo_name);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();


                }
                else if (content.getTypeofrequest() == 19) {
                    System.out.println(content.getSender() + "로 부터 본인이 팔로우하고 있는 사람 수 요청이 들어옴");
                    String sender = content.getSender();
                    int following_num = db.get_follow_num(sender);
                    protocol tmp_content = new protocol(19, following_num);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();

                } else if (content.getTypeofrequest() == 20) {
                    System.out.println(content.getSender() + "로 부터 본인을 팔로우 하고 있는 사람 수 요청이 들어옴");
                    String sender = content.getSender();
                    int follower_num = db.get_follower_num(sender);
                    protocol tmp_content = new protocol(20, follower_num);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                }else if(content.getTypeofrequest()==21){
                    System.out.println(content.getSender()+"로부터 글의 작성자 확인 요청이 들어옴");
                    String post_id=content.getSender();
                    String writer=db.get_post_writer(post_id);
                    protocol tmp_content = new protocol(21, writer);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                }else if(content.getTypeofrequest()==49){//좋아요 확인요청
                    System.out.println(content.getSender()+"로부터 좋아요 확인 요청이 들어옴");
                    String sender=content.getSender();
                    boolean like= db.is_liked(sender,content.getFeed_id());
                    String result;
                    if(like==true){
                        result="true";
                    }else{
                        result="false";
                    }
                    protocol tmp_content = new protocol(49, result);
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                    
                }else if(content.getTypeofrequest()==50) {//좋아요 요청
                    String sender=content.getSender();
                    String post_id=content.getFeed_id();
                    boolean like = db.is_liked(sender,post_id);
                    if(like==true) {
                        db.delete_like(sender, post_id);
                    }else {
                        db.like(sender, post_id);
                    }
                    protocol tmp_content = new protocol(50, "success");
                    ObjectOutputStream temp_oos = new ObjectOutputStream(socket.getOutputStream());
                    temp_oos.writeObject(tmp_content);
                    temp_oos.flush();
                }
                else {
                    System.out.println("잘못된 요청입니다.");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
