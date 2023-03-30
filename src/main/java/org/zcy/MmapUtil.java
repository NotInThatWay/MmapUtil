package org.zcy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.fastjson.JSONObject;
import org.zcy.read.rule.ReadRule;
import org.zcy.write.rule.SplitRule;

public class MmapUtil<T> {
    /**
     * 文件存储目录
     */
    private String directory;
    /**
     * 映射写入和读取每个对象的缓存大小，缓存大小需大于等于对象大小
     */
    private long bufferSize;

    /**
     * 文件存储的格式，如 csv、txt等
     */
    private String fileType;

    /**
     * 存储要写入磁盘的对象的队列
     */
    private BlockingQueue<T> queue;


    public MmapUtil(String directory, long bufferSize, String fileType) throws Exception {
        this.directory = directory;
        this.bufferSize = bufferSize;
        this.fileType = fileType;
        queue = new LinkedBlockingQueue<>();
        initDirectory();
    }

    /**
     * 将对象载入队列
     *
     * @param object 要写入磁盘的对象
     */
    public void loadData(T object) {
        queue.offer(object);
    }

    /**
     * 将多个对象载入队列
     *
     * @param objects 装有多个对象的列表
     */
    public void loadData(List<T> objects) {
        for (int i = 0; i < objects.size(); i++) {
            queue.offer(objects.get(i));
        }
    }

    /**
     * 清空队列
     */
    public void clearData() {
        queue = new LinkedBlockingQueue<>();
    }

    /**
     * 内存映射写入文件
     *
     * @param rule 文件划分规则
     * @throws Exception 当无法将对象写入磁盘中时报错
     */
    public void writeToFile(SplitRule rule) throws Exception {
        while (!queue.isEmpty()) {    // 当队列中有正在被等待写入磁盘的对象
            // 检查文件夹是否存在，不存在时创建
            initDirectory();
            T object = queue.poll();
            String fileName = rule.getName(object);
            String path = directory + fileName + "." + fileType;
            FileChannel fc = new RandomAccessFile(path, "rw").getChannel();
            String str = JSONObject.toJSONString(object) + ",";
            // buffer 大小不足
            if (bufferSize < str.length())
                throw new Exception("Buffer 大小不足！Object: " + str.length() + " > Buffer: " + bufferSize);

            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bufferSize);
            buffer.put(str.getBytes());

            fc.close();
        }
    }


    //    /**
//     * 通过内存映射读取磁盘中的对象
//     *
//     * @param queryName 文件划分的名称
//     * @param num       查询的对象数量
//     * @param cls       读取对象的类别
//     * @return 包含所有读取到的对象的列表
//     * @throws IOException 文件不存在或读取不成功时报错
//     */
    public Map<String, List<T>> readFromFile(ReadRule rule, Class<T> cls) throws IOException {
        Map<String, List<T>> result = new ConcurrentHashMap<>();
        List<String> queryNames = rule.getNames();
        for (int i = 0; i < queryNames.size(); i++) {
            String name = queryNames.get(i);
            String path = directory + name + "." + fileType;   // 文件路径
            if (!new File(path).exists()) continue;
            FileChannel fc = new RandomAccessFile(path, "r").getChannel();

            // 读取大小判断，查询大小需小于等于文件大小
            long querySize = bufferSize * rule.getNum();
            long fileSize = fc.size();
            long size = Math.min(querySize, fileSize);  // 查询大小与文件大小取最小值
            MappedByteBuffer buffer = rule.isReverse() ?
                    fc.map(FileChannel.MapMode.READ_ONLY, fileSize - size, size) :
                    fc.map(FileChannel.MapMode.READ_ONLY, 0, size);

            byte[] bytes = new byte[(int) size];
            buffer.get(bytes);
            StringBuffer sb = new StringBuffer("[");
            String content = new String(bytes);
            sb.append(content);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            result.put(name, JSONObject.parseArray(sb.toString(), cls));
        }
        return result;
    }

    /**
     * 初始化目录
     */
    private void initDirectory() {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 设置目录
     *
     * @param directory 目录路径
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 设置缓存大小
     *
     * @param bufferSize 缓存大小
     */
    public void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * 设置文件存储的格式
     *
     * @param fileType 文件存储的格式
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


}

