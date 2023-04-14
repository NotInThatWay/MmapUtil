package mmaputil.read.rule;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 根据大小划分读取
 */
public class SizeSpan extends ReadRule {
    /**
     * 大小间隔
     */
    private int interval;
    /**
     * 最小值
     */
    private int lowerBound;
    /**
     * 最大值
     */
    private int upperBound;

    /**
     * 通过文件大小划分读取的构造函数
     *
     * @param interval   大小间隔
     * @param lowerBound 最小值
     * @param upperBound 最大值
     * @param index      起始位置
     * @param count      每个文件读取对象的个数
     * @throws Exception 间隔小于等于 0 或创建失败
     */
    public SizeSpan(int interval, int lowerBound, int upperBound, int index, int count) throws Exception {
        super(new ConcurrentLinkedQueue<>(), index, count);
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        read();
    }

    /**
     * 通过文件大小划分读取的构造函数，默认获取符合条件的所有对象
     *
     * @param interval   大小间隔
     * @param lowerBound 最小值
     * @param upperBound 最大值
     * @throws Exception 间隔小于等于 0 或创建失败
     */
    public SizeSpan(int interval, int lowerBound, int upperBound) throws Exception {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        read();
    }

    /**
     * 读取符合规则的对象
     */
    @Override
    public void read() {
        names.clear();
        for (int i = lowerBound; i <= upperBound; i += interval) {
            names.offer(Integer.toString(i));
        }
    }

    /**
     * 设置大小间隔
     *
     * @param interval 大小间隔
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 获取大小间隔
     *
     * @return 大小间隔
     */
    public int getInterval() {
        return interval;
    }

    /**
     * 获取最小值
     *
     * @return 最小值
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
     * 获取最大值
     *
     * @return 最大值
     */
    public int getUpperBound() {
        return upperBound;
    }

    /**
     * 设置最小值
     *
     * @param lowerBound 最小值
     */
    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * 设置最大值
     *
     * @param upperBound 最大值
     */
    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

}
