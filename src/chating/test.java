package chating;

import java.util.Scanner;

public class test {
    public static void main(String[] args){
        try{
            chating_client client = new chating_client();
            client.chat_member_add("서지호");
            client.chat_member_add("김명선");
            client.chat_member_add("김의진");
            client.chat_create();

            Scanner keyboard = new Scanner(System.in);


        }
        catch(Exception e){

        }
    }
}
