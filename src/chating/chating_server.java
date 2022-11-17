package chating;

import java.util.HashSet;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import database.*;

public class chating_server {
    
    public static void main(String[] args) throws Exception{
        ServerSocket listener = new ServerSocket(7777);
        System.out.println("서지호 멍청이");
        ExecutorService pool = Executors.newFixedThreadPool(20);

        while(true){
            Socket sock = listener.accept();
            pool.execute(new create(sock));
        }
    }

    public static class create implements Runnable{
        private Socket sock;
        private HashSet<String> member = new HashSet<String>();
        create(Socket sock) {
            this.sock = sock;
        }

        @Override
        public void run(){
            try{
                var in = sock.getInputStream();
                var out = sock.getOutputStream();

                ObjectInputStream ois = new ObjectInputStream(in);
                this.member = (HashSet<String>) ois.readObject();

                Iterator iter = member.iterator();
                while(iter.hasNext()) {
                    System.out.print(iter.next() + " ");
                }
                //맴버 받기

                database db = new database();
                int char_id = db.newroom(member);
                //database.database 톡방 schema에 맴버

                System.out.println(char_id);
                ObjectOutputStream oos = new ObjectOutputStream(out);

                oos.writeObject(char_id);

                //채팅방 id 보내기


                //메세지 받기
            }
            catch(Exception e){

            }
        }
    }
}
