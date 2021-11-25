package com.hspedu.qqclient.view;

import com.hspedu.qqclient.service.FileClientService;
import com.hspedu.qqclient.service.MessageClientService;
import com.hspedu.qqclient.service.UserClientService;

import java.util.Scanner;

public class QQView {

    private boolean loop = true;//用于控制是否显示主菜单
    private String key = "";
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();
    private UserClientService userClientService = new UserClientService();//用于登录服务器
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统..........");
    }

    //显示主菜单
    private void mainMenu(){

        while (loop){
            System.out.println("========================欢迎登录网络通信系统==========================");
            System.out.println("\t\t 1.登录系统");
            System.out.println("\t\t 9.退出系统");
            System.out.println("==================================================================");
            System.out.println();
            System.out.print("请输入你的选择：");

            key = sc.nextLine();

            switch (key){
                case "1":

                    System.out.print("请输入用户名：");
                    String userId = sc.nextLine();
                    System.out.print("请输入密码：");
                    String pwd = sc.nextLine();

                    if (userClientService.checkUser(userId,pwd)){

                        System.out.println("===========================欢迎（用户" + userId + "）========================");
                        while (loop){
                            System.out.println("===============网络通信系统二级菜单（用户" + userId + "登录成功）=================");
                            System.out.println("\t\t 1.显示在线用户系统");
                            System.out.println("\t\t 2.群发消息");
                            System.out.println("\t\t 3.私聊消息");
                            System.out.println("\t\t 4.发送文件");
                            System.out.println("\t\t 9.退出系统");
                            System.out.println("=======================================================================");
                            System.out.println();
                            System.out.print("请输入你的选择：");
                            key = sc.nextLine();
                            switch (key){
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入想对大家说的话：");
                                    String s = sc.nextLine();
                                    messageClientService.sendMessageToAll(s,userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号（在线）：");
                                    String gettreId = sc.nextLine();
                                    System.out.print("请输入想说的话：");
                                    String content = sc.nextLine();
                                    messageClientService.sendMessageToOne(content,userId,gettreId);
                                    System.out.println();
                                    break;
                                case "4":
                                    System.out.print("请输入你想把文件发送给的用户（在线）");
                                    gettreId = sc.nextLine();
                                    System.out.print("请输入发送文件的路径（形式 d:\\xx.jpg）");
                                    String src = sc.nextLine();
                                    System.out.print("请输入把文件发送到对应的路径（形式 d:\\yy.jpg）");
                                    String dest = sc.nextLine();
                                    fileClientService.sendFileToOne(src,dest,userId,gettreId);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else {
                        System.out.println("===============登录失败===============");
                    }

                    break;
                case "9":
                    loop = false;
                    break;

            }

        }
    }

}
