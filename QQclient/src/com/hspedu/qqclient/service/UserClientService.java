package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {

    private User u = new User();
    private Socket socket;

    public boolean checkUser(String userId,String pwd){

        boolean b = false;
        u.setUserID(userId);
        u.setPasswd(pwd);

        try {

            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送user对象

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){

                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();

                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);
                b = true;

            }else{

                socket.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserID());
        try {
            ObjectOutputStream oos
                    = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserID()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserID());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserID() + "用户退出系统");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
