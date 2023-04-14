package mmaputil.write.rule;

/**
 * 根据对象大小划分
 */
public class SizeSplit extends SplitRule {
    /**
     * 间隔的大小，最小为 1
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

    /**
     * 根据对象大小划分的构建函数
     *
     * @param interval 划分间隔，最小为 1
     * @throws Exception 间隔小于等于 0
     */
    public SizeSplit(int interval) throws Exception {
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.size = -1;
        this.downcast = true;
    }

    /**
     * 根据对象大小划分的构建函数，可设置是否向下取间隔
     *
     * @param interval 间隔大小，最小为 1
     * @param downcast 是否向下取间隔
     * @throws Exception 间隔小于等于 0
     */
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
    private void updateValue(Object object) {
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
     * 执行文件划分
     *
     * @param object 要划分的对象
     */
    @Override
    public void split(Object object) {
        updateValue(object);
        name = Integer.toString(size);
    }

    /**
     * 获取对象大小
     *
     * @return 对象大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 是否为向下取间隔
     *
     * @return True 为是向下取间隔
     */
    public boolean isDowncast() {
        return downcast;
    }

    /**
     * 获取当前的间隔
     *
     * @return 间隔的值
     */
    public int getInterval() {
        return interval;
    }

    /**
     * 设置大小划分间隔
     *
     * @param interval 间隔的值
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 设置是否向下取间隔
     *
     * @param downcast True 为是向下取间隔
     */
    public void setDowncast(boolean downcast) {
        this.downcast = downcast;
    }
}
