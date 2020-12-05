package com.ayuan.staging.common.util.autoid;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * @author sYuan
 * @Description 分布式自增长主键ID：64位ID(42[毫秒]+5[机器ID]+5[业务编码]+12[重复累加])
 */
public class AutoIdHelper {

    /**
     * 时间启示标记点，作为基准，一般取系统的最近时间(一旦确定不能变动)
     */
    private final static long twepoch = 1288834974657L;

    /**
     * 机器标识位数
     */
    private final static long workerIdBits = 5L;

    /**
     * 机器ID最大值
     */
    private final static long maxWorkerId = -1L * (-1L << workerIdBits);

    /**
     * 数据中心标识位数
     */
    private final static long dataCenterIdBits = 5L;

    /**
     * 数据中心Id最大值
     */
    private final static long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 毫秒内自增位
     */
    private final static long sequenceBits = 12L;

    /**
     * 机器ID偏左移12位
     */
    private final static long workerIdShift = sequenceBits;

    /**
     * 数据中心ID左移17位
     */
    private final static long dataCenterShift = sequenceBits + workerIdBits;

    /**
     * 时间毫秒左移22位
     */
    private final static long timeStampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

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
        this.dataCenterId = getDataCenterId(maxDataCenterId);
        this.workerId = getMaxWorkerId(dataCenterId, maxWorkerId);
    }

    /**
     * @param workerId     工作机器id
     * @param dataCenterId 序列号
     */
    public AutoIdHelper(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id cann't be  be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter id cann't be  be greater than %d or less than 0", maxDataCenterId));
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
        long nextId = ((timeStamp - twepoch)<<timeStampLeftShift)
                |(dataCenterId<<dataCenterShift)
                |(workerId<<workerIdShift)
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
        return (mpid.toString().hashCode() & 0xff) % (maxWorkerId + 1);
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
