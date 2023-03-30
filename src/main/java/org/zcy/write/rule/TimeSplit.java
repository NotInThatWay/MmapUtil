package org.zcy.write.rule;

import java.text.SimpleDateFormat;

/**
 * 根据时间划分文件
 */
public class TimeSplit extends SplitRule {
    /**
     * 时间划分模式
     * 1 = 秒钟划分
     * 2 = 分钟划分
     * 3 = 小时划分
     * 4 = 日期划分
     */
    private int mode;

    public TimeSplit(int mode) {
        this.mode = mode;
    }

    /**
     * 根据不同模式，更新文件命名
     */
    private void split() {
        long currTime = System.currentTimeMillis();
        switch (mode) {
            case 1: // 秒钟划分
                name = timestampToSecond(currTime);
                break;
            case 2: // 分钟划分
                name = timestampToMinute(currTime);
                break;
            case 3: // 小时划分
                name = timestampToHour(currTime);
                break;
            default:    // 日期划分
                name = timestampToDay(currTime);
        }
    }


    /**
     * 返回划分完的文件命名
     *
     * @param object 要划分的对象
     * @return 文件命名
     */
    @Override
    public String getName(Object object) {
        split();
        return name;
    }

    /**
     * 获取当前时间划分的模式
     *
     * @return 时间划分的模式
     */
    public int getMode() {
        return mode;
    }

    /**
     * 设置时间划分的模式
     *
     * @param mode 模式
     */
    public void setMode(int mode) {
        this.mode = mode;
    }


    /**
     * 将时间戳转换为秒钟格式
     *
     * @param timestamp 时间戳
     * @return 秒钟格式时间字符串
     */
    private String timestampToSecond(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 将时间戳转换为分钟格式
     *
     * @param timestamp 时间戳
     * @return 分钟格式时间字符串
     */
    private String timestampToMinute(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mmZ");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 将时间戳转换为小时格式
     *
     * @param timestamp 时间戳
     * @return 小时格式时间字符串
     */
    private String timestampToHour(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHZ");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 将时间戳转换为日期格式
     *
     * @param timestamp 时间戳
     * @return 天格式时间字符串
     */
    private String timestampToDay(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddZ");
        return simpleDateFormat.format(timestamp);
    }

}
