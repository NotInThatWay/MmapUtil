package mmaputil.read.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 根据时间跨度读取
 */
public class TimeSpan extends ReadRule {
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
     * 时间间隔
     */
    private long interval;

    /**
     * 根据时间划分读取的构建函数
     *
     * @param timeFormat 时间格式
     * @param index      每个文件的起点读取位置
     * @param count      每个文件的对象读取个数
     * @param start      起始时间
     * @param end        截止时间
     * @param interval   时间间隔
     * @throws Exception 文件格式不合法或起始时间在结束时间之后
     */
    public TimeSpan(String timeFormat, int index, int count, Date start, Date end, long interval) throws Exception {
        super(new ConcurrentLinkedQueue<>(), index, count);
        if (!isFileNameValid(timeFormat)) throw new Exception("文件格式不合法");
        if (start.after(end)) throw new Exception("起始时间在截止时间之后");
        simpleDateFormat = new SimpleDateFormat(timeFormat);
        this.start = start.getTime();
        this.end = end.getTime();
        this.interval = interval;
        read();
    }

    /**
     * 据时间划分读取的构建函数，默认读取每个文件中的所有对象
     *
     * @param timeFormat 时间格式
     * @param start      起始时间
     * @param end        截止时间
     * @param interval   时间间隔
     * @throws Exception 文件格式不合法或起始时间在结束时间之后
     */
    public TimeSpan(String timeFormat, Date start, Date end, long interval) throws Exception {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
        if (!isFileNameValid(timeFormat)) throw new Exception("文件格式不合法");
        if (start.after(end)) throw new Exception("起始时间在截止时间之后");
        simpleDateFormat = new SimpleDateFormat(timeFormat);
        this.start = start.getTime();
        this.end = end.getTime();
        this.interval = interval;
        read();
    }


    /**
     * 读取符合规则的对象
     */
    @Override
    public void read() {
        names.clear();
        for (long c = start; c <= end; c += interval) {
            names.offer(simpleDateFormat.format(c));
        }
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
     * 设置时间间隔
     *
     * @param interval 时间间隔
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * 获取时间间隔
     *
     * @return
     */
    public long getInterval() {
        return interval;
    }

    /**
     * 设置日期格式
     *
     * @param simpleDateFormat 日期格式
     */
    public void setSimpleDateFormat(String simpleDateFormat) {
        this.simpleDateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    /**
     * 获取日期格式
     *
     * @return 日期格式
     */
    public String getSimpleDateFormat() {
        return simpleDateFormat.toString();
    }

    /**
     * 判断文件名是否合法
     *
     * @param name 文件名
     * @return True 为合法
     */
    private boolean isFileNameValid(String name) {
        return name.matches("^[a-zA-Z0-9._ -]+");
    }
}
