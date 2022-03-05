package me.theseems.thrut.util

import liquibase.repackaged.org.apache.commons.lang3.StringUtils
import java.net.InetAddress
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

class HttpUtils {
    companion object {
        private const val LOCALHOST_IPV4 = "127.0.0.1"
        private const val LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1"

        fun getRequestIp(request: HttpServletRequest): String {
            var ipAddress = request.getHeader("X-Forwarded-For")
            if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("Proxy-Client-IP")
            }

            if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP")
            }

            if (StringUtils.isEmpty(ipAddress) || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.remoteAddr
                if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                    try {
                        val inetAddress = InetAddress.getLocalHost()
                        ipAddress = inetAddress.hostAddress
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                    }
                }
            }

            if (!StringUtils.isEmpty(ipAddress) &&
                ipAddress.length > 15 && ipAddress.indexOf(",") > 0
            ) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
            }

            return ipAddress
        }
    }
}
