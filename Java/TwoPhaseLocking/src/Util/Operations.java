package Util;

import Bean.*;
import Controller.DataController;
import Services.Utility;

import java.util.HashSet;

public class Operations {

    //事务开始
    public static void begin(int txId) {
        int ts = Utility.getNextTimeStamp();
        Transaction tx = new Transaction();
        tx.setId(txId);
        tx.setTimeStamp(new java.util.Date(ts * 1000L));
        tx.setTx_status("active");
        tx.setReadList(new HashSet<>());
        tx.setWriteList(new HashSet<>());
        tx.setShrinking(false);

        DataController.updateTransInfo(txId, tx);
        System.out.println("开始事务 T" + txId + "，时间戳为 " + ts);
    }
    //请求读锁
    public static void read(int txId, String itemId) {
        Utility.read_lock(txId, itemId);
    }
    //请求写锁
    public static void write(int txId, String itemId) {
        Utility.write_lock(txId, itemId);
    }
    //最终结果
    public static void end(int txId) {
        // 获取事务对象
        Transaction tx = DataController.getTransInfo(txId);
        if (tx == null) return;

        tx.setTx_status("committed");
        System.out.println("  事务 T" + txId + "已提交");
        System.out.println();
        HashSet<Lock> released = Utility.releaseAllLocks(txId);
        // 对每一个被释放的锁，尝试唤醒其等待队列中的下一个事务
        for (Lock lock : released) {
            Utility.serveWaiting(lock);
        }
    }
}
