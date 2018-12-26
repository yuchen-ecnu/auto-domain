package utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * HMACSHA256
 *
 * @author Michael Chen
 * @date 2018-12-26 10:06
 */
public class HMACSHA {
    /**
     * @param content 加密内容
     * @param secret  加密key
     * @return
     */
    public static String encrytSHA1(String content, String secret) {
        try {
            byte[] hmacDigest = new byte[0];
            hmacDigest = HmacSha1(content, secret);
            byte[] signContent = new byte[hmacDigest.length];
            System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
            return Base64Encode(signContent);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String Base64Encode(byte[] binaryData) {
        String encodedstr = Base64.getEncoder().encodeToString(binaryData);
        return encodedstr;
    }

    public static byte[] HmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] HmacSha1Digest = mac.doFinal(binaryData);
        return HmacSha1Digest;
    }

    public static byte[] HmacSha1(String plainText, String key) throws Exception {
        return HmacSha1(plainText.getBytes(), key);
    }

    public static void main(String[] args) {
        String secretKey = "Gu5t9xGARNpq86cd98joQYCN3Cozk1qA";
        String srcStr = "GETcvm.api.qcloud.com/v2/index.php?Action=DescribeInstances&InstanceIds.0=ins-09dx96dg&Nonce=11886&Region=ap-guangzhou&SecretId=AKIDz8krbsJ5yKBZQpn74WFkmLPx3gnPhESA&SignatureMethod=HmacSHA1&Timestamp=1465185768";
        String s1 = encrytSHA1(srcStr, secretKey);
        System.out.println(s1);
    }
}
