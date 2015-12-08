package net.dkahn.starter.apps.webapps.common.utils.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * quirks to store in the thread the real ip of the user
 */
public class IPStorage {

    public static final Logger logger = LoggerFactory.getLogger(IPStorage.class);

    public static ThreadLocal<String> ip = new ThreadLocal<String>();

    public static String getIpAddress() {
        return ip.get();
    }


    public static void setLocation(String ipAddress) {
        ip.set(ipAddress);
    }

    public static void setLocation(HttpServletRequest request) {
        ip.set(getClientAddress(request));
    }

    public static void resetLocation() {
        ip.set(null);
    }

    public static String getClientAddress(HttpServletRequest request){
        String clientAddress;
        String xforwardedforHeader = request.getHeader("X-FORWARDED-FOR");
        if(xforwardedforHeader != null){
            int firstComa  = xforwardedforHeader.indexOf(',');
            clientAddress = firstComa>0?xforwardedforHeader.substring(0, firstComa):xforwardedforHeader;
        }else{
            logger.warn("This should only happen in developement");
            clientAddress = request.getRemoteAddr();
        }
        if(logger.isDebugEnabled()) {
            logger.debug(String.format("X-FORWARDED-FOR = %s, user ip = %s", xforwardedforHeader, clientAddress));
        }
        return clientAddress;
    }
}
