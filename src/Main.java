import chatting.*;
import loginregisterserver.*;
public class Main {
    public static void main(String[] args) {
        manager login = new manager();
        chating_server chat = new chating_server();
        System.out.println("Hello world!");
        login.run();
        chat.run();
    }
}