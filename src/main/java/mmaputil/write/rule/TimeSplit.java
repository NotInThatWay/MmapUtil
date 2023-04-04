package mmaputil.write.rule;

import java.text.SimpleDateFormat;

/**
 * 根据时间划分文件
 */
public class TimeSplit extends SplitRule {
    private SimpleDateFormat simpleDateFormat;

    public TimeSplit(String timeFormat) throws Exception {
        if (!isFileNameValid(timeFormat)) throw new Exception("命名不合法");
        this.simpleDateFormat = new SimpleDateFormat(timeFormat);
    }

    /**
     * 根据所提供的日期格式划分对象
     *
     * @param object 要划分的对象
     */
    @Override
    public void split(Object object) {
        name = simpleDateFormat.format(System.currentTimeMillis());
    }

    public String getFormat() {
        return simpleDateFormat.toString();
    }

    public void setFormat(String simpleDateFormat) {
        this.simpleDateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    private boolean isFileNameValid(String name) {
        return name.matches("^[a-zA-Z0-9._ -]+");
    }
}
