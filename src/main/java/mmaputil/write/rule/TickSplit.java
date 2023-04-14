package mmaputil.write.rule;

import java.text.SimpleDateFormat;

/**
 * 通过对象类的字段进行划分
 */
public class TickSplit extends SplitRule {
    /**
     * 要划分的对象字段
     */
    private String code;
    /**
     * 当前 Tick 的时间信息
     */
    private String time;

    /**
     * 更新划分完的文件命名
     *
     * @param object 要划分的对象
     */
    @Override
    public void split(Object object) {
        try {
            code = object.getClass().getDeclaredField("code").get(object).toString();
            time = timestampToDay(Long.parseLong(object.getClass().getDeclaredField("time").get(object).toString()));
            name = code + "//" + time;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将时间转换成年月日
     *
     * @param timestamp 要转换的时间戳，单位毫秒
     * @return 年月日格式的时间字符串
     */
    private String timestampToDay(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 获取股票代码
     *
     * @return 股票代码
     */
    public String getCode() {
        return code;
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
     * 设置时间
     *
     * @param time 时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 获取时间
     *
     * @return 时间
     */
    public String getTime() {
        return time;
    }
}
