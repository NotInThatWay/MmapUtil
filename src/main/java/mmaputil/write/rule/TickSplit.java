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

    private String timestampToDay(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(timestamp);
    }

    public String getCode() {
        return code;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }
}
