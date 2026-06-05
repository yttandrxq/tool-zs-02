package com.example;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.io.*;

/**
 * 获取当前IP地址的工具类
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println("测试正在获取本地IP地址...");
            String ipAddress = getLocalIpAddress();
            System.out.println("当前本地IP地址: " + ipAddress);
            
            // 获取当天天气
            System.out.println("\n正在获取当天天气...");
            String weather = getWeather("Beijing");
            System.out.println("当前天气: " + weather);
        } catch (Exception e) {
            System.err.println("程序执行出错: " + e.getMessage());
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
    
    /**
     * 获取指定城市的当天天气信息
     * 使用 wttr.in 天气服务API，返回简洁的天气文本
     * 
     * @param city 城市名称（英文，如 "Beijing"、"Shanghai"）
     * @return 当天的天气描述字符串，例如 "Beijing: ☀️  +26°C"
     * @throws IOException 如果网络请求失败或读取响应出错
     */
    public static String getWeather(String city) throws IOException {
        // 构建API URL，使用format=3获取简洁文本格式
        String url = "http://wttr.in/" + city + "?format=3";
        
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // 创建HTTP连接
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5秒连接超时
            connection.setReadTimeout(5000);    // 5秒读取超时
            
            // 设置User-Agent，避免被服务器拒绝
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("天气API请求失败，HTTP状态码: " + responseCode);
            }
            
            // 读取响应内容
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            // 返回天气信息，去除首尾空格
            return response.toString().trim();
        } finally {
            // 关闭资源
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
