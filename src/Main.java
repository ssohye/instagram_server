import chatting.*;
import loginregisterserver.*;
import filetranfer.*;


public class Main {
    public static void main(String[] args) {
        manager login = new manager();
        chating_server chat = new chating_server();
        file_server file =new file_server();
        request_server rs=new request_server();
        System.out.println("Hello world!");
        login.run();
        chat.run();
        file.run();
        rs.run();


    }
}