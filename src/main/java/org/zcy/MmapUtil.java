package org.zcy;

import com.alibaba.fastjson.JSONObject;
import org.zcy.read.rule.ReadRule;
import org.zcy.write.rule.SplitRule;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import sun.nio.ch.FileChannelImpl;


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
    private Queue<T> queue;

    private Map<String, FileChannel> channels;


    public MmapUtil(String directory, long bufferSize, String fileType) {
        this.directory = directory;
        this.bufferSize = bufferSize;
        this.fileType = fileType;
        queue = new ConcurrentLinkedQueue<>();
        channels = new ConcurrentHashMap<>();
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
        queue.clear();
    }

    /**
     * 内存映射写入文件
     *
     * @param rule 文件划分规则
     * @throws Exception 当无法将对象写入磁盘中时报错
     */
    public void writeToFile(SplitRule rule) throws Exception {
        while (!queue.isEmpty()) {    // 当队列中有正在被等待写入磁盘的对象
            T object = queue.poll();
            writeToFile(object, rule);
        }
    }


    public void writeToFile(T object, SplitRule rule) throws Exception {
        initDirectory();    // 检查文件夹是否存在，不存在时创建
        rule.split(object);
        String path = directory + rule.getName() + "." + fileType;
        FileChannel fc = new RandomAccessFile(path, "rw").getChannel();

        String str = JSONObject.toJSONString(object) + ",";
        // buffer 大小不足
        if (bufferSize < str.length())
            throw new Exception("Buffer 大小不足！Object: " + str.length() + " > Buffer: " + bufferSize);
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bufferSize);
        buffer.put(str.getBytes());
        fc.close();
//        Method unmap = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
//        unmap.setAccessible(true);
//        unmap.invoke(FileChannelImpl.class, buffer);
    }


    public List<List<T>> readFromFile(ReadRule rule, Class<T> cls) throws Exception {
        List<List<T>> result = new LinkedList<>();
        rule.read();
        Queue<String> queryNames = rule.getNames();
//        Method unmap = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
//        unmap.setAccessible(true);

        while (!queryNames.isEmpty()) {
            String name = queryNames.poll();
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
            result.add(JSONObject.parseArray(sb.toString(), cls));
            fc.close();
//            unmap.invoke(FileChannelImpl.class, buffer);
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


    public int getChannelMapSize() {
        return channels.size();
    }

    public void clearChannelMap() throws IOException {
        for (FileChannel fc : channels.values()) {
            fc.close();
        }
        channels.clear();
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

