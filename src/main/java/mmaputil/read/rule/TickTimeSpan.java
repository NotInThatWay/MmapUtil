package mmaputil.read.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 根据 Tick 时间读取
 */
public class TickTimeSpan extends ReadRule {
    /**
     * 时间格式
     */
    private SimpleDateFormat simpleDateFormat;
    /**
     * 起始时间
     */
    private long start;
    /**
     * 截止时间
     */
    private long end;
    /**
     * 股票代码
     */
    private String code;

    /**
     * 根据 Tick 时间读取的构建函数
     *
     * @param count 读取的个数
     * @param start 起始时间
     * @param end   截止时间
     * @param code  股票代码
     * @throws Exception 起始时间在截止时间之后
     */
    public TickTimeSpan(int count, Date start, Date end, String code) throws Exception {
        super(new ConcurrentLinkedQueue<>(), count);

        if (start.after(end)) throw new Exception("起始时间在截止时间之后");

        this.start = start.getTime();
        this.end = end.getTime();
        this.code = code;
    }

    /**
     * 根据 Tick 时间读取所有对象的构建函数
     *
     * @param start 起始时间
     * @param end   截止时间
     * @param code  股票代码
     * @throws Exception 起始时间在截止时间之后
     */
    public TickTimeSpan(Date start, Date end, String code) throws Exception {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);

        if (start.after(end)) throw new Exception("起始时间在截止时间之后");

        this.start = start.getTime();
        this.end = end.getTime();
        this.code = code;
    }

    /**
     * 读取符合规则的对象
     */
    @Override
    public void read() {
        names.clear();
        for (long time = start; time <= end; time += 86400000) {
            names.offer(code + "//" + timestampToDay(time));
        }
    }

    /**
     * 将时间转换成年月日格式
     *
     * @param timestamp 时间戳，单位毫秒
     * @return 年月日格式的时间字符串
     */
    private String timestampToDay(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 设置起始时间
     *
     * @param start 起始时间
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * 获取起始时间
     *
     * @return 起始时间
     */
    public long getStart() {
        return start;
    }

    /**
     * 获取截止时间
     *
     * @return 截止时间
     */
    public long getEnd() {
        return end;
    }

    /**
     * 设置截止时间
     *
     * @param end 截止时间
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * 设置股票代码
     *
     * @param code 股票代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取股票代码
     *
     * @return 股票代码
     */
    public String getCode() {
        return code;
    }
}
