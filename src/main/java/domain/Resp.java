package domain;

/**
 * 返回实体封装
 *
 * @author Michael Chen
 * @date 2018-12-26 9:29
 */
public class Resp<T> {
    public Integer code;
    public String message;
    public String codeDesc;
    public T data;
}
