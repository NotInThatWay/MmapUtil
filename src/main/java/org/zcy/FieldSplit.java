package org.zcy;

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
    public void split(Object object) {
        try {
            name = object.getClass().getDeclaredField(fieldName).get(object).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回划分完成的文件命名
     *
     * @param object 要划分的对象
     * @return 文件命名
     */
    @Override
    public String getName(Object object) {
        split(object);
        return name;
    }
}
