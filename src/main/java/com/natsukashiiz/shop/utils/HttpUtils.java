package com.natsukashiiz.shop.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // If there are multiple IP addresses, take the first one.
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }

        return ipAddress;
    }

    public static String getDeviceName(String userAgent) {
        if (userAgent.toLowerCase().contains("iphone")) {
            return "iPhone";
        } else if (userAgent.toLowerCase().contains("ipad")) {
            return "iPad";
        } else if (userAgent.toLowerCase().contains("android")) {
            return "Android";
        } else if (userAgent.toLowerCase().contains("windows")) {
            return "Windows";
        } else if (userAgent.toLowerCase().contains("macintosh")) {
            return "Mac";
        } else {
            return "Unknown";
        }
    }

    public static String getOsName(String userAgent) {
        if (userAgent.toLowerCase().contains("windows nt 10.0")) {
            return "Windows 10";
        } else if (userAgent.toLowerCase().contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.toLowerCase().contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (userAgent.toLowerCase().contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (userAgent.toLowerCase().contains("mac os x")) {
            return "Mac OS X";
        } else if (userAgent.toLowerCase().contains("android")) {
            return "Android";
        } else if (userAgent.toLowerCase().contains("iphone os")) {
            return "iOS";
        } else if (userAgent.toLowerCase().contains("linux")) {
            return "Linux";
        } else {
            return "Unknown";
        }
    }
}
