package org.zcy.write.rule;

/**
 * 将对象写入一个文件中
 */
public class SingleSplit extends SplitRule {
    /**
     * 要写入的文件名称
     */
    private String fileName;

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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
