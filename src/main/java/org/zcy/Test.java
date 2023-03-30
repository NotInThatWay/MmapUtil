package org.zcy;

import org.zcy.read.rule.SizeSpan;
import org.zcy.write.rule.FieldSplit;
import org.zcy.write.rule.SingleSplit;
import org.zcy.write.rule.SizeSplit;
import org.zcy.write.rule.TimeSplit;

public class Test {
    public static void main(String[] args) {
        String directory = "D://Tool//";
        long bufferSize = 700L;
        String fileType = "csv";

        Tick t1, t2, t3, t4;
        t1 = new Tick("sh000123", System.nanoTime());
        t2 = new Tick("sh000222", System.nanoTime());
        t3 = new Tick("sh000222", System.nanoTime());
        t4 = new Tick("sh000123", System.nanoTime());
        t1.sp2 = 123;
        t2.sp2 = 321;
        t3.sp2 = 11199;
        t4.sp2 = 100;


        try {
            // 建立划分规则
            SizeSplit sizeSplit = new SizeSplit(1);
            TimeSplit timeSplit = new TimeSplit(2);
            SingleSplit singleSplit = new SingleSplit("single");
            FieldSplit fieldSplit = new FieldSplit("code");


            SizeSpan sizeSpan = new SizeSpan(1, 400, 500);


            MmapUtil<Tick> mmap = new MmapUtil<>(directory, bufferSize, fileType);
            mmap.loadData(t1);
            mmap.loadData(t2);
            mmap.loadData(t3);
            mmap.loadData(t4);

            long start = System.nanoTime();
            mmap.writeToFile(sizeSplit);
            long end = System.nanoTime();
            System.out.println(end - start);


            mmap.readFromFile(sizeSpan, Tick.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
