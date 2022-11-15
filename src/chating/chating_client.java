package chating;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.LocalDate;
import java.io.Serializable;
public class chating_client {
    private final static HashSet<String> chat_member= new HashSet<String>();
    private int chat_id = 0;

    chating_client(){

    }

    public void chat_member_add(String member){
        chat_member.add(member);
    }

    public void chat_create(){
        //서버로 그.... 어.... chat_member넘기기
        try{
            var socket = new Socket("localhost", 7777);
            var in = socket.getInputStream();
            var out = socket.getOutputStream();

            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);

            oos.writeObject(chat_member);
            this.chat_id = (int) ois.readObject();

        }
        catch(Exception e){

        }

    }

    public void chat_message(int member_id,String a){
        try{
            var socket = new Socket("localhost", 7777);
            var in = socket.getInputStream();
            var out = socket.getOutputStream();

            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);

            LocalDate now = LocalDate.now();

            oos.writeObject(new message_output_form(chat_id,now,member_id,a));
        }
        catch(Exception e){

        }
    }

    public static class message_output_form implements Serializable{
        private int id;
        private LocalDate now;
        private int member_id;
        private String message;

        message_output_form(int id, LocalDate now, int member_id, String message){
            this.id = id;
            this.message = message;
            this.member_id = member_id;
            this.now = now;
        }
    }
}
