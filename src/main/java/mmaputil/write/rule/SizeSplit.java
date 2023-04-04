package mmaputil.write.rule;

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

    public int getSize() {
        return size;
    }

    public boolean isDowncast() {
        return downcast;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setDowncast(boolean downcast) {
        this.downcast = downcast;
    }
}
