package org.zcy;

import org.zcy.MmapUtil;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        String directory = "D://Tool//";
        long bufferSize = 700L;
        int splitMode = 1;
        String fieldName = "code";
        String fileType = "csv";


        Tick t1 = new Tick("sh000123", System.nanoTime());
        Tick t2 = new Tick("sh000222", System.nanoTime());
        Tick t3 = new Tick("sh000222", System.nanoTime());
        Tick t4 = new Tick("sh000123", System.nanoTime());

        t1.sp2=123;
        t2.sp2=321;
        t3.sp2=999;
        t4.sp2=100;



        try {
            MmapUtil<Tick> mmap = new MmapUtil<>(directory, bufferSize, splitMode, fieldName, fileType);
            mmap.writeToFile(t1);
            mmap.writeToFile(t2);
            mmap.writeToFile(t3);
            mmap.writeToFile(t4);

            List<Tick> list = mmap.readFromFile("sh000123", 4, true, Tick.class);
            for(Tick t: list){
                System.out.println(t);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
