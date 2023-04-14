package mmaputil.write.rule;

import java.text.SimpleDateFormat;

/**
 * 根据时间划分文件
 */
public class TimeSplit extends SplitRule {
    /**
     * 日期的格式
     */
    private SimpleDateFormat simpleDateFormat;

    /**
     * 日期划分文件的构造器
     *
     * @param timeFormat 用来命名文件的日期格式
     * @throws Exception 命名包含非法字符
     */
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

    /**
     * 获取当前的时间格式
     *
     * @return 用来命名划分文件的时间格式
     */
    public String getFormat() {
        return simpleDateFormat.toString();
    }

    /**
     * 设置时间格式
     *
     * @param simpleDateFormat 时间格式
     */
    public void setFormat(String simpleDateFormat) {
        this.simpleDateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    /**
     * 判断文件命名是否合法
     *
     * @param name 要判断的文件名
     * @return 是否合法
     */
    private boolean isFileNameValid(String name) {
        return name.matches("^[a-zA-Z0-9._ -]+");
    }
}
