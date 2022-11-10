package chating;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
public class chating_client {
    private final static HashSet<String> chat_member= new HashSet<String>();
    chating_client(){

    }

    public void chat_member_add(String member){
        chat_member.add(member);
    }

    public void chat_create(){
        //서버로 그.... 어.... chat_member넘기기
        try{
            var socket = new Socket("localhost", 7777);
            var out = socket.getOutputStream();
            var os = new ObjectOutputStream(out);
            os.writeObject(chat_member);

        }
        catch(Exception e){

        }

    }

    public void chat_message(){

    }
}
