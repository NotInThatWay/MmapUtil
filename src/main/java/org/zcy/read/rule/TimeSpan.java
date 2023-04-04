package org.zcy.read.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimeSpan extends ReadRule {
    private SimpleDateFormat simpleDateFormat;
    private long start;
    private long end;
    private long interval;

    public TimeSpan(String timeFormat, int num, Date start, Date end, long interval) {
        super(new ConcurrentLinkedQueue<>(), num);
        simpleDateFormat = new SimpleDateFormat(timeFormat);
        this.start = start.getTime();
        this.end = end.getTime();
        this.interval = interval;
    }


    @Override
    public void read() {
        if (start < end) reverse = false;
        for (long c = start; c <= end; c += interval) {
            names.offer(simpleDateFormat.format(c));
        }
    }
}
