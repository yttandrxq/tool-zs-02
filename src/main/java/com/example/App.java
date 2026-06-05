package com.example;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取当前IP地址的工具类
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println("正在获取本地IP地址...");
            String ipAddress = getLocalIpAddress();
            System.out.println("当前本地IP地址: " + ipAddress);
        } catch (Exception e) {
            System.err.println("获取IP地址时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取本地非回环IP地址
     * @return 本地IP地址字符串
     * @throws SocketException 如果网络接口访问出错
     */
    public static String getLocalIpAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            
            // 跳过回环接口和未启用的接口
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }
            
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                
                // 只返回IPv4地址
                if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(':') == -1) {
                    return address.getHostAddress();
                }
            }
        }
        
        return "127.0.0.1"; // 默认回环地址
    }
}
