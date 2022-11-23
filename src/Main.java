import loginregisterserver.*;
import chatting.*;
public class Main {
    public static void main(String[] args) {
        manager login = new manager();
        chating_server chat = new chating_server();
        System.out.println("Hello world!");
        login.run();
        chat.run();

    }
}