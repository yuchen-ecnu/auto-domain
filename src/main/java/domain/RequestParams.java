package domain;

/**
 * 请求参数集合
 *
 * @author Michael Chen
 * @date 2018-12-26 12:20
 */
public class RequestParams {

    private String secretId;
    private String region;
    private String secretKey;
    private String domain;
    private String ip;
    private String signatureMethod;
    private String record;
    private String timeChecked;
    private String timeUpdated;
    private String status;
    private String errorMsg;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(final String secretId) {
        this.secretId = secretId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(final String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(final String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "secretId='" + secretId + '\'' +
                ", region='" + region + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", domain='" + domain + '\'' +
                ", ip='" + ip + '\'' +
                ", signatureMethod='" + signatureMethod + '\'' +
                ", record='" + record + '\'' +
                '}';
    }

    public String getTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(final String timeChecked) {
        this.timeChecked = timeChecked;
    }

    public String getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(final String timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
