package org.zcy.write.rule;

/**
 * 将对象写入一个文件中
 */
public class SingleSplit extends SplitRule {
    public SingleSplit(String name) {
        super.name = name;
    }

    /**
     * 返回划分完的文件命名
     *
     * @param object 要划分的对象
     * @return 划分后的文件命名
     */
    @Override
    public String getName(Object object) {
        return name;
    }
}
