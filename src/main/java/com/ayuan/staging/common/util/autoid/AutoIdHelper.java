package com.ayuan.staging.common.util.autoid;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author sYuan
 * @Description 分布式自增长主键ID：64位ID(42[毫秒]+5[机器ID]+5[业务编码]+12[重复累加])
 * @Remark  雪花id的弊端：
 *          生成id不依赖数据库和内存，而是依赖服务器的系统时间，
 *          如果服务器的系统时间出现问题，会影响id的生成，
 *          生成id之后，id无法随数据库时间的修复而修复
 */
public class AutoIdHelper {

    /**
     * 时间启示标记点，作为基准，一般取系统的最近时间(一旦确定不能变动)
     */
    private final static long TWEPOCH = 1288834974657L;

    /**
     * 机器标识位数
     */
    private final static long WORKER_ID_BITS = 5L;

    /**
     * 机器ID最大值
     */
    private final static long MAX_WORKER_ID = -1L * (-1L << WORKER_ID_BITS);

    /**
     * 数据中心标识位数
     */
    private final static long DATA_CENTER_ID_BITS = 5L;

    /**
     * 数据中心Id最大值
     * -1L ^ (-1L << DATA_CENTER_ID_BITS)
     */
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 毫秒内自增位
     */
    private final static long SEQUENCE_BITS = 12L;

    /**
     * 机器ID偏左移12位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移17位
     */
    private final static long DATA_CENTER_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间毫秒左移22位
     */
    private final static long TIME_STAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 上次生产id时间戳
     */
    private static long lastTimeStamp = -1L;

    /**
     * 并发控制
     */
    private long sequence = 0L;

    private final long workerId;

    private final long dataCenterId;


    public AutoIdHelper() {
        this.dataCenterId = getDataCenterId(MAX_DATA_CENTER_ID);
        this.workerId = getMaxWorkerId(dataCenterId, MAX_WORKER_ID);
    }

    /**
     * @param workerId     工作机器id
     * @param dataCenterId 序列号
     */
    public AutoIdHelper(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id cann't be  be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter id cann't be  be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }


    /**
     * 获取下一个id
     * @return
     */
    public synchronized long nextId() {
        long timeStamp = timeGen();
        if (timeStamp < lastTimeStamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimeStamp - timeStamp));
        }
        if (lastTimeStamp == timeStamp) {
            // 当前毫秒内，则加1
            sequence = (sequence + 1) & sequence;
            if(sequence == 0){
                // 当前毫秒计数满了，则等待下一秒
                timeStamp = tilNextMills(lastTimeStamp);
            }
        }else {
            sequence = 0L;
        }
        lastTimeStamp = timeStamp;

        // id偏移组合生成最终id，并返回
        long nextId = ((timeStamp - TWEPOCH)<<TIME_STAMP_LEFT_SHIFT)
                |(dataCenterId<<DATA_CENTER_SHIFT)
                |(workerId<<WORKER_ID_SHIFT)
                |sequence;
        return nextId;
    }

    private long tilNextMills(long lastTimeStamp) {
        long timeStamp = this.timeGen();
        while (timeStamp <= lastTimeStamp){
            timeStamp = this.timeGen();
        }

        return timeStamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    protected static long getMaxWorkerId(long dataCenterId, long maxDataCenterId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name.isEmpty()) {
            /*
             * GET JvmId
             */
            mpid.append(name.split("@")[0]);
        }

        /*
         * MAC + PID 的 hashCode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xff) % (MAX_WORKER_ID + 1);
    }

    protected static long getDataCenterId(long maxDataCenterId) {
        long id = 0L;

        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0X000000FF & (long) mac[mac.length - 1])
                        | (0X0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDataCenterId + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
