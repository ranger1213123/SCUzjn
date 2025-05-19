package Controller;

import Bean.*;
import Bean.Transaction;

import java.util.*;
//实现一个
public class DataController {
    private static HashMap<Integer, Transaction> transList = new HashMap<>();//记录事务的数量
    private static HashMap<String, Lock> LockList = new HashMap<>();//记录锁的总量
    private static HashMap<Integer, Queue<Request>> RequestList = new HashMap<>();//记录每个锁的请求表

    public static Transaction getTransInfo(int txId){
        if(!transList.containsKey(txId)){
            return null;
        }
        return transList.get(txId);
    }
    //添加数据
    public static void updateTransInfo(int txId, Transaction trans){
        transList.put(txId, trans);
    }
    //获取单个锁数据
    public static Lock getLockInfo(String dataId){
        Lock lock = LockList.get(dataId);
        if (lock == null) {
            lock = new Lock();
            lock.setDataId(dataId);
            LockList.put(dataId, lock);
            System.out.println("  初始化数据项锁：" + dataId);
        }
        return lock;
    }
    //更新锁数据
    public static void  updateLockInfo(String lock, Lock l){
        LockList.put(lock, l);
    }
    //获取请求数据
    public static Queue<Request> getWaitingInfo(int txId){
        if(!RequestList.containsKey(txId)){
            return null;
        }
        return RequestList.get(txId);
    }
    //添加请求数据
    public static void updateWaitingInfo(Queue<Request> r, int transId){
        RequestList.put(transId, r);
    }
    //获取全部锁
    public static Collection<Lock> getAllLocks() {
        return LockList.values();
    }

    public static Map<Integer, Transaction> transMap() {
        return transList;
    }

}
