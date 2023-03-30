package org.zcy.write.rule;

/**
 * 文件划分规则的抽象类
 */
public abstract class SplitRule {
    /**
     * 划分后的文件命名
     */
    public String name;

    /**
     * 返回划分完的文件命名
     *
     * @param object 要划分的对象
     * @return 文件命名
     */
    public abstract String getName(Object object);
}
