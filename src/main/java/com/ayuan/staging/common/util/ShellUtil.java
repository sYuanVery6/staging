package com.ayuan.staging.common.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sYuan
 * @Description SSH远程服务器执行Shell命令
 */
public class ShellUtil {

    //sessionMap
    private static final Map<String,ShellSession> SHELL_SESSION_MAP = new HashMap<>();

    //session构造器
    private static final JSch jsch = new JSch();

    //超时时间
    private static final Integer TIME_OUT_HOUR = 1;

    //默认连接端口
    private static final Integer DEFAULT_PORT = 22;

    //默认类别
    private static final String DEFAULT_CHANNEL_TYPE = "exec";


    public static final String shellCommand(final String address,final String username,final String password,final String command){
        return ShellUtil.shellCommand(address, username, password, ShellUtil.DEFAULT_CHANNEL_TYPE,command);
    }
    public static final String shellCommand(final String address,final String username,final String password,final String channelType,final String command){
        return ShellUtil.shellCommand(address, username, password,ShellUtil.DEFAULT_PORT, channelType,command);
    }
    /**
     * @Description
     * 1、判断连接是否长时间未使用，超出时间则删除
     * 2、通过指定服务器ip 用户名密码 连接端口号 调用内部类ShellSession的execute方法执行channelType类别的command
     * @param address 服务器地址
     * @param username 用户名
     * @param password 密码
     * @param port 端口号
     * @param channelType 管道类型
     * @param command shell命令
     * @return java.lang.String
     * @author sYuan
     * @Date 2021/01/17
     */
    public static final String shellCommand(final String address,final String username,final String password,final Integer port,final String channelType,final String command){
        if (!ShellUtil.checkNotNull(address,username,password,port,channelType,command)){
            throw new NullPointerException("params cannot be null");
        }

        if (!ShellUtil.SHELL_SESSION_MAP.containsKey(address+":"+username)){
            ShellUtil.SHELL_SESSION_MAP.put(address + ":" + username, new ShellUtil.ShellSession(address, username, password, port));
        }

        ShellSession shellSession = ShellUtil.SHELL_SESSION_MAP.get(address+":"+username);
        shellSession.updateLastExecTime();//更新操作时间

        //清除超时session实例
        ShellUtil.SHELL_SESSION_MAP.entrySet().removeIf(
                entry -> entry.getValue().lastExecTime.getTime()+ShellUtil.TIME_OUT_HOUR*60*60*1000 < System.currentTimeMillis()
        );
        return shellSession==null?null:shellSession.execute(channelType, command);
    }

    private static final Boolean checkNotNull(Object... objects){
        if (objects==null){
            return false;
        }
        for (Object obj:objects){
            if(obj == null){
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 连接实例类
     * @author sYuan
     * @Date 2021/1/17 11:10
     */
    static class ShellSession{
        Session session;
        Date lastExecTime;

        /**
         * @Description 创建连接
         * @param address
         * @param username
         * @param password
         * @param port
         * @author sYuan
         * @Date 2021/1/17 0016 11:19
         */
        private ShellSession(String address, String username, String password, Integer port) {
            try {
                session = ShellUtil.jsch.getSession(username, address, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");//去掉连接确认的
                session.connect(30000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void updateLastExecTime(){
            lastExecTime = Calendar.getInstance().getTime();
        }
        /**
         * @Description 执行命令
         * @param channelType
         * @param command
         * @return java.lang.String
         * @author zjt
         * @Date 2019/8/16 0016 11:20
         */
        private String execute(String channelType,String command){
            if (this.session==null){
                return null;
            }
            Channel channel = null;
            //InputStream input = null;
            BufferedReader input = null;
            String resp = "";
            try {
                channel = this.session.openChannel(channelType);
                ((ChannelExec) channel).setCommand(command);
                input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                channel.connect();
                String line;
                while ((line = input.readLine()) != null) {
                    resp+=line+"\n";
                }
                if (resp!=null&&!resp.equals("")){
                    resp = resp.substring(0, resp.length()-1);
                }
                System.out.println(resp);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (input!=null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (channel!=null){
                    channel.disconnect();
                }
            }
            return resp;
        }
    }

}
