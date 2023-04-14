package mmaputil;

import com.alibaba.fastjson.JSONObject;
import mmaputil.read.rule.ReadRule;
import mmaputil.write.rule.SplitRule;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


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

    /**
     * 工具类构造函数
     *
     * @param directory  文件存储路径
     * @param bufferSize 缓存大小
     * @param fileType   文件存储格式
     */
    public MmapUtil(String directory, long bufferSize, String fileType) {
        this.directory = directory;
        this.bufferSize = bufferSize;
        this.fileType = fileType;
        queue = new ConcurrentLinkedQueue<>();
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
     * 内存映射写入队列中的文件
     *
     * @param rule 文件划分规则
     * @throws Exception 无法将对象写入磁盘中时报错
     */
    public void writeToFile(SplitRule rule) throws Exception {
        while (!queue.isEmpty()) {    // 当队列中有正在被等待写入磁盘的对象
            T object = queue.poll();
            writeToFile(object, rule);
        }
    }

    /**
     * 根据定义的写入规则，将单个对象写入磁盘文件
     *
     * @param object 要写入的对象
     * @param rule   自定义的写入划分规则
     * @throws Exception 无法将对象写入磁盘中时报错
     */
    public synchronized void writeToFile(T object, SplitRule rule) throws Exception {
        initDirectory();    // 检查文件夹是否存在，不存在时创建
        rule.split(object); // 对当前对象进行划分
        String path = directory + rule.getName() + "." + fileType;
        try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
            FileChannel fc = file.getChannel();
            String str = JSONObject.toJSONString(object) + ",";
            // Buffer 大小不足
            if (bufferSize < str.length())
                throw new Exception("Buffer 大小不足！对象: " + str.length() + " > Buffer: " + bufferSize);

            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bufferSize);
            buffer.put(str.getBytes());
            buffer.force(); // 刷盘
            fc.close();
        }
    }

    /**
     * 从磁盘目录的所有文件中，读取符合条件的对象
     *
     * @param rule 自定义的读取规则
     * @param cls  读取对象的类别
     * @return 包含所有读取对象的列表
     * @throws Exception 无法读取到对象
     */
    public List<List<T>> readFromFile(ReadRule rule, Class<T> cls) throws Exception {
        List<List<T>> result = new LinkedList<>();
        Queue<String> queryNames = rule.getNames();

        while (!queryNames.isEmpty()) {
            String name = queryNames.poll();
            String path = directory + name + "." + fileType;   // 文件路径
            if (!new File(path).exists()) continue;

            result.add(readFromFile(path, rule, cls));
        }
        return result;
    }

    /**
     * 从磁盘的规定文件中，读取所有符合条件的对象
     *
     * @param fileName 要读取的文件名称
     * @param rule     自定义的读取规则
     * @param cls      读取对象的类别
     * @return 包含所有读取对象的列表
     * @throws Exception 无法读取到对象
     */
    public List<T> readFromFile(String fileName, ReadRule rule, Class<T> cls) throws Exception {
        String path = directory + fileName + "." + fileType;   // 文件路径
        if (!new File(path).exists()) throw new Exception("文件不存在");
        List<T> result;
        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            FileChannel fc = file.getChannel();
            // 读取大小判断，查询大小需小于等于文件大小
            int count = rule.getCount();
            int index = rule.getIndex();
            long fileSize = fc.size();
            long size, start;

            if (Math.abs(index) * bufferSize > fileSize) return new ArrayList<>();

            if (count >= 0) {    // 向后数
                if (index >= 0) {    // 从最早数据开始看
                    start = Math.min(fileSize, index * bufferSize);
                } else {    // 从最新的数据开始看
                    start = Math.max(0, fileSize + index * bufferSize);
                }
                size = Math.min(fileSize - start, count * bufferSize);
            } else {    // 向前数
                if (index >= 0) { // 从最早数据开始看
                    start = Math.max(0, (index + count) * bufferSize);
                    size = Math.min(-count, index) * bufferSize;
                } else { // 从最新的数据开始看
                    start = Math.max(0, fileSize + (index + count + 1) * bufferSize);
                    size = Math.min(fileSize - start, -count * bufferSize);
                }
            }

            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, start, size);

            byte[] bytes = new byte[(int) size];
            buffer.get(bytes);

            // 反序列化
            StringBuffer sb = new StringBuffer("[");
            String content = new String(bytes);
            sb.append(content);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");

            result = JSONObject.parseArray(sb.toString(), cls);
            fc.close();
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

    /**
     * 更新要写入磁盘的对象的队列
     *
     * @param queue 新的对象队列
     */
    public void setQueue(Queue<T> queue) {
        this.queue = queue;
    }

    /**
     * 获取要写入磁盘的对象的队列
     *
     * @return 对象的队列
     */
    public Queue<T> getQueue() {
        return queue;
    }

    /**
     * 清空要写入磁盘的对象的队列
     */
    public void clearQueue() {
        queue.clear();
    }
}

