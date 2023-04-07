//package mmaputil.read.rule;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//public class TickSpan extends ReadRule {
//    private String code;
//
//    public TickSpan(String code, int num) throws Exception {
//        super(new ConcurrentLinkedQueue<>(), num);
//        this.code = code;
//    }
//
//    public TickSpan(Date start, Date end, String code) throws Exception {
//        super(new ConcurrentLinkedQueue<>(), Integer.MAX_VALUE);
//
//        if (start.after(end)) throw new Exception("起始时间在截止时间之后");
//
//        this.start = start.getTime();
//        this.end = end.getTime();
//        this.code = code;
//    }
//
//
//    @Override
//    public void read() {
//        names.clear();
//        for (long time = start; time <= end; time += 86400000) {
//            names.offer(code + "//" + timestampToDay(time));
//        }
//    }
//
//    private String timestampToDay(long timestamp) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        return simpleDateFormat.format(timestamp);
//    }
//
//    public void setStart(long start) {
//        this.start = start;
//    }
//
//    public long getStart() {
//        return start;
//    }
//
//    public long getEnd() {
//        return end;
//    }
//
//    public void setEnd(long end) {
//        this.end = end;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getCode() {
//        return code;
//    }
//}
