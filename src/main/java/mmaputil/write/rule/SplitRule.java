package mmaputil.write.rule;

/**
 * 文件划分规则的抽象类
 */
public abstract class SplitRule {
    /**
     * 划分后的文件命名
     */
    public String name;


    public abstract void split(Object object);


    /**
     * 获得划分后的文件命名
     *
     * @return 文件命名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
