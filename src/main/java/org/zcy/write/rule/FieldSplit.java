package org.zcy.write.rule;

/**
 * 通过对象类的字段进行划分
 */
public class FieldSplit extends SplitRule {
    /**
     * 要划分的对象字段
     */
    private String fieldName;

    public FieldSplit(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 更新划分完的文件命名
     *
     * @param object 要划分的对象
     */
    @Override
    public void split(Object object) {
        try {
            name = object.getClass().getDeclaredField(fieldName).get(object).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
