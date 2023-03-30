package org.zcy.write.rule;

/**
 * 根据对象大小划分
 */
public class SizeSplit extends SplitRule {
    /**
     * 大小的间隔，最小为 1
     */
    private int interval;
    /**
     * 对象的大小
     */
    private int size;
    /**
     * 是否向下取间隔，默认为 True
     */
    private boolean downcast;

    public SizeSplit(int interval) throws Exception {
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.size = -1;
        this.downcast = true;
    }

    public SizeSplit(int interval, boolean downcast) throws Exception {
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.size = -1;
        this.downcast = downcast;
    }

    /**
     * 更新对象的大小
     *
     * @param object 当前要划分的对象
     */
    public void updateValue(Object object) {
        size = cast(object.toString().length());
    }

    /**
     * 取间隔
     *
     * @param value 要去间隔的对象大小
     * @return 取完间隔的对象大小
     */
    private int cast(int value) {
        int result = value;
        if (downcast) {
            while ((result % interval) != 0) {
                result--;
            }
        } else {
            while ((result % interval) != 0) {
                result++;
            }
        }
        return result;
    }

    /**
     * 返回划分完的文件命名
     *
     * @param object 要划分的对象
     * @return 文件命名
     */
    @Override
    public String getName(Object object) {
        updateValue(object);
        return Integer.toString(size);
    }

}
