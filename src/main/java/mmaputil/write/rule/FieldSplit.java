package mmaputil.write.rule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过对象类的字段进行划分
 */
public class FieldSplit extends SplitRule {
    /**
     * 要划分的对象字段
     */
    private String fieldName;

    /**
     * 根据对象的字段进行划分的构建函数
     *
     * @param fieldName 字段名称
     */
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
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() +
                    fieldName.substring(1).toLowerCase();
            Method m = object.getClass().getDeclaredMethod(methodName);
            m.setAccessible(true);
            name = (String) m.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
     * 设置要划分的字段名称
     *
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
