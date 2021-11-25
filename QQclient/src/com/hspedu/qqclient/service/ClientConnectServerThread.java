package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{

    private Socket socket;

    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println("客户端线程等待，等待读取从服务器端发送的消息....");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message =(Message) ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n===================当前在线用户列表======================");
                    for (int i = 0; i < onlineUsers.length; i++){
                        System.out.println("                用户：" + onlineUsers[i]);
                    }
                    System.out.println("======================================================");
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println("\n" + message.getSender() + "对" + message.getGetter() + "说："
                    + message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    System.out.println("\n" + message.getSender() + "对大家说：" + message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n" + message.getSender() + "给" + message.getGetter()
                    + "发文件：" + message.getSrc() + " 到我的电脑的目录" + message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n" + "保存文件成功！");
                }else {
                    System.out.println("是其他类型的message，暂时不处理..... ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket(){
        return socket;
    }

}
