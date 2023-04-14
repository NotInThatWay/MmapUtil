package mmaputil.write.rule;

/**
 * 文件划分规则的抽象类
 */
public abstract class SplitRule {
    /**
     * 划分后的文件命名
     */
    public String name;

    /**
     * 将当前对象进行划分
     *
     * @param object 要划分的对象
     */
    public abstract void split(Object object);

    /**
     * 获得划分后的文件命名
     *
     * @return 文件命名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文件划分名称
     *
     * @param name 文件名
     */
    public void setName(String name) {
        this.name = name;
    }
}
