package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {

    private ServerSocket ss = null;
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("菩提老祖",new User("菩提老祖","123456"));
    }

    private boolean checkUser(String userId,String pwd){

        User user = validUsers.get(userId);

        if (user == null){
            return false;
        }
        if (!user.getPasswd().equals(pwd)){
            return false;
        }
        return true;
    }

    public QQServer(){
        try {

            System.out.println("服务器在9999端口监听....");
            ss = new ServerSocket(9999);

            while (true){

                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();

                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                if (checkUser(u.getUserID(),u.getPasswd())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread
                            = new ServerConnectClientThread(socket, u.getUserID());
                    serverConnectClientThread.start();
                    ManageClientThreads.addClientThread(u.getUserID(),serverConnectClientThread);
                }else {
                    System.out.println("用户 id=" + u.getUserID() + "   pwd=" + u.getPasswd() + "验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
