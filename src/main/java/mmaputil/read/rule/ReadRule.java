package mmaputil.read.rule;

import java.util.Queue;

public abstract class ReadRule {
    /**
     * 要读取的所有文件名字
     */
    public Queue<String> names;
    /**
     * 每个文件的起点读取位置
     */
    public int index;
    /**
     * 每个文件要读取的数据量
     */
    public int count;

    /**
     * 读取规则的构造函数
     *
     * @param names 划分后的文件名称
     * @param index 每个文件的起点读取位置
     * @param count 每个文件要读取的数据量
     */
    public ReadRule(Queue<String> names, int index, int count) {
        this.names = names;
        this.count = count;
        this.index = index;
    }

    /**
     * 读取规则的构造函数，从头开始读取
     *
     * @param names 划分后的文件名称
     * @param count 每个文件要读取的数据量
     */
    public ReadRule(Queue<String> names, int count) {
        this.names = names;
        this.count = count;
        this.index = 0;
    }

    /**
     * 读取符合规则的对象
     */
    public abstract void read();

    /**
     * 获取所有要读取的文件的名称
     *
     * @return 文件名称
     */
    public Queue<String> getNames() {
        return names;
    }

    /**
     * 获取在每个划分文件中，要读取对象的数量
     *
     * @return 对象的数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置读取的起点位置
     *
     * @param index 起点位置
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 获取读取的起点位置
     *
     * @return 起点位置
     */
    public int getIndex() {
        return index;
    }

    /**
     * 设置读取的个数
     *
     * @param count 读取个数
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 设置要读取的文件名称
     *
     * @param names 文件名称
     */
    public void setNames(Queue<String> names) {
        this.names = names;
    }

    /**
     * 清除所有要读取的文件名称
     */
    public void clearNames() {
        names.clear();
    }

    /**
     * 添加要读取的文件名称
     *
     * @param name 文件名称
     */
    public void addName(String name) {
        names.offer(name);
    }
}
