package org.zcy.read.rule;

import java.util.LinkedList;
import java.util.List;

public class SizeSpan extends ReadRule {

    public SizeSpan(int interval, int lowerBound, int upperBound, int num) {
        super(new LinkedList<>(), num, true);
        setNames(interval, lowerBound, upperBound);
    }

    public SizeSpan(int interval, int lowerBound, int upperBound) {
        super(new LinkedList<>(), Integer.MAX_VALUE, true);
        setNames(interval, lowerBound, upperBound);
    }


    public void setNames(int interval, int lowerBound, int upperBound) {
        List<String> result = new LinkedList<>();
        for (int i = lowerBound; i <= upperBound; i += interval) {
            result.add(Integer.toString(i));
        }
        names = result;
    }

    public void setNum(int num) {
        super.num = num;
    }

    @Override
    public List<String> getNames() {
        return names;
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public boolean isReverse() {
        return reverse;
    }
}
