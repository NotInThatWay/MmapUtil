package mmaputil.read.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimeSpan extends ReadRule {
    private SimpleDateFormat simpleDateFormat;
    private long start;
    private long end;
    private long interval;

    public TimeSpan(String timeFormat, int num, Date start, Date end, long interval) throws Exception {
        super(new ConcurrentLinkedQueue<>(), num);
        if (!isFileNameValid(timeFormat)) throw new Exception("文件格式不合法");
        if (start.after(end)) throw new Exception("起始时间在截止时间之后");
        simpleDateFormat = new SimpleDateFormat(timeFormat);
        this.start = start.getTime();
        this.end = end.getTime();
        this.interval = interval;
    }
    public TimeSpan(String timeFormat, Date start, Date end, long interval) throws Exception {
        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
        if (!isFileNameValid(timeFormat)) throw new Exception("文件格式不合法");
        if (start.after(end)) throw new Exception("起始时间在截止时间之后");
        simpleDateFormat = new SimpleDateFormat(timeFormat);
        this.start = start.getTime();
        this.end = end.getTime();
        this.interval = interval;
    }


    @Override
    public void read() {
        names.clear();
        for (long c = start; c <= end; c += interval) {
            names.offer(simpleDateFormat.format(c));
        }
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return interval;
    }

    public void setSimpleDateFormat(String simpleDateFormat) {
        this.simpleDateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    public String getSimpleDateFormat() {
        return simpleDateFormat.toString();
    }

    private boolean isFileNameValid(String name) {
        return name.matches("^[a-zA-Z0-9._ -]+");
    }
}
