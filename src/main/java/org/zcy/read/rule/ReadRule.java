package org.zcy.read.rule;

import java.util.List;

public abstract class ReadRule {
    public List<String> names;
    public int num;
    public boolean reverse;

    public ReadRule(List<String> names, int num, boolean reverse){
        this.names = names;
        this.num = num;
        this.reverse = reverse;
    }


    public abstract List<String> getNames();

    /**
     * 读取每个划分的对象个数
     * @return
     */
    public abstract int getNum();

    public abstract boolean isReverse();

}
