package chatting;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class chating_server {


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
        public void run ()
        {
            try
            {
                while (true) //계속 새로운 클라이언트의 연결을 수락하고 새 소켓을 cLIENTtHREAD에 넘겨줌
                {
                    Socket socket = serverSocket.accept();  //클라이언트의 연결을 수락
                    System.out.println("    Thread " + count + " is started.");
                    chat_unit serverThread = new chat_unit(socket, count);
                    serverThread.start(); //새로운 클라이언트의 연결을 수락하고 새 소켓을 cLIENTtHREAD에 넘겨줌
                    count++;
                }
            } catch (IOException e)
            {
                System.out.println(e);
                System.out.println("    SERVER CLOSE    ");
            }
        }
    }

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try
        {   // 서버소켓을 생성, 25588 포트와 binding
            serverSocket = new ServerSocket(25588); // 생성자 내부에 bind()가 있고, bind() 내부에 listen() 있음
            ConnectThread connectThread = new ConnectThread(serverSocket); // 서버소켓을 connectThread에 넘겨줌
            connectThread.start(); // connectThread 시작


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }




    private static class chat_unit extends Thread
    {
        Socket socket;
        int id;


        //생성자를 통해 입력받은 소켓과 클라이언트(쓰레드)의 id를 저장
        chat_unit(Socket socket, int id)
        {
            this.socket = socket;
            this.id = id;
        }

        public String getServerDateTime(){
            String DateTime=null;
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm_ss");
            DateTime = now.format(formatter);
            return DateTime;

        }



        @Override
        public void run ()
        {
            protocol content = null;
            try {
                 ObjectInputStream ois= new ObjectInputStream(socket.getInputStream());
                 content= (protocol) ois.readObject();
                 if(content.getTypeofrequest()==1){

                 }else if(content.getTypeofrequest()==2){

                } else if (content.getTypeofrequest()==3){

                } else if (content.getTypeofrequest()==4) {

                }else{
                    System.out.println("잘못된 요청입니다.");
                }

                ois.close();
                 socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }//finally
        }
    }



//ClientList