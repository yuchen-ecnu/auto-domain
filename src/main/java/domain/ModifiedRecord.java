package domain;

/**
 * @author Michael Chen
 * @date 2018-12-25 11:52
 */
public class ModifiedRecord {
    public Integer code;
    public String message;
    public String codeDesc;
    public RecordList.Data data;

    class DataWrapper {
        public Long id;
        public String name;
        public String value;
        public String status;
        public String weight;
    }

}
