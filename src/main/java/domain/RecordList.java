package domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * 解析记录列表返回
 *
 * @author Michael Chen
 * @date 2018-12-25 12:08
 */
public class RecordList {

    public Integer code;
    public String message;
    public String codeDesc;
    public Data data;

    public static class Data {
        public Domain domain;
        public Info info;
        public List<Record> records;

        public Data() {
        }
    }

    public static class Domain {
        public String id;
        public String name;
        public String punycode;
        public String grade;
        public String owner;
        public String ext_status;
        public Integer ttl;
        public Integer min_ttl;
        public List<String> dnspod_ns;
        public String status;
        public Integer q_project_id;
    }

    public static class Info {
        public String sub_domains;
        public String record_total;
        public String records_num;
    }

    public static class Record {
        public long id;
        public long ttl;
        public String value;
        public Integer enabled;
        public String status;
        public Timestamp updated_on;
        public Integer q_project_id;
        public String name;
        public String line;
        public String line_id;
        public String type;
        public String remark;
        public Integer mx;
        public String hold;

        @Override
        public String toString() {
            return "Record{" +
                    "id=" + id +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
