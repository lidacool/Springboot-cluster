package com.lee.util.net;

import com.lee.util.string.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class NetUtil {
    private static final String[] bi_headers = new String[]{"udid", "client_version", "phone_type", "phone_version", "ratio", "service", "download_from", "network_type", "os_type"};

    public NetUtil() {
    }

    public static long findClientIP() {
        String ipStr = findClientIPStr();
        return findAndconvertIpToInt(ipStr);
    }

    public static long findServerIP() {
        String ipStr = findServerIPStr();
        return ipStr != null ? findAndconvertIpToInt(ipStr) : 0L;
    }

    public static long findServerIP2() {
        String ipStr = findServerIPStr2();
        return ipStr != null ? findAndconvertIpToInt(ipStr) : 0L;
    }

    public static String findServerIPStr4() {
        String ipStr = findServerIPStr();
        return ipStr != null ? ipStr : "";
    }

    public static long findServerIP3() {
        String ipStr = findServerIPStr3();
        return ipStr != null ? findAndconvertIpToInt(ipStr) : 0L;
    }

    private static long findAndconvertIpToInt(String uiClientIP) {
        long ip = 0L;
        if (uiClientIP != null && uiClientIP.length() >= 7) {
            String[] ips = uiClientIP.split("\\.");
            ip += Long.parseLong(ips[0]) << 24;
            ip += Long.parseLong(ips[1]) << 16;
            ip += Long.parseLong(ips[2]) << 8;
            ip += Long.parseLong(ips[3]);
            return ip;
        } else {
            return ip;
        }
    }

    public static String findClientIPStr() {

         final ThreadLocal<HttpServletRequest> requests = new ThreadLocal();
        HttpServletRequest request = requests.get();

        request = request==null ? null : request;

        if (request == null) {
            return "127.0.0.1";
        } else {
            String ip = request.getHeader("X-Real-IP");
            if (StringUtil.isEmpty(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }

            if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            String[] ips = ip.split(",");
            ip = ips[ips.length - 1].trim();
            return ip;
        }
    }

    public static String findServerIPStr() {
        String ip = null;

        try {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

        return ip;
    }

    private static String findServerIPStr2() {
        InetAddress ip = null;

        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();

            while(netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                ip = (InetAddress)ni.getInetAddresses().nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    return ip.getHostAddress();
                }
            }
        } catch (SocketException var3) {
            var3.printStackTrace();
        }

        return null;
    }

    private static String findServerIPStr3() {
        String ip = null;
        BufferedReader bufferedReader = null;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            boolean var4 = true;

            while((line = bufferedReader.readLine()) != null) {
                int index = line.toLowerCase().indexOf("inet addr");
                if (index >= 0) {
                    ip = findFirstIP(line);
                    break;
                }
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            bufferedReader = null;
            process = null;
        }

        return ip;
    }

    private static String findFirstIP(String info) {
        String ip = null;
        String regEx = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(info);
        if (m.find()) {
            ip = m.group();
        }

        return ip;
    }

    public static String parseServerIp() {
        String ipAddr = null;

        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();

            while(netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();

                while(address.hasMoreElements()) {
                    InetAddress ip = (InetAddress)address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        ipAddr = ip.getHostAddress();
                        return ipAddr;
                    }

                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        ipAddr = ip.getHostAddress();
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return ipAddr;
    }
}

