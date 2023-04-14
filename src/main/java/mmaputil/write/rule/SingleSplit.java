package mmaputil.write.rule;

/**
 * 将对象写入一个文件中
 */
public class SingleSplit extends SplitRule {
    /**
     * 要写入的文件名称
     */
    private String fileName;

    /**
     * 将对象全部写入一个文件中的构建函数
     *
     * @param name 文件名称
     */
    public SingleSplit(String name) {
        this.fileName = name;
    }

    /**
     * 执行划分
     *
     * @param object 要划分的对象
     */
    @Override
    public void split(Object object) {
        name = fileName;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件名称
     *
     * @return 文件名称
     */
    public String getFileName() {
        return fileName;
    }
}
