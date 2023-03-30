package org.zcy;

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
        t3.sp2 = 999;
        t4.sp2 = 100;


        try {
            // 建立划分规则
            SizeSplit sizeSplit = new SizeSplit(10);
            TimeSplit timeSplit = new TimeSplit(2);
            SingleSplit singleSplit = new SingleSplit("single");
            FieldSplit fieldSplit = new FieldSplit("code");


            MmapUtil<Tick> mmap = new MmapUtil<>(directory, bufferSize, fileType);
            mmap.loadData(t1);
            mmap.loadData(t2);
            mmap.loadData(t3);
            mmap.loadData(t4);

            long start = System.nanoTime();
            mmap.writeToFile(fieldSplit);
            long end = System.nanoTime();
            System.out.println(end - start);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
