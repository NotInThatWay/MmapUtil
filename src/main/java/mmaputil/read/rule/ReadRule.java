package mmaputil.read.rule;

import java.util.Queue;

public abstract class ReadRule {
    /**
     * 要读取的所有文件名字
     */
    public Queue<String> names;
    /**
     * 每个文件要读取的数据量
     */
    public int num;
    /**
     * 顺序还是逆序读取
     */
    public boolean reverse;

    public ReadRule(Queue<String> names, int num, boolean reverse) {
        this.names = names;
        this.num = num;
        this.reverse = reverse;
    }

    public ReadRule(Queue<String> names, int num) {
        this.names = names;
        this.num = num;
        this.reverse = true;
    }


    public abstract void read();

    public Queue<String> getNames() {
        return names;
    }


    /**
     * 返回在每个划分文件中，要读取对象的数量
     *
     * @return 对象的数量
     */
    public int getNum() {
        return num;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setNames(Queue<String> names) {
        this.names = names;
    }

    public void clearNames(){
        names.clear();
    }

    public void addName(String name){
        names.offer(name);
    }
}
