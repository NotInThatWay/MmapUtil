package mmaputil;

import mmaputil.read.rule.FieldSpan;
import mmaputil.read.rule.ReadRule;
import mmaputil.read.rule.SizeSpan;
import mmaputil.write.rule.*;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {


    public static void main(String[] args) throws Exception {
        Random rand = new Random(System.nanoTime());
        ExecutorService executor = Executors.newFixedThreadPool(30);

        int numStk = 100;
        int numTick = 300;
        List<Tick> tickList = new LinkedList<>();
        long start, end;
        String directory = "D://Tool//";
        long bufferSize = 600L;
        String fileType = "csv";


        MmapUtil<Tick> mmap = new MmapUtil<>(directory, bufferSize, fileType);

        SplitRule fieldSplit = new FieldSplit("code");
        SplitRule sizeSplit = new SizeSplit(1);
        SplitRule timeSplit = new TimeSplit("yyyy-MM-dd-HH-mm-ssZ");
        SplitRule singleSplit = new SingleSplit("single");

        ReadRule sizeSpan = new SizeSpan(100, 480, 480);
        ReadRule fieldSpan = new FieldSpan("code");


        CountDownLatch countDownLatch = new CountDownLatch(numStk * numTick);
        start = System.nanoTime();
        for (int i = 0; i < numTick; i++) {
            for(int j = 0; j < numStk; j++){
                String code = "sh" + String.format("%06d", j);
                Tick tick = new Tick(code, System.currentTimeMillis());
                tick.sp1 = rand.nextInt(1000);
                executor.execute(() -> {
                    try {
                        mmap.writeToFile(tick, timeSplit);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                countDownLatch.countDown();
                });
            }
        }

        countDownLatch.await();
        end = System.nanoTime();
        System.out.println(end - start);



//
//        CountDownLatch countDownLatch = new CountDownLatch(numStk);
//        start = System.nanoTime();
//        for(int i = 0; i < numStk; i++) {
//            String code = "sh" + String.format("%06d", i);
//            executor.execute(() -> {
//                try {
//                    ReadRule rule = new FieldSpan(code);
//                    List<List<Tick>> ticks = mmap.readFromFile(rule, Tick.class);
//                    while(ticks.isEmpty()){
//                        ticks = mmap.readFromFile(rule, Tick.class);
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                countDownLatch.countDown();
//            });
//        }
//        countDownLatch.await();
//        end = System.nanoTime();
//        System.out.println(end - start);
    }
}
