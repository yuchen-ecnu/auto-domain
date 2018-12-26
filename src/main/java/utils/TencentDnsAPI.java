package utils;

import com.alibaba.fastjson.JSON;
import domain.ModifiedRecord;
import domain.RecordList;

import java.net.URLEncoder;
import java.util.*;

/**
 * 腾讯DNS相关API
 * <p>
 *
 * @author Michael Chen
 * @date 2018-12-26 13:07
 */
public class TencentDnsAPI {
    private static String URL_RECORD_LIST = "cns.api.qcloud.com/v2/index.php";

    private String secretId;
    private String secretKey;
    private String signatureMethod;
    private Random rand = new Random();

    public TencentDnsAPI(String secretId, String secretKey, String signatureMethod) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.signatureMethod = signatureMethod;
    }

    /**
     * 获取指定Region下的指定域名的解析记录列表
     *
     * @param region 域
     * @param domain 域名字符串，不含www
     */
    public List<RecordList.Record> getRecordList(String region, String domain) {
        try {
            HashMap<String, String> listParams = new HashMap<>();
            listParams.put("Action", "RecordList");
            listParams.put("offset", "0");
            listParams.put("length", "20");
            listParams.put("domain", domain);
            listParams.put("Region", region);
            listParams.put("SecretId", secretId);
            listParams.put("signatureMethod", signatureMethod);
            String paramString = generateSignedParamString(URL_RECORD_LIST, listParams);
            String s = HttpRequest.sendGet("https://cns.api.qcloud.com/v2/index.php", paramString);
            return JSON.parseObject(s, RecordList.class).data.records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改指定ID的解析记录
     */
    public ModifiedRecord modifyRecord(String region, String domain, String recordId, String subDomain, String recordType, String recordLine, String value) {
        try {
            HashMap<String, String> modifyParams = new HashMap<>();
            modifyParams.put("Action", "RecordModify");
            modifyParams.put("domain", domain);
            modifyParams.put("recordId", recordId);
            modifyParams.put("subDomain", subDomain);
            modifyParams.put("recordType", recordType);
            modifyParams.put("recordLine", recordLine);
            modifyParams.put("value", value);
            modifyParams.put("SecretId", secretId);
            modifyParams.put("signatureMethod", signatureMethod);
            modifyParams.put("Region", region);

            String paramString = generateSignedParamString(URL_RECORD_LIST, modifyParams);
            String s = HttpRequest.sendGet("https://cns.api.qcloud.com/v2/index.php", paramString);
            return JSON.parseObject(s, ModifiedRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成签名后的请求参数字符串
     *
     * @param url
     * @param params
     */
    private String generateSignedParamString(String url, HashMap<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("Timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("Nonce", String.valueOf(rand.nextInt(10000) + 1));
        List<Map.Entry<String, String>> paramList = new ArrayList<>(params.entrySet());
        paramList.sort(Comparator.comparing(Map.Entry::getKey));

        StringBuilder param = new StringBuilder();
        for (Map.Entry<String, String> item : paramList) {
            param.append(item.getKey()).append("=").append(item.getValue()).append("&");
        }

        // 生成签名串
        String srcString = "GET" + url + "?" + param.toString();
        if (srcString.charAt(srcString.length() - 1) == '&') {
            srcString = srcString.substring(0, srcString.length() - 1);
        }
        String signStr = HMACSHA.encrytSHA1(srcString, secretKey);
        signStr = URLEncoder.encode(signStr, "UTF-8");
        param.append("Signature").append("=").append(signStr);
        return param.toString();
    }
}
