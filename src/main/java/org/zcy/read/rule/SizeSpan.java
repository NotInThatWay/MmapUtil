package org.zcy.read.rule;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SizeSpan extends ReadRule {
    private int interval;
    private int lowerBound;
    private int upperBound;
    /**
     * 构造函数
     * @param interval 大小间隔
     * @param lowerBound 最小值
     * @param upperBound 最大值
     * @param num 每个文件读取对象的个数
     */
    public SizeSpan(int interval, int lowerBound, int upperBound, int num) throws Exception {
        super(new ConcurrentLinkedQueue<>(), num);
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public SizeSpan(int interval, int lowerBound, int upperBound) throws Exception {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
        if (interval <= 0) throw new Exception("间隔应大于0");
        this.interval = interval;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void setNum(int num) {
        super.num = num;
    }

    @Override
    public void read(){
        for (int i = lowerBound; i <= upperBound; i += interval) {
            names.offer(Integer.toString(i));
        }
    }
}
