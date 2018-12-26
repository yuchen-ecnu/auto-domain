package utils;

/**
 * IP工具类
 *
 * @author Michael Chen
 * @date 2018-12-26 12:58
 */
public class IpUtils {
    public static String getIp() throws Exception {
        return HttpRequest.sendGet("https://api.ipify.org/?format=json", null);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getIp());
    }
}
