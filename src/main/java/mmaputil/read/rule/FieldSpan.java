package mmaputil.read.rule;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 根据字段名称读取
 */
public class FieldSpan extends ReadRule {
    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 根据字段名称读取的构造函数
     *
     * @param fieldName 字段名称
     * @param index     读取的起始位置
     * @param count     读取的个数
     */
    public FieldSpan(String fieldName, int index, int count) {
        super(new ConcurrentLinkedQueue<>(), index, count);
        this.fieldName = fieldName;
        read();
    }

    /**
     * 根据字段名称读取的构造函数，默认每个文件中，读取所有符合条件的对象
     *
     * @param fieldName 字段名称
     */
    public FieldSpan(String fieldName) {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
        this.fieldName = fieldName;
        read();
    }

    /**
     * 读取符合规则的对象
     */
    @Override
    public void read() {
        names.clear();
        names.offer(fieldName);
    }

    /**
     * 获取字段名称
     *
     * @return 字段名称
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置字段名称
     *
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
