package org.zcy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

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
     * 文件划分的模式
     * 1. 自定义模式，通过选取对象的域作划分
     * 2. 分钟划分，同一分钟写入的对象会被划分在一个文件中
     * 3. 小时划分，同一小时写入的对象会被划分在一个文件中
     */
    private int splitMode;
    /**
     * 自定义模式下，所要选取作为划分标准的域。
     * 例如 Tick 类中的 code 域，当选取 code 域作划分，所有相同股票代码的 Tick 将会划分在一个文件中
     */
    private String fieldName;
    /**
     * 文件存储的格式，如 csv、txt等
     */
    private String fileType;

    /**
     * 初始化内存映射读写工具包
     *
     * @param directory  文件存储目录
     * @param bufferSize 映射写入和读取每个对象的缓存大小，缓存大小需大于等于对象大小
     * @param splitMode  文件划分的模式
     * @param fieldName  自定义模式下，所要选取作为划分标准的域
     * @param fileType   文件存储的格式
     * @throws Exception 当模式选为自定义模式，但为提供划分定义时报错
     */
    public MmapUtil(String directory, long bufferSize, int splitMode, String fieldName, String fileType) throws Exception {
        // 文件划分定义不存在
        if (splitMode == 1 && (fieldName == null || fieldName.isEmpty())) throw new Exception("文件划分定义不存在");

        this.directory = directory;
        this.bufferSize = bufferSize;
        this.splitMode = splitMode;
        this.fieldName = fieldName;
        this.fileType = fileType;

        initDirectory();
    }

    /**
     * 内存映射写入文件
     *
     * @param object 要写入的对象
     * @throws Exception 当无法将对象写入磁盘中时报错
     */
    public void writeToFile(T object) throws Exception {
        // 检查文件夹是否存在，不存在时创建
        initDirectory();

        // 文件划分定义不存在
        if ((fieldName == null || fieldName.isEmpty()) && splitMode == 1) throw new Exception("文件划分定义不存在");

        // 文件存储名称的选择
        String fileName;
        switch (splitMode) {
            case 1: // 自定义划分
                fileName = object.getClass().getDeclaredField(fieldName).get(object).toString();
                break;
            case 2: // 分钟划分
                fileName = timestampToMinute(System.currentTimeMillis());
                break;
            default:    // 小时划分
                fileName = timestampToHour(System.currentTimeMillis());
        }

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

    /**
     * 将列表中的所有对象通过内存映射写入磁盘
     *
     * @param objects 包含要写入对象的列表
     * @throws Exception 当无法将对象写入磁盘中时报错
     */
    public void writeListToFile(List<T> objects) throws Exception {
        for (int i = 0; i < objects.size(); i++) {
            writeToFile(objects.get(i));
        }
    }

    /**
     * 通过内存映射读取磁盘中的对象
     *
     * @param queryName 文件划分的名称
     * @param num       查询的对象数量
     * @param reverse   是否从最新的内容读取
     * @param cls       读取对象的类别
     * @return 包含所有读取到的对象的列表
     * @throws IOException 文件不存在或读取不成功时报错
     */
    public List<T> readFromFile(String queryName, int num, boolean reverse, Class<T> cls) throws IOException {
        String path = directory + queryName + "." + fileType;   // 文件路径
        FileChannel fc = new RandomAccessFile(path, "r").getChannel();

        // 读取大小判断，查询大小需小于等于文件大小
        long querySize = bufferSize * num;
        long fileSize = fc.size();
        long size = Math.min(querySize, fileSize);  // 查询大小与文件大小取最小值
        MappedByteBuffer buffer = reverse ?
                fc.map(FileChannel.MapMode.READ_ONLY, fileSize - size, size) :
                fc.map(FileChannel.MapMode.READ_ONLY, 0, size);

        byte[] bytes = new byte[(int) size];
        buffer.get(bytes);
        StringBuffer sb = new StringBuffer("[");
        String content = new String(bytes);
        sb.append(content);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return JSONObject.parseArray(sb.toString(), cls);
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
     * 设置文件划分模式
     *
     * @param splitMode 文件划分模式
     */
    public void setSplitMode(int splitMode) {
        this.splitMode = splitMode;
    }

    /**
     * 设置自定义划分时所要选取的对象的域
     *
     * @param fieldName 域名
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
     * 将时间戳转换为 RFC3339 格式
     *
     * @param timestamp 时间戳
     * @return RFC3339 格式时间字符串
     */
    private String timestampToRFC3339(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 将时间戳转换为分钟格式
     *
     * @param timestamp 时间戳
     * @return 分钟格式时间字符串
     */
    private String timestampToMinute(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mmZ");
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 将时间戳转换为小时格式
     *
     * @param timestamp 时间戳
     * @return 小时格式时间字符串
     */
    private String timestampToHour(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHZ");
        return simpleDateFormat.format(timestamp);
    }
}

