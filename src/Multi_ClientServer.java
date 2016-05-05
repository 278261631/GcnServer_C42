//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class SocketClient {
//    public static void main(String[] args) {
//        try {
//            Socket socket = new Socket("127.0.0.1", 2013);
//            socket.setSoTimeout(60000);
//
//            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            
//            String result = "";
//            while(result.indexOf("bye") == -1){
//                BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));
//                printWriter.println(sysBuff.readLine());
//                printWriter.flush();
//                
//                result = bufferedReader.readLine();
//                System.out.println("Server say : " + result);
//            }
//
//            printWriter.close();
//            bufferedReader.close();
//            socket.close();
//        } catch (Exception e) {
//            System.out.println("Exception:" + e);
//        }
//    }
//}
//　　服务器端程序：
//
//
//
//import java.io.*;
//import java.net.*;
//
//public class Server extends ServerSocket {
//    private static final int SERVER_PORT = 2013;
//
//    public Server() throws IOException {
//        super(SERVER_PORT);
//
//        try {
//            while (true) {
//                Socket socket = accept();
//                new CreateServerThread(socket);//当有请求时，启一个线程处理
//            }
//        } catch (IOException e) {
//        } finally {
//            close();
//        }
//    }
//
//    //线程类
//    class CreateServerThread extends Thread {
//        private Socket client;
//        private BufferedReader bufferedReader;
//        private PrintWriter printWriter;
//
//        public CreateServerThread(Socket s) throws IOException {
//            client = s;
//
//            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            
//            printWriter = new PrintWriter(client.getOutputStream(), true);
//            System.out.println("Client(" + getName() + ") come in...");
//            
//            start();
//        }
//
//        public void run() {
//            try {
//                String line = bufferedReader.readLine();
//
//                while (!line.equals("bye")) {
//                    printWriter.println("continue, Client(" + getName() + ")!");
//                    line = bufferedReader.readLine();
//                    System.out.println("Client(" + getName() + ") say: " + line);
//                }
//                printWriter.println("bye, Client(" + getName() + ")!");
//                
//                System.out.println("Client(" + getName() + ") exit!");
//                printWriter.close();
//                bufferedReader.close();
//                client.close();
//            } catch (IOException e) {
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        new Server();
//    }
//}