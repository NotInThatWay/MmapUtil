package org.zcy.read.rule;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FieldSpan extends ReadRule{

    private String fieldName;

    public FieldSpan(String fieldName, int num) {
        super(new ConcurrentLinkedQueue<>(), num);
        this.fieldName = fieldName;
    }
    @Override
    public void read() {
        names.offer(fieldName);
    }

    public String getFieldName(){
        return fieldName;
    }

    public void setFieldName(String fieldName){
        this.fieldName = fieldName;
    }
}
