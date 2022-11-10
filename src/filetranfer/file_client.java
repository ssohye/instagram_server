package filetranfer;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
public class file_client {
    DataOutputStream dataOutput = null;
    Socket socket = null;
    File file = null;        //파일에 정보를 얻기위한 File 클래스
    DataInputStream dataInput = null;
    BufferedOutputStream bufferedOutput = null; //output 속도 향상을 위한 BufferedOutputStream

    public static void main(String[] args){



        try{
            Socket socket = new Socket("localhost", 8080); //서버에 접속
            file_client(socket,"image.jpg");

        }catch(IOException e){
            e.printStackTrace();
        }

    }



}//ServerSend 클래스
